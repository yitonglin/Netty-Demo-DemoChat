package com.ytl.netty.nettyChat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

//聊天程序客户端
public class ChatClient {
    private final String host;//服务器端IP地址
    private final int port;//服务器端端口号

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run(){
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();//得到Pineline链
                            //向Pipeline链中添加解码器
                            pipeline.addLast("decoder",new StringDecoder());
                            //向Pipeline链中添加编码器
                            pipeline.addLast("encoder",new StringEncoder());
                            //向Pipeline链中添加Handler
                            pipeline.addLast("handler",new ChatClientHandler());
                        }
                    });
            Channel channel = bootstrap.connect(host,port).sync().channel();
            System.out.println("========"+channel.localAddress().toString().substring(1)+"================");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()){
                String msg = scanner.nextLine();
                channel.writeAndFlush(msg+"\r\n");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new ChatClient("127.0.0.1",9999).run();
    }

}
