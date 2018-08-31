package io.dddbyexamples.cqrs.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class CreditCard {

    @Id @GeneratedValue @Getter
    private UUID id;

    private BigDecimal initialLimit;

    private BigDecimal usedLimit = BigDecimal.ZERO;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cardId")
    private List<Withdrawal> withdrawals = new ArrayList<>();

    public CreditCard(BigDecimal limit) {
        this.initialLimit = limit;
    }

    public void withdraw(BigDecimal amount) {
        if (thereIsMoneyToWithdraw(amount)) {
            usedLimit = usedLimit.add(amount);
            withdrawals.add(new Withdrawal(amount, id));
        } else {
            throw new NotEnoughMoneyException(id, amount, availableBalance());
        }
    }

    public BigDecimal availableBalance() {
        return initialLimit.subtract(usedLimit);
    }

    private boolean thereIsMoneyToWithdraw(BigDecimal amount) {
        return availableBalance().compareTo(amount) >= 0;
    }

    public List<Withdrawal> getWithdrawals() {
        return Collections.unmodifiableList(withdrawals);
    }
}
