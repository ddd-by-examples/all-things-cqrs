package io.dddbyexamples.cqrs.persistence;

import io.dddbyexamples.cqrs.model.ports.CreditCardDao;
import io.dddbyexamples.cqrs.model.ports.CreditCardRecord;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class CreditCardDaoJpa implements CreditCardDao {

    private final CreditCardDaoDataJpa dataJpaDao;

    @Override
    public Optional<CreditCardRecord> load(UUID cardId) {
        return dataJpaDao.findById(cardId);
    }

    @Override
    public void save(CreditCardRecord record) {
        dataJpaDao.save(record);
    }
}
