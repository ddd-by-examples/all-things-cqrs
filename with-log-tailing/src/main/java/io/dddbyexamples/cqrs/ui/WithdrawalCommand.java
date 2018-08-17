package io.dddbyexamples.cqrs.ui;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalCommand {
    private String card;
    private BigDecimal amount;

}
