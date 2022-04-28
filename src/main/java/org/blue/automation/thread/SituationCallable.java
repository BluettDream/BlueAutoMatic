package org.blue.automation.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.entities.Situation;
import org.blue.automation.entities.SituationImage;
import org.blue.automation.entities.enums.PathEnum;
import org.blue.automation.utils.ImageUtil;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

/**
 * name: MengHao Tian
 * date: 2022/4/25 21:36
 */
public class SituationCallable implements Callable<Situation>{
    private final static Logger log = LogManager.getLogger(SituationCallable.class);
    private final Situation situation;
    public SituationCallable(Situation situation) {
        this.situation = situation;
    }

    @Override
    public Situation call() throws Exception {
        ImageUtil imageUtil = ImageUtil.getInstance();
        SituationImage templateImage = situation.getImage();
        Mat originMat = imageUtil.interceptImage(PathEnum.IMAGE_OUTER+"main.png", templateImage.getRect());
        Mat templateMat = Imgcodecs.imread(templateImage.getPath());
        situation.setSimile(BigDecimal.valueOf(imageUtil.getSimile(originMat,templateMat)));
        return situation;
    }
}
