package org.blue.automation.services.impl;

import org.blue.automation.entities.AdbDevice;
import org.blue.automation.services.AdbService;
import org.blue.automation.utils.CMDUtil;
import org.opencv.core.Point;

import java.util.ArrayList;

/**
 * name: MengHao Tian
 * date: 2022/4/25 11:37
 */
public class AdbServiceImpl implements AdbService {
    private static final CMDUtil CMD_UTIL = CMDUtil.getInstance();

    @Override
    public ArrayList<AdbDevice> getAllDevices() {
        String output = CMD_UTIL.executeCMDCommand(getAdb().append("devices").toString());
        //分割控制台输出语句
        String[] split = output.split("\n");
        //判断当前语句之后是否为设备列表
        boolean isShow = false;
        ArrayList<AdbDevice> deviceArrayList = new ArrayList<>();
        //遍历控制台的每一行输出
        for (String str : split) {
            //如果开始显示设备列表,则进行存储
            if (isShow) {
                //根据\t进行当前行的分割,把设备名称和设备状态分开
                String[] device = str.split("\t");
                //如果分割后的数组大于1个,则成功获取设备
                if (device.length > 1) {
                    //选择设备的状态
                    switch (device[1]) {
                        case "device":
                            deviceArrayList.add(new AdbDevice(device[0], AdbDevice.State.DEVICE));
                            break;
                        case "offline":
                            deviceArrayList.add(new AdbDevice(device[0], AdbDevice.State.OFFLINE));
                            break;
                    }
                }
            }
            //如果当前行含有attached,则下一行开始显示设备列表
            if (str.contains("attached")) isShow = true;
        }
        return deviceArrayList;
    }

    @Override
    public boolean isConnected(AdbDevice adbDevice) {
        return adbDevice.getState() == AdbDevice.State.DEVICE;
    }

    @Override
    public void click(Point clickPoint) {
        CMD_UTIL.executeCMDCommand(
                getAdbShellInput().append("tap").append(" ")
                        .append(clickPoint.x).append(" ").append(clickPoint.y).toString()
        );
    }

    @Override
    public void longClick(Point clickPoint, long delayTime) {
        longSlide(clickPoint, clickPoint, delayTime);
    }

    @Override
    public void slide(Point startPoint, Point endPoint) {
        longSlide(startPoint, endPoint, 50);
    }

    @Override
    public void longSlide(Point startPoint, Point endPoint, long delayTime) {
        CMD_UTIL.executeCMDCommand(
                getAdbShellInput().append("swipe").append(" ")
                        .append(startPoint.x).append(" ").append(startPoint.y).append(" ")
                        .append(endPoint.x).append(" ").append(endPoint.y).append(" ")
                        .append(delayTime).toString()
        );
    }

    @Override
    public void pull(String phoneFile, String computerFile) {
        CMD_UTIL.executeCMDCommand(
                getAdb().append("pull").append(" ")
                        .append(phoneFile).append(" ")
                        .append(computerFile).toString()
        );
    }

    @Override
    public void execOut(String computerFile) {
        CMD_UTIL.executeCMDCommand(
                getAdb().append("exec-out").append(" ")
                        .append("screencap").append(" ")
                        .append("-p").append(" ").append(">").append(" ")
                        .append(computerFile).toString()
        );
    }

    @Override
    public void screenCap(String phoneFile) {
        CMD_UTIL.executeCMDCommand(
                getAdbShell().append("screencap").append(" ")
                        .append("-p").append(" ")
                        .append(phoneFile).toString()
        );
    }

    @Override
    public void captureAndSave(String phoneFile, String computerFile) {
        screenCap(phoneFile);
        pull(phoneFile,computerFile);
    }

    private StringBuilder getAdb() {
        return new StringBuilder().append("adb").append(" ");
    }

    private StringBuilder getAdbShell() {
        return new StringBuilder().append("adb").append(" ")
                .append("shell").append(" ");
    }

    private StringBuilder getAdbShellInput() {
        return getAdbShell().append("input").append(" ");
    }
}
