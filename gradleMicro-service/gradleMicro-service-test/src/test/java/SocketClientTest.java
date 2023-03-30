import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.hai.micro.service.test.TestApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName SocketServerTest
 * @Description socket通讯客户端
 * @Author ZXH
 * @Date 2023/3/23 15:49
 * @Version 1.0
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestApplication.class})
@ActiveProfiles("dev")
public class SocketClientTest {

    /*public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 9999);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write("Hello, I am socket client!");
        bufferedWriter.flush();
        socket.shutdownOutput();
    }*/

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 9999);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        while (true) {
            String str = bufferedReader.readLine();
            bufferedWriter.write(str);
            bufferedWriter.write("\n");
            bufferedWriter.flush();
        }
    }

    /*public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 9999));
        socketChannel.configureBlocking(false);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(str.length());
            byteBuffer.put(str.getBytes(StandardCharsets.UTF_8));
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        }
    }*/
}
