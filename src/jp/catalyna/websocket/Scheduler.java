package jp.catalyna.websocket;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by ishida on 2017/04/25.
 */
@Named(value="scheduler")
@Stateless
public class Scheduler {
    @Inject
    transient Logger logger;
    @Inject
    SessionHandler sessionHandler;

    long delayInSec = 0;

    @Resource(name = "concurrent/DefaultManagedScheduledExecutorService")
    ManagedScheduledExecutorService managedScheduledExecsvc;

    public void setDelayInSec(long time) {
        delayInSec = time;
        executeScheduledTask();
    }

    public long getDelayInSec() {
        return delayInSec;
    }

    private void executeScheduledTask() {
        logger.info("Scheduled task to execute in " + delayInSec + "sec(s)");
        MyRunnableTask task = new MyRunnableTask();
        managedScheduledExecsvc.schedule(
                task, delayInSec, TimeUnit.SECONDS);
    }

    private class MyRunnableTask implements Runnable {
        @Override
        public void run() {
            logger.info("Executing scheduled task");
            sessionHandler.pushConcurrentlyToAllSessions();
        }
    }
}
