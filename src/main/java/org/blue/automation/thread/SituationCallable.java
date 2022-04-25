package org.blue.automation.thread;

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

    private final Situation situation;

    public SituationCallable(Situation situation) {
        this.situation = situation;
    }

    @Override
    public Situation call() throws Exception {
        TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 49 + 1));
        situation.setSimile(BigDecimal.valueOf(Math.random()));
        return situation;
    }
}
