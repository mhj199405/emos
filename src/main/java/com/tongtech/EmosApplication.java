package com.tongtech;

import com.tongtech.common.AnalyzeMethodUrlProperties;
import com.tongtech.common.ProcessMethodUrlProperties;
import com.tongtech.common.RuleMethodUrlProperties;
import com.tongtech.common.StorageProperties;
import com.tongtech.service.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = "com.tongtech")
@EntityScan(basePackages = { "com.tongtech.*.**" })
@EnableConfigurationProperties(value = {StorageProperties.class, RuleMethodUrlProperties.class, ProcessMethodUrlProperties.class, AnalyzeMethodUrlProperties.class})
public class EmosApplication
{
	public static void main( String[] args )
	{
		SpringApplication.run(EmosApplication.class,args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}

}

