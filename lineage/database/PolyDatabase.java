package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.bean.database.Poly;
import lineage.bean.database.Skill;
import lineage.bean.lineage.Inventory;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.controller.ChattingController;
import lineage.world.controller.RankController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.PcInstance;

public final class PolyDatabase {

	// weapon equip bit
	private static final int DAGGER_EQUIP = 1;
	private static final int SWORD_EQUIP = 2;
	private static final int TWOHANDSWORD_EQUIP = 4;
	private static final int AXE_EQUIP = 8;
	private static final int SPEAR_EQUIP = 16;
	private static final int STAFF_EQUIP = 32;
	private static final int EDORYU_EQUIP = 64;
	private static final int CLAW_EQUIP = 128;
	private static final int BOW_EQUIP = 256;
	private static final int KIRINGKU_EQUIP = 512;
	private static final int TWOHANDKIRINGKU_EQUIP = 512;
	private static final int CHAINSWORD_EQUIP = 1024;
	private static final Map<Integer, Integer> weaponFlg = new HashMap<Integer, Integer>();
	//
	static private List<Poly> list;

	static public void init(Connection con) {
		TimeLine.start("PolyDatabase..");

		//
		list = new ArrayList<Poly>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM poly");
			rs = st.executeQuery();
			while (rs.next()) {
				Poly p = new Poly();
				p.setUid(rs.getInt("id"));
				p.setName(rs.getString("name"));
				p.setPolyName(rs.getString("db"));
				p.setGfxId(rs.getInt("polyid"));
				p.setGfxMode(rs.getInt("polymode"));
				p.setMinLevel(rs.getInt("minlevel"));
				p.setWeapon(rs.getInt("isWeapon"));
				p.setHelm(rs.getInt("isHelm") == 1);
				p.setEarring(rs.getInt("isEarring") == 1);
				p.setNecklace(rs.getInt("isNecklace") == 1);
				p.setT(rs.getInt("isT") == 1);
				p.setArmor(rs.getInt("isArmor") == 1);
				p.setCloak(rs.getInt("isCloak") == 1);
				p.setRing(rs.getInt("isRing") == 1);
				p.setBelt(rs.getInt("isBelt") == 1);
				p.setGlove(rs.getInt("isGlove") == 1);
				p.setShield(rs.getInt("isShield") == 1);
				p.setBoots(rs.getInt("isBoots") == 1);
				p.setGarder(rs.getInt("isGarder") == 1);
				p.setSkill(rs.getInt("isSkill") == 1);
				p.setItem(rs.getInt("isItem") == 1);

				list.add(p);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", PolyDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		//
		weaponFlg.put(0x04, SWORD_EQUIP);
		weaponFlg.put(0x32, TWOHANDSWORD_EQUIP);
		weaponFlg.put(0x0B, AXE_EQUIP);
		weaponFlg.put(0x14, BOW_EQUIP);
		weaponFlg.put(0x18, SPEAR_EQUIP);
		weaponFlg.put(0x28, STAFF_EQUIP);
		weaponFlg.put(0x2E, DAGGER_EQUIP);
		weaponFlg.put(0x0B, AXE_EQUIP);
		weaponFlg.put(0x3A, CLAW_EQUIP);
		weaponFlg.put(0x36, EDORYU_EQUIP);
		weaponFlg.put(0x0B6A, BOW_EQUIP);
		weaponFlg.put(0x3E, BOW_EQUIP);
		weaponFlg.put(0x18, CHAINSWORD_EQUIP);
		weaponFlg.put(0x3a, KIRINGKU_EQUIP);

		TimeLine.end();
	}

	static public void close() {
		list.clear();
	}

	/**
	 * 고유 이름으로 추출하기.
	 * 
	 * @param name
	 * @return
	 */
	static public Poly getName(String name) {
		if (name == null || name.length() == 0)
			return null;

		for (Poly p : list) {
			if (p.getName().replaceAll(" ", "").equalsIgnoreCase(name))
				return p;
		}
		return null;
	}

	/**
	 * 변신 이름으로 추출하기.
	 */
	static public Poly getPolyName(String polyName) {
		if (polyName == null || polyName.length() == 0)
			return null;

		for (Poly p : list) {
			if (p.getPolyName().equalsIgnoreCase(polyName))
				return p;
		}
		return null;
	}

	static public Poly getPolyName2(String paramString) {
		if ((paramString == null) || (paramString.length() == 0))
			return null;
		for (Poly localPoly : list) {
			if (localPoly.getPolyName().replaceAll(" ", "").equalsIgnoreCase(paramString))
				return localPoly;
		}
		return null;
	}

	/**
	 * 필드 uid값으로 추출하기.
	 */
	static public Poly getPolyId(int uid) {
		for (Poly p : list) {
			if (p.getUid() == uid)
				return p;
		}
		return null;
	}

	/**
	 * gfx정보와 일치하는 클레스 찾아서 리턴.
	 */
	static public Poly getPolyGfx(int gfx) {
		for (Poly p : list) {
			if (p.getGfxId() == gfx)
				return p;
		}
		return null;
	}

	/**
	 * 변신에따른 아이템 해제처리 메서드.
	 */
	static public void toEquipped(Character cha, Poly p) {
		Inventory inv = cha.getInventory();
		if (inv != null) {
			for (int i = 0; i <= Lineage.SLOT_AMULET5; ++i) {
				ItemInstance slot = inv.getSlot(i);
				if (slot != null && slot.isEquipped()) {
					if (!toEquipped(slot, p)) {
						// 강제 착용 해제 처리.
						slot.setEquipped(false);
					/*	if (cha.isInvis() && cha instanceof PcInstance) {
							PcInstance pc = (PcInstance) cha;
							pc.setTimeInv(2);
							pc.setChkInv(true);
						} */

						slot.toSetoption(cha, true);
						slot.toEquipped(cha, inv, slot.getSlot());
						slot.toOption(cha, true);
						slot.toBuffCheck(cha);
					}
				}
			}
		}
	}

	/**
	 * polys를 참고해서 해당 아이템이 착용해도 되는지 판단하는 메서드.
	 */
	static private boolean toEquipped(ItemInstance item, Poly p) {
		if (item instanceof ItemWeaponInstance) {
			// 비트연산으로 착용가능한 무기 확인하기.
			// : 원하는 아이템에 비트값을 다 더한값으로 poly디비에 isWeapon칼럼에다가 세팅하면됨.
			switch (p.getWeapon()) {
			case 0:
				return false;
			case 1: // 칼종류
				return item.getItem().getGfxMode() == Lineage.WEAPON_SWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_DAGGER
						|| item.getItem().getGfxMode() == Lineage.WEAPON_SPEAR
						|| item.getItem().getGfxMode() == Lineage.WEAPON_TOHANDSWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_WAND
						|| item.getItem().getGfxMode() == Lineage.WEAPON_AXE;
			case 2: // 한손무기만
				return item.getItem().getGfxMode() == Lineage.WEAPON_SWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_DAGGER
						|| item.getItem().getGfxMode() == Lineage.WEAPON_AXE;
			case 3: // 양손무기만
				return item.getItem().getGfxMode() == Lineage.WEAPON_TOHANDSWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_BOW
						|| item.getItem().getGfxMode() == Lineage.WEAPON_SPEAR
						|| item.getItem().getGfxMode() == Lineage.WEAPON_WAND;
			case 4: // 활만
				return item.getItem().getGfxMode() == Lineage.WEAPON_BOW;
			case 5: // 창만
				return item.getItem().getGfxMode() == Lineage.WEAPON_SPEAR;
			case 6: // 지팡이만
				return item.getItem().getGfxMode() == Lineage.WEAPON_WAND;
			case 7: // 장거리공격 아이템을뺀 모든 무기
				return (p.getGfxId() != 13965 && p.getGfxId() != 6279
						&& item.getItem().getGfxMode() == Lineage.WEAPON_AXE)
						|| item.getItem().getGfxMode() == Lineage.WEAPON_WAND
						|| item.getItem().getGfxMode() == Lineage.WEAPON_SWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_DAGGER
						|| item.getItem().getGfxMode() == Lineage.WEAPON_TOHANDSWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_CLAW
						|| item.getItem().getGfxMode() == Lineage.WEAPON_EDORYU;
			case 8: // 이도류
				return item.getItem().getGfxMode() == Lineage.WEAPON_EDORYU;
			case 9: // 크로우
				return item.getItem().getGfxMode() == Lineage.WEAPON_CLAW;
			case 10: // 건들렛
				return item.getItem().getGfxMode() == Lineage.WEAPON_GAUNTLET;
			case 11: // 체인소드
				return item.getItem().getGfxMode() == Lineage.WEAPON_CHAINSWORD;
			case 13:
				return item.getItem().getGfxMode() == Lineage.WEAPON_SWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_AXE
						|| item.getItem().getGfxMode() == Lineage.WEAPON_WAND
						|| item.getItem().getGfxMode() == Lineage.WEAPON_DAGGER
						|| item.getItem().getGfxMode() == Lineage.WEAPON_TOHANDSWORD;
			case 14:
				return item.getItem().getGfxMode() == Lineage.WEAPON_SWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_AXE
						|| item.getItem().getGfxMode() == Lineage.WEAPON_WAND
						|| item.getItem().getGfxMode() == Lineage.WEAPON_DAGGER
						|| item.getItem().getGfxMode() == Lineage.WEAPON_TOHANDSWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_EDORYU;
			case 15:
				return item.getItem().getGfxMode() == Lineage.WEAPON_SWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_AXE
						|| item.getItem().getGfxMode() == Lineage.WEAPON_DAGGER
						|| item.getItem().getGfxMode() == Lineage.WEAPON_TOHANDSWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_EDORYU;
			case 16: // 장거리공격 아이템을뺀 모든 무기
				return item.getItem().getGfxMode() == Lineage.WEAPON_SWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_AXE
						|| item.getItem().getGfxMode() == Lineage.WEAPON_SPEAR
						|| item.getItem().getGfxMode() == Lineage.WEAPON_DAGGER
						|| item.getItem().getGfxMode() == Lineage.WEAPON_TOHANDSWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_EDORYU
						|| item.getItem().getGfxMode() == Lineage.WEAPON_CLAW;
			case 17:
				return item.getItem().getGfxMode() == Lineage.WEAPON_EDORYU
						|| item.getItem().getGfxMode() == Lineage.WEAPON_CLAW;
			case 18: // 장거리공격 아이템을뺀 모든 무기
				return item.getItem().getGfxMode() == Lineage.WEAPON_SWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_AXE
						|| item.getItem().getGfxMode() == Lineage.WEAPON_WAND
						|| item.getItem().getGfxMode() == Lineage.WEAPON_TOHANDSWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_EDORYU
						|| item.getItem().getGfxMode() == Lineage.WEAPON_CLAW;
			case 19: // 칼종류
				return item.getItem().getGfxMode() == Lineage.WEAPON_SWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_WAND;
			case 20:
				return item.getItem().getGfxMode() == Lineage.WEAPON_SWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_AXE
						|| item.getItem().getGfxMode() == Lineage.WEAPON_WAND
						|| item.getItem().getGfxMode() == Lineage.WEAPON_DAGGER
						|| item.getItem().getGfxMode() == Lineage.WEAPON_TOHANDSWORD;
			case 21:
				return item.getItem().getGfxMode() == Lineage.WEAPON_SWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_AXE
						|| item.getItem().getGfxMode() == Lineage.WEAPON_DAGGER
						|| item.getItem().getGfxMode() == Lineage.WEAPON_TOHANDSWORD;
			case 22:
				return item.getItem().getGfxMode() == Lineage.WEAPON_SWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_AXE
						|| item.getItem().getGfxMode() == Lineage.WEAPON_WAND
						|| item.getItem().getGfxMode() == Lineage.WEAPON_DAGGER;
			case 23:
				return item.getItem().getGfxMode() == Lineage.WEAPON_SWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_AXE
						|| item.getItem().getGfxMode() == Lineage.WEAPON_DAGGER
						|| item.getItem().getGfxMode() == Lineage.WEAPON_EDORYU
						|| item.getItem().getGfxMode() == Lineage.WEAPON_CLAW;
			case 24:
				return item.getItem().getGfxMode() == Lineage.WEAPON_SWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_DAGGER
						|| item.getItem().getGfxMode() == Lineage.WEAPON_EDORYU
						|| item.getItem().getGfxMode() == Lineage.WEAPON_CLAW;
			case 25:
				return item.getItem().getGfxMode() == Lineage.WEAPON_SWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_WAND
						|| item.getItem().getGfxMode() == Lineage.WEAPON_DAGGER;
			case 26: // 장거리공격 아이템을뺀 모든 무기
				return item.getItem().getGfxMode() == Lineage.WEAPON_SWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_AXE
						|| item.getItem().getGfxMode() == Lineage.WEAPON_DAGGER
						|| item.getItem().getGfxMode() == Lineage.WEAPON_TOHANDSWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_EDORYU
						|| item.getItem().getGfxMode() == Lineage.WEAPON_CLAW;
			case 27: // 장거리공격 아이템을뺀 모든 무기
				return (p.getGfxId() != 8800 && item.getItem().getGfxMode() == Lineage.WEAPON_AXE)
						|| item.getItem().getGfxMode() == Lineage.WEAPON_SPEAR
						|| item.getItem().getGfxMode() == Lineage.WEAPON_WAND
						|| item.getItem().getGfxMode() == Lineage.WEAPON_SWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_DAGGER
						|| item.getItem().getGfxMode() == Lineage.WEAPON_TOHANDSWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_CLAW
						|| item.getItem().getGfxMode() == Lineage.WEAPON_EDORYU;
			case 28: // 창 아이템을뺀 모든 무기
				return (item.getItem().getGfxMode() == Lineage.WEAPON_AXE)
						|| item.getItem().getGfxMode() == Lineage.WEAPON_WAND
						|| item.getItem().getGfxMode() == Lineage.WEAPON_SWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_DAGGER
						|| item.getItem().getGfxMode() == Lineage.WEAPON_TOHANDSWORD
						|| item.getItem().getGfxMode() == Lineage.WEAPON_BOW;
			case 12: // 모든 종류에 무기 착용.
				return true;
			}
		} else {
			// 방어구 확인
			if (item.getItem().getSlot() == 2)
				return p.isArmor();
			if (item.getItem().getSlot() == 4)
				return p.isCloak();
			if (item.getItem().getSlot() == 6)
				return p.isGlove();
			if (item.getItem().getSlot() == 1)
				return p.isHelm();
			if (item.getItem().getSlot() == 18)
				return p.isRing();
			if (item.getItem().getSlot() == 3)
				return p.isT();
			if (item.getItem().getSlot() == 7)
				return p.isShield() || p.isGarder();
			if (item.getItem().getSlot() == 5)
				return p.isBoots();
			if (item.getItem().getSlot() == 11)
				return p.isBelt();
			if (item.getItem().getSlot() == 12)
				return p.isEarring();
			if (item.getItem().getSlot() == 10)
				return p.isNecklace();
		}
		return true;
	}

	/**
	 * 아이템을 착용할때 호출해서 해당 아이템을 착용해도 되는지 확인하는 메서드. gfx에 따라 poly객체를 추출. 확인해봄.
	 */
	static public boolean toEquipped(Character cha, ItemInstance item) {
		for (Poly p : list) {
			if (p.getGfxId() == cha.getGfx())
				return toEquipped(item, p);
		}
		return true;
	}

	static public int getSize() {
		return list.size();
	}

	/**
	 * 랭커변신을 위한 함수
	 * @param
	 * @return
	 * 2017-09-04
	 * by all_night.
	 */
	static public String toRankPolyMorph(Character cha, String polyName){		
		int allRank = RankController.getAllRank(cha.getObjectId());
		int classRank = RankController.getClassRank(cha.getObjectId(), cha.getClassType());
		
		// 전체랭킹 20위 또는 클래스랭킹 3위는 랭커변신
		if ((((allRank > 0 && allRank <= Lineage.rank_poly_all) || 
				(classRank > 0 && classRank <= Lineage.rank_poly_class)) && cha.getLevel() >= Lineage.rank_min_level2) || 
				Lineage.event_rank_poly || cha.getGm() > 0) {
			switch (cha.getClassType()) {
			case 0:
				if (cha.getClassSex() == 0)
					polyName = "왕자 랭커";
				else
					polyName = "공주 랭커";
				break;
			case 1:
				if (cha.getClassSex() == 0)
					polyName = "남자기사 랭커";
				else
					polyName = "여자기사 랭커";
				break;
			case 2:
				if (cha.getClassSex() == 0)
					polyName = "남자요정 랭커";
				else
					polyName = "여자요정 랭커";
				break;
			case 3:
				if (cha.getClassSex() == 0)
					polyName = "남자법사 랭커";
				else
					polyName = "여자법사 랭커";
				break;
			}
		} else {
			ChattingController.toChatting(cha, String.format("%d레벨 이상 전체 랭킹 [%d위]이내 또는 클래스 랭킹 [%d위]이내 변신 가능",
					Lineage.rank_min_level2, Lineage.rank_poly_all, Lineage.rank_poly_class), Lineage.CHATTING_MODE_MESSAGE);
		}

		return polyName;
	}

}
