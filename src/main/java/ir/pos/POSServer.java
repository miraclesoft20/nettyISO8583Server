package ir.pos;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.internal.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class POSServer {

    private static final Logger log = LoggerFactory.getLogger(POSServer.class);

    private final ServerBootstrap bootstrap;

    private final NioEventLoopGroup bossGroup;
    private final NioEventLoopGroup workerGroup;

    private final int port;


    public POSServer(int port) {
        this.bossGroup = new NioEventLoopGroup(2);
        this.workerGroup = new NioEventLoopGroup(POSServer.ioIntesivePoolSize() / 2);
        this.bootstrap = new ServerBootstrap();
        this.port = port;
    }

    public void start() throws InterruptedException, IOException {
        WebSocketChannelInitializer webSocketChannelInitializer = new WebSocketChannelInitializer();
        this.bootstrap.group(this.bossGroup, this.workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(webSocketChannelInitializer);
        ChannelFuture channelFuture = this.bootstrap.bind(this.port).sync();
        log.info(" server start at port: {}", this.port);

        try {
            channelFuture.channel().closeFuture().sync();
        } finally {
            this.bossGroup.shutdownGracefully();
            this.workerGroup.shutdownGracefully();
        }
    }


    private static int ioIntesivePoolSize() {
        double blockingCoefficient = 0.9D;
        return poolSize(blockingCoefficient);
    }

    private static int poolSize(double blockingCoefficient) {
        int numberOfCores = Runtime.getRuntime().availableProcessors();
        int poolSize = (int)((double)numberOfCores / (1.0D - blockingCoefficient));
        return poolSize;
    }
}
