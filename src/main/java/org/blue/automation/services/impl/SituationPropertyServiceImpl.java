package org.blue.automation.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.entities.vo.ModeProperty;
import org.blue.automation.entities.vo.SituationProperty;
import org.blue.automation.services.ModePropertyService;
import org.blue.automation.services.SituationPropertyService;
import org.blue.automation.utils.StringUtil;

import java.util.ArrayList;

/**
 * name: MengHao Tian
 * date: 2022/4/30 13:11
 */
public class SituationPropertyServiceImpl implements SituationPropertyService {
    private static final Logger log = LogManager.getLogger(SituationPropertyServiceImpl.class);
    private final ModePropertyService modePropertyService;
    private ModeProperty currentMode;

    public SituationPropertyServiceImpl(ModePropertyService modePropertyService) {
        this.modePropertyService = modePropertyService;
    }

    @Override
    public ArrayList<SituationProperty> selectAllSituationProperties(String modeName) {
        ModeProperty modeProperty = modePropertyService.selectModePropertyByName(modeName);
        if(modeProperty == null) return null;
        currentMode = modeProperty;
        return currentMode.getSituationList();
    }

    @Override
    public SituationProperty selectSituationPropertyByName(String name) {
        if(currentMode == null || StringUtil.isWrong(name)) return null;
        ArrayList<SituationProperty> situationList = currentMode.getSituationList();
        if(situationList == null || situationList.size() < 1) return null;
        for (SituationProperty originSituation : situationList) {
            if(originSituation.getName().equals(name)) return originSituation;
        }
        return null;
    }

    @Override
    public boolean addSituationProperty(SituationProperty situationProperty) {
        if(currentMode == null || situationProperty == null || StringUtil.isWrong(situationProperty.getName())) return false;
        ArrayList<SituationProperty> situationList = currentMode.getSituationList();
        if(situationList == null) return false;
        //如果名称相同则添加失败
        for (SituationProperty originSituation : situationList) {
            if(originSituation.getName().equals(situationProperty.getName())) return false;
        }
        currentMode.getSituationList().add(situationProperty);
        return modePropertyService.updateModeProperty(currentMode);
    }

    @Override
    public boolean updateSituationProperty(SituationProperty situationProperty) {
        if(currentMode == null || situationProperty == null || StringUtil.isWrong(situationProperty.getName())) return false;
        ArrayList<SituationProperty> situationList = currentMode.getSituationList();
        if(situationList == null || situationList.size() < 1) return false;
        for (int i = 0; i < situationList.size(); ++i) {
            //名称相同则更新当前情景
            if(situationList.get(i).getName().equals(situationProperty.getName())){
                currentMode.getSituationList().set(i,situationProperty);
                return modePropertyService.updateModeProperty(currentMode);
            }
        }
        return false;
    }

    @Override
    public boolean deleteSituationPropertyByName(String name) {
        if(currentMode == null || StringUtil.isWrong(name)) return false;
        if(currentMode.getSituationList() == null || currentMode.getSituationList().size() < 1) return false;
        return currentMode.getSituationList().removeIf(situationProperty -> situationProperty.getName().equals(name))
                && modePropertyService.updateModeProperty(currentMode);
    }

}
