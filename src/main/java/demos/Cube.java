package demos;// Copyright (c) 2013, John Thomas McDole.
/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.melchor629.engine.gl.GLContext;
import org.melchor629.engine.gl.BufferObject;
import org.melchor629.engine.gl.ShaderProgram;
import org.melchor629.engine.gl.VertexArrayObject;

class Cube {

    BufferObject positionBuffer,
            normalBuffer,
            textureCoordBuffer,
            colorBuffer,
            indexBuffer;
    VertexArrayObject vao;

    GLContext gl;

    Cube(GLContext _gl) {
        gl = _gl;
        vao = gl.createVertexArrayObject();

        float[] vertices = {
            // Front face
                -1.0f, -1.0f,  1.0f,
                1.0f, -1.0f,  1.0f,
                1.0f,  1.0f,  1.0f,
                -1.0f,  1.0f,  1.0f,

                // Back face
                -1.0f, -1.0f, -1.0f,
                -1.0f,  1.0f, -1.0f,
                1.0f,  1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,

                // Top face
                -1.0f,  1.0f, -1.0f,
                -1.0f,  1.0f,  1.0f,
                1.0f,  1.0f,  1.0f,
                1.0f,  1.0f, -1.0f,

                // Bottom face
                -1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, -1.0f,  1.0f,
                -1.0f, -1.0f,  1.0f,

                // Right face
                1.0f, -1.0f, -1.0f,
                1.0f,  1.0f, -1.0f,
                1.0f,  1.0f,  1.0f,
                1.0f, -1.0f,  1.0f,

                // Left face
                -1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f,  1.0f,
                -1.0f,  1.0f,  1.0f,
                -1.0f,  1.0f, -1.0f}
        ;

        float[] vertexNormals = {
                // Front face
                0.0f,  0.0f,  1.0f,
                0.0f,  0.0f,  1.0f,
                0.0f,  0.0f,  1.0f,
                0.0f,  0.0f,  1.0f,

                // Back face
                0.0f,  0.0f, -1.0f,
                0.0f,  0.0f, -1.0f,
                0.0f,  0.0f, -1.0f,
                0.0f,  0.0f, -1.0f,

                // Top face
                0.0f,  1.0f,  0.0f,
                0.0f,  1.0f,  0.0f,
                0.0f,  1.0f,  0.0f,
                0.0f,  1.0f,  0.0f,

                // Bottom face
                0.0f, -1.0f,  0.0f,
                0.0f, -1.0f,  0.0f,
                0.0f, -1.0f,  0.0f,
                0.0f, -1.0f,  0.0f,

                // Right face
                1.0f,  0.0f,  0.0f,
                1.0f,  0.0f,  0.0f,
                1.0f,  0.0f,  0.0f,
                1.0f,  0.0f,  0.0f,

                // Left face
                -1.0f,  0.0f,  0.0f,
                -1.0f,  0.0f,  0.0f,
                -1.0f,  0.0f,  0.0f,
                -1.0f,  0.0f,  0.0f}
        ;

        float[] textureCoords = {
                // Front face
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f,

                // Back face
                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f,
                0.0f, 0.0f,

                // Top face
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,

                // Bottom face
                1.0f, 1.0f,
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,

                // Right face
                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f,
                0.0f, 0.0f,

                // Left face
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f}
        ;

        positionBuffer = gl.createBufferObject(GLContext.BufferTarget.ARRAY_BUFFER, GLContext.BufferUsage.STATIC_DRAW);
        normalBuffer = gl.createBufferObject(GLContext.BufferTarget.ARRAY_BUFFER, GLContext.BufferUsage.STATIC_DRAW);
        textureCoordBuffer = gl.createBufferObject(GLContext.BufferTarget.ARRAY_BUFFER, GLContext.BufferUsage.STATIC_DRAW);
        colorBuffer = gl.createBufferObject(GLContext.BufferTarget.ARRAY_BUFFER, GLContext.BufferUsage.STATIC_DRAW);
        indexBuffer = gl.createBufferObject(GLContext.BufferTarget.ELEMENT_ARRAY_BUFFER, GLContext.BufferUsage.STATIC_DRAW);

        positionBuffer.fillBuffer(vertices);
        textureCoordBuffer.fillBuffer(textureCoords);
        normalBuffer.fillBuffer(vertexNormals);
        colorBuffer.fillBuffer(new float[] {
                1.0f, 0.0f, 0.0f, // Front face
                1.0f, 0.0f, 0.0f, // Front face
                1.0f, 0.0f, 0.0f, // Front face
                1.0f, 0.0f, 0.0f, // Front face
                1.0f, 1.0f, 0.0f, // Back face
                1.0f, 1.0f, 0.0f, // Back face
                1.0f, 1.0f, 0.0f, // Back face
                1.0f, 1.0f, 0.0f, // Back face
                0.0f, 1.0f, 0.0f, // Top face
                0.0f, 1.0f, 0.0f, // Top face
                0.0f, 1.0f, 0.0f, // Top face
                0.0f, 1.0f, 0.0f, // Top face
                1.0f, 0.5f, 0.5f, // Bottom face
                1.0f, 0.5f, 0.5f, // Bottom face
                1.0f, 0.5f, 0.5f, // Bottom face
                1.0f, 0.5f, 0.5f, // Bottom face
                1.0f, 0.0f, 1.0f, // Right face
                1.0f, 0.0f, 1.0f, // Right face
                1.0f, 0.0f, 1.0f, // Right face
                1.0f, 0.0f, 1.0f, // Right face
                0.0f, 0.0f, 1.0f, // Left face
                0.0f, 0.0f, 1.0f, // Left face
                0.0f, 0.0f, 1.0f, // Left face
                0.0f, 0.0f, 1.0f, // Left face
        });
        indexBuffer.fillBuffer(new int[]{
                0, 1, 2, 0, 2, 3, // Front face
                4, 5, 6, 4, 6, 7, // Back face
                8, 9, 10, 8, 10, 11, // Top face
                12, 13, 14, 12, 14, 15, // Bottom face
                16, 17, 18, 16, 18, 19, // Right face
                20, 21, 22, 20, 22, 23  // Left face
        });
        indexBuffer.unbind();
        colorBuffer.unbind();
    }

    void bindStuff(ShaderProgram s, String vertex, String normal, String coord, String color) {
        s.bind();
        vao.bind();
        indexBuffer.bind();

        if (vertex != null) {
            positionBuffer.bind();
            s.vertexAttribPointer(vertex, 3, GLContext.type.FLOAT, false, 0, 0);
            s.enableAttrib(vertex);
        }

        if (normal != null) {
            normalBuffer.bind();
            s.vertexAttribPointer(normal, 3, GLContext.type.FLOAT, false, 0, 0);
            s.enableAttrib(normal);
        }

        if (coord != null) {
            textureCoordBuffer.bind();
            s.vertexAttribPointer(coord, 2, GLContext.type.FLOAT, false, 0, 0);
            s.enableAttrib(coord);
        }

        if (color != null) {
            colorBuffer.bind();
            s.vertexAttribPointer(color, 3, GLContext.type.FLOAT, false, 0, 0);
            s.enableAttrib(color);
        }

        vao.unbind();
        s.unbind();
        indexBuffer.unbind();
        colorBuffer.unbind();
    }

    void bindStuff(int vertex, int normal, int coord, int color) {
        vao.bind();
        indexBuffer.bind();

        if (vertex >= 0) {
            positionBuffer.bind();
            gl.vertexAttribPointer(vertex, 3, GLContext.type.FLOAT, false, 0, 0);
            gl.enableVertexAttribArray(vertex);
        }

        if (normal >= 0) {
            normalBuffer.bind();
            gl.vertexAttribPointer(normal, 3, GLContext.type.FLOAT, false, 0, 0);
            gl.enableVertexAttribArray(normal);
        }

        if (coord >= 0) {
            textureCoordBuffer.bind();
            gl.vertexAttribPointer(coord, 2, GLContext.type.FLOAT, false, 0, 0);
            gl.enableVertexAttribArray(coord);
        }

        if (color >= 0) {
            colorBuffer.bind();
            gl.vertexAttribPointer(color, 3, GLContext.type.FLOAT, false, 0, 0);
            gl.enableVertexAttribArray(color);
        }

        vao.unbind();
        indexBuffer.unbind();
        colorBuffer.unbind();
    }

    void draw() {
        vao.bind();
        gl.drawElements(GLContext.DrawMode.TRIANGLES, 36, GLContext.type.UNSIGNED_INT, 0);
        vao.unbind();
    }
}
