package org.blue.automation.entities;

import org.blue.automation.entities.enums.Action;

import java.io.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * name: MengHao Tian
 * date: 2022/4/24 16:09
 */
public class Situation implements Serializable {
    private static final long serialVersionUID = -2382817620238052735L;
    //情景名称
    private String name;
    //情景优先级
    private Integer priority = -1;
    //情景图片
    private SituationImage image;
    //情景是否需要点击
    private boolean click = false;
    //情景动作
    private Action action;
    //情景相似度
    private BigDecimal simile;

    public Situation() {
    }

    public Situation(String name) {
        this.name = name;
    }

    public Situation(String name, SituationImage image, boolean click) {
        this.name = name;
        this.image = image;
        this.click = click;
    }

    public Situation(String name, SituationImage image, boolean click, Action action) {
        this.name = name;
        this.image = image;
        this.click = click;
        this.action = action;
    }

    public Situation(String name, Integer priority, SituationImage image, boolean click) {
        this.name = name;
        this.priority = priority;
        this.image = image;
        this.click = click;
    }

    public Situation(String name, Integer priority, SituationImage image, boolean click, Action action) {
        this.name = name;
        this.priority = priority;
        this.image = image;
        this.click = click;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public boolean isClick() {
        return click;
    }

    public void setClick(boolean click) {
        this.click = click;
    }

    public SituationImage getImage() {
        return image;
    }

    public void setImage(SituationImage image) {
        this.image = image;
    }

    public BigDecimal getSimile() {
        return simile;
    }

    public void setSimile(BigDecimal simile) {
        this.simile = simile;
    }

    public Situation copy() {
        Situation situation = null;
        try {
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bas);
            oos.writeObject(this);
            ByteArrayInputStream bis = new ByteArrayInputStream(bas.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            situation = (Situation) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return situation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Situation situation = (Situation) o;
        return click == situation.click && Objects.equals(name, situation.name) && Objects.equals(priority, situation.priority) && Objects.equals(image, situation.image) && action == situation.action && Objects.equals(simile, situation.simile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, priority, image, click, action, simile);
    }

    @Override
    public String toString() {
        return "Situation{" +
                "name='" + name + '\'' +
                ", priority=" + priority +
                ", image=" + image +
                ", click=" + click +
                ", action=" + action +
                ", simile=" + simile +
                '}';
    }
}
