package org.blue.automation.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.entities.Situation;
import org.blue.automation.entities.SituationImage;
import org.blue.automation.entities.enums.Action;
import org.blue.automation.entities.enums.PathEnum;
import org.blue.automation.services.impl.ModeServiceImpl;
import org.blue.automation.services.impl.SituationServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.opencv.core.Rect;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SituationServiceTest {
    private final static Logger log = LogManager.getLogger(SituationServiceTest.class);
    private final static SituationService situationService = new SituationServiceImpl();

    @BeforeAll
    static void getAllSituations() {
        ArrayList<Situation> situations = situationService.getAllSituationsByName("测试1");
        log.info("测试1全部的情景:{}",situations);
    }

    @Test
    void addSituation() {
        situationService.addSituation(new Situation("新增情景2",new SituationImage(PathEnum.IMAGE+"/newPic.png",new Rect()),true, Action.RANDOM_CLICK));
    }

    @Test
    void deleteSituation() {
        boolean res = situationService.deleteSituation(new Situation("情景6"));
        if(res) log.info("成功删除");
        else log.warn("删除失败");
    }

    @Test
    void updateSituation() {
        boolean res = situationService.updateSituation(new Situation("情景5", new SituationImage(PathEnum.IMAGE_OUTER + "", new Rect()), false));
        if(res) log.info("更新成功");
        else log.warn("更新失败");
    }
}