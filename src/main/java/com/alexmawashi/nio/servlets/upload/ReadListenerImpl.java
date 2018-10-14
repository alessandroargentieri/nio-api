package com.alexmawashi.nio.servlets.upload;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ReadListenerImpl implements ReadListener {
    private ServletInputStream input = null;
    private HttpServletResponse res = null;
    private AsyncContext ac = null;
    private Queue queue = new LinkedBlockingQueue();
    ReadListenerImpl(ServletInputStream in, HttpServletResponse r, AsyncContext c) {
        input = in;
        res = r;
        ac = c;
    }
    public void onDataAvailable() throws IOException {
        System.out.println("Data is available");

        StringBuilder sb = new StringBuilder();
        int len = -1;
        byte b[] = new byte[1024];
        while (input.isReady() && (len = input.read(b)) != -1) {
            String data = new String(b, 0, len);
            sb.append(data);
        }
        queue.add(sb.toString());
    }
    public void onAllDataRead() throws IOException {
        System.out.println("Data is all read");

        // now all data are read, set up a WriteListener to write
        ServletOutputStream output = res.getOutputStream();
        WriteListener writeListener = new WriteListenerImpl(output, queue, ac);
        output.setWriteListener(writeListener);
    }
    public void onError(final Throwable t) {
        ac.complete();
        t.printStackTrace();
    }
}