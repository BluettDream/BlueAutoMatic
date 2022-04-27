package org.blue.automation.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.Main;
import org.blue.automation.entities.Mode;
import org.blue.automation.entities.Situation;
import org.blue.automation.entities.enums.PathEnum;
import org.blue.automation.services.AdbService;
import org.blue.automation.services.impl.AdbServiceImpl;

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
        log.info("开始运行:{}", mode);
        ArrayList<Situation> situationArrayList = mode.getSituations();
        //如果没有情景列表,则直接返回false
        if (situationArrayList == null || situationArrayList.size() <= 0) return false;
        //创建接收完成任务的线程池
        CompletionService<Situation> completionService = new ExecutorCompletionService<>(THREAD_POOL);
        //接收任务的列表
        ArrayList<Future<Situation>> futureArrayList = new ArrayList<>();
        //得到的最终情景
        Situation ans = new Situation();
        //ADB服务接口
        AdbService adbService = new AdbServiceImpl();
        //初始时间
        long initMillis = 0, waitTime = 0;
        try {
            while (!Thread.currentThread().isInterrupted() && waitTime < 15000) {
                initMillis = System.currentTimeMillis();
                futureArrayList.clear();
                ans.setSimile(BigDecimal.valueOf(-1));
                adbService.captureAndSave("/sdcard/blue_main.png", PathEnum.IMAGE_OUTER + "main.png");
                for (Situation situation : situationArrayList) {
                    Future<Situation> res = completionService.submit(new SituationCallable(situation));
                    futureArrayList.add(res);
                }
                for (int i = 0; i < futureArrayList.size(); ++i) {
                    Situation situation = completionService.take().get();
                    log.debug("{}的相似度为:{}", situation.getName(), situation.getSimile());
                    if (ans.getSimile().compareTo(situation.getSimile()) < 0) ans = situation;
                }
                log.info("最大相似度为:{},情景是:{}", ans.getSimile().setScale(2, BigDecimal.ROUND_HALF_UP), ans);
                waitTime = System.currentTimeMillis() - initMillis;
                log.debug("等待时间:{}ms",waitTime);
            }
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
        }
        log.info("{}运行结束", mode.getName());
        return true;
    }
}
