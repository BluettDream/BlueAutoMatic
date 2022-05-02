package org.blue.automation.entities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.Objects;

/**
 * name: MengHao Tian
 * date: 2022/4/29 22:05
 */
public class ImageInformation implements Serializable{
    private static final long serialVersionUID = 4959158909659101095L;

    /**
     * 图像路径
     **/
    private SimpleStringProperty path = new SimpleStringProperty();
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
    public ImageInformation cloneFor(ImageInformation cloneObject){
        ImageInformation imageInformation = new ImageInformation();
        imageInformation.path = new SimpleStringProperty(cloneObject.getPath());
        imageInformation.x = new SimpleIntegerProperty(cloneObject.getX());
        imageInformation.y = new SimpleIntegerProperty(cloneObject.getY());
        imageInformation.width = new SimpleIntegerProperty(cloneObject.getWidth());
        return imageInformation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageInformation that = (ImageInformation) o;
        return Objects.equals(path, that.path) && Objects.equals(x, that.x) && Objects.equals(y, that.y) && Objects.equals(width, that.width) && Objects.equals(height, that.height);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, x, y, width, height);
    }

    @Override
    public String toString() {
        return "ImageProperty{" +
                "path=" + path +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    public String getPath() {
        return path.get();
    }

    public SimpleStringProperty pathProperty() {
        return path;
    }

    public void setPath(String path) {
        this.path.set(path);
    }

    public int getX() {
        return x.get();
    }

    public SimpleIntegerProperty xProperty() {
        return x;
    }

    public void setX(int x) {
        this.x.set(x);
    }

    public int getY() {
        return y.get();
    }

    public SimpleIntegerProperty yProperty() {
        return y;
    }

    public void setY(int y) {
        this.y.set(y);
    }

    public int getWidth() {
        return width.get();
    }

    public SimpleIntegerProperty widthProperty() {
        return width;
    }

    public void setWidth(int width) {
        this.width.set(width);
    }

    public int getHeight() {
        return height.get();
    }

    public SimpleIntegerProperty heightProperty() {
        return height;
    }

    public void setHeight(int height) {
        this.height.set(height);
    }
}
