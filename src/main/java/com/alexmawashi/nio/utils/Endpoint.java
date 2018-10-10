package com.alexmawashi.nio.utils;

public class Endpoint {
    private String path;
    private Action action;

    public Endpoint(String path, Action action) {
        this.path = path;
        this.action = action;
    }

    public String getPath() {
        return path;
    }

    public Action getAction() {
        return action;
    }
}
