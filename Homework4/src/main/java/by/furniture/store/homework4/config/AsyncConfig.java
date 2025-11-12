package by.furniture.store.homework4.config;

import by.furniture.store.homework4.util.Randomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean("storeTaskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Randomizer.CUSTOMERS_COUNT + Randomizer.WORKERS_COUNT);
        executor.setMaxPoolSize(Randomizer.CUSTOMERS_COUNT + Randomizer.WORKERS_COUNT);
        executor.setQueueCapacity(0);
        executor.setThreadNamePrefix("Store-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(Randomizer.FINISH_WAITING_TIME);
        executor.initialize();
        return executor;
    }
}
