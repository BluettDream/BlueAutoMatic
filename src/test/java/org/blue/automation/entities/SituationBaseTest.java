package org.blue.automation.entities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.entities.enums.PathEnum;
import org.blue.automation.services.SituationService;
import org.blue.automation.services.impl.ModeServiceImpl;
import org.blue.automation.services.impl.SituationServiceImpl;
import org.blue.automation.utils.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

class SituationBaseTest {
    static final Logger log = LogManager.getLogger(SituationBaseTest.class);
    private ObjectMapper objectMapper = FileUtil.getInstance().getObjectMapper();
    private SituationBase situationBase = new SituationBase();
    private SituationService situationService = new SituationServiceImpl(new ModeServiceImpl("main.json"));

    @Test
    void read() throws IOException {
        SituationBase situationBase = objectMapper.readValue(
                new InputStreamReader(new FileInputStream(PathEnum.JSON + "test.json"), StandardCharsets.UTF_8)
                , SituationBase.class);
        log.debug(situationBase);
    }

    @Test
    void write() throws IOException{
        SituationBase situationBase = new SituationBase();
        situationBase.setCustom(true);
        situationBase.setCustomImage(new ImageBase().setX(20).setY(60).setWidth(200).setHeight(200));
        situationBase.setName("测试");
        situationBase.setImage(new ImageBase());
        situationBase.setClick(false);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(
                new OutputStreamWriter(new FileOutputStream(PathEnum.JSON+"test.json"),StandardCharsets.UTF_8)
        , situationBase);
    }

    @Test
    void convert() throws IOException{

    }
}