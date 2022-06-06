package com.meta.utils;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Martin
 * 共享线程池(饿汉模式)
 * */
@Log4j2
public class ShareThreadPoolUtil {

    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 200, 3,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1024), new MyRejectPolicy()
    );

    public static void execute(String describe, Runnable command){
        threadPool.execute(new Task(describe, command));
    }

    static class Task implements Runnable{
        private String describe;
        private Runnable command;

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        private Task(String describe, Runnable command){
            this.describe = describe;
            this.command = command;
        }

        @Override
        public void run() {
            command.run();
        }
    }

    // 拒绝策略
    static class MyRejectPolicy implements RejectedExecutionHandler {
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
            String describe = "";
            if (runnable instanceof Task){
                Task task = (Task) runnable;
                describe = task.getDescribe();
            }
            log.error("ShareThreadPool线程池任务溢出, describe:{}", describe);
        }
    }

}
