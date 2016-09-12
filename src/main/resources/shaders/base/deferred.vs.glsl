#version 330 core

layout (location = 0) in  vec3  position;
layout (location = 1) in  vec3  normal;
layout (location = 2) in  vec2  texCoords;

out vec3 Position;
out vec3 Normal;
out vec2 TexCoords;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    vec4 worldPos = model * vec4(position, 1.0f);
    Position = worldPos.xyz;
    gl_Position = projection * view * worldPos;

    TexCoords = texCoords;

    Normal = transpose(inverse(mat3(model))) * normal + vec3(1);
    //TODO projection * view calculado de CPU
    //TODO transpose(inverse(mat3(model))) calculado de CPU
}
