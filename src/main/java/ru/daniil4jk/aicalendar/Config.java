package ru.daniil4jk.aicalendar;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Config {

    @Bean
    @Lazy
    public ExecutorService executorService() {
        return Executors.newCachedThreadPool();
    }
}
