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
public class Withdrawal {

    @Id @GeneratedValue private UUID id;
    private @Getter BigDecimal amount;
    private @Getter UUID cardId;

    public Withdrawal(BigDecimal amount, UUID cardNo) {
        this.amount = amount;
        this.cardId = cardNo;
    }
}
