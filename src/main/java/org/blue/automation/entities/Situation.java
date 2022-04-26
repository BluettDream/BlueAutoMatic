package org.blue.automation.entities;

import org.blue.automation.entities.enums.Action;

import java.math.BigDecimal;

/**
 * name: MengHao Tian
 * date: 2022/4/24 16:09
 */
public class Situation {
    //情景名称
    private String name;
    //情景动作
    private Action action;
    //情景是否需要点击
    private boolean click;
    //情景图片
    private SituationImage image;
    //情景相似度
    private BigDecimal simile;

    public Situation() {
    }

    public Situation(String name) {
        this.name = name;
    }

    public Situation(String name, boolean click, SituationImage image) {
        this.name = name;
        this.click = click;
        this.image = image;
    }

    public Situation(String name, Action action, boolean click, SituationImage image) {
        this.name = name;
        this.action = action;
        this.click = click;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Situation{" +
                "name='" + name + '\'' +
                ", action=" + action +
                ", click=" + click +
                ", image=" + image +
                ", simile=" + simile +
                '}';
    }
}
