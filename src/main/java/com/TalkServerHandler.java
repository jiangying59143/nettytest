package com;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

public class TalkServerHandler extends SimpleChannelInboundHandler<String> {

    static List<Channel> channelList = new ArrayList();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel inChannel = ctx.channel();
        channelList.add(inChannel);
        System.out.println("[Server:]" + inChannel.remoteAddress().toString().substring(1) + "上线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel inChannel = ctx.channel();
        channelList.remove(inChannel);
        System.out.println("[Server:]" + inChannel.remoteAddress().toString().substring(1) + "下线");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Channel inChannel = channelHandlerContext.channel();
        for (Channel channel : channelList) {
            if(channel != inChannel) {
                String msg = channel.remoteAddress().toString().substring(1) + ":" + s;
                System.out.println(msg);
                channel.writeAndFlush(msg);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
