package study.netty;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static String host = "127.0.0.1";
    private static int port = 12345;

    public static void main(String[] args) {

        EchoClient client = new EchoClient();
        System.out.println("введите одно или несколько сообщений  для эхо взаимодействия что бы отключиться от сервера, введите disconnection:");
        while (true) {
            client.start();
        }
    }

    public void start() {

        NioEventLoopGroup nelg = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(nelg)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(host, port)
                    .handler(new ChannelInitializer<Channel>() {

                        @Override
                        protected void initChannel(Channel ch) throws Exception {

                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect().sync();
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {

            e.printStackTrace();
        } finally {

            try {
                nelg.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
