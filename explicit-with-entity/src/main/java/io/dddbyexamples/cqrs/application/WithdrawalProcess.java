package io.dddbyexamples.cqrs.application;

import io.dddbyexamples.cqrs.model.CreditCard;
import io.dddbyexamples.cqrs.model.Withdrawal;
import io.dddbyexamples.cqrs.persistence.CreditCardRepository;
import io.dddbyexamples.cqrs.persistence.WithdrawalRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WithdrawalProcess {

    private final CreditCardRepository creditCardRepository;
    private final WithdrawalRepository withdrawalRepository;

    WithdrawalProcess(CreditCardRepository creditCardRepository, WithdrawalRepository withdrawalRepository) {
        this.creditCardRepository = creditCardRepository;
        this.withdrawalRepository = withdrawalRepository;
    }

    @Transactional
    public void withdraw(UUID cardId, BigDecimal amount) {
        CreditCard creditCard = creditCardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalStateException("Cannot find card with id " + cardId));
        creditCard.withdraw(amount);
        withdrawalRepository.save(new Withdrawal(amount, cardId));
    }

}
