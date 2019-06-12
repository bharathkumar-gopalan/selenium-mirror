package com.bharath.experiments.mirror.io;

public class ActionContext {

    public enum Action {
        CLICK,
        TYPE,
        HOVER
    }

    private Action action;
    private String value;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
