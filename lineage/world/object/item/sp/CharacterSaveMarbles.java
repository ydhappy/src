package lineage.world.object.item.sp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage.database.AccountDatabase;
import lineage.database.CharactersDatabase;
import lineage.database.DatabaseConnection;
import lineage.database.WarehouseDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Disconnect;
import lineage.network.packet.server.S_InventoryCount;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.network.packet.server.S_MessageYesNo;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class CharacterSaveMarbles extends ItemInstance {

	public final int ACCOUNT_ADD_VALUE = 10000000; // account_uid에 증가시킬수
	public final String ACCOUNT_ADD_NAME = "[CHANGE]";
	private String accountName;
	private int accountUid;
	private String characterName;
	private int characterLevel;
	private int characterClassType;
	private double characterExp;

	@Override
	public void close() {
		super.close();
		accountName = null;
		accountUid = characterLevel = characterClassType = 0;
		characterExp = 0;
		characterName = null;
	}
	
	private CharacterSaveMarbles() {
		accountName = null;
		accountUid = characterLevel = characterClassType = 0;
		characterExp = 0;
		characterName = null;
	}
	

	public String getAccountName() {
		return accountName;
	}

	public int getAccountUid() {
		return accountUid;
	}

	public String getCharacterName() {
		return characterName;
	}

	public int getCharacterLevel() {
		return characterLevel;
	}

	public int getCharacterClassType() {
		return characterClassType;
	}

	public double getCharacterExp() {
		return characterExp;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setAccountUid(int accountUid) {
		this.accountUid = accountUid;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public void setCharacterLevel(int characterLevel) {
		this.characterLevel = characterLevel;
	}

	public void setCharacterClassType(int characterClassType) {
		this.characterClassType = characterClassType;
	}

	public void setCharacterExp(double characterExp) {
		this.characterExp = characterExp;
	}

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new CharacterSaveMarbles();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {


		if (Lineage.server_version > 270) {
			if (cha instanceof PcInstance) {
				PcInstance pc = (PcInstance) cha;

				/**
				 * 저장된 캐릭터가 있다면 현재의 계정으로 이전
				 */
				if (accountUid != 0) {
					pc.setCharacterMarblesUse(true);
					pc.setMarblesObjectId(getObjectId());
					pc.toSender(S_MessageYesNo.clone(BasePacketPooling.getPool(S_MessageYesNo.class), 622, "저장된 캐릭터정보를 현재 계정으로 등록 하시겠습니까?"));
				} else {
					pc.setSaveCharacter(true);
					pc.setMarblesObjectId(getObjectId());
					pc.toSender(S_MessageYesNo.clone(BasePacketPooling.getPool(S_MessageYesNo.class), 622, "정말로 등록 하시겠습니까?"));
				}
			}
		} else {
			try {
				if (cha instanceof PcInstance) {
					PcInstance pc = (PcInstance) cha;

					/**
					 * 저장된 캐릭터가 있다면 현재의 계정으로 이전
					 */
					//같은계정에 등록가능한지
					if (accountUid != 0) {
						/*if (pc.getClient().getAccountId().equalsIgnoreCase(accountName)) {
							ChattingController.toChatting(cha, "해당 캐릭터를 저장한 계정에서는 재등록 할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
							return;
						}*/

						Connection con = null;
						try {
							con = DatabaseConnection.getLineage();
							if (AccountDatabase.getCharacterLength(con, pc.getClient().getAccountUid()) >= 6) {
								ChattingController.toChatting(cha, "계정에 비어있는 캐릭터 슬롯이 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
								return;
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							DatabaseConnection.close(con);
						}

						toDeleteDB();
						String change_account_name = pc.getClient().getAccountId();
						int change_account_uid = pc.getClient().getAccountUid();
						CharactersDatabase.getChangeAccountName(characterName, change_account_name, change_account_uid);

						pc.getInventory().count(this, getCount() - 1, true);
						ChattingController.toChatting(cha, "이전이 완료 되었습니다. 클라이언트가 강제종료 됩니다.", Lineage.CHATTING_MODE_MESSAGE);
						Thread.sleep(2000);
						pc.toSender(S_Disconnect.clone(BasePacketPooling.getPool(S_Disconnect.class), 0x0A));
					} else {
						if (!isCharacterMarblesMake(cha)) {
							ChattingController.toChatting(cha, "이미 저장된 캐릭터 입니다.", Lineage.CHATTING_MODE_MESSAGE);
							System.out.println("이미 캐릭터저장구슬에 등록된 캐릭터가 동일한 아이템을 사용하려합니다. 확인바랍니다. [" + cha.getName() + "]");
							return;
						}

						if (World.findPersnalShop(pc.getName()) != null) {
							ChattingController.toChatting(pc, "무인상점을 종료 후 사용해 주십시오.", Lineage.CHATTING_MODE_MESSAGE);
							return;
						}

						if (pc.getClanId() != 0) {
							ChattingController.toChatting(pc, "혈맹 탈퇴 후 사용해 주십시오.", Lineage.CHATTING_MODE_MESSAGE);
							return;
						}
							if (AccountDatabase.getTradeing(pc)) {
						ChattingController.toChatting(cha, "계정에 아데나거래 등록/진행중인 물품이 있습니다.");
						return;
						}

						/**
						 * 저장된 캐릭터가 없이 빈 아이템이라면 캐릭터 저장
						 */
						toUpdate(pc);
						ChattingController.toChatting(pc, "캐릭터 저장이 완료 되었습니다. 창고로 저장 되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
						ChattingController.toChatting(pc, "잠시 후 클라이언트가 강제종료 됩니다.", Lineage.CHATTING_MODE_MESSAGE);

						WarehouseDatabase.insert(this, getObjectId(), getCount(), accountUid, Lineage.DWARF_TYPE_NONE);

						/**
						 * 캐릭터 종료시키고 해당 계정에 더이상 출력안되게 하기 아이템 창고로 이동시키기
						 */
						String change_account_name = ACCOUNT_ADD_NAME + accountName;
						int change_account_uid = accountUid + ACCOUNT_ADD_VALUE;
						CharactersDatabase.getChangeAccountName(pc.getName(), change_account_name, change_account_uid);

						pc.getInventory().count(this, getCount() - 1, true);

						pc.toWorldOut();
						Thread.sleep(2000);
						pc.toSender(S_Disconnect.clone(BasePacketPooling.getPool(S_Disconnect.class), 0x0A));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 구슬에 캐릭터 저장시키기
	 */
	public void toUpdate(Character cha) {
		if (cha instanceof PcInstance) {
			PcInstance pc = (PcInstance) cha;
			this.accountName = pc.getClient().getAccountId();
			this.accountUid = pc.getClient().getAccountUid();
			this.characterName = pc.getName();
			this.characterLevel = pc.getLevel();
			this.characterClassType = pc.getClassType();
			this.characterExp = pc.getExp();

			/**
			 * 디비에 구슬 정보 입력하기...
			 */
			Connection con = null;
			PreparedStatement st = null;
			try {
				con = DatabaseConnection.getLineage();
				st = con.prepareStatement(
						"INSERT INTO characters_save_marble SET itemobjid=?,account_name=?,account_uid=?, character_name=?, character_level=?, character_class=?, character_exp=?");
				st.setLong(1, getObjectId());
				st.setString(2, accountName);
				st.setInt(3, accountUid);
				st.setString(4, characterName);
				st.setInt(5, characterLevel);
				st.setInt(6, characterClassType);
				st.setDouble(7, characterExp);
				st.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DatabaseConnection.close(con, st);
			}
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
			st = con.prepareStatement("DELETE FROM characters_save_marble WHERE character_name=?");
			st.setString(1, characterName);
			st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(con, st);
		}

	}

	/**
	 * 캐릭터 저장구슬을 만들 수 있는 캐릭터 인지 검사.
	 * 
	 * @param cha
	 * @return
	 */
	public boolean isCharacterMarblesMake(Character cha) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM characters_save_marble WHERE character_name=?");
			st.setString(1, cha.getName());
			rs = st.executeQuery();
			if (rs.next()) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(st, rs);
		}

		return true;
	}

	@Override
	public void toWorldJoin(Connection con, PcInstance pc) {
		super.toWorldJoin(con, pc);

		// 디비에서 정보 추출.
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM characters_save_marble WHERE itemobjid=?");
			st.setLong(1, getObjectId());
			rs = st.executeQuery();
			if (rs.next()) {
				this.accountName = rs.getString("account_name");
				this.accountUid = rs.getInt("account_uid");
				this.characterName = rs.getString("character_name");
				this.characterLevel = rs.getInt("character_level");
				this.characterClassType = rs.getInt("character_class");
				this.characterExp = rs.getDouble("character_exp");
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
}
