package com.yilnz.bluesteel.test;


import java.time.Duration;
import java.time.Instant;

public class MethodTimeCaculator {
    public static void invokeMethod(Runnable function){
        final Instant start = Instant.now();
        function.run();
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println("[YILNZ-UTILS]" + Thread.currentThread().getStackTrace()[1].getMethodName()
                + "方法花费了" + duration.toMillis() + "毫秒");
    }
}
