package io.dddbyexamples.cqrs.model.ports;

import java.util.Optional;
import java.util.UUID;

public interface CreditCardDao {

    Optional<CreditCardRecord> load(UUID cardId);

    void save(CreditCardRecord record);
}
