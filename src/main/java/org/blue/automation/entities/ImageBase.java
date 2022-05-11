package org.blue.automation.entities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.Objects;

/**
 * name: MengHao Tian
 * date: 2022/4/29 22:05
 */
public class ImageBase implements Serializable{
    private static final long serialVersionUID = 4959158909659101095L;

    /**
     * 图像名称
     **/
    private SimpleStringProperty name = new SimpleStringProperty();
    /**
     * 左上角x坐标
     **/
    private SimpleIntegerProperty x = new SimpleIntegerProperty(0);
    /**
     * 左上角y坐标
     **/
    private SimpleIntegerProperty y = new SimpleIntegerProperty(0);
    /**
     * 图像宽度
     **/
    private SimpleIntegerProperty width = new SimpleIntegerProperty(0);
    /**
     * 图像高度
     **/
    private final SimpleIntegerProperty height = new SimpleIntegerProperty(0);

    /**
     * 图像克隆
     **/
    public ImageBase cloneFor(ImageBase cloneObject){
        ImageBase imageBase = new ImageBase();
        imageBase.name = new SimpleStringProperty(cloneObject.getName());
        imageBase.x = new SimpleIntegerProperty(cloneObject.getX());
        imageBase.y = new SimpleIntegerProperty(cloneObject.getY());
        imageBase.width = new SimpleIntegerProperty(cloneObject.getWidth());
        return imageBase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageBase that = (ImageBase) o;
        return Objects.equals(name, that.name) && Objects.equals(x, that.x) && Objects.equals(y, that.y) && Objects.equals(width, that.width) && Objects.equals(height, that.height);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, x, y, width, height);
    }

    @Override
    public String toString() {
        return "ImageProperty{" +
                "name=" + name +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public ImageBase setName(String name) {
        this.name.set(name);
        return this;
    }

    public int getX() {
        return x.get();
    }

    public SimpleIntegerProperty xProperty() {
        return x;
    }

    public ImageBase setX(int x) {
        this.x.set(x);
        return this;
    }

    public int getY() {
        return y.get();
    }

    public SimpleIntegerProperty yProperty() {
        return y;
    }

    public ImageBase setY(int y) {
        this.y.set(y);
        return this;
    }

    public int getWidth() {
        return width.get();
    }

    public SimpleIntegerProperty widthProperty() {
        return width;
    }

    public ImageBase setWidth(int width) {
        this.width.set(width);
        return this;
    }

    public int getHeight() {
        return height.get();
    }

    public SimpleIntegerProperty heightProperty() {
        return height;
    }

    public ImageBase setHeight(int height) {
        this.height.set(height);
        return this;
    }
}
