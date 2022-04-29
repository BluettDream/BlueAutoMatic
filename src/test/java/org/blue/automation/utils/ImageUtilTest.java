package org.blue.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.entities.SituationImage;
import org.junit.jupiter.api.Test;
import org.opencv.core.Point;

import javax.sound.midi.Soundbank;

import static org.junit.jupiter.api.Assertions.*;

class ImageUtilTest {
    private static final Logger log = LogManager.getLogger(ImageUtilTest.class);
    ImageUtil imageUtil = ImageUtil.getInstance();

    @Test
    void calculateRandomClick() {
        SituationImage image = new SituationImage(null,2073,917,122,112);
        Point point = imageUtil.calculateRandomClick(image);
        log.info(point);
    }
}