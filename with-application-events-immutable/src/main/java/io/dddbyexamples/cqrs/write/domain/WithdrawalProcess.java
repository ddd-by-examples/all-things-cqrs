package io.dddbyexamples.cqrs.write.domain;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WithdrawalProcess {

    private final CreditCardRepository repository;

    public void withdraw(UUID cardId, BigDecimal amount) {
        repository.apply(cardId,creditCard -> creditCard.withdraw(amount));
    }
}
