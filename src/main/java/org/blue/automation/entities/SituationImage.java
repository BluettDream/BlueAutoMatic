package org.blue.automation.entities;

import org.opencv.core.Rect;

import java.io.*;
import java.util.Objects;

/**
 * name: MengHao Tian
 * date: 2022/4/25 21:56
 */
public class SituationImage implements Serializable {
    private static final long serialVersionUID = 4216728156561935668L;
    //图片路径
    private String path;
    //图片大小以及左上角坐标
    private Integer x;
    private Integer y;
    private Integer width;
    private Integer height;

    public SituationImage() {
    }

    public SituationImage(String path) {
        this.path = path;
    }

    public SituationImage(String path, Integer x, Integer y) {
        this.path = path;
        this.x = x;
        this.y = y;
    }

    public SituationImage(String path, Integer x, Integer y, Integer width, Integer height) {
        this.path = path;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public SituationImage copy(){
        SituationImage image = null;
        try {
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bas);
            oos.writeObject(this);
            ByteArrayInputStream bis = new ByteArrayInputStream(bas.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            image = (SituationImage) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SituationImage that = (SituationImage) o;
        return Objects.equals(path, that.path) && Objects.equals(x, that.x) && Objects.equals(y, that.y) && Objects.equals(width, that.width) && Objects.equals(height, that.height);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, x, y, width, height);
    }

    @Override
    public String toString() {
        return "SituationImage{" +
                "path='" + path + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
