package org.blue.automation.thread;

import org.blue.automation.entities.SituationBase;
import org.blue.automation.entities.enums.PathEnum;
import org.blue.automation.entities.ImageBase;
import org.blue.automation.utils.ImageUtil;
import org.opencv.core.Core;
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
        Core.MinMaxLocResult maxLocResult = imageUtil.getMaxResult(Imgcodecs.imread(PathEnum.IMAGE_OUTER + "main.png"), Imgcodecs.imread(PathEnum.IMAGE_INNER+templateImage.getName()));
        //设置相似度最大的位置和相似度
        situation.getImage().setX((int) maxLocResult.maxLoc.x);
        situation.getImage().setY((int) maxLocResult.maxLoc.y);
        situation.setRealSimile(BigDecimal.valueOf(maxLocResult.maxVal));
        return situation;
    }
}
