package org.blue.automation.entities.vo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.services.ModePropertyService;
import org.blue.automation.services.impl.ModePropertyServiceImpl;
import org.junit.jupiter.api.Test;

class ModePropertyTest {
    static final Logger log = LogManager.getLogger(ModePropertyTest.class);
    ModePropertyService modePropertyService = new ModePropertyServiceImpl("test.json");

    @Test
    void test(){
        ModeProperty modeProperty = new ModeProperty();
        modeProperty.setName("御魂1");
        SituationProperty situationProperty = new SituationProperty();
        situationProperty.setName("等待结算");
        situationProperty.setPriority(1);
        situationProperty.setClick(false);
        modeProperty.getSituationList().add(situationProperty);
        boolean b = modePropertyService.addModeProperty(modeProperty);
        log.info("结果:{}",b);
    }

}