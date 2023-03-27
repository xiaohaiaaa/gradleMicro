import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.hai.micro.service.test.TestApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName SocketServerTest
 * @Description socket通讯服务端
 * @Author ZXH
 * @Date 2023/3/23 15:49
 * @Version 1.0
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestApplication.class})
@ActiveProfiles("dev")
public class SocketServerTest {

    /*public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        Socket socket = serverSocket.accept();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String str = bufferedReader.readLine();
        System.out.println(str);
    }*/

    /*public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        Socket socket = serverSocket.accept();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            System.out.println(str);
        }
    }*/
    
    /*public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        while (true) {
            Socket socket = serverSocket.accept();
            ThreadPoolUtil.execute(() -> {
                try {
                    BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                    String str;
                    while ((str = bufferedReader.readLine()) != null) {
                        System.out.println(str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }*/

    /*public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(9999));
        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) {
                System.out.println("new socket client come in");
                socketChannel.configureBlocking(false);
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                while (socketChannel.read(byteBuffer) != -1) {
                    byteBuffer.flip();
                    System.out.println(Charset.defaultCharset().newDecoder().decode(byteBuffer));
                    byteBuffer.clear();
                }
            }
        }
    }*/

    private static Selector selector;

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(9999));
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            System.out.println("-----");
            int select = selector.select();
            if (select == 0) {
                System.out.println("select not found");
                continue;
            }
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
            while(keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if(key.isAcceptable()) {
                    // 一个连接被ServerSocketChannel接受
                    accept(key);
                } else if (key.isConnectable()) {
                    // 与远程服务器建立了连接
                } else if (key.isReadable()) {
                    // 一个channel做好了读准备
                    read(key);
                } else if (key.isWritable()) {
                    // 一个channel做好了写准备
                    write(key);
                }
                keyIterator.remove();
            }
        }
    }

    private static void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocket = (ServerSocketChannel)key.channel();

        // 7、获取客户端连接
        SocketChannel socketChannel = serverSocket.accept();
        System.out.println("客户端[" + socketChannel.getRemoteAddress() + "]接入");
        socketChannel.configureBlocking(false);

        // 8、注册读事件到选择器上，等待客户端发送内容
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    private static void read(SelectionKey key) throws IOException {
        // 9、读取客户端发送内容
        SocketChannel sc = (SocketChannel)key.channel();

        StringBuilder stringBuilder = new StringBuilder();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (sc.read(byteBuffer) > 0) {
            //System.out.println("position:" + byteBuffer.position() + "\t limit:" + byteBuffer.limit() + "\t capacity:" + byteBuffer.capacity());
            byteBuffer.flip();
            //System.out.println("position:" + byteBuffer.position() + "\t limit:" + byteBuffer.limit() + "\t capacity:" + byteBuffer.capacity());
            byte[] content = new byte[byteBuffer.limit()];
            byteBuffer.get(content);
            String result = new String(content, StandardCharsets.UTF_8);
            stringBuilder.append(result);
            //System.out.println("position:" + byteBuffer.position() + "\t limit:" + byteBuffer.limit() + "\t capacity:" + byteBuffer.capacity());
            byteBuffer.clear();
        }

        System.out.println("接收客户端[" + sc.getRemoteAddress() + "]发送消息:" + stringBuilder);

        sc.write(ByteBuffer.wrap("同步回复收到了".getBytes(StandardCharsets.UTF_8)));
        System.out.println("回复客户端[" + sc.getRemoteAddress() + "]: 同步回复收到了");

        // 10、读完成，注册写事件到选择器
        sc.register(selector, SelectionKey.OP_WRITE);
    }

    private static void write(SelectionKey key) throws IOException {
        // 11、响应客户端
        SocketChannel socketChannel = (SocketChannel)key.channel();
        socketChannel.write(ByteBuffer.wrap("主动回复收到了".getBytes(StandardCharsets.UTF_8)));
        System.out.println("回复客户端[" + socketChannel.getRemoteAddress() + "]: 主动回复收到了");

        key.interestOps(0);

        socketChannel.register(selector, SelectionKey.OP_READ);

        // 12、关闭相关资源
        //socketChannel.close();
    }

}
