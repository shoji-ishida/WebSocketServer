package jp.catalyna.websocket;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Created by ishida on 2017/05/11.
 */
@Singleton
@Startup
public class ScheduledEjb {
    @Inject
    transient Logger logger;

    @PostConstruct
    public void initialized() {
        logger.info("constructed");
    }

    @Schedule(hour="*", minute="*", second="*/30")
    public void timeout() {
        logger.info("Scheduled ScheduledEjb");
    }
}
