package com.alexmawashi.nio.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@FunctionalInterface
public interface Action {
    void act(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
