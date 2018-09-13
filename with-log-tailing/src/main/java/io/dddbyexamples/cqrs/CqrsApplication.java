package io.dddbyexamples.cqrs;

import io.dddbyexamples.cqrs.persistence.CreditCardRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CqrsApplication {

	private final CreditCardRepository creditCardRepository;

	public CqrsApplication(CreditCardRepository creditCardRepository) {
		this.creditCardRepository = creditCardRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(CqrsApplication.class, args);
	}

}
