package org.blue.automation.entities.vo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.entities.Mode;
import org.blue.automation.entities.Situation;
import org.blue.automation.services.ModeService;
import org.blue.automation.services.impl.ModeServiceImpl;
import org.junit.jupiter.api.Test;

class ModeTest {
    static final Logger log = LogManager.getLogger(ModeTest.class);
    ModeService modeService = new ModeServiceImpl("test.json");

    @Test
    void test(){
        Mode mode = new Mode();
        mode.setName("御魂1");
        Situation situation = new Situation();
        situation.setName("等待结算");
        situation.setPriority(1);
        situation.setClick(false);
        mode.getSituationList().add(situation);
        boolean b = modeService.addMode(mode);
        log.info("结果:{}",b);
    }

}