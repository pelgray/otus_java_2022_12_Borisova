package com.pelgray.otus.atm;

import com.pelgray.otus.atm.exception.IllegalCashException;
import com.pelgray.otus.atm.exception.NotEnoughCashException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AutomaticTellerMachineTest {

    private final BanknoteDenomination d5 = new BanknoteDenomination(5);

    private final BanknoteDenomination d10 = new BanknoteDenomination(10);

    private final BanknoteDenomination d50 = new BanknoteDenomination(50);

    private final BanknoteDenomination d100 = new BanknoteDenomination(100);

    private AutomaticTellerMachine atm;

    @BeforeEach
    void setUp() {
        atm = new AutomaticTellerMachine(new AtmCellsBlock(Set.of(d5, d10, d50, d100)));
    }

    @Test
    @DisplayName("Положить 100 наличными")
    void putCash1() {
        atm.putCash(new Banknote(d100));

        assertThat(atm.getAmount()).isEqualTo(100);
    }

    @Test
    @DisplayName("Положить 100 наличными по 50")
    void putCash2() {
        atm.putCash(new Banknote(d50), new Banknote(d50));

        assertThat(atm.getAmount()).isEqualTo(100);
    }

    @Test
    @DisplayName("Положить наличные, с которыми банкомат не работает")
    void putCash3() {
        assertThrows(IllegalCashException.class, () -> atm.putCash(new Banknote(new BanknoteDenomination(150))));

        assertThat(atm.getAmount()).isEqualTo(0);
    }

    @Test
    @DisplayName("Положить несколько банкнот, одна из которых будет неподдерживаемого номинала для банкомата")
    void putCash4() {
        assertThrows(IllegalCashException.class,
                     () -> atm.putCash(new Banknote(d100), new Banknote(new BanknoteDenomination(70))));

        assertThat(atm.getAmount()).isEqualTo(100);
    }

    @Test
    @DisplayName("Снять 100 наличными")
    void getCash1() {
        var banknote = new Banknote(d100);
        atm.putCash(banknote);

        var actual = atm.getCash(100);
        assertThat(actual).hasSize(1).element(0).isEqualTo(banknote);

        assertThat(atm.getAmount()).isEqualTo(0);
    }

    @Test
    @DisplayName("Снять 130 наличными из 240 в банкомате наименьшим количеством банкнот")
    void getCash2() {
        var banknote100 = new Banknote(d100);
        var banknote50_1 = new Banknote(d50);
        var banknote50_2 = new Banknote(d50);
        var banknote10_1 = new Banknote(d10);
        var banknote10_2 = new Banknote(d10);
        var banknote10_3 = new Banknote(d10);
        var banknote5_1 = new Banknote(d5);
        var banknote5_2 = new Banknote(d5);
        // put 240
        atm.putCash(banknote100,
                    banknote50_1, banknote50_2,
                    banknote10_1, banknote10_2, banknote10_3,
                    banknote5_1, banknote5_2);

        var actual = atm.getCash(130);
        assertThat(actual).flatExtracting(Banknote::getDenomination).containsExactlyInAnyOrder(d100, d10, d10, d10);

        assertThat(atm.getAmount()).isEqualTo(110);
    }

    @Test
    @DisplayName("Ошибка при снятии заданной суммы наличных, т.к. в банкомате нет денег")
    void getCash3() {
        assertThrows(NotEnoughCashException.class, () -> atm.getCash(100));
    }

    @Test
    @DisplayName("Ошибка при снятии заданной суммы наличных, т.к. в банкомате не хватает денег")
    void getCash4() {
        var banknote = new Banknote(d100);
        atm.putCash(banknote);

        assertThrows(NotEnoughCashException.class, () -> atm.getCash(150));
    }

    @Test
    @DisplayName("Ошибка при снятии заданной суммы наличных, т.к. в банкомате нет нужного номинала для выдачи денег")
    void getCash5() {
        var banknote1 = new Banknote(d50);
        var banknote2 = new Banknote(d50);
        atm.putCash(banknote1, banknote2);

        assertThrows(NotEnoughCashException.class, () -> atm.getCash(60));
    }

    @Test
    @DisplayName("Снять все наличные")
    void getAllCash1() {
        var banknote = new Banknote(d100);
        atm.putCash(banknote);

        var actual = atm.getAllCash();
        assertThat(actual).hasSize(1).element(0).isEqualTo(banknote);

        assertThat(atm.getAmount()).isZero();
    }

    @Test
    @DisplayName("Ошибка при снятии всех наличных, т.к. в банкомате нет денег")
    void getAllCash2() {
        assertThrows(NotEnoughCashException.class, () -> atm.getAllCash());
    }
}
