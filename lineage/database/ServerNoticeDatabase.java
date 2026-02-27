package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Notice;
import lineage.network.LineageClient;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Notice;
import lineage.share.TimeLine;

public class ServerNoticeDatabase {

	static private List<Notice> list;
	static private List<Notice> list_static;
	
	static public void init(Connection con){
		TimeLine.start("ServerNoticeDatabase..");
		
		list = new ArrayList<Notice>();
		list_static = new ArrayList<Notice>();
		
//		//
//		Notice n = new Notice();
//		n.setUid( list_static.size()+1 );
//		n.setType("static");
//		n.setSubject("SP Revolution");
//		n.setContent(
//				"1.38부터 최신한국옵까지 구동가능한 서버팩입니다.\r\n" +
//				"디비 및 컨텐츠가 부실하여 실제 운영에는 많은 문제가 많지만 \r\n" +
//				"지속적인 개발과 여러분에 관심으로 계속 발전하는 중입니다.\r\n" +
//				"앞으로도 잘 부탁드립니다. 감사합니다.\r\n\r\n" + 
//				"네이트온 : lucy_lineage@nate.com\r\n" +
//				"사이트 : lincom.whis.co.kr\r\n"
//		);
//		list_static.add( n );
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			// 무조건 표현할 공지 먼저 추출.
			st = con.prepareStatement("SELECT * FROM server_notice ORDER BY uid");
			rs = st.executeQuery();
			while(rs.next()){
				Notice n = new Notice();
				n.setUid( list_static.size()+1 );
				n.setType( rs.getString("type") );
				n.setSubject( rs.getString("subject") );
				n.setContent( rs.getString("content") );
				
				if(n.getType().equalsIgnoreCase("static"))
					list_static.add( n );
				else
					list.add( n );
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", ServerNoticeDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		
		TimeLine.end();
	}
	
	/**
	 * 공지사항 확인하는 함수.
	 *  : 표현될 공지사항이 있을경우 true 리턴함.
	 *  : 공지사항 표현 패킷 처리함.
	 * @param c
	 * @return
	 */
	static public boolean toNotice(LineageClient c){
		// 매번 표현될 공지 부분.
		for(Notice n : list_static){
			if(c.getAccountNoticeStaticUid()<n.getUid()){
				toNotice(c, n, true);
				return true;
			}
		}
		// 한번만 표현될 공지 부분.
		for(Notice n : list){
			if(c.getAccountNoticeUid() < n.getUid()){
				toNotice(c, n, false);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 중복코드 방지용.
	 * @param c
	 * @param n
	 * @param _static
	 */
	static private void toNotice(LineageClient c, Notice n, boolean _static){
		// uid 갱신.
		if(_static)
			c.setAccountNoticeStaticUid(n.getUid());
		else
			c.setAccountNoticeUid(n.getUid());
		// 공지사항 표현.
		c.toSender(S_Notice.clone(BasePacketPooling.getPool(S_Notice.class), String.format("\\f2%s\r\n%s", n.getSubject(), n.getContent())));
	}
	
	static public String getNoticeFirst() {
		for(Notice n : list_static)
			return n.getContent();
		for(Notice n : list)
			return n.getContent();
		return null;
	}
	
	static public void setSpNotice(String title, String content) {
//		if(title==null || content==null) {
//			list_static.remove(0);
//		} else {
//			Notice n = list_static.get(0);
//			n.setSubject(title);
//			n.setContent(content);
//		}
	}
	
}
