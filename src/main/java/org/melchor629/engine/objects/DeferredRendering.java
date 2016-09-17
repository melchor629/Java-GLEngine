package org.melchor629.engine.objects;

import org.melchor629.engine.gl.*;
import org.melchor629.engine.objects.lights.Light;
import org.melchor629.engine.utils.IOUtils;
import org.melchor629.engine.utils.math.ModelMatrix;
import org.melchor629.engine.window.Window;

import java.io.IOException;
import java.util.List;

/**
 * Created by melchor9000 on 10/9/16.
 */
public class DeferredRendering {
    private final GLContext gl;
    private final Camera camera;
    private final FrameBuffer fbo;
    private final Texture positionTex, normalTex, albedoTex, specularTex;
    private final ShaderProgram deferredPass, lightPass;
    private final VertexArrayObject vao;
    private int width, height;

    public DeferredRendering(Window window, Camera camera) {
        this.gl = window.getGLContext();
        this.camera = camera;
        fbo = gl.createFrameBuffer();
        width = window.getFramebufferSize().width;
        height = window.getFramebufferSize().height;

        // http://www.learnopengl.com/#!Advanced-Lighting/Deferred-Shading
        positionTex = gl.createTexture(GLContext.TextureFormat.RGB16F, width, height, GLContext.TextureExternalFormat.RGB);
        normalTex = gl.createTexture(GLContext.TextureFormat.RGB16F, width, height, GLContext.TextureExternalFormat.RGB);
        albedoTex = gl.createTexture(GLContext.TextureFormat.RGBA, width, height, GLContext.TextureExternalFormat.RGBA);
        specularTex = gl.createTexture(GLContext.TextureFormat.RG, width, height, GLContext.TextureExternalFormat.RGB);

        fbo.attachColorTexture(positionTex, 0);
        fbo.attachColorTexture(normalTex, 1);
        fbo.attachColorTexture(albedoTex, 2);
        fbo.attachColorTexture(specularTex, 3);
        gl.drawBuffers(GLContext.FramebufferAttachment.COLOR_ATTACHMENT, GLContext.FramebufferAttachment.COLOR_ATTACHMENT1,
                GLContext.FramebufferAttachment.COLOR_ATTACHMENT2, GLContext.FramebufferAttachment.COLOR_ATTACHMENT3);

        RenderBuffer depth = gl.createRenderBuffer(GLContext.TextureFormat.DEPTH_COMPONENT, window.getFramebufferSize().width, window.getFramebufferSize().height);
        fbo.attachDepthRenderbuffer(depth);
        fbo.unbind();

        try {
            deferredPass = gl.createShader(
                    IOUtils.readStream(IOUtils.getResourceAsStream("shaders/base/deferred.vs.glsl")),
                    IOUtils.readStream(IOUtils.getResourceAsStream("shaders/base/deferred.fs.glsl"))
            );
            lightPass = gl.createShader(
                    IOUtils.readStream(IOUtils.getResourceAsStream("shaders/base/basePass.vs.glsl")),
                    IOUtils.readStream(IOUtils.getResourceAsStream("shaders/base/lightPass.fs.glsl"))
            );

            deferredPass.setUniform("texture_diffuse", 0);
            deferredPass.setUniform("texture_specular", 1);
            deferredPass.setUniform("texture_normal", 2);
            deferredPass.setUniform("useNormalMap", 0);
            lightPass.setColorOutput("outColor", 0);
            lightPass.setUniform("gPosition", 0);
            lightPass.setUniform("gNormal", 1);
            lightPass.setUniform("gAlbedo", 2);
            lightPass.setUniform("gSpecular", 3);
        } catch (IOException e) {
            throw new RuntimeException("Base shaders are not installed", e);
        }

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

        vao = gl.createVertexArrayObject();
        vao.bind();
        plane.bind();
        gl.vertexAttribPointer(0, 2, GLContext.type.FLOAT, false, 4 * 4, 0);
        gl.vertexAttribPointer(1, 2, GLContext.type.FLOAT, false, 4 * 4, 2 * 4);
        gl.enableVertexAttribArray(0);
        gl.enableVertexAttribArray(1);
        plane.unbind();
        vao.unbind();
    }

    public void startRenderPass() {
        fbo.bind();
        deferredPass.bind();
        deferredPass.setUniformMatrix("projection", camera.getProjectionMatrix());
        deferredPass.setUniformMatrix("view", camera.getViewMatrix());
    }

    public void endRenderPass() {
        deferredPass.unbind();
        fbo.unbind();

        gl.disable(GLContext.GLEnable.DEPTH_TEST);
        gl.clear(GLContext.COLOR_CLEAR_BIT);
        lightPass.bind();

        gl.setActiveTexture(0);
        positionTex.bind();
        gl.setActiveTexture(1);
        normalTex.bind();
        gl.setActiveTexture(2);
        albedoTex.bind();
        gl.setActiveTexture(3);
        specularTex.bind();

        vao.bind();
        gl.drawArrays(GLContext.DrawMode.TRIANGLES, 0, 6);
        vao.bind();
        lightPass.unbind();
        gl.enable(GLContext.GLEnable.DEPTH_TEST);

        fbo.bindForRead();
        gl.blitFramebuffer(0, 0, width, height, 0, 0, width, height, GLContext.DEPTH_BUFFER_BIT, GLContext.TextureFilter.NEAREST);
        fbo.unbind();
    }

    public void setModelMatrix(ModelMatrix modelMatrix) {
        deferredPass.setUniformMatrix("model", modelMatrix.getModelMatrix());
    }

    public void setLights(List<Light> lights) {
        lightPass.bind();
        for(int i = 0; i < lights.size(); i++) lights.get(i).configureLightInShader(lightPass, i);
        lightPass.setUniform("lightsCount", lights.size());
        lightPass.unbind();
    }

    public void setLights(Light... lights) {
        lightPass.bind();
        for(int i = 0; i < lights.length; i++) lights[i].configureLightInShader(lightPass, i);
        lightPass.setUniform("lightsCount", lights.length);
        lightPass.unbind();
    }
}
