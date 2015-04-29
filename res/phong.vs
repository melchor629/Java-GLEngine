#version 150 core

in vec3 position;

uniform mat4 model, view, projection;

void main() {
    gl_Position = projection * view * model * position;
}