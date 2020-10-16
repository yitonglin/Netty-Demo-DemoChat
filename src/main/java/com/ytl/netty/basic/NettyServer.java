package com.ytl.netty.basic;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws Exception{

        //1.创建轮询线程组：接收客户端的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //2.创建一个线程组：处理网络IO操作
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //3.创建服务器的启动助手来配置参数
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup,workerGroup)//4.设置两个线程组
            .channel(NioServerSocketChannel.class)//5.底层使用NIO的服务连接通道作为服务器的通道实现
            .option(ChannelOption.SO_BACKLOG,128)//6.设置线程队列的等待的连接个数
            .childOption(ChannelOption.SO_KEEPALIVE,true)//7.保持活动的连接
            .childHandler(new ChannelInitializer<SocketChannel>() { //8.创建一个通道初始化的对象
                public void initChannel(SocketChannel sc){ //9.向Pipeline（Handler链中添加自定义的Handler）
                    sc.pipeline().addLast(new NettyServerHandler());
                }
            });
        System.out.println("======  Server is ready  =======");
        ChannelFuture cf = b.bind(9999).sync();//10.绑定端口，绑定好端口后会自动启动   .sync为设置非阻塞
        System.out.println("*********  Server is starting  ********");

        //11.关闭通道，关闭线程组
        cf.channel().closeFuture().sync();//此时设置非阻塞是因为线程组关闭可能会比较耗费时间
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
