package org.blue.automation.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * name: MengHao Tian
 * date: 2022/4/25 15:41
 */
public class ModeCallable implements Callable<Boolean> {
    private final static Logger log = LogManager.getLogger(ModeCallable.class);
    private final Mode mode;

    public ModeCallable(Mode mode) {
        this.mode = mode;
    }

    @Override
    public Boolean call() {
        log.info("{}开始运行:{}", mode.getName(), mode);
        ArrayList<Situation> situationArrayList = mode.getSituations();
        long millis = System.currentTimeMillis();
        try {
            while (!Thread.currentThread().isInterrupted() && System.currentTimeMillis() - millis < 3000) {
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("{}结束运行", mode.getName());
        return true;
    }
}
