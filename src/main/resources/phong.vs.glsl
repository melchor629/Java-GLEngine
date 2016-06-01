#version 150 core

in vec3 position, color, normal;
in vec2 texCoord;

out vec3 f_color, f_normal, f_pos, f_cameraPos;
out vec2 f_texCoord;

uniform mat4 model, view, projection;

void main() {
    gl_Position = projection * view * model * vec4(position, 1.0);
    f_color = color;
    //Calcular la siguiente matriz en Java y no aquí para eficiencia TODO
    f_normal = mat3(transpose(inverse(model))) * normal;
    f_texCoord = texCoord;
    f_pos = vec3(model * vec4(position, 1.0));
}
