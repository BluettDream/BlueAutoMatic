package org.blue.automation.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.*;
import org.blue.automation.entities.enums.Action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * name: MengHao Tian
 * date: 2022/5/4 12:31
 */
public class SituationBase implements Serializable {
    private static final long serialVersionUID = 8438026337136155490L;

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
    private SimpleObjectProperty<ImageBase> image = new SimpleObjectProperty<>(new ImageBase());
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
    private SimpleObjectProperty<ImageBase> customImage = new SimpleObjectProperty<>(new ImageBase());
    /**
     * 最大延迟时间
     **/
    private SimpleDoubleProperty maxDelayTime = new SimpleDoubleProperty(30000.0);
    /**
     * 自定义坐标是否和图片关联
     **/
    private SimpleBooleanProperty relation = new SimpleBooleanProperty(false);
    /**
     * 真实相似度
     **/
    @JsonIgnore
    private SimpleObjectProperty<BigDecimal> realSimile = new SimpleObjectProperty<>();

    public SituationBase cloneFor(SituationBase cloneObject){
        SituationBase situation = new SituationBase();
        situation.name = new SimpleStringProperty(cloneObject.getName());
        situation.priority = new SimpleIntegerProperty(cloneObject.getPriority());
        situation.image = new SimpleObjectProperty<>(cloneObject.getImage().cloneFor(cloneObject.getImage()));
        situation.lowestSimile = new SimpleObjectProperty<>(cloneObject.getLowestSimile());
        situation.click = new SimpleBooleanProperty(cloneObject.isClick());
        situation.action = new SimpleObjectProperty<>(cloneObject.getAction());
        situation.custom = new SimpleBooleanProperty(cloneObject.isCustom());
        situation.customImage = new SimpleObjectProperty<>(cloneObject.getCustomImage().cloneFor(cloneObject.getCustomImage()));
        situation.maxDelayTime = new SimpleDoubleProperty(cloneObject.getMaxDelayTime());
        situation.realSimile = new SimpleObjectProperty<>(cloneObject.getRealSimile());
        situation.relation = new SimpleBooleanProperty(cloneObject.isRelation());
        return situation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SituationBase that = (SituationBase) o;
        return Objects.equals(name, that.name) && Objects.equals(priority, that.priority) && Objects.equals(image, that.image) && Objects.equals(lowestSimile, that.lowestSimile) && Objects.equals(click, that.click) && Objects.equals(action, that.action) && Objects.equals(custom, that.custom) && Objects.equals(customImage, that.customImage) && Objects.equals(maxDelayTime, that.maxDelayTime) && Objects.equals(relation, that.relation) && Objects.equals(realSimile, that.realSimile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, priority, image, lowestSimile, click, action, custom, customImage, maxDelayTime, relation, realSimile);
    }

    @Override
    public String toString() {
        return "SituationBase{" +
                "name=" + name +
                ", priority=" + priority +
                ", image=" + image +
                ", lowestSimile=" + lowestSimile +
                ", click=" + click +
                ", action=" + action +
                ", custom=" + custom +
                ", customImage=" + customImage +
                ", maxDelayTime=" + maxDelayTime +
                ", relation=" + relation +
                ", realSimile=" + realSimile +
                '}';
    }

    public boolean isRelation() {
        return relation.get();
    }

    public SimpleBooleanProperty relationProperty() {
        return relation;
    }

    public void setRelation(boolean relation) {
        this.relation.set(relation);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SituationBase setName(String name) {
        this.name.set(name);
        return this;
    }

    public int getPriority() {
        return priority.get();
    }

    public SimpleIntegerProperty priorityProperty() {
        return priority;
    }

    public SituationBase setPriority(int priority) {
        this.priority.set(priority);
        return this;
    }

    public ImageBase getImage() {
        return image.get();
    }

    public SimpleObjectProperty<ImageBase> imageProperty() {
        return image;
    }

    public SituationBase setImage(ImageBase image) {
        this.image.set(image);
        return this;
    }

    public boolean isClick() {
        return click.get();
    }

    public SimpleBooleanProperty clickProperty() {
        return click;
    }

    public SituationBase setClick(boolean click) {
        this.click.set(click);
        return this;
    }

    public Action getAction() {
        return action.get();
    }

    public SimpleObjectProperty<Action> actionProperty() {
        return action;
    }

    public SituationBase setAction(Action action) {
        this.action.set(action);
        return this;
    }

    public BigDecimal getLowestSimile() {
        return lowestSimile.get();
    }

    public SimpleObjectProperty<BigDecimal> lowestSimileProperty() {
        return lowestSimile;
    }

    public SituationBase setLowestSimile(BigDecimal lowestSimile) {
        this.lowestSimile.set(lowestSimile);
        return this;
    }

    public BigDecimal getRealSimile() {
        return realSimile.get();
    }

    public SimpleObjectProperty<BigDecimal> realSimileProperty() {
        return realSimile;
    }

    public SituationBase setRealSimile(BigDecimal realSimile) {
        this.realSimile.set(realSimile);
        return this;
    }

    public ImageBase getCustomImage() {
        return customImage.get();
    }

    public SimpleObjectProperty<ImageBase> customImageProperty() {
        return customImage;
    }

    public SituationBase setCustomImage(ImageBase customImage) {
        this.customImage.set(customImage);
        return this;
    }

    public double getMaxDelayTime() {
        return maxDelayTime.get();
    }

    public SimpleDoubleProperty maxDelayTimeProperty() {
        return maxDelayTime;
    }

    public SituationBase setMaxDelayTime(double maxDelayTime) {
        this.maxDelayTime.set(maxDelayTime);
        return this;
    }

    public boolean isCustom() {
        return custom.get();
    }

    public SimpleBooleanProperty customProperty() {
        return custom;
    }

    public SituationBase setCustom(boolean custom) {
        this.custom.set(custom);
        return this;
    }
}
