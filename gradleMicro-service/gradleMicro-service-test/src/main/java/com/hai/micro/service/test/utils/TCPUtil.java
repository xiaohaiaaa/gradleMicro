package com.hai.micro.service.test.utils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName TCPUtil
 * @Description: tcp请求工具类
 * @Author ZXH
 * @Date 2023/03/29 19:57
 * @Version 1.0
 **/
@Slf4j
public class TCPUtil {

    public static String sendData(String ip, Integer port, String data) {
        Socket socket = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        StringBuilder result = new StringBuilder();
        try {
            socket = new Socket(ip, port);
            socket.setTcpNoDelay(true);
            socket.setReuseAddress(true);
            //socket.setSoTimeout(30000);
            socket.setSendBufferSize(1024);
            socket.setReceiveBufferSize(1024);

            outputStream = socket.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            bufferedWriter.write(data);
            bufferedWriter.flush();
            socket.shutdownOutput();
            inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                result.append(str);
            }
        } catch (IOException e) {
            log.error("tcp.sendData error | {},{},{}", ip, port, data, e);
        } finally {
            try {
                if (Objects.nonNull(inputStream)) {
                    inputStream.close();
                }
                if (Objects.nonNull(outputStream)) {
                    outputStream.close();
                }
                if (Objects.nonNull(socket)) {
                    socket.close();
                }
            } catch (IOException e) {
                log.error("tcp.sendData close resource fail | {},{},{}", ip, port, data, e);
            }
        }
        return result.length() > 0 ? result.toString() : null;
    }

}
