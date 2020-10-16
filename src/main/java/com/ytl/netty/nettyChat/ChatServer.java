package com.ytl.netty.nettyChat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ChatServer {
    private int port;//服务器端端口号

    public ChatServer(int port) {
        this.port = port;
    }

    public void run(){
        EventLoopGroup bossGroup = new NioEventLoopGroup();//设置轮询线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();//设置IO线程组
        try{
            ServerBootstrap b = new ServerBootstrap();//设置启动器
            b.group(bossGroup,workerGroup)  //将线程组设置其中
                .channel(NioServerSocketChannel.class)//底层使用NIO的通道作为服务端的通道
                .childHandler(new ChannelInitializer<SocketChannel>() {  //向Pipeline中设置Handler
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();//得到Pipeline链
                        //向Pipeline链中添加解码器
                        pipeline.addLast("decoder",new StringDecoder());
                        //向Pipeline链中添加编码器
                        pipeline.addLast("encoder",new StringEncoder());
                        //向Pipeline链中添加Handler
                        pipeline.addLast("handler",new ChatServerHandler());
                    }
                }).option(ChannelOption.SO_BACKLOG,128)//设置线程队列的等待个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true);//一直保持连接
            System.out.println("Netty Chat Server 启动…………");
            ChannelFuture f = b.bind(port).sync();//服务器设置端口并启动  bind为非阻塞  整个语句为阻塞
            f.channel().closeFuture().sync();//关闭通道  关闭线程组
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            System.out.println("Netty Chat Server 关闭…………");
        }
    }

    public static void main(String[] args) {
        new ChatServer(9999).run();
    }
}
