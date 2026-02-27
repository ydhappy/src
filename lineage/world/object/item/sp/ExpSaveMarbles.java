package lineage.world.object.item.sp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage.bean.database.Exp;
import lineage.database.DatabaseConnection;
import lineage.database.ExpDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryCount;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.network.packet.server.S_MessageYesNo;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class ExpSaveMarbles extends ItemInstance {
	private long saveExp;
	private int saveLevel;

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new ExpSaveMarbles();
		return item;
	}

	private ExpSaveMarbles() {
		saveExp = 0;
		saveLevel= 0;
	}
	
	@Override
	public void close() {
		super.close();
		saveExp = 0;
		saveExp = 0;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		try {
			if (getItem().getLevelMin() <= cha.getLevel() && getItem().getLevelMax() >= cha.getLevel()) {
				if (Lineage.server_version > 270) {
					if (cha instanceof PcInstance) {
						PcInstance pc = (PcInstance) cha;
						if (saveExp != 0) {
							pc.setMarblesObjectId(getObjectId());
							pc.setExpMarblesUse(true);
							pc.toSender(S_MessageYesNo.clone(BasePacketPooling.getPool(S_MessageYesNo.class), 622, "저장되어 있는 경험치를 사용하시겠습니까?"));
						} else {
							pc.setMarblesObjectId(getObjectId());
							pc.setSaveExp(true);
							pc.toSender(S_MessageYesNo.clone(BasePacketPooling.getPool(S_MessageYesNo.class), 622, "경험치를 저장하시겠습니까?"));
						}
					}
				} else {
					if (cha instanceof PcInstance) {
						PcInstance pc = (PcInstance) cha;

						if (saveExp != 0) {
							ChattingController.toChatting(pc, "[서버알림] 저장되어있는 경험치를 획득하였습니다 .1", Lineage.CHATTING_MODE_MESSAGE);
							toDeleteDB();
							pc.setExp(pc.getExp() + saveExp);
							pc.getInventory().count(this, getCount() - 1, true);
						} else {
							// 경험치 저장하기
							Exp exp_bean = ExpDatabase.find(pc.getLevel());
							double exp = 0;
							if (exp_bean != null) {
								double e = cha.getExp() - (exp_bean.getBonus() - exp_bean.getExp());
								exp = (e / exp_bean.getExp()) * 100;
								if (exp < 50) {
									ChattingController.toChatting(cha, "[서버알림] 경험치 50% 이상이 되어야 사용가능합니다 .", Lineage.CHATTING_MODE_MESSAGE);
									return;
								} else {

									// needExp
									toUpdate(pc);
									pc.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), this));
								}
							}
						}
					}
					// pc.getInventory().count(this, getCount() - 1, true);
				}
			} else {
				ChattingController.toChatting(cha, "[서버알림] 현재 레벨에선 사용이 불가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 아이템 사용시 구슬테이블에서 정보삭제
	 */
	public void toDeleteDB() {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("DELETE FROM characters_exp_marble WHERE itemobjid=?");
			st.setLong(1, getObjectId());
			st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(con, st);
		}

	}

	/**
	 * 구슬에 exp 저장시키기
	 */
	public void toUpdate(Character cha) {
		if (cha instanceof PcInstance) {
			PcInstance pc = (PcInstance) cha;
			Exp getExp = ExpDatabase.find(pc.getLevel());
			double needExp = getExp.getExp() * (50 * 0.01);
			ChattingController.toChatting(cha, "[서버알림] 경험치 50% 가 저장되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
			double total_exp = cha.getExp() - needExp;
			saveExp = (long) needExp;
			saveLevel = pc.getLevel();
			pc.setExp(total_exp);
			/**
			 * 디비에 구슬 정보 입력하기...
			 */
			Connection con = null;
			PreparedStatement st = null;
			try {
				con = DatabaseConnection.getLineage();
				st = con.prepareStatement("INSERT INTO characters_exp_marble SET itemobjid=?, save_exp=?, save_level=?");
				st.setLong(1, getObjectId());
				st.setLong(2, saveExp);
				st.setInt(3, saveLevel);
				st.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DatabaseConnection.close(con, st);
			}
		}
	}

	@Override
	public void toWorldJoin(Connection con, PcInstance pc) {
		super.toWorldJoin(con, pc);

		// 디비에서 정보 추출.
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM characters_exp_marble WHERE itemobjid=?");
			st.setLong(1, getObjectId());
			rs = st.executeQuery();
			if (rs.next()) {
				saveExp = rs.getLong("save_exp");
				saveLevel = rs.getInt("save_level");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(st, rs);
		}

		if (pc != null) {
			// 구슬정보 갱신
			if (Lineage.server_version <= 144) {
				pc.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));
				pc.toSender(S_InventoryCount.clone(BasePacketPooling.getPool(S_InventoryCount.class), this));
			} else if (Lineage.server_version <= 200) {
				pc.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), this));
			}
		}

	}

	public long getExp() {
		return saveExp;
	}

	public void setExp(long exp) {
		this.saveExp = exp;
	}

	public int getSaveLevel() {
		return saveLevel;
	}

	public void setSaveLevel(int saveLevel) {
		this.saveLevel = saveLevel;
	}
}
