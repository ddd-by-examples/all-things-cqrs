package io.dddbyexamples.cqrs.sink;

class Envelope {

    private CreditCardStateChanged payload;

    public Envelope() {
    }

    public Envelope(CreditCardStateChanged payload) {
        this.payload = payload;
    }

    CreditCardStateChanged getPayload() {
        return payload;
    }

    void setPayload(CreditCardStateChanged payload) {
        this.payload = payload;
    }

    boolean isUpdate() {
        return payload.getOp().equals("u");
    }
}
