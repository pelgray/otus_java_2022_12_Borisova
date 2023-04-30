package com.pelgray.otus.atm;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Банкнота
 */
@EqualsAndHashCode
public class Banknote {
    private final long id = System.currentTimeMillis();

    @Getter
    private final BanknoteDenomination denomination;

    public Banknote(BanknoteDenomination denomination) {
        this.denomination = denomination;
    }
}
