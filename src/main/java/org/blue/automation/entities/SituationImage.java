package org.blue.automation.entities;

import org.opencv.core.Rect;

/**
 * name: MengHao Tian
 * date: 2022/4/25 21:56
 */
public class SituationImage {
    //图片路径
    private String path;
    //图片大小以及左上角坐标
    private Rect rect;

    public SituationImage() {
    }

    public SituationImage(String path, Rect rect) {
        this.path = path;
        this.rect = rect;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }
}
