package io.dddbyexamples.cqrs.sink;


import io.dddbyexamples.cqrs.model.CreditCard;
import io.dddbyexamples.cqrs.model.Withdrawal;
import io.dddbyexamples.cqrs.persistence.CreditCardRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ReadModelUpdaterTest {

    @Autowired TestRestTemplate restTemplate;
    @Autowired CreditCardRepository creditCardRepository;
    @Autowired Sink sink;

    @Test
    public void shouldSynchronizeQuerySideAfterLogTailing() {
        // given
        String cardUUid = thereIsCreditCardWithLimit(new BigDecimal(100));
        // when
        creditCardUpdateReadFromDbTransactionLog(TEN, cardUUid);
        // then
        thereIsOneWithdrawalOf(TEN, cardUUid);
    }

    String thereIsCreditCardWithLimit(BigDecimal limit) {
        CreditCard creditCard = new CreditCard(limit);
        return creditCardRepository.save(creditCard).getId();
    }

    void creditCardUpdateReadFromDbTransactionLog(BigDecimal usedLimitAfter, String cardUUid) {
        Card before = createCardDbRepresentation(cardUUid, ZERO);
        Card after = createCardDbRepresentation(cardUUid, usedLimitAfter);
        CreditCardStateChanged message = new CreditCardStateChanged();
        message.setOp("u");
        message.setBefore(before);
        message.setAfter(after);
        sink.input().send(new GenericMessage<>(new Envelope(message)));
    }

    Card createCardDbRepresentation(String cardUUid, BigDecimal val) {
        Card before = new Card();
        before.setId(cardUUid);
        before.setUsed_limit(new String(Base64.getEncoder().encode(val.multiply(new BigDecimal(100)).unscaledValue().toByteArray())));
        return before;
    }

    void thereIsOneWithdrawalOf(BigDecimal amount, String cardId) {
        Map<String, Object> params = new HashMap<>();
        params.put("uuid", cardId);
        List<Withdrawal> withdrawals =
                restTemplate.exchange(
                        "/withdrawals?cardId={uuid}",
                        GET, null,
                        new ParameterizedTypeReference<List<Withdrawal>>() {},
                        params)
                        .getBody();
        assertThat(withdrawals).hasSize(1);
        assertThat(withdrawals.get(0).getAmount()).isEqualByComparingTo(amount);
    }


}