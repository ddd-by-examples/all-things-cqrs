package io.dddbyexamples.cqrs.persistance;

import io.dddbyexamples.cqrs.model.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, String> {

    List<Withdrawal> findByCardId(String cardId);
}
