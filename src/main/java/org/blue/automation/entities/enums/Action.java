package org.blue.automation.entities.enums;

import org.blue.automation.entities.ImageBase;
import org.blue.automation.entities.SituationBase;
import org.blue.automation.services.OperationService;
import org.blue.automation.services.impl.PCOperationServiceImpl;
import org.blue.automation.utils.ImageUtil;
import org.opencv.core.Point;

import java.util.ArrayList;

public enum Action{
    CLICK("单击"),
    LONG_CLICK("长按"),
    MULTIPLE_CLICKS("随机多次点击"),
    SLIDE("滑动"),
    LONG_SLIDE("缓慢滑动");

    /**
     * 情景中具体实现operationService的方法
     * 与operationService接口方法相关联,接口新增方法,此处就需要添加case顾虑.
     *
     * @param operationService 操作服务接口
     * @param situation 情景
     **/
    public static void operate(OperationService operationService, SituationBase situation) throws InterruptedException {
        ImageUtil imageUtil = ImageUtil.getInstance();
        ImageBase temp = situation.getImage().cloneFor(situation.getImage());
        if(operationService instanceof PCOperationServiceImpl) {
            Thread.sleep((long) (Math.random()*400+100));
        }
        if(situation.isCustom()) {
            temp = situation.getCustomImage().cloneFor(situation.getCustomImage());
            if(situation.isRelation()){
                temp.setX(situation.getImage().getX()+temp.getX());
                temp.setY(situation.getImage().getY()+temp.getY());
            }
        }
        if(!situation.isClick() || situation.getAction() == null) return;
        switch (situation.getAction()){
            case CLICK:
                operationService.click(imageUtil.getRandomPoint(temp.getX(),temp.getY(),temp.getWidth(),temp.getHeight()));
                break;
            case LONG_CLICK:
                operationService.longClick(imageUtil.getRandomPoint(temp.getX(),temp.getY(),temp.getWidth(),temp.getHeight()),100);
                break;
            case MULTIPLE_CLICKS:
                ArrayList<Point> points = new ArrayList<>();
                //随机次数(1~4次)
                int times = (int) (Math.random()*3) + 1;
                for (int i = 0; i < times; i++) {
                    points.add(imageUtil.getRandomPoint(temp.getX(),temp.getY(),temp.getWidth(),temp.getHeight()));
                }
                operationService.multipleClicks(points);
                break;
            case SLIDE:
                operationService.slide(imageUtil.getRandomPoint(temp.getX(),temp.getY(),temp.getWidth(),temp.getHeight()),imageUtil.getRandomPoint(temp.getX(),temp.getY(),temp.getWidth(),temp.getHeight()));
                break;
            case LONG_SLIDE:
                operationService.longSlide(imageUtil.getRandomPoint(temp.getX(),temp.getY(),temp.getWidth(),temp.getHeight()),imageUtil.getRandomPoint(temp.getX(),temp.getY(),temp.getWidth(),temp.getHeight()),500);
                break;
            default:
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
