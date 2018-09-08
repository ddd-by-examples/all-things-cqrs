package io.dddbyexamples.cqrs.write.domain.ports;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CreditCardDao extends JpaRepository<CreditCardRecord, UUID> {

}
