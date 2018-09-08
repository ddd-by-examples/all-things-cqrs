package io.dddbyexamples.cqrs.write.domain;

import io.dddbyexamples.cqrs.write.domain.ports.CreditCardRecord;
import io.dddbyexamples.cqrs.write.domain.produces.CardWithdrawn;

import java.math.BigDecimal;
import java.util.UUID;

class CreditCard {

    private final UUID cardId;
    private final BigDecimal initialLimit;
    private final BigDecimal usedLimit;


    CreditCard(CreditCardRecord record) {
        this.initialLimit = record.getInitialLimit();
        this.usedLimit = record.getUsedLimit();
        this.cardId = record.getId();
    }

    CardWithdrawn withdraw(BigDecimal amount) {
        if (thereIsMoneyToWithdraw(amount)) {
            return new CardWithdrawn(cardId, amount);
        } else {
            throw new NotEnoughMoneyException(cardId, amount, availableBalance());
        }
    }

    private BigDecimal availableBalance() {
        return initialLimit.subtract(usedLimit);
    }

    private boolean thereIsMoneyToWithdraw(BigDecimal amount) {
        return availableBalance().compareTo(amount) >= 0;
    }

}
