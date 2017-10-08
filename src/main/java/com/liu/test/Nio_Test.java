package com.liu.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by hadoop on 2016/12/12 0012.
 */
public class Nio_Test {
    void nio_input(){

        String path = "E:\\IDEA_Project\\NIO_Test\\data.txt";
        RandomAccessFile aFile  = null;
        try {
            aFile = new RandomAccessFile(path,"rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FileChannel inChannl = aFile.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(48);
//        ByteBuffer buf2 = ByteBuffer.allocate(48);
//        ByteBuffer buf3 = ByteBuffer.allocate(24);
//        ByteBuffer[] arraybuff = {buf,buf2,buf3};

        long byteRead = 0;
        try {
            long pos = inChannl.position();
            inChannl.position(pos + 10);
            byteRead = inChannl.read(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while ( byteRead != -1 ){
            System.out.println("Read " + byteRead);
            buf.flip();
            while(buf.hasRemaining()){
                System.out.print((char)buf.get());
            }
            buf.clear();
            try {
                byteRead = inChannl.read(buf);
                inChannl.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        Nio_Test nio = new Nio_Test();
        nio.nio_input();
    }


}

