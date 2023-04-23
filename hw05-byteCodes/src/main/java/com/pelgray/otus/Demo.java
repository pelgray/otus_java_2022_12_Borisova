package com.pelgray.otus;

import com.pelgray.otus.logging.test.TestLoggingInterface;

public class Demo {
    public void action(TestLoggingInterface testLogging) {
        int i = 0;
        i = callAllMethods(testLogging, i);

        System.out.println("Calls for the second time");
        i = callAllMethods(testLogging, i);

        System.out.println("Calls for the third time");
        i = callAllMethods(testLogging, i);

        System.out.println("Calls for the fourth time");
        callAllMethods(testLogging, i);
    }

    private static int callAllMethods(TestLoggingInterface testLogging, int i) {
        testLogging.calculation();
        testLogging.calculation(i++);
        testLogging.calculation(i++, i++);
        testLogging.calculation(i++, i++, String.valueOf(i++));
        System.out.println("Got result = " + testLogging.calculation(i++, i++, i++));

        testLogging.calculationNoLog();
        testLogging.calculationNoLog(i++);
        testLogging.calculationNoLog(i++, i++);
        testLogging.calculationNoLog(i++, i++, String.valueOf(i++));
        System.out.println("Got result without log = " + testLogging.calculationNoLog(i++, i++, i++));
        return i;
    }
}
