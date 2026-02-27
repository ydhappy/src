package lineage.world.object.instance;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage.database.DatabaseConnection;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_ShopBuy;
import lineage.util.Util;
import lineage.world.object.object;

public class BackgroundInstance extends object {

	static synchronized public BackgroundInstance clone(BackgroundInstance bi){
		if(bi == null)
			bi = new BackgroundInstance();
		return bi;
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM npc_html WHERE npc_name_id=? ORDER BY uid ASC");
			st.setString(1, getName());
			rs = st.executeQuery();
			if(rs.next()) {
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, rs.getString("html")));
//				if(pc.getGm() > 0)
//					System.out.println( rs.getString("html") );
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : toTalk(PcInstance pc, ClientBasePacket cbp)\r\n", BackgroundInstance.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
		super.toTalk(pc, action, type, cbp, opt);
		
		//
		if("teleport trade-zone".equalsIgnoreCase(action)) {
			// 시장으로 텔레포트한다.
			int x = Util.random(32799, 32802);
			int y = Util.random(32925, 32927);
			pc.toTeleport(x, y, 800, true);
		} else if("buy".equalsIgnoreCase(action)) {
			Connection con = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				con = DatabaseConnection.getLineage();
				st = con.prepareStatement("SELECT * FROM npc_shop_packet WHERE npc_name_id=? ORDER BY uid ASC");
				st.setString(1, getName());
				rs = st.executeQuery();
				if(rs.next()) {
					// 패킷 추출한게 상점ui가 변경되기전꺼라서 기록된 패킷을 복원해서 약간 수정 후 다시 byte[] 로 변경. 그걸 전송처리하게함.
					byte[] data = Util.StringToByte(rs.getString("list"));
					
//					//
//					ClientBasePacket bp = (ClientBasePacket)ClientBasePacket.clone(null, data, data.length);
//					bp.seek(0);
//					//
//					int length = bp.readH();
//					int temp = 0;
//					//
//					for(int i=0 ; i<length ; ++i) {
//						int uid = bp.readD();
//						int gfx = bp.readH();
//						int price = bp.readD();
//						String name = bp.readS();
//						int tap = bp.readD();
//						int option_length = bp.readC()-1;
//						bp.readB(option_length);
//						
//						try {
//							System.out.println(Desc.find(name)+" >> " + tap);
//						} catch (Exception e) {
//							// TODO: handle exception
//						}
//					}
//					pc.toSender(S_ShopBuy.clone(BasePacketPooling.getPool(S_ShopBuy.class), this, data));
					//
					ClientBasePacket bp = (ClientBasePacket)ClientBasePacket.clone(null, data, data.length);
					bp.seek(0);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					//
					baos.write(data, bp.readIndex(), 2);
					int length = bp.readH();
					int temp = 0;
					//
					for(int i=0 ; i<length ; ++i) {
						baos.write(data, bp.readIndex(), 4);
						long uid = bp.readD();
						baos.write(data, bp.readIndex(), 2);
						int gfx = bp.readH();
						baos.write(data, bp.readIndex(), 4);
						long price = bp.readD();
						temp = bp.readIndex();
						String name = bp.readS();
						baos.write(name.getBytes());
						baos.write(0);
						baos.write(51&0xff);
						baos.write(0);
						baos.write(0);
						baos.write(0);
						baos.write(data, bp.readIndex(), 1);
						int option_length = bp.readC();
						baos.write(bp.readB(option_length));
						baos.write(0);
					}
					//
					pc.toSender(S_ShopBuy.clone(BasePacketPooling.getPool(S_ShopBuy.class), this, baos.toByteArray()));
				}
			} catch (Exception e) {
				lineage.share.System.printf("%s : toTalk(PcInstance pc, ClientBasePacket cbp)\r\n", BackgroundInstance.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(con, st, rs);
			}
		} else {
//			if(pc.getGm() > 0)
//				System.out.println(action);
		}
	}
	
}
