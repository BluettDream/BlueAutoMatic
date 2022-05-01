package org.blue.automation.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.Main;
import org.blue.automation.controller.IndexController;
import org.blue.automation.entities.enums.Action;
import org.blue.automation.entities.enums.PathEnum;
import org.blue.automation.entities.Situation;
import org.blue.automation.services.OperationService;

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
    private final CompletionService<Situation> completionService = new ExecutorCompletionService<>(Main.THREAD_POOL);
    /**
     * 任务接收列表
     **/
    private final ArrayList<Future<Situation>> futureArrayList = new ArrayList<>();
    /**
     * 情景列表
     **/
    ArrayList<Situation> situationList = IndexController.getCurrentMode().getSituationList();
    /**
     * 初始时间
     **/
    private long initMillis = System.currentTimeMillis();
    /**
     * 等待时间
     **/
    private long waitTime = 0;
    /**
     * 最终情景
     **/
    private Situation endSituation = new Situation();

    /**
     * 当前情景相似度大于最低相似度,进入判断,如果当前对象的优先级比结果对象的优先级高则直接将结果设置为当前对象
     * 或者优先级相同时,比较当前对象的相似度和结果对象的相似度,相似度高者确定最终情景
     **/
    @Override
    public Boolean call() {
        log.info("{}模式开始运行", IndexController.getCurrentMode().getName());
        if (situationList == null || situationList.size() <= 0) return false;
        while (!Thread.currentThread().isInterrupted() && waitTime < 15000) {
            clearEndSituation();
            futureArrayList.clear();
            waitTime = System.currentTimeMillis() - initMillis;
            OPERATION_SERVICE.captureAndSave(PathEnum.IMAGE_OUTER + "main.png");
            situationList.forEach(situation -> futureArrayList.add(completionService.submit(new SituationCallable(situation))));
            for (Future<Situation> situationFuture : futureArrayList) {
                Situation temp;
                try {
                    temp = situationFuture.get();
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
            if (!endSituation.getName().equals("匹配失败")) {
                initMillis = System.currentTimeMillis();
                Action.operate(OPERATION_SERVICE, endSituation);
            }
            log.debug("等待时间:{}ms", waitTime);
        }
        log.info("{}模式运行结束", IndexController.getCurrentMode().getName());
        return true;
    }

    private void clearEndSituation() {
        endSituation.setName("匹配失败");
        endSituation.setPriority(-1);
        endSituation.setImage(null);
        endSituation.setRealSimile(BigDecimal.valueOf(-1));
    }
}
