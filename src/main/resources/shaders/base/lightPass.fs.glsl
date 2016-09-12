#version 330 core

in vec2 TexCoords;
out vec4 outColor;

uniform sampler2D gPosition;
uniform sampler2D gNormal;
uniform sampler2D gAlbedo;
uniform sampler2D gSpecular;

struct Light {
    int type; // 0 -> disabled, 1 -> Point, 2 -> Directional
    vec3 position;
    vec3 color;
    float linear, quadratic;
};

const int NR_LIGHTS = 32;
uniform int lightsCount;
uniform Light lights[NR_LIGHTS];
uniform vec3 viewPos;

void main() {
    vec3 pos = texture(gPosition, TexCoords).rgb;
    vec3 normal = texture(gNormal, TexCoords).rgb;
    vec4 albedo = texture(gAlbedo, TexCoords);
    float specular = texture(gSpecular, TexCoords).r;

    vec3 lighting = vec3(albedo) * 0.1; //Hardcoded ambient
    vec3 viewDir = normalize(viewPos - pos);
    for(int i = 0; i < lightsCount; ++i) {
        //Diffuse
        vec3 lightDir = normalize(lights[i].position - pos);
        vec3 diffuse = max(dot(normal, lightDir), 0.0) * vec3(albedo) * lights[i].color;
        //Specular
        vec3 halfwayDir = normalize(lightDir + viewDir);
        float spec = pow(max(dot(normal, halfwayDir), 0.0), 16.0);
        vec3 specular_ = lights[i].color * spec * specular;
        //Attenuation
        float distance = length(lights[i].position - pos);
        float attenuation = 1.0 / (1.0 + lights[i].linear * distance + lights[i].quadratic * distance * distance);
        diffuse *= attenuation;
        specular *= attenuation;
        lighting += diffuse + specular;
    }

    outColor = vec4(lighting, albedo.a);
}