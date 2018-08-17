package io.dddbyexamples.cqrs.persistance;

import io.dddbyexamples.cqrs.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository extends JpaRepository<CreditCard, String> {

}
