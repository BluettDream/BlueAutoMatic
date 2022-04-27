package org.blue.automation.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.blue.automation.entities.Mode;
import org.blue.automation.entities.Situation;
import org.blue.automation.services.ModeService;
import org.blue.automation.services.SituationService;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * name: MengHao Tian
 * date: 2022/4/26 18:41
 */
public class SituationServiceImpl implements SituationService {
    private final ModeService modeService = new ModeServiceImpl();
    private static Mode CURRENT_MODE = null;

    @Override
    public ArrayList<Situation> getAllSituationsByName(String modeName) {
        ArrayList<Mode> allModes = modeService.getAllModes();
        for (Mode originMode : allModes) {
            if (originMode.getName().equals(modeName)) {
                setCurrentMode(originMode);
                return originMode.getSituations();
            }
        }
        return new ArrayList<>();
    }

    @Override
    public boolean addSituation(Situation situation) {
        boolean existed = isExisted(situation);
        if (existed) return false;
        getCurrentMode().getSituations().add(situation);
        return modeService.updateMode(getCurrentMode());
    }

    @Override
    public boolean deleteSituation(Situation situation) {
        boolean existed = isExisted(situation);
        if (!existed) return false;
        if (!getCurrentMode().getSituations().removeIf(originSituation -> originSituation.getName().equals(situation.getName()))) {
            return false;
        }
        return modeService.updateMode(getCurrentMode());
    }

    @Override
    public boolean updateSituation(Situation newSituation) {
        boolean existed = isExisted(newSituation);
        if (!existed) return false;
        ArrayList<Situation> situations = getCurrentMode().getSituations();
        for (int i = 0; i < situations.size(); ++i) {
            if (situations.get(i).getName().equals(newSituation.getName())) {
                situations.set(i, newSituation);
                getCurrentMode().setSituations(situations);
                return modeService.updateMode(getCurrentMode());
            }
        }
        return false;
    }

    @Override
    public boolean isCompliance(Situation situation) {
        return !(StringUtils.isBlank(situation.getName()) || situation.getName().contains(" "));
    }

    @Override
    public boolean isExisted(Situation situation) {
        Mode mode = getCurrentMode();
        if (mode == null || situation == null) return false;
        if (!isCompliance(situation)) return false;
        ArrayList<Situation> situations = mode.getSituations();
        if (situations == null) return false;
        for (Situation temp : situations) {
            if (temp.getName().equals(situation.getName())) return true;
        }
        return false;
    }

    private static Mode getCurrentMode() {
        return CURRENT_MODE;
    }

    private static void setCurrentMode(Mode currentMode) {
        CURRENT_MODE = currentMode;
    }
}
