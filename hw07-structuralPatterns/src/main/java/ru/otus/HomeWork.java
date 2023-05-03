package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.ProcessorSwapFields;
import ru.otus.processor.homework.ProcessorThrowingExceptionAtEvenSeconds;

import java.util.List;

public class HomeWork {

    public static void main(String[] args) {
        List<Processor> processors = List.of(
                new ProcessorThrowingExceptionAtEvenSeconds(() -> System.currentTimeMillis() / 1000),
                new ProcessorSwapFields()
        );

        var complexProcessor = new ComplexProcessor(processors, ex -> System.out.println("\tERROR CAUGHT " + ex));

        var historyListener = new HistoryListener();
        complexProcessor.addListener(historyListener);

        var id = 1L;
        var message = new Message.Builder(id)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13(new ObjectForMessage())
                .build();

        System.out.println("\noriginal message:\n\t" + message);

        var historicMessage0 = historyListener.findMessageById(id);
        System.out.println("historicMessage 0:\n\t" + historicMessage0);

        var result1 = complexProcessor.handle(message);
        System.out.println("\nresult 1:\n\t" + result1);

        var historicMessage1 = historyListener.findMessageById(id);
        System.out.println("historicMessage 1:\n\t" + historicMessage1);

        var result2 = complexProcessor.handle(result1.toBuilder().field1("test").build());
        System.out.println("\nresult 2:\n\t" + result2);

        var historicMessage2 = historyListener.findMessageById(id);
        System.out.println("historicMessage 2:\n\t" + historicMessage2);

        complexProcessor.removeListener(historyListener);
    }
}
