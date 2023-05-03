package ru.otus.processor.homework;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProcessorThrowingExceptionAtEvenSecondsTest {

    @Test
    void processAtEvenSecond() {
        // given
        var processor = new ProcessorThrowingExceptionAtEvenSeconds(() -> 2);
        var message = new Message.Builder(1L).build();

        // when
        var actual = assertThrows(RuntimeException.class, () -> processor.process(message));

        // then
        assertThat(actual)
                .hasMessage("It's an even second!");
    }

    @Test
    void processAtOddSecond() {
        // given
        var processor = new ProcessorThrowingExceptionAtEvenSeconds(() -> 1);
        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field4("field4")
                .field5("field5")
                .field6("field6")
                .field7("field7")
                .field8("field8")
                .field9("field9")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13(new ObjectForMessage())
                .build();

        // when
        var actual = assertDoesNotThrow(() -> processor.process(message));

        // then
        assertThat(actual).isEqualTo(new Message.Builder(1L)
                                             .field1("field1")
                                             .field2("field2")
                                             .field3("field3")
                                             .field4("field4")
                                             .field5("field5")
                                             .field6("field6")
                                             .field7("field7")
                                             .field8("field8")
                                             .field9("field9")
                                             .field10("field10")
                                             .field11("field11")
                                             .field12("field12")
                                             .field13(new ObjectForMessage())
                                             .build());
    }
}
