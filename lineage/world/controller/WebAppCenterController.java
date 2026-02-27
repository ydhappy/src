package lineage.world.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import lineage.share.TimeLine;

import org.jboss.netty.handler.codec.http.HttpRequest;

public class WebAppCenterController {

	private static Object sync;
	private static Map<String, String> CookieMap;
	
	public static void init() {
		TimeLine.start("WebAppCenterController..");
		
		//
		sync = new Object();
		CookieMap = new HashMap<String, String>();
		
		TimeLine.end();
	}
	
	public static byte[] action(HttpRequest request) {
		synchronized (sync) {
			//
			initCookie( request.getHeader("Cookie") );
			//
			String host = request.getHeader("Host");
			String uri = request.getUri();
			String path = uri;
			String parameter = null;
			//
			if(uri.indexOf("?") > 0) {
				path = uri.substring(0, uri.indexOf("?"));
				parameter = uri.substring(uri.indexOf("?") + 1);
			}
			//
			if(path.endsWith(".jpg") || path.endsWith(".gif")) {
				String url = "http://"+host+uri;
				return getHtml("UTF-8", url, parameter, null, request.getHeader("Cookie"), host, request.getHeader("Referer"), true);
			}
			//
			if(host.equalsIgnoreCase("www.youtube.com")) {
				String url = "https://"+host+uri;
				byte[] data = getHtml("UTF-8", url, parameter, null, request.getHeader("Cookie"), host, request.getHeader("Referer"), true);
				return data;
			}
			//
			if(host.equalsIgnoreCase("kr.plaync.com") || host.equalsIgnoreCase("www.google.com") || host.equalsIgnoreCase("s.ytimg.com") || host.equalsIgnoreCase("s.youtube.com") || host.equalsIgnoreCase("r3---sn-3u-bh2l.googlevideo.com") || host.equalsIgnoreCase("g.lineage.power.plaync.com") || host.equalsIgnoreCase("g.lineage.qna.plaync.com")) {
				String url = "http://"+host+uri;
				try {
					return getHtml("UTF-8", url, parameter, null, request.getHeader("Cookie"), host, request.getHeader("Referer"), false);
				} catch (Exception e) {
					return getHtml("UTF-8", url, parameter, null, request.getHeader("Cookie"), host, request.getHeader("Referer"), true);
				}
			}
			//
			if(path.equalsIgnoreCase("/ingame/index"))
				return toIndex(host, uri, path + ".html");
			else if(path.equalsIgnoreCase("/ingame/ingame_warrior"))
				return readFile(host, path + ".html");
			else if(path.equalsIgnoreCase("/ingame/item/intro"))
				return toItemIntro(host, uri, path + ".html");
			else if(path.equalsIgnoreCase("/ingame/item/itemClientSearch"))
				return itemClientSearch(host, uri, path + ".html");
			else if(path.equalsIgnoreCase("/ingame/item/ajaxMainNewItemList"))
				return ajaxMainNewItemList(host, uri, path + ".html");
			else if(path.equalsIgnoreCase("/ingame/item/itemInfo"))
				return itemInfo(host, uri, path + ".html");
			else if(path.equalsIgnoreCase("/ingame/item/itemSearch"))
				return toItemSearch(host, uri, path + ".html");
			else if(path.equalsIgnoreCase("/ingame/item/itemMonthInfo"))
				return itemMonthInfo(host, uri, path + ".html");
			else if(path.equalsIgnoreCase("/ingame/item/itemRank"))
				return itemRank(host, uri, path + ".html");
			else if(path.equalsIgnoreCase("/ingame/item/favoriteList"))
				return favoriteList(host, uri, path + ".html");
			else
				return readFile(host, path);
			
//			String path = uri.lastIndexOf(".")<0 ? String.format("resource/http%s/index.html", uri) : String.format("resource/http%s", uri);
//			if(uri.indexOf("?") > 0) {
//				path = String.format("resource/http%s/index.html", uri.substring(0, uri.indexOf("?")));
//				String parameter = uri.substring(uri.indexOf("?") + 1);
//			}
//			System.out.println( path );
			
		}
	}
	
	/**
	 * 쿠키정보를 토대로 기본 세팅을 하는 메서드.
	 * @param cookie
	 */
	static private void initCookie(String cookie) {
		// lineage1_webLoginState=1; JSESSIONID=2E2C0C1C477518B2319DBFFA2A451946.GP-webmwA_Lineage1-Cloud3_849; GPLLV=1; MGPVLU=A410A1853637EE50901788DDC542AC962072FFD80A232557571BF6F17695F70124E10FB1814AD21EA911F6EB46132058; MGPSEC=6F9F3A58B3E4F92D4C25533FA51E14851C55A5E3AB69BB2BDF53424E7D86A55E79F05CF4E598BE1DDC391899B0647F76372D0F70BD9B0E7815929B583F0AFC46; MGPSECEXPIREAT=B17E5659CAD86EEE95D0B8470F30CD9A74B6BFA36C441D53EEE82CD777BE41EF; lineage1_itemSearchQuery=15224%2C1283%2C26%2C3015%2C; lineage1_itemQuery=1234; lineage1_serverId=1; lineage1_charId=%C0%FC%BB%E7; lineage1_charLevel=3; lineage1_charClassId=7; lineage1_charGender=0; lineage1_pledgeName=
		CookieMap.clear();
		if(cookie == null)
			return;
		StringTokenizer token = new StringTokenizer(cookie, ";");
		while(token.hasMoreTokens()) {
			String t = token.nextToken();
			String[] value = t.split("=");
			try { CookieMap.put(value[0].trim(), value[1].trim()); } catch (Exception e) {}
		}
	}
	
	static private byte[] readFile(String host, String path) {
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream( String.format("resource/http/%s%s", host, path) )); 
			byte[] data = new byte[bis.available()]; 
			bis.read(data, 0, data.length); 
			bis.close();
			return data;
		} catch (Exception e) { }
		return null;
	}
	
	static private Map<String, String> readParameter(String uri) {
		Map<String, String> parameter_list = new HashMap<String, String>();
		try {
			String parameter = uri.indexOf("?")>0 ? uri.substring(uri.indexOf("?")+1) : null;
			if(parameter != null) {
					for(String value : parameter.split("&")) {
						String[] values = value.split("=");
						parameter_list.put(values[0], URLDecoder.decode(values[1], "UTF-8"));
					}
			}
		} catch (Exception e) { }
		return parameter_list;
	}

	static private byte[] toIndex(String host, String uri, String path) {
		//
		byte[] data = readFile(host, path);
		String html = new String(data);
		//
		try {
			html = html.replaceAll("\\{lineage1_charLevel}", CookieMap.get("lineage1_charLevel"));
			html = html.replaceAll("\\{lineage1_charClassId}", CookieMap.get("lineage1_charClassId"));
			html = html.replaceAll("\\{lineage1_charId}", URLDecoder.decode(CookieMap.get("lineage1_charId"), "EUC-KR"));
			html = html.replaceAll("\\{lineage1_charGender}", CookieMap.get("lineage1_charGender").equalsIgnoreCase("0") ? "false" : "true");
			int charClassId = Integer.valueOf( CookieMap.get("lineage1_charClassId") );
			switch(charClassId) {
				case 0:
					html = html.replaceAll("\\{lineage1_charClassName}", "군주");
					break;
				case 1:
					html = html.replaceAll("\\{lineage1_charClassName}", "기사");
					break;
				case 2:
					html = html.replaceAll("\\{lineage1_charClassName}", "요정");
					break;
				case 3:
					html = html.replaceAll("\\{lineage1_charClassName}", "마법사");
					break;
				case 4:
					html = html.replaceAll("\\{lineage1_charClassName}", "다크엘프");
					break;
				case 5:
					html = html.replaceAll("\\{lineage1_charClassName}", "용기사");
					break;
				case 6:
					html = html.replaceAll("\\{lineage1_charClassName}", "환술사");
					break;
				case 7:
					html = html.replaceAll("\\{lineage1_charClassName}", "전사");
					break;
			}
		} catch (Exception e) { }
		//
		return html.getBytes();
	}
	
	static private byte[] toItemIntro(String host, String uri, String path) {
		//
		byte[] data = readFile(host, path);
		String html = new String(data);
		//
		return html.getBytes();
	}
	
	static private byte[] itemClientSearch(String host, String uri, String path) {
		//
		byte[] data = readFile(host, path);
		String html = new String(data);
		//
		return html.getBytes();
	}
	
	static private byte[] ajaxMainNewItemList(String host, String uri, String path) {
		//
		byte[] data = readFile(host, path);
		String html = new String(data);
		//
		return html.getBytes();
	}
	
	static private byte[] itemInfo(String host, String uri, String path) {
		//
		byte[] data = readFile(host, path);
		String html = new String(data);
		//
		return html.getBytes();
	}
	
	static private byte[] toItemSearch(String host, String uri, String path) {
		//
		byte[] data = readFile(host, path);
		String html = new String(data);
		// query, page
		Map<String, String> parameter_list = readParameter(uri);
		html = html.replaceAll("\\{sitem}", parameter_list.get("query"));
		//
		return html.getBytes();
	}
	
	static private byte[] itemMonthInfo(String host, String uri, String path) {
		//
		byte[] data = readFile(host, path);
		String html = new String(data);
		//
		return html.getBytes();
	}
	
	static private byte[] itemRank(String host, String uri, String path) {
		//
		byte[] data = readFile(host, path);
		String html = new String(data);
		//
		return html.getBytes();
	}
	
	static private byte[] favoriteList(String host, String uri, String path) {
		//
		byte[] data = readFile(host, path);
		String html = new String(data);
		//
		return html.getBytes();
	}
	
	static private byte[] getHtml(String charset, String url, String parameter, String method, String cookie, String host, String referer, boolean isByte) {
		if(charset == null)
			charset = "EUC-KR";
		
		HttpURLConnection conn = null;
		BufferedReader br = null;
		ByteArrayOutputStream data = null;
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
			//
			conn = toUrl(url, parameter, method, cookie, host, referer);
			//
			if(isByte) {
				// 파일명 추출.
				String header = conn.getHeaderField("Content-Disposition");
				String filename = header==null ? null : header.substring(header.indexOf("=")+1);
				// 쌍콤마로 시작된다면 양족에 상콤마 제거하기.
				if(filename!=null && filename.startsWith("\""))
					filename = filename.substring(1, filename.length()-1);
				// / 로 시작된다면 제거.
				if(filename!=null && filename.startsWith("/"))
					filename = filename.substring(1);
				// 
				if(filename != null)
					filename = URLDecoder.decode(filename, charset);
				else
					filename = url.substring(url.lastIndexOf("/") + 1);
				//
				if(filename != null)
					filename = filename.indexOf("?")>0 ? filename.substring(0, filename.indexOf("?")) : filename;

				data = new ByteArrayOutputStream();
				bos = new BufferedOutputStream(data);
				if("gzip".equals(conn.getContentEncoding()))
					bis = new BufferedInputStream( new GZIPInputStream(conn.getInputStream()) );
				else
					bis = new BufferedInputStream(conn.getInputStream());
				int count = 300;
				while(true) {
					if (count-- < 0) {
						break;
					}
					int a = bis.read();
					if(a < 0)
						break;
					
					bos.write(a);
				}
				bos.flush();
				
				return data.toByteArray();
			} else {
				if("gzip".equals(conn.getContentEncoding()))
					br = new BufferedReader(new InputStreamReader(new GZIPInputStream(conn.getInputStream()), charset));
				else
					br = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
				
				String temp;
				StringBuilder sb = new StringBuilder();
				while ((temp = br.readLine()) != null)
					sb.append(temp);
				return sb.toString().getBytes();
			}
			
		} catch (Exception e) {
		} finally {
			try {
				if(br != null)
					br.close();
				if(conn != null)
					conn.disconnect();
			} catch (Exception e2) { }
		}

		return null;
	}
	
	static private HttpURLConnection toUrl(String url, String parameter, String method, String cookie, String host, String referer) throws Exception {
		//
		URL Url = new URL(url);
		HttpURLConnection conn = null;
		conn = (HttpURLConnection)Url.openConnection();
		conn.setRequestMethod(method==null ? "GET" : method);
		if(host != null)
			conn.setRequestProperty("Host", host);
		if(cookie != null)
			conn.setRequestProperty("Cookie", cookie);
		if(referer != null)
			conn.setRequestProperty("Referer", referer);
		conn.setRequestProperty("Accept", "*/*");
		conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
		conn.setRequestProperty("Accept-Language", "ko-KR,ko");
		conn.setRequestProperty("Accept-Charset", "iso-8859-1,*,utf-8");
		conn.setRequestProperty("User-Agent", "LineageIngameBrowser");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.connect();
		// 파라미터 등록.
		if(parameter != null) {
			OutputStream os = conn.getOutputStream();
			os.write(parameter.getBytes());
			os.flush();
			os.close();
		}
		//
		return conn;
	}
	
}
