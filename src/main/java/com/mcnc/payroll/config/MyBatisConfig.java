package com.mcnc.payroll.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.mcnc.payroll.mapper") // Ensure this matches your mapper package
public class MyBatisConfig {
	// No need to define DataSource and SqlSessionFactory beans if using Spring Boot auto-configuration
}
