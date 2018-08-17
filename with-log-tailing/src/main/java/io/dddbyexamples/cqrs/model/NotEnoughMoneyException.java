package io.dddbyexamples.cqrs.model;

import java.math.BigDecimal;

class NotEnoughMoneyException extends RuntimeException {

    NotEnoughMoneyException(String cardNo, BigDecimal wanted, BigDecimal availableBalance) {
        super(String.format("Card %s not able to withdraw %s. Balance is %s", cardNo, wanted, availableBalance));
    }


}
