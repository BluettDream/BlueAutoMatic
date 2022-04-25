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
        CompletionService<Situation> completionService = new ExecutorCompletionService<>(THREAD_POOL);
        ArrayList<Situation> situationArrayList = mode.getSituations();
        long millis = System.currentTimeMillis();
        ArrayList<Future<Situation>> futureArrayList = new ArrayList<>();
        Situation ans = new Situation();
        ans.setSimile(BigDecimal.valueOf(-1));
        try {
            while (!Thread.currentThread().isInterrupted() && System.currentTimeMillis() - millis < 15000) {
                futureArrayList.clear();
                log.debug("开始进入循环");
                for (Situation situation : situationArrayList) {
                    Future<Situation> res = completionService.submit(new SituationCallable(situation));
                    futureArrayList.add(res);
                }
                for (int i = 0; i < futureArrayList.size(); ++i) {
                    Situation situation = completionService.take().get();
                    //log.debug("相似度:{}",situation.getSimile());
                    if(ans.getSimile().compareTo(situation.getSimile()) < 0) ans = situation;
                }
                log.info("最大相似度为:{},情形是:{}",ans.getSimile(),ans);
                TimeUnit.SECONDS.sleep(2);
            }
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
        }
        log.info("{}结束运行", mode.getName());
        return true;
    }
}
