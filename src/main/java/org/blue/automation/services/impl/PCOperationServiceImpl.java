package org.blue.automation.services.impl;

import com.github.joonasvali.naturalmouse.api.MouseMotionFactory;
import org.blue.automation.services.OperationService;
import org.opencv.core.Point;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.ArrayList;

/**
 * name: MengHao Tian
 * date: 2022/5/3 18:42
 */
public class PCOperationServiceImpl implements OperationService {
    private final Robot robot = getRobot();

    @Override
    public void click(Point clickPoint) {
        try {
            MouseMotionFactory.getDefault().move((int) clickPoint.x, (int) clickPoint.y);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(50);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    @Override
    public void longClick(Point clickPoint, long delayTime) {

    }

    @Override
    public void multipleClicks(ArrayList<Point> points) {

    }

    @Override
    public void slide(Point startPoint, Point endPoint) {

    }

    @Override
    public void longSlide(Point startPoint, Point endPoint, long delayTime) {

    }

    @Override
    public void captureAndSave(String computerFile) {

    }

    @Override
    public void setFilePath(String filePath) {

    }

    private Robot getRobot(){
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return robot;
    }
}
