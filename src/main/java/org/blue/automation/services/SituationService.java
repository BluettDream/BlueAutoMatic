package org.blue.automation.services;

import org.blue.automation.entities.Mode;
import org.blue.automation.entities.Situation;

import java.util.ArrayList;

public interface SituationService {

    ArrayList<Situation> getAllSituationsByName(String modeName);

    boolean addSituation(Situation situation);

    boolean deleteSituation(Situation situation);

    boolean updateSituation(Situation newSituation);

    boolean isCompliance(Situation situation);

    boolean isExisted(Situation situation);

}
