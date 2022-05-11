package org.blue.automation.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.joonasvali.naturalmouse.api.MouseMotionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.entities.ImageBase;
import org.blue.automation.services.OperationService;
import org.blue.automation.utils.FileUtil;
import org.opencv.core.Point;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * name: MengHao Tian
 * date: 2022/5/3 18:42
 */
public class PCOperationServiceImpl implements OperationService {
    private final Logger log = LogManager.getLogger(PCOperationServiceImpl.class);
    private final Robot robot = getRobot();
    private int x;
    private int y;
    private int width;
    private int height;

    @Override
    public void click(Point clickPoint) {
        longClick(clickPoint,0);
    }

    @Override
    public void longClick(Point clickPoint, long delayTime) {
        clickPoint.x += x;
        clickPoint.y += y;
        try {
            MouseMotionFactory.getDefault().move((int) clickPoint.x, (int) clickPoint.y);
        } catch (InterruptedException e) {
            log.error("移动鼠标异常:",e);
            Thread.currentThread().interrupt();
        }
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay((int) delayTime);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    @Override
    public void multipleClicks(ArrayList<Point> points) {
        for (Point point : points) {
            longClick(point, (long) (Math.random()*150+50));
        }
    }

    @Override
    public void slide(Point startPoint, Point endPoint) {
        longSlide(startPoint,endPoint,0);
    }

    @Override
    public void longSlide(Point startPoint, Point endPoint, long delayTime) {
        try {
            startPoint.x += x;
            startPoint.y += y;
            MouseMotionFactory.getDefault().move((int) startPoint.x, (int) startPoint.y);
            endPoint.x += x;
            endPoint.y += y;
            robot.delay((int) delayTime);
            MouseMotionFactory.getDefault().move((int) endPoint.x, (int) endPoint.y);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void captureAndSave(String computerFile) {
        BufferedImage image = robot.createScreenCapture(new Rectangle(x, y, width, height));
        try {
            ImageIO.write(image,"png",new FileOutputStream(computerFile));
        } catch (IOException e) {
            log.error("电脑写入文件异常:",e);
        }
    }

    @Override
    public void setFilePath(String filePath) {
        ObjectMapper objectMapper = FileUtil.getInstance().getObjectMapper();
        ImageBase imageBase=null;
        try {
            imageBase = objectMapper.readValue(
                    new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8)
                    , ImageBase.class);
        } catch (IOException e) {
            log.error("电脑json文件读取异常:",e);
        }
        if(imageBase != null){
            this.x = imageBase.getX();
            this.y = imageBase.getY();
            this.height = imageBase.getHeight();
            this.width = imageBase.getWidth();
        }
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
