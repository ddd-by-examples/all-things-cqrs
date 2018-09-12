package io.dddbyexamples.cqrs.model;

import io.dddbyexamples.cqrs.ui.WithdrawalCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WithdrawalProcess {

    private final CreditCardRepository repository;

    public void withdraw(WithdrawalCommand command) {
        repository.apply(command.getCard(),creditCard -> creditCard.withdraw(command));
    }
}
