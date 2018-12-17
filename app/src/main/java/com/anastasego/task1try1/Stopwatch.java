package com.anastasego.task1try1;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class Stopwatch {

    // with the singleton I implemented, instance is not immutable. should smth be done about it?
    private static Stopwatch instance = null;
    private AtomicLong stopwatch = new AtomicLong(0);
    // is it a good idea to make this final?
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private AtomicBoolean stopwatchStopped = new AtomicBoolean(true);
    private ConcurrentHashMap<String, StopwatchUser> observingUsers = new ConcurrentHashMap<>();

    private Runnable runnableStopwatch = new Runnable() {
        // java didn't let me user forEach without this:
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void run() {
            // only one thread can (is supposed to) access this code, thus not synchronized/locked
            if (!stopwatchStopped.get()) {
                stopwatch.incrementAndGet();
                observingUsers.forEach((k, v) -> v.setStopwatchValueReceiver(stopwatch.get()));
                observingUsers.forEach((k, v) -> v.setStopwatchUpdated());
                System.out.println("--- STOPWATCH thread id: "
                        + Thread.currentThread().getId() + ", actual value: " + stopwatch.get());
            }
        }
    };

    private Stopwatch() {}

    void startExecutor() {
        // i made stopwatch a scheduled task running once per second
        executor.scheduleAtFixedRate(runnableStopwatch, 0, 1, TimeUnit.SECONDS);
    }

    void stopExecutor() {
        executor.shutdown();
    }

    // singleton because it seems logical - only one stopwatch should exist
    public static Stopwatch getInstance() {
        if (instance == null) {
            synchronized (Stopwatch.class) {
                if (instance == null) {
                    instance = new Stopwatch();
                }
            }
        }
        return instance;
    }

    // synchronized because this code should be done in one piece
    synchronized void startStopwatch(StopwatchUser user) {
        if (observingUsers.isEmpty()) {
            stopwatchStopped.set(false);
        }
        observingUsers.putIfAbsent(user.getUserID(), user);
        user.setStopwatchStarted(true);
    }

    // synchronized because this code should be done in one piece
    synchronized long stopStopwatch(StopwatchUser user) {
        observingUsers.remove(user.getUserID());
        user.setStopwatchStarted(false);
        if (observingUsers.isEmpty()) {
            stopwatchStopped.set(true);
        }
        return stopwatch.get();
    }

    long getStopwatch() {
        return stopwatch.get();
    }
}
