package org.blue.automation.entities.vo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.entities.ModeBase;
import org.blue.automation.entities.SituationBase;
import org.blue.automation.services.ModeService;
import org.blue.automation.services.impl.ModeServiceImpl;
import org.junit.jupiter.api.Test;

class ModeBaseTest {
    static final Logger log = LogManager.getLogger(ModeBaseTest.class);
    ModeService modeService = new ModeServiceImpl("test.json");

    @Test
    void test(){
        ModeBase modeBase = new ModeBase();
        modeBase.setName("御魂1");
        SituationBase situation = new SituationBase();
        situation.setName("等待结算");
        situation.setPriority(1);
        situation.setClick(false);
        modeBase.getSituationList().add(situation);
        boolean b = modeService.addMode(modeBase);
        log.info("结果:{}",b);
    }

}