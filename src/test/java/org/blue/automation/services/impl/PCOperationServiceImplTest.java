package org.blue.automation.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.services.OperationService;
import org.junit.jupiter.api.Test;
import org.opencv.core.Point;

import static org.junit.jupiter.api.Assertions.*;

class PCOperationServiceImplTest {
    static final Logger log = LogManager.getLogger(PCOperationServiceImplTest.class);
    private OperationService operationService = new PCOperationServiceImpl();
    private final Point startPoint = new Point(new double[]{30,400});
    private final Point endPoint = new Point(new double[]{500,400});
    private final Point clickPoint = new Point(new double[]{730,500});
    private final long delayTime = 1000;

    @Test
    void click() {
        operationService.click(clickPoint);
    }

    @Test
    void longClick() {
    }

    @Test
    void multipleClicks() {
    }

    @Test
    void slide() {
    }

    @Test
    void longSlide() {
    }

    @Test
    void captureAndSave() {
    }
}