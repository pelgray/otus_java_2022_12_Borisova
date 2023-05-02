package com.pelgray.otus.logging.test;

import com.pelgray.otus.logging.annotation.Log;

public class TestLogging implements TestLoggingInterface {
    @Log
    @Override
    public void calculation() {
        print("Process method with no params");
    }

    @Log
    @Override
    public void calculation(int param) {
        print("Process method with param=%d%n", param);
    }

    @Log
    @Override
    public void calculation(int param1, int param2) {
        print("Process method with param1=%d; param2=%d%n", param1, param2);
    }

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {
        print("Process method with param1=%d; param2=%d; param3=%s%n", param1, param2, param3);
    }

    @Log
    @Override
    public int calculation(int param1, int param2, int param3) {
        int result = param1 + param2 + param3;
        print("Process method with param1=%d; param2=%d; param3=%d; return=%d%n", param1, param2, param3, result);
        return result;
    }

    @Override
    public void calculationNoLog() {
        print("[NO LOGS] Process method with no params");
    }

    @Override
    public void calculationNoLog(int param) {
        print("[NO LOGS] Process method with param=%d%n", param);
    }

    @Override
    public void calculationNoLog(int param1, int param2) {
        print("[NO LOGS] Process method with param1=%d; param2=%d%n", param1, param2);
    }

    @Override
    public void calculationNoLog(int param1, int param2, String param3) {
        print("[NO LOGS] Process method with param1=%d; param2=%d; param3=%s%n", param1, param2, param3);
    }

    @Override
    public int calculationNoLog(int param1, int param2, int param3) {
        int result = param1 + param2 + 2 * param3;
        print("[NO LOGS] Process method with param1=%d; param2=%d; param3=%d; return=%d%n", param1, param2, param3, result);
        return result;
    }

    @Override
    public String toString() {
        return "TestLogging{}";
    }

    private void print(String msg, Object... params) {
        String resultMsg = msg + (params.length > 0 ? "" : "%n");
        System.out.printf(resultMsg, params);
    }
}
