package com.ytl.netty.basic;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) throws Exception{

        //创建一个线程组
        EventLoopGroup group = new NioEventLoopGroup();
        //创建客户端的启动助手，完成各种配置
        Bootstrap b = new Bootstrap();
        b.group(group)//3.设置线程组
            .channel(NioSocketChannel.class) //4.设置客户端通道的实现类
            .handler(new ChannelInitializer<SocketChannel>() {  //5.创建一个通道的初始化对象

                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new NettyClientHandler());//6.往Pipeline链中添加Handler方法
                }
            });
        System.out.println("======Client is ready======");
        //7.启动客户端去连接服务器端（异步非阻塞）
        ChannelFuture cf = b.connect("127.0.0.1",9999).sync();
        //8.关闭连接（异步非阻塞）
        cf.channel().closeFuture().sync();

    }


}
