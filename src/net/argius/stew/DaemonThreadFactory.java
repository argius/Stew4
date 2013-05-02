package net.argius.stew;

import java.util.concurrent.*;

/**
 * This is a ThreadFactory which creates threads as a daemon.
 */
public final class DaemonThreadFactory implements ThreadFactory {

    private static final Logger log = Logger.getLogger(DaemonThreadFactory.class);

    private static volatile int count;

    private static ThreadFactory instance;

    private DaemonThreadFactory() {
    }

    /**
     * Returns an instance of DaemonThreadFactory (as ThreadFactory).
     * @return
     */
    public static ThreadFactory getInstance() {
        if (instance == null) {
            instance = new DaemonThreadFactory();
        }
        return instance;
    }

    @Override
    public Thread newThread(Runnable r) {
        final String name = String.format("ChildDaemon%d-of-%s", count++, Thread.currentThread());
        if (log.isDebugEnabled()) {
            log.debug("create thread: name=" + name);
        }
        Thread thread = new Thread(r, name);
        thread.setDaemon(true);
        return thread;
    }

    /**
     * Executes a task by DaemonThread.
     * @param task
     */
    public static void execute(Runnable task) {
        getInstance().newThread(task).start();
    }

}