package org.blue.automation.services;

import org.blue.automation.entities.vo.ModeProperty;
import org.blue.automation.entities.vo.SituationProperty;

import java.util.ArrayList;

public interface SituationPropertyService {
    ArrayList<SituationProperty> selectAllSituationProperties(String modeName);

    SituationProperty selectSituationPropertyByName(String name);

    boolean addSituationProperty(SituationProperty situationProperty);

    boolean updateSituationProperty(SituationProperty situationProperty);

    boolean deleteSituationPropertyByName(String name);
}
