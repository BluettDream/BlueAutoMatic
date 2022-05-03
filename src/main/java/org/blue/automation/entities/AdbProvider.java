package org.blue.automation.entities;

/**
 * name: MengHao Tian
 * date: 2022/5/3 17:52
 */
public class AdbProvider {
    private String phoneFilePath;
    private String deviceNumber;

    public String getPhoneFilePath() {
        return phoneFilePath;
    }

    public AdbProvider setPhoneFilePath(String phoneFilePath) {
        this.phoneFilePath = phoneFilePath;
        return this;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public AdbProvider setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
        return this;
    }

    @Override
    public String toString() {
        return "AdbProvider{" +
                "phoneFilePath='" + phoneFilePath + '\'' +
                ", deviceNumber='" + deviceNumber + '\'' +
                '}';
    }
}
