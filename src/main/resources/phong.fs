#version 150 core

struct Material {
    vec3 emission, ambient, diffuse, specular, reflective;
    float shininess, reflectivity, transparency, ior;
    sampler2D emissionTex, ambientTex, diffuseTex, specularTex,
        reflectiveTex;
    bool useEmissionTex, useAmbientTex, useDiffuseTex, useSpecularTex
        , useReflectiveTex;
};

struct Light {
    vec3 pos, dir;
    float cutOff, outerCutOff;
    float c, l, q;
    vec4 emission;
    vec4 ambient;
};

in vec3 f_color, f_normal, f_pos;
in vec2 f_texCoord;

uniform vec3 cameraPos, cameraDir;
uniform Material material;
uniform Light light;

out vec4 outColor;

vec4 diffuseColor() {
    if(material.useDiffuseTex)
        return vec4(f_texCoord, 0.0, 1.0);//texture(material.diffuseTex, f_texCoord);
    else
        return vec4(material.diffuse, material.transparency);
}

vec4 specularColor() {
    if(material.useSpecularTex)
        return texture(material.specularTex, f_texCoord);
    else
        return vec4(material.specular, material.transparency);
}

vec4 ambientColor() {
    if(material.useAmbientTex)
        return texture(material.ambientTex, f_texCoord);
    else
        return vec4(material.ambient, material.transparency);
}

vec4 reflectiveColor() {
    if(material.useReflectiveTex)
        return texture(material.reflectiveTex, f_texCoord);
    else
        return vec4(material.reflective, material.transparency);
}

vec4 colorPointLight(Light l) {
    vec3 norm = normalize(f_normal);
    vec3 lightDir = normalize(l.pos - f_pos);

    float diff = max(dot(norm, lightDir), 0.0);
    vec4 diffuseLight = diff * diffuseColor();

    vec3 viewDir = normalize(cameraPos - f_pos);
    vec3 reflectDir = normalize(lightDir + cameraDir);//reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec4 specularLight = spec * specularColor();
    //TODO specular map

    float dist = length(l.pos - f_pos);
    float att = 1.0f / (l.c + l.l * dist + l.q * dist * dist);
    return l.emission * att * (diffuseLight + specularLight + ambientColor())
        + (1 - att) * l.ambient * diffuseColor();
}

vec4 colorSpotLight(Light l) {
    vec3 normal = normalize(f_normal);
    vec3 viewDir = normalize(cameraPos - f_pos);
    vec3 lightDir = normalize(l.pos - f_pos);

    // Diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    // Specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    // Attenuation
    float distance = length(light.pos - f_pos);
    float attenuation = 1.0f / (l.c + l.l * distance + l.q * (distance * distance));
    // Spotlight intensity
    float theta = dot(lightDir, normalize(-l.dir));
    float epsilon = l.cutOff - l.outerCutOff;
    float intensity = clamp((theta - l.outerCutOff) / epsilon, 0.0, 1.0);
    // Combine results
    vec4 ambient = l.ambient * ambientColor();
    vec4 diffuse = l.emission * diff * diffuseColor();
    vec4 specular = spec * specularColor();
    ambient *= attenuation * intensity;
    diffuse *= attenuation * intensity;
    specular *= attenuation * intensity;
    return (ambient + diffuse + specular);

    /*//Spot Light values
    float theta = dot(lightDir, normalize(-l.dir));
    float epsilon = l.cutOff - l.outerCutOff;
    float intensity = clamp((theta - l.outerCutOff) / epsilon, 0.0, 1.0);

    //Attenuation values
    float dist = length(l.pos - f_pos);
    float att = 1.0f / (l.c + l.l * dist + l.q * dist * dist);

    //Diffuse
    float diff = max(dot(norm, lightDir), 0.0);
    vec4 diffuseLight = diff * diffuseColor();

    //Specular
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec4 specularLight = spec * specularColor();
    //TODO specular map

    return l.emission * att * (intensity * (diffuseLight + specularLight) + ambientColor());*/
}

void main() {
    //vec3 hardcodeLight = vec3(4.076245, 1.005454, 5.903862);
    //vec4 hardcodeLightColor = vec4(1, 1, 1, 1);
    //vec3 hardcodeLightDir = vec3(0, 0, 1);
    outColor = colorPointLight(light);
    //outColor = colorSpotLight(light);
}
