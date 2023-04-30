package com.pelgray.otus.atm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Ячейка банкомата
 */
public class AtmCell {
    Deque<Banknote> banknotes = new ArrayDeque<>();

    public void add(Banknote banknote) {
        banknotes.push(banknote);
    }

    public int count() {
        return banknotes.size();
    }

    public List<Banknote> get(int number) {
        var result = new ArrayList<Banknote>(number);
        for (int i = 0; i < number; i++) {
            result.add(banknotes.pop());
        }
        return result;
    }

    public List<Banknote> getAll() {
        var result = banknotes.stream().toList();
        banknotes.clear();
        return result;
    }

    public boolean isNotEmpty() {
        return this.count() != 0;
    }
}
