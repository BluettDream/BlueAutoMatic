package org.blue.automation.entities.vo;

import com.fasterxml.jackson.annotation.JsonGetter;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * name: MengHao Tian
 * date: 2022/4/29 20:14
 */
public class ModeProperty implements Serializable {
    private static final long serialVersionUID = 3847206222193416167L;
    private final SimpleStringProperty name = new SimpleStringProperty();
    private ArrayList<SituationProperty> situationList = new ArrayList<>();

    public ModeProperty copy() {
        ModeProperty modeProperty = null;
        try {
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bas);
            oos.writeObject(this);
            ByteArrayInputStream bis = new ByteArrayInputStream(bas.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            modeProperty = (ModeProperty) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return modeProperty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModeProperty that = (ModeProperty) o;
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

    public ModeProperty setName(String name) {
        this.name.set(name);
        return this;
    }

    public ArrayList<SituationProperty> getSituationList() {
        return situationList;
    }

    public ModeProperty setSituationList(ArrayList<SituationProperty> situationList) {
        this.situationList = situationList;
        return this;
    }
}
