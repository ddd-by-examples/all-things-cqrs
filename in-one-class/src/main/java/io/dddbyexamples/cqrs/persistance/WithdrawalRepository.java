package io.dddbyexamples.cqrs.persistance;

import io.dddbyexamples.cqrs.model.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, UUID> {

    List<Withdrawal> findByCardId(UUID cardId);
}
