package com.anastasego.task1try1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// just some temp test code to demonstrate what i've written so far
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            Thread.sleep(20000); // 20 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // one user stops the stopwatch
        stopwatch.stopStopwatch(user1);
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
    }
}
