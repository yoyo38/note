package cn.holdmoral.forever.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author lujianhua
 * @version 1.0
 * @company holdmoral
 * @date 2021/2/2 14:12
 */
public class ThreadUtil {
    private static ScheduledExecutorService service;
    public static void execute(Runnable runnable){
        if(service == null){
            service = Executors.newScheduledThreadPool(64);
        }
        service.schedule(runnable,0, TimeUnit.SECONDS);
    }

    public static void execute(Runnable runnable,long delay){
        if(service == null){
            service = Executors.newScheduledThreadPool(64);
        }
        service.schedule(runnable,delay, TimeUnit.SECONDS);
    }

    public static void scheduleAtFixedRate(Runnable runnable,long delay,long period,TimeUnit timeUnit){
        if(service == null){
            service = Executors.newScheduledThreadPool(64);
        }
        service.scheduleAtFixedRate(runnable, delay, period, timeUnit);
    }


}
