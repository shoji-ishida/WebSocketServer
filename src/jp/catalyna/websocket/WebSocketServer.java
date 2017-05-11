package jp.catalyna.websocket;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;


/**
 * Created by ishida on 2017/04/18.
 */
@ServerEndpoint("/actions")
public class WebSocketServer {
    @Inject
    transient Logger logger;
    @Inject
    SessionHandler sessionHandler;

    static AtomicInteger count = new AtomicInteger(0);

    @OnOpen
    public void onOpen(Session session) {
        sessionHandler.addSession(session);
        int n = count.incrementAndGet();
        logger.info("onOpen("+n+")");
    }

    @OnClose
    public void onClose(Session session) {
        sessionHandler.removeSession(session);
        int n = count.decrementAndGet();
        logger.info("onClose("+n+")");
    }

    @OnError
    public void onError(Throwable error) {
        logger.severe("onError:" + error.getMessage());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("onMessage:" + message);
    }
}
