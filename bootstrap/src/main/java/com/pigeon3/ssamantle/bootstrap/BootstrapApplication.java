package com.pigeon3.ssamantle.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
	"com.pigeon3.ssamantle.bootstrap",
	"com.pigeon3.ssamantle.adapter.in.http",
	"com.pigeon3.ssamantle.adapter.out.rdb",
	"com.pigeon3.ssamantle.application"
})
public class BootstrapApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootstrapApplication.class, args);
	}

}
