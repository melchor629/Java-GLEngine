package org.melchor629.engine.gui;

import org.lwjgl.nanovg.NVGLUFramebuffer;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;
import org.melchor629.engine.Erasable;
import org.melchor629.engine.Game;
import org.melchor629.engine.gl.*;
import org.melchor629.engine.input.Keyboard;
import org.melchor629.engine.input.Mouse;
import org.melchor629.engine.utils.IOUtils;
import org.melchor629.engine.utils.logger.Logger;
import org.melchor629.engine.window.Window;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

/**
 * Contains the wrapper to create any GUI and lets you create objects
 * to represent on the screen
 */
public class GUI implements Erasable {
    private static final Logger LOGGER = Logger.getLogger(GUI.class);
    static GUI gui;

    private final FrameBuffer renderFBO;
    private final Texture renderTexture;
    private final ShaderProgram guiShader;
    private final VertexArrayObject guiVao;
    private final GLContext gl;
    private NVGLUFramebuffer nvgFB;

    private int width, height;
    private float scaleFactor;
    private float[] clearColor = new float[4];
    private boolean hidegui = false, hideguic = false;
    private String defaultFont;
    private Map<String, Integer> fontsMap;
    private List<CodeBlock> onDrawOnceCallableFunctions;
    private Map<Long, CodeBlock> onDrawTimeoutFunctions;

    final long nvgCtx;
    boolean isRenderingGUI = false, dirty = true;

    public final Container rootView;

    private boolean clicked = false;

    public GUI(Game game, GLContext gl, Window window) {
        if(gui != null) throw new IllegalStateException("Cannot create more than one GUI");
        gui = this;
        this.gl = gl;
        this.width = window.getFramebufferSize().width;
        this.height = window.getFramebufferSize().height;
        this.scaleFactor = (float) window.getPixelScaleFactor();

        //Creating Plane on Buffer Object
        BufferObject plane = gl.createBufferObject(GLContext.BufferTarget.ARRAY_BUFFER, GLContext.BufferUsage.STATIC_DRAW);
        plane.fillBuffer(new float[] {
              // x,  y, u, v
                 1, -1, 0, 0,
                 1,  1, 1, 0,
                -1,  1, 1, 1,
                -1, -1, 0, 1,
                 1, -1, 0, 0,
                -1,  1, 1, 1
        });

        //Create Render Buffer for depth and stencil
        RenderBuffer stencilDepthRenderBuffer = gl.createRenderBuffer(GLContext.TextureFormat.DEPTH24_STENCIL8, width, height);

        //Create Texture for color
        renderTexture = gl.createTexture(GLContext.TextureFormat.RGBA, width, height, GLContext.TextureExternalFormat.RGBA);

        //Create Frame Buffer
        renderFBO = gl.createFrameBuffer();
        renderFBO.attachColorTexture(renderTexture, 0);
        renderFBO.attachDepthStencilRenderbuffer(stencilDepthRenderBuffer);
        renderFBO.unbind();

        //Load and create shader
        try {
            guiShader = gl.createShader(
                    IOUtils.readStream(IOUtils.getResourceAsStream("shaders/gui/gui.vs.glsl")),
                    IOUtils.readStream(IOUtils.getResourceAsStream("shaders/gui/gui.fs.glsl"))
            );
        } catch(IOException io) {
            LOGGER.throwable("Could not load GUI shaders", io);
            throw new RuntimeException("GUI Shaders must be in shaders/gui/ inside the jar", io);
        }

        //Bind attributes to VAO
        guiVao = gl.createVertexArrayObject();
        guiShader.setColorOutput("color", 0);
        guiShader.setUniform("tex1", 0);
        guiShader.setUniform("tex2", 1);
        guiVao.bind();
        guiShader.bind();
        plane.bind();
        guiShader.vertexAttribPointer("position", 2, GLContext.type.FLOAT, false, 4 * 4, 0);
        guiShader.vertexAttribPointer("tex_coord", 2, GLContext.type.FLOAT, false, 4 * 4, 2 * 4);
        guiShader.enableAttrib("position");
        guiShader.enableAttrib("tex_coord");
        plane.unbind();
        guiShader.unbind();
        guiVao.unbind();

        //Create NanoVG Context
        nvgCtx = NanoVGGL3.nvgCreateGL3(NanoVGGL3.NVG_ANTIALIAS | NanoVGGL3.NVG_STENCIL_STROKES | NanoVGGL3.NVG_DEBUG);
        nvgFB = NanoVGGL3.nvgluCreateFramebuffer(nvgCtx, width, height, 0);

        rootView = new Container(new Frame(0, 0, window.getWindowSize().width, window.getWindowSize().height));

        //Listen for mouse events
        window.getMouseController().addListener((Mouse.OnMouseClickEvent) mouse -> {
            clicked = !clicked;
            if(clicked) {
                rootView.onMouseDown(new MouseEvent(mouse, mouse.getMousePosition()));
            } else {
                rootView.onMouseUp(new MouseEvent(mouse, mouse.getMousePosition()));
            }
        });

        window.getMouseController().addListener((Mouse.OnMouseMoveEvent) (mouse, delta) ->
            rootView.onMouseMove(new MouseEvent(mouse, mouse.getMousePosition()))
        );

        window.getMouseController().addListener((Mouse.OnWheelMoveEvent) mouse ->
            rootView.onWheelMove(new MouseEvent(mouse, mouse.getMousePosition()))
        );

        //Listen for keyboard events
        window.getKeyboardController().addListener((Keyboard.OnPressKeyEvent) rootView::onKeyDown);
        window.getKeyboardController().addListener((Keyboard.OnReleaseKeyEvent) rootView::onKeyUp);
        window.getKeyboardController().addListener(rootView::onCharKey);

        //Listen for resize event
        window.addResizeEventListener((newWidth, newHeight) ->
            game.post(() -> {
                width = window.getFramebufferSize().width;
                height = window.getFramebufferSize().height;
                renderTexture.resize(width, height);
                stencilDepthRenderBuffer.resize(width, height);
                NanoVGGL3.nvgluDeleteFramebuffer(nvgCtx, nvgFB);
                nvgFB = NanoVGGL3.nvgluCreateFramebuffer(nvgCtx, width, height, 0);
                dirty = true;
            })
        );

        fontsMap = new TreeMap<>();
        onDrawOnceCallableFunctions = new ArrayList<>();
        onDrawTimeoutFunctions = new TreeMap<>();
    }

    /**
     * Wraps the code block for rendering all 3D objects to mix with the
     * GUI after both are rendered.
     * @param render Render code block to call
     */
    public void render(RenderBlock render) {
        hidegui = hideguic;
        if(!hidegui) {
            renderFBO.bind();
            render.render();
            renderFBO.unbind();
        } else {
            render.render();
        }
    }

    /**
     * Wraps the GUI rendering code block to after render it, mix it with
     * the 3D render and produce the output to the screen. Some of the
     * OpenGL status may be changed.
     * <br>
     * List of states that could be changed:
     * <ul>
     *     <li>glCullFace(GL_BACK)</li>
     *     <li>glFrontFace(GL_CCW)</li>
     *     <li>glDisable(GL_SCISSOR_TEST)</li>
     *     <li>glColorMask(true, true, true, true)</li>
     *     <li>glStencilMask(0xffffffff)</li>
     *     <li>glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP)</li>
     *     <li>glStencilFunc(GL_ALWAYS, 0, 0xffffffff)</li>
     * </ul>
     * @param renderFunction extra GUI rendering code block
     */
    public void gui(Consumer<Long> renderFunction) {
        if(!hidegui) {
            //Save some stuff
            gl.getFloat(GLContext.GLGet.COLOR_CLEAR_VALUE, clearColor);
            boolean blendEnabled = gl.isEnabled(GLContext.GLEnable.BLEND);
            int blendSrc = gl.getInt(GLContext.GLGet.BLEND_SRC_RGB);
            int blendDst = gl.getInt(GLContext.GLGet.BLEND_DST_RGB);
            boolean cullFaceEnabled = gl.isEnabled(GLContext.GLEnable.CULL_FACE);
            boolean depthTestEnabled = gl.isEnabled(GLContext.GLEnable.DEPTH_TEST);
            boolean stencilTestEnabled = gl.isEnabled(GLContext.GLEnable.STENCIL_TEST);

            //Render GUI
            List<CodeBlock> list = new ArrayList<>(onDrawOnceCallableFunctions);
            onDrawOnceCallableFunctions.clear();
            list.forEach(CodeBlock::invoke);

            new TreeSet<>(onDrawTimeoutFunctions.keySet()).stream()
                    .filter(time -> time <= System.currentTimeMillis())
                    .forEach(time -> {
                        onDrawTimeoutFunctions.get(time).invoke();
                        onDrawTimeoutFunctions.remove(time);
                    });

            if(dirty) {
                NanoVGGL3.nvgluBindFramebuffer(nvgCtx, nvgFB);
                gl.clearColor(0, 0, 0, 0);
                gl.clear(GLContext.COLOR_CLEAR_BIT | GLContext.STENCIL_BUFFER_BIT);
                NanoVG.nvgBeginFrame(nvgCtx, width, height, scaleFactor);
                isRenderingGUI = true;

                rootView.draw();
                dirty = false;
                renderFunction.accept(nvgCtx);

                isRenderingGUI = false;
                NanoVG.nvgEndFrame(nvgCtx);
                NanoVGGL3.nvgluBindFramebuffer(nvgCtx, null);
            }

            //Ready to render the mix
            gl.enable(GLContext.GLEnable.BLEND);
            gl.enable(GLContext.GLEnable.CULL_FACE);
            gl.blendFunc(GLContext.BlendOption.SRC_ALPHA, GLContext.BlendOption.ONE_MINUS_SRC_ALPHA);
            gl.disable(GLContext.GLEnable.DEPTH_TEST);
            gl.clearColor(clearColor[0], clearColor[1], clearColor[2], clearColor[3]);
            gl.clear(GLContext.COLOR_CLEAR_BIT);

            guiVao.bind();
            guiShader.bind();
            gl.setActiveTexture(0);
            gl.bindTexture(GLContext.TextureTarget.TEXTURE_2D, nvgFB.texture());
            gl.setActiveTexture(1);
            renderTexture.bind();
            gl.drawArrays(GLContext.DrawMode.TRIANGLES, 0, 6);
            guiShader.unbind();
            guiVao.unbind();

            //Back to the "original" OpenGL state
            gl.bindTexture(GLContext.TextureTarget.TEXTURE_2D, 0);
            gl.setActiveTexture(0);
            gl.bindTexture(GLContext.TextureTarget.TEXTURE_2D, 0);
            if(blendEnabled) {
                gl.blendFunc(GLContext.valueOf(GLContext.BlendOption.class, blendSrc), GLContext.valueOf(GLContext.BlendOption.class, blendDst));
            } else {
                gl.disable(GLContext.GLEnable.BLEND);
            }

            if(!cullFaceEnabled) gl.disable(GLContext.GLEnable.CULL_FACE);
            if(depthTestEnabled) gl.enable(GLContext.GLEnable.DEPTH_TEST);
            if(stencilTestEnabled) gl.enable(GLContext.GLEnable.STENCIL_TEST); else gl.disable(GLContext.GLEnable.STENCIL_TEST);
        }
        hidegui = hideguic;
    }

    /**
     * Hides GUI
     */
    public void hide() {
        hideguic = true;
    }

    /**
     * Shows GUI
     */
    public void show() {
        hideguic = false;
    }

    /**
     * Shows/Hide GUI
     */
    public void toggleShow() {
        hideguic = !hideguic;
    }

    /**
     * Loads a font from the disk and gives a {@code name} to it. Only
     * supports {@code ttf} fonts. When this method is called, if there's
     * only one loaded font (the one loaded in that moment) then this become
     * the default font.
     * @param name a name for the font
     * @param path path to the font file
     * @throws FileNotFoundException if file doesn't exist
     * @throws RuntimeException if something else bad happens
     * @see #setDefaultFont(String) Change default font
     */
    public void loadFont(String name, File path) throws FileNotFoundException {
        if(!path.getName().endsWith(".ttf")) throw new IllegalArgumentException("File must be a TTF Font");
        if(!path.exists()) throw new FileNotFoundException("File must exist");
        int handle = NanoVG.nvgCreateFont(nvgCtx, name, path.getAbsolutePath());
        if(handle == -1) throw new RuntimeException("Could not load TTF Font");
        fontsMap.put(name, handle);
        if(fontsMap.size() == 1) defaultFont = name;
    }

    /**
     * Loads a font from the System fonts locations with file name
     * {@code fileName} (<i>without extension</i>) and gives a name
     * to it. Only supports {@code ttf} fonts. When this method is called, if there's
     * only one loaded font (the one loaded in that moment) then this become
     * the default font.
     * @param name a name for the font
     * @param fileName file name of the font
     * @throws FileNotFoundException if file doesn't exist
     * @throws IllegalArgumentException if file extension is not a ttf
     * @throws RuntimeException if something else bad happens
     * @see #setDefaultFont(String) Change default font
     */
    public void loadSystemFont(String name, String fileName) throws FileNotFoundException {
        File ttf;
        if(org.lwjgl.system.Platform.get() == org.lwjgl.system.Platform.MACOSX) {
            ttf = new File(System.getenv("HOME") + "/Library/Fonts", fileName + ".ttf");
            if(!ttf.exists()) {
                ttf = new File("/Library/Fonts", fileName + ".ttf");
                if(!ttf.exists()) {
                    ttf = new File("/System/Library/Fonts", fileName + ".ttf");
                    if(!ttf.exists()) {
                        throw new FileNotFoundException("Cannot find " + fileName + ".ttf");
                    }
                }
            }
        } else if(org.lwjgl.system.Platform.get() == org.lwjgl.system.Platform.LINUX) {
            ttf = new File(System.getenv("HOME") + "/.fonts/truetype", fileName + ".ttf");
            if(!ttf.exists()) {
                ttf = new File("/usr/local/share/fonts/truetype", fileName + ".ttf");
                if(!ttf.exists()) {
                    ttf = new File("/usr/share/fonts/truetype", fileName + ".ttf");
                    if(!ttf.exists()) {
                        throw new FileNotFoundException("Cannot find " + fileName + ".ttf");
                    }
                }
            }
        } else if(org.lwjgl.system.Platform.get() == org.lwjgl.system.Platform.WINDOWS) {
            ttf = new File("\\Windows\\Fonts", fileName + ".ttf");
            if(!ttf.exists()) {
                throw new FileNotFoundException("Cannot find " + fileName + ".ttf");
            }
        } else {
            throw new RuntimeException("Platform not supported");
        }
        int handle = NanoVG.nvgCreateFont(nvgCtx, name, ttf.getAbsolutePath());
        if(handle == -1) throw new RuntimeException("Could not load TTF Font");
        fontsMap.put(name, handle);
        if(fontsMap.size() == 1) defaultFont = name;
    }

    /**
     * Sets the current font for drawing text to {@code name} font and
     * its font size in px (DPI independent).
     * If the font doesn't exist, does nothing.
     * @param name name of the font, or {@code null} for default font
     * @param size size in px of the font
     */
    public void setFont(String name, float size) {
        Integer id = fontsMap.get(name != null ? name : defaultFont);
        if(id == null) throw new NoSuchElementException("Font named by " + name + " is not loaded");
        NanoVG.nvgFontFaceId(nvgCtx, id);
        NanoVG.nvgFontSize(nvgCtx, size);
    }

    /**
     * Sets the current font for drawing text to {@code name} font.
     * If the font doesn't exist, does nothing.
     * @param name name of the font, or {@code null} for default font
     */
    public void setFont(String name) {
        Integer id = fontsMap.get(name != null ? name : defaultFont);
        if(id == null) throw new NoSuchElementException("Font named by " + name + " is not loaded");
        NanoVG.nvgFontFaceId(nvgCtx, id);
    }

    /**
     * Changes the default font to another. The first font
     * loaded becomes the default if a default font is not
     * specified.
     * @param name name of the new default font
     */
    public void setDefaultFont(String name) {
        if(fontsMap.containsKey(name)) {
            defaultFont = name;
        } else {
            throw new NoSuchElementException("Font named by " + name + " is not loaded");
        }
    }

    /**
     * Changes the font size in px (DPI independent).
     * @param size size in px of the font
     */
    public void setFontSize(int size) {
        NanoVG.nvgFontSize(nvgCtx, size);
    }

    /**
     * Returns true if the font named {@code name} is loaded or false
     * if it's not.
     * @param name font name to check
     * @return true if it's loaded, false otherwise
     */
    public boolean isFontLoaded(String name) {
        return fontsMap.containsKey(name);
    }

    /**
     * Draws a text on the position. The position
     * is on the bottom right corner of the <i>text box</i>.
     * @param x X position
     * @param y Y position
     * @param text text to draw
     */
    public void drawText(float x, float y, String text) {
        throwIfNoGui();
        NanoVG.nvgText(nvgCtx, x, y, text, 0);
    }

    /**
     * Draws a formatted text on the position. The position
     * is on the bottom right corner of the <i>text box</i>
     * @param x X position
     * @param y Y position
     * @param fmt format text to draw
     * @param objects objects for the formatted text
     */
    public void drawText(float x, float y, String fmt, Object... objects) {
        drawText(x, y, String.format(fmt, objects));
    }

    public void markForRedraw() {
        dirty = true;
    }

    @Override
    public void delete() {
        NanoVGGL3.nvgluDeleteFramebuffer(nvgCtx, nvgFB);
        NanoVGGL3.nvgDeleteGL3(nvgCtx);
    }

    void executeOnce(CodeBlock block) {
        onDrawOnceCallableFunctions.add(block);
    }

    void executeOnceDelayed(long milliseconds, CodeBlock block) {
        onDrawTimeoutFunctions.put(System.currentTimeMillis() + milliseconds, block);
    }

    private void throwIfNoGui() {
        if(!isRenderingGUI) throw new IllegalStateException("Cannot call this method if not rendering the GUI");
    }

    public interface RenderBlock {
        void render();
    }

    interface CodeBlock {
        void invoke();
    }
}
