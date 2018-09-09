package io.dddbyexamples.cqrs.ui;

import io.dddbyexamples.cqrs.model.WithdrawalProcess;
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
    private final WithdrawalReader finder;

    @PostMapping
    ResponseEntity withdraw(@RequestBody WithdrawalCommand withdrawalCommand) {
        withdrawalProcess.withdraw(withdrawalCommand);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    ResponseEntity<List<WithdrawalReadModel>> withdrawals(@PathParam("cardId") String cardId) {
        return ResponseEntity.ok().body(finder.loadWithdrawalsFor(UUID.fromString(cardId)));
    }

}
