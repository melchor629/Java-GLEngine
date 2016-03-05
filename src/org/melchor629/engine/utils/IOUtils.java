package org.melchor629.engine.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * I/O Utils for reading &amp; writing data from all type of streams
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
    public static String readFile(File file) throws IOException {
        return readStream(new FileInputStream(file));
    }

    /**
     * Read the content of a text input stream and then return its contents
     * @param is An input stream to be read
     * @return Contents of the file
     * @throws IOException If an I/O error happend while reading
     */
    public static String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        try(Scanner sc = new Scanner(is)) {
            while(sc.hasNextLine())
                sb.append(sc.nextLine()).append('\n');
        }
        return sb.toString();
    }

    /**
     * Write some text into a file (and create it if doesn't exist)
     * @param file File where the text will be written
     * @param content Text to be written
     * @throws FileNotFoundException If the file cannot be created or is a directory
     * @throws IOException If an I/O error happend while writing
     */
    public static void writeFile(File file, String content) throws IOException {
        BufferedWriter bw = null;
        boolean created = true;
        try {
            if(!file.exists())
                created = file.createNewFile();
            if(!created) {
                throw new IOException("File cannot be created");
            }
            bw = new BufferedWriter(new FileWriter(file, false));
            bw.write(content);
            bw.flush();
        } catch(FileNotFoundException e) {
            if(bw != null)
                bw.close();
            throw e;
        } finally {
            if(bw != null)
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
    public static void appendFile(File file, String content) throws IOException {
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
            if(bw != null)
                bw.close();
        }
    }

    /**
     * Read the contents of a URL as text
     * @param url Url to read
     * @return Contents of the URL
     * @throws IOException If an error occurs while reading the connexion
     */
    public static String readUrl(URL url) throws IOException {
        StringBuilder sb = new StringBuilder();

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

    /**
     * Creates a temporary file with the prefix engine and extension .tmp
     * @return temporary file
     * @throws IOException If the file cannot be created
     */
    public static File createTempFile() throws IOException {
        return File.createTempFile("engine", ".tmp");
    }

    /**
     * Looks for a resource in class path and creates an Input Stream
     * @param path relative path to the file
     * @return InputStream of the file
     */
    public static InputStream getResourceAsStream(String path) {
        try{return new java.io.FileInputStream(new File(path));}catch(Exception ignore){}
        return IOUtils.class.getClassLoader().getResourceAsStream(path);
    }
}
