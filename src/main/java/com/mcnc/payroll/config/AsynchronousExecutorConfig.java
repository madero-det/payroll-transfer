package com.mcnc.payroll.config;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsynchronousExecutorConfig {

	@Value("${spring.task.execution.pool.max-size}")
	private int maxPoolSize;

	@Value("${spring.task.execution.pool.core-size}")
	private int corePoolSize;

	@Value("${spring.task.execution.thread-name-prefix}")
	private String threadNamePrefix;

	@Bean(name = "payrollExecutor")
	Executor payrollExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setMaxPoolSize(maxPoolSize);
		executor.setCorePoolSize(corePoolSize);
		executor.setThreadNamePrefix(threadNamePrefix);
		executor.initialize();
		return executor;
	}

}
