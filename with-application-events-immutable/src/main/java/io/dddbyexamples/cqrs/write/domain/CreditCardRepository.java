package io.dddbyexamples.cqrs.write.domain;

import io.dddbyexamples.cqrs.DomainEvent;
import io.dddbyexamples.cqrs.write.domain.ports.CreditCardDao;
import io.dddbyexamples.cqrs.write.domain.ports.CreditCardRecord;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;
import java.util.function.Function;

@Component
@AllArgsConstructor
class CreditCardRepository {

    private final CreditCardDao dao;
    private final ApplicationEventPublisher publisher;

    @Transactional
    void apply(UUID cardId, Function<CreditCard, DomainEvent> function) {

        CreditCardRecord record = dao.findById(cardId)
            .orElseThrow(() -> new IllegalStateException("Cannot find card with id " + cardId));
        DomainEvent event = function.apply(new CreditCard(record));
        record.apply(event);
        publisher.publishEvent(event);
        dao.save(record);
    }

}
