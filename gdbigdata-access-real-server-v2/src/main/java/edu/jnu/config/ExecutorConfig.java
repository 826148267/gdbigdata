package edu.jnu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @作者: 郭梓繁
 * @邮箱: 826148267@qq.com
 * @版本: 1.0
 * @创建日期: 2023年05月17日 20时33分
 * @功能描述: TODO
 */
@Configuration
@EnableAsync
public class ExecutorConfig {
    /**
     * 线程池的配置
     *
     * @Author 盛春强
     * @Date 2021/4/21 16:12
     * @Param []
     * @Return java.util.concurrent.Executor
     */
    @Bean
    public Executor threadPoolExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(16);
        //配置最大线程数
        executor.setMaxPoolSize(23);
        //配置队列大小
        executor.setQueueCapacity(50);

        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("threadPool-");
        // rejection-policy：当pool已经达到max size的时候，并且队列已经满了，如何处理新任务
        // DiscardPolicy: 直接丢弃
        // CallerRunsPolicy：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    /**
     * 配置stream并行流线程池
     *
     * @Author 盛春强
     * @Date 2021/4/21 15:17
     * @Param []
     * @Return void
     */
    @Bean
    public String parallelStreamConfig() {
        return System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "23");
    }
}
