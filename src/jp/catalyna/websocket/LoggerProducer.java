package jp.catalyna.websocket;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ishida on 2017/04/18.
 */

@Dependent
public class LoggerProducer {
    @Inject
    InjectionPoint point;

    @Produces
    public Logger getLogger() {
        String loggerName = point.getMember().getDeclaringClass().getName();
        Logger logger = Logger.getLogger(loggerName);
        logger.setLevel(Level.WARNING);
        return logger;
    }
}