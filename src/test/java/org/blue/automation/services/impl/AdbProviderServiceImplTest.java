package org.blue.automation.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.entities.AdbProvider;
import org.blue.automation.services.AdbProviderService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdbProviderServiceImplTest {
    static final Logger log = LogManager.getLogger(AdbProviderServiceImplTest.class);
    private final AdbProviderService adbProviderService = new AdbProviderServiceImpl("模拟器.json");

    @Test
    void setAdbProvider() {
        AdbProvider adbProvider = new AdbProvider();
        adbProvider.setDeviceNumber("192.168.100.21:5555");
        adbProvider.setPhoneFilePath("/sdcard/blue_main.png");
        adbProviderService.setAdbProvider(adbProvider);
    }

    @Test
    void getAdbProvider() {
        AdbProvider adbProvider = adbProviderService.getAdbProvider();
        log.debug(adbProvider);
    }
}