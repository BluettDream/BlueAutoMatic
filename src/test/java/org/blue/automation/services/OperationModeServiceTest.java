package org.blue.automation.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.entities.AdbDevice;
import org.blue.automation.entities.enums.PathEnum;
import org.blue.automation.services.impl.AdbOperationServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.opencv.core.Point;

import java.util.ArrayList;

class OperationModeServiceTest {
    private final static Logger log = LogManager.getLogger(OperationModeServiceTest.class);
    private final static OperationModeService OPERATION_MODE_SERVICE = new AdbOperationServiceImpl();
    private final Point startPoint = new Point(new double[]{30,400});
    private final Point endPoint = new Point(new double[]{500,400});
    private final Point clickPoint = new Point(new double[]{730,1485});
    private final long delayTime = 1000;

    @BeforeAll
    static void isConnected() {
        boolean connected = OPERATION_MODE_SERVICE.isConnected(OPERATION_MODE_SERVICE.getAllDevices().get(0));
        log.debug("设备是否连接:{}",connected);
    }

    @Test
    void getAllDevices() {
        ArrayList<AdbDevice> devices = OPERATION_MODE_SERVICE.getAllDevices();
        log.debug(devices);
    }

    @Test
    void click() {
        OPERATION_MODE_SERVICE.click(clickPoint);
    }

    @Test
    void longClick() {
        OPERATION_MODE_SERVICE.longClick(clickPoint,delayTime);
    }

    @Test
    void slide() {
        OPERATION_MODE_SERVICE.slide(startPoint,endPoint);
    }

    @Test
    void longSlide() {
        OPERATION_MODE_SERVICE.longSlide(startPoint,endPoint,delayTime);
    }

    @Test
    void execOut() {
        OPERATION_MODE_SERVICE.execOut(PathEnum.IMAGE_OUTER+"test.png");
    }

    @Test
    void screenCap() {
        OPERATION_MODE_SERVICE.screenCap("/sdcard/blue_test.png");
    }

    @Test
    void pull() {
        OPERATION_MODE_SERVICE.pull("/sdcard/blue_test.png", PathEnum.IMAGE_OUTER+"test.png");
    }

    @Test
    void captureAndSave() {
        OPERATION_MODE_SERVICE.captureAndSave("/sdcard/blue_main.png",PathEnum.IMAGE_OUTER+"main.png");
    }
}