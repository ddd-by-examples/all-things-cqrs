package io.dddbyexamples.cqrs;

import io.dddbyexamples.cqrs.read.WithdrawalReadModel;
import io.dddbyexamples.cqrs.write.domain.ports.CreditCardDao;
import io.dddbyexamples.cqrs.write.domain.consumes.WithdrawalCommand;
import io.dddbyexamples.cqrs.write.domain.ports.CreditCardRecord;
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
    @Autowired
    CreditCardDao creditCardDao;

    @Test
    public void shouldSynchronizeQuerySideAfterSendingACommand() {
        // given
        UUID cardUUid = thereIsCreditCardWithLimit(new BigDecimal(100));
        // when
        clientWantsToWithdraw(TEN, cardUUid);
        // then
        thereIsOneWithdrawalOf(TEN, cardUUid);
    }

    private UUID thereIsCreditCardWithLimit(BigDecimal limit) {
        CreditCardRecord creditCard = new CreditCardRecord(limit);
        return creditCardDao.save(creditCard).getId();
    }

    private void clientWantsToWithdraw(BigDecimal amount, UUID cardId) {
        restTemplate.postForEntity("/withdrawals", new WithdrawalCommand(cardId, amount), Void.class);
    }

    private void thereIsOneWithdrawalOf(BigDecimal amount, UUID cardId) {
        Map<String, Object> params = new HashMap<>();
        params.put("uuid", cardId);
        List<WithdrawalReadModel> withdrawals =
                restTemplate.exchange(
                        "/withdrawals?cardId={uuid}",
                        GET, null,
                        new ParameterizedTypeReference<List<WithdrawalReadModel>>() {
                        },
                        params)
                        .getBody();
        assertThat(withdrawals).hasSize(1);
        assertThat(withdrawals.get(0).getAmount()).isEqualByComparingTo(amount);
    }

}
