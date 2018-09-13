package io.dddbyexamples.cqrs.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Entity
class StoredDomainEvent {

    @Id String id;
    private String content;
    private boolean sent;
    private Instant eventTimestamp = Instant.now();
    private String eventType;

    StoredDomainEvent(String content, String eventType) {
        this.content = content;
        this.id = UUID.randomUUID().toString();
        this.eventType = eventType;
    }

    private StoredDomainEvent() {
    }

    void sent() {
        sent = true;
    }

    String getContent() {
        return content;
    }

    public String getEventType() {
        return eventType;
    }
}