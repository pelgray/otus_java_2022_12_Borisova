package com.pelgray.otus.logging.test;

public interface TestLoggingInterface {
    void calculation();

    void calculation(int param);

    void calculation(int param1, int param2);

    void calculation(int param1, int param2, String param3);

    int calculation(int param1, int param2, int param3);

    void calculationNoLog();

    void calculationNoLog(int param);

    void calculationNoLog(int param1, int param2);

    void calculationNoLog(int param1, int param2, String param3);

    int calculationNoLog(int param1, int param2, int param3);

}
