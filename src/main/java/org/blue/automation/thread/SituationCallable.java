package org.blue.automation.thread;

import org.blue.automation.entities.SituationBase;
import org.blue.automation.entities.enums.PathEnum;
import org.blue.automation.entities.ImageBase;
import org.blue.automation.utils.ImageUtil;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

/**
 * name: MengHao Tian
 * date: 2022/4/25 21:36
 */
public class SituationCallable implements Callable<SituationBase>{
    private final SituationBase situation;
    public SituationCallable(SituationBase situation) {
        this.situation = situation;
    }

    @Override
    public SituationBase call() {
        ImageUtil imageUtil = ImageUtil.getInstance();
        ImageBase templateImage = situation.getImage();
        Mat originMat = imageUtil.interceptImage(PathEnum.IMAGE_OUTER+"main.png", templateImage.getX(), templateImage.getY(), templateImage.getWidth(), templateImage.getHeight());
        Mat templateMat = Imgcodecs.imread(templateImage.getPath());
        situation.setRealSimile(BigDecimal.valueOf(imageUtil.getSimile(originMat,templateMat)));
        return situation;
    }
}
