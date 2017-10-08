package com.liu.test;


import java.nio.ByteBuffer;

/**
 * Created by hadoop on 2016/12/25 0025.
 */
public class Buffer_Test {
    public static void ByteBuffer_Test(){
        ByteBuffer  buffer = ByteBuffer.allocate(30);
        char[] myByteArray = new char[20];
        buffer.put((byte) 'H' ).put((byte) 'e').put((byte)'l').put((byte)'l').put((byte) 'o');
        buffer.put(5,(byte) '!');   //插入数据不是添加数据
        System.out.println("Buffer postion is: " + buffer.position());
        System.out.println("Buffer limit is: " + buffer.limit());
        System.out.println("Buffer capacity is :" + buffer.capacity());
        System.out.println("Buffer remaining is" + buffer.remaining());
        System.out.println(buffer);
        buffer.flip();
        System.out.println("Buffer postion is: " + buffer.position());
        System.out.println("Buffer limit is: " + buffer.limit());
        System.out.println("Buffer capacity is :" + buffer.capacity());
        for (int i = 0; buffer.hasRemaining( );++i){
            myByteArray[i] = (char)buffer.get( );
            System.out.print(myByteArray[i]);
        }
        System.out.println();
        buffer.clear();
    }
    public static void groupintobuffer(){
        byte[] b ;
        b = new byte[]{(byte)'h',(byte)'e',(byte)'l',(byte)'l',(byte)'o',(byte)' ',(byte)'w',(byte)'o',(byte)'r',(byte)'l',(byte)'d'};
        ByteBuffer buffer1 = ByteBuffer.allocate(20);
        buffer1.put(b);
        buffer1.flip();
        while(buffer1.hasRemaining())
            System.out.print((char)buffer1.get());
        buffer1.asCharBuffer();
    }
    public static void main(String[] args) {
      //      ByteBuffer_Test();
             groupintobuffer();

    }

}
