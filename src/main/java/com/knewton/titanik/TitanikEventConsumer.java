package com.knewton.titanik;

/**
 */
public interface TitanikEventConsumer {
    void consume(TitanikTransactionEvent titanikTransactionEvent);
}
