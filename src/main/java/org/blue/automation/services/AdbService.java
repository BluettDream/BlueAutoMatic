package org.blue.automation.services;

import org.blue.automation.entities.AdbDevice;
import org.opencv.core.Point;

import java.util.ArrayList;

public interface AdbService {

    ArrayList<AdbDevice> getAllDevices();

    boolean isConnected(AdbDevice adbDevice);

    void click(Point clickPoint);

    void longClick(Point clickPoint,long delayTime);

    void slide(Point startPoint,Point endPoint);

    void longSlide(Point startPoint,Point endPoint,long delayTime);

    void pull(String phoneFile,String computerFile);

    void execOut(String computerFile);

    void screenCap(String phoneFile);

}
