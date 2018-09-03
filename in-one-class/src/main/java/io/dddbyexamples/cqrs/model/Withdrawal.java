package io.dddbyexamples.cqrs.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Withdrawal {

    @Id @GeneratedValue private UUID id;
    private BigDecimal amount;
    private UUID cardId;

    Withdrawal(BigDecimal amount, UUID cardNo) {
        this.amount = amount;
        this.cardId = cardNo;
    }
}
