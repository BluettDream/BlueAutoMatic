package org.blue.automation.entities.enums;

import org.blue.automation.entities.Situation;
import org.blue.automation.entities.SituationImage;
import org.blue.automation.services.OperationService;
import org.blue.automation.utils.ImageUtil;
import org.opencv.core.Point;

import java.util.ArrayList;

public enum Action {
    CLICK("单击"),
    LONG_CLICK("长按"),
    RANDOM_CLICK("随机多次点击"),
    SLIDE("滑动"),
    LONG_SLIDE("缓慢滑动");

    public void operate(OperationService operationService,SituationImage image){
        ImageUtil imageUtil = ImageUtil.getInstance();
        switch (this){
            case CLICK:operationService.click(imageUtil.calculateRandomClick(image));break;
            case LONG_CLICK:operationService.longClick(imageUtil.calculateRandomClick(image),100);break;
            case RANDOM_CLICK:
                ArrayList<Point> points = new ArrayList<>();
                SituationImage temp;
                // TODO: 2022/4/29 完善图片处理逻辑,添加自定义路径功能
                if(image.getPath().contains("YouXiJieShuZhong")){
                    temp = image.copy();
                    temp.setX(1565);
                    temp.setY(605);
                    temp.setWidth(770);
                    temp.setHeight(460);
                    //随机次数
                    int times = (int) (Math.random() * 3) + 3;
                    for (int i = 0; i < times; i++) {
                        points.add(imageUtil.calculateRandomClick(temp));
                    }
                }else{
                    //随机次数
                    int times = (int) (Math.random()*2) + 3;
                    for (int i = 0; i < times; i++) {
                        points.add(imageUtil.calculateRandomClick(image));
                    }
                }
                operationService.multipleClicks(points);
                break;
            case SLIDE:
            case LONG_SLIDE:
                break;
        }
    }

    private String description;

    Action(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
