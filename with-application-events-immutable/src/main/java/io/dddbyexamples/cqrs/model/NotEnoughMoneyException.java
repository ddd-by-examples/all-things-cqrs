package io.dddbyexamples.cqrs.model;

import java.math.BigDecimal;
import java.util.UUID;

public class NotEnoughMoneyException extends RuntimeException {

    NotEnoughMoneyException(UUID cardNo, BigDecimal wanted, BigDecimal availableBalance) {
        super(String.format("Card %s not able to withdraw %s. Balance is %s", cardNo, wanted, availableBalance));
    }


}
