package com.liu.test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 *  服务端
 * Created by hadoop on 2016/12/29 0029.
 */
public class ChannelAccept {
    public static final String GREETING = "Hello I must going.\r\n";

    public static void main(String[] args) throws  Exception{
            int port = 1234;
            if(args.length > 0){
                port = Integer.parseInt(args[0]);
            }
        ByteBuffer buffer = ByteBuffer.wrap(GREETING.getBytes());
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(port));
        ssc.configureBlocking(false);
        while(true){
            System.out.println("Waiting for connection");
            SocketChannel sc = ssc.accept();
            if(sc == null )
                Thread.sleep(2000);
            else {
                System.out.println("Incoming connection from :" + sc.socket().getRemoteSocketAddress());
                buffer.rewind();
                sc.write(buffer);
                sc.close();
            }
        }
    }
}
