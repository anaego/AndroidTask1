package com.anastasego.task1try1;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class StopwatchUser implements Runnable {

    private String userID;
    // should those be atomic? or will just primitives do?
    private AtomicBoolean stopwatchStarted = new AtomicBoolean(false);
    private AtomicBoolean thisUserFinished = new AtomicBoolean(false);
    private AtomicLong stopwatchValueReceiver = new AtomicLong(0);
    private Thread userThread;

    // i made parameterless constructor private so that it can't be accessed, but is it necessary?
    private StopwatchUser() {}

    // userid is just so it's simpler to output results
    StopwatchUser(String userID) {
        this.userID = userID;
    }

    void newStopwatchUserThread() {
        userThread = new Thread(this);
        userThread.start();
    }

    // every 1 second the user checks the stopwatch and gets time from it. but I doubt it's
    // the best solution, i want to try to make stopwatch itself trigger the user to process
    // the value every time it's changed. would that be a good thing to do?
    public void run() {
        System.out.println("--- User thread started, userID = " + this.userID
                + ", thread id = " + Thread.currentThread().getId());
        while (!thisUserFinished.get()) {
            if (stopwatchStarted.get()) {
                System.out.println("--- Running stopwatch, userID = " + this.userID
                        + ", thread id = " + Thread.currentThread().getId()
                        + ", stopwatch value: " + stopwatchValueReceiver);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("--- User thread interrupted, userID = " + this.userID);
                }
            }
        }
    }

    void finishThisUser() {
        this.thisUserFinished.set(true);
    }

    void stopUserThread() {
        try {
            userThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // just a setter
    void setStopwatchStarted(boolean stopwatchStarted) {
        this.stopwatchStarted.set(stopwatchStarted);
    }

    // just a setter
    void setStopwatchValueReceiver(long stopwatchValueReceiver) {
        this.stopwatchValueReceiver.set(stopwatchValueReceiver);
    }

    String getUserID() {
        return userID;
    }
}
