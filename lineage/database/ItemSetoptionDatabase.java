package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.ItemSetoption;
import lineage.bean.database.Poly;
import lineage.bean.lineage.BuffInterface;
import lineage.bean.lineage.Inventory;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterSpMr;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.network.packet.server.S_ObjectHitratio;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.controller.BuffController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.magic.BuffRing;
import lineage.world.object.magic.Haste;
import lineage.world.object.magic.HolyWalk;
import lineage.world.object.magic.ShapeChange;
import lineage.world.object.magic.item.Bravery;
import lineage.world.object.magic.item.Wafer;

public final class ItemSetoptionDatabase {

	static private List<ItemSetoption> list;

	static public void init(Connection con) {
		TimeLine.start("ItemSetopionDatabase..");

		list = new ArrayList<ItemSetoption>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM item_setoption");
			rs = st.executeQuery();
			while (rs.next()) {
				ItemSetoption i = new ItemSetoption();
				i.setUid(rs.getInt("uid"));
				i.setName(rs.getString("name"));
					i.setCount(rs.getInt("count"));
					i.setAdd_hp(rs.getInt("add_hp"));
					i.setAdd_mp(rs.getInt("add_mp"));
					i.setAdd_str(rs.getInt("add_str"));
					i.setAdd_dex(rs.getInt("add_dex"));
					i.setAdd_con(rs.getInt("add_con"));
					i.setAdd_int(rs.getInt("add_int"));
					i.setAdd_wis(rs.getInt("add_wis"));
					i.setAdd_cha(rs.getInt("add_cha"));
					i.setAdd_ac(rs.getInt("add_ac"));
					i.setAdd_mr(rs.getInt("add_mr"));
					i.setAdd_sp(rs.getInt("add_sp"));
					i.setAddRed(rs.getInt("add_reduction"));
					i.setAdd_dmg(rs.getInt("add_dmg"));
					i.setAdd_bow_dmg(rs.getInt("add_bow_dmg"));
					i.setAdd_exp(Double.valueOf(rs.getString("add_exp")));
					i.setTic_hp(rs.getInt("tic_hp"));
					i.setTic_mp(rs.getInt("tic_mp"));
					i.setPolymorph(rs.getInt("polymorph"));
					i.setWindress(rs.getInt("windress"));
					i.setWateress(rs.getInt("wateress"));
					i.setFireress(rs.getInt("fireress"));
					i.setEarthress(rs.getInt("earthress"));
					i.setGm(rs.getInt("gm"));
					i.setHaste(rs.getString("haste").equalsIgnoreCase("true"));
					i.setBrave(rs.getString("brave").equalsIgnoreCase("true"));
					i.setWafer(rs.getString("wafer").equalsIgnoreCase("true"));

					list.add(i);
				}
			} catch (Exception e) {
				lineage.share.System.printf("%s : init(Connection con)\r\n", ItemSetoptionDatabase.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(st, rs);
			}

			TimeLine.end();
		}

	static public ItemSetoption find(int uid) {
		synchronized (list) {
			for (ItemSetoption is : list) {
				if (is.getUid() == uid)
					return is;
			}
			return null;
		}
	}
	
	static public ItemSetoption find(String nameid) {
		synchronized (list) {
			for (ItemSetoption is : list) {
				if (is.getNameIdList().contains(nameid))
					return is;
			}
			return null;
		}
	}
	
	static public List<ItemSetoption> findList(String nameid) {
		List<ItemSetoption> temp = new ArrayList<ItemSetoption>();
		synchronized (list) {
			for (ItemSetoption is : list) {
				if (is.getNameIdList().contains(nameid))
					temp.add(is);
			}
			return temp;
		}
	}

	static public synchronized void setting(Character cha, ItemSetoption i, boolean equipped, boolean sendPacket) {
		Inventory inv = cha.getInventory();
		if (inv != null) {
			// 초기화
			int Hp = 0;
			int Mp = 0;
			int Str = 0;
			int Dex = 0;
			int Con = 0;
			int Int = 0;
			int Wis = 0;
			int Cha = 0;
			int Ac = 0;
			int Mr = 0;
			int Sp = 0;
			int Red=0;
			int Dmg = 0;
			int BowDmg = 0;
			double exp = 0;
			int Tichp = 0;
			int Ticmp = 0;
			int windress = 0;
			int wateress = 0;
			int fireress = 0;
			int earthress = 0;
			int Gm = 0;
			// 정리
			for (ItemSetoption is : inv.getSetitemList()) {
				Hp += is.getAdd_hp();
				Mp += is.getAdd_mp();
				Str += is.getAdd_str();
				Dex += is.getAdd_dex();
				Con += is.getAdd_con();
				Int += is.getAdd_int();
				Wis += is.getAdd_wis();
				Cha += is.getAdd_cha();
				Ac += is.getAdd_ac();
				Mr += is.getAdd_mr();
				Sp += is.getAdd_sp();
				Red += is.getAddRed();
				Dmg += is.getAdd_dmg();
				BowDmg += is.getAdd_bow_dmg();
				exp += is.getAdd_exp();
				Tichp += is.getTic_hp();
				Ticmp += is.getTic_mp();
				windress += is.getWindress();
				wateress += is.getWateress();
				fireress += is.getFireress();
				earthress += is.getEarthress();
				if (is.getGm() >= Gm)
					Gm = is.getGm();

			
			}
			// 적용
			cha.setSetitemHp(Hp);
			cha.setSetitemMp(Mp);
			cha.setSetitemStr(Str);
			cha.setSetitemDex(Dex);
			cha.setSetitemCon(Con);
			cha.setSetitemInt(Int);
			cha.setSetitemWis(Wis);
			cha.setSetitemCha(Cha);
			cha.setSetitemAc(Ac);
			cha.setSetitemMr(Mr);
			cha.setSetitemSp(Sp);
			cha.setSetitemRed( Red );
			cha.setSetitemDmg(Dmg);
			cha.setSetitemBowDmg(BowDmg);
			cha.setSetitemExp(exp);
			cha.setSetitemTicHp(Tichp);
			cha.setSetitemTicMp(Ticmp);
			cha.setSetitemWindress(windress);
			cha.setSetitemWaterress(wateress);
			cha.setSetitemFireress(fireress);
			cha.setSetitemEarthress(earthress);
			cha.setGm(Gm);
			if (equipped && i.getPolymorph() > 0) {
				ShapeChange.onBuff(cha, cha, PolyDatabase.getPolyGfx(i.getPolymorph()), -1, false, sendPacket);
	            if (!cha.getFast_poly().equalsIgnoreCase("girtas") || !cha.getFast_poly().equalsIgnoreCase("Platinum death knight")
			    		|| !cha.getFast_poly().equalsIgnoreCase("Dragon Slayer") || !cha.getFast_poly().equalsIgnoreCase("Doppelganger")
			    		|| !cha.getFast_poly().equalsIgnoreCase("zillian of moon") || !cha.getFast_poly().equalsIgnoreCase("Flame death knight")
			    		|| !cha.getFast_poly().equalsIgnoreCase("Platinum Baphomet")) {

					BuffController.remove(cha, BuffRing.class);
					}
				cha.isSetPoly = true;
			} else if (!equipped && i.getPolymorph() > 0) {
				BuffController.remove(cha, ShapeChange.class);
				cha.isSetPoly = false;
			}
	
		if (equipped && i.isHaste())
			Haste.init(cha, -1);
		else if (!equipped && i.isHaste())
			BuffController.remove(cha, Haste.class);
		
		if (equipped && i.isBrave()) {
			Bravery.init(cha, -1);
			Wafer.init(cha, -1);
			HolyWalk.init(cha, -1);
		} else if (!equipped && i.isBrave()) {
			BuffController.remove(cha, Bravery.class);
			BuffController.remove(cha, Wafer.class);
			BuffController.remove(cha, HolyWalk.class);
		}

			if (sendPacket) {
				cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
				cha.toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), cha));
			}
		}
	}

	static public int getSize() {
		return list.size();
	}
}
