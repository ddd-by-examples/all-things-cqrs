package io.dddbyexamples.cqrs.read;

import io.dddbyexamples.cqrs.write.domain.CardWithdrawn;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
class WithdrawalProjection {

    private final JdbcTemplate jdbcTemplate;

    WithdrawalProjection(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener
    public void addWithdrawalOnCardWithdrawn(CardWithdrawn event) {
        jdbcTemplate.update("INSERT INTO WITHDRAWAL(ID, CARD_ID, AMOUNT) VALUES (?,?,?)", UUID.randomUUID(), event.getCardNo(), event.getAmount());
    }
}
