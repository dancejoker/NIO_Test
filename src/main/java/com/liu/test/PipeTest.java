package com.liu.test;

import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.Pipe;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Random;

/**
 * Created by hadoop on 2016/12/30 0030.
 */
public class PipeTest {
    private static ReadableByteChannel startWorker(int reps) throws Exception{
        Pipe pipe = Pipe.open();
        Worker worker = new Worker(pipe.sink(),reps); //sink为写
        worker.start();
        return (pipe.source());
    }


    private static class Worker extends Thread{
        private String[] products = {
                "No good dead goes unpunished",
                "To be, or what?",
                "No matter where you go, there you are",
                "Just say \"Yo\"",
                "My karma ran over my dogma"
        };
        WritableByteChannel channel;
        private int reps;
        Worker(WritableByteChannel channel, int reps){
            this.channel = channel;
            this.reps = reps;
        }

        @Override
        public void run() {
            ByteBuffer buffer = ByteBuffer.allocate(100);
            try {
                for(int i = 0 ;i < this.reps;++i){
                    doSomeWork(buffer);
                    while(channel.write(buffer) > 0);
                }
                this.channel.close();
            }catch (Exception e){
                e.printStackTrace( );
            }
        }
        private Random rand = new Random();
        private void  doSomeWork(ByteBuffer buffer){
            int product  = rand.nextInt(products.length);
            buffer.clear();
            buffer.put(products [product].getBytes());
            buffer.put ("\r\n".getBytes( ));
            buffer.flip( );
        }
    }
    public static void main(String[] args) throws  Exception{
        WritableByteChannel out = Channels.newChannel(System.out);
        ReadableByteChannel workerChannel = startWorker(10);
        ByteBuffer buffer = ByteBuffer.allocate(100);
        while(workerChannel.read(buffer) >= 0){
            buffer.flip();
            out.write(buffer);
            buffer.clear();
        }
    }
}
