package org.blue.automation.services;

import org.blue.automation.entities.Mode;

import java.util.ArrayList;

/**
 * name: MengHao Tian
 * date: 2022/4/29 22:15
 */
public interface ModeService {

    /**
     * 设置模式文件路径
     *
     * @param fileName 文件路径
     **/
    void setFileName(String fileName);

    /**
     * 获取所有的模式
     *
     * @return 模式列表
     **/
    ArrayList<Mode> selectAllModes();

    /**
     * 根据模式名称查询模式
     *
     * @param name 模式名称
     * @return 模式
     **/
    Mode selectModeByName(String name);

    /**
     * 添加模式
     *
     * @param mode 模式
     * @return 添加成功为true,失败为false
     **/
    boolean addMode(Mode mode);

    /**
     * 更新模式
     *
     * @param mode 模式
     * @return 更新成功为true,失败为false
     **/
    boolean updateMode(Mode mode);

    /**
     * 根据模式名称删除模式
     *
     * @param name 模式名称
     * @return 删除成功为true,失败为false
     **/
    boolean deleteModeByName(String name);

}
