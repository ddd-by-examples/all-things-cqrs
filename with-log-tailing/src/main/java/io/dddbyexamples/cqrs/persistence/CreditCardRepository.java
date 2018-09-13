package io.dddbyexamples.cqrs.persistence;

import io.dddbyexamples.cqrs.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository extends JpaRepository<CreditCard, String> {

}
