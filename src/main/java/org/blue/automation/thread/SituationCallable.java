package org.blue.automation.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.Main;
import org.blue.automation.entities.Situation;

import java.math.BigDecimal;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * name: MengHao Tian
 * date: 2022/4/25 21:36
 */
public class SituationCallable implements Callable<Situation>{
    private final static Logger log = LogManager.getLogger(SituationCallable.class);
    private final Situation situation;

    public SituationCallable(Situation situation) {
        this.situation = situation;
    }

    @Override
    public Situation call() throws Exception {
        long l = System.currentTimeMillis();
        TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 999 + 500));
        situation.setSimile(BigDecimal.valueOf(Math.random()));
        log.debug("{}耗时:{}",situation.getName(),System.currentTimeMillis() - l);
        return situation;
    }
}
