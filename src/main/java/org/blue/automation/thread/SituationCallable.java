package org.blue.automation.thread;

import org.blue.automation.entities.enums.PathEnum;
import org.blue.automation.entities.ImageInformation;
import org.blue.automation.entities.Situation;
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
    private final Situation situation;
    public SituationCallable(Situation situation) {
        this.situation = situation;
    }

    @Override
    public Situation call() {
        ImageUtil imageUtil = ImageUtil.getInstance();
        ImageInformation templateImage = situation.getImage();
        Mat originMat = imageUtil.interceptImage(PathEnum.IMAGE_OUTER+"main.png", templateImage.getX(), templateImage.getY(), templateImage.getWidth(), templateImage.getHeight());
        Mat templateMat = Imgcodecs.imread(templateImage.getPath());
        situation.setRealSimile(BigDecimal.valueOf(imageUtil.getSimile(originMat,templateMat)));
        return situation;
    }
}
