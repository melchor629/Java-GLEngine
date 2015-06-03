#define STB_IMAGE_IMPLEMENTATION
#define STB_IMAGE_WRITE_IMPLEMENTATION
#include <stb/stb_image.h>
#include <stb/stb_image_write.h>
#include <string.h>

typedef struct {
    unsigned char* data;
    int width, height, components;
} image_data_t;

void stb_load_image(const char* file_path, image_data_t* info) {
    info->data = stbi_load(file_path, &info->width, &info->height, &info->components, 0);
}

void stb_clear_image(image_data_t* info) {
    stbi_image_free(info->data);
    free(info);
}

void stb_write_image(const char* file_path, const char* fmt, image_data_t* info) {
    if(!strcmp("png", fmt)) {
        stbi_write_png(file_path, info->width, info->height, info->components, info->data, info->width * info->components);
    } else if(!strcmp("tga", fmt)) {
        stbi_write_tga(file_path, info->width, info->height, info->components, info->data);
    } else if(!strcmp("bmp", fmt)) {
        stbi_write_bmp(file_path, info->width, info->height, info->components, info->data);
    }
}