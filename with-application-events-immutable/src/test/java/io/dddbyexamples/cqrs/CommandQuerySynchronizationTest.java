package io.dddbyexamples.cqrs;

import io.dddbyexamples.cqrs.ui.WithdrawalsReadModel;
import io.dddbyexamples.cqrs.ui.WithdrawalCommand;
import io.dddbyexamples.cqrs.model.ports.CreditCardDao;
import io.dddbyexamples.cqrs.model.ports.CreditCardRecord;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CommandQuerySynchronizationTest {

    @Autowired TestRestTemplate restTemplate;
    @Autowired CreditCardDao creditCardDao;

    @Test
    public void shouldSynchronizeQuerySideAfterSendingACommand() {
        // given
        BigDecimal amount = new BigDecimal("12.22");

        UUID cardUUid = thereIsCreditCardWithLimit(new BigDecimal(100));
        // when
        clientWantsToWithdraw(amount, cardUUid);
        // then
        thereIsOneWithdrawalOf(amount, cardUUid);
    }

    private UUID thereIsCreditCardWithLimit(BigDecimal limit) {
        UUID id = UUID.randomUUID();
        CreditCardRecord creditCard = new CreditCardRecord(id,limit);
        creditCardDao.save(creditCard);
        return id;
    }

    private void clientWantsToWithdraw(BigDecimal amount, UUID cardId) {
        restTemplate.postForEntity("/withdrawals", new WithdrawalCommand(cardId, amount), Void.class);
    }

    private void thereIsOneWithdrawalOf(BigDecimal amount, UUID cardId) {
        Map<String, Object> params = new HashMap<>();
        params.put("uuid", cardId);
        List<WithdrawalsReadModel> withdrawals =
                restTemplate.exchange(
                        "/withdrawals?cardId={uuid}",
                        GET, null,
                        new ParameterizedTypeReference<List<WithdrawalsReadModel>>() {
                        },
                        params)
                        .getBody();
        assertThat(withdrawals).containsOnly(new WithdrawalsReadModel(cardId,amount));
    }

}
