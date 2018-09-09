package io.dddbyexamples.cqrs.ui;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalCommand {
    private UUID card;
    private BigDecimal amount;

}
