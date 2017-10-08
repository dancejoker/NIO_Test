package com.liu.test;

import java.io.FileInputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Created by hadoop on 2016/12/29 0029.
 */
public class ChannelTransfer {

    private static void catFiles(WritableByteChannel target,String[] files)
        throws Exception{
        for(int i = 0;i < files.length;++i){
            FileInputStream fis = new FileInputStream(files[i]);
            FileChannel channel = fis.getChannel();
            channel.transferTo(0,channel.size(),target);
            channel.close();
            fis.close();
        }
    }

    public static void main(String[] args) throws Exception{
            if(args.length == 0){
                System.out.println("Usage: filename ...");
                return;
            }
        catFiles(Channels.newChannel(System.out),args);

    }
}
