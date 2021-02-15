package com.example.logicalpuzzles;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileController{

    private Context context;

    public FileController(Context context){
        this.context = context;
    }
    void saveToStatsFile(String text) {
        saveToFile("statsFile", text);
    }
    public void saveToFile(String fileName, String text){
        try {
            File gpxfile = new File(String.valueOf(context.getFilesDir()), fileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(text);
            writer.flush();
            writer.close();
        } catch (Exception ignored) {
        }
    }
    String readFromAssetsFile(String fileName){
        try {
            return getInputStreamContent(context.getAssets().open("data/"+fileName));
        } catch (IOException ex) {
            return "";
        }
    }
    String readFromStatsFile(){
        return readFromSysFile("statsFile");
    }
    public String readFromSysFile(String fileName){
        try {
            File file = new File(context.getFilesDir(), fileName);
            return getInputStreamContent(new FileInputStream(file));
        }catch (IOException ex){
            return "";
        }
    }
    public boolean deleteFile(String fileName){
        File file = new File(context.getFilesDir(), fileName);
        return file.delete();
    }
    private String getInputStreamContent(InputStream is) throws IOException {
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        is.close();
        return new String(buffer, StandardCharsets.UTF_8);
    }

}

