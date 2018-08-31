package io.dddbyexamples.cqrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableBinding(Source.class)
public class CqrsSourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CqrsSourceApplication.class, args);
	}
}
