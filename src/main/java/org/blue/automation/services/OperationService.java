package org.blue.automation.services;

import org.opencv.core.Point;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public interface OperationService {

    /**
     * 点击功能
     *
     * @param clickPoint 点击位置
     **/
    void click(Point clickPoint) throws InterruptedException;

    /**
     * 长按功能
     *
     * @param clickPoint 长按位置
     * @param delayTime 长按时间(单位ms)
     **/
    void longClick(Point clickPoint,long delayTime) throws InterruptedException;

    /**
     * 随机多次点击
     *
     * @param points 点击的坐标点
     **/
    void multipleClicks(ArrayList<Point> points) throws InterruptedException;

    /**
     * 滑动功能(默认滑动50ms)
     *
     * @param startPoint 滑动起始坐标
     * @param endPoint 滑动结束坐标
     **/
    void slide(Point startPoint,Point endPoint) throws InterruptedException;

    /**
     * 滑动,可控制滑动速度
     *
     * @param startPoint 滑动起始坐标
     * @param endPoint 滑动结束坐标
     * @param delayTime 滑动时间(单位ms)
     **/
    void longSlide(Point startPoint,Point endPoint,long delayTime) throws InterruptedException;

    /**
     * 截屏并保存图片
     *
     * @param computerFile 电脑图片路径
     **/
    void captureAndSave(String computerFile) throws IOException;

    /**
     * 后台运行需要的文件路径
     *
     * @param filePath 文件路径
     **/
    void setFilePath(String filePath) throws IOException;

}
