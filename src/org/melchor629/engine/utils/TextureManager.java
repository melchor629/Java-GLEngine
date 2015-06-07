package org.melchor629.engine.utils;

import org.melchor629.engine.gl.GLContext;
import org.melchor629.engine.gl.types.Texture;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Manager for textures. It will load texture and save a its reference
 * for reuse.
 * TODO Añadir que para cargar una textura use un json con la información requerida para cargarla
 */
public class TextureManager {
    private ArrayList<TextureWrapper> textures;
    private static final TextureManager instance = new TextureManager();

    public static TextureManager getInstance() {
        return instance;
    }

    private TextureManager() {
        textures = new ArrayList<>();
    }

    public Texture loadTexture(String name, String url) throws IOException {
        Texture texture;
        if(searchTexture(name) != null)
            texture = searchTexture(name);
        else if(searchTexture(url) != null)
            texture = searchTexture(url);
        else {
            TextureWrapper textureWrapper = new TextureWrapper();
            textureWrapper.name = name;
            textureWrapper.url = url;
            textureWrapper.texture = texture = new Texture.builder().setFile(new File(url)).setMipmap(true).
                    setWrap(GLContext.TextureWrap.CLAMP_TO_BORDER).setMin(GLContext.TextureFilter.NEAREST_MIPMAP_LINEAR)
                    .setMag(GLContext.TextureFilter.LINEAR_MIPMAP_LINEAR).build();
        }

        return texture;
    }

    public Texture searchTexture(String name_or_url) {
        int i = 0;
        while(i < textures.size() && !(textures.get(i).name.equals(name_or_url) || textures.get(i).url.equals(name_or_url)))
            i++;
        return i < textures.size() ? textures.get(i).texture : null;
    }

    public void deleteTextures() {
        textures.forEach(TextureWrapper::delete);
    }

    private static class TextureWrapper {
        String name, url;
        Texture texture;

        void delete() {texture.delete();}
    }
}
