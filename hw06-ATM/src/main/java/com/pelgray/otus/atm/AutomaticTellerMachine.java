package com.pelgray.otus.atm;

import com.pelgray.otus.atm.exception.IllegalCashException;
import com.pelgray.otus.atm.exception.NotEnoughCashException;

import java.util.List;

/**
 * Банкомат
 */
public class AutomaticTellerMachine {
    private final AtmCellsBlock cellsBlock;

    public AutomaticTellerMachine(AtmCellsBlock atmCellsBlock) {
        this.cellsBlock = atmCellsBlock;
    }

    public void putCash(Banknote banknote) {
        if (banknote == null) {
            throw new IllegalCashException();
        }
        cellsBlock.put(banknote);
    }

    public void putCash(Banknote... banknotes) {
        if (banknotes.length == 0) {
            throw new IllegalCashException();
        }
        for (var banknote : banknotes) {
            putCash(banknote);
        }
    }

    /**
     * @throws NotEnoughCashException если в банкомате не хватает средств для выдачи наличных
     */
    public List<Banknote> getCash(int amount) {
        if (getAmount() < amount) {
            throw new NotEnoughCashException();
        }
        return cellsBlock.get(amount);
    }

    /**
     * @throws NotEnoughCashException если в банкомате нет наличных
     */
    public List<Banknote> getAllCash() {
        if (getAmount() == 0) {
            throw new NotEnoughCashException();
        }
        return cellsBlock.getAll();
    }

    public int getAmount() {
        return cellsBlock.getAmount();
    }

}
