package server;



import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerImpl implements Server {
	//EventLoop 这个相当于一个处理线程，是Netty接收请求和处理IO请求的线程。
    //EventLoopGroup 可以理解为将多个EventLoop进行分组管理的一个类，是EventLoop的一个组。

	private EventLoopGroup bossGroup = new NioEventLoopGroup();
	private EventLoopGroup workGroup = new NioEventLoopGroup();
	private ServerBootstrap bootStrap = new ServerBootstrap(); //ServerBootstrap 从命名上看就可以知道，这是一个对服务端做配置和启动的类。
	private ChannelFuture cf;
	private String zkAddress;
	private String serversPath;
	private String currentServerPath;
	private ServerData sd;
	
	private volatile boolean binded = false;
	
	private final ZkClient zc;
	private final RegistProvider registProvider;
	
	private static final Integer SESSION_TIME_OUT = 10000;
	private static final Integer CONNECT_TIME_OUT = 10000;
		
	
	
	public String getCurrentServerPath() {
		return currentServerPath;
	}

	public String getZkAddress() {
		return zkAddress;
	}

	public String getServersPath() {
		return serversPath;
	}
	
	public ServerData getSd() {
		return sd;
	}

	public void setSd(ServerData sd) {
		this.sd = sd;
	}

	public ServerImpl(String zkAddress, String serversPath, ServerData sd){
		this.zkAddress = zkAddress;
		this.serversPath = serversPath;
		this.zc = new ZkClient(this.zkAddress,SESSION_TIME_OUT,CONNECT_TIME_OUT,new SerializableSerializer());		
		this.registProvider = new DefaultRegistProvider();
		this.sd = sd;
	}	
	
	//初始化服务端
	private void initRunning() throws Exception {
		
		String mePath = serversPath.concat("/").concat(sd.getPort().toString());
		//注册到zookeeper
		registProvider.regist(new ZookeeperRegistContext(mePath,zc,sd));
		currentServerPath = mePath;
	}

	public void bind() {
		
		if (binded){
			return;
		}
		
		System.out.println(sd.getPort()+":binding...");
		
		try {
			initRunning();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		bootStrap.group(bossGroup,workGroup)
		.channel(NioServerSocketChannel.class)
		.option(ChannelOption.SO_BACKLOG, 1024)
		.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline(); //ChannelPipeline 这是Netty处理请求的责任链，这是一个ChannelHandler的链表，而ChannelHandler就是用来处理网络请求的内容的。
                p.addLast(new ServerHandler(new DefaultBalanceUpdateProvider(currentServerPath,zc)));
            }
        });
		//ChannelHandler 用来处理网络请求内容，有ChannelInboundHandler和ChannelOutboundHandler两种，ChannlPipeline会从头到尾顺序调用ChannelInboundHandler处理网络请求内容，从尾到头调用

        //ChannelOutboundHandler处理网络请求内容。这也是Netty用来灵活处理网络请求的机制之一，
        // 因为使用的时候可以用多个decoder和encoder进行组合，从而适应不同的网络协议。
        // 而且这种类似分层的方式可以让每一个Handler专注于处理自己的任务而不用管上下游，
        // 这也是pipeline机制的特点。这跟TCP/IP协议中的五层和七层的分层机制有异曲同工之妙。
		try {
			cf =  bootStrap.bind(sd.getPort()).sync();
			binded = true;
			System.out.println(sd.getPort()+":binded...");
			cf.channel().closeFuture().sync();			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
		
	}
	



}
