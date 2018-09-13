package io.dddbyexamples.cqrs.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dddbyexamples.cqrs.model.DomainEvent;
import io.dddbyexamples.cqrs.model.DomainEventPublisher;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaDomainEventPublisher implements DomainEventPublisher {

    private final Source source;
    private final DomainEventsStorage domainEventStorage;
    private final ObjectMapper objectMapper;

    public KafkaDomainEventPublisher(Source source, DomainEventsStorage domainEventStorage, ObjectMapper objectMapper) {
        this.source = source;
        this.domainEventStorage = domainEventStorage;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(DomainEvent domainEvent) {
        try {
            domainEventStorage.save(new StoredDomainEvent(objectMapper.writeValueAsString(domainEvent), domainEvent.getType()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @Scheduled(fixedRate = 2000)
    @Transactional
    public void publishExternally() {
        domainEventStorage
                .findAllBySentOrderByEventTimestampDesc(false)
                .forEach(event -> {
                            Map<String, Object> headers = new HashMap<>();
                            headers.put("type", event.getEventType());
                            source.output().send(new GenericMessage<>(event.getContent(), headers));
                            event.sent();
                        }

                );
    }
}
