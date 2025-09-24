package app.springdev.socket.tcp.netty.advanced.arch;

import app.springdev.socket.tcp.netty.advanced.MyMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.sync.RedisCommands;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.time.Duration;
import java.util.Properties;

public class ArchServer {
    public static void main(String[] args) throws Exception {
//        int port = 8084;
//        ObjectMapper mapper = new ObjectMapper();
//
//        // --- Redis (Lettuce) 예시 (로컬 Redis 가 필요)
//        RedisClient redisClient = RedisClient.create("redis://localhost:6379");
//        RedisCommands<String, String> redis;
//        try {
//            redis = redisClient.connect().sync();
//        } catch (Exception e) {
//            System.err.println("Redis 연결 실패: " + e.getMessage());
//            redis = null;
//        }
//
//        // --- Kafka producer 예시 (로컬 Kafka 필요)
//        Properties kafkaProps = new Properties();
//        kafkaProps.put("bootstrap.servers", "localhost:9092");
//        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProps);
//
//        // --- Metrics
//        PrometheusMeterRegistry prometheus = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
//        MeterRegistry registry = prometheus;
//
//        EventLoopGroup boss = new NioEventLoopGroup(1);
//        EventLoopGroup worker = new NioEventLoopGroup();
//
//        try {
//            ServerBootstrap b = new ServerBootstrap();
//            b.group(boss, worker)
//                    .channel(NioServerSocketChannel.class)
//                    .childHandler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        protected void initChannel(SocketChannel ch) {
//                            ch.pipeline().addLast(new io.netty.handler.codec.LineBasedFrameDecoder(4096));
//                            ch.pipeline().addLast(new io.netty.handler.codec.string.StringDecoder(), new io.netty.handler.codec.string.StringEncoder());
//                            ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
//                                @Override
//                                protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//                                    // JSON -> MyMessage
//                                    MyMessage m = mapper.readValue(msg, MyMessage.class);
//                                    System.out.println("Arch Received: " + m);
//
//                                    // 세션 정보 Redis에 저장 (예시)
//                                    if (redis != null && m.getSessionId() != null) {
//                                        redis.set("session:" + m.getSessionId(), m.getPayload());
//                                        redis.expire("session:" + m.getSessionId(), 300); // 5분
//                                    }
//
//                                    // Kafka로 전송 (내부 처리자에게 전달)
//                                    producer.send(new ProducerRecord<>("netty-topic", m.getSessionId(), mapper.writeValueAsString(m)));
//
//                                    // 간단 응답
//                                    ctx.writeAndFlush(mapper.writeValueAsString(new MyMessage("ack", m.getSessionId(), "ok")) + "\n");
//                                }
//
//                                @Override
//                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//                                    cause.printStackTrace();
//                                    ctx.close();
//                                }
//                            });
//                        }
//                    });
//
//            ChannelFuture f = b.bind(port).sync();
//            System.out.println("Arch Server running on " + port);
//            System.out.println("Prometheus metrics endpoint not implemented as HTTP here; use registry.scrape() where needed.");
//            f.channel().closeFuture().sync();
//        } finally {
//            producer.close(Duration.ofSeconds(1));
//            if (redisClient != null) redisClient.shutdown();
//            boss.shutdownGracefully();
//            worker.shutdownGracefully();
//        }
    }
}
