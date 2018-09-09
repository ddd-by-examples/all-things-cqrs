package io.dddbyexamples.cqrs.persistence;

import io.dddbyexamples.cqrs.model.ports.CreditCardRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CreditCardDaoDataJpa extends JpaRepository<CreditCardRecord, UUID> {

}
