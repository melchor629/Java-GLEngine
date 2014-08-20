package org.melchor629.engine.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

/**
 * I/O Utils for reading & writing data from all type of streams
 * @author melchor9000
 */
public class IOUtils {

    /**
     * Read the content of a text file and then return its contents
     * @param file File to be read
     * @return Contents of the file
     * @throws FileNotFoundException If the file doesn't exist, is a directory or (rarely) failed reading
     * @throws IOException If an I/O error happend while reading
     */
    public static final String readFile(File file) throws FileNotFoundException, IOException {
        BufferedReader bis = null;
        StringBuilder sb = new StringBuilder();
        try {
            bis = new BufferedReader(new FileReader(file));
            
            String line;
            while((line = bis.readLine()) != null)
                sb.append(line).append('\n');
        } catch(FileNotFoundException e) {
            if(bis != null)
                bis.close();
            throw e;
        } finally {
            bis.close();
        }
        return sb.toString();
    }

    /**
     * Read an image to a ByteBuffer, ready for use as a texture
     * or other stuff. Automatically detect if the image is
     * RGB or RGBA. This method can read files with random extensions,
     * but the format ({@code ext}), has to be valid, like PNG or JPG.
     * @param file Image to decode
     * @param ext Format of the image
     * @return {@link IOUtils.Image} with the image decoded
     * @throws IOException If we cannot read the file, or is a directory
     */
    public static final Image readImage(File file, String ext) throws IOException {
        BufferedImage bi = null;

        boolean imageio = false;
        for(String fmt : ImageIO.getReaderFormatNames()) {
            if(fmt.toLowerCase().equals(ext)) {
                imageio = true;
                break;
            }
        }
        if(imageio)
            bi = ImageIO.read(file);
        else {
            throw new IOException("Cannot read file with extension: " + ext);
        }
        
        int[] pixels = new int[bi.getWidth() * bi.getHeight()];
        boolean alpha = bi.getColorModel().hasAlpha();
        int channels = alpha ? 4 : 3;
        bi.getRGB(0, 0, bi.getWidth(), bi.getHeight(), pixels, 0, bi.getWidth());
        byte[] buffer = new byte[bi.getWidth() * bi.getHeight() * channels];
        int i = 0;

        for(int y = 0; y < bi.getHeight(); y++) {
            for(int x = 0; x < bi.getWidth(); x++) {
                int pos = y * bi.getHeight() + x;
                int pixel = pixels[pos];
                buffer[i++] = (byte) ((pixel >> 16) & 0xFF);
                buffer[i++] = (byte) ((pixel >> 8) & 0xFF);
                buffer[i++] = (byte) (pixel & 0xFF);
                if(alpha)
                    buffer[i++] = ((byte) ((pixel >> 24) & 0xFF));
            }
        }
        Image img = new Image();
        img.buffer = buffer;
        img.width = bi.getWidth();
        img.height = bi.getHeight();
        img.channels = channels;
        img.alpha = alpha;
        return img;
    }

    /**
     * Read an image to a ByteBuffer, ready for use as a texture
     * or other stuff. Automatically detect if the image is
     * RGB or RGBA. This method detects the format of the file
     * depending on its extension.
     * @param file Image to decode
     * @return {@link IOUtils.Image} with the image decoded
     * @throws IOException If we cannot read the file, or is a directory
     */
    public static final Image readImage(File file) throws IOException {
        String ext = file.getName();
        ext = ext.substring(ext.lastIndexOf('.') + 1).toLowerCase();
        return readImage(file, ext);
    }

    /**
     * Write some text into a file (and create it if doesn't exist)
     * @param file File where the text will be written
     * @param content Text to be written
     * @throws FileNotFoundException If the file cannot be created or is a directory
     * @throws IOException If an I/O error happend while writing
     */
    public static final void writeFile(File file, String content) throws FileNotFoundException, IOException {
        BufferedWriter bw = null;
        try {
            if(!file.exists()) file.createNewFile();
            bw = new BufferedWriter(new FileWriter(file, false));
            bw.write(content);
            bw.flush();
        } catch(FileNotFoundException e) {
            if(bw != null)
                bw.close();
            throw e;
        } finally {
            bw.close();
        }
    }

    /**
     * Write some text into the end of a file (append)
     * @param file File where the text will be written
     * @param content Text to be written
     * @throws FileNotFoundException If the file is a directory
     * @throws IOException If an I/O error happend while writing
     */
    public static final void appendFile(File file, String content) throws FileNotFoundException, IOException {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(content);
            bw.flush();
        } catch(FileNotFoundException e) {
            if(bw != null)
                bw.close();
            throw e;
        } finally {
            bw.close();
        }
    }

    /**
     * Read the contents of a URL as text
     * @param url Url to read
     * @return Contents of the URL
     * @throws IOException If an error occurs while reading the connexion
     */
    public static final String readUrl(URL url) throws IOException {
        StringBuilder sb = new StringBuilder();
        //ByteArrayOutputStream baos = new ByteArrayOutputStream();

        URLConnection con = url.openConnection();
        con.setConnectTimeout(1000);
        con.setReadTimeout(2000);
        InputStream in = con.getInputStream();

        String enc = con.getHeaderField("Content-Type");
        enc = enc.substring(enc.indexOf("charset=") > 0 ? enc.indexOf("charset=") + 8 : enc.length());
        if(enc.length() < 3) enc = "UTF-8";
        
        BufferedReader br = new BufferedReader(new InputStreamReader(in, enc));
        
        String line;
        while((line = br.readLine()) != null)
            sb.append(line).append('\n');

        br.close();

        return sb.toString();
    }

    public static final File createTempFile() throws IOException {
        return File.createTempFile("engine", ".tmp");
    }

    /**
     * Class containing information about images for reading and writting
     * @author melchor9000
     */
    public static final class Image {
        public byte[] buffer;
        public int width, height;
        public int channels;
        public boolean alpha;
    }
}
