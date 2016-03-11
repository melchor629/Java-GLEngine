package org.melchor629.engine.utils;

import org.melchor629.engine.gl.GLContext;
import org.melchor629.engine.gl.Texture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Manager for textures. It will load texture and save a its reference
 * for reuse.
 * TODO Añadir que para cargar una textura use un json con la información requerida para cargarla
 */
public class TextureManager {
    private final ArrayList<TextureWrapper> textures;
    private final GLContext gl;

    public TextureManager(GLContext gl) {
        this.gl = gl;
        textures = new ArrayList<>();
    }

    public Texture loadTexture(String name, String url) throws IOException {
        Texture texture;
        if(searchTexture(name) != null)
            texture = searchTexture(name);
        else if(searchTexture(url) != null)
            texture = searchTexture(url);
        else {
            if(!new File(url).exists()) throw new FileNotFoundException("File " + url + " not found");
            TextureWrapper textureWrapper = new TextureWrapper();
            textureWrapper.name = name;
            textureWrapper.url = url;
            textureWrapper.texture = texture = new Texture.Builder(gl).setFile(new File(url)).setMipmap(true).
                    setWrap(GLContext.TextureWrap.CLAMP_TO_BORDER).setMin(GLContext.TextureFilter.NEAREST_MIPMAP_LINEAR)
                    .setMag(GLContext.TextureFilter.LINEAR_MIPMAP_LINEAR).build();
            textures.add(textureWrapper);
        }

        return texture;
    }

    public Texture loadTexture(String name, InputStream io) throws IOException {
        Texture texture = searchTexture(name);
        if(texture == null) {
            TextureWrapper textureWrapper = new TextureWrapper();
            textureWrapper.name = name;
            textureWrapper.url = "";
            textureWrapper.texture = texture = new Texture.Builder(gl).setStreamToFile(io).setMipmap(true).
                    setWrap(GLContext.TextureWrap.CLAMP_TO_BORDER).setMin(GLContext.TextureFilter.NEAREST_MIPMAP_LINEAR)
                    .setMag(GLContext.TextureFilter.LINEAR_MIPMAP_LINEAR).build();
            textures.add(textureWrapper);
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
