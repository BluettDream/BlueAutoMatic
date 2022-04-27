package org.blue.automation.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.entities.Mode;
import org.blue.automation.entities.Situation;
import org.blue.automation.entities.SituationImage;
import org.blue.automation.entities.enums.Action;
import org.blue.automation.entities.enums.PathEnum;
import org.blue.automation.services.impl.ModeServiceImpl;
import org.junit.jupiter.api.Test;
import org.opencv.core.Rect;

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
        mode.setName("测试10");
        ArrayList<Situation> situations = new ArrayList<>();
        situations.add(new Situation("情景1",new SituationImage(PathEnum.IMAGE_OUTER+"test1.png",new Rect(new double[]{10,20,100,200})),true, Action.CLICK));
        situations.add(new Situation("情景2",new SituationImage(PathEnum.IMAGE_OUTER+"test2.png",new Rect(new double[]{10,20,100,200})),false));
        situations.add(new Situation("情景3",new SituationImage(PathEnum.IMAGE_OUTER+"test3.png",new Rect(new double[]{10,20,100,200})),true, Action.LONG_CLICK));
        situations.add(new Situation("情景4",new SituationImage(PathEnum.IMAGE_OUTER+"test4.png",new Rect(new double[]{10,20,100,200})),true, Action.SLIDE));
        situations.add(new Situation("情景5",new SituationImage(PathEnum.IMAGE_OUTER+"test5.png",new Rect(new double[]{10,20,100,200})),false));
        situations.add(new Situation("情景6",new SituationImage(PathEnum.IMAGE_OUTER+"test6.png",new Rect(new double[]{10,20,100,200})),true, Action.RANDOM_CLICK));
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