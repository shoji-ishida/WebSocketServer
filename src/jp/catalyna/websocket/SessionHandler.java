package jp.catalyna.websocket;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ishida on 2017/04/21.
 */
@Named(value="sessionHandler")
@ApplicationScoped
public class SessionHandler {
    @Inject
    transient Logger logger;
    private final ConcurrentHashMap.KeySetView<Session,Boolean> sessions;

    private final AtomicInteger sentCount = new AtomicInteger(0);
    private long processTime = 0;

    public SessionHandler() {
        sessions = ConcurrentHashMap.newKeySet();
    }

    public void addSession(Session session) {
        logger.info("addSession");
        sessions.add(session);
    }

    public void removeSession(Session session) {
        logger.info("removeSession");
        sessions.remove(session);
    }

    public int getCount() {
        return sessions.size();
    }

    public int getSentCount() {
        return sentCount.get();
    }

    public long getProcessTime() {
        return processTime;
    }

    public String pushToAllSessions() {
        logger.info("Push!!!");
        sentCount.set(0);
        long start = System.currentTimeMillis();
        for (Session session: sessions) {
            push(session);
        }
        processTime = System.currentTimeMillis() - start;
        return "result";
    }

    public String pushConcurrentlyToAllSessions() {
        ConcurrentHashMap hashMap = sessions.getMap();
        sentCount.set(0);
        long start = System.currentTimeMillis();
        hashMap.forEachKey(1, (session) -> {
            push((Session)session);
        });
        processTime = System.currentTimeMillis() - start;
        return "result";
    }

    private void push(Session session) {
        try {
            session.getBasicRemote().sendText("push");
            sentCount.incrementAndGet();
        } catch (IOException ex) {
            sessions.remove(session);
            logger.log(Level.SEVERE, null, ex);
        }
    }
}
