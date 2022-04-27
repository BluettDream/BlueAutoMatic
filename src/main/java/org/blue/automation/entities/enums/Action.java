package org.blue.automation.entities.enums;

public enum Action {
    CLICK("单击"),
    LONG_CLICK("长按"),
    RANDOM_CLICK("随机多次点击"),
    SLIDE("滑动"),
    LONG_SLIDE("缓慢滑动");

    private String description;

    Action(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
