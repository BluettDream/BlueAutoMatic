package org.blue.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import sun.util.resources.cldr.om.LocaleNames_om;

import static org.junit.jupiter.api.Assertions.*;

class PinYinUtilTest {

    private final static Logger log = LogManager.getLogger(PinYinUtilTest.class);

    @Test
    void getPinYin() {
        log.info(PinYinUtil.getInstance().getPinYin("开始游戏(12345)"));
    }
}