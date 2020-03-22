package com.shpak.polyglotreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileOperations {

    public void createFolder(String path) {
        File booksDirectory = new File(path);
        if (!booksDirectory.exists()) {
            booksDirectory.mkdirs();
        }
    }

    public boolean textToFile(String text, String path){
        try {
            File textFile = new File(path);
            if (!textFile.exists()) {
                textFile.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(textFile);
            fileOutputStream.write((text + System.getProperty("line.separator")).getBytes());
            return true;
        } catch(FileNotFoundException ex) {
        } catch(IOException ex) {
        }
        return  false;
    }

    public String textFromFile(File file) {
        StringBuilder inputText = new StringBuilder();
        String line;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
            while ((line = bufferedReader.readLine()) != null) {
                inputText.append(line);
            }
            bufferedReader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return inputText.toString();
    }

}
