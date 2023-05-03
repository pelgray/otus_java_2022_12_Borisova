package ru.otus.processor.homework;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessorSwapFieldsTest {

    @Test
    void processTest() {
        // given
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
        var processor = new ProcessorSwapFields();

        // when
        var actual = processor.process(message);

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
                                             .field11("field12")
                                             .field12("field11")
                                             .field13(new ObjectForMessage())
                                             .build());
    }

    @Test
    void processTwiceTest() {
        // given
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
        var processor = new ProcessorSwapFields();

        // when
        processor.process(message);
        var actual = processor.process(message);

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
