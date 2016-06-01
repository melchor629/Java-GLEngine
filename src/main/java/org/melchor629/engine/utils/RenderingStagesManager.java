package org.melchor629.engine.utils;

import org.melchor629.engine.Game;
import org.melchor629.engine.gl.*;

import java.util.ArrayList;
import java.util.function.Function;

import static org.melchor629.engine.gl.GLContext.TextureExternalFormat.DEPTH_COMPONENT;
import static org.melchor629.engine.gl.GLContext.TextureExternalFormat.RGBA;

/**
 * Created by melchor9000 on 24/3/16.
 */
public class RenderingStagesManager {
    private Runnable renderSceneFunction;
    private ArrayList<RenderStep> renderSteps = new ArrayList<>();
    private FrameBuffer fb1, fb2, fb0;
    private int stepNo = 0;
    private GLContext gl;
    private BufferObject plane;

    public RenderingStagesManager(Game g, Runnable rsf) {
        renderSceneFunction = rsf;

        gl = g.getOpenGLContext();
        fb0 = gl.createFrameBuffer();
        fb1 = gl.createFrameBuffer();
        fb2 = gl.createFrameBuffer();
        int width = g.getWindow().getFramebufferSize().width;
        int height = g.getWindow().getFramebufferSize().height;

        fb0.attachColorTexture(gl.createTexture(GLContext.TextureFormat.RGBA, width, height, RGBA), 0);
        fb0.attachDepthTexture(gl.createTexture(GLContext.TextureFormat.DEPTH_COMPONENT, width, height, DEPTH_COMPONENT));
        fb0.attachStencilRenderbuffer(gl.createRenderBuffer(GLContext.TextureFormat.STENCIL_INDEX, width, height));

        fb1.attachColorTexture(gl.createTexture(GLContext.TextureFormat.RGBA, width, height, RGBA), 0);
        fb1.attachDepthTexture(gl.createTexture(GLContext.TextureFormat.DEPTH_COMPONENT, width, height, DEPTH_COMPONENT));
        fb1.attachStencilRenderbuffer(gl.createRenderBuffer(GLContext.TextureFormat.STENCIL_INDEX, width, height));

        fb2.attachColorTexture(gl.createTexture(GLContext.TextureFormat.RGBA, width, height, RGBA), 0);
        fb2.attachDepthTexture(gl.createTexture(GLContext.TextureFormat.DEPTH_COMPONENT, width, height, DEPTH_COMPONENT));
        fb2.attachStencilRenderbuffer(gl.createRenderBuffer(GLContext.TextureFormat.STENCIL_INDEX, width, height));

        plane = gl.createBufferObject(GLContext.BufferTarget.ARRAY_BUFFER, GLContext.BufferUsage.STATIC_DRAW);
        plane.fillBuffer(new float[] {
              //POS   | TEXCOORD
                1, -1,  0, 0,
                1,  1,  1, 0,
                -1,  1, 1, 1,
                -1, -1, 0, 1,
                1, -1,  0, 0,
                -1,  1, 1, 1
        });
    }

    public void addRenderStep(RenderStep renderStep) {
        renderSteps.add(renderStep);
    }

    public void render() {
        fb1.bind();
        renderSceneFunction.run();
        fb1.unbind();
        stepNo = 0;

        renderSteps.forEach(rs -> {
            FrameBuffer fba, fbb;
            if(stepNo == -1) {
                fba = fb0;
                fbb = fb1;
            } else if((stepNo % 2) == 0) {
                fba = fb1;
                fbb = fb2;
            } else {
                fba = fb2;
                fbb = fb1;
            }

            gl.setActiveTexture(0);
            fba.getColorRBAttachment(0);
            gl.setActiveTexture(1);
            fba.getDepthTexAttachment();
            gl.setActiveTexture(2);
            fb0.getColorRBAttachment(0);
            fbb.bind();

            gl.clear(GLContext.COLOR_CLEAR_BIT | GLContext.DEPTH_BUFFER_BIT | GLContext.STENCIL_BUFFER_BIT);
            rs.renderStep();
            stepNo++;
            fb1.unbind();
        });

    }

    public class RenderStep {
        private ShaderProgram shader;
        private VertexArrayObject vao;
        private Function<RenderStep, Void> renderFunction;
        private String name;

        public RenderStep(String name, ShaderProgram shader, Function<RenderStep, Void> rf) {
            this.name = name;
            this.shader = shader;
            this.renderFunction = rf;

            vao = gl.createVertexArrayObject();
            vao.bind();
            plane.bind();
            shader.vertexAttribPointer("position", 2, GLContext.type.FLOAT, false, 4 * 4, 0);
            shader.vertexAttribPointer("tex_coord", 2, GLContext.type.FLOAT, false, 4 * 4, 2 * 4);
            shader.enableAttrib("position");
            shader.enableAttrib("tex_coord");
            vao.unbind();
            shader.setUniform("color", 0);
            shader.setUniform("depth", 1);
            shader.setUniform("orig_color", 2);
            shader.setColorOutput("out_color", 0);
        }

        public RenderStep(String name) {
            this(name, null, null);
        }

        public RenderStep setShader(ShaderProgram s) {
            this.shader = s;
            return this;
        }

        public RenderStep setRenderFunction(Function<RenderStep, Void> rf) {
            this.renderFunction = rf;
            return this;
        }

        private void renderStep() {
            renderFunction.apply(this);
        }
    }
}
