package org.blue.automation.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.blue.automation.entities.ImageBase;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilTest {

    FileUtil fileUtil = FileUtil.getInstance();

    @Test
    void test() throws IOException {
        ObjectMapper objectMapper = fileUtil.getObjectMapper();
        ImageBase imageBase = new ImageBase();
        imageBase.setX(1172);
        imageBase.setY(72);
        imageBase.setWidth(720);
        imageBase.setHeight(406);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(
                new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir")+"/conf/电脑.json"), StandardCharsets.UTF_8)
        , imageBase);
    }

    @Test
    void getPoint() throws InterruptedException {
        while (true){
            Point location = MouseInfo.getPointerInfo().getLocation();
            System.out.println(location.x+","+ location.y);
            Thread.sleep(1000);
        }

    }

}