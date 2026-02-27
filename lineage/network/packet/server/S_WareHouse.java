package lineage.network.packet.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import lineage.bean.database.Exp;
import lineage.bean.database.Item;
import lineage.bean.database.Warehouse;
import lineage.database.DatabaseConnection;
import lineage.database.ExpDatabase;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.share.Lineage;
import lineage.world.object.instance.DwarfInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.item.sp.CharacterSaveMarbles;

public class S_WareHouse extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, DwarfInstance dwarf, int dwarf_type, List<Warehouse> list){
		if(bp == null)
			bp = new S_WareHouse(dwarf, dwarf_type, list);
		else
			((S_WareHouse)bp).toClone(dwarf, dwarf_type, list);
		return bp;
	}
	
	public S_WareHouse(DwarfInstance dwarf, int dwarf_type, List<Warehouse> list){
		toClone(dwarf, dwarf_type, list);
	}
	
	public void toClone(DwarfInstance dwarf, int dwarf_type, List<Warehouse> list){
		clear();
		
		writeC(Opcodes.S_OPCODE_WAREHOUSE);
		writeD(dwarf.getObjectId());
		writeH(list.size());
		switch(dwarf_type){
			case 1:	// 혈맹창고찾기
				writeC(5);
				break;
			case 2:	// 요정창고찾기
				writeC(9);
				break;
			default:						// 일반창고찾기
				writeC(3);
				break;
		}
		readDB(list);		// 창고 목록
	}
	
	private void readDB(List<Warehouse> list){
		for(Warehouse wh : list) {
			Item item = ItemDatabase.find( wh.getName() );
			StringBuffer item_name = new StringBuffer();

			if(item == null){
				item_name.append("none");
			}else{
				if(item.getNameIdNumber()==1075 && item.getInvGfx()!=464){
					item_name.append( readLetterDB(wh.getLetterId()) );
				}else{
					if (item.getItemId() == 1000639) {
						Connection con = null;
						PreparedStatement st = null;
						ResultSet rs = null;
						try {
							con = DatabaseConnection.getLineage();
							st = con.prepareStatement("SELECT * FROM characters_save_marble WHERE itemobjid=?");
							st.setLong(1, wh.getInvId());
							rs = st.executeQuery();
							if (rs.next()) {
								ItemInstance marble = ItemDatabase.newInstance(item);
								if (marble != null && marble instanceof CharacterSaveMarbles) {
									CharacterSaveMarbles ii = (CharacterSaveMarbles) marble;
									ii.setObjectId(wh.getInvId());
									ii.toWorldJoin(con, null);

									String class_type = "";
									switch (ii.getCharacterClassType()) {
									case 0:
										class_type = "군주";
										break;
									case 1:
										class_type = "기사";
										break;
									case 2:
										class_type = "요정";
										break;
									case 3:
										class_type = "법사";
										break;
									case 4:
										class_type = "다크엘프";
										break;
									}
									item_name.append(class_type + " (" + ii.getCharacterName() + ")");
									item_name.append("\r\n");
									item_name.append("[Lv." + ii.getCharacterLevel() + " ");
									double exp = 0.0;
									Exp exp_bean = ExpDatabase.find(ii.getCharacterLevel());
									if (exp_bean != null) {
										double e = ii.getCharacterExp() - (exp_bean.getBonus() - exp_bean.getExp());
										exp = (e / exp_bean.getExp()) * 100;
									}
									item_name.append(String.format("%.2f%%", exp) + "]");
									ii.close();
									ii = null;
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							DatabaseConnection.close(con, st, rs);
						}
					}else {
					
					if(wh.isDefinite() && (wh.getType()==1 || wh.getType()==2)){
						String element_name = null;
						Integer element_en = 0;
						if(wh.getEnWind() > 0) {
							element_name = "풍령";
							element_en = wh.getEnWind();
						}
						if(wh.getEnEarth() > 0) {
							element_name = "지령";
							element_en = wh.getEnEarth();
						}
						if(wh.getEnWater() > 0) {
							element_name = "수령";
							element_en = wh.getEnWater();
						}
						if(wh.getEnFire() > 0) {
							element_name = "화령";
							element_en = wh.getEnFire();
						}
						if(element_name != null) {
							item_name.append(element_name).append(":").append(element_en).append("단");
							item_name.append(" ");
						}
						if(wh.getEn()<0)
							item_name.append("-");
						else
							item_name.append("+");
						item_name.append(wh.getEn());
					}
					item_name.append(" ").append(item.getNameId());
					if(wh.getCount()>1){
						item_name.append(" (");
						item_name.append(wh.getCount());
						item_name.append(")");
					}
					if(item.getNameIdNumber()==1173)
						item_name.append( readPetDB(wh.getPetId()) );
				
					}
				}
			}
			
			writeD(wh.getUid());				// 번호
			writeC(wh.getType());				// 타입
			writeH(wh.getGfxid());				// GFX 아이디
			writeC(wh.getBress());				//	1: 보통 0: 축 2: 저주
			writeD(wh.getCount());				// 현재아템 총수량
			writeC(wh.isDefinite() ? 1 : 0);	// 1: 확인 0: 미확인
			writeS(item_name.toString());		// 이름
			if(Lineage.server_version >= 380)
				writeC(0x00);
		}
		writeD(Lineage.warehouse_price);
	}
	
	/**
	 * 펫 아이디로 이름처리에 사용될 정보만 뽑아서 문자열로 리턴.
	 * @param con
	 * @param pet_id
	 * @return
	 */
	private String readPetDB(int pet_id){
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters_pet WHERE objid=?");
			st.setInt(1, pet_id);
			rs = st.executeQuery();
			if(rs.next())
				return String.format(" [Lv.%d %s]", rs.getInt("level"), rs.getString("name"));
			
		} catch (Exception e) {
			lineage.share.System.printf("%s : readPetDB(int pet_id)\r\n", S_WareHouse.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return "";
	}
	
	/**
	 * 편지 정보 추출.
	 * @param con
	 * @param letter_id
	 * @return
	 */
	private String readLetterDB(int letter_id) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters_letter WHERE uid=?");
			st.setInt(1, letter_id);
			rs = st.executeQuery();
			if(rs.next())
				return String.format("%s : %s", rs.getString("paperFrom"), rs.getString("paperSubject"));
			
		} catch (Exception e) {
			lineage.share.System.printf("%s : readLetterDB(int letter_id)\r\n", S_WareHouse.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
		return "";
	}
}
