package app.springdev.socket.tcp.netty.advanced.performance;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.channel.socket.SocketChannel;

public class PerformanceServer {
    public static void main(String[] args) throws Exception {
        int port = 8082;
        int cores = Runtime.getRuntime().availableProcessors();
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup(cores * 2);

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 2048)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_RCVBUF, 256 * 1024)
                    .childOption(ChannelOption.SO_SNDBUF, 256 * 1024)
                    .childOption(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 64 * 1024)
                    .childOption(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 32 * 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new StringDecoder(), new StringEncoder());
                            p.addLast(new SimpleChannelInboundHandler<String>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                                    // lightweight processing
                                    ctx.writeAndFlush("OK:" + msg + "\n");
                                }
                            });
                        }
                    });

            ChannelFuture f = b.bind(port).sync();
            System.out.println("Performance Server running on " + port + " (cores=" + cores + ")");
            f.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
