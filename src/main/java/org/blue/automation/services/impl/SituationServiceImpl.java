package org.blue.automation.services.impl;

import org.blue.automation.entities.ModeBase;
import org.blue.automation.entities.SituationBase;
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
    private ModeBase currentModeBase;

    public SituationServiceImpl(ModeService modeService) {
        this.modeService = modeService;
    }

    @Override
    public ArrayList<SituationBase> selectAllSituations(String modeName) {
        ModeBase modeBase = modeService.selectModeByName(modeName);
        if(modeBase == null) return null;
        currentModeBase = modeBase;
        return currentModeBase.getSituationList();
    }

    @Override
    public SituationBase selectSituationByName(String name) {
        if(currentModeBase == null || StringUtil.isWrong(name)) return null;
        ArrayList<SituationBase> situationList = currentModeBase.getSituationList();
        if(situationList == null || situationList.size() < 1) return null;
        for (SituationBase originSituation : situationList) {
            if(originSituation.getName().equals(name)) return originSituation;
        }
        return null;
    }

    @Override
    public boolean addSituation(SituationBase situation) {
        if(currentModeBase == null || situation == null || StringUtil.isWrong(situation.getName())) return false;
        ArrayList<SituationBase> situationList = currentModeBase.getSituationList();
        if(situationList == null) return false;
        //如果名称相同则更新情景
        if(situationList.size() > 0){
            for (SituationBase originSituation : situationList) {
                if(originSituation.getName().equals(situation.getName())) return updateSituation(situation);
            }
        }
        currentModeBase.getSituationList().add(situation);
        return modeService.updateMode(currentModeBase);
    }

    @Override
    public boolean updateSituation(SituationBase situation) {
        if(currentModeBase == null || situation == null || StringUtil.isWrong(situation.getName())) return false;
        ArrayList<SituationBase> situationList = currentModeBase.getSituationList();
        if(situationList == null || situationList.size() < 1) return false;
        for (int i = 0; i < situationList.size(); ++i) {
            //名称相同则更新当前情景
            if(situationList.get(i).getName().equals(situation.getName())){
                currentModeBase.getSituationList().set(i, situation);
                return modeService.updateMode(currentModeBase);
            }
        }
        return false;
    }

    @Override
    public boolean deleteSituationByName(String name) {
        if(currentModeBase == null || StringUtil.isWrong(name)) return false;
        if(currentModeBase.getSituationList() == null || currentModeBase.getSituationList().size() < 1) return false;
        return currentModeBase.getSituationList().removeIf(situationProperty -> situationProperty.getName().equals(name))
                && modeService.updateMode(currentModeBase);
    }

}
