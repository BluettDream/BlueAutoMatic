package org.blue.automation.entities;

import java.util.ArrayList;

/**
 * name: MengHao Tian
 * date: 2022/4/24 16:09
 */
public class Mode {
    private String name;
    private ArrayList<Situation> situations;

    public Mode() {
    }

    public Mode(String name) {
        this.name = name;
    }

    public Mode(String name, ArrayList<Situation> situations) {
        this.name = name;
        this.situations = situations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Situation> getSituations() {
        return situations;
    }

    public void setSituations(ArrayList<Situation> situations) {
        this.situations = situations;
    }

    @Override
    public String toString() {
        return "Mode{" +
                "name='" + name + '\'' +
                ", situations=" + situations +
                '}';
    }
}
