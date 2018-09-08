package io.dddbyexamples.cqrs.write.domain;

import io.dddbyexamples.cqrs.write.domain.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CreditCardRepository extends JpaRepository<CreditCard, UUID> {

}
