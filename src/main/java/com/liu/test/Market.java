package com.liu.test;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by hadoop on 2016/12/26 0026.
 */
public class Market {
    private static final String DEMOGRAPHIC = "blahblah.txt";
    private static String[] col1 = {
            "Aggregate","Enable","Leverage",
            "Facilitate","Synergize","Repurpose",
            "Strategize","Reinvent","Harness"
    };
    private static String[] col2 = {
            "cross-platform","best-of-breed","frictionless",
            "ubiquitous","extensible","compelling",
            "mission-critical","collaborative","integrated"
    };
    private static String[] col3 = {
            "methodologies","infomediaries","platforms",
            "schemas","mindshare","paradigms",
            "functionalities","web services","infrastructure"
    };

    private static String newline = System.getProperty("line.separator");

    private static ByteBuffer[] utterBS(int howMany)
        throws Exception{
        List list = new LinkedList();
        for(int i = 0 ;i < howMany;++i){
            list.add(pickRandom(col1," "));
            list.add(pickRandom(col2," "));
            list.add(pickRandom(col3,newline));
        }
        ByteBuffer[] bufs = new ByteBuffer[list.size()];
        System.out.println("ByteBuffer has " + list.size() + " Buffers");
        list.toArray(bufs);
        return  (bufs);
    }
        private static Random rand = new Random();

    private static ByteBuffer pickRandom(String[] strings,String suffix)
    throws Exception{
        String string  = strings[rand.nextInt(strings.length)]; //随机得到string数组中的一个string
        int total = string.length() + suffix.length();
        ByteBuffer buf = ByteBuffer.allocate(total);
        buf.put(string.getBytes("US-ASCII"));
        buf.put (suffix.getBytes ("US-ASCII"));
        buf.flip( );
        return (buf);
    }

    public static void main(String[] args)
    throws  Exception{
        int reps = 10;
        if(args.length > 0){
            reps = Integer.parseInt(args[0]);
        }
        FileOutputStream fos = new FileOutputStream(DEMOGRAPHIC);
        GatheringByteChannel gatheringByteChannel = fos.getChannel();
        ByteBuffer[] bs = utterBS(reps);
        //从buffer数组中读取数据，直到读完循环结束
        while(gatheringByteChannel.write(bs) > 0);
        System.out.println("Mindshare paradigms synergized to " + DEMOGRAPHIC);

    }
}
