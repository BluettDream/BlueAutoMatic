package org.blue.automation.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.entities.Mode;
import org.blue.automation.entities.Situation;
import org.blue.automation.services.impl.ModeServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ModeServiceTest {
    private static final Logger log = LogManager.getLogger(ModeServiceTest.class);
    private final ModeService modeService = new ModeServiceImpl();

    @Test
    void getAllModes() {
        modeService.getAllModes().forEach(log::info);
    }

    @Test
    void getModeByName() {
        Mode mode = modeService.getModeByName("御魂模式");
        log.info(mode);
    }

    @Test
    void addMode() {
        Mode mode = new Mode();
        mode.setName("测试模式4");
        ArrayList<Situation> situations = new ArrayList<>();
        situations.add(new Situation("情形1"));
        situations.add(new Situation("清形2"));
        situations.add(new Situation("情形3"));
        situations.add(new Situation("清形4"));
        situations.add(new Situation("情形5"));
        situations.add(new Situation("清形6"));
        mode.setSituations(situations);
        modeService.addMode(mode);
    }

    @Test
    void updateMode() {
        Mode mode = modeService.getModeByName("测试模式");
        ArrayList<Situation> situations = mode.getSituations();
        situations.get(0).setName("情形1被改动");
        modeService.updateMode(mode);
    }

    @Test
    void deleteMode() {
        modeService.deleteMode(modeService.getModeByName("特使模式"));
    }
}