package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage {
    private List<String> data;

    public ObjectForMessage() {
    }

    public ObjectForMessage(ObjectForMessage copyObj) {
        if (copyObj.getData() != null) {
            this.data = new ArrayList<>(copyObj.getData().size());
            this.data.addAll(copyObj.getData());
        }
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
