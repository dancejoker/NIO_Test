package com.liu.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Created by hadoop on 2016/12/14 0014.
 */
public class channaltochannal {
    void Trasnform_Test(){
        try {
            RandomAccessFile fromfile = new RandomAccessFile("fromFile.txt","rw");
            FileChannel fromChannel = fromfile.getChannel();
            RandomAccessFile tofile = new RandomAccessFile("toFile.txt","rw");
            FileChannel toChannel = tofile.getChannel();
            long position = 0;
            long count = fromChannel.size();
            toChannel.transferFrom(fromChannel,position,count);
            fromChannel.transferTo(position,count,fromChannel);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        channaltochannal channal_test = new channaltochannal();
        channal_test.Trasnform_Test();
    }
}
