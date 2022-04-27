package org.blue.automation.entities;

import java.util.ArrayList;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mode mode = (Mode) o;
        return Objects.equals(name, mode.name) && Objects.equals(situations, mode.situations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, situations);
    }

    @Override
    public String toString() {
        return "Mode{" +
                "name='" + name + '\'' +
                ", situations=" + situations +
                '}';
    }
}
