package com.arc.common.http;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.http.protocol.HTTP;

public class HttpServletResponseImpl extends HttpServletResponseWrapper {
    private String charset;
    private int statusCode;
    private String statusMesg;
    private boolean errorFlag;
    private boolean rdrctFlag;
    private String location;
    private Locale locale;
    private List<Cookie> cookies = new ArrayList<Cookie>();

    private final Map<String, Object> headerMap = new HashMap<String, Object>();

    private PrintWriter writer = null;
    private final ServletOutputStreamImpl output = new ServletOutputStreamImpl();
    private Boolean outputType = null;

    public HttpServletResponseImpl(HttpServletResponse response) {
        super(response);
    }

    public byte[] getByteArray() {
        return this.output.getByteArray();
    }

    @Override
    public void setCharacterEncoding(String charset) {
        this.charset = charset;
    }

    @Override
    public String getCharacterEncoding() {
        return this.charset;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (this.outputType == null) {
            this.outputType = Boolean.TRUE;
            return this.output;
        }

        if (this.outputType == false) {
            throw new IOException("已经选择了PrintWriter!");
        }

        return this.output;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (this.outputType == null) {
            this.outputType = Boolean.FALSE;
            this.writer = new PrintWriter(new OutputStreamWriter(this.output));
            return this.writer;
        }

        if (this.outputType) {
            throw new IOException("已经选择了StreamOutputStream!");
        }

        return this.writer;
    }

    @Override
    public void setContentLength(int len) {
        this.setHeader(HTTP.CONTENT_LEN, String.valueOf(len));
    }

    @Override
    public void setContentLengthLong(long length) {
        this.setHeader(HTTP.CONTENT_LEN, String.valueOf(length));
    }

    @Override
    public void setContentType(String type) {
        this.setHeader(HTTP.CONTENT_TYPE, type);
    }

    @Override
    public String getContentType() {
        return this.getHeader(HTTP.CONTENT_TYPE);
    }

    @Override
    public void setBufferSize(int size) {
    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {
    }

    @Override
    public boolean isCommitted() {
        return true;
    }

    @Override
    public void reset() {
        throw new IllegalStateException("不支持的操作!");
    }

    @Override
    public void resetBuffer() {
        throw new IllegalStateException("不支持的操作!");
    }

    @Override
    public void setLocale(Locale loc) {
        this.locale = loc;
    }

    @Override
    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public boolean isWrapperFor(ServletResponse wrapped) {
        return super.isWrapperFor(wrapped);
    }

    @Override
    public boolean isWrapperFor(Class<?> wrappedType) {
        return super.isWrapperFor(wrappedType);
    }

    @Override
    public void addCookie(Cookie cookie) {
        this.cookies.add(cookie);
    }

    @Override
    public boolean containsHeader(String name) {
        return this.headerMap.containsKey(name);
    }

    @Override
    public String encodeURL(String url) {
        return super.encodeURL(url);
    }

    @Override
    public String encodeRedirectURL(String url) {
        return super.encodeRedirectURL(url);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String encodeUrl(String url) {
        return super.encodeUrl(url);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String encodeRedirectUrl(String url) {
        return super.encodeRedirectUrl(url);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        this.errorFlag = true;
        this.setStatus(sc, msg);
    }

    @Override
    public void sendError(int sc) throws IOException {
        this.errorFlag = true;
        this.setStatus(sc);
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        this.rdrctFlag = true;
        this.location = location;
    }

    @Override
    public void setDateHeader(String name, long date) {
        this.headerMap.put(name, new Date(date));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addDateHeader(String name, long date) {
        Object value = this.headerMap.get(name);
        if (value == null) {
            this.headerMap.put(name, new Date(date));
        } else if (List.class.isInstance(value)) {
            List<Object> values = (List<Object>) value;
            values.add(new Date(date));
        } else {
            List<Object> values = new ArrayList<Object>();
            values.add(value);
            values.add(new Date(date));
            this.headerMap.put(name, values);
        }
    }

    @Override
    public void setHeader(String name, String value) {
        this.headerMap.put(name, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addHeader(String name, String value) {
        Object object = this.headerMap.get(name);
        if (object == null) {
            this.headerMap.put(name, value);
        } else if (List.class.isInstance(object)) {
            List<Object> values = (List<Object>) object;
            values.add(value);
        } else {
            List<Object> values = new ArrayList<Object>();
            values.add(object);
            values.add(value);
            this.headerMap.put(name, values);
        }
    }

    @Override
    public void setIntHeader(String name, int value) {
        this.headerMap.put(name, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addIntHeader(String name, int value) {
        Object object = this.headerMap.get(name);
        if (object == null) {
            this.headerMap.put(name, value);
        } else if (List.class.isInstance(object)) {
            List<Object> values = (List<Object>) object;
            values.add(value);
        } else {
            List<Object> values = new ArrayList<Object>();
            values.add(object);
            values.add(value);
            this.headerMap.put(name, values);
        }
    }

    @Override
    public void setStatus(int sc) {
        this.statusCode = sc;
    }

    @Override
    public void setStatus(int sc, String sm) {
        this.statusCode = sc;
        this.statusMesg = sm;
    }

    @Override
    public int getStatus() {
        return this.statusCode;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getHeader(String name) {
        Object value = this.headerMap.get(name);
        if (value == null) {
            return null;
        } else if (List.class.isInstance(value)) {
            List<Object> values = (List<Object>) value;
            StringBuilder ber = new StringBuilder();
            for (int i = 0; i < values.size(); i++) {
                Object element = values.get(i);
                String text = this.doGetHeader(element);
                if (i == 0) {
                    ber.append(text);
                } else {
                    ber.append("; ").append(text);
                }
            }
            return ber.toString();
        } else if (String.class.isInstance(value)) {
            return (String) value;
        } else {
            return String.valueOf(value);
        }
    }

    @SuppressWarnings("unchecked")
    public String doGetHeader(Object value) {
        if (value == null) {
            return null;
        } else if (List.class.isInstance(value)) {
            List<Object> values = (List<Object>) value;
            StringBuilder ber = new StringBuilder();
            for (int i = 0; i < values.size(); i++) {
                Object element = values.get(i);
                String text = this.doGetHeader(element);
                if (i == 0) {
                    ber.append(text);
                } else {
                    ber.append("; ").append(text);
                }
            }
            return ber.toString();
        } else if (String.class.isInstance(value)) {
            return (String) value;
        } else {
            return String.valueOf(value);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<String> getHeaders(String name) {
        Object value = this.headerMap.get(name);
        if (value == null) {
            return new ArrayList<String>();
        } else if (List.class.isInstance(value)) {
            List<Object> values = (List<Object>) value;
            List<String> result = new ArrayList<String>();
            for (int i = 0; i < values.size(); i++) {
                Object element = values.get(i);
                result.add(String.valueOf(element));
            }
            return result;
        } else {
            List<String> result = new ArrayList<String>();
            result.add(String.valueOf(value));
            return result;
        }
    }

    @Override
    public Collection<String> getHeaderNames() {
        return this.headerMap.keySet();
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMesg() {
        return statusMesg;
    }

    public void setStatusMesg(String statusMesg) {
        this.statusMesg = statusMesg;
    }

    public boolean isErrorFlag() {
        return errorFlag;
    }

    public void setErrorFlag(boolean errorFlag) {
        this.errorFlag = errorFlag;
    }

    public boolean isRdrctFlag() {
        return rdrctFlag;
    }

    public void setRdrctFlag(boolean rdrctFlag) {
        this.rdrctFlag = rdrctFlag;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }

    public Map<String, Object> getHeaderMap() {
        return headerMap;
    }

    public ServletOutputStream getOutput() {
        return output;
    }

}
