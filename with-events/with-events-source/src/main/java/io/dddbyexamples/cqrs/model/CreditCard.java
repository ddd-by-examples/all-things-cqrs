package io.dddbyexamples.cqrs.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class CreditCard {

    @Id @GeneratedValue @Getter private UUID id;
    private BigDecimal initialLimit;
    private BigDecimal usedLimit = BigDecimal.ZERO;

    public CreditCard(BigDecimal limit) {
        this.initialLimit = limit;
    }

    public void withdraw(BigDecimal amount) {
        if (thereIsMoneyToWithdraw(amount)) {
            usedLimit = usedLimit.add(amount);
        } else {
            throw new NotEnoughMoneyException(id, amount, availableBalance());
        }
    }

    public void chargeBack(BigDecimal amount) {
        usedLimit = usedLimit.subtract(amount);
    }

    public BigDecimal availableBalance() {
        return initialLimit.subtract(usedLimit);
    }

    private boolean thereIsMoneyToWithdraw(BigDecimal amount) {
        return availableBalance().compareTo(amount) >= 0;
    }

}
