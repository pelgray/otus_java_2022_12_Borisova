package com.pelgray.otus;

import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {

    private final Deque<Customer> data = new ArrayDeque<>();

    public void add(Customer customer) {
        data.addFirst(customer);
    }

    public Customer take() {
        return data.pollFirst();
    }

}
