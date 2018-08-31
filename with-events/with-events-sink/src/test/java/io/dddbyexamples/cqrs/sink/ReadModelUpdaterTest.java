package io.dddbyexamples.cqrs.sink;

import io.dddbyexamples.cqrs.ui.WithdrawalDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.http.MediaType;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.math.BigDecimal.TEN;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ReadModelUpdaterTest {

    private final UUID cardID = UUID.randomUUID();

    @Autowired Sink sink;
    @Autowired WebTestClient webTestClient;

    @Test
    public void shouldSeeWithdrawalAfterGettingAnEvent() {
        //when
        anEventAboutWithdrawalCame(TEN, cardID);

        //then
        thereIsOneWithdrawalOf(TEN, cardID);
    }

    void anEventAboutWithdrawalCame(BigDecimal amount, UUID cardId) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "card-withdrawn");
        sink.input().send(new GenericMessage<>(new CardWithdrawn(cardId, amount), headers));
    }

    void thereIsOneWithdrawalOf(BigDecimal amount, UUID cardId) {
        this.webTestClient
                .get()
                .uri("/withdrawals?cardId=" + cardId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(WithdrawalDto.class)
                .hasSize(1);
    }
}