package org.blue.automation.entities.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.blue.automation.entities.enums.Action;

import java.io.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * name: MengHao Tian
 * date: 2022/4/29 20:28
 */
public class SituationProperty implements Serializable {
    private static final long serialVersionUID = -54614979185635915L;

    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleIntegerProperty priority = new SimpleIntegerProperty(-1);
    private final SimpleObjectProperty<ImageProperty> image = new SimpleObjectProperty<>(new ImageProperty());
    private final SimpleObjectProperty<BigDecimal> lowestSimile = new SimpleObjectProperty<>(new BigDecimal("0.9").setScale(2,BigDecimal.ROUND_HALF_UP));
    private final SimpleBooleanProperty click = new SimpleBooleanProperty(false);
    private final SimpleObjectProperty<Action> action = new SimpleObjectProperty<>();
    private final SimpleBooleanProperty custom = new SimpleBooleanProperty(false);
    private final SimpleObjectProperty<ImageProperty> customImage = new SimpleObjectProperty<>(new ImageProperty());
    @JsonIgnore
    private final SimpleObjectProperty<BigDecimal> realSimile = new SimpleObjectProperty<>();

    public SituationProperty copy() {
        SituationProperty situationProperty = null;
        try {
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bas);
            oos.writeObject(this);
            ByteArrayInputStream bis = new ByteArrayInputStream(bas.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            situationProperty = (SituationProperty) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return situationProperty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SituationProperty that = (SituationProperty) o;
        return Objects.equals(name, that.name) && Objects.equals(priority, that.priority) && Objects.equals(image, that.image) && Objects.equals(lowestSimile, that.lowestSimile) && Objects.equals(click, that.click) && Objects.equals(action, that.action) && Objects.equals(custom, that.custom) && Objects.equals(customImage, that.customImage) && Objects.equals(realSimile, that.realSimile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, priority, image, lowestSimile, click, action, custom, customImage, realSimile);
    }

    @Override
    public String toString() {
        return "SituationProperty{" +
                "name=" + name +
                ", priority=" + priority +
                ", image=" + image +
                ", lowestSimile=" + lowestSimile +
                ", click=" + click +
                ", action=" + action +
                ", custom=" + custom +
                ", customImage=" + customImage +
                ", realSimile=" + realSimile +
                '}';
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SituationProperty setName(String name) {
        this.name.set(name);
        return this;
    }

    public int getPriority() {
        return priority.get();
    }

    public SimpleIntegerProperty priorityProperty() {
        return priority;
    }

    public SituationProperty setPriority(int priority) {
        this.priority.set(priority);
        return this;
    }

    public ImageProperty getImage() {
        return image.get();
    }

    public SimpleObjectProperty<ImageProperty> imageProperty() {
        return image;
    }

    public SituationProperty setImage(ImageProperty image) {
        this.image.set(image);
        return this;
    }

    public boolean isClick() {
        return click.get();
    }

    public SimpleBooleanProperty clickProperty() {
        return click;
    }

    public SituationProperty setClick(boolean click) {
        this.click.set(click);
        return this;
    }

    public Action getAction() {
        return action.get();
    }

    public SimpleObjectProperty<Action> actionProperty() {
        return action;
    }

    public SituationProperty setAction(Action action) {
        this.action.set(action);
        return this;
    }

    public BigDecimal getLowestSimile() {
        return lowestSimile.get();
    }

    public SimpleObjectProperty<BigDecimal> lowestSimileProperty() {
        return lowestSimile;
    }

    public SituationProperty setLowestSimile(BigDecimal lowestSimile) {
        this.lowestSimile.set(lowestSimile);
        return this;
    }

    public BigDecimal getRealSimile() {
        return realSimile.get();
    }

    public SimpleObjectProperty<BigDecimal> realSimileProperty() {
        return realSimile;
    }

    public SituationProperty setRealSimile(BigDecimal realSimile) {
        this.realSimile.set(realSimile);
        return this;
    }

    public ImageProperty getCustomImage() {
        return customImage.get();
    }

    public SimpleObjectProperty<ImageProperty> customImageProperty() {
        return customImage;
    }

    public SituationProperty setCustomImage(ImageProperty customImage) {
        this.customImage.set(customImage);
        return this;
    }

    public boolean isCustom() {
        return custom.get();
    }

    public SimpleBooleanProperty customProperty() {
        return custom;
    }

    public SituationProperty setCustom(boolean custom) {
        this.custom.set(custom);
        return this;
    }
}
