package com.pelgray.otus;

import com.pelgray.otus.logging.test.TestLoggingInterface;

public class Demo {
    public void action(TestLoggingInterface testLogging) {
        testLogging.calculation();
        testLogging.calculation(1);
        testLogging.calculation(2, 3);
        testLogging.calculation(4, 5, "6");
        System.out.println("Got result = " + testLogging.calculation(7, 8, 9));

        testLogging.calculationNoLog();
        testLogging.calculationNoLog(7);
        testLogging.calculationNoLog(8, 9);
        testLogging.calculationNoLog(10, 11, "12");
        System.out.println("Got result without log = " + testLogging.calculationNoLog(13, 14, 15));
    }
}
