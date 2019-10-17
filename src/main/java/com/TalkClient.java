package com;

import b.a.B;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TalkClient {
    public void run() throws UnknownHostException, InterruptedException {
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker);
            bootstrap.channel(SocketChannel.class);
            bootstrap.handler(new ChannelInitializer() {

                protected void initChannel(Channel channel) throws Exception {
                    channel.pipeline().addLast(new TalkClientHandler());
                }
            });

            ChannelFuture cf = bootstrap.connect(InetAddress.getLocalHost().getHostAddress(), 8080).sync();
            cf.channel().closeFuture().sync();
        }finally {
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException, UnknownHostException {
        new TalkClient().run();
    }
}
