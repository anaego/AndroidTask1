package com.anastasego.task1try1;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTwoUserTest {

    @Test
    public void checkResultCorrection() {
        // create stopwatch
        Stopwatch stopwatch = Stopwatch.getInstance();
        // create two users to demonstrate how it works
        StopwatchUser user1 = new StopwatchUser("1");
        StopwatchUser user2 = new StopwatchUser("2");
        // separate threads for those users
        user1.newStopwatchUserThread();
        user2.newStopwatchUserThread();
        // users start stopwatch
        stopwatch.startExecutor();
        stopwatch.startStopwatch(user1);
        stopwatch.startStopwatch(user2);
        waitForMilliseconds(10000);
        // one user stops the stopwatch
        stopwatch.stopStopwatch(user1);
        long stopwatchValueAfterUser1Stopped = stopwatch.getStopwatch();
        waitForMilliseconds(3000);
        // the other user stops the stopwatch
        stopwatch.stopStopwatch(user2);
        // cleanup - should the cleanup be automatic? should it be done somewhere else?
        user1.finishThisUser();
        user1.stopUserThread();
        user2.finishThisUser();
        user2.stopUserThread();
        stopwatch.stopExecutor();
        waitForMilliseconds(3000);
        System.out.println("---------- Stopwatch value some time after ending: "
                + stopwatch.getStopwatch());
        assertEquals(stopwatch.getStopwatch(), user2.getStopwatchValueReceiver());
        assertEquals(stopwatchValueAfterUser1Stopped, user1.getStopwatchValueReceiver());
    }

    private void waitForMilliseconds(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
