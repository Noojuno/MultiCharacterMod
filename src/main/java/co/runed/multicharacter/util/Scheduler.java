package co.runed.multicharacter.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler
{
    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    /**
     * Schedules a task to run after a defined delay
     *
     * @param task Task to run
     * @param delay Delay in ms
     */
    public static void run(Runnable task, int delay)
    {
        executor.schedule(task, (long) delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Schedules a task to run
     *
     * @param task Task to run
     */
    public static void run(Runnable task)
    {
        executor.schedule(task, 0L, TimeUnit.MILLISECONDS);
    }
}
