package io.dddbyexamples.cqrs.ui;

import io.dddbyexamples.cqrs.application.WithdrawalProcess;
import io.dddbyexamples.cqrs.read.WithdrawalFinder;
import io.dddbyexamples.cqrs.read.WithdrawalReadModel;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@RestController("/withdrawals")
class WithdrawalsController {

    private final WithdrawalProcess withdrawalProcess;
    private final WithdrawalFinder finder;

    @PostMapping
    ResponseEntity withdraw(@RequestBody WithdrawalCommand withdrawalCommand) {
        withdrawalProcess.withdraw(withdrawalCommand.getCard(), withdrawalCommand.getAmount());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    ResponseEntity<List<WithdrawalReadModel>> withdrawals(@PathParam("cardId") String cardId) {
        return ResponseEntity.ok().body(finder.loadWithdrawalsFor(UUID.fromString(cardId)));
    }

}
