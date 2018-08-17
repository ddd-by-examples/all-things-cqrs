package io.dddbyexamples.cqrs.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class Withdrawal {

    @Id private String id;
    private @Getter BigDecimal amount;
    private String cardId;

    public Withdrawal(BigDecimal amount, String cardNo) {
        this.id = UUID.randomUUID().toString();
        this.amount = amount;
        this.cardId = cardNo;
    }
}
