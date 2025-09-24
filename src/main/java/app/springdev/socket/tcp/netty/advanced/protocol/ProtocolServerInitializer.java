package app.springdev.socket.tcp.netty.advanced.protocol;

import app.springdev.socket.tcp.netty.advanced.MyMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ProtocolServerInitializer extends ChannelInitializer<SocketChannel> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
        ch.pipeline().addLast(new StringDecoder(), new StringEncoder());
        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
            @Override
            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                MyMessage message = mapper.readValue(msg, MyMessage.class);
                System.out.println("받은 JSON: " + message);
                ctx.writeAndFlush(mapper.writeValueAsString(message) + "\n");
            }
        });
    }
}