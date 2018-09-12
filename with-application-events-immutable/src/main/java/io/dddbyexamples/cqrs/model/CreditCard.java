package io.dddbyexamples.cqrs.model;

import io.dddbyexamples.cqrs.model.ports.CreditCardRecord;
import io.dddbyexamples.cqrs.ui.WithdrawalCommand;

import java.math.BigDecimal;
import java.util.UUID;

class CreditCard {

    private final UUID cardId;
    private final BigDecimal usedLimit;
    private final BigDecimal initialLimit;


    CreditCard(CreditCardRecord record) {
        this.cardId = record.getId();
        this.usedLimit = record.getUsedLimit();
        this.initialLimit = record.getInitialLimit();
    }

    CardWithdrawn withdraw(WithdrawalCommand command) {
        BigDecimal amount = command.getAmount();
        if (thereIsMoneyToWithdraw(amount)) {
            return new CardWithdrawn(cardId, amount);
        } else {
            throw new NotEnoughMoneyException(cardId, command.getAmount(), availableBalance());
        }
    }

    private BigDecimal availableBalance() {
        return initialLimit.subtract(usedLimit);
    }

    private boolean thereIsMoneyToWithdraw(BigDecimal amount) {
        return availableBalance().compareTo(amount) >= 0;
    }

}
