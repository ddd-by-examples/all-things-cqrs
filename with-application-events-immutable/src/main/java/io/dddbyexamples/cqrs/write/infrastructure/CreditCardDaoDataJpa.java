package io.dddbyexamples.cqrs.write.infrastructure;

import io.dddbyexamples.cqrs.write.domain.ports.CreditCardRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CreditCardDaoDataJpa extends JpaRepository<CreditCardRecord, UUID> {

}
