package org.blue.automation.entities.enums;

public enum PathEnum {
    ROOT(System.getProperty("user.dir")),
    JSON(ROOT+"/confFiles/"),
    IMAGE(ROOT+"/images"),
    IMAGE_OUTER(IMAGE+"/outer/");

    private final String path;

    PathEnum(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return this.path;
    }
}
