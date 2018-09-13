package io.dddbyexamples.cqrs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dddbyexamples.cqrs.persistence.CreditCardRepository;
import io.dddbyexamples.cqrs.model.CardWithdrawn;
import io.dddbyexamples.cqrs.model.CreditCard;
import io.dddbyexamples.cqrs.ui.WithdrawalCommand;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Duration.FIVE_SECONDS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class EventsPublishingTest {

    @Autowired TestRestTemplate restTemplate;
    @Autowired CreditCardRepository creditCardRepository;
    @Autowired MessageCollector messageCollector;
    @Autowired Source source;
    @Autowired ObjectMapper objectMapper;

    BlockingQueue<Message<?>> events;

    @Before
    public void setup() {
        events = messageCollector.forChannel(source.output());
    }

    @Test
    public void shouldEventuallySendAnEventAboutCardWithdrawal() throws IOException {
        // given
        UUID cardUUid = thereIsCreditCardWithLimit(new BigDecimal(100));
        // when
        clientWantsToWithdraw(TEN, cardUUid);
        // then
        await().atMost(FIVE_SECONDS).until(() -> eventAboutWithdrawalWasSent(TEN, cardUUid));
    }

    UUID thereIsCreditCardWithLimit(BigDecimal limit) {
        CreditCard creditCard = new CreditCard(limit);
        return creditCardRepository.save(creditCard).getId();
    }

    void clientWantsToWithdraw(BigDecimal amount, UUID cardId) {
        restTemplate.postForEntity("/withdrawals", new WithdrawalCommand(cardId, amount), Void.class);
    }

    boolean eventAboutWithdrawalWasSent(BigDecimal amount, UUID cardId) throws IOException {
        Message msg = events.poll();
        if (msg == null) {
            return false;
        }
        String payload = msg.getPayload().toString();
        CardWithdrawn event = objectMapper.readValue(payload, CardWithdrawn.class);
        return event.getAmount().compareTo(amount) == 0 && event.getCardNo().equals(cardId);
    }

}
