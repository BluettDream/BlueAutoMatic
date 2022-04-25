package org.blue.automation.entities;

/**
 * name: MengHao Tian
 * date: 2022/4/25 13:55
 */
public class AdbDevice {
    private String name;
    private Statue statue;

    public enum Statue{
        DEVICE,
        OFFLINE
    }

    public AdbDevice() {
    }

    public AdbDevice(String name, Statue statue) {
        this.name = name;
        this.statue = statue;
    }

    public String getName() {
        return name;
    }

    public AdbDevice setName(String name) {
        this.name = name;
        return this;
    }

    public Statue getStatue() {
        return statue;
    }

    public AdbDevice setStatue(Statue statue) {
        this.statue = statue;
        return this;
    }

    @Override
    public String toString() {
        return "AdbDevice{" +
                "name='" + name + '\'' +
                ", statue=" + statue +
                '}';
    }
}
