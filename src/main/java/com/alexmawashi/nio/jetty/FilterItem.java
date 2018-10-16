package com.alexmawashi.nio.jetty;

public class FilterItem {
    private final Class filterClass;
    private final String path;
    private final Jetty.Dispatch dispatch;

    public FilterItem(Class filterClass, String path, Jetty.Dispatch dispatch) {
        this.filterClass = filterClass;
        this.path = path;
        this.dispatch = dispatch;
    }

    public Class getFilterClass() {
        return filterClass;
    }

    public String getPath() {
        return path;
    }

    public Jetty.Dispatch getDispatch() {
        return dispatch;
    }
}