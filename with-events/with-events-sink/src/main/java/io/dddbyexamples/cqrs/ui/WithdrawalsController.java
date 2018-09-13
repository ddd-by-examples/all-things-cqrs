package io.dddbyexamples.cqrs.ui;

import io.dddbyexamples.cqrs.persistence.WithdrawalsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.websocket.server.PathParam;

@RestController("/withdrawals")
class WithdrawalsController {

    private final WithdrawalsRepository withdrawalsRepository;

    WithdrawalsController(WithdrawalsRepository withdrawalsRepository) {
        this.withdrawalsRepository = withdrawalsRepository;
    }

    @GetMapping
    Flux<WithdrawalDto> withdrawals(@PathParam("cardId") String cardId) {
        return withdrawalsRepository.findAllByCard(cardId);
    }


}

