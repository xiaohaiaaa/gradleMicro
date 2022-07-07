package com.hai.micro.service.test.utils;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.util.Strings;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.hai.micro.service.test.entity.bo.CmdResult;

/**
 * @ClassName FfmpegUtil，使用的ffmpeg需要先单独下载安装
 * @Description
 * @Author ZXH
 * @Date 2021/12/4 15:55
 * @Version 1.0
 **/
@Component
public class FfmpegUtil {

    private final static Pattern DURATION = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");

    /**
     * 执行Cmd命令方法
     *
     * @param command 相关命令
     * @return 执行结果
     */
    public static CmdResult runCommand(List<String> command) {
        CmdResult cmdResult = new CmdResult(false, "");
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);
        try {
            Process process = builder.start();
            final StringBuilder stringBuilder = new StringBuilder();
            final InputStream inputStream = process.getInputStream();
            //启动新线程为异步读取缓冲器，防止线程阻塞
            ThreadPoolUtil.execute(() -> {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            process.waitFor();
            cmdResult.setSuccess(true);
            cmdResult.setMsg(stringBuilder.toString());
        } catch (Exception e) {
            throw new RuntimeException("ffmpeg执行异常：" + e.getMessage());
        }
        return cmdResult;
    }

    /**
     * 获取视频文件时长
     *
     * @param url 文件地址
     * @return 时长 格式hh:MM:ss
     */
    public static String getVideoTime(String url) {
        List<String> commands = new ArrayList<>();
        // cmd /c 是执行完命令后关闭命令窗口的意思
        commands.add("cmd");
        commands.add("/c");
        commands.add("ffmpeg");
        commands.add("-i");
        commands.add(url);
        CmdResult result = runCommand(commands);
        String msg = result.getMsg();
        if (result.isSuccess()) {
            msg = msg.substring(msg.lastIndexOf("Duration:"));
            Matcher matcher = DURATION.matcher(msg);
            String time = "";
            if (matcher.find()) {
                time = matcher.group();
            }
            return time;
        } else {
            return "";
        }
    }

    /**
     * 视频切割
     * @param total
     * @param url
     */
    public static void cutVideo(Integer total, String url) {
        int begin = url.lastIndexOf("\\");
        int end = url.lastIndexOf(".");
        String name = url.substring(begin, end);
        // 获取视频时长
        String videoTime = getVideoTime(url);
        int videoTimes = parseTimeToSecond(videoTime);
        // 根据切割数量计算切割时长
        int second = new BigDecimal(String.valueOf(videoTimes)).divide(new BigDecimal(total), 0, RoundingMode.UP).intValue();
        // 切割视频
        List<String> commands = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            if (i * second >= videoTimes) {
                break;
            }
            commands.add("cmd");
            commands.add("/c");
            commands.add("ffmpeg");
            commands.add("-ss");
            commands.add(parseTimeToString(i * second));
            commands.add("-t");
            commands.add(parseTimeToString(second));
            commands.add("-i");
            commands.add(url);
            commands.add("-vcodec");
            commands.add("copy");
            commands.add("-acodec");
            commands.add("copy");
            commands.add(url.replace(name, name + (i + 1)));
            runCommand(commands);
            commands.clear();
        }
    }

    /**
     * m3u8视频转mp4下载
     * @param url
     * @param name
     */
    @Async
    public void downVideo(String url, String name) {
        if (Strings.isBlank(name)) {
            name = DistributedIdUtil.ObjectId();
        }
        List<String> commands = new ArrayList<>();
        commands.add("cmd");
        commands.add("/c");
        commands.add("D:");
        commands.add("&");
        commands.add("cd");
        commands.add("D:\\save");
        commands.add("&");
        commands.add("ffmpeg");
        commands.add("-i");
        commands.add(url);
        commands.add("-c");
        commands.add("copy");
        commands.add(name + ".mp4");
        runCommand(commands);
    }

    /**
     * MultipartFile转File方法1
     * @param file
     * @return
     * @throws Exception
     */
    public static File multipartFileToFile1(MultipartFile file) throws Exception {
        File toFile;
        if (file.isEmpty()) {
            throw new FileNotFoundException(file.getOriginalFilename() + "不存在");
        } else {
            InputStream ins = file.getInputStream();
            toFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
            OutputStream os = new FileOutputStream(toFile);
            int bytesRead;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        }
        return toFile;
    }

    /**
     * MultipartFile转File方法2
     * @param multiFile
     * @return
     */
    public static File multipartFileToFile2(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若须要防止生成的临时文件重复,能够在文件名后添加随机码
        try {
            File file = File.createTempFile(fileName, prefix);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将00:00:00时间格式转换为整型，以秒为单位
     *
     * @param timeString 字符串时间时长
     * @return 时间所对应的秒数
     */
    private static int parseTimeToSecond(String timeString) {
        Matcher matcher = DURATION.matcher(timeString);
        if (!matcher.matches()) {
            try {
                throw new Exception("时间格式不正确");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int[] time = Arrays.stream(timeString.split(":")).mapToInt(Integer::parseInt).toArray();
        return time[0] * 3600 + time[1] * 60 + time[2];
    }

    /**
     * 将秒表示时长转为00:00:00格式
     *
     * @param second 秒数时长
     * @return 字符串格式时长
     */
    private static String parseTimeToString(int second) {
        String miao = String.format("%02d", second % 60);
        int minute = second / 60;
        String fen;
        if (minute >= 60) {
            fen = String.format("%02d", minute % 60);
        } else {
            fen = String.format("%02d", minute);
        }
        String shi = String.format("%02d", minute / 60);
        return shi + ":" + fen + ":" + miao;
    }
}
