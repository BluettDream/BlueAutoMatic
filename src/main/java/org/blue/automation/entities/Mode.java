package org.blue.automation.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * name: MengHao Tian
 * date: 2022/4/29 20:14
 */
public class Mode implements Serializable {
    private static final long serialVersionUID = 3847206222193416167L;
    private final SimpleStringProperty name = new SimpleStringProperty();
    private ArrayList<Situation> situationList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mode that = (Mode) o;
        return Objects.equals(name, that.name) && Objects.equals(situationList, that.situationList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, situationList);
    }

    @Override
    public String toString() {
        return "ModeProperty{" +
                "name=" + name +
                ", situationList=" + situationList +
                '}';
    }

    @JsonGetter
    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public Mode setName(String name) {
        this.name.set(name);
        return this;
    }

    public ArrayList<Situation> getSituationList() {
        return situationList;
    }

    public Mode setSituationList(ArrayList<Situation> situationList) {
        this.situationList = situationList;
        return this;
    }
}
