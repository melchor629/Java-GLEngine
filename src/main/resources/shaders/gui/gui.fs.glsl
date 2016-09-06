#version 330 core

uniform sampler2D tex1, tex2;

in vec2 texCoord;
out vec4 color;

void main() {
    vec4 tex1Texel = texture(tex1, texCoord);
    vec4 tex2Texel = texture(tex2, texCoord);

    if(tex1Texel.a == 0) {
        color = tex2Texel;
    } else if(tex2Texel.a == 0) {
        color = tex1Texel;
    } else {
        color = tex1Texel * tex1Texel.a + tex2Texel * (1-tex1Texel.a);
    }
}