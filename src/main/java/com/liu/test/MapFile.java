package com.liu.test;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 诠释了各种模式的内存映射如何交互。具体来说，
 * 例中代码诠释了写时拷贝是如何页导向（page-oriented）的。
 * 当在使用MAP_MODE.PRIVATE模式创建的MappedByteBuffer对象
 * 上调用put( )方法而引发更改时，就会生成一个受影响页的拷贝。
 * 这份私有的拷贝不仅反映本地更改，而且使缓冲区免受来自外部
 * 对原来页更改的影响。然而，对于被映射文件其他区域的更改还
 * 是可以看到的。
 * Created by hadoop on 2016/12/29 0029.
 */
public class MapFile {
    public static void showBuffers(ByteBuffer ro,ByteBuffer rw,ByteBuffer cow)
    throws  Exception{
            dumpBuffer("R/O", ro);
            dumpBuffer("R/W",rw);
            dumpBuffer("COW",cow);
            System.out.println("");
    }
    public static void dumpBuffer(String prefix ,ByteBuffer buffer)
    throws  Exception{
        System.out.print(prefix + ": '");
        int nulls = 0;
        int limit = buffer.limit();
        for(int i = 0 ;i < limit;++i){
            char c = (char) buffer.get(i);
            if(c == '\u0000'){  //若文件内容为0则，++nulls
                ++nulls;
                continue;
            }
            if(nulls != 0) {
                System.out.print ("|[" + nulls + " nulls]|");
                nulls = 0;
            }
            System.out.print (c);
        }
        System.out.println ("'");
    }
    /*
      *
      *
     */
    public static void main(String[] args) throws Exception{
        File tempFile = File.createTempFile("maptest",null);
        RandomAccessFile file = new RandomAccessFile(tempFile,"rw");
        FileChannel channel = file.getChannel();
        ByteBuffer temp  = ByteBuffer.allocate(100);
        temp.put("This is the file content".getBytes());
        temp.flip();
        channel.write(temp,0);
        //在文件位置为8192(也就是8KB)插入数据,这个位置肯定是一个新页。
        //这可能引起文件空洞，取决于文件系统的页大小。
        //也就是说该文件存储在2个页上
        //在PRIVATE模式下，出现修改操作，则buffer会修改所在区的页
        //一旦某个页因为写操作而被拷贝，
        // 之后就将使用该拷贝页，并且不能被其他缓冲区或文件更新所修改
        temp.clear();
        temp.put("This is more file content".getBytes());
        temp.flip();
        channel.write(temp, 8192);
        //制作三种类型的mapping
        MappedByteBuffer ro = channel.map ( FileChannel.MapMode.READ_ONLY, 0, channel.size( ));
        MappedByteBuffer rw = channel.map ( FileChannel.MapMode.READ_WRITE, 0, channel.size());
        MappedByteBuffer cow = channel.map ( FileChannel.MapMode.PRIVATE, 0, channel.size( ));
        System.out.println("Channel size is : " + channel.size());
        System.out.println("Begin");
        showBuffers(ro, rw, cow);
        //改变写时拷贝PRIVATEbuffer
        cow.position(8);
        cow.put("COW".getBytes());
        System.out.println("Change to COW buffer");
        showBuffers(ro, rw, cow);
        ///改变READ_WRITEbuffer
        rw.position(9);
        rw.put(" R/W ".getBytes());
        rw.position(8194);
        rw.put(" R/W ".getBytes());
        rw.force();
        System.out.println("Change to R/W buffer");
        showBuffers(ro, rw, cow);
        //通过管道写文件，对2边都有影响
        temp.clear();
        temp.put("Channel write ".getBytes());
        temp.flip();
        channel.write(temp, 0);
        temp.rewind();
        channel.write(temp, 8202);
        System.out.println("Write on channel");
        showBuffers(ro, rw, cow);
        //对写时拷贝再一次改变
        cow.position(8207);
        cow.put(" COW2 ".getBytes());
        System.out.println("Second change to COW buffer");
        showBuffers(ro, rw, cow);
        //改变read/write buffer
        rw.position(0);
        rw.put(" R/W2 ".getBytes());
        rw.position(8210);
        rw.put(" R/W2 ".getBytes());
        rw.force();
        System.out.println("Second change to R/W buffer");
        showBuffers(ro, rw, cow);
        channel.close();
        file.close();
        tempFile.delete();
    }
}
