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

import org.junit.Test;
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
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
                    bufferedWriter.write("收到了");
                    bufferedWriter.flush();
                    socket.shutdownOutput();
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

    @Test
    public void testMethod() {
        // 测试逻辑
    }

    private static ServerSocketChannel serverSocketChannel = null;
    private static Selector selector = null;
    // 缓存一个read事件中一个不完整的包，以待下次read事件到来时拼接成完整的包
    private static final ByteBuffer cacheBuffer = ByteBuffer.allocate(100);
    private static int cacheBufferLen = 4;

    public static void main(String[] args) throws Exception {
        serverSocketChannel = ServerSocketChannel.open();
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
                    read2(key);
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

    private static void write(SelectionKey key) throws Exception {
        Thread.sleep(1000);
        // 11、响应客户端
        SocketChannel socketChannel = (SocketChannel)key.channel();
        socketChannel.write(ByteBuffer.wrap("主动回复收到了".getBytes(StandardCharsets.UTF_8)));
        System.out.println("回复客户端[" + socketChannel.getRemoteAddress() + "]: 主动回复收到了");

        socketChannel.register(selector, SelectionKey.OP_READ);

        // 12、关闭相关资源
        //socketChannel.close();
    }

    private static void read2(SelectionKey selectionKey) {
        int head_length = 4;//数据包长度
        byte[] headByte = new byte[4];

        try {
            SocketChannel channel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(100);
            int bodyLen = -1;
            if (cacheBufferLen > head_length) {
                byteBuffer = ByteBuffer.allocate(cacheBufferLen);
                cacheBuffer.flip();
                byteBuffer.put(cacheBuffer);
            }
            System.out.println("byteBuffer position: " + byteBuffer.position() + " byteBuffer capacity: " + byteBuffer.capacity());
            // 当前read事件
            channel.read(byteBuffer);
            // write mode to read mode
            byteBuffer.flip();
            System.out.println("byteBuffer position: " + byteBuffer.position() + " byteBuffer capacity: " + byteBuffer.capacity());
            int remainingLen = 0;
            while ((remainingLen = byteBuffer.remaining()) > 0) {
                System.out.println("remainingLen: " + remainingLen);
                System.out.println("bodyLen: " + bodyLen);
                if (bodyLen == -1) {
                    // 还没有读出包头，先读包头
                    System.out.println("还没有读出包头，先读包头");
                    if (byteBuffer.remaining() >= head_length) {
                        // 可以读出包头
                        System.out.println("可以读出包头");
                        byteBuffer.mark();
                        byteBuffer.get(headByte);
                        bodyLen = byteArrayToInt(headByte);
                    } else {
                        // 读不出包头，把已经读了的先存起来
                        System.out.println("信息异常，不足以读出包头");
                        break;
                    }
                } else {
                    // 已经读出包头
                    System.out.println("已经读出包头，准备读消息");
                    if (byteBuffer.remaining() >= bodyLen) {
                        // 可以读完一个包
                        System.out.println("可以读完一个包");
                        byte[] bodyByte = new byte[bodyLen];
                        byteBuffer.get(bodyByte, 0, bodyLen);
                        bodyLen = -1;
                        cacheBufferLen = 4;
                        cacheBuffer.clear();

                        System.out.println("收到信息: " + new String(bodyByte));
                    } else {
                        // 没办法读完一个包，把已经读了的先存起来
                        System.out.println("不足以读完一个包，先存起来");
                        byteBuffer.reset();
                        cacheBuffer.clear();
                        cacheBuffer.put(byteBuffer);
                        cacheBufferLen += bodyLen;
                        break;
                    }
                }
            }

            selectionKey.interestOps(SelectionKey.OP_READ);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            try {
                serverSocketChannel.close();
                selectionKey.cancel();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

    }

    /**
     * byte[]转int
     *
     * @param bytes
     * @return
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        // 由高位到低位
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;// 往高位游
        }
        return value;
    }

}
