#version 150 core

in vec3 f_color, f_normal;
in vec2 f_texCoord;

uniform vec4 emission, ambient, diffuse, specular, reflective;
uniform float shininess, reflectivity, transparency, ior;
uniform sampler2D emissionTex, ambientTex, diffuseTex, specularTex,
    reflectiveTex;
uniform bool useEmissionTex, useAmbientTex, useDiffuseTex, useSpecularTex
    , useReflectiveTex;

out vec4 outColor;

void main() {
    if(useDiffuseTex)
        outColor = texture(diffuseTex, f_texCoord);
    else
        outColor = diffuse;
}