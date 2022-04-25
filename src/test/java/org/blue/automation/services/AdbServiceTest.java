package org.blue.automation.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.entities.AdbDevice;
import org.blue.automation.entities.PathEnum;
import org.blue.automation.services.impl.AdbServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.opencv.core.Point;

import java.util.ArrayList;

class AdbServiceTest {
    private final static Logger log = LogManager.getLogger(AdbServiceTest.class);
    private final static AdbService adbService = new AdbServiceImpl();
    private final Point startPoint = new Point(new double[]{30,400});
    private final Point endPoint = new Point(new double[]{500,400});
    private final Point clickPoint = new Point(new double[]{730,1485});
    private final long delayTime = 1000;

    @BeforeAll
    static void isConnected() {
        boolean connected = adbService.isConnected(adbService.getAllDevices().get(0));
        log.debug(connected);
    }

    @Test
    void getAllDevices() {
        ArrayList<AdbDevice> devices = adbService.getAllDevices();
        log.debug(devices);
    }

    @Test
    void click() {
        adbService.click(clickPoint);
    }

    @Test
    void longClick() {
        adbService.longClick(clickPoint,delayTime);
    }

    @Test
    void slide() {
        adbService.slide(startPoint,endPoint);
    }

    @Test
    void longSlide() {
        adbService.longSlide(startPoint,endPoint,delayTime);
    }

    @Test
    void execOut() {
        adbService.execOut(PathEnum.IMAGE_OUTER+"test.png");
    }

    @Test
    void screenCap() {
        adbService.screenCap("/sdcard/blue_test.png");
    }

    @Test
    void pull() {
        adbService.pull("/sdcard/blue_test.png", PathEnum.IMAGE_OUTER+"test.png");
    }

}