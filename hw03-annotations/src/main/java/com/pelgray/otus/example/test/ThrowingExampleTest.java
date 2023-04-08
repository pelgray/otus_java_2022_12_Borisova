package com.pelgray.otus.example.test;

import com.pelgray.otus.annotation.After;
import com.pelgray.otus.annotation.Before;
import com.pelgray.otus.annotation.Test;

import java.time.LocalDateTime;

public class ThrowingExampleTest {
    private final LocalDateTime startTime = LocalDateTime.now();

    @Before
    void setUp() {
        Utils.printCalledInfo("setUp", this);
        throw new RuntimeException("Test exception in setUp");
    }

    @Test("Этот тест не будет вызван из-за исключения в setUp")
    void test() {
        Utils.printCalledInfo("test", this);
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
