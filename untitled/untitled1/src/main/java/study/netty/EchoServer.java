
package study.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EchoServer.class);

    private int port;

    public EchoServer(int port) {

        this.port = port;
    }

    public static void main(String... args) {

        EchoServer echoServer = new EchoServer(12345);
        echoServer.start();
    }

    public void start() {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(),
                workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(port)
                    .childHandler(new ChannelInitializer<Channel>() {

                        @Override
                        protected void initChannel(Channel ch) throws Exception {

                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });

            ChannelFuture f = bootstrap.bind().sync();
            System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {

            LOGGER.error(e.getMessage());
        } finally {

            try {
                workerGroup.shutdownGracefully().sync();
                bossGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {

                LOGGER.error(e.getMessage());
            }
        }
    }
}


