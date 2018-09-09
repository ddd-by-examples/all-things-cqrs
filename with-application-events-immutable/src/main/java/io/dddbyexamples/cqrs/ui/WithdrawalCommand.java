package io.dddbyexamples.cqrs.ui;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalCommand {
    private UUID card;
    private BigDecimal amount;

    public MonetaryAmount getMonetaryAmount(CurrencyUnit DEFAULT_CURRENCY) {
        return Money.of(amount,DEFAULT_CURRENCY);
    }
}
