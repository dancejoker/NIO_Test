package com.liu.test;

import java.nio.CharBuffer;

/**
 * Created by hadoop on 2016/12/18 0018.
 */
public class BufferFillDrain {
    private static int index = 0;
    private static String[] words = {
            "A random string value",
            "The product of an infinite number of monekeys",
            "Hey hey we are  the monekeys",
            "Hello sorry i am poor in English",
            "Goodbye mylover ,new road is coming ",
            "I have to go "
    };

    public static void drainBuffer(CharBuffer buffer) {
        while (buffer.hasRemaining())
            System.out.print(buffer.get());
        System.out.println(" ");
    }

    public static boolean fillBuffer(CharBuffer buffer) {
        if (index > buffer.length())
            return false;
        String vocabulary = words[index++];
        for (int i = 0; i < vocabulary.length(); ++i)
            buffer.put(vocabulary.charAt(i));
        return true;
    }

    public static void main(String[] args) {
        CharBuffer buffer = CharBuffer.allocate(2000);
        while (fillBuffer(buffer)) {
            buffer.flip();
            drainBuffer(buffer);
            buffer.clear();
        }
    }
}
