package org.blue.automation.services;

import org.blue.automation.entities.AdbDevice;
import org.opencv.core.Point;

import java.util.ArrayList;

public interface AdbService {

    /**
     * 获取ADB连接的所有设备
     *
     * @return AdbDevice列表
     **/
    ArrayList<AdbDevice> getAllDevices();

    /**
     * 判断当前设备是否成功连接
     *
     * @param adbDevice adb设备对象
     * @return adb状态如果是device则返回true,否则返回false
     **/
    boolean isConnected(AdbDevice adbDevice);

    /**
     * 手机屏幕点击功能
     *
     * @param clickPoint 点击的坐标点
     **/
    void click(Point clickPoint);

    /**
     * 手机屏幕长按功能
     *
     * @param clickPoint 长按的坐标点
     * @param delayTime 长按时间(单位ms)
     **/
    void longClick(Point clickPoint,long delayTime);

    /**
     * 手机屏幕滑动功能(默认滑动50ms)
     *
     * @param startPoint 滑动起始坐标
     * @param endPoint 滑动结束坐标
     **/
    void slide(Point startPoint,Point endPoint);

    /**
     * 手机屏幕滑动,可控制滑动的速度
     *
     * @param startPoint 滑动起始坐标
     * @param endPoint 滑动结束坐标
     * @param delayTime 滑动时间(单位ms)
     **/
    void longSlide(Point startPoint,Point endPoint,long delayTime);

    /**
     * 手机文件推送到电脑
     *
     * @param phoneFile 手机文件路径
     * @param computerFile 电脑文件要保存的路径
     **/
    void pull(String phoneFile,String computerFile);

    /**
     * 手机截屏并将图片推送到电脑(新版ADB可用),可以不需要在手机端保存文件
     *
     * @param computerFile 电脑文件要保存的路径
     **/
    void execOut(String computerFile);

    /**
     * 手机截屏
     *
     * @param phoneFile 截屏图片保存的路径(例:/sdcard/xxx.png)
     **/
    void screenCap(String phoneFile);

    /**
     * 截屏并保存图片到电脑
     *
     * @param phoneFile 手机图片路径
     * @param computerFile 电脑图片路劲
     **/
    void captureAndSave(String phoneFile,String computerFile);

}
