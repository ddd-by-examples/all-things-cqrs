package io.dddbyexamples.cqrs.sink;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Base64;

class Card {

    private String id;
    private String used_limit;

    BigDecimal getUsed_limit() {
        return new BigDecimal(new BigInteger(Base64.getDecoder().decode(used_limit)), 2);
    }

    void setUsed_limit(String used_limit) {
        this.used_limit = used_limit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
