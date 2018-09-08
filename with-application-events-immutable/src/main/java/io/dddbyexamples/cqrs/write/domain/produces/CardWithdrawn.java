package io.dddbyexamples.cqrs.write.domain.produces;

import io.dddbyexamples.cqrs.DomainEvent;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class CardWithdrawn implements DomainEvent {

    private final UUID cardNo;
    private final BigDecimal amount;

}
