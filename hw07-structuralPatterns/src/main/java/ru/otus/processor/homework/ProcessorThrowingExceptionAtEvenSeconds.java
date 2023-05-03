package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.util.function.LongSupplier;

/**
 * Процессор, который выбрасывает исключение в четную секунду
 */
public class ProcessorThrowingExceptionAtEvenSeconds implements Processor {
    private final LongSupplier currentSecondsGetter;

    public ProcessorThrowingExceptionAtEvenSeconds(LongSupplier currentSecondsGetter) {
        this.currentSecondsGetter = currentSecondsGetter;
    }

    @Override
    public Message process(Message message) {
        if (currentSecondsGetter.getAsLong() % 2 == 0) {
            throw new RuntimeException("It's an even second!");
        }
        return message.toBuilder().build();
    }
}
