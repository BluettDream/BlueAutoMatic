package org.blue.automation.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * name: MengHao Tian
 * date: 2022/4/29 20:14
 */
public class ModeBase implements Serializable {
    private static final long serialVersionUID = 3847206222193416167L;
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleLongProperty runTime = new SimpleLongProperty(3600000);
    private ArrayList<SituationBase> situationList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModeBase modeBase = (ModeBase) o;
        return Objects.equals(name, modeBase.name) && Objects.equals(runTime, modeBase.runTime) && Objects.equals(situationList, modeBase.situationList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, runTime, situationList);
    }

    @Override
    public String toString() {
        return "ModeBase{" +
                "name=" + name +
                ", runTime=" + runTime +
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

    public ModeBase setName(String name) {
        this.name.set(name);
        return this;
    }

    public ArrayList<SituationBase> getSituationList() {
        return situationList;
    }

    public ModeBase setSituationList(ArrayList<SituationBase> situationList) {
        this.situationList = situationList;
        return this;
    }

    public long getRunTime() {
        return runTime.get();
    }

    public SimpleLongProperty runTimeProperty() {
        return runTime;
    }

    public void setRunTime(long runTime) {
        this.runTime.set(runTime);
    }
}
