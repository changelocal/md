package com.arc.common.http;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class HttpServletRequestImpl extends HttpServletRequestWrapper {

    private final ByteArrayInputStream input;

    public HttpServletRequestImpl(HttpServletRequest request) {
        super(request);
        this.input = null;
    }

    public HttpServletRequestImpl(HttpServletRequest request, byte[] byteArray) {
        super(request);
        this.input = new ByteArrayInputStream(byteArray);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return this.input == null ? super.getInputStream() : new ServletInputStreamImpl(this.input);
    }

}
