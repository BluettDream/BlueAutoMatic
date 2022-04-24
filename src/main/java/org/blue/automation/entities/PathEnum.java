package org.blue.automation.entities;

public enum PathEnum {
    ROOT(System.getProperty("user.dir")),
    JSON(ROOT+"/confFiles/"),
    IMAGE(ROOT+"/images/");

    private final String path;

    PathEnum(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return this.path;
    }
}
