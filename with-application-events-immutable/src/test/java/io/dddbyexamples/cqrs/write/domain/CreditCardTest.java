package io.dddbyexamples.cqrs.write.domain;

import io.dddbyexamples.cqrs.write.domain.ports.CreditCardRecord;
import io.dddbyexamples.cqrs.write.domain.produces.CardWithdrawn;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CreditCardTest {

    @Test
    public void shouldNotBeAbleToWithdrawWhenThereIsNotEnoughMoney() {
        //given
        CreditCardRecord record = new CreditCardRecord(UUID.randomUUID(),TEN);
        CreditCard creditCard = new CreditCard(record);

        //expect
        assertThatExceptionOfType(NotEnoughMoneyException.class)
                .isThrownBy(
                () -> creditCard.withdraw(new BigDecimal(100)));
    }

    @Test
    public void shouldBeAbleToWithdrawMoney() {
        //given
        UUID cardId = UUID.randomUUID();
        CreditCardRecord record = new CreditCardRecord(cardId,TEN);
        CreditCard creditCard = new CreditCard(record);

        //when
        CardWithdrawn event = creditCard.withdraw(ONE);

        //expect
        assertThat(event).isEqualTo(new CardWithdrawn(cardId, ONE));
    }

}