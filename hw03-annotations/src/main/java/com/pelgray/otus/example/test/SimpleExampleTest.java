package com.pelgray.otus.example.test;

import com.pelgray.otus.annotation.After;
import com.pelgray.otus.annotation.Before;
import com.pelgray.otus.annotation.Test;

import java.time.LocalDateTime;

public class SimpleExampleTest {
    private final LocalDateTime startTime = LocalDateTime.now();

    @Before
    void setUp() {
        Utils.printCalledInfo("setUp", this);
    }

    @Test("Этот тест должен просто быть вызван")
    void test1() {
        Utils.printCalledInfo("test1", this);
    }

    @Test("Этот тест не должен прерывать выполнение")
    void test2() {
        Utils.printCalledInfo("test2", this);
        throw new RuntimeException("Test");
    }

    @After
    void tearDown() {
        Utils.printCalledInfo("tearDown", this);
    }

    @Override
    public String toString() {
        return super.toString() + " started at " + startTime;
    }

}
