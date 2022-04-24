package org.blue.automation.services;

import org.blue.automation.entities.Mode;

import java.util.ArrayList;

public interface ModeService {

    /**
     * 获取全部的模式
     *
     * @return 模式列表
     **/
    ArrayList<Mode> getAllModes();

    /**
     * 根据模式名称获取模式
     *
     * @param name 模式名称
     * @return 模式存在返回模式,不存在返回null
     **/
    Mode getModeByName(String name);

    /**
     * 添加模式
     *
     * @param mode 将要添加的模式
     * @return 添加成功返回true,失败返回false
     **/
    boolean addMode(Mode mode);

    /**
     * 更新模式
     *
     * @param mode 将要更新的模式
     * @return 更新成功返回true,失败返回false
     **/
    boolean updateMode(Mode mode);

    /**
     * 删除模式
     *
     * @param mode 将要删除的模式
     * @return 删除成功返回true,失败返回false
     **/
    boolean deleteMode(Mode mode);

    /**
     * 设置json文件的名称
     *
     * @param fileName json文件名称(例:test.png)
     **/
    void setFileName(String fileName);

}
