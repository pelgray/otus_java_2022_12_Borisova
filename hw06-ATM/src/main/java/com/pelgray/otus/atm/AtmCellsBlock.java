package com.pelgray.otus.atm;

import com.pelgray.otus.atm.exception.IllegalCashException;
import com.pelgray.otus.atm.exception.NotEnoughCashException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Блок ячеек банкомата
 */
public class AtmCellsBlock {
    private final Map<BanknoteDenomination, AtmCell> cells;

    public AtmCellsBlock(Set<BanknoteDenomination> allowedDenominations) {
        this.cells = new TreeMap<>(Comparator.comparing(BanknoteDenomination::value, Comparator.reverseOrder()));
        for (var allowedDenomination : allowedDenominations) {
            this.cells.put(allowedDenomination, new AtmCell());
        }
    }

    public boolean isAllowedDenomination(BanknoteDenomination denomination) {
        return cells.containsKey(denomination);
    }

    public void put(Banknote banknote) {
        if (!this.isAllowedDenomination(banknote.getDenomination())) {
            throw new IllegalCashException();
        }
        var cell = cells.get(banknote.getDenomination());
        cell.put(banknote);
    }

    /**
     * @throws NotEnoughCashException если в банкомате нет подходящего номинала для выдачи наличных
     */
    public List<Banknote> get(int amount) {
        var result = new ArrayList<Banknote>();
        int amountLeft = amount;
        for (var entry : cells.entrySet()) {
            var curDenomination = entry.getKey().value();
            var curCell = entry.getValue();
            if (amountLeft >= curDenomination && curCell.isNotEmpty()) {
                var numOfBanknotes = amountLeft / curDenomination;
                result.addAll(curCell.remove(numOfBanknotes));
                amountLeft -= curDenomination * numOfBanknotes;
            }
            if (amountLeft == 0) {
                break;
            }
        }
        if (amountLeft != 0) {
            throw new NotEnoughCashException();
        }
        return result;
    }

    public List<Banknote> getAll() {
        var result = new ArrayList<Banknote>();
        for (var cell : cells.values()) {
            result.addAll(cell.removeAll());
        }
        return result;
    }

    public int getAmount() {
        int amount = 0;
        for (var entry : cells.entrySet()) {
            var denomination = entry.getKey();
            var atmCell = entry.getValue();
            amount += denomination.value() * atmCell.count();
        }
        return amount;
    }
}
