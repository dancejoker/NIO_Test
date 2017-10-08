package com.file.text;

import java.io.*;

/**
 * Created by hadoop on 2016/12/15 0015.
 */
public class File_Start {
    public static void  createFile() {
        String path = "E:" + File.separator + "file";
        String path2 =  "X man" + File.separator + "fight" + File.separator + "with" + File.separator + "superman";
        String file = "E:" + File.separator + "file" + File.separator + "file_test.txt";
        File f = new File(file);
        File p = new File(path);
        File f_p = new File(path2,"file_test.txt");
        try {
            if(p.exists())
                p.delete();
            p.mkdirs();
            if(f.exists())
                f.delete();
            f.createNewFile();
            if(f_p.exists())
                System.out.println("file is exist");
            else{
                f_p.getParentFile().mkdirs();
                f_p.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        File_Start.createFile();
    }
}
