package com.example.OrderService.config;

import io.micrometer.context.ContextExecutorService;
import io.micrometer.context.ContextSnapshot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.*;

@Configuration(proxyBeanMethods = false)
class TraceConfig {

    // Example of Async Servlets setup
    @Configuration(proxyBeanMethods = false)
    @EnableAsync
    static class AsyncConfig implements AsyncConfigurer, WebMvcConfigurer {
        @Override
        public Executor getAsyncExecutor() {
            return ContextExecutorService.wrap(Executors.newCachedThreadPool(), ContextSnapshot::captureAll);
        }

        @Override
        public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
            configurer.setTaskExecutor(new SimpleAsyncTaskExecutor(r -> new Thread(ContextSnapshot.captureAll().wrap(r))));
        }
    }


    /**
     * NAME OF THE BEAN IS IMPORTANT!
     * <p>
     * We need to wrap this for @Async related things to propagate the context.
     *
     * @see EnableAsync
     */
    // [Observability] instrumenting executors
    @Bean(name = "taskExecutor", destroyMethod = "shutdown")
    ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler() {
            @Override
            protected ExecutorService initializeExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
                ExecutorService executorService = super.initializeExecutor(threadFactory, rejectedExecutionHandler);
                return ContextExecutorService.wrap(executorService, ContextSnapshot::captureAll);
            }
        };
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }
}
