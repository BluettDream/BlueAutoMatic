package org.blue.automation.services;

import org.blue.automation.entities.ModeBase;

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
    ArrayList<ModeBase> selectAllModes();

    /**
     * 根据模式名称查询模式
     *
     * @param name 模式名称
     * @return 模式
     **/
    ModeBase selectModeByName(String name);

    /**
     * 添加模式
     *
     * @param modeBase 模式
     * @return 添加成功为true,失败为false
     **/
    boolean addMode(ModeBase modeBase);

    /**
     * 更新模式
     *
     * @param modeBase 模式
     * @return 更新成功为true,失败为false
     **/
    boolean updateMode(ModeBase modeBase);

    /**
     * 根据模式名称删除模式
     *
     * @param name 模式名称
     * @return 删除成功为true,失败为false
     **/
    boolean deleteModeByName(String name);

}
