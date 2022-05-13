package org.blue.automation.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.entities.enums.PathEnum;
import org.blue.automation.services.OperationService;
import org.junit.jupiter.api.Test;
import org.opencv.core.Point;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AdbOperationServiceImplTest {
    private final static Logger log = LogManager.getLogger(AdbOperationServiceImplTest.class);
    private final static OperationService operationService = new AdbOperationServiceImpl();
    private final Point startPoint = new Point(new double[]{30,400});
    private final Point endPoint = new Point(new double[]{500,400});
    private final Point clickPoint = new Point(new double[]{730,1485});
    private final long delayTime = 1000;
    @Test
    void click() throws InterruptedException {
        operationService.click(clickPoint);
    }

    @Test
    void longClick() throws InterruptedException {
        operationService.longClick(clickPoint,delayTime);
    }

    @Test
    void slide() throws InterruptedException {
        operationService.slide(startPoint,endPoint);
    }

    @Test
    void longSlide() throws InterruptedException {
        operationService.longSlide(startPoint,endPoint,delayTime);
    }

    @Test
    void captureAndSave() throws IOException {
        operationService.captureAndSave(PathEnum.IMAGE_OUTER+"main.png");
    }
}