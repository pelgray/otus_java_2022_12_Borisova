package com.pelgray.otus.example.test;

public class Utils {
    private Utils() {
    }

    public static void printCalledInfo(String methodName, Object obj) {
        System.out.println("\tcalled " + methodName + " method from " + obj);
    }
}
