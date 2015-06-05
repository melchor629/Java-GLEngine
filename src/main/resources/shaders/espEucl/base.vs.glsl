#version 330 core

in vec2 position;
in vec2 tex_coord;

out vec2 texCoord;

void main() {
    gl_Position = vec4(position.x, position.y, 0.0, 1.0);
    texCoord = vec2(1.0 - tex_coord.y, tex_coord.x);
}