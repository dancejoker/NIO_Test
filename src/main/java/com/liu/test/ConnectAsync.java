package com.liu.test;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 客户端
 * Created by hadoop on 2016/12/30 0030.
 */
public class ConnectAsync {
    private static void doSomethingUseful(ByteBuffer buffer) {
        buffer.flip();
        while (buffer.hasRemaining())
            System.out.println(buffer.get());
        System.out.println("doing something useless");
    }

    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 80;
        ByteBuffer buffer = ByteBuffer.allocate(100);
        if (args.length == 2) {
            host = args[0];
            port = Integer.parseInt(args[1]);
        }
        InetSocketAddress addr = new InetSocketAddress(host, port);
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        System.out.println("Initiating connection");
        sc.connect(addr);
        while (!sc.finishConnect()) {
            doSomethingUseful(buffer);
        }
        int numBytesRead;
        while ((numBytesRead = sc.read(buffer)) != -1) {
            if (numBytesRead == 0) {
                // 如果没有数据，则稍微等待一下
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            // 转到最开始
            buffer.flip();
            while (buffer.remaining() > 0) {
                System.out.print((char) buffer.get());
            }

            System.out.println("connection established");
            sc.close();
        }
    }
}
