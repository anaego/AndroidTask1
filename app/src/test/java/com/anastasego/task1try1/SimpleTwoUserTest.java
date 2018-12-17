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
        try {
            // it keeps going for 20s
            Thread.sleep(10000); // 20 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // one user stops the stopwatch
        stopwatch.stopStopwatch(user1);
        long stopwatchValueAfterUser1Stopped = stopwatch.getStopwatch();
        try {
            // stopwatch is going for another 3 seconds
            Thread.sleep(3000); // 3 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // the other user stops the stopwatch
        stopwatch.stopStopwatch(user2);
        // cleanup - should the cleanup be automatic? should it be done somewhere else?
        user1.finishThisUser();
        user1.stopUserThread();
        user2.finishThisUser();
        user2.stopUserThread();
        stopwatch.stopExecutor();
        try {
            Thread.sleep(3000);
            // this turns out to be 1s more then the last value user got! I think because user's
            // code checks once eery 1000ms which doesn't seem ideal...
            System.out.println("---------- Stopwatch value some time after ending: "
                    + stopwatch.getStopwatch());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(stopwatch.getStopwatch(), user2.getStopwatchValueReceiver());
        assertEquals(stopwatchValueAfterUser1Stopped, user1.getStopwatchValueReceiver());
    }
}
