package io.dddbyexamples.cqrs.persistence;

import io.dddbyexamples.cqrs.model.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, UUID> {

    List<Withdrawal> findByCardId(UUID cardId);
}
