package com.liu.test;

import java.io.IOException;
import java.nio.channels.Selector;

/**
 * Created by hadoop on 2016/12/14 0014.
 */
public class Selector_Test {
    void Selector_Test(){
        try {
            Selector selector = Selector.open();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
