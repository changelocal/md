package com.arc.common.filter;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.arc.common.ServiceException;
import com.arc.common.SystemException;
import com.arc.common.http.HttpServletResponseImpl;
import com.arc.util.http.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HTTP;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResponseWrapFilter implements Filter {
    public static final String TARGET_CONTENT_TYPE = ContentType.APPLICATION_JSON.getMimeType();

    public static final String KEY_RETURN_STATUS = "status";
    public static final String KEY_RETURN_VALUE = "result";
    public static final String KEY_RETURN_MESSAGE = "message";

    public static final String KEY_PATTERN_NUMBER = "\\d+(\\.\\d+)?";

    public static final String KEY_OBJECT_NULL = "null";
    public static final String KEY_BOOLEAN_TRUE = "true";
    public static final String KEY_BOOLEAN_FALSE = "false";

    public static final String HEADER_DISABLE_FLAG = "X-Disable-Wrap";
    public static final String HEADER_RECV_REQUEST = "X-Recv-Request-Only";

    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (StringUtils.equalsIgnoreCase(((HttpServletRequest) request).getHeader(HEADER_DISABLE_FLAG), KEY_BOOLEAN_TRUE)) {
            chain.doFilter(request, response);
        } else {
            this.invokeFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
        }
    }

    public void invokeFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponseImpl resp = new HttpServletResponseImpl(response);

        Throwable error = null;
        try {
            chain.doFilter(request, resp);
        } catch (ServletException ex) {
            error = (Throwable) ex.getCause();
        } catch (Exception ex) {
            error = ex;
        }

        int statusCode = resp.getStatusCode();
        String statusMesg = resp.getStatusMesg();

        if (statusCode >= HttpServletResponse.SC_MULTIPLE_CHOICES && statusCode < HttpServletResponse.SC_BAD_REQUEST) {
            response.sendRedirect(resp.getLocation());
            return; // response.setStatus(statusCode);
        }

        if (statusCode >= HttpServletResponse.SC_BAD_REQUEST && statusCode < HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            // 因客户端错误导致响应码为4xx时, 不会存在服务端处理的异常。
            error = new SystemException(String.format("客户端请求错误: status= %s, message= %s", statusCode, statusMesg));
        } else if (error == null && statusCode >= HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            // 如果代码中直接通过HttpServletResponse.sendError(status)设置返回码, 则可能需要手工转换成异常以便后续统一处理
            error = new SystemException(String.format("服务端请求错误: status= %s, message= %s", statusCode, statusMesg));
        }

        String responseContentType = resp.getContentType();
        byte[] responseByteArray = resp.getByteArray();

        boolean responseTextFlag = this.contentTypeMatched(responseContentType, ContentType.TEXT_PLAIN)
                || this.contentTypeMatched(responseContentType, ContentType.TEXT_HTML);

        boolean wrapDisabled = StringUtils.equalsIgnoreCase(resp.getHeader(HEADER_DISABLE_FLAG), "true");
        boolean wrapRequired = false;
        if (wrapDisabled) {
            wrapRequired = false;
        } else if (error == null) {
            wrapRequired = StringUtils.isBlank(responseContentType)
                    ? (responseByteArray == null || responseByteArray.length == 0)
                    : (this.responseContentTypeIsJson(resp) || responseTextFlag);
        } else {
            wrapRequired = true;
        }

        ServletOutputStream output = response.getOutputStream();
        if (wrapRequired == false) {
            response.setContentLength(responseByteArray.length);
            response.setContentType(responseContentType);

            if (error == null) {
                // 必须先拷贝响应header再写消息体.
                // org.apache.catalina.connector.Response会检查Content-Length:
                // 如果不为0并且已经写满, 则忽略后续的header添加.
                this.copyResponseHeaders(resp, response);
                this.copyResponseCookies(resp, response);
                output.write(responseByteArray);
                return;
            } else if (IOException.class.isInstance(error)) {
                throw (IOException) error;
            } else if (ServletException.class.isInstance(error)) {
                throw (ServletException) error;
            } else if (RuntimeException.class.isInstance(error)) {
                throw (RuntimeException) error;
            } else {
                throw new RuntimeException(error);
            }
        }

        response.setContentType(StringUtils.isBlank(responseContentType) ? TARGET_CONTENT_TYPE : responseContentType);

        if (error == null) {
            String recvRequestValue = resp.getHeader(HEADER_RECV_REQUEST);
            boolean recvRequestOnly = StringUtils.equalsIgnoreCase(recvRequestValue, KEY_BOOLEAN_TRUE);

            this.writeSuccessResponse(output, responseByteArray, recvRequestOnly);
            this.copyResponseHeaders(resp, response);
            this.copyResponseCookies(resp, response);
        } else if (ServiceException.class.isInstance(error)) {
            Throwable cause = error.getCause();
            if (cause == null) {
                log.debug("服务响应失败: uri= {}", request.getRequestURI(), error);
            } else {
                log.warn("服务响应失败: uri= {}", request.getRequestURI(), cause);
            }
            this.writeFailureResponse(output, ((ServiceException) error).getErrCode(), error.getMessage());
        } else if (SystemException.class.isInstance(error)) {
            log.warn("服务响应出错: uri= {}", request.getRequestURI(), error);
            this.writeFailureResponse(output, BaseResponse.STATUS_SYSTEM_FAILURE, error.getMessage());
        } else {
            log.error("服务响应异常: uri= {}", request.getRequestURI(), error);
            this.writeFailureResponse(output, BaseResponse.STATUS_SYSTEM_FAILURE, error.getMessage());
        }
    }

    protected void writeSuccessResponse(ServletOutputStream output, byte[] byteArray, boolean recvRequestOnly)
            throws IOException {
        StringBuilder ber = new StringBuilder();
        ber.append("\"").append(KEY_RETURN_STATUS).append("\"");
        ber.append(":");
        ber.append("\"");
        ber.append(recvRequestOnly ? BaseResponse.STATUS_HANDLE_SUCCESS : BaseResponse.STATUS_HANDLE_SUCCESS);
        ber.append("\"");
        output.write("{".getBytes());
        output.write(ber.toString().getBytes());
        output.write(",".getBytes());
        output.write("\"".getBytes());
        output.write(KEY_RETURN_VALUE.getBytes());
        output.write("\":".getBytes());

        Class<?> clazz = null;
        try {
            Object target = byteArray == null || byteArray.length == 0 ? null : this.mapper.readValue(byteArray, Object.class);
            clazz = target == null ? null : target.getClass();
        } catch (JsonParseException jpex) {
            clazz = String.class;
        } catch (JsonMappingException jpex) {
            clazz = String.class;
        }

        if (clazz == null) {
            output.write(KEY_OBJECT_NULL.getBytes());
        } else if (Number.class.isAssignableFrom(clazz)) {
            output.write(byteArray);
        } else if (Boolean.class.isAssignableFrom(clazz)) {
            output.write(byteArray);
        } else if (List.class.isAssignableFrom(clazz)) {
            output.write(byteArray);
        } else if (Map.class.isAssignableFrom(clazz)) {
            output.write(byteArray);
        } else {
            output.write("\"".getBytes());
            output.write(byteArray);
            output.write("\"".getBytes());
        }
        output.write("}".getBytes());
    }

    protected void writeFailureResponse(ServletOutputStream output, String status, String message) throws IOException {
        StringBuilder ber = new StringBuilder();
        ber.append("{");

        ber.append("\"").append(KEY_RETURN_STATUS).append("\"");
        ber.append(":");
        ber.append("\"").append(status).append("\"");

        ber.append(",");

        ber.append("\"").append(KEY_RETURN_MESSAGE).append("\"");
        ber.append(":");
        if (StringUtils.isBlank(message)) {
            ber.append("null");
        } else {
            ber.append("\"").append(message).append("\"");
        }

        ber.append("}");

        output.write(ber.toString().getBytes());
    }

    protected void copyResponseHeaders(HttpServletResponseImpl source, HttpServletResponse target) {
        Map<String, Object> headerMap = source.getHeaderMap();
        for (Iterator<Map.Entry<String, Object>> itr = headerMap.entrySet().iterator(); itr.hasNext();) {
            Map.Entry<String, Object> entry = itr.next();
            String headerKey = entry.getKey();
            Object headerVal = entry.getValue();

            if (StringUtils.equalsIgnoreCase(HTTP.CONTENT_TYPE, headerKey)) {
                continue;
            } else if (StringUtils.equalsIgnoreCase(HTTP.CONTENT_LEN, headerKey)) {
                continue;
            }

            if (Date.class.isInstance(headerVal)) {
                target.addDateHeader(headerKey, ((Date) headerVal).getTime());
            } else if (Integer.class.isInstance(headerVal) || Integer.TYPE.isInstance(headerVal)) {
                target.addIntHeader(headerKey, ((Integer) headerVal).intValue());
            } else {
                target.addHeader(headerKey, headerVal == null ? "" : String.valueOf(headerVal));
            }
        }
    }

    protected void copyResponseCookies(HttpServletResponseImpl source, HttpServletResponse target) {
        List<Cookie> cookies = source.getCookies();
        for (int i = 0; cookies != null && i < cookies.size(); i++) {
            Cookie cookie = cookies.get(i);
            target.addCookie(cookie);
        }
    }

    @Override
    public void destroy() {
    }

    protected boolean requestContentTypeIsJson(HttpServletRequest request) {
        String requestContentType = request.getContentType();
        return this.contentTypeIsJson(requestContentType);
    }

    protected boolean responseContentTypeIsJson(HttpServletResponse response) {
        String responseContentType = response.getContentType();
        return this.contentTypeIsJson(responseContentType);
    }

    protected boolean contentTypeIsJson(String contentType) {
        if (StringUtils.isBlank(contentType)) {
            return false;
        }

        String[] values = contentType.split("\\s*;\\s*");
        return StringUtils.equalsIgnoreCase(values[0], TARGET_CONTENT_TYPE);
    }

    protected boolean contentTypeMatched(String contentType, ContentType targetContentType) {
        if (StringUtils.isBlank(contentType) || targetContentType == null) {
            return false;
        }

        String[] values = contentType.split("\\s*;\\s*");
        return StringUtils.equalsIgnoreCase(values[0], targetContentType.getMimeType());
    }

}
