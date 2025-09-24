package app.springdev.socket.tcp.netty.advanced.protocol;

import app.springdev.socket.tcp.netty.advanced.MyMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ProtocolServer {
    public static void main(String[] args) throws Exception {
        int port = 8081;
        ObjectMapper mapper = new ObjectMapper();
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new LineBasedFrameDecoder(2048));
                            ch.pipeline().addLast(new StringDecoder(), new StringEncoder());
                            ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                    // msg ends w/ newline trimmed by LineBasedFrameDecoder
                                    MyMessage m = mapper.readValue(msg, MyMessage.class);
                                    System.out.println("JSON RECV: " + m);
                                    // 답장: type echo
                                    MyMessage reply = new MyMessage("echo", m.getSessionId(), m.getPayload());
                                    ctx.writeAndFlush(mapper.writeValueAsString(reply) + "\n");
                                }
                            });
                        }
                    });

            ChannelFuture f = b.bind(port).sync();
            System.out.println("Protocol Server running on " + port);
            f.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
