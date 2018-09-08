package io.dddbyexamples.cqrs.write.domain;

import io.dddbyexamples.cqrs.write.domain.consumes.WithdrawalCommand;
import io.dddbyexamples.cqrs.write.domain.ports.CreditCardRecord;
import io.dddbyexamples.cqrs.write.domain.produces.CardWithdrawn;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CreditCardTest {

    @Test
    public void shouldNotBeAbleToWithdrawWhenThereIsNotEnoughMoney() {
        //given
        UUID cardId = UUID.randomUUID();
        CreditCard creditCard = new CreditCard(new CreditCardRecord(cardId, TEN));

        //expect
        assertThatExceptionOfType(NotEnoughMoneyException.class)
            .isThrownBy(
                () -> creditCard.withdraw(new WithdrawalCommand(cardId, new BigDecimal(100))));
    }

    @Test
    public void shouldBeAbleToWithdrawMoney() {
        //given
        UUID cardId = UUID.randomUUID();
        CreditCard creditCard = new CreditCard(new CreditCardRecord(cardId, TEN));

        //when
        CardWithdrawn event = creditCard.withdraw(new WithdrawalCommand(cardId,ONE));

        //expect
        assertThat(event).isEqualTo(new CardWithdrawn(cardId, ONE.setScale(2, RoundingMode.UNNECESSARY)));
    }

}