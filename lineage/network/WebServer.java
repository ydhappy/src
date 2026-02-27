package lineage.network;

import static org.jboss.netty.channel.Channels.pipeline;
import static org.jboss.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static org.jboss.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.OK;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lineage.plugin.PluginController;
import lineage.share.Socket;
import lineage.share.TimeLine;
import lineage.share.Web;
import lineage.util.Util;
import lineage.world.controller.BoardController;
import lineage.world.controller.RankController;
import lineage.world.controller.ShopController;
import lineage.world.controller.SpServerController;
import lineage.world.controller.WebAccountController;
import lineage.world.controller.WebAppCenterController;
import lineage.world.controller.WebAuctionController;
import lineage.world.controller.WebInformationController;
import lineage.world.controller.WebPremiumController;
import lineage.world.controller.WebSearchItemController;
import lineage.world.controller.WebSearchMonsterController;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.util.CharsetUtil;

public final class WebServer extends SimpleChannelUpstreamHandler implements ChannelPipelineFactory {

	//
	private static WebServer ws;
	private static ServerBootstrap sb;
	//
	private HttpRequest request;
	
	/**
	 * 웹서비스 처리를 위해 사용될 서버 초기화 함수.
	 */
	static public void init(){
		TimeLine.start("WebServer..");
		
		if(Web.web_server) {
			//
			ws = new WebServer();
			//
			ExecutorService se = Executors.newCachedThreadPool();
			sb = new ServerBootstrap(new NioServerSocketChannelFactory(se, se));
			sb.setPipelineFactory(ws);
			sb.setOption("child.tcpNoDelay", true);
			sb.setOption("child.receiveBufferSize", Socket.packet_recv_max);
			sb.setOption("connectTimeoutMillis", 1000);
			sb.bind(new InetSocketAddress(Web.web_server_port));
		}
		
		TimeLine.end();
	}
	
	static public void close() {
		TimeLine.start("WebServer Close...");
		
		if(Web.web_server) {
			sb = null;
		}
		
		TimeLine.end();
	}
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();
		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("deflater", new HttpContentCompressor());
		pipeline.addLast("handler", this);
		return pipeline;
	}
	
	@Override
	public synchronized void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		//
		HttpRequest request = this.request = (HttpRequest) e.getMessage();
		if (is100ContinueExpected(request))
			send100Continue(e);
		//
		ChannelBuffer content = ChannelBuffers.dynamicBuffer();
		String type = "application/javascript; charset=UTF-8";
		String uri = request.getUri();
		String host = request.getHeader("Host");
		if(uri.indexOf("?") > 0)
			uri = uri.substring(0, uri.indexOf("?"));
		// 380 앱센터 연동 부분.
//		System.out.println( request.getHeader("Host") );
//		System.out.println( " : " + uri );
		if(
				host!=null &&
				(
						//
						host.equalsIgnoreCase("g.lineage.plaync.co.kr") ||
						host.equalsIgnoreCase("pna.ncsoft.net") ||
						host.equalsIgnoreCase("static.plaync.co.kr") ||
						// 
						host.equalsIgnoreCase("www.youtube.com") ||
						host.equalsIgnoreCase("kr.plaync.com") ||
						host.equalsIgnoreCase("s.ytimg.com") ||
						host.equalsIgnoreCase("www.google.com") ||
						host.equalsIgnoreCase("s.youtube.com") ||
						host.equalsIgnoreCase("r3---sn-3u-bh2l.googlevideo.com") ||
						host.equalsIgnoreCase("g.lineage.power.plaync.com") ||
						host.equalsIgnoreCase("lineage.power.plaync.com") ||
						host.equalsIgnoreCase("g.lineage.qna.plaync.com")
						
				)
																							) {
			try {
				//
				if(uri.endsWith(".gif"))
					type = "image/gif";
				else if(uri.endsWith(".css"))
					type = "text/css";
				else if(uri.endsWith(".js"))
					type = "text/javascript; charset=utf-8";
				else
					type = "text/html; charset=utf-8";
				//
				content.writeBytes( WebAppCenterController.action(request) );
			} catch (Exception e2) {
				System.out.println(e2);
			}
//			System.out.println( " : " + type );
		} else {
			// 파라미터구분.
			QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
			Map<String, List<String>> params = queryStringDecoder.getParameters();
			if (!params.isEmpty()) {
				StringBuffer buf = new StringBuffer();
				try {
					// 인증
					String time = params.get("time").get(0);
					String passwd = params.get("sp").get(0);
					String web_server_passwd = Util.toMD5( Web.web_server_passwd + time );
					// 커트라인 30초 내로 설정.
					long cutline = System.currentTimeMillis() - Long.valueOf(time);
					if(90*1000>cutline && passwd.equalsIgnoreCase( web_server_passwd )) {
						// 처리
						List<String> value = params.get("action");
						if(value!=null && value.size()==1) {
							String action = value.get(0);
							if(PluginController.init(WebServer.class, "messageReceived", action, buf, params) == null) {
								if("rank".equalsIgnoreCase(action))
									buf.append( RankController.toJavaScript() );
								if("shop".equalsIgnoreCase(action))
									buf.append( ShopController.toJavaScript(params) );
								if("board".equalsIgnoreCase(action))
									buf.append( BoardController.toJavaScript(params) );
								if("auction".equalsIgnoreCase(action))
									buf.append( WebAuctionController.toJavaScript(params) );
								if("items".equalsIgnoreCase(action))
									buf.append( WebSearchItemController.toJavaScript(params) );
								if("info".equalsIgnoreCase(action))
									buf.append( WebInformationController.toJavaScript(params) );
								if("premium".equalsIgnoreCase(action))
									buf.append( WebPremiumController.toJavaScript(params) );
								if("monster".equalsIgnoreCase(action))
									buf.append( WebSearchMonsterController.toJavaScript(params) );
								if("account".equalsIgnoreCase(action))
									buf.append( WebAccountController.toJavaScript(params) );
								if("sp_server_chatting_global".equalsIgnoreCase(action))
									buf.append( SpServerController.toChattingGlobalRequest(params) );
								if("sp_controller_ping".equalsIgnoreCase(action))
									buf.append( "yes" );
								if("sp_controller_info".equalsIgnoreCase(action))
									buf.append( SpServerController.toServerInfo(params) );
								if("sp_controller_shutdown".equalsIgnoreCase(action))
									SpServerController.toShutdown();
								if("sp_controller_save".equalsIgnoreCase(action))
									SpServerController.toSave();
							}
						}
					}
				} catch (Exception e2) { }
				content.writeBytes( ChannelBuffers.copiedBuffer(buf.toString(), CharsetUtil.UTF_8) );
			}
		}
		// 전송
		writeResponse(e, content, type);
	}
	
	private void writeResponse(MessageEvent e, ChannelBuffer content, String type) {
		// Decide whether to close the connection or not.
		boolean keepAlive = isKeepAlive(request);

		// Build the response object.
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
		response.setContent(content);
		response.setHeader(CONTENT_TYPE, type);
		if (keepAlive) {
			// Add 'Content-Length' header only for a keep-alive connection.
			response.setHeader(CONTENT_LENGTH, response.getContent().readableBytes());
			// Add keep alive header as per:
			// - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
			response.setHeader(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		}
		// Write the response.
		ChannelFuture future = e.getChannel().write(response);
		// Close the non-keep-alive connection after the write operation is done.
		if (!keepAlive)
			future.addListener(ChannelFutureListener.CLOSE);
	}
	
	private static void send100Continue(MessageEvent e) {
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, CONTINUE);
		e.getChannel().write(response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		e.getChannel().close();
	}

}
