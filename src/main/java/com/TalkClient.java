package com;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TalkClient {
    public void run() throws UnknownHostException, InterruptedException {
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer() {

                protected void initChannel(Channel channel) throws Exception {
                    channel.pipeline()
                            .addLast("decoder", new StringDecoder())
                            .addLast("encoder", new StringEncoder())
                            .addLast(new TalkClientHandler());
                }
            });

            ChannelFuture cf = bootstrap.connect(InetAddress.getLocalHost().getHostAddress(), 9999).sync();
            Channel channel = cf.channel();
            System.out.println("-------" + channel.localAddress().toString().substring(1) + "--------");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()){
                String msg = scanner.nextLine();
                channel.writeAndFlush(msg);
            }
            cf.channel().closeFuture().sync();
        }finally {
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException, UnknownHostException {
        new TalkClient().run();
    }
}
