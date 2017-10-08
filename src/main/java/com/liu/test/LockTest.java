package com.liu.test;


import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Random;

/**
 * Created by hadoop on 2016/12/27 0027.
 */
public class LockTest {
    private static final int SIZE_INT = 4;
    private static final int INDEX_START = 0;
    private static final int INDEX_COUNT = 10;
    private static final int INDEX_SIZE = INDEX_COUNT * SIZE_INT;
    private ByteBuffer buffer = ByteBuffer.allocate(INDEX_SIZE);
    private IntBuffer indexBuffer = buffer.asIntBuffer();
    private Random rand = new Random();

    void doQueries(FileChannel fc) throws Exception{
        int index = 1;
            while(true){
                index++;
                println("trying  for shared lock....");
                FileLock lock = fc.lock(INDEX_START,INDEX_SIZE,true);  //启动共享锁
                int reps = rand.nextInt(60) + 20;
                for(int i = 0; i < reps; ++i){
                    int n = rand.nextInt(INDEX_COUNT);
                    int position = INDEX_START + (n * SIZE_INT);
                    buffer.clear();
                    fc.read(buffer, position);
                    int value = indexBuffer.get(n);
                    println("Index entry " + n + "=" + value);
                    Thread.sleep(100);
                }
                if(index == 2)
                    break;
                lock.release();
                println("<sleep>");
                Thread.sleep(rand.nextInt(3000) + 500);
            }
    }
    void doUpdates(FileChannel fc) throws Exception{
        int index = 1;
            while(true) {
                index++;
                println("trying for exclusive lock...");
                FileLock lock = fc.lock(INDEX_START, INDEX_SIZE, false);
                updateIndex(fc);
                lock.release();
                println("<sleep>");
                Thread.sleep(rand.nextInt(2000) + 500);
                if(index == 2)
                    break;
            }
    }
    private int idxval = 1;
    private void updateIndex(FileChannel fc) throws Exception{
            indexBuffer.clear();
            for(int i = 0; i < INDEX_COUNT;++i){
                idxval ++;
                println("Updating index " + i + "=" + idxval);
                indexBuffer.put(idxval);
                Thread.sleep(500);
            }
        buffer.clear();
        fc.write(buffer,INDEX_START);
    }

    private int lastLineLen = 0;
    private void println(String msg){
        System.out.println(" ");
        System.out.print(msg);
//        for(int i = msg.length(); i < lastLineLen; i++)
//            System.out.print(" ");
        System.out.print(" ");
        lastLineLen = msg.length();
    }
    public static void main(String[] args)
    throws Exception{
        boolean writer = false;
        String filename ;
        if(args.length != 2 ){
            System.out.println("Usage:[ -r | -w ] filename");
            return;
        }
        writer = args[0].equals("-w");   //若参数是写，则返回true
        filename = args[1];
        RandomAccessFile raf = new RandomAccessFile(filename,(writer) ? "rw": "r"); //若是true则是读写形式，否则是读
        FileChannel fc  = raf.getChannel();
        LockTest locktest = new LockTest();
        if(writer){
            locktest.doUpdates(fc);   //读写
        }else{
            locktest.doQueries(fc);    //读
        }
    }
}