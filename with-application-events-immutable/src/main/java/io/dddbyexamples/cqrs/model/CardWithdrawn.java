package io.dddbyexamples.cqrs.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class CardWithdrawn {

    private UUID cardNo;
    private BigDecimal amount;
    private Instant timestamp = Instant.now();

    public CardWithdrawn(UUID cardNo, BigDecimal amount) {
        this.cardNo = cardNo;
        this.amount = amount;
    }


    public UUID getCardNo() {
        return cardNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

}
