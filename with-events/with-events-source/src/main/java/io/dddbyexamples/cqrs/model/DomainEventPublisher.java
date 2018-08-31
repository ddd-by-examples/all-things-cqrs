package io.dddbyexamples.cqrs.model;

public interface DomainEventPublisher {

    void publish(DomainEvent event);

}
