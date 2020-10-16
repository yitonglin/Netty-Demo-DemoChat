package com.ytl.netty.basic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    //通道就绪事件
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("Client:"+ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("老兲还钱", CharsetUtil.UTF_8));
    }

    //读取数据事件
    public void channelRead(ChannelHandlerContext ctx,Object msg){
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器端发来的数据："+buf.toString(CharsetUtil.UTF_8));

    }
}
