package lineage.world.object.instance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage.bean.database.FishList;
import lineage.bean.database.Item;
import lineage.database.DatabaseConnection;
import lineage.database.FishItemListDatabase;
import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.CharacterController;
import lineage.world.controller.FishingController;

public class FishermanInstance extends BackgroundInstance {
	private long pc_objectId;
	private int pc_accountUid;
	private String pc_name;
	private int fishTime;
	private long coin;
	private long rice;;

	public FishermanInstance(long objId, int accountUid, String name, int gfx, int gfxMode, int fishTime, long coin,
			int x, int y, int map, int heading, long rice) {
		pc_objectId = objId;
		pc_accountUid = accountUid;
		pc_name = name;
		this.fishTime = fishTime;
		this.coin = coin;
		this.rice = rice;

		setObjectId(ServerDatabase.nextEtcObjId());
		setName(pc_name + "의 자동낚시");
		setClanId(0);
		setClanName("");
		setTitle("");
		setLawful(66536);
		setGfx(gfx);
		setGfxMode(gfxMode);
		setX(x);
		setY(y);
		setMap(map);
		setHeading(heading);

		CharacterController.toWorldJoin(this);

		toTeleport(getX(), getY(), getMap(), false);
		FishingController.auto_fish_list.put(pc_accountUid, this);
	}

	@Override
	public void close() {
		BackgroundInstance effect = FishingController.auto_fish_effect_list.get(pc_accountUid);

		FishingController.auto_fish_effect_list.remove(pc_accountUid);

		if (effect != null) {
			effect.clearList(true);
			World.remove(effect);
		}

		FishingController.auto_fish_list.remove(pc_accountUid);

		clearList(true);
		World.remove(this);

		pc_objectId = coin = 0L;
		pc_accountUid = fishTime = 0;
		pc_name = null;

		super.close();

		CharacterController.toWorldOut(this);
	}

	@Override
	public void toTimer(long time) {
		if (this != null) {
			BackgroundInstance effect = FishingController.auto_fish_effect_list.get(pc_accountUid);

			if (effect != null)
				effect.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), effect, 366),
						true);

			if (--fishTime <= 0 && coin >= Lineage.auto_fish_expense && rice > 0) {
				coin -= Lineage.auto_fish_expense;
				rice -= 1;
				fishTime = Lineage.fish_delay;

				Connection con = null;
				PreparedStatement st = null;
				ResultSet rs = null;

				try {
					con = DatabaseConnection.getLineage();
					updateExp(con, st, rs);
					updateItem(con, st, rs);
					updateCoin(con, st, rs, rice);
				} catch (Exception e) {
					lineage.share.System.printf("%s : updateError(long count)\r\n", FishermanInstance.class.toString());
					lineage.share.System.println(e);
				} finally {
					DatabaseConnection.close(con, st, rs);
				}

				if (coin < Lineage.auto_fish_expense || rice < 1)
					close();
			}
		}
	}

	public long getPc_objectId() {
		return pc_objectId;
	}

	public void setPc_objectId(long pc_objectId) {
		this.pc_objectId = pc_objectId;
	}

	public int getPc_accountUid() {
		return pc_accountUid;
	}

	public void setPc_accountUid(int pc_accountUid) {
		this.pc_accountUid = pc_accountUid;
	}

	public String getPc_name() {
		return pc_name;
	}

	public void setPc_name(String pc_name) {
		this.pc_name = pc_name;
	}

	public int getFishTime() {
		return fishTime;
	}

	public long getCoin() {
		return coin;
	}

	public long getRice() {
		return rice;
	}

	public void setRice(long rice) {
		this.rice = rice;
	}

	/**
	 * 군터의 인장 감소 함수
	 * 
	 * @param
	 * @return 2017-09-07 by all_night.
	 */
	private void updateCoin(Connection con, PreparedStatement st, ResultSet rs, long rice) {
		long coinCount = 0;

		try {
			st = con.prepareStatement("SELECT * FROM characters_inventory WHERE cha_objId=? AND name=?");
			st.setLong(1, pc_objectId);
			st.setString(2, Lineage.auto_fish_coin);
			rs = st.executeQuery();

			if (rs.next())
				coinCount = rs.getLong("count");

			coinCount -= Lineage.auto_fish_expense;

			// 군터의 인장 삭제
			if (coinCount <= 0) {
				st.close();
				st = con.prepareStatement("DELETE FROM characters_inventory WHERE cha_objId=? AND name=?");
				st.setLong(1, pc_objectId);
				st.setString(2, Lineage.auto_fish_coin);
				st.executeUpdate();
			} else {
				st.close();
				st = con.prepareStatement("UPDATE characters_inventory SET count=? WHERE cha_objId=? AND name=?");
				st.setLong(1, coinCount);
				st.setLong(2, pc_objectId);
				st.setString(3, Lineage.auto_fish_coin);
				st.executeUpdate();
			}

			st.close();

			// 영양 미끼 삭제
			if (rice <= 0) {
				st = con.prepareStatement("DELETE FROM characters_inventory WHERE cha_objId=? AND name=?");
				st.setLong(1, pc_objectId);
				st.setString(2, Lineage.fish_rice);
				st.executeUpdate();
			} else {
				st = con.prepareStatement("UPDATE characters_inventory SET count=? WHERE cha_objId=? AND name=?");
				st.setLong(1, rice);
				st.setLong(2, pc_objectId);
				st.setString(3, Lineage.fish_rice);
				st.executeUpdate();
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : updateCoin(long count)\r\n", FishermanInstance.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
	}

	/**
	 * 경험치 지급단 추가 함수
	 * 
	 * @param
	 * @return 2017-09-07 by all_night.
	 */
	private void updateExp(Connection con, PreparedStatement st, ResultSet rs) {
		Item i = ItemDatabase.find(Lineage.fish_exp);

		if (i != null) {
			try {
				st = con.prepareStatement("SELECT * FROM characters_inventory WHERE cha_objId=? AND name=?");
				st.setLong(1, pc_objectId);
				st.setString(2, Lineage.fish_exp);
				rs = st.executeQuery();
				// 아이템이 겹칠 경우
				if (i.isPiles() && rs.next()) {
					long count = rs.getLong("count");

					st.close();
					st = con.prepareStatement("UPDATE characters_inventory SET count=? WHERE cha_objId=? AND name=?");
					st.setLong(1, count + 1);
					st.setLong(2, pc_objectId);
					st.setString(3, Lineage.fish_exp);
					st.executeUpdate();
				} else {
					st.close();
					st = con.prepareStatement(
							"INSERT INTO characters_inventory SET objId=?, cha_objId=?, cha_name=?, name=?, count=1, en=0, definite=1, bress=1, 구분1=?, 구분2=?");
					st.setLong(1, ServerDatabase.nextItemObjId());
					st.setLong(2, pc_objectId);
					st.setString(3, pc_name);
					st.setString(4, i.getName());
					st.setString(5, i.getType1());
					st.setString(6, i.getType2());
					st.executeUpdate();
				}
			} catch (Exception e) {
				lineage.share.System.printf("%s : updateExp()\r\n", FishermanInstance.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(st, rs);
			}
		}
	}

	/**
	 * 아이템 추가 함수
	 * 
	 * @param
	 * @return 2017-09-07 by all_night.
	 */
	private void updateItem(Connection con, PreparedStatement st, ResultSet rs) {
		if (FishItemListDatabase.getFishList().size() > 0) {
			// fishing_item_list 테이블의 목록중 랜덤으로 하나 추출
			FishList fishList = FishItemListDatabase.getFishList()
					.get(Util.random(0, FishItemListDatabase.getFishList().size() - 1));

			if (fishList != null) {
				Item i = ItemDatabase.find(fishList.getItemName());

				if (i != null) {
					try {
						long itemCount = Util.random(fishList.getItemCountMin(), fishList.getItemCountMax());

						st = con.prepareStatement(
								"SELECT * FROM characters_inventory WHERE cha_objId=? AND name=? AND bress=?");
						st.setLong(1, pc_objectId);
						st.setString(2, fishList.getItemName());
						st.setInt(3, fishList.getItemBress());
						rs = st.executeQuery();

						// 아이템이 겹칠 경우
						if (i.isPiles() && rs.next()) {
							long count = rs.getLong("count");

							st.close();
							st = con.prepareStatement(
									"UPDATE characters_inventory SET count=? WHERE cha_objId=? AND name=? AND bress=?");
							st.setLong(1, count + itemCount);
							st.setLong(2, pc_objectId);
							st.setString(3, fishList.getItemName());
							st.setInt(4, fishList.getItemBress());
							st.executeUpdate();
						} else {
							st.close();
							st = con.prepareStatement(
									"INSERT INTO characters_inventory SET objId=?, cha_objId=?, cha_name=?, name=?, count=?, en=?, definite=?, bress=?, 구분1=?, 구분2=?");
							st.setLong(1, ServerDatabase.nextItemObjId());
							st.setLong(2, pc_objectId);
							st.setString(3, pc_name);
							st.setString(4, i.getName());
							st.setLong(5, itemCount);
							st.setInt(6, fishList.getItemEnchant());
							st.setInt(7, 1);
							st.setInt(8, fishList.getItemBress());
							st.setString(9, i.getType1());
							st.setString(10, i.getType2());
							st.executeUpdate();
						}
					} catch (Exception e) {
						lineage.share.System.printf("%s : updateItem()\r\n", FishermanInstance.class.toString());
						lineage.share.System.println(e);
					} finally {
						DatabaseConnection.close(st, rs);
					}
				}
			}
		}
	}

}
