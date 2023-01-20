package com.pelgray.otus;

import com.google.common.base.Splitter;

public class HelloOtus {
    public static void main(String[] args) {
        String example = "Мама мыла раму";
        int i = 1;
        for (var ex : Splitter.on(" ").splitToList(example)) {
            System.out.printf("%1$d) %2$s%n", i++, ex);
        }
    }
}
