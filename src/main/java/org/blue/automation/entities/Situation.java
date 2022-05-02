package org.blue.automation.entities;

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
public class Situation implements Serializable{
    private static final long serialVersionUID = -54614979185635915L;

    /**
     * 情景名称
     **/
    private SimpleStringProperty name = new SimpleStringProperty();
    /**
     * 优先级
     **/
    private SimpleIntegerProperty priority = new SimpleIntegerProperty(-1);
    /**
     * 图像信息
     **/
    private SimpleObjectProperty<ImageInformation> image = new SimpleObjectProperty<>(new ImageInformation());
    /**
     * 最低相似度
     **/
    private SimpleObjectProperty<BigDecimal> lowestSimile = new SimpleObjectProperty<>(new BigDecimal("0.9").setScale(2,BigDecimal.ROUND_HALF_UP));
    /**
     * 是否需要点击
     **/
    private SimpleBooleanProperty click = new SimpleBooleanProperty(false);
    /**
     * 点击的具体行动
     **/
    private SimpleObjectProperty<Action> action = new SimpleObjectProperty<>();
    /**
     * 是否自定义点击位置
     **/
    private SimpleBooleanProperty custom = new SimpleBooleanProperty(false);
    /**
     * 自定义的位置
     **/
    private SimpleObjectProperty<ImageInformation> customImage = new SimpleObjectProperty<>(new ImageInformation());
    /**
     * 真实的相似度
     **/
    @JsonIgnore
    private SimpleObjectProperty<BigDecimal> realSimile = new SimpleObjectProperty<>();

    public Situation cloneFor(Situation cloneObject){
        Situation situation = new Situation();
        situation.name = new SimpleStringProperty(cloneObject.getName());
        situation.priority = new SimpleIntegerProperty(cloneObject.getPriority());
        situation.image = new SimpleObjectProperty<>(cloneObject.getImage().cloneFor(cloneObject.getImage()));
        situation.lowestSimile = new SimpleObjectProperty<>(cloneObject.getLowestSimile());
        situation.click = new SimpleBooleanProperty(cloneObject.isClick());
        situation.action = new SimpleObjectProperty<>(cloneObject.getAction());
        situation.custom = new SimpleBooleanProperty(cloneObject.isCustom());
        situation.customImage = new SimpleObjectProperty<>(cloneObject.getCustomImage().cloneFor(cloneObject.getCustomImage()));
        situation.realSimile = new SimpleObjectProperty<>(cloneObject.getRealSimile());
        return situation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Situation that = (Situation) o;
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

    public Situation setName(String name) {
        this.name.set(name);
        return this;
    }

    public int getPriority() {
        return priority.get();
    }

    public SimpleIntegerProperty priorityProperty() {
        return priority;
    }

    public Situation setPriority(int priority) {
        this.priority.set(priority);
        return this;
    }

    public ImageInformation getImage() {
        return image.get();
    }

    public SimpleObjectProperty<ImageInformation> imageProperty() {
        return image;
    }

    public Situation setImage(ImageInformation image) {
        this.image.set(image);
        return this;
    }

    public boolean isClick() {
        return click.get();
    }

    public SimpleBooleanProperty clickProperty() {
        return click;
    }

    public Situation setClick(boolean click) {
        this.click.set(click);
        return this;
    }

    public Action getAction() {
        return action.get();
    }

    public SimpleObjectProperty<Action> actionProperty() {
        return action;
    }

    public Situation setAction(Action action) {
        this.action.set(action);
        return this;
    }

    public BigDecimal getLowestSimile() {
        return lowestSimile.get();
    }

    public SimpleObjectProperty<BigDecimal> lowestSimileProperty() {
        return lowestSimile;
    }

    public Situation setLowestSimile(BigDecimal lowestSimile) {
        this.lowestSimile.set(lowestSimile);
        return this;
    }

    public BigDecimal getRealSimile() {
        return realSimile.get();
    }

    public SimpleObjectProperty<BigDecimal> realSimileProperty() {
        return realSimile;
    }

    public Situation setRealSimile(BigDecimal realSimile) {
        this.realSimile.set(realSimile);
        return this;
    }

    public ImageInformation getCustomImage() {
        return customImage.get();
    }

    public SimpleObjectProperty<ImageInformation> customImageProperty() {
        return customImage;
    }

    public Situation setCustomImage(ImageInformation customImage) {
        this.customImage.set(customImage);
        return this;
    }

    public boolean isCustom() {
        return custom.get();
    }

    public SimpleBooleanProperty customProperty() {
        return custom;
    }

    public Situation setCustom(boolean custom) {
        this.custom.set(custom);
        return this;
    }
}
