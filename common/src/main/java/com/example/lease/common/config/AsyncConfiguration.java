package com.example.lease.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步线程池配置
 * <p>
 * 为日志记录等异步任务提供自定义线程池，避免阻塞主业务线程
 */
@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {

    /**
     * 自定义异步任务执行器
     * <p>
     * 核心线程数：CPU 核心数
     * 最大线程数：CPU 核心数 * 2
     * 队列容量：100
     * 拒绝策略：由调用线程执行（CallerRunsPolicy）
     */
    @Bean(name = "logTaskExecutor")
    public Executor logTaskExecutor() {
        int cpuCores = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(cpuCores);
        executor.setMaxPoolSize(cpuCores * 2);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("log-async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return logTaskExecutor();
    }
}
