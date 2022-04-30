package org.blue.automation.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.entities.enums.PathEnum;
import org.blue.automation.entities.vo.ImageProperty;
import org.blue.automation.entities.vo.SituationProperty;
import org.blue.automation.utils.ImageUtil;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

/**
 * name: MengHao Tian
 * date: 2022/4/25 21:36
 */
public class SituationPropertyCallable implements Callable<SituationProperty>{
    private final static Logger log = LogManager.getLogger(SituationPropertyCallable.class);
    private final SituationProperty situation;
    public SituationPropertyCallable(SituationProperty situation) {
        this.situation = situation;
    }

    @Override
    public SituationProperty call() throws Exception {
        ImageUtil imageUtil = ImageUtil.getInstance();
        ImageProperty templateImage = situation.getImage();
        Mat originMat = imageUtil.interceptImage(PathEnum.IMAGE_OUTER+"main.png", templateImage.getX(), templateImage.getY(), templateImage.getWidth(), templateImage.getHeight());
        Mat templateMat = Imgcodecs.imread(templateImage.getPath());
        situation.setRealSimile(BigDecimal.valueOf(imageUtil.getSimile(originMat,templateMat)));
        return situation;
    }
}
