#version 330 core

uniform sampler2D tex1, tex2;

in vec2 texCoord;
out vec4 color;

void main() {
    vec4 tex1Texel = texture(tex1, texCoord);
    vec4 tex2Texel = texture(tex2, texCoord);
    color = mix(tex2Texel, tex1Texel, tex1Texel.a);
}