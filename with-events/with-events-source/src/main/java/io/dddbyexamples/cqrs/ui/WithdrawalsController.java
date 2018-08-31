package io.dddbyexamples.cqrs.ui;

import io.dddbyexamples.cqrs.application.WithdrawalProcess;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/withdrawals")
class WithdrawalsController {

    private final WithdrawalProcess withdrawalProcess;

    WithdrawalsController(WithdrawalProcess withdrawalsProcess) {
        this.withdrawalProcess = withdrawalsProcess;
    }

    @PostMapping
    ResponseEntity withdraw(@RequestBody WithdrawalCommand withdrawalCommand) {
        withdrawalProcess.withdraw(withdrawalCommand.getCard(), withdrawalCommand.getAmount());
        return ResponseEntity.ok().build();
    }

}

