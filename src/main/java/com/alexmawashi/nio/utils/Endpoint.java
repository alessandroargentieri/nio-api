package com.alexmawashi.nio.utils;

public class Endpoint {
    private final String path;
    private final Action action;

    public Endpoint(String path, Action action) {
        this.path = path;
        this.action = action;
    }

    public synchronized String getPath() {
        return path;
    }

    public synchronized Action getAction() {
        return action;
    }
}
