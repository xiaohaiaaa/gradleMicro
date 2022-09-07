package com.hai.micro.common.other.request;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName TestRequestWrapper
 * @Description request请求包装类
 * @Author ZXH
 * @Date 2021/12/8 19:36
 * @Version 1.0
 **/
@Slf4j
public class RequestWrapper extends HttpServletRequestWrapper {

    private final String body;
    private final Map<String, String> headerMap;

    public RequestWrapper(HttpServletRequest request) {
        super(request);
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        ServletInputStream inputStream = null;

        try {
            inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()));
                char[] charBuffer = new char[256];
                int bytesRead;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }

        } catch (IOException e) {
            log.error("获取request请求体失败: ", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        this.body = stringBuilder.toString();
        Enumeration<String> headerNames = request.getHeaderNames();
        HashMap<String, String> headerMap = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String element = headerNames.nextElement();
            headerMap.put(element, request.getHeader(element));
        }

        this.headerMap = headerMap;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {}

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}
