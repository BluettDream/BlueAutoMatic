package org.blue.automation.entities.enums;

public enum PathEnum {
    ROOT(System.getProperty("user.dir")),
    CONF(ROOT+"/conf/"),
    IMAGE(ROOT+"/images"),
    IMAGE_OUTER(IMAGE+"/outer/"),
    IMAGE_INNER(IMAGE+"/inner/"),
    MODE(CONF+"/mode/"),
    BIN(ROOT+"/bin/");

    private String path;

    PathEnum(String path) {
        this.path = path.replaceAll("\\\\", "/");
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path.replaceAll("\\\\", "/");
    }

    @Override
    public String toString() {
        return this.path;
    }
}
