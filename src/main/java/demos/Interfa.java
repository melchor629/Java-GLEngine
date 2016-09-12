package demos;

import org.melchor629.engine.Game;
import org.melchor629.engine.gl.GLContext;
import org.melchor629.engine.gl.LWJGLWindow;
import org.melchor629.engine.gl.WindowBuilder;
import org.melchor629.engine.gui.*;
import org.melchor629.engine.gui.easing.*;
import org.melchor629.engine.utils.IOUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * pruebas de interfá
 */
public class Interfa extends Game {

    public static void main(String... args) {
        new Interfa();
    }

    private Interfa() {
        super(new LWJGLWindow.Builder()
                .setResizable(true)
                .setTitle("Pruebas de GUI")
                .setOpenGLContextVersion(WindowBuilder.OpenGLContextVersion.GL_33)
                .setDoubleBuffered(true)
                .setVisible(false)
                .create(1280, 720), true);
        startEngine();
    }

    @Override
    public void init() {
        window.setVsync(true);
        try {
            gui.loadSystemFont("Ubuntu", "Ubuntu-R");
            gui.loadSystemFont("Courier New", "Courier New");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        gui.setFont("Ubuntu", 20);

        Container container = new Container(new Frame(100, 100, 200, 200));
        container.paddingTop(25);
        container.paddingLeft(10);
        container.backgroundColor(Color.rgba(1, 1, 1, 0.35));

        Button button = new Button("Raul matao");
        button.backgroundColor(Color.white());
        button.x(0);
        button.y(0);
        button.padding(10);

        Box box = new Box();
        box.backgroundColor(Color.hex("#FFDEAD"));
        box.x(0);
        box.y(0);
        box.width(100f);
        box.height(30f);

        TextLabel label = new TextLabel("Paco :)");
        label.position(590, 0);
        label.size(100f, 100f);
        label.color(Color.grey());
        label.textAlign(TextLabel.VerticalAlign.CENTER);
        label.textAlign(TextLabel.HorizontalAlign.CENTER);
        label.backgroundColor(Color.rgba(0.6, 1, 1, 0.65));
        label.font("Courier New");
        label.fontSize(20);
        label.borderRadius(0, 0, 10, 10);

        TextInput input = new TextInput();
        input.position(10, 60);
        input.fontSize(14);
        input.backgroundColor(Color.rgba(0.8, 0.8, 0.8, 0.75));
        input.color(Color.rgb(0.2, 0.2, 0.2));

        ScrollContainer scroll = new ScrollContainer(new Frame(300, 100, 100, 100));

        TextLabel label2 = new TextLabel("Que putes se jo??");
        label2.position(50, 10);
        scroll.addSubview(label2);

        Box box2 = new Box();
        box2.position(0, 50);
        box2.size(100f, 100f);
        box2.backgroundColor(Color.rgb(0.1, 1, 0.1));
        scroll.addSubview(box2);


            new Thread(() -> {
                try {
                    ByteBuffer b = IOUtils.readUrl(new URL("http://www.patosolidario.org/wp-content/uploads/2015/09/pato_solidario.jpg"));
                    post(() -> {
                        Image i = Image.fromMemory(b, 0);
                        i.position(0, 0);
                        i.size(100, 100);
                        box2.backgroundImage(i);
                    });
                } catch (IOException ignore) {}
            }).start();

        container.addSubview(box);
        container.addSubview(button);
        container.addSubview(input);
        gui.rootView.addSubview(container);
        gui.rootView.addSubview(label);
        gui.rootView.addSubview(scroll);
        window.showWindow();

        new Animation(label, 0.5, new Animation.Property<>("y", -80f)).setDelay(1)
                .setEasing(BounceEasing.easing, Easing.Type.OUT).chainAnimation(
        new Animation(button, 0.5, new Animation.Property<>("backgroundColor", Color.red()))
                .setDelay(0.1).chainAnimation(
        new Animation(box, 0.5, new Animation.Property<>("frame", new Frame(10, 10, 100, 60))).setDelay(0.1))).startAnimation();

        //new Animation(raulgolfo, 10, new Animation.Property<>("opacity", 1f)).setDelay(1).setEasing(QuadEasing.easing, Easing.Type.IN).startAnimation();

        gl.clearColor(1, 1, 1, 1);
    }

    private boolean doneFirstRender = false;
    @Override
    public void render() {
        if(!doneFirstRender) {
            gl.clear(GLContext.COLOR_CLEAR_BIT);
            doneFirstRender = true;
        }
    }

    @Override
    public void gui(long ctx) {
        /*GUIDrawUtils.setFillColor(Color.cyan());
        GUIDrawUtils.drawRoundedRectangle(400, 400, 100, 100, 10);
        GUIDrawUtils.drawRoundedRectangle(600, 400, 100, 100, 50, 50, 50, 50);
        GUIDrawUtils.drawCircle(850, 450, 50);*/
    }

    @Override
    public void closing() {

    }
}
