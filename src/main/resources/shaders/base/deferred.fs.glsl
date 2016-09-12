#version 330 core

layout (location = 0) out vec4 gPosition;
layout (location = 1) out vec4 gNormal;
layout (location = 2) out vec4 gAlbedo;
layout (location = 3) out vec4 gSpecular;

in  vec3 Position;
in  vec3 Normal;
in  vec2 TexCoords;

uniform sampler2D texture_diffuse;
uniform sampler2D texture_specular;
uniform sampler2D texture_normal;
uniform bool useNormalMap;

void main() {
    gPosition = vec4(Position, 1.0);
    gNormal = vec4(useNormalMap ? texture(texture_normal, TexCoords).rgb : normalize(Normal), 1.0);
    gAlbedo = texture(texture_diffuse, TexCoords);
    gSpecular = vec4(texture(texture_specular, TexCoords).r, gl_FragCoord.z, 0.0, 1.0);
}
