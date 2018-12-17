package com.anastasego.task1try1;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class StopwatchUser implements Runnable {

    private String userID;
    private AtomicBoolean stopwatchStarted = new AtomicBoolean(false);
    private AtomicBoolean thisUserFinished = new AtomicBoolean(false);
    private AtomicLong stopwatchValueReceiver = new AtomicLong(0);
    private AtomicBoolean stopwatchUpdated = new AtomicBoolean(false);
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

    public void run() {
        System.out.println("--- User thread started, userID = " + this.userID
                + ", thread id = " + Thread.currentThread().getId());
        while (!thisUserFinished.get()) {
            // when the stopwatch is updated, it sets stopwatchUpdated for the user so that it can
            // process the new stopwatch value
            if (stopwatchStarted.get() && stopwatchUpdated.get()) {
                System.out.println("--- Running stopwatch, userID = " + this.userID
                        + ", thread id = " + Thread.currentThread().getId()
                        + ", stopwatch value: " + stopwatchValueReceiver);
                stopwatchUpdated.set(false);
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

    void setStopwatchUpdated() {
        this.stopwatchUpdated.set(true);
    }

    String getUserID() {
        return userID;
    }

    long getStopwatchValueReceiver() {
        return stopwatchValueReceiver.get();
    }

    // Java didn't let me user hash without this annotation
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StopwatchUser that = (StopwatchUser) o;
        return Objects.equals(userID, that.userID) &&
                Objects.equals(stopwatchStarted, that.stopwatchStarted) &&
                Objects.equals(thisUserFinished, that.thisUserFinished) &&
                Objects.equals(stopwatchValueReceiver, that.stopwatchValueReceiver) &&
                Objects.equals(stopwatchUpdated, that.stopwatchUpdated) &&
                Objects.equals(userThread, that.userThread);
    }

    // Java didn't let me user hash without this annotation
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(userID, stopwatchStarted, thisUserFinished, stopwatchValueReceiver,
                stopwatchUpdated, userThread);
    }
}
