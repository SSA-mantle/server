package com.pigeon3.ssamantle.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {
        "com.pigeon3.ssamantle.adapter.in.http",
        "com.pigeon3.ssamantle.adapter.in.scheduler",
        "com.pigeon3.ssamantle.adapter.out.inference",
        "com.pigeon3.ssamantle.adapter.out.inmemory",
        "com.pigeon3.ssamantle.adapter.out.rdb",
        "com.pigeon3.ssamantle.application",
        "com.pigeon3.ssamantle.bootstrap"
})
public class BootstrapApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootstrapApplication.class, args);
	}

}
