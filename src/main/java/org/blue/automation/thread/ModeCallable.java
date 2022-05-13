package org.blue.automation.thread;

import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.Main;
import org.blue.automation.controller.IndexController;
import org.blue.automation.controller.ModeRunningController;
import org.blue.automation.entities.SituationBase;
import org.blue.automation.entities.enums.Action;
import org.blue.automation.entities.enums.PathEnum;
import org.blue.automation.services.OperationService;
import org.blue.automation.utils.StringUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * name: MengHao Tian
 * date: 2022/4/25 15:41
 */
public class ModeCallable implements Callable<Boolean> {
    private final static Logger log = LogManager.getLogger(ModeCallable.class);
    private final OperationService OPERATION_SERVICE = IndexController.getOperationService();
    /**
     * 接收完成任务的线程池
     **/
    private final CompletionService<SituationBase> completionService = new ExecutorCompletionService<>(Main.THREAD_POOL);
    /**
     * 任务接收列表
     **/
    private final ArrayList<Future<SituationBase>> futureArrayList = new ArrayList<>();
    /**
     * 情景列表
     **/
    private final ArrayList<SituationBase> situationList = IndexController.getCurrentModeProperty().get().getSituationList();
    /**
     * 初始时间
     **/
    private long initMillis = System.currentTimeMillis();
    /**
     * 等待时间
     **/
    private long runningTime = 0;
    /**
     * 最终情景
     **/
    private SituationBase endSituation = new SituationBase();
    /**
     * 前一次情景
     **/
    private SituationBase preSituation = new SituationBase();
    private long equalTimes = 0;

    /**
     * 当前情景相似度大于最低相似度,进入判断,如果当前对象的优先级比结果对象的优先级高则直接将结果设置为当前对象
     * 或者优先级相同时,比较当前对象的相似度和结果对象的相似度,相似度高者确定最终情景
     **/
    @Override
    public Boolean call(){
        log.info("{}模式开始运行", IndexController.getCurrentModeProperty().get().getName());
        if (situationList == null || situationList.size() <= 0) return false;
        long millis = System.currentTimeMillis();
        while (!Thread.currentThread().isInterrupted() && runningTime < IndexController.getCurrentModeProperty().get().getRunTime()) {
            clearEndSituation();
            futureArrayList.clear();
            try {
                OPERATION_SERVICE.captureAndSave(PathEnum.IMAGE_OUTER + "main.png");
            } catch (IOException e) {
                log.error("图片截图保存异常:",e);
                Thread.currentThread().interrupt();
            }
            situationList.forEach(situation -> futureArrayList.add(completionService.submit(new SituationCallable(situation))));
            for (Future<SituationBase> situationFuture : futureArrayList) {
                SituationBase temp;
                try {
                    temp = situationFuture.get();
                    log.debug("{}相似度:{}",temp.getName(),temp.getRealSimile());
                    if (temp.getRealSimile().compareTo(temp.getLowestSimile()) >= 0 &&
                            (temp.getPriority() > endSituation.getPriority() || (temp.getPriority() == endSituation.getPriority()
                            && temp.getRealSimile().compareTo(endSituation.getRealSimile()) >= 0))) {
                        endSituation = temp.cloneFor(temp);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    log.error("获取情景线程结果出现异常:", e);
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
            log.info("匹配结果:{},相似度:{}", endSituation.getName(), endSituation.getRealSimile().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            if (!endSituation.getName().equals("正在匹配")) {
                try {
                    Action.operate(OPERATION_SERVICE, endSituation);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            if(!StringUtil.isWrong(preSituation.getName()) && endSituation.getName().equals(preSituation.getName())){
                equalTimes = System.currentTimeMillis() - initMillis;
                if(equalTimes > preSituation.getMaxDelayTime()){
                    break;
                }
            }else{
                initMillis = System.currentTimeMillis();
            }
            Platform.runLater(()->{
                ModeRunningController.resultProperty().set("匹配结果: "+endSituation.getName());
                ModeRunningController.WAIT_TIMEProperty().set(runningTime / (60*60.0*1000));
                ModeRunningController.SITUATION_LISTProperty().get().setAll(situationList);
            });
            preSituation = endSituation.cloneFor(endSituation);
            runningTime = System.currentTimeMillis() - millis;
            log.debug("等待时间:{}ms", equalTimes);
        }
        log.info("{}模式运行结束", IndexController.getCurrentModeProperty().getName());
        return true;
    }

    /**
     * 重置endSituation类
     *
     **/
    private void clearEndSituation() {
        endSituation.setName("正在匹配");
        endSituation.setPriority(-1);
        endSituation.setRealSimile(BigDecimal.valueOf(-1));
    }
}
