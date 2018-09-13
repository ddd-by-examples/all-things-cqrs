package io.dddbyexamples.cqrs;

import io.dddbyexamples.cqrs.model.CreditCard;
import io.dddbyexamples.cqrs.persistence.CreditCardRepository;
import io.dddbyexamples.cqrs.ui.WithdrawalDto;
import io.dddbyexamples.cqrs.ui.WithdrawalCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CommandQuerySynchronizationTest {

    @Autowired TestRestTemplate restTemplate;
    @Autowired CreditCardRepository creditCardRepository;

    @Test
    public void shouldSynchronizeQuerySideAfterSendingACommand() {
        // given
        UUID cardUUid = thereIsCreditCardWithLimit(new BigDecimal(100));
        // when
        clientWantsToWithdraw(TEN, cardUUid);
        // then
        thereIsOneWithdrawalOf(TEN, cardUUid);
    }

    UUID thereIsCreditCardWithLimit(BigDecimal limit) {
        CreditCard creditCard = new CreditCard(limit);
        return creditCardRepository.save(creditCard).getId();
    }

    void clientWantsToWithdraw(BigDecimal amount, UUID cardId) {
        restTemplate.postForEntity("/withdrawals", new WithdrawalCommand(cardId, amount), Void.class);
    }

    void thereIsOneWithdrawalOf(BigDecimal amount, UUID cardId) {
        Map<String, Object> params = new HashMap<>();
        params.put("uuid", cardId);
        List<WithdrawalDto> withdrawals =
                restTemplate.exchange(
                        "/withdrawals?cardId={uuid}",
                        GET, null,
                        new ParameterizedTypeReference<List<WithdrawalDto>>() {
                        },
                        params)
                        .getBody();
        assertThat(withdrawals).hasSize(1);
        assertThat(withdrawals.get(0).getAmount()).isEqualByComparingTo(amount);
    }

}
