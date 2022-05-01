package org.blue.automation.services;

import org.blue.automation.entities.Situation;

import java.util.ArrayList;

public interface SituationService {
    ArrayList<Situation> selectAllSituations(String modeName);

    Situation selectSituationByName(String name);

    boolean addSituation(Situation situation);

    boolean updateSituation(Situation situation);

    boolean deleteSituationByName(String name);
}
