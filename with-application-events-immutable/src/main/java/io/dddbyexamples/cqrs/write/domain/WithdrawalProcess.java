package io.dddbyexamples.cqrs.write.domain;

import io.dddbyexamples.cqrs.write.domain.consumes.WithdrawalCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WithdrawalProcess {

    private final CreditCardRepository repository;

    public void withdraw(WithdrawalCommand command) {
        repository.apply(command.getCard(),creditCard -> creditCard.withdraw(command));
    }
}
