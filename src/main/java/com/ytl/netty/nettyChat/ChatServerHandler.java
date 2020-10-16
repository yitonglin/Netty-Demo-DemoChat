package com.ytl.netty.nettyChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {


    //存储通道  广播时使用
    public static List<Channel> channels = new ArrayList<>();

    //通道就绪
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel inChannel = ctx.channel();  //获取通道
        channels.add(inChannel);//将获取到的通道放入集合中 用于广播使用
        System.out.println("[Server]："+inChannel.remoteAddress().toString().substring(1)+"上线^_^");
    }


    //通道未就绪
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel inChannel = ctx.channel();
        channels.remove(inChannel);//此处可理解为离线状态 将通道从广播集合中移除
        System.out.println("[Server]："+inChannel.remoteAddress().toString().substring(1)+"离线-_-");

    }


    //读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        Channel inChannel = ctx.channel();
        for (Channel channel:channels) {
            if (channel!=inChannel){  //将自己排除在广播之外
                channel.writeAndFlush("["+inChannel.remoteAddress().toString().substring(1)+"]"+"说："+s+"\n");
            }

        }

    }
}
