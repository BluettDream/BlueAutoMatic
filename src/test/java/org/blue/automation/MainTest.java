package org.blue.automation;

import org.junit.jupiter.api.Test;

import java.awt.*;

/**
 * name: MengHao Tian
 * date: 2022/5/11 12:42
 */
public class MainTest {

    @Test
    void test() throws InterruptedException {
        while (true){
            Point point = MouseInfo.getPointerInfo().getLocation();
            System.out.println(point.x+","+point.y);
            Thread.sleep(1000);
        }

    }
}
