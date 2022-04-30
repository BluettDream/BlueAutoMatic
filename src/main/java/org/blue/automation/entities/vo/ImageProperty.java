package org.blue.automation.entities.vo;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.Objects;

/**
 * name: MengHao Tian
 * date: 2022/4/29 22:05
 */
public class ImageProperty implements Serializable{
    private static final long serialVersionUID = 4959158909659101095L;

    private final SimpleStringProperty path = new SimpleStringProperty();
    private final SimpleIntegerProperty x = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty y = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty width = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty height = new SimpleIntegerProperty(0);

    public ImageProperty copy() {
        ImageProperty imageProperty = null;
        try {
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bas);
            oos.writeObject(this);
            ByteArrayInputStream bis = new ByteArrayInputStream(bas.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            imageProperty = (ImageProperty) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return imageProperty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageProperty that = (ImageProperty) o;
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
