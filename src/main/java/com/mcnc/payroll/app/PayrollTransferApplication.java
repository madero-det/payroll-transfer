package com.mcnc.payroll.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
@ComponentScan("com.mcnc.payroll.config")
@ComponentScan("com.mcnc.payroll.service")
@ComponentScan("com.mcnc.payroll.rest.service")
@ComponentScan("com.mcnc.payroll.controller")
public class PayrollTransferApplication {
	public static void main(String[] args) {
		SpringApplication.run(PayrollTransferApplication.class, args);
	}
}
