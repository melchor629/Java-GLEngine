#version 330 core

uniform sampler2D currentFrame, prevFrame;
uniform float phosphor;

in vec2 texCoord;
out vec4 color;

void main() {
    vec4 currTexel = texture(currentFrame, texCoord);
    vec4 prevTexel = texture(prevFrame, texCoord);

    color = vec4(max(prevTexel.rgb * phosphor, currTexel.rgb), 1.0);
}