package org.hyoj.mysbbp.aspects;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.apache.commons.io.IOUtils;
import org.hyoj.mysbbp.common.util.StringUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class CustomRequestWrapper extends HttpServletRequestWrapper {
    private final Charset encoding;
    private byte[] rawData;

    public CustomRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        String characterEncoding = request.getCharacterEncoding();
        if (StringUtils.isBlank(characterEncoding)) {
            characterEncoding = StandardCharsets.UTF_8.name();
        }
        this.encoding = Charset.forName(characterEncoding);

        try {
            InputStream inputStream = request.getInputStream();
            this.rawData = IOUtils.toByteArray(inputStream);
            String requestBody = new String(rawData, StandardCharsets.UTF_8);

            writeRequestLogs(request, requestBody);

        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 공통 Request 로그 작성
     */
    private void writeRequestLogs(HttpServletRequest request, String requestBody) {
        String requestId = (String) request.getAttribute("requestId");
        String publicIp = (String) request.getAttribute("publicIp");
        String userAgent = request.getHeader("user-agent");
        String requestMethod = request.getMethod();
        String servletPath = request.getServletPath();
        String queryString = !StringUtil.isEmpty(request.getQueryString()) ? "?" + request.getQueryString() : "";

        log.info("-------------------------------------");
        log.info("[Request] => {}", requestId);
        log.info("{} {} {}", requestId, requestMethod, servletPath + queryString);
        log.info("{} public ip:{}", requestId, publicIp);
        log.info("{} user-agent:{}", requestId, userAgent);
        log.info("{} request body=[{}]", requestId, requestBody);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rawData);
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
        return servletInputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), this.encoding));
    }

    @Override
    public ServletRequest getRequest() {
        return super.getRequest();
    }
}

