package org.blue.automation.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.Main;
import org.blue.automation.entities.Mode;
import org.blue.automation.entities.Situation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * name: MengHao Tian
 * date: 2022/4/25 15:41
 */
public class ModeCallable implements Callable<Boolean> {
    private final static Logger log = LogManager.getLogger(ModeCallable.class);
    private final Mode mode;
    private final ExecutorService THREAD_POOL = Main.THREAD_POOL;

    public ModeCallable(Mode mode) {
        this.mode = mode;
    }

    @Override
    public Boolean call() {
        log.info("{}开始运行:{}", mode.getName(), mode);
        ArrayList<Situation> situationArrayList = mode.getSituations();
        //如果没有情景列表,则直接返回false
        if(situationArrayList == null || situationArrayList.size() <= 0) return false;
        //创建接收完成任务的线程池
        CompletionService<Situation> completionService = new ExecutorCompletionService<>(THREAD_POOL);
        //接收任务的列表
        ArrayList<Future<Situation>> futureArrayList = new ArrayList<>();
        //得到的最终情景
        Situation ans = new Situation();
        long initMillis = System.currentTimeMillis();
        try {
            while (!Thread.currentThread().isInterrupted() && System.currentTimeMillis() - initMillis < 15000) {
                futureArrayList.clear();
                ans.setSimile(BigDecimal.valueOf(-1));
                log.debug("开始进入循环");
                for (Situation situation : situationArrayList) {
                    Future<Situation> res = completionService.submit(new SituationCallable(situation));
                    futureArrayList.add(res);
                }
                for (int i = 0; i < futureArrayList.size(); ++i) {
                    Situation situation = completionService.take().get();
                    log.debug("{}的相似度为:{}",situation.getName(),situation.getSimile());
                    if(ans.getSimile().compareTo(situation.getSimile()) < 0) ans = situation;
                }
                log.info("最大相似度为:{},情形是:{}",ans.getSimile(),ans);
            }
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
        }
        log.info("{}结束运行", mode.getName());
        return true;
    }
}
