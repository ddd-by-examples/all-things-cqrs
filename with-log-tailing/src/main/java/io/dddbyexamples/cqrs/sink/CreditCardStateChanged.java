package io.dddbyexamples.cqrs.sink;

class CreditCardStateChanged {

    private String op;
    private String ts_ms;
    private Card before;
    private Card after;

    String getOp() {
        return op;
    }

    void setOp(String op) {
        this.op = op;
    }

    Card getBefore() {
        return before;
    }

    void setBefore(Card before) {
        this.before = before;
    }

    String getTs_ms() {
        return ts_ms;
    }

    void setTs_ms(String ts_ms) {
        this.ts_ms = ts_ms;
    }

    Card getAfter() {
        return after;
    }

    void setAfter(Card after) {
        this.after = after;
    }
}
