package io.dddbyexamples.cqrs.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface DomainEventsStorage extends CrudRepository<StoredDomainEvent, Long> {
    List<StoredDomainEvent> findAllBySentOrderByEventTimestampDesc(boolean sent);
}