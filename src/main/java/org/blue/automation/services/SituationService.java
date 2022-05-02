package org.blue.automation.services;

import org.blue.automation.entities.Situation;

import java.util.ArrayList;

public interface SituationService {
    /**
     * 根据模式名称查询当前模式的所有情景
     *
     * @param modeName 模式名称
     * @return 情景列表
     **/
    ArrayList<Situation> selectAllSituations(String modeName);

    /**
     * 根据情景名称返回当前情景
     *
     * @param name 情景名称
     * @return 情景
     **/
    Situation selectSituationByName(String name);

    /**
     * 添加情景,若情景已存在则自动更新
     *
     * @param situation 情景
     * @return 添加成功为true,失败为false
     **/
    boolean addSituation(Situation situation);

    /**
     * 更新情景
     *
     * @param situation 情景
     * @return 更新成功为true,失败为false
     **/
    boolean updateSituation(Situation situation);

    /**
     * 根据情景名称删除情景
     *
     * @param name 情景名称
     * @return 删除成功返回true,失败返回false
     **/
    boolean deleteSituationByName(String name);
}
