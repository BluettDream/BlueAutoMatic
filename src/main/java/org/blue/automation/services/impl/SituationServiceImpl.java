package org.blue.automation.services.impl;

import org.blue.automation.entities.Mode;
import org.blue.automation.entities.Situation;
import org.blue.automation.services.ModeService;
import org.blue.automation.services.SituationService;
import org.blue.automation.utils.StringUtil;

import java.util.ArrayList;

/**
 * name: MengHao Tian
 * date: 2022/4/30 13:11
 */
public class SituationServiceImpl implements SituationService {
    private final ModeService modeService;
    private Mode currentMode;

    public SituationServiceImpl(ModeService modeService) {
        this.modeService = modeService;
    }

    @Override
    public ArrayList<Situation> selectAllSituations(String modeName) {
        Mode mode = modeService.selectModeByName(modeName);
        if(mode == null) return null;
        currentMode = mode;
        return currentMode.getSituationList();
    }

    @Override
    public Situation selectSituationByName(String name) {
        if(currentMode == null || StringUtil.isWrong(name)) return null;
        ArrayList<Situation> situationList = currentMode.getSituationList();
        if(situationList == null || situationList.size() < 1) return null;
        for (Situation originSituation : situationList) {
            if(originSituation.getName().equals(name)) return originSituation;
        }
        return null;
    }

    @Override
    public boolean addSituation(Situation situation) {
        if(currentMode == null || situation == null || StringUtil.isWrong(situation.getName())) return false;
        ArrayList<Situation> situationList = currentMode.getSituationList();
        if(situationList == null) return false;
        //如果名称相同则更新情景
        for (Situation originSituation : situationList) {
            if(originSituation.getName().equals(situation.getName())) return updateSituation(situation);
        }
        currentMode.getSituationList().add(situation);
        return modeService.updateMode(currentMode);
    }

    @Override
    public boolean updateSituation(Situation situation) {
        if(currentMode == null || situation == null || StringUtil.isWrong(situation.getName())) return false;
        ArrayList<Situation> situationList = currentMode.getSituationList();
        if(situationList == null || situationList.size() < 1) return false;
        for (int i = 0; i < situationList.size(); ++i) {
            //名称相同则更新当前情景
            if(situationList.get(i).getName().equals(situation.getName())){
                currentMode.getSituationList().set(i, situation);
                return modeService.updateMode(currentMode);
            }
        }
        return false;
    }

    @Override
    public boolean deleteSituationByName(String name) {
        if(currentMode == null || StringUtil.isWrong(name)) return false;
        if(currentMode.getSituationList() == null || currentMode.getSituationList().size() < 1) return false;
        return currentMode.getSituationList().removeIf(situationProperty -> situationProperty.getName().equals(name))
                && modeService.updateMode(currentMode);
    }

}
