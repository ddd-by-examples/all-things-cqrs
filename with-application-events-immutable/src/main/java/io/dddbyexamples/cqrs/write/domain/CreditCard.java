package io.dddbyexamples.cqrs.write.domain;

import io.dddbyexamples.cqrs.write.domain.consumes.WithdrawalCommand;
import io.dddbyexamples.cqrs.write.domain.ports.CreditCardRecord;
import io.dddbyexamples.cqrs.write.domain.produces.CardWithdrawn;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

class CreditCard {

    private final CurrencyUnit DEFAULT_CURRENCY = Monetary.getCurrency("USD");
    private final UUID cardId;
    private final MonetaryAmount usedLimit;
    private final MonetaryAmount initialLimit;


    CreditCard(CreditCardRecord record) {
        this.cardId = record.getId();
        this.usedLimit = Money.of(record.getUsedLimit(),DEFAULT_CURRENCY);
        this.initialLimit = Money.of(record.getInitialLimit(), DEFAULT_CURRENCY);
    }

    CardWithdrawn withdraw(WithdrawalCommand command) {
        MonetaryAmount amount = command.getMonetaryAmount(DEFAULT_CURRENCY);
        if (thereIsMoneyToWithdraw(amount)) {
            return new CardWithdrawn(cardId, toBigDecimal(amount));
        } else {
            throw new NotEnoughMoneyException(cardId, command.getAmount(), toBigDecimal(availableBalance()));
        }
    }

    private MonetaryAmount availableBalance() {
        return initialLimit.subtract(usedLimit);
    }

    private boolean thereIsMoneyToWithdraw(MonetaryAmount amount) {
        return availableBalance().isGreaterThanOrEqualTo(amount);
    }

    private BigDecimal toBigDecimal(final MonetaryAmount amount) {
        final BigDecimal decimal = amount.getNumber().numberValueExact(BigDecimal.class);
        final int defaultFractionDigits = amount.getCurrency().getDefaultFractionDigits();
        final int scale = Math.max(decimal.scale(), defaultFractionDigits);

        return decimal.setScale(scale, RoundingMode.UNNECESSARY);
    }
}
