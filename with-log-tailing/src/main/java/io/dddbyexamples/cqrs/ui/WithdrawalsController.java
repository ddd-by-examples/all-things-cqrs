package io.dddbyexamples.cqrs.ui;

import io.dddbyexamples.cqrs.application.WithdrawalProcess;
import io.dddbyexamples.cqrs.model.Withdrawal;
import io.dddbyexamples.cqrs.persistence.WithdrawalRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController("/withdrawals")
class WithdrawalsController {

    private final WithdrawalRepository withdrawalRepository;
    private final WithdrawalProcess withdrawalProcess;

    WithdrawalsController(WithdrawalRepository withdrawalRepository, WithdrawalProcess withdrawalsProcess) {
        this.withdrawalRepository = withdrawalRepository;
        this.withdrawalProcess = withdrawalsProcess;
    }

    @PostMapping
    ResponseEntity withdraw(@RequestBody WithdrawalCommand withdrawalCommand) {
        withdrawalProcess.withdraw(withdrawalCommand.getCard(), withdrawalCommand.getAmount());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    ResponseEntity<List<Withdrawal>> withdrawals(@PathParam("cardId") String cardId) {
        return ResponseEntity.ok().body(withdrawalRepository.findByCardId(cardId));
    }
}

