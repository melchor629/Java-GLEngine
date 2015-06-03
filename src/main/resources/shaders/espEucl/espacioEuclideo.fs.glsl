#version 330 core

in vec2 texCoord;
in float zVal;
out vec4 color;

uniform sampler2D euclides;
uniform float opacity;

void main() {
    if(zVal < 90.0)
        color = texture(euclides, texCoord) * opacity;
    else
        color = texture(euclides, texCoord) * opacity * (100.0 - zVal) / 10.0;
}