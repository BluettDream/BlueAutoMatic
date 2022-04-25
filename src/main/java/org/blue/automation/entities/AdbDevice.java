package org.blue.automation.entities;

/**
 * name: MengHao Tian
 * date: 2022/4/25 13:55
 */
public class AdbDevice {
    private String name;
    private State state;

    public enum State{
        DEVICE,
        OFFLINE
    }

    public AdbDevice() {
    }

    public AdbDevice(String name, State state) {
        this.name = name;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public AdbDevice setName(String name) {
        this.name = name;
        return this;
    }

    public State getState() {
        return state;
    }

    public AdbDevice setState(State state) {
        this.state = state;
        return this;
    }

    @Override
    public String toString() {
        return "AdbDevice{" +
                "name='" + name + '\'' +
                ", state=" + state +
                '}';
    }
}
