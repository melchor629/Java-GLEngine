#version 330 core

in vec3 punto;
in vec2 texcoord;
in vec3 puntos;

out vec2 texCoord;
out float zVal;

uniform mat4 view, projection;

void main() {
    vec3 posae = puntos;
    vec4 relativePos = view * vec4(punto + posae, 1.0);
    gl_Position = projection * relativePos;
    texCoord = texcoord;
    zVal = gl_Position.z;
}