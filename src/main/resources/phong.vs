#version 150 core

in vec3 position, color, normal;
in vec2 texCoord;

out vec3 f_color, f_normal;
out vec2 f_texCoord;

uniform mat4 model, view, projection;

void main() {
    gl_Position = projection * view * model * vec4(position, 1.0);
    f_color = color;
    f_normal = normal;
    f_texCoord = texCoord;
}