package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import cholong.item.StormWalkItem;
import jsn_soft.AddHunt;
import lineage.bean.database.Item;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemBookInstance;
import lineage.world.object.instance.ItemCrystalInstance;
import lineage.world.object.instance.ItemDarkSpiritCrystalInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.item.Aden;
import lineage.world.object.item.Arrow;
import lineage.world.object.item.BlessEva;
import lineage.world.object.item.CaliphPouch;
import lineage.world.object.item.Candle;
import lineage.world.object.item.bundle.ChanceBundle;
import lineage.world.object.item.BuffMaan;
import lineage.world.object.item.CookBook;
import lineage.world.object.item.CreateWand;
import lineage.world.object.item.DogCollar;
import lineage.world.object.item.ElementalStone;
import lineage.world.object.item.Firework;
import lineage.world.object.item.FishingPole;
import lineage.world.object.item.InnRoomKey;
import lineage.world.object.item.ItemSwap;
import lineage.world.object.item.Lamp;
import lineage.world.object.item.Lantern;
import lineage.world.object.item.LanternOil;
import lineage.world.object.item.Letter;
import lineage.world.object.item.MagicDoll;
import lineage.world.object.item.MagicFirewood;
import lineage.world.object.item.MagicFlute;
import lineage.world.object.item.Meat;
import lineage.world.object.item.MetieceClanJoinScroll;
import lineage.world.object.item.MiniMap;
import lineage.world.object.item.MonsterEyeMeat;
import lineage.world.object.item.PK_clean;
import lineage.world.object.item.PetWhistle;
import lineage.world.object.item.PvP_clean;
import lineage.world.object.item.RaceTicket;
import lineage.world.object.item.RandomEnchantBox;
import lineage.world.object.item.RedSock;
import lineage.world.object.item.Solvent;
//import lineage.world.object.item.TreasureBox;
import lineage.world.object.item.NPC제거막대;
import lineage.world.object.item.monsterClean;
import lineage.world.object.item.아이템드랍확인막대;
import lineage.world.object.item.인첸트복구주문서;
import lineage.world.object.item.Whetstone;
import lineage.world.object.item.자동칼질;
import lineage.world.object.item.회상의촛불;
import lineage.world.object.item.던전북;
import lineage.world.object.item.Exp_support2;
import lineage.world.object.item.AutoPotion;
import lineage.world.object.item.SPITEM.화염의막대;
import lineage.world.object.item.all_night.LifeLost;
import lineage.world.object.item.all_night.ScrollOfGiranDungeon;
import lineage.world.object.item.all_night.Sword_lack;
import lineage.world.object.item.all_night.균열의핵;
import lineage.world.object.item.all_night.셀프시전마법;
import lineage.world.object.item.all_night.신규인형상자;
import lineage.world.object.item.all_night.테베티칼의보물상자조각;
import lineage.world.object.item.all_night.환상지배부적;
import lineage.world.object.item.armor.AntharasArmor;
import lineage.world.object.item.armor.ArmorOfIllusion;
import lineage.world.object.item.armor.FafurionArmor;
import lineage.world.object.item.armor.LindviorArmor;
import lineage.world.object.item.armor.RichLobe;
import lineage.world.object.item.armor.ValakasArmor;
import lineage.world.object.item.armor.용의호박갑옷;
import lineage.world.object.item.armor.창천갑옷;
import lineage.world.object.item.bow.BowOfIllusion;
import lineage.world.object.item.bundle.AlchemistStone;
import lineage.world.object.item.bundle.AllBundleCoin;
import lineage.world.object.item.bundle.Bundle;
import lineage.world.object.item.bundle.MagBundle;
import lineage.world.object.item.bundle.MagicBundle;
import lineage.world.object.item.bundle.UnicBundle;
import lineage.world.object.item.bundle.룸티스의보랏빛귀걸이상자;
import lineage.world.object.item.bundle.룸티스의붉은빛귀걸이상자;
import lineage.world.object.item.bundle.룸티스의푸른빛귀걸이상자;
import lineage.world.object.item.bundle.무한의화살통;
import lineage.world.object.item.bundle.붉은빛나는물고기;
import lineage.world.object.item.bundle.스냅퍼의마나반지상자;
import lineage.world.object.item.bundle.스냅퍼의마법저항반지상자;
import lineage.world.object.item.bundle.스냅퍼의용사반지상자;
import lineage.world.object.item.bundle.스냅퍼의지혜반지상자;
import lineage.world.object.item.bundle.스냅퍼의집중반지상자;
import lineage.world.object.item.bundle.스냅퍼의체력반지상자;
import lineage.world.object.item.bundle.스냅퍼의회복반지상자;
import lineage.world.object.item.bundle.열린상급오시리스의보물상자;
import lineage.world.object.item.bundle.열린하급오시리스의보물상자;
import lineage.world.object.item.bundle.초록빛나는물고기;
import lineage.world.object.item.bundle.파란빛나는물고기;
import lineage.world.object.item.bundle.흰빛나는물고기;
import lineage.world.object.item.cloak.CloakInvisibility;
import lineage.world.object.item.cloak.ElvenCloak;
import lineage.world.object.item.cloak.마물의망토;
import lineage.world.object.item.cloak.마물의부츠;
import lineage.world.object.item.cloak.마물의장갑;
import lineage.world.object.item.cook.Cook;
import lineage.world.object.item.diablo.아이스블링크;
import lineage.world.object.item.earring.룸티스의검은빛귀걸이;
import lineage.world.object.item.earring.룸티스의보랏빛귀걸이;
import lineage.world.object.item.earring.룸티스의붉은빛귀걸이;
import lineage.world.object.item.earring.룸티스의푸른빛귀걸이;
import lineage.world.object.item.etc.AdenArmor;
import lineage.world.object.item.etc.Ani;
import lineage.world.object.item.etc.ChangeSexPotion;
import lineage.world.object.item.etc.EvolutionFruit;
import lineage.world.object.item.etc.Exp_marble;
import lineage.world.object.item.etc.Exp_marble2;
import lineage.world.object.item.etc.Gid;
import lineage.world.object.item.etc.GoldMonster;
import lineage.world.object.item.etc.Hellf;
import lineage.world.object.item.etc.LaskBookMix;
import lineage.world.object.item.etc.LuckArmor;
import lineage.world.object.item.etc.MagicDoll2;
import lineage.world.object.item.etc.MagicDoll3;
import lineage.world.object.item.etc.MagicDoll4;
import lineage.world.object.item.etc.Mix;
import lineage.world.object.item.etc.Mix2;
import lineage.world.object.item.etc.Mix3;
import lineage.world.object.item.etc.Mix4;
import lineage.world.object.item.etc.Mix5;
import lineage.world.object.item.etc.Mix6;
import lineage.world.object.item.etc.OmanMix;
import lineage.world.object.item.etc.OmanMix2;
import lineage.world.object.item.etc.OmanSoul;
import lineage.world.object.item.etc.ShadowShrine;
import lineage.world.object.item.etc.SpellPotion;
import lineage.world.object.item.etc.그림자신전2층열쇠;
import lineage.world.object.item.etc.그림자신전3층열쇠;
import lineage.world.object.item.etc.순백의티_천연비누;
import lineage.world.object.item.etc.순백의티인장;
import lineage.world.object.item.etc.신비한날개깃털;
import lineage.world.object.item.etc.정령의결정;
import lineage.world.object.item.etc.흑마법가루;
import lineage.world.object.item.etc.희미한기억의구슬;
import lineage.world.object.item.gamble.Gambledice2;
import lineage.world.object.item.gamble.Gambledice3;
import lineage.world.object.item.gamble.Gambledice4;
import lineage.world.object.item.gamble.Gambledice6;
import lineage.world.object.item.garder.마법사의가더;
import lineage.world.object.item.garder.수호의가더;
import lineage.world.object.item.garder.체력의가더;
import lineage.world.object.item.helm.HelmInfravision;
import lineage.world.object.item.helm.HelmMagicHealing;
import lineage.world.object.item.helm.HelmMagicPower;
import lineage.world.object.item.helm.HelmMagicSpeed;
import lineage.world.object.item.helm.Turban;
import lineage.world.object.item.pet.PetArmor;
import lineage.world.object.item.pet.PetWeapon;
import lineage.world.object.item.potion.BlindingPotion;
import lineage.world.object.item.potion.BluePotion;
import lineage.world.object.item.potion.BraveryPotion;
import lineage.world.object.item.potion.BraveryPotion2;
import lineage.world.object.item.potion.CurePoisonPotion;
import lineage.world.object.item.potion.ElixirPotion;
import lineage.world.object.item.potion.ElixirPotion2;
import lineage.world.object.item.potion.ElixirPotion3;
import lineage.world.object.item.potion.ElvenWafer;
import lineage.world.object.item.potion.ExpReStorePowerPotion;
import lineage.world.object.item.potion.ExpRisePotion;
import lineage.world.object.item.potion.GreateBluePotion2;
import lineage.world.object.item.potion.HastePotion;
import lineage.world.object.item.potion.HealingPotion;
import lineage.world.object.item.potion.LawfulPotion;
import lineage.world.object.item.potion.ManaPotion;
import lineage.world.object.item.potion.PPotion;
import lineage.world.object.item.potion.SuperBluePotion;
import lineage.world.object.item.potion.WisdomPotion;
import lineage.world.object.item.quest.AriaReward;
import lineage.world.object.item.quest.BlackKey;
import lineage.world.object.item.quest.ElvenTreasure;
import lineage.world.object.item.quest.RedKey;
import lineage.world.object.item.quest.SecretRoomKey;
import lineage.world.object.item.quest.로빈후드의메모지1;
import lineage.world.object.item.quest.로빈후드의메모지2;
import lineage.world.object.item.quest.로빈후드의소개장;
import lineage.world.object.item.quest.신성한에바의물;
import lineage.world.object.item.ring.RingPolyControl;
import lineage.world.object.item.ring.RingSummonControl;
import lineage.world.object.item.ring.RingTeleportControl;
import lineage.world.object.item.ring.RingZnis;
import lineage.world.object.item.ring.스냅퍼의마나반지;
import lineage.world.object.item.ring.스냅퍼의마법저항반지;
import lineage.world.object.item.ring.스냅퍼의용사반지;
import lineage.world.object.item.ring.스냅퍼의지혜반지;
import lineage.world.object.item.ring.스냅퍼의집중반지;
import lineage.world.object.item.ring.스냅퍼의체력반지;
import lineage.world.object.item.ring.스냅퍼의회복반지;
import lineage.world.object.item.scroll.*;
import lineage.world.object.item.scroll.newscroll.KernodwelArmorHpMpNomal;
import lineage.world.object.item.scroll.newscroll.KernodwelArmorHpMpRoyal;
import lineage.world.object.item.scroll.newscroll.KernodwelArmorStatNomal;
import lineage.world.object.item.scroll.newscroll.KernodwelArmorStatRoyal;
import lineage.world.object.item.scroll.newscroll.KernodwelWeaponDmgNomal;
import lineage.world.object.item.scroll.newscroll.KernodwelWeaponDmgRoyal;
import lineage.world.object.item.scroll.newscroll.KernodwelWeaponHpMpNomal;
import lineage.world.object.item.scroll.newscroll.KernodwelWeaponHpMpRoyal;
import lineage.world.object.item.scroll.newscroll.KernodwelWeaponStatNomal;
import lineage.world.object.item.scroll.newscroll.KernodwelWeaponStatRoyal;
import lineage.world.object.item.scroll.newscroll.ScrollChangeBress;
import lineage.world.object.item.scroll.newscroll.ScrollCharLock;
import lineage.world.object.item.scroll.newscroll.ScrollCharUnLock;
import lineage.world.object.item.scroll.newscroll.ScrollCrobar;
import lineage.world.object.item.scroll.newscroll.ScrollLocationReset;
import lineage.world.object.item.scroll.newscroll.ScrollRoon;
import lineage.world.object.item.scroll.newscroll.ScrollRoonLast;
import lineage.world.object.item.scroll.newscroll.ScrollRoonReset;
import lineage.world.object.item.scroll.newscroll.Sskmu;
import lineage.world.object.item.scroll.newscroll.SuperScrollLabeledKernodwel;
import lineage.world.object.item.scroll.newscroll.Unic;
import lineage.world.object.item.scroll.SealedScroll;
import lineage.world.object.item.scroll.SealedCancelScroll;
import lineage.world.object.item.shield.ElvenShield;
import lineage.world.object.item.shield.반역자의방패;
import lineage.world.object.item.skill.ItemBloodtoSoul;
import lineage.world.object.item.skill.ItemBrAvt;
import lineage.world.object.item.skill.ItemGloing;
import lineage.world.object.item.skill.ItemGrAvt;
import lineage.world.object.item.skill.ItemReturntona;
import lineage.world.object.item.skill.ItemShing;
import lineage.world.object.item.skill.ItemShockStun;
import lineage.world.object.item.skill.ItemSoulOfFlame;
import lineage.world.object.item.skill.ItemTryple;
import lineage.world.object.item.sp.CharacterSaveMarbles;
import lineage.world.object.item.sp.LevelDownScroll;
import lineage.world.object.item.sp.LevelUpScroll;
import lineage.world.object.item.sp.Onea;
import lineage.world.object.item.sp.ScrollTOITeleport;
import lineage.world.object.item.sp.StatClear;
import lineage.world.object.item.sp.경험치아이템;
import lineage.world.object.item.sp.마력증강아이템;
import lineage.world.object.item.sp.버프물약;
import lineage.world.object.item.sp.버프물약2;
import lineage.world.object.item.sp.버프물약3;
import lineage.world.object.item.sp.지배반지버프;
import sp.item.Item_Remove_Wand;
import lineage.world.object.item.sp.전투강화아이템;
import lineage.world.object.item.sp.체력증강아이템;
import lineage.world.object.item.wand.EbonyWand;
import lineage.world.object.item.wand.EbonyWand2;
import lineage.world.object.item.wand.ExpulsionWand;
import lineage.world.object.item.wand.MapleWand;
import lineage.world.object.item.wand.MonsterInfoWand;
import lineage.world.object.item.wand.PineWand;
import lineage.world.object.item.wand.TeleportWand;
import lineage.world.object.item.weapon.Alice;
import lineage.world.object.item.weapon.BowOfSin;
import lineage.world.object.item.weapon.Claw;
import lineage.world.object.item.weapon.DiceDagger;
import lineage.world.object.item.weapon.Edoryu;
import lineage.world.object.item.weapon.SwordOfIllusion;
import lineage.world.object.item.weapon.디케이무기;
import lineage.world.object.item.weapon.창천무기;

public final class ItemDatabase {

	static private List<Item> list;
	static private Map<String, List<ItemInstance>> pool;
	static private List<Item> potion;

	/**
	 * 초기화 함
	 * 
	 * @param conㄷㄹ
	 */
	static public void init(Connection con) {
		TimeLine.start("ItemDatabase..");

		if (pool == null) {
			pool = new HashMap<String, List<ItemInstance>>();
			list = new ArrayList<Item>();
			potion = new ArrayList<Item>();
		}
		synchronized (list) {
			//
			pool.clear();
			list.clear();
			//
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				st = con.prepareStatement("SELECT * FROM item");
				rs = st.executeQuery();
				while (rs.next()) {
					Item i = new Item();
					i.setItemId(rs.getInt("item_id"));
					i.setName(rs.getString("아이템이름"));
					i.setType1(rs.getString("구분1"));
					i.setType2(rs.getString("구분2"));
					i.setNameId(rs.getString("NAMEID"));
					i.setNameIdCheck(rs.getString("NAMEID_CHECK"));
					i.setMaterial(getMaterial(rs.getString("재질")));
					i.setMaterialName(rs.getString("재질"));
					i.setDmgMin(rs.getInt("최소데미지"));
					i.setDmgMax(rs.getInt("최대데미지"));
					i.setWeight(Double.valueOf(rs.getString("무게")));
					i.setInvGfx(rs.getInt("인벤ID"));
					i.setGroundGfx(rs.getInt("GFXID"));
					i.setAction1(rs.getInt("ACTION1"));
					i.setAction2(rs.getInt("ACTION2"));
					i.setSell(rs.getString("판매").equalsIgnoreCase("true"));
					i.setPiles(rs.getString("겹침").equalsIgnoreCase("true"));
					i.setTrade(rs.getString("거래").equalsIgnoreCase("true"));
					i.setDrop(rs.getString("드랍").equalsIgnoreCase("true"));
					i.setWarehouse(rs.getString("창고").equalsIgnoreCase("true"));
					i.setWarehouseElf(rs.getString("창고_요숲").equalsIgnoreCase("true"));
					i.setWarehouseClan(rs.getString("창고_혈맹").equalsIgnoreCase("true"));
					i.setEnchant(rs.getString("인첸트").equalsIgnoreCase("true"));
					i.setSafeEnchant(rs.getInt("안전인첸트"));
					i.setRoyal(rs.getInt("군주"));
					i.setKnight(rs.getInt("기사"));
					i.setElf(rs.getInt("요정"));
					i.setWizard(rs.getInt("마법사"));
					i.setDarkElf(rs.getInt("다크엘프"));
					i.setDragonKnight(rs.getInt("용기사"));
					i.setBlackWizard(rs.getInt("환술사"));
					i.setWarrior(rs.getInt("전사"));
					i.setAddHit(rs.getInt("근거리명중"));
					i.setAddBowHit(rs.getInt("BowHit"));
					i.setAddBowDmg(rs.getInt("BowDmg"));
					i.setAddDmg(rs.getInt("근거리대미지"));
					i.setAc(rs.getInt("ac"));
					i.setAddStr(rs.getInt("add_str"));
					i.setAddDex(rs.getInt("add_dex"));
					i.setAddCon(rs.getInt("add_con"));
					i.setAddInt(rs.getInt("add_int"));
					i.setAddWis(rs.getInt("add_wis"));
					i.setAddCha(rs.getInt("add_cha"));
					i.setAddHp(rs.getInt("HP증가"));
					i.setAddMp(rs.getInt("MP증가"));
					i.setAddSp(rs.getInt("SP증가"));
					i.setAddMr(rs.getInt("MR증가"));
					i.setCanbedmg(rs.getString("손상").equalsIgnoreCase("true"));
					i.setLevelMin(rs.getInt("level_min"));
					i.setLevelMax(rs.getInt("level_max"));
					i.setEffect(rs.getInt("이펙트ID"));
					i.setSetId(rs.getInt("셋트아이템ID"));
					i.setContinuous(rs.getInt("continuous"));
					i.setWaterress(rs.getInt("waterress"));
					i.setWindress(rs.getInt("windress"));
					i.setEarthress(rs.getInt("earthress"));
					i.setFireress(rs.getInt("fireress"));
					i.setStunDefense(rs.getInt("stun_defense"));
					i.setHoldDefense(rs.getInt("hold_defense"));
					i.setAddWeight(rs.getFloat("add_weight"));
					i.setTicHp(rs.getInt("tic_hp"));
					i.setTicMp(rs.getInt("tic_mp"));
					i.setShopPrice(rs.getInt("shop_price"));
					i.setDropChance(rs.getInt("drop_chance"));
					i.setGfxMode(getWeaponGfx(i.getType2()));
					i.setWeaponAttackDistance(getWeaponAttackDistance(i.getGfxMode()));
					i.setSlot(getSlot(i.getType2()));
					i.setEquippedSlot(getEquippedSlot(i.getType2()));
					i.setSolvent(rs.getInt("solvent"));
					i.setBookChaoticZone(rs.getString("book_chaotic_zone").equalsIgnoreCase("true"));
					i.setBookLawfulZone(rs.getString("book_lawful_zone").equalsIgnoreCase("true"));
					i.setBookMomtreeZone(rs.getString("book_momtree_zone").equalsIgnoreCase("true"));
					i.setBookNeutralZone(rs.getString("book_neutral_zone").equalsIgnoreCase("true"));
					i.setBookTowerZone(rs.getString("book_tower_zone").equalsIgnoreCase("true"));
					if (rs.getString("attribute_crystal").equalsIgnoreCase("earth"))
						i.setAttributeCrystal(Lineage.ELEMENT_EARTH);
					else if (rs.getString("attribute_crystal").equalsIgnoreCase("fire"))
						i.setAttributeCrystal(Lineage.ELEMENT_FIRE);
					else if (rs.getString("attribute_crystal").equalsIgnoreCase("wind"))
						i.setAttributeCrystal(Lineage.ELEMENT_WIND);
					else if (rs.getString("attribute_crystal").equalsIgnoreCase("water"))
						i.setAttributeCrystal(Lineage.ELEMENT_WATER);
					i.setPolyName(rs.getString("poly_name"));
					i.setInventorySave(rs.getString("is_inventory_save").equalsIgnoreCase("true"));
					i.setAqua(rs.getString("is_aqua").equalsIgnoreCase("true"));
					i.setStealHp(rs.getInt("steal_hp"));
					i.setStealMp(rs.getInt("steal_mp"));
					i.setTohand(rs.getString("is_tohand").equalsIgnoreCase("true"));
					i.setReduction(rs.getInt("reduction"));
					i.setEnchantMr(rs.getInt("enchant_mr"));
					i.setCriticalEffect(rs.getInt("critical_effect"));
					i.setNote(rs.getString("note"));
					i.setHideAddDmg(rs.getInt("hide_add_dmg"));
					i.setEnchantDmg(rs.getInt("enchant_dmg"));
					i.setPvpDmg(rs.getInt("pvp_dmg"));
					i.setMonDmg(rs.getInt("mon_dmg"));

					try {
						StringBuffer sb = new StringBuffer();
						StringTokenizer stt = new StringTokenizer(i.getNameId(), " $ ");
						while (stt.hasMoreTokens())
							sb.append(stt.nextToken());
						i.setNameIdNumber(Integer.valueOf(sb.toString().trim()));
					} catch (Exception e) {
					}
					if (i.getType2().equalsIgnoreCase("potion") || i.getType2().equalsIgnoreCase("무한물약"))
						potion.add(i);

					list.add(i);
				}
			} catch (Exception e) {
				lineage.share.System.printf("%s : init(Connection con)\r\n", ItemDatabase.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(st, rs);
			}
		}

		TimeLine.end();
	}

	static public Item find(String type1, String type2) {
		synchronized (list) {
			for (Item i : list) {
				if (i.getType1().equalsIgnoreCase(type1) && i.getType2().equalsIgnoreCase(type2))
					return i;
			}
		}
		return null;
	}

	static public Item find2(String name) {
		synchronized (list) {
			for (Item i : list) {
				if (i.getName().replace(" ", "").equalsIgnoreCase(name))
					return i;
			}
			return null;
		}
	}
	
	/*
	 * 아이템 이름으로 아이템번호 찾기
	 */
	public static int findItemId(String name) {
		synchronized (list) {
			int itemid = 0;
			for (Item item : list) {
				if (item != null && item.getName().replace(" ", "").equals(name)) {
					itemid = item.getItemId();
					return itemid;
				}
			}
			return itemid;
		}
	}

	public static String findItemIdByNameWithoutSpace2(String name) {
		synchronized (list) {
			String itemid = null;
			for (Item item : list) {
				if (item != null && item.getName().replace(" ", "").equals(name)) {
					itemid = name;
					break;
				}
			}
			return itemid;
		}
	}

	public static Item findItemIdByNameWithoutSpace3(String name) {
		synchronized (list) {
			for (Item item : list) {
				if (item != null && item.getName().replace(" ", "").equals(name)) {
					return item;
				}
			}
			return null;
		}
	}

	
	/*
	 * 아이템 id로 검색
	 */

	static public Item find_ItemId(int nameid) {
		synchronized (list) {
			for (Item i : list) {
				if (i.getItemId() == nameid)
					return i;
			}
			return null;
		}
	}

	/**
	 * 이름으로 해당 객체 찾기 함수.
	 * 
	 * @param name
	 * @return
	 */
	static public Item find(String name) {
		synchronized (list) {
			for (Item i : list) {
				if (i.getName().equalsIgnoreCase(name))
					return i;
			}
			return null;
		}
	}

	static public Item find(int name_id) {
		synchronized (list) {
			for (Item i : list) {
				if (i.getNameIdNumber() == name_id)
					return i;
			}
			return null;
		}
	}

	
	static public Item find3(String name) {
		for (Item i : list) {
			if (i.getName().equalsIgnoreCase(name))
				return i;
		}
		return null;
	}

	public static Item find4(String str) {
		for (Item i : list) {
			if (i.getName().replaceAll(" ", "").equalsIgnoreCase(str))
				return i;
		}
		return null;
	}
	
	static public Item find5(String name) {
		synchronized (list) {
			for (Item i : list) {
				if (i.getName().replace(" ", "").equalsIgnoreCase(name))
					return i;
			}
			return null;
		}
	}
	/*
	 * public ItemInstance find(Class<?> c){ for( ItemInstance item : getList() ){
	 * if(item.getClass().toString().equals(c.toString())) return item; } return
	 * null; }
	 */

	public static Item find(Class<?> c) {
		synchronized (list) {
			for (Item item : list) {
				if (item.getClass().toString().equals(c.toString()))
					return item;
			}
			return null;
		}
	}

	static public Item findpotion() {
		synchronized (list) {
			for (Item i : list) {
				if (i.getNameIdNumber() == 26 || i.getNameIdNumber() == 235 || i.getNameIdNumber() == 237
						|| i.getNameIdNumber() == 238 || i.getNameIdNumber() == 255 || i.getNameIdNumber() == 328
						|| i.getNameIdNumber() == 794 || i.getNameIdNumber() == 1251 || i.getNameIdNumber() == 1252
						|| i.getNameIdNumber() == 1253 || i.getNameIdNumber() == 1943 || i.getNameIdNumber() == 2575
						|| i.getNameIdNumber() == 2576 || i.getNameIdNumber() == 2577 || i.getNameIdNumber() == 3301
						|| i.getNameIdNumber() == 852372 || i.getNameIdNumber() == 1062372
						|| i.getNameIdNumber() == 1072372 || i.getNameIdNumber() == 5256 || i.getNameIdNumber() == 5258
						|| i.getNameIdNumber() == 5257 || i.getNameIdNumber() == 5249 || i.getNameIdNumber() == 5250)
					return i;
			}
			return null;
		}
	}

	/**
	 * 아이템 객체 생성해주는 함수 : 아이템 고유 이름번호를 이용해 구분해서 클레스 생성 풀에 등록되어있는 객체 가져와서 재사용.
	 * 
	 * @param item
	 * @return
	 */
	static public ItemInstance newInstance(Item item) {
		// 버그 방지.
		if (item == null)
			return null;

		Object o = PluginController.init(ItemDatabase.class, "newInstance", item);
		if (o != null && o instanceof ItemInstance)
			return (ItemInstance) o;

		// 생성 처리.
		switch (item.getNameIdNumber()) {
		case 2: // 등잔
			return Lamp.clone(getPool(Lamp.class)).clone(item);
		case 4: // 아데나
			return Aden.clone(getPool(Aden.class)).clone(item);
		case 23: // 고기
		case 68: // 당근
		case 72: // 달걀
		case 82: // 레몬
		case 85: // 오렌지
		case 106: // 사과
		case 107: // 바나나
		case 1179: // 사탕
		case 1191: // 허브
		case 14294: // 수련자의 허브
			// case 5249: // 붕어
			// case 5250: // 잉어
			return Meat.clone(getPool(Meat.class)).clone(item);
		case 12940: // 화염의 막대
			return 화염의막대.clone(getPool(화염의막대.class)).clone(item);
		case 27: // 단풍나무 막대
		case 260: // 변신 막대
			return MapleWand.clone(getPool(MapleWand.class)).clone(item);
		case 28: // 소나무 막대
		case 258: // 괴물 생성 막대
			return PineWand.clone(getPool(PineWand.class)).clone(item);
		case 263: // 흑단 막대
		case 262: // 번개 소환 막대
			return EbonyWand.clone(getPool(EbonyWand.class)).clone(item);
		case 30: // 갑옷 마법 주문서 `젤고 머'
		case 249:
			return ScrollLabeledZelgoMer.clone(getPool(ScrollLabeledZelgoMer.class)).clone(item);
		case 3359:// 기안
			return ScrollLabeledZelgoMer2Ga.clone(getPool(ScrollLabeledZelgoMer2Ga.class)).clone(item);
		case 3366:// 고대인의 주술
			return ScrollLabeledZelgoMer2Ju.clone(getPool(ScrollLabeledZelgoMer2Ju.class)).clone(item);
		case 34: // 저주 풀기 주문서 `프라탸바야'
		case 243:
		case 3299: // 상아탑의 저주풀기 주문서
			return ScrollLabeledPratyavayah.clone(getPool(ScrollLabeledPratyavayah.class)).clone(item);
		case 35: // 무기 마법 주문서 `데이엔 푸엘스'
		case 244:
			return ScrollLabeledDaneFools.clone(getPool(ScrollLabeledDaneFools.class)).clone(item);
		case 3360: // 칼바스
			return ScrollLabeledDaneFools2Cal.clone(getPool(ScrollLabeledDaneFools2Cal.class)).clone(item);
		case 3363:// 고대인의 연금술
			return ScrollLabeledDaneFools2Yg.clone(getPool(ScrollLabeledDaneFools2Yg.class)).clone(item);
		case 39: // 귀환 주문서 `베르 예드 호레'
		case 505:
		case 3297: // 상아탑의 귀환 주문서
			return ScrollLabeledVerrYedHorae.clone(getPool(ScrollLabeledVerrYedHorae.class)).clone(item);
		case 40: // 순간이동 주문서 `벤자르 보르가브'
		case 230:
		case 3296: // 상아탑의 순간이동 주문서
			return ScrollLabeledVenzarBorgavve.clone(getPool(ScrollLabeledVenzarBorgavve.class)).clone(item);
		case 43: // 확인 주문서 `케르노드 웰'
		case 55:
		case 3298: // 상아탑의 확인 주문서, 수련자의 확인 주문서
			return ScrollLabeledKernodwel.clone(getPool(ScrollLabeledKernodwel.class)).clone(item);
		case 61: // 화살
		case 93: // 은 화살
		case 773: // 미스릴 화살
		case 2377: // 고대인의 화살
		case 77161: // 오리하루콘 화살
		case 3512: // 황금 화살
		case 3513: // 오리하루콘 화살
		case 3514: // 블랙미스릴 화살
		case 8586: // 수련자의 화살
		case 10948: // 수령의 블랙미스릴 화살
		case 10949: // 풍령의 블랙미스릴 화살
		case 10950: // 지령의 블랙미스릴 화살
		case 10951: // 화령의 블랙미스릴 화살
			return Arrow.clone(getPool(Arrow.class)).clone(item);
		case 2463: // 스팅
		case 2516: // 실버 스팅
//			case 2517:	// 헤비 스팅
//				return ThrowingKnife.clone( getPool(ThrowingKnife.class) ).clone(item);
		case 67: // 양초
			return Candle.clone(getPool(Candle.class)).clone(item);
		case 180: // 투명망토
			return CloakInvisibility.clone(getPool(CloakInvisibility.class)).clone(item);
		case 26: // 체력 회복제
		case 235: // 주홍 물약
		case 237: // 빨간 물약
		case 238: // 맑은 물약
		case 255: // 고급 체력 회복제
		case 328: // 강력 체력 회복제
		case 794: // 엔트의 열매
		case 1251: // 농축 체력 회복제
		case 1252: // 농축 고급 체력 회복제
		case 1253: // 농축 강력 체력 회복제
		case 1943: // 토끼의 간
		case 2575: // 고대의 체력 회복제
		case 2576: // 고대의 고급 체력 회복제
		case 2577: // 고대의 강력 체력 회복제
		case 3301: // 상아탑의 체력 회복제
		case 852372: // 오렌지 주스
		case 1062372: // 사과 주스
		case 1072372: // 바나나 주스
		case 5233: //쿠작의 식량
		case 5256: // 어린 물고기
		case 5258: // 강한 물고기
		case 5257: // 재빠른 물고기
			return HealingPotion.clone(getPool(HealingPotion.class)).clone(item);
		// case 1252: // 농축 고급 체력 회복제
		// return HealingPotion2.clone( getPool(HealingPotion2.class) ).clone(item);
		case 110: // 엘븐 와퍼
		case 7895: //농축 집중
		case 5999: // 수련자의 엘븐 와퍼
			return ElvenWafer.clone(getPool(ElvenWafer.class)).clone(item);
		case 170: // 요정족 망토
			return ElvenCloak.clone(getPool(ElvenCloak.class)).clone(item);
		case 187: // 요정족 방패
			return ElvenShield.clone(getPool(ElvenShield.class)).clone(item);
		case 232: // 파란물약
		case 507: // 마력 회복 물약
		case 6000: // 수련자의 마나 회복 물약
			return BluePotion.clone(getPool(BluePotion.class)).clone(item);
		case 7897:
			return SuperBluePotion.clone(getPool(SuperBluePotion.class)).clone(item);
		case 233: // 비취물약
		case 763: // 엔트의 줄기.
		case 8425: // 수련자의 해독제
			return CurePoisonPotion.clone(getPool(CurePoisonPotion.class)).clone(item);
		case 234: // 초록물약
		case 264: // 속도향상물약
		case 3302: // 상아탑의 속도향상 물약
		case 3405: // 와인
		case 1652234: // 강화 초록 물약
		case 7898: // 농축 속도향상 물약
			return HastePotion.clone(getPool(HastePotion.class)).clone(item);
		case 239: // 불투명 물약
		case 242: // 눈멀기 물약
			return BlindingPotion.clone(getPool(BlindingPotion.class)).clone(item);
		case 241: // 순간이동 조종 반지
		//case 26936: // 순간이동 지배 반지
			return RingTeleportControl.clone(getPool(RingTeleportControl.class)).clone(item);
		case 49: // 벨록스 넵
		case 257: // 부활 주문서
			return ScrollResurrection.clone(getPool(ScrollResurrection.class)).clone(item);
		case 261: // 변신 조종 반지
			return RingPolyControl.clone(getPool(RingPolyControl.class)).clone(item);
		case 326: // 랜턴
		case 14220326: // 수련자의 랜턴
			return Lantern.clone(getPool(Lantern.class)).clone(item);
		case 327: // 랜턴용 기름
			return LanternOil.clone(getPool(LanternOil.class)).clone(item);
		case 343: // 슬라임 레이스표
		case 1247: // 개 레이스 표
			return RaceTicket.clone(getPool(RaceTicket.class)).clone(item);
		case 416: // 악운의 단검
			return DiceDagger.clone(getPool(DiceDagger.class)).clone(item);
		case 617: // 빨간 양말
			return RedSock.clone(getPool(RedSock.class)).clone(item);
		case 623: // 괴물 눈 고기
			return MonsterEyeMeat.clone(getPool(MonsterEyeMeat.class)).clone(item);
		case 762: // 정령의 돌
			return ElementalStone.clone(getPool(ElementalStone.class)).clone(item);
		case 777: // 마법의 플룻
			return MagicFlute.clone(getPool(MagicFlute.class)).clone(item);
		case 872: // 앨리스
		case 8723697: // 앨리스 1단계
		case 8723698: // 앨리스 2단계
		case 8723699: // 앨리스 3단계
		case 8723700: // 앨리스 4단계
		case 8723701: // 앨리스 5단계
		case 8723702: // 앨리스 6단계
		case 8723703: // 앨리스 7단계
		case 8723704: // 앨리스 8단계
		case 16116: // 앨리스 1단계
		case 16117: // 앨리스 2단계
		case 16118: // 앨리스 3단계
		case 16119: // 앨리스 4단계
		case 16120: // 앨리스 5단계
		case 16121: // 앨리스 6단계
		case 16122: // 앨리스 7단계
		case 16123: // 앨리스 8단계
			return Alice.clone(getPool(Alice.class)).clone(item);
		case 12333: // 오만의 탑 1층 이동 주문서
		case 2168: // 오만의 탑 2층 주문서
		case 2404: // 오만의 탑 3층 주문서
		case 2405: // 오만의 탑 4층 주문서
		case 2673: // 오만의 탑 5층 주문서
			return TOITeleportScroll.clone(getPool(TOITeleportScroll.class)).clone(item);
		case 954: // 여관 열쇠
			return InnRoomKey.clone(getPool(InnRoomKey.class)).clone(item);
		case 938: // 마법의 투구: 치유
			return HelmMagicHealing.clone(getPool(HelmMagicHealing.class)).clone(item);
		case 939: // 마법의 투구: 신속
			return HelmMagicSpeed.clone(getPool(HelmMagicSpeed.class)).clone(item);
		case 940: // 마법의 투구: 힘
			return HelmMagicPower.clone(getPool(HelmMagicPower.class)).clone(item);
		case 1008: // 인프라비젼 투구
			return HelmInfravision.clone(getPool(HelmInfravision.class)).clone(item);
		case 943: // 용기의 물약
		case 5997: // 수련자의 용기의 물약
		case 3406: // 위스키
		case 7896: //농축 용기
			return BraveryPotion.clone(getPool(BraveryPotion.class)).clone(item);
		case 3372: // 악마의 피
		case 6007: // 초보자 악마의 피
		case 7899: // 농축 악마의 피
			return BraveryPotion2.clone(getPool(BraveryPotion2.class)).clone(item);
		case 944: // 지혜의 물약
		case 7894:
		case 7900:
			return WisdomPotion.clone(getPool(WisdomPotion.class)).clone(item);
		case 7893:
			return ScrollPolymorph5.clone(getPool(ScrollPolymorph5.class)).clone(item);
		case 971:
		case 3300:
			return ScrollPolymorph3.clone(getPool(ScrollPolymorph3.class)).clone(item);
		case 975: // 추방 막대
			return ExpulsionWand.clone(getPool(ExpulsionWand.class)).clone(item);
		case 1086: // 펫 호루라기
			return PetWhistle.clone(getPool(PetWhistle.class)).clone(item);
		case 1100: // 숫돌
		case 142201100: // 수련자의 숫돌
			return Whetstone.clone(getPool(Whetstone.class)).clone(item);
		case 1486: // 빈 주문서 (레벨 1)
		case 1892: // 2
		case 1893: // 3
		case 1894: // 4
		case 1895: // 5
			return BlankScroll.clone(getPool(BlankScroll.class)).clone(item);
		case 1507: // 에바의 물약
		//case 1508: // 인어의 비늘
			return BlessEva.clone(getPool(BlessEva.class)).clone(item);
		case 517: // 마법서 (힐)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 1, 0).clone(item);
		case 518: // 마법서 (라이트)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 1, 1).clone(item);
		case 519: // 마법서 (실드)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 1, 2).clone(item);
		case 520: // 마법서 (에너지 볼트)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 1, 3).clone(item);
		case 521: // 마법서 (텔리포트)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 1, 4).clone(item);
		case 522: // 마법서 (큐어 포이즌)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 2, 0).clone(item);
		case 523: // 마법서 (칠 터치)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 2, 1).clone(item);
		case 524: // 마법서 (커스: 포이즌)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 2, 2).clone(item);
		case 525: // 마법서 (인챈트 웨폰)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 2, 3).clone(item);
		case 526: // 마법서 (디텍션)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 2, 4).clone(item);
		case 527: // 마법서 (라이트닝)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 3, 0).clone(item);
		case 528: // 마법서 (턴 언데드)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 3, 1).clone(item);
		case 529: // 마법서 (익스트라 힐)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 3, 2).clone(item);
		case 530: // 마법서 (커스: 블라인드)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 3, 3).clone(item);
		case 531: // 마법서 (블레스드 아머)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 3, 4).clone(item);
		case 532: // 마법서 (파이어볼)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 4, 0).clone(item);
		case 533: // 마법서 (피지컬 인챈트: DEX)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 4, 1).clone(item);
		case 534: // 마법서 (웨폰 브레이크)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 4, 2).clone(item);
		case 535: // 마법서 (뱀파이어릭 터치)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 4, 3).clone(item);
		case 536: // 마법서 (슬로우)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 4, 4).clone(item);
		case 537: // 마법서 (커스: 패럴라이즈)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 5, 0).clone(item);
		case 538: // 마법서 (콜 라이트닝)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 5, 1).clone(item);
		case 539: // 마법서 (그레이터 힐)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 5, 2).clone(item);
		case 540: // 마법서 (테이밍 몬스터)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 5, 3).clone(item);
		case 541: // 마법서 (리무브 커스)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 5, 4).clone(item);
		case 542: // 마법서 (크리에이트 좀비)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 6, 0).clone(item);
		case 543: // 마법서 (피지컬 인챈트: STR)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 6, 1).clone(item);
		case 544: // 마법서 (헤이스트)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 6, 2).clone(item);
		case 545: // 마법서 (캔슬레이션)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 6, 3).clone(item);
		case 546: // 마법서 (이럽션)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 6, 4).clone(item);
		case 547: // 마법서 (힐 올)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 7, 0).clone(item);
		case 548: // 마법서 (아이스 랜스)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 7, 1).clone(item);
		case 549: // 마법서 (서먼 몬스터)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 7, 2).clone(item);
		case 550: // 마법서 (홀리 서클)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 7, 3).clone(item);
		case 551: // 마법서 (토네이도)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 7, 4).clone(item);
		case 552: // 마법서 (풀 힐)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 8, 0).clone(item);
		case 553: // 마법서 (파이어월)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 8, 1).clone(item);
		case 554: // 마법서 (블리자드)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 8, 2).clone(item);
		case 555: // 마법서 (인비지블리티)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 8, 3).clone(item);
		case 556: // 마법서 (리절렉션)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 8, 4).clone(item);
		case 557: // 마법서 (라이트닝 스톰)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 9, 0).clone(item);
		case 558: // 마법서 (포그 오브 슬리핑)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 9, 1).clone(item);
		case 559: // 마법서 (셰이프 체인지)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 10, 7).clone(item);
		case 560: // 마법서 (이뮨 투 함)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 9, 3).clone(item);
		case 561: // 마법서 (매스 텔리포트)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 9, 4).clone(item);
		case 562: // 마법서 (크리에이트 매지컬 웨폰)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 10, 0).clone(item);
		case 563: // 마법서 (미티어 스트라이크)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 10, 1).clone(item);
		case 564: // 마법서 (리플렉팅 풀)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 10, 2).clone(item);
		case 565: // 마법서 (스톱)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 10, 3).clone(item);
		case 566: // 마법서 (디스인티그레이트)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 10, 4).clone(item);
		case 1581: // 마법서 (아이스 대거)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 1, 5).clone(item);
		case 1582: // 마법서 (윈드 커터)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 1, 6).clone(item);
		case 1583: // 마법서 (파이어 애로우)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 2, 6).clone(item);
		case 1584: // 마법서 (스탈락)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 2, 7).clone(item);
		case 1585: // 마법서 (프로즌 클라우드)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 3, 5).clone(item);
		case 1586: // 마법서 (어스 재일)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 4, 5).clone(item);
		case 1587: // 마법서 (콘 오브 콜드)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 5, 5).clone(item);
		case 1588: // 마법서 (선 버스트)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 6, 5).clone(item);
		case 1589: // 마법서 (어스 퀘이크)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 8, 5).clone(item);
		case 1590: // 마법서 (파이어 스톰)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 9, 5).clone(item);
		case 1651: // 마법서 (그레이터 헤이스트)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 7, 5).clone(item);
		case 1857: // 마법서 (홀리 웨폰)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 1, 7).clone(item);
		case 1858: // 마법서 (디크리즈 웨이트)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 2, 5).clone(item);
		case 1859: // 마법서 (위크 엘리멘트)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 3, 6).clone(item);
		case 1860: // 마법서 (카운터 매직)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 4, 6).clone(item);
		case 1861: // 마법서 (메디테이션) 4 7
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 9, 0).clone(item);
		case 1862: // 마법서 (마나 드레인)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 5, 6).clone(item);
		case 1863: // 마법서 (다크니스)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 5, 7).clone(item);
		case 1864: // 마법서 (위크니스)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 6, 6).clone(item);
		case 1865: // 마법서 (블레스 웨폰)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 6, 7).clone(item);
		case 1866: // 마법서 (매지컬 마인)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 7, 6).clone(item);
		case 1867: // 마법서 (디지즈)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 7, 7).clone(item);
		case 1868: // 마법서 (라이프 스트림)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 8, 6).clone(item);
		case 1869: // 마법서 (사일런스)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 8, 7).clone(item);
		case 1870: // 마법서 (디케이 포션)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 9, 6).clone(item);
		case 1871: // 마법서 (이너 사이트)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 9, 7).clone(item);
		case 1872: // 마법서 (맵솔루트 배리어)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 10, 5).clone(item);
		case 1874: // 마법서 (프리징 블리자드)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 10, 6).clone(item);
		case 1873: // 마법서 (어드밴스)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 9, 2).clone(item);
		case 1959: // 마법서 (트루 타겟)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 15, 0).clone(item);
		case 1960: // 마법서 (글로잉 오라)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 15, 1).clone(item);
		case 3175: // 마법서 (샤이닝 오라)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 15, 2).clone(item);
		case 2089: // 마법서 (콜 클렌)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 15, 3).clone(item);
		case 3260: // 마법서 (런 클렌)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 15, 5).clone(item);
		case 3176: // 마법서 (브레이브 오라)
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 15, 4).clone(item);
		case 1829: // 정령의 수정 (레지스트 매직)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 17, 0).clone(item);
		case 1830: // 정령의 수정 (바디 투 마인드)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 17, 1).clone(item);
		case 1831: // 정령의 수정 (텔레포트 투 마더)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 17, 2).clone(item);
		case 3261: // 정령의 수정 (트리플 애로우)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 17, 3).clone(item);
		// return ItemCrystalInstance.clone( getPool(ItemCrystalInstance.class), 19, 0
		// ).clone(item);
		case 3262: // 정령의 수정 (엘리멘탈 폴 다운)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 17, 4).clone(item);
		case 1832: // 정령의 수정 (클리어 마인드)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 18, 0).clone(item);
		case 1833: // 정령의 수정 (레지스트 엘리멘트)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 18, 1).clone(item);
		case 1834: // 정령의 수정 (리턴 투 네이쳐)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 19, 0).clone(item);
		case 1835: // 정령의 수정 (블러드 투 소울)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 19, 1).clone(item);
		case 1836: // 정령의 수정 (프로텍션 프롬 엘리멘트)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 19, 2).clone(item);
		case 1837: // 정령의 수정 (파이어 웨폰) 19 3
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 19, 3).clone(item);
		case 1838: // 정령의 수정 (윈드 샷)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 19, 4).clone(item);
		case 1839: // 정령의 수정 (윈드 워크)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 19, 5).clone(item);
		case 1840: // 정령의 수정 (어스 스킨)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 19, 6).clone(item);
		case 1841: // 정령의 수정 (인탱글)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 19, 7).clone(item);
		case 1842: // 정령의 수정 (이레이즈 매직)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 20, 0).clone(item);
		case 1843: // 정령의 수정 (서먼 레서 엘리멘탈)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 20, 1).clone(item);
		case 1844: // 정령의 수정 (브레스 오브 파이어)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 20, 2).clone(item);
		case 1845: // 정령의 수정 (아이 오브 스톰)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 20, 3).clone(item);
		case 1846: // 정령의 수정 (어스 바인드)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 20, 4).clone(item);
		case 1847: // 정령의 수정 (네이쳐스 터치)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 20, 5).clone(item);
		case 1848: // 정령의 수정 (블레스 오브 어스)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 20, 6).clone(item);
		case 4716: // 정령의 수정 (아쿠아 프로텍트)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 20, 7).clone(item);
		case 1849: // 정령의 수정 (에어리어 오브 사일런스)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 21, 0).clone(item);
		case 1850: // 정령의 수정 (서먼 그레이터 엘리멘탈)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 21, 1).clone(item);
		case 1851: // 정령의 수정 (버닝 웨폰)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 21, 2).clone(item);
		case 1852: // 정령의 수정 (네이쳐스 블레싱)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 21, 3).clone(item);
		case 1853: // 정령의 수정 (콜 오브 네이쳐)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 21, 4).clone(item);
		case 1854: // 정령의 수정 (스톰 샷)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 21, 5).clone(item);
		case 3264: // 정령의 수정 (윈드 쉐클)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 21, 6).clone(item);
		case 1856: // 정령의 수정 (아이언 스킨)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 21, 7).clone(item);
		case 3265: // 정령의 수정 (엑조틱 바이탈라이즈)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 22, 0).clone(item);
		case 3266: // 정령의 수정 (워터 라이프)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 22, 1).clone(item);
		case 3267: // 정령의 수정 (엘리멘탈 파이어)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 22, 2).clone(item);
		case 1855: // 정령의 수정 (스톰 워크)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 22, 3).clone(item);
		case 4717: // 정령의 수정 (폴루트 워터)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 22, 4).clone(item);
		case 4718: // 정령의 수정 (스트라이크 게일)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 22, 5).clone(item);
		case 4714: // 정령의 수정 (소울 오브 프레임)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 22, 6).clone(item);
		case 4715: // 정령의 수정 (어디셔널 파이어)
			return ItemCrystalInstance.clone(getPool(ItemCrystalInstance.class), 22, 7).clone(item);
		case 1173: // 개목걸이
			return DogCollar.clone(getPool(DogCollar.class)).clone(item);
		case 1101: // 지도 본토
			return MiniMap.clone(getPool(MiniMap.class), 16).clone(item);
		case 1102: // 지도 말섬
			return MiniMap.clone(getPool(MiniMap.class), 1).clone(item);
		case 1103: // 지도 글루딘
			return MiniMap.clone(getPool(MiniMap.class), 2).clone(item);
		case 1104: // 지도 켄트
			return MiniMap.clone(getPool(MiniMap.class), 3).clone(item);
		case 1105: // 지도 화민촌
			return MiniMap.clone(getPool(MiniMap.class), 4).clone(item);
		case 1106: // 지도 요숲
			return MiniMap.clone(getPool(MiniMap.class), 5).clone(item);
		case 1107: // 지도 우드벡
			return MiniMap.clone(getPool(MiniMap.class), 6).clone(item);
		case 1108: // 지도 은기사
			return MiniMap.clone(getPool(MiniMap.class), 7).clone(item);
		case 1109: // 지도 용계
			return MiniMap.clone(getPool(MiniMap.class), 8).clone(item);
		case 1188: // 지도 기란
			return MiniMap.clone(getPool(MiniMap.class), 9).clone(item);
		case 1533: // 지도 노섬
			return MiniMap.clone(getPool(MiniMap.class), 10).clone(item);
		case 1534: // 지도 숨계
			return MiniMap.clone(getPool(MiniMap.class), 11).clone(item);
		case 1535: // 지도 하이네
			return MiniMap.clone(getPool(MiniMap.class), 12).clone(item);
		case 1607: // 지도 웰던
			return MiniMap.clone(getPool(MiniMap.class), 13).clone(item);
		case 1889: // 지도 오렌
			return MiniMap.clone(getPool(MiniMap.class), 14).clone(item);
		// case 1821: // 사이하의 활
		// return SayhaBow.clone( getPool(SayhaBow.class) ).clone(item);
		//case 1411: // 노래하는 섬 귀환 주문서
			//return ScrollReturnSingingIsland.clone(getPool(ScrollReturnSingingIsland.class)).clone(item);
		//case 1424: // 숨겨진 계곡 귀환 주문서
			//return ScrollReturnHiddenValley.clone(getPool(ScrollReturnHiddenValley.class)).clone(item);
		case 2203: // 지도 아덴
			return MiniMap.clone(getPool(MiniMap.class), 15).clone(item);
		case 2543: // 지도 침동
			return MiniMap.clone(getPool(MiniMap.class), 17).clone(item);
		case 12335: // 봉인된 오만의 탑 1층 이동 부적
			return SealedTOITeleportCharm.clone(getPool(SealedTOITeleportCharm.class), 1).clone(item);
		case 2582: // 봉인된 오만의 탑 2층 이동 부적
			return SealedTOITeleportCharm.clone(getPool(SealedTOITeleportCharm.class), 2).clone(item);
		case 2583: // 봉인된 오만의 탑 3층 이동 부적
			return SealedTOITeleportCharm.clone(getPool(SealedTOITeleportCharm.class), 3).clone(item);
		case 2584: // 봉인된 오만의 탑 4층 이동 부적
			return SealedTOITeleportCharm.clone(getPool(SealedTOITeleportCharm.class), 4).clone(item);
		case 2585: // 봉인된 오만의 탑 5층 이동 부적
			return SealedTOITeleportCharm.clone(getPool(SealedTOITeleportCharm.class), 5).clone(item);
		case 12336:	// 오만의 탑 1층 이동 부적
		case 2400: // 오만의 탑 2층 이동 부적
		case 2401: // 오만의 탑 3층 이동 부적
		case 2402: // 오만의 탑 4층 이동 부적
		case 2403: // 오만의 탑 5층 이동 부적
		case 12337:	// 오만의 탑 1층 지배 부적
		case 12338: // 오만의 탑 2층 지배 부적
		case 12339: // 오만의 탑 3층 지배 부적
		case 12340: // 오만의 탑 4층 지배 부적
		case 12341: // 오만의 탑 5층 지배 부적
			return TOITeleportCharm.clone(getPool(TOITeleportCharm.class)).clone(item);
		case 3616: // 지도 해적섬
			return MiniMap.clone(getPool(MiniMap.class), 18).clone(item);
		case 1075: // 편지지
		case 1606: // 크리스마스 카드
			return Letter.clone(getPool(Letter.class)).clone(item);
		case 1146: // 혈맹 편지지
			return Letter.clone(getPool(Letter.class)).clone(item);
//			case 1146:	// 혈맹 편지지
//				return PledgeLetter.clone( getPool(PledgeLetter.class) ).clone(item);
		case 5116: // 신비한 날개깃털
			return 신비한날개깃털.clone(getPool(신비한날개깃털.class)).clone(item);
		case 1755: // 붉은 열쇠
			return RedKey.clone(getPool(RedKey.class)).clone(item);
		case 1756: // 검은 열쇠
			return BlackKey.clone(getPool(BlackKey.class)).clone(item);
		case 2022: // 비밀방 열쇠
			return SecretRoomKey.clone(getPool(SecretRoomKey.class)).clone(item);
		case 2090: // 아리아의 보답
			return AriaReward.clone(getPool(AriaReward.class)).clone(item);
		case 2091: // 요정족 보물
			return ElvenTreasure.clone(getPool(ElvenTreasure.class)).clone(item);
		case 2380: // 혈맹 귀환 주문서
		case 142202380: // 수련자의 혈맹 귀환 주문서
			return ScrollLabeledVerrYedHoraePledgeHouse.clone(getPool(ScrollLabeledVerrYedHoraePledgeHouse.class))
					.clone(item);
		case 1997: // 환상의 검
			return SwordOfIllusion.clone(getPool(SwordOfIllusion.class)).clone(item);
		case 1998: // 환상의 갑옷
			return ArmorOfIllusion.clone(getPool(ArmorOfIllusion.class)).clone(item);
		case 1999: // 환상의 활
			return BowOfIllusion.clone(getPool(BowOfIllusion.class)).clone(item);
		case 2000: // 환상의 무기 마법 주문서
			return ScrollofEnchantWeaponIllusion.clone(getPool(ScrollofEnchantWeaponIllusion.class)).clone(item);
		case 2001: // 환상의 갑옷 마법 주문서
			return ScrollOfEnchantArmorIllusion.clone(getPool(ScrollOfEnchantArmorIllusion.class)).clone(item);
		case 5239: // 용해제
			return Solvent.clone(ItemDatabase.getPool(Solvent.class)).clone(item);
		case 14871436: // 마법주문서 (힐)
			return SpellScrollLesserHeal.clone(getPool(SpellScrollLesserHeal.class)).clone(item);
		case 14871437: // 마법주문서 (라이트)
			return SpellScrollLight.clone(getPool(SpellScrollLight.class)).clone(item);
		case 14871438: // 마법주문서 (실드)
			return SpellScrollShield.clone(getPool(SpellScrollShield.class)).clone(item);
		case 14871439: // 마법주문서 (에너지 볼트)
			return SpellScrollEnergyBolt.clone(getPool(SpellScrollEnergyBolt.class)).clone(item);
		case 14871966: // 마법주문서 (아이스 대거)
			return SpellScrollIceDagger.clone(getPool(SpellScrollIceDagger.class)).clone(item);
		case 14871967: // 마법주문서 (윈드커터)
			return SpellScrollWindShuriken.clone(getPool(SpellScrollWindShuriken.class)).clone(item);
		case 14871440: // 마법주문서 (텔레포트)
			return SpellScrollTeleport.clone(getPool(SpellScrollTeleport.class)).clone(item);
		case 14871977: // 마법주문서 (홀리웨폰)
			return SpellScrollHolyWeapon.clone(getPool(SpellScrollHolyWeapon.class)).clone(item);
		case 14871441: // 마법주문서 (큐어포이즌)
			return SpellScrollCurePoison.clone(getPool(SpellScrollCurePoison.class)).clone(item);
		case 14871442: // 마법주문서 (칠터치)
			return SpellScrollChillTouch.clone(getPool(SpellScrollChillTouch.class)).clone(item);
		case 14871443: // 마법주문서 (커스: 포이즌)
			return SpellScrollCursePoison.clone(getPool(SpellScrollCursePoison.class)).clone(item);
		case 14871444: // 마법주문서 (인첸트 웨폰)
			return SpellScrollEnchantWeapon.clone(getPool(SpellScrollEnchantWeapon.class)).clone(item);
		case 14871445: // 마법주문서 (디텍션)
			return SpellScrollDetection.clone(getPool(SpellScrollDetection.class)).clone(item);
		case 14871978: // 마법주문서 (디크리즈 웨이트)
			return SpellScrollDecreaseWeight.clone(getPool(SpellScrollDecreaseWeight.class)).clone(item);
		case 14871968: // 마법주문서 (파이어 애로우)
			return SpellScrollFireArrow.clone(getPool(SpellScrollFireArrow.class)).clone(item);
		case 14871969: // 마법주문서 (스탈락)
			return SpellScrollStalac.clone(getPool(SpellScrollStalac.class)).clone(item);
		case 14871446: // 마법주문서 (라이트닝)
			return SpellScrollLightning.clone(getPool(SpellScrollLightning.class)).clone(item);
		case 14871447: // 마법주문서 (턴 언데드)
			return SpellScrollTurnUndead.clone(getPool(SpellScrollTurnUndead.class)).clone(item);
		case 14871448: // 마법주문서 (익스트라 힐)
			return SpellScrollHeal.clone(getPool(SpellScrollHeal.class)).clone(item);
		case 14871449: // 마법주문서 (커스: 블라인드)
			return SpellScrollCurseBlind.clone(getPool(SpellScrollCurseBlind.class)).clone(item);
		case 14871450: // 마법주문서 (블레스드 아머)
			return SpellScrollBlessedArmor.clone(getPool(SpellScrollBlessedArmor.class)).clone(item);
		case 14871451: // 마법주문서 (파이어볼)
			return SpellScrollFireball.clone(getPool(SpellScrollFireball.class)).clone(item);
		case 14871452: // 마법주문서 (피치컬 인챈트 DEX)
			return SpellScrollPhysicalEnchantDex.clone(getPool(SpellScrollPhysicalEnchantDex.class)).clone(item);
		case 14871453: // 마법주문서 (웨폰 브레이크)
			return SpellScrollWeaponBreak.clone(getPool(SpellScrollWeaponBreak.class)).clone(item);
		case 14871454: // 마법주문서 (뱀파이어릭 터치)
			return SpellScrollVampiricTouch.clone(getPool(SpellScrollVampiricTouch.class)).clone(item);
		case 14871455: // 마법주문서 (슬로우)
			return SpellScrollSlow.clone(getPool(SpellScrollSlow.class)).clone(item);
		case 14871971: // 마법주문서 (어스 재일)
			return SpellScrollEarthJail.clone(getPool(SpellScrollEarthJail.class)).clone(item);
		case 14871980: // 마법주문서 (카운터 매직)
			return SpellScrollCounterMagic.clone(getPool(SpellScrollCounterMagic.class)).clone(item);
		case 14871981: // 마법주문서 (메디테이션)
			return SpellScrollMeditation.clone(getPool(SpellScrollMeditation.class)).clone(item);
		case 14871485: // 마법주문서 (디스인그레이트)
			return SpellScrollDestroy.clone(getPool(SpellScrollDestroy.class)).clone(item);
		case 14871479: // 마법주문서 (이뮨 투 함)
			return SpellScrollImmunetoHarm.clone(getPool(SpellScrollImmunetoHarm.class)).clone(item);
		case 14871992: // 마법주문서 (앱솔루트 베리어)
			return SpellScrollAbsoluteBarrier.clone(getPool(SpellScrollAbsoluteBarrier.class)).clone(item);
		case 14871970: // 마법주문서 (프로즌클라우드)
			return SpellScrollFrozenCloud.clone(getPool(SpellScrollFrozenCloud.class)).clone(item);
		case 14871456: // 마법주문서 (커스: 패럴라이즈)
			return SpellScrollCurseParalyze.clone(getPool(SpellScrollCurseParalyze.class)).clone(item);
		case 14871457: // 마법주문서 (콜 라이트닝)
			return SpellScrollCallLightning.clone(getPool(SpellScrollCallLightning.class)).clone(item);
		case 14871458: // 마법주문서 (그레이터 힐)
			return SpellScrollGreaterHeal.clone(getPool(SpellScrollGreaterHeal.class)).clone(item);
		case 14871459: // 마법주문서 (테이밍 몬스터)
			return SpellScrollTameMonster.clone(getPool(SpellScrollTameMonster.class)).clone(item);
		case 14871460: // 마법주문서 (리무브 커스)
			return SpellScrollRemoveCurse.clone(getPool(SpellScrollRemoveCurse.class)).clone(item);
		case 14871972: // 마법주문서 (콘 오브 콜드)
			return SpellScrollConeOfCold.clone(getPool(SpellScrollConeOfCold.class)).clone(item);
		case 14871982: // 마법주문서 (마나 드레인)
			return SpellScrollManaDrain.clone(getPool(SpellScrollManaDrain.class)).clone(item);
		case 14871983: // 마법주문서 (다크니스)
			return SpellScrollDarkness.clone(getPool(SpellScrollDarkness.class)).clone(item);
		case 3278: // 말하는 두루마리
			return TalkingScroll.clone(getPool(TalkingScroll.class)).clone(item);
		case 2530: // 엘릭서-STR
		case 2532: // dex
		case 2531: // con
		case 2534: // wis
		case 2533: // int
		case 2535: // cha
			return ElixirPotion.clone(getPool(ElixirPotion.class)).clone(item);
		case 2578: // 송편
		case 2579: // 쑥송편
		case 3370: // 정신력의 물약
		case 5249: // 붕어
		case 5250: // 잉어
		case 5234: //쿠작 영양
			return ManaPotion.clone(getPool(ManaPotion.class)).clone(item);
		case 2518: // 흑정령의 수정 (블라인드 하이딩)
			return ItemDarkSpiritCrystalInstance.clone(getPool(ItemDarkSpiritCrystalInstance.class), 13, 0).clone(item);
		case 2519: // 흑정령의 수정 (인챈트 베놈)
			return ItemDarkSpiritCrystalInstance.clone(getPool(ItemDarkSpiritCrystalInstance.class), 13, 1).clone(item);
		case 2520: // 흑정령의 수정 (쉐도우 아머)
			return ItemDarkSpiritCrystalInstance.clone(getPool(ItemDarkSpiritCrystalInstance.class), 13, 2).clone(item);
		case 2521: // 흑정령의 수정 (브링 스톤)
			return ItemDarkSpiritCrystalInstance.clone(getPool(ItemDarkSpiritCrystalInstance.class), 13, 3).clone(item);
		case 2522: // 흑정령의 수정 (무빙 악셀레이션)
			return ItemDarkSpiritCrystalInstance.clone(getPool(ItemDarkSpiritCrystalInstance.class), 13, 4).clone(item);
		case 2523: // 흑정령의 수정 (버닝 스피릿츠)
			return ItemDarkSpiritCrystalInstance.clone(getPool(ItemDarkSpiritCrystalInstance.class), 13, 5).clone(item);
		case 2524: // 흑정령의 수정 (다크 블라인드)
			return ItemDarkSpiritCrystalInstance.clone(getPool(ItemDarkSpiritCrystalInstance.class), 13, 6).clone(item);
		case 2525: // 흑정령의 수정 (베놈 레지스트)
			return ItemDarkSpiritCrystalInstance.clone(getPool(ItemDarkSpiritCrystalInstance.class), 13, 7).clone(item);
		case 2526: // 흑정령의 수정 (더블 브레이크)
			return ItemDarkSpiritCrystalInstance.clone(getPool(ItemDarkSpiritCrystalInstance.class), 14, 0).clone(item);
		case 2527: // 흑정령의 수정 (언케니 닷지)
			return ItemDarkSpiritCrystalInstance.clone(getPool(ItemDarkSpiritCrystalInstance.class), 14, 1).clone(item);
		case 2528: // 흑정령의 수정 (쉐도우 팽)
			return ItemDarkSpiritCrystalInstance.clone(getPool(ItemDarkSpiritCrystalInstance.class), 14, 2).clone(item);
		case 2529: // 흑정령의 수정 (파이널 번)
			return ItemDarkSpiritCrystalInstance.clone(getPool(ItemDarkSpiritCrystalInstance.class), 14, 3).clone(item);
		case 2810: // 진화의 열매
			return EvolutionFruit.clone(getPool(EvolutionFruit.class)).clone(item);
		case 3172: // 흑정령의 수정 (드레스 마이티)
			return ItemDarkSpiritCrystalInstance.clone(getPool(ItemDarkSpiritCrystalInstance.class), 14, 4).clone(item);
		case 3173: // 흑정령의 수정 (드레스 덱스터리티)
			return ItemDarkSpiritCrystalInstance.clone(getPool(ItemDarkSpiritCrystalInstance.class), 14, 5).clone(item);
		case 3174: // 흑정령의 수정 (드레스 이베이젼)
			return ItemDarkSpiritCrystalInstance.clone(getPool(ItemDarkSpiritCrystalInstance.class), 14, 6).clone(item);
		case 3303: // 말하는 섬 마을 귀환 주문서
			return ScrollReturnTalkingIslandVillage.clone(getPool(ScrollReturnTalkingIslandVillage.class)).clone(item);
		case 3304: // 글루딘 마을 귀환 주문서
			return ScrollReturnGludinTown.clone(getPool(ScrollReturnGludinTown.class)).clone(item);
		case 3305: // 켄트 마을 귀환 주문서
			return ScrollReturnKentVillage.clone(getPool(ScrollReturnKentVillage.class)).clone(item);
		case 3306: // 우드벡 마을 귀환 주문서
			return ScrollReturnWoodbecVillage.clone(getPool(ScrollReturnWoodbecVillage.class)).clone(item);
		case 3307: // 화전민 마을 귀환 주문서
			return ScrollReturnOrctown.clone(getPool(ScrollReturnOrctown.class)).clone(item);
		case 3308: // 요정숲 귀환 주문서
			return ScrollReturnElvenForest.clone(getPool(ScrollReturnElvenForest.class)).clone(item);
		case 3309: // 은기사 마을 귀환 주문서
			return ScrollReturnSilverKnightTown.clone(getPool(ScrollReturnSilverKnightTown.class)).clone(item);
		case 3310: // 기란 마을 귀환 주문서
			return ScrollReturnGiranCity.clone(getPool(ScrollReturnGiranCity.class)).clone(item);
		case 3311: // 하이네 마을 귀환 주문서
			return ScrollReturnHeineCity.clone(getPool(ScrollReturnHeineCity.class)).clone(item);
		case 3312: // 오렌 마을 귀환 주문서
			return ScrollReturnIvoryTowerTown.clone(getPool(ScrollReturnIvoryTowerTown.class)).clone(item);
		case 3313: // 웰던 마을 귀환 주문서
			return ScrollReturnWerldernTown.clone(getPool(ScrollReturnWerldernTown.class)).clone(item);
		case 3314: // 아덴 마을 귀환 주문서
			return ScrollReturnAdenCity.clone(getPool(ScrollReturnAdenCity.class)).clone(item);
		case 3315: // 침묵의 동굴 귀환 주문서
			return ScrollReturnSilentCavern.clone(getPool(ScrollReturnSilentCavern.class)).clone(item);
		case 3521: // 정령의 결정
			return 정령의결정.clone(getPool(정령의결정.class)).clone(item);
//			case 3546:	// 저항군 마을 귀환 주문서
//				return ScrollReturnResistanceVillage.clone( getPool(ScrollReturnResistanceVillage.class) ).clone(item);
//			case 3547:	// 은둔자 마을 귀환 주문서
//				return ScrollReturnRecluseVillage.clone( getPool(ScrollReturnRecluseVillage.class) ).clone(item);
		case 3369: // 연금술사의 돌
			return AlchemistStone.clone(getPool(AlchemistStone.class)).clone(item);
		/*
		 * case 3879: // 명법군의 징표함 return 명법군의징표함.clone( getPool(명법군의징표함.class)
		 * ).clone(item); case 3881: // 마령군의 징표함 return 마령군의징표함.clone(
		 * getPool(마령군의징표함.class) ).clone(item); case 3883: // 마수군의 징표함 return
		 * 마수군의징표함.clone( getPool(마수군의징표함.class) ).clone(item); case 3885: // 암살군의 징표함
		 * return 암살군의징표함.clone( getPool(암살군의징표함.class) ).clone(item);
		 */
		case 3945: // 그림자 신전 2층 열쇠
			return 그림자신전2층열쇠.clone(getPool(그림자신전2층열쇠.class)).clone(item);
		case 3946: // 그림자 신전 3층 열쇠
			return 그림자신전3층열쇠.clone(getPool(그림자신전3층열쇠.class)).clone(item);
		case 4009: // 흑마법 가루
			return 흑마법가루.clone(getPool(흑마법가루.class)).clone(item);
		case 5725: // 바람의 무기 강화 주문서
		case 5726: // 대지의 무기 강화 주문서
		case 5727: // 물의 무기 강화 주문서
		case 5728: // 불의 무기 강화 주문서
			return ScrollOfEnchantElementalWeapon.clone(getPool(ScrollOfEnchantElementalWeapon.class)).clone(item);
		case 5163: // 기마용 투구
			return Turban.clone(getPool(Turban.class)).clone(item);
		case 5557: // 창천의 가죽 갑옷
		case 5562: // 창천의 두건
		case 5558: // 창천의 로브
		case 5559: // 창천의 망토
		case 5560: // 창천의 부츠
		case 5561: // 창천의 장갑
		case 5556: // 창천의 판금 갑옷
			return 창천갑옷.clone(getPool(창천갑옷.class)).clone(item);
		case 5125: // 창천의 지팡이
		case 5121: // 창천의 창
		case 5124: // 창천의 크로우
		case 5117: // 창천의 검
		case 5120: // 창천의 활
		case 5123: // 창천의 이도류
		case 5122: // 창천의 단검
		case 5118: // 창천의 대검
		case 5126: // 창천의 건틀렛
		case 5119: // 창천의 도끼
			return 창천무기.clone(getPool(창천무기.class)).clone(item);
		case 5879: // 샤르나의 변신 주문서 (레벨 40)
		case 5880: // 샤르나의 변신 주문서 (레벨 52)
		case 5881: // 샤르나의 변신 주문서 (레벨 55)
			return 샤르나의변신주문서.clone(getPool(샤르나의변신주문서.class)).clone(item);
		case 4007: // 리덕션아머
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 1, 2).clone(item); // 11 7
		case 4713: // 카운터 베리어
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 1, 1).clone(item); // 12 2
		case 4712: // 솔리드케리지
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 12, 1).clone(item);
		case 3259: // 쇼크스턴
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 11, 6).clone(item); // 11 6
		case 4008: // 바운스어택
			return ItemBookInstance.clone(getPool(ItemBookInstance.class), 12, 0).clone(item);// 12 0
		case 1937: // 소환 조종 반지
			return RingSummonControl.clone(getPool(RingSummonControl.class)).clone(item);
		case 4922: // 장작
			return MagicFirewood.clone(getPool(MagicFirewood.class)).clone(item);
		case 4923: // 요리책 : 1단계
		case 4924:
		case 4925:
		case 4926:
		case 4927: // 요리책 : 5단계
			return CookBook.clone(getPool(CookBook.class)).clone(item);
		case 4932: // 괴물눈 스테이크
		case 5218: // 곰고기 구이
		case 4929: // 씨호떡
		case 5219: // 개미다리 치즈구이
		case 4930: // 과일 샐러드
		case 4931: // 과일 탕수육
		case 5220: // 멧돼지 꼬치 구이
		case 5221: // 버섯 스프
		case 49284932: // 환상의 괴물눈 스테이크
		case 49285218: // 환상의 곰고기 구이
		case 49284929: // 환상의 씨호떡
		case 49285219: // 환상의 개미다리 치즈구이
		case 49284930: // 환상의 과일 샐러드
		case 49284931: // 환상의 과일 탕수육
		case 49285220: // 환상의 멧돼지 꼬치 구이
		case 49285221: // 환상의 버섯 스프
			return Cook.clone(getPool(Cook.class)).clone(item);
		case 3052: // 카리프의 주머니
		case 3184: // 카리프의 고급 주머니
			return CaliphPouch.clone(getPool(CaliphPouch.class)).clone(item);
		case 5246: // 길고 무거운 낚싯대
			return FishingPole.clone(getPool(FishingPole.class)).clone(item);
		case 3763: // 혼돈의 머리카락
		case 3764: // 죽음의 머리카락
			return ShadowShrine.clone(getPool(ShadowShrine.class)).clone(item);
		case 5307: // 승자의 열매
			return null;
		case 5589: //
			return 회상의촛불.clone(getPool(회상의촛불.class)).clone(item);
		case 7865: // 안타라스의 완력
		case 7868: // 안타라스의 예지력
		case 7867: // 안타라스의 인내력
		case 7866: // 안타라스의 마력
			return AntharasArmor.clone(getPool(AntharasArmor.class)).clone(item);
		case 2212: // 리치로브
			return RichLobe.clone(getPool(RichLobe.class)).clone(item);
		case 2406:
			return RingZnis.clone(getPool(RingZnis.class)).clone(item);
		case 7869: // 파푸리온의 완력
		case 7870: // 파푸리온의 마력 
		case 7871: // 파푸리온의 인내력
		case 7872: // 파푸리온의 예지력 
			return FafurionArmor.clone( getPool(FafurionArmor.class)).clone(item);
		 
		case 7873: // 린드비오르의 완력
		case 7876: // 린드비오르의 예지력
		case 7875: // 린드비오르의 인내력
		case 7874: // 린드비오르의 마력
			return LindviorArmor.clone(getPool(LindviorArmor.class)).clone(item);
		case 7877: // 발라카스의 완력
		case 7880: // 발라카스의 예지력
		case 7879: // 발라카스의 인내력
		case 7878: // 발라카스의 마력
			return ValakasArmor.clone(getPool(ValakasArmor.class)).clone(item);
		case 11994: // 룸티스의 붉은빛 귀걸이
			return 룸티스의붉은빛귀걸이.clone(getPool(룸티스의붉은빛귀걸이.class)).clone(item);
		case 11995: // 룸티스의 푸른빛 귀걸이
			return 룸티스의푸른빛귀걸이.clone(getPool(룸티스의푸른빛귀걸이.class)).clone(item);
		case 11996: // 룸티스의 보랏빛 귀걸이
			return 룸티스의보랏빛귀걸이.clone(getPool(룸티스의보랏빛귀걸이.class)).clone(item);
		case 11997: // 룸티스의 강화 주문서
			return 룸티스의강화주문서.clone(getPool(룸티스의강화주문서.class)).clone(item);
		case 11998: // 룸티스의 붉은빛 귀걸이상자
			return 룸티스의붉은빛귀걸이상자.clone(getPool(룸티스의붉은빛귀걸이상자.class)).clone(item);
		case 11999: // 룸티스의 푸른빛 귀걸이상자
			return 룸티스의푸른빛귀걸이상자.clone(getPool(룸티스의푸른빛귀걸이상자.class)).clone(item);
		case 12000: // 룸티스의 보랏빛 귀걸이상자
			return 룸티스의보랏빛귀걸이상자.clone(getPool(룸티스의보랏빛귀걸이상자.class)).clone(item);
		case 5260: // 초록 빛 나는 물고기
			return 초록빛나는물고기.clone(getPool(초록빛나는물고기.class)).clone(item);
		case 5259: // 붉은 빛 나는 물고기
			return 붉은빛나는물고기.clone(getPool(붉은빛나는물고기.class)).clone(item);
		case 5261: // 파란 빛 나는 물고기
			return 파란빛나는물고기.clone(getPool(파란빛나는물고기.class)).clone(item);
		case 5262: // 흰 빛 나는 물고기
			return 흰빛나는물고기.clone(getPool(흰빛나는물고기.class)).clone(item);
		case 10704: // 체력의 가더
			return 체력의가더.clone(getPool(체력의가더.class)).clone(item);
		case 10705: // 수호의 가더
			return 수호의가더.clone(getPool(수호의가더.class)).clone(item);
		case 10706: // 마법사의 가더
			return 마법사의가더.clone(getPool(마법사의가더.class)).clone(item);
		case 13782: // 반역자의 방패
			return 반역자의방패.clone(getPool(반역자의방패.class)).clone(item);
		case 25460: // 변신 지배 반지
			return ScrollPolymorph4.clone(getPool(ScrollPolymorph4.class)).clone(item);
		case 15276: // 순백의 티 인장:HOLD/DEX
		case 15277: // 순백의 티 인장:HOLD/INT
		case 15275: // 순백의 티 인장:HOLD/STR
		case 15270: // 순백의 티 인장:MR/DEX
		case 15271: // 순백의 티 인장:MR/INT
		case 15269: // 순백의 티 인장:MR/STR
		case 15273: // 순백의 티 인장:STUN/DEX
		case 15274: // 순백의 티 인장:STUN/INT
		case 15272: // 순백의 티 인장:STUN/STR
			return 순백의티인장.clone(getPool(순백의티인장.class)).clone(item);
		case 15296: // 천연비누:순백의티
			return 순백의티_천연비누.clone(getPool(순백의티_천연비누.class)).clone(item);
		case 16798: // 용의 호박 갑옷
			return 용의호박갑옷.clone(getPool(용의호박갑옷.class)).clone(item);
		case 16895: // 스냅퍼의 반지 강화 주문서
			return 스냅퍼의반지강화주문서.clone(getPool(스냅퍼의반지강화주문서.class)).clone(item);
		case 18754: // 오림의 장신구 마법주문서
			return 오림의장신구마법주문서.clone(getPool(오림의장신구마법주문서.class)).clone(item);
		case 16906: // 스냅퍼의 마나 반지 상자
			return 스냅퍼의마나반지상자.clone(getPool(스냅퍼의마나반지상자.class)).clone(item);
		case 16904: // 스냅퍼의 마법저항 반지 상자
			return 스냅퍼의마법저항반지상자.clone(getPool(스냅퍼의마법저항반지상자.class)).clone(item);
		case 16909: // 스냅퍼의 용사 반지 상자
			return 스냅퍼의용사반지상자.clone(getPool(스냅퍼의용사반지상자.class)).clone(item);
		case 16908: // 스냅퍼의 지혜 반지 상자
			return 스냅퍼의지혜반지상자.clone(getPool(스냅퍼의지혜반지상자.class)).clone(item);
		case 16905: // 스냅퍼의 집중 반지 상자
			return 스냅퍼의집중반지상자.clone(getPool(스냅퍼의집중반지상자.class)).clone(item);
		case 16903: // 스냅퍼의 체력 반지 상자
			return 스냅퍼의체력반지상자.clone(getPool(스냅퍼의체력반지상자.class)).clone(item);
		case 16907: // 스냅퍼의 회복 반지 상자
			return 스냅퍼의회복반지상자.clone(getPool(스냅퍼의회복반지상자.class)).clone(item);
		case 16899: // 스냅퍼의 마나 반지
			return 스냅퍼의마나반지.clone(getPool(스냅퍼의마나반지.class)).clone(item);
		case 16897: // 스냅퍼의 마법 저항 반지
			return 스냅퍼의마법저항반지.clone(getPool(스냅퍼의마법저항반지.class)).clone(item);
		case 16902: // 스냅퍼의 용사 반지
			return 스냅퍼의용사반지.clone(getPool(스냅퍼의용사반지.class)).clone(item);
		case 16901: // 스냅퍼의 지혜 반지
			return 스냅퍼의지혜반지.clone(getPool(스냅퍼의지혜반지.class)).clone(item);
		case 16898: // 스냅퍼의 집중 반지
			return 스냅퍼의집중반지.clone(getPool(스냅퍼의집중반지.class)).clone(item);
		case 16896: // 스냅퍼의 체력 반지
			return 스냅퍼의체력반지.clone(getPool(스냅퍼의체력반지.class)).clone(item);
		case 16900: // 스냅퍼의 회복 반지
			return 스냅퍼의회복반지.clone(getPool(스냅퍼의회복반지.class)).clone(item);
		case 5017: // 로빈후드의 소개장
			return 로빈후드의소개장.clone(getPool(로빈후드의소개장.class)).clone(item);
		case 4358: // 신성한 에바의 물
			return 신성한에바의물.clone(getPool(신성한에바의물.class)).clone(item);
		case 3493: // 무한의 화살통
			return 무한의화살통.clone(getPool(무한의화살통.class)).clone(item);
		case 10197:
		case 10200:
			return BowOfSin.clone(getPool(BowOfSin.class)).clone(item);
		// 마법주사위2~6
		case 2719:
			return Gambledice2.clone(getPool(Gambledice2.class)).clone(item);
		case 2720:
			return Gambledice3.clone(getPool(Gambledice3.class)).clone(item);
		case 2721:
			return Gambledice4.clone(getPool(Gambledice4.class)).clone(item);
		case 2722:
			return Gambledice6.clone(getPool(Gambledice6.class)).clone(item);
		case 5956: // 상급 오시리스의 보물상자 조각(하)
		case 5717: // 하급 오시리스의 보물상자 조각(하)
				return 테베티칼의보물상자조각.clone(getPool(테베티칼의보물상자조각.class)).clone(item);
		case 5715: // 균열의 핵
				return 균열의핵.clone(getPool(균열의핵.class)).clone(item);
		case 5909: // 열린 하급 오시리스의 보물상자
				return 열린하급오시리스의보물상자.clone(getPool(열린하급오시리스의보물상자.class)).clone(item);
		case 5966: // 열린 상급 오시리스의 보물상자
				return 열린상급오시리스의보물상자.clone(getPool(열린상급오시리스의보물상자.class)).clone(item);
		default:
			if (item.getNameId().equalsIgnoreCase("변신 조종 반지")) {
				return RingPolyControl.clone(getPool(RingPolyControl.class)).clone(item);
			} else if (item.getType1().equalsIgnoreCase("weapon")) {
				if (item.getType2().equalsIgnoreCase("edoryu")) {
					return Edoryu.clone(getPool(Edoryu.class)).clone(item);
				} else if (item.getType2().equalsIgnoreCase("claw")) {
					return Claw.clone(getPool(Claw.class)).clone(item);
				} else if (item.getNameId().equalsIgnoreCase("앨리스")) {
					return 디케이무기.clone(getPool(디케이무기.class)).clone(item);
				} else {
					return ItemWeaponInstance.clone(getPool(ItemWeaponInstance.class)).clone(item);
				}
			} else if (item.getType1().equalsIgnoreCase("armor")) {
				if (item.getNameId().equalsIgnoreCase("룸티스의 검은빛 귀걸이")) {
					return 룸티스의검은빛귀걸이.clone(getPool(룸티스의검은빛귀걸이.class)).clone(item);
				}
				if (item.getNameId().equalsIgnoreCase("마물의 망토")) {
					return 마물의망토.clone(getPool(마물의망토.class)).clone(item);
				}
				if (item.getNameId().equalsIgnoreCase("마물의 부츠")) {
					return 마물의부츠.clone(getPool(마물의부츠.class)).clone(item);
				}
				if (item.getNameId().equalsIgnoreCase("마물의 장갑")) {
					return 마물의장갑.clone(getPool(마물의장갑.class)).clone(item);
				}
				/* 디아블로템 */
				if (item.getType2().equalsIgnoreCase("luck"))
					return LuckArmor.clone(getPool(LuckArmor.class)).clone(item);
				if (item.getType2().equalsIgnoreCase("aden"))
					return AdenArmor.clone(getPool(AdenArmor.class)).clone(item);
				if (item.getType2().equalsIgnoreCase("gid"))
					return Gid.clone(getPool(Gid.class)).clone(item);
				if (item.getType2().equalsIgnoreCase("ani"))
					return Ani.clone(getPool(Ani.class)).clone(item);
				if (item.getType2().equalsIgnoreCase("hellf"))
					return Hellf.clone(getPool(Hellf.class)).clone(item);
				
				
				/*
				 * if(item.getType2().equalsIgnoreCase("ring")) return
				 * Ring.clone(getPool(Ring.class)).clone(item);
				 * if(item.getType2().equalsIgnoreCase("belt")) return
				 * Belt.clone(getPool(Belt.class)).clone(item);
				 * if(item.getType2().equalsIgnoreCase("earring")) return
				 * Earring.clone(getPool(Earring.class)).clone(item);
				 * if(item.getType2().equalsIgnoreCase("necklace")) return
				 * Earring2.clone(getPool(Earring2.class)).clone(item);
				 */
				/* 장비관련 추가 */
				/*
				 * if(item.getType2().equalsIgnoreCase("helm")) return
				 * Helm.clone(getPool(Helm.class)).clone(item);
				 * if(item.getType2().equalsIgnoreCase("t")) return
				 * T.clone(getPool(T.class)).clone(item);
				 * if(item.getType2().equalsIgnoreCase("cloak")) return
				 * Clock.clone(getPool(Clock.class)).clone(item);
				 * if(item.getType2().equalsIgnoreCase("glove")) return
				 * Glove.clone(getPool(Glove.class)).clone(item);
				 * if(item.getType2().equalsIgnoreCase("boot")) return
				 * Boot.clone(getPool(Boot.class)).clone(item);
				 * if(item.getType2().equalsIgnoreCase("shield")) return
				 * Shild.clone(getPool(Shild.class)).clone(item);
				 */
				if (item.getNameId().equalsIgnoreCase("아이스 블링크"))
					return 아이스블링크.clone(getPool(아이스블링크.class)).clone(item);

				return ItemArmorInstance.clone(getPool(ItemArmorInstance.class)).clone(item);

			} else if (item.getType1().equalsIgnoreCase("petWeapon")) {
				return PetWeapon.clone(getPool(PetWeapon.class)).clone(item);
			} else if (item.getType1().equalsIgnoreCase("petArmor")) {
				return PetArmor.clone(getPool(PetArmor.class)).clone(item);
			}
			break;
		}
		return newDefaultItem(item);
	}

	/**
	 * 아이템 생성처리 함수. : 관리를 위해 함수 따로 뺌. : item타입에 type2값에 의한 생성처리.
	 * 
	 * @param item
	 * @return
	 */
	public static ItemInstance newDefaultItem(Item item) {
		if (item.getType2().equalsIgnoreCase("shield")) {
			return ItemArmorInstance.clone(getPool(ItemArmorInstance.class)).clone(item);

		} else if (item.getType2().equalsIgnoreCase("bravery potion")) {
			// 용기 물약
			return BraveryPotion.clone(getPool(BraveryPotion.class)).clone(item);

		} else if (item.getType2().equalsIgnoreCase("타켓 지정 막대")) {
			// 트루타겟막대
			return EbonyWand2.clone(getPool(EbonyWand2.class)).clone(item);

		} else if (item.getNameId().equalsIgnoreCase("인형 용해제")) {
			return Solvent.clone(ItemDatabase.getPool(Solvent.class)).clone(item);

		} else if (item.getNameId().equalsIgnoreCase("신규 혈맹 가입주문서")) {
			return MetieceClanJoinScroll.clone(ItemDatabase.getPool(MetieceClanJoinScroll.class)).clone(item);

		} else if (item.getName().equalsIgnoreCase("랜덤인챈무기상자") || item.getName().equalsIgnoreCase("랜덤인챈방어구상자")) {
			return RandomEnchantBox.clone(getPool(RandomEnchantBox.class)).clone(item);

		} else if (item.getName().equalsIgnoreCase("인챈트 교환 막대")) {
			return CreateWand.clone(getPool(CreateWand.class)).clone(item);
			
		} else if (item.getName().equalsIgnoreCase("반지 강화 주문서")) {
			return 반지강화주문서.clone(getPool(반지강화주문서.class)).clone(item);

		} else if (item.getType2().equalsIgnoreCase("신규레벨지원")) {
			// 캐릭터 성별 변경 주문서
			return Exp_support2.clone(getPool(Exp_support2.class)).clone(item);

		} else if (item.getType2().equalsIgnoreCase("인첸트 복구 주문서")) {
			// 인첸트 복구 주문서
			return 인첸트복구주문서.clone(getPool(인첸트복구주문서.class)).clone(item);
			
		} else if (item.getType2().equalsIgnoreCase("봉인 주문서")) {
			//봉인 주문서
			return SealedScroll.clone(getPool(SealedScroll.class)).clone(item);
			
		} else if (item.getType2().equalsIgnoreCase("봉인 해제 주문서")) {
			//봉인 해제 주문서
			return SealedCancelScroll.clone(getPool(SealedCancelScroll.class)).clone(item);
			
		} else if (item.getType2().equalsIgnoreCase("character_marbles")) {
			return CharacterSaveMarbles.clone(getPool(CharacterSaveMarbles.class)).clone(item);

		} else if (item.getType2().equalsIgnoreCase("텔레포트 막대")) {
			// 텔레포트 막대
			return TeleportWand.clone(getPool(TeleportWand.class)).clone(item);

		} else if (item.getName().equalsIgnoreCase("스톰 워크")) {
			return StormWalkItem.clone(getPool(StormWalkItem.class)).clone(item);

		} else if (item.getType2().equalsIgnoreCase("scroll_levelup")) {
			// 레벨업 주문서
			return LevelUpScroll.clone(getPool(LevelUpScroll.class)).clone(item);

		} else if (item.getType2().equalsIgnoreCase("sword_lack")) {
			// 칼렉풀기
			return Sword_lack.clone(getPool(Sword_lack.class)).clone(item);

		} else if (item.getType2().equalsIgnoreCase("scroll_leveldown")) {
			// 레벨다운 주문서
			return LevelDownScroll.clone(getPool(LevelDownScroll.class)).clone(item);

		} else if (item.getType2().equalsIgnoreCase("제거막대")) {
			// 아이템 제거막대
			return Item_Remove_Wand.clone(getPool(Item_Remove_Wand.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("memory_ball")) {
			// 희미한 기억의 구슬
			return 희미한기억의구슬.clone(ItemDatabase.getPool(희미한기억의구슬.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("dungeon book")) {
			return 던전북.clone(getPool(던전북.class)).clone(item);
			
		} else if (item.getType2().equalsIgnoreCase("oman book")) {
			return 환상지배부적.clone(getPool(환상지배부적.class)).clone(item);
			
		} else if (item.getType2().equalsIgnoreCase("$2380")) {
			return ScrollLabeledVerrYedHoraePledgeHouse.clone(getPool(ScrollLabeledVerrYedHoraePledgeHouse.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("firework")) {
			// 폭죽
			return Firework.clone(getPool(Firework.class)).clone(item);
		} else if (item.getType2().startsWith("spell_potion_")) {
			// 마법 포션
			return SpellPotion.clone(getPool(SpellPotion.class)).clone(item);

		} else if (item.getType2().equalsIgnoreCase("change_sex")) {
			// 성별 전환 아이템
			return ChangeSexPotion.clone(getPool(ChangeSexPotion.class)).clone(item);
			
		} else if (item.getType2().equalsIgnoreCase("신규인형상자")) {
			// 신규인형상자
			return 신규인형상자.clone(getPool(신규인형상자.class)).clone(item);

		} else if (item.getType2().equalsIgnoreCase("bundle")) {
			// 번들 아이템
			return Bundle.clone(getPool(Bundle.class)).clone(item);

		} else if (item.getType2().equalsIgnoreCase("chance_bundle")) {
			// 확률 번들 아이템
			return ChanceBundle.clone(getPool(ChanceBundle.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("Abcoin")) {
			return AllBundleCoin.clone(getPool(AllBundleCoin.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("MagicBundle")) {
			return MagicBundle.clone(getPool(MagicBundle.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("UnicBundle")) {
			return UnicBundle.clone(getPool(UnicBundle.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("MagBundle")) {
			return MagBundle.clone(getPool(MagBundle.class)).clone(item);

		} else if (item.getType2().startsWith("teleport_")) {
			// 이동 주문서
			return ScrollTeleport.clone(getPool(ScrollTeleport.class)).clone(item);

		} else if (item.getType2().startsWith("TOIteleport_")) {
			// 부적개념에 이동 주문서
			return ScrollTOITeleport.clone(getPool(ScrollTOITeleport.class)).clone(item);

		} else if (item.getType2().equalsIgnoreCase("dog collar")) {
			// 펫 목걸이
			return DogCollar.clone(getPool(DogCollar.class)).clone(item);

		} else if (item.getType2().equalsIgnoreCase("dog whistle")) {
			// 펫 호루라기
			return PetWhistle.clone(getPool(PetWhistle.class)).clone(item);

		} else if (item.getType2().startsWith("healing potion")) {
			// 체력 회복 물약
			return HealingPotion.clone(getPool(HealingPotion.class)).clone(item);

		} else if (item.getType2().equalsIgnoreCase("item_swap")) {
			// 장비 스왑
			return ItemSwap.clone(getPool(ItemSwap.class)).clone(item);

		} else if (item.getType2().startsWith("haste potion")) {
			// 초록 물약
			return HastePotion.clone(getPool(HastePotion.class)).clone(item);

		} else if (item.getType2().startsWith("exp_marble")) {

			return Exp_marble.clone(getPool(Exp_marble.class)).clone(item);

		} else if (item.getType2().startsWith("exp2_marble")) {

			return Exp_marble2.clone(getPool(Exp_marble2.class)).clone(item);

		} else if (item.getType2().equalsIgnoreCase("pp")) {
			return PPotion.clone(getPool(PPotion.class)).clone(item);

		} else if (item.getType2().startsWith("blue potion")) {
			// 파란 물약
			return BluePotion.clone(getPool(BluePotion.class)).clone(item);
			
		} else if (item.getType2().startsWith("wisdoma potion")) {
			// 상아탑 지혜물약
			return WisdomPotion.clone(getPool(WisdomPotion.class)).clone(item);
			
		} else if (item.getType2().startsWith("elven wafer")) {
			// 엘븐와퍼
			return ElvenWafer.clone(getPool(ElvenWafer.class)).clone(item);

		} else if (item.getType2().equalsIgnoreCase("exp restore power")) {
			// 경험치 100프로 복구물약
			return ExpReStorePowerPotion.clone(getPool(ExpReStorePowerPotion.class)).clone(item);

		} else if (item.getType2().startsWith("verr yed horae")) {
			// 귀환 주문서
			return ScrollLabeledVerrYedHorae.clone(getPool(ScrollLabeledVerrYedHorae.class)).clone(item);

		} else if (item.getType2().equalsIgnoreCase("자동 물약")) {
			// 자동 물약
			return AutoPotion.clone(getPool(AutoPotion.class)).clone(item);		
			
		} else if (item.getType2().equalsIgnoreCase("몹정리")) {
			// 몹 정리
			return monsterClean.clone(getPool(monsterClean.class)).clone(item);
			
		} else if (item.getType2().startsWith("bbbbb")) {
			// 힘기사 변경물약
			return ResetStatusScroll2.clone(getPool(ResetStatusScroll2.class)).clone(item);

		} else if (item.getType2().startsWith("ccccc")) {
			// 콘기사 변경
			return ResetStatusScroll3.clone(getPool(ResetStatusScroll3.class)).clone(item);
		} else if (item.getType2().startsWith("ddddd")) {
			// 덱요정
			return ResetStatusScroll4.clone(getPool(ResetStatusScroll4.class)).clone(item);
		} else if (item.getType2().startsWith("eeeee")) {
			// 콘요정
			return ResetStatusScroll5.clone(getPool(ResetStatusScroll5.class)).clone(item);
		} else if (item.getType2().startsWith("fffff")) {
			// 힘기사 변경
			return ResetStatusScroll6.clone(getPool(ResetStatusScroll6.class)).clone(item);
		} else if (item.getType2().startsWith("ggggg")) {
			// 힘기사 변경물약
			return ResetStatusScroll7.clone(getPool(ResetStatusScroll7.class)).clone(item);
		} else if (item.getType2().startsWith("hhhhh")) {
			// 힘기사 변경물약
			return ResetStatusScroll8.clone(getPool(ResetStatusScroll8.class)).clone(item);
		} else if (item.getType2().startsWith("iiiii")) {
			// 힘기사 변경물약
			return ResetStatusScroll9.clone(getPool(ResetStatusScroll9.class)).clone(item);
		} else if (item.getType2().startsWith("jjjjj")) {
			// 힘기사 변경물약
			return ResetStatusScroll10.clone(getPool(ResetStatusScroll10.class)).clone(item);
		} else if (item.getType2().startsWith("kkkkk")) {
			// 힘기사 변경물약
			return ResetStatusScroll11.clone(getPool(ResetStatusScroll11.class)).clone(item);
		} else if (item.getType2().startsWith("LLLLL")) {
			// 힘기사 변경물약
			return ResetStatusScroll12.clone(getPool(ResetStatusScroll12.class)).clone(item);

		} else if (item.getType2().startsWith("kernodwel")) {
			// 확인 주문서
			return ScrollLabeledKernodwel.clone(getPool(ScrollLabeledKernodwel.class)).clone(item);

		} else if (item.getType2().startsWith("polymorph")) {
			// 변신 주문서
			return ScrollPolymorph.clone(getPool(ScrollPolymorph.class)).clone(item);
			
		} else if (item.getType2().startsWith("venzar borgavve")) {
			// 순간이동 주문서
			return ScrollLabeledVenzarBorgavve.clone(getPool(ScrollLabeledVenzarBorgavve.class)).clone(item);
		} else if (item.getType2().startsWith("dungeon_")) {
			return ScrollDungeon.clone(getPool(ScrollDungeon.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("change_name")) {
			return ScrollChangeName.clone(getPool(ScrollChangeName.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("stat_clear")) {
			return StatClear.clone(getPool(StatClear.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("magic_doll")) {
			return MagicDoll.clone(getPool(MagicDoll.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("Mix")) {
			return Mix.clone(getPool(Mix.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("Mix2")) {
			return Mix2.clone(getPool(Mix2.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("Mix3")) {
			return Mix3.clone(getPool(Mix3.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("Mix4")) {
			return Mix4.clone(getPool(Mix4.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("Mix5")) {
			return Mix5.clone(getPool(Mix5.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("Mix6")) {
			return Mix6.clone(getPool(Mix6.class)).clone(item);

		} else if (item.getType2().equalsIgnoreCase("brroyal")) {
			return BraveryPotion.clone(getPool(BraveryPotion.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("maan")) {
			// 수/풍/지/화/탄생/형상/생명의 마안
			return BuffMaan.clone(getPool(BuffMaan.class)).clone(item);
			// ------------ 장길호님 요청 아이템
		} else if (item.getType2().startsWith("[giro] ")) {
			String key = item.getType2().substring(0, 6).trim();
			String value = item.getType2().substring(key.length()).trim();
			if (value.equalsIgnoreCase("전투강화")) {
				return 전투강화아이템.clone(getPool(전투강화아이템.class)).clone(item);
			} else if (value.equalsIgnoreCase("마력증강")) {
				return 마력증강아이템.clone(getPool(마력증강아이템.class)).clone(item);
			} else if (value.equalsIgnoreCase("체력증강")) {
				return 체력증강아이템.clone(getPool(체력증강아이템.class)).clone(item);
			} else if (value.equalsIgnoreCase("송편")) {
				return ManaPotion.clone(getPool(ManaPotion.class)).clone(item);
			} else if (value.equalsIgnoreCase("경험치")) {
				return 경험치아이템.clone(getPool(경험치아이템.class)).clone(item);

			} else {
				return ItemInstance.clone(getPool(ItemInstance.class)).clone(item);
			}

		// ------------ 그외~
		} else if (item.getType2().equalsIgnoreCase("DmgNomal")) {
			return KernodwelWeaponDmgNomal.clone(getPool(KernodwelWeaponDmgNomal.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("DmgRoyal")) {
			return KernodwelWeaponDmgRoyal.clone(getPool(KernodwelWeaponDmgRoyal.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("StatNomal")) {
			return KernodwelWeaponStatNomal.clone(getPool(KernodwelWeaponStatNomal.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("StatRoyal")) {
			return KernodwelWeaponStatRoyal.clone(getPool(KernodwelWeaponStatRoyal.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("HpmpNomal")) {
			return KernodwelWeaponHpMpNomal.clone(getPool(KernodwelWeaponHpMpNomal.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("HpmpRoyal")) {
			return KernodwelWeaponHpMpRoyal.clone(getPool(KernodwelWeaponHpMpRoyal.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("AStatNomal")) {
			return KernodwelArmorStatNomal.clone(getPool(KernodwelArmorStatNomal.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("AStatRoyal")) {
			return KernodwelArmorStatRoyal.clone(getPool(KernodwelArmorStatRoyal.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("AHpmpNomal")) {
			return KernodwelArmorHpMpNomal.clone(getPool(KernodwelArmorHpMpNomal.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("AHpmpRoyal")) {
			return KernodwelArmorHpMpRoyal.clone(getPool(KernodwelArmorHpMpRoyal.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("keanLab")) {
			return SuperScrollLabeledKernodwel.clone(getPool(SuperScrollLabeledKernodwel.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("boss")) {
			return OmanSoul.clone(getPool(OmanSoul.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("crobar")) {
			return ScrollCrobar.clone(getPool(ScrollCrobar.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("goldmon")) {
			return GoldMonster.clone(getPool(GoldMonster.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("unicroon")) {
			return ScrollRoon.clone(getPool(ScrollRoon.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("unicroonlast")) {
			return ScrollRoonLast.clone(getPool(ScrollRoonLast.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("unicroonreset")) {
			return ScrollRoonReset.clone(getPool(ScrollRoonReset.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("sskmu")) {
			return Sskmu.clone(getPool(Sskmu.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("exp rise")) {
			return ExpRisePotion.clone(getPool(ExpRisePotion.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("NPC 제거 막대")) {
			return NPC제거막대.clone(getPool(NPC제거막대.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("아이템 드랍 확인 막대")) {
			return 아이템드랍확인막대.clone(getPool(아이템드랍확인막대.class)).clone(item);
			// 로빈후드의 메모지1
		} else if (item.getType2().equalsIgnoreCase("로빈후드의메모지1")) {
			return 로빈후드의메모지1.clone(getPool(로빈후드의메모지1.class)).clone(item);
			// 로빈후드의 메모지2
		} else if (item.getType2().equalsIgnoreCase("로빈후드의메모지2")) {
			return 로빈후드의메모지2.clone(getPool(로빈후드의메모지2.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("1억원금괴")) {
			return Onea.clone(getPool(Onea.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("전투용버프물약")) {
			return 버프물약3.clone(getPool(버프물약3.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("무한버프물약")) {
			return 버프물약2.clone(getPool(버프물약2.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("버프물약")) {
			return 버프물약.clone(getPool(버프물약.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("무한 물약")) {
			return HealingPotion.clone(getPool(HealingPotion.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("INPblue")) {
			return GreateBluePotion2.clone(getPool(GreateBluePotion2.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("lawful potion")) {//
			return LawfulPotion.clone(getPool(LawfulPotion.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("mg2")) {
			return MagicDoll2.clone(getPool(MagicDoll2.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("mg3")) {
			return MagicDoll3.clone(getPool(MagicDoll3.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("mg4")) {
			return MagicDoll4.clone(getPool(MagicDoll4.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("Unic")) {
			return Unic.clone(getPool(Unic.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("jiarmor")) {
			return 장인의갑옷마법주문서.clone(getPool(장인의갑옷마법주문서.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("jiweapon")) {
			return 장인의무기마법주문서.clone(getPool(장인의무기마법주문서.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("GAVT")) {
			return ItemGrAvt.clone(getPool(ItemGrAvt.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("BAVT")) {
			return ItemBrAvt.clone(getPool(ItemBrAvt.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("속성삭제")) {
			return ScrollOfEnchantElementalWeaponRe.clone(getPool(ScrollOfEnchantElementalWeaponRe.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("SOF")) {
			return ItemSoulOfFlame.clone(getPool(ItemSoulOfFlame.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("rton")) {
			return ItemReturntona.clone(getPool(ItemReturntona.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("location scroll")) {
			return ScrollLocationReset.clone(getPool(ScrollLocationReset.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("장신구")) {
			return ScrollLabeledAccZelgoMer.clone(getPool(ScrollLabeledAccZelgoMer.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("reset status scroll")) {
			return ResetStatusScroll.clone(getPool(ResetStatusScroll.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("newel")) {
			return ElixirPotion2.clone(getPool(ElixirPotion2.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("reel")) {
			return ElixirPotion3.clone(getPool(ElixirPotion3.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("change bress")) {
			return ScrollChangeBress.clone(getPool(ScrollChangeBress.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("omanmix")) {
			return OmanMix.clone(getPool(OmanMix.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("omanmix2")) {
			return OmanMix2.clone(getPool(OmanMix2.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("moninfo")) {
			return MonsterInfoWand.clone(getPool(MonsterInfoWand.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("lastbook")) {
			return LaskBookMix.clone(getPool(LaskBookMix.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("lock")) {
			return ScrollCharLock.clone(getPool(ScrollCharLock.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("Unlock")) {
			return ScrollCharUnLock.clone(getPool(ScrollCharUnLock.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("자동 칼질")) {
			return 자동칼질.clone(getPool(자동칼질.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("orim")) {
			return 오림의장신구마법주문서.clone(getPool(오림의장신구마법주문서.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("ibs")) {
			return ItemBloodtoSoul.clone(getPool(ItemBloodtoSoul.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("ig")) {
			return ItemGloing.clone(getPool(ItemGloing.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("is")) {
			return ItemShing.clone(getPool(ItemShing.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("iks")) {
			return ItemShockStun.clone(getPool(ItemShockStun.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("itpl")) {
			return ItemTryple.clone(getPool(ItemTryple.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("accent")) {
			return 반지인첸트주문서.clone(getPool(반지인첸트주문서.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("생명의 나뭇잎")) {
			return LifeLost.clone(getPool(LifeLost.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("autoadd")) {
			return AddHunt.clone(getPool(AddHunt.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("kscroll")) {
			return ScrollLabeledVerrYedHoraePledgeHouse2.clone(getPool(ScrollLabeledVerrYedHoraePledgeHouse2.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("wscroll")) {
			return ScrollLabeledVerrYedHoraePledgeHouse3.clone(getPool(ScrollLabeledVerrYedHoraePledgeHouse3.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("oscroll")) {
			return ScrollLabeledVerrYedHoraePledgeHouse4.clone(getPool(ScrollLabeledVerrYedHoraePledgeHouse4.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("kar_weapon")) {
			return ScrollLabeledDaneFools.clone(getPool(ScrollLabeledDaneFools.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("kar_armor")) {
			return ScrollLabeledZelgoMer.clone(getPool(ScrollLabeledZelgoMer.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("셀프 시전 마법")) {
			return 셀프시전마법.clone(getPool(셀프시전마법.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("ScrollOfNewClanJoin")) {
			return ScrollOfNewClanJoin.clone(getPool(ScrollOfNewClanJoin.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("PvP_clean")) {
			return PvP_clean.clone(getPool(PvP_clean.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("PK_clean")) {
			return PK_clean.clone(getPool(PK_clean.class)).clone(item);
		} else if (item.getType2().equalsIgnoreCase("giran_time_scroll")) {
			return ScrollOfGiranDungeon.clone(getPool(ScrollOfGiranDungeon.class)).clone(item);
		} else {

			return ItemInstance.clone(getPool(ItemInstance.class)).clone(item);
		}
	}

	/**
	 * 아이템 객체 정보를 그대로 복사해서 객체 생성. object_id 새로 할당 해당 객체 리턴.
	 * 
	 * @param item
	 * @return
	 */
	static public ItemInstance newInstance(ItemInstance item) {
		if (item != null) {
			ItemInstance temp = newInstance(item.getItem());
			if (temp != null) {
				temp.setObjectId(ServerDatabase.nextItemObjId());
				temp.setDefinite(item.isDefinite());
				temp.setCount(item.getCount());
				temp.setBress(item.getBress());
				temp.setQuantity(item.getQuantity());
				temp.setEnLevel(item.getEnLevel());
				temp.setDurability(item.getDurability());
				temp.setDynamicMr(item.getDynamicMr());
				temp.setTime(item.getTime());
				temp.setNowTime(item.getNowTime());
				temp.setEquipped(item.isEquipped());
				temp.setTimeDrop(item.getTimeDrop());
				temp.setDynamicLight(item.getDynamicLight());
				temp.setDynamicAc(item.getDynamicAc());
				temp.setUsershopBuyPrice(item.getUsershopBuyPrice());
				temp.setUsershopSellPrice(item.getUsershopSellPrice());
				temp.setUsershopBuyCount(item.getUsershopBuyCount());
				temp.setUsershopSellCount(item.getUsershopSellCount());
				temp.setUsershopIdx(item.getUsershopIdx());
				temp.setSkill(item.getSkill());
				temp.setCharacter(item.getCharacter());

				// InnRoomKey
				temp.setInnRoomKey(item.getInnRoomKey());
				// RaceTicket
				temp.setRaceTicket(item.getRaceTicket());
				// DogCollar
				if (item instanceof DogCollar) {
					DogCollar dc = (DogCollar) item;
					DogCollar temp_dc = (DogCollar) temp;
					temp_dc.setPetObjectId(dc.getPetObjectId());
					temp_dc.setPetName(dc.getPetName());
					temp_dc.setPetClassId(dc.getPetClassId());
					temp_dc.setPetLevel(dc.getPetLevel());
					temp_dc.setPetHp(dc.getPetHp());
					temp_dc.setPetSpawn(dc.isPetSpawn());
					temp_dc.setPetDel(dc.isPetDel());
				}
				// Letter
				if (item instanceof Letter) {
					Letter l = (Letter) item;
					Letter temp_l = (Letter) temp;
					temp_l.setFrom(l.getFrom());
					temp_l.setTo(l.getTo());
					temp_l.setSubject(l.getSubject());
					temp_l.setMemo(l.getMemo());
					temp_l.setDate(l.getDate());
					temp_l.setLetterUid(l.getLetterUid());
				}

				return temp;
			}
		}
		return null;
	}

	static private void clearPool() {
		TimeLine.start("ItemDatabase 에서 Pool 초과로 메모리 정리 중..");

		// 풀 전체 제거.
		pool.clear();

		TimeLine.end();
	}

	/**
	 * 사용다된 아이템 풀에 다시 넣는 함수.
	 * 
	 * @param item
	 */
	static public void setPool(ItemInstance item) {
		item.close();
		if (!Lineage.memory_recycle)
			return;
		synchronized (pool) {
			List<ItemInstance> list = pool.get(item.getClass().toString());
			if (list == null) {
				list = new ArrayList<ItemInstance>();
				pool.put(item.getClass().toString(), list);
			}
			synchronized (list) {
				if (!list.contains(item)) {
					// class lineage.world.object.item.Aden
					list.add(item);
				} else {
					item = null;
					clearPool();
				}
			}
		}

//		lineage.share.System.println("append : "+pool.size());
	}

	/**
	 * 아이템 재사용을위해 풀에서 필요한 객체 찾아서 리턴.
	 * 
	 * @param c
	 * @return
	 */
	static public ItemInstance getPool(Class<?> c) {
		if (!Lineage.memory_recycle)
			return null;
		synchronized (pool) {
			return findPool(c);
		}
	}

	static private ItemInstance findPool(Class<?> c) {
		List<ItemInstance> list = pool.get(c.toString());
		if (list == null) {
			list = new ArrayList<ItemInstance>();
			pool.put(c.toString(), list);
		}
		if (list.size() > 0) {
			synchronized (list) {
				ItemInstance item = list.get(list.size() - 1);
				list.remove(list.size() - 1);
				return item;
			}
		}
		return null;
//		return list.size()>0 ? list.pop() : null;
	}

	static public int getMaterial(final String meterial) {
		if (meterial.equalsIgnoreCase("액체"))
			return 1;
		else if (meterial.equalsIgnoreCase("밀랍"))
			return 2;
		else if (meterial.equalsIgnoreCase("식물성"))
			return 3;
		else if (meterial.equalsIgnoreCase("동물성"))
			return 4;
		else if (meterial.equalsIgnoreCase("종이"))
			return 5;
		else if (meterial.equalsIgnoreCase("천"))
			return 6;
		else if (meterial.equalsIgnoreCase("가죽"))
			return 7;
		else if (meterial.equalsIgnoreCase("나무"))
			return 8;
		else if (meterial.equalsIgnoreCase("뼈"))
			return 9;
		else if (meterial.equalsIgnoreCase("용 비늘") || meterial.equalsIgnoreCase("용비늘"))
			return 10;
		else if (meterial.equalsIgnoreCase("철"))
			return 11;
		else if (meterial.equalsIgnoreCase("금속"))
			return 12;
		else if (meterial.equalsIgnoreCase("구리"))
			return 13;
		else if (meterial.equalsIgnoreCase("은"))
			return 14;
		else if (meterial.equalsIgnoreCase("금"))
			return 15;
		else if (meterial.equalsIgnoreCase("백금"))
			return 16;
		else if (meterial.equalsIgnoreCase("미스릴"))
			return 17;
		else if (meterial.equalsIgnoreCase("블랙 미스릴") || meterial.equalsIgnoreCase("블랙미스릴"))
			return 18;
		else if (meterial.equalsIgnoreCase("유리"))
			return 19;
		else if (meterial.equalsIgnoreCase("보석"))
			return 20;
		else if (meterial.equalsIgnoreCase("광석"))
			return 21;
		else if (meterial.equalsIgnoreCase("오리하루콘"))
			return 22;
		return 0;
	}

	static public int getWeaponAttackDistance(int gfxMode) {
		switch (gfxMode) {
		case 0x14:
		case 0x3E:
			return 10;
		case 0x18:
			return 2;
		default:
			return 1;
		}
	}

	static public int getWeaponGfx(final String type2) {
		if (type2.equalsIgnoreCase("sword"))
			return 0x04;
		else if (type2.equalsIgnoreCase("tohandsword") || type2.equalsIgnoreCase("twohandsword"))
			return Lineage.server_version > 230 ? 0x32 : 0x04;
		else if (type2.equalsIgnoreCase("axe"))
			return 0x0B;
		else if (type2.equalsIgnoreCase("bow"))
			return 0x14;
		else if (type2.equalsIgnoreCase("spear"))
			return 0x18;
		else if (type2.equalsIgnoreCase("wand"))
			return 0x28;
		else if (type2.equalsIgnoreCase("staff"))
			return 0x28;
		else if (type2.equalsIgnoreCase("dagger"))
			return Lineage.server_version > 230 ? 0x2E : 0x04;
		else if (type2.equalsIgnoreCase("blunt"))
			return 0x0B;
		else if (type2.equalsIgnoreCase("edoryu") || type2.equalsIgnoreCase("dualsword"))
			return 0x36;
		else if (type2.equalsIgnoreCase("claw"))
			return 0x3A;
		else if (type2.equalsIgnoreCase("throwingknife"))
			return 0x0B6A;
		else if (type2.equalsIgnoreCase("arrow") || type2.equalsIgnoreCase("etc"))
			return 0x42;
		else if (type2.equalsIgnoreCase("gauntlet"))
			return Lineage.server_version > 200 ? 0x3E : 0x0B;
		else if (type2.equalsIgnoreCase("chainsword"))
			return 0x18;
		else if (type2.equalsIgnoreCase("keyrink"))
			return 0x3a;
		else if (type2.equalsIgnoreCase("tohandblunt"))
			return 0x0B;
		else if (type2.equalsIgnoreCase("tohandstaff"))
			return 0x28;
		else if (type2.equalsIgnoreCase("tohandspear"))
			return 0x18;
		else if (type2.equalsIgnoreCase("fishing_rod"))
			return 0x1C;

		return 0x00;
	}

	static public int getSlot(final String type2) {
		if (type2.equalsIgnoreCase("helm"))
			return 1;
		else if (type2.equalsIgnoreCase("earring"))
			return 12;
		else if (type2.equalsIgnoreCase("necklace"))
			return 10;
		else if (type2.equalsIgnoreCase("t"))
			return 3;
		else if (type2.equalsIgnoreCase("armor"))
			return 2;
		else if (type2.equalsIgnoreCase("cloak"))
			return 4;
		else if (type2.equalsIgnoreCase("belt"))
			return 11;
		else if (type2.equalsIgnoreCase("glove"))
			return 6;
		else if (type2.equalsIgnoreCase("shield"))
			return 7;
		else if (type2.equalsIgnoreCase("boot"))
			return 5;
		else if (type2.equalsIgnoreCase("ring"))
			return 18;
		else if (type2.equalsIgnoreCase("sword"))
			return 8;
		else if (type2.equalsIgnoreCase("axe"))
			return 8;
		else if (type2.equalsIgnoreCase("bow"))
			return 8;
		else if (type2.equalsIgnoreCase("wand"))
			return 8;
		else if (type2.equalsIgnoreCase("tohandsword"))
			return 8;
		else if (type2.equalsIgnoreCase("spear"))
			return 8;
		else if (type2.equalsIgnoreCase("dagger"))
			return 8;
		else if (type2.equalsIgnoreCase("blunt"))
			return 8;
		else if (type2.equalsIgnoreCase("claw"))
			return 8;
		else if (type2.equalsIgnoreCase("edoryu"))
			return 8;
		else if (type2.equalsIgnoreCase("gauntlet"))
			return 8;
		else if (type2.equalsIgnoreCase("speer"))
			return 8;
		else if (type2.equalsIgnoreCase("staff"))
			return 8;
		else if (type2.equalsIgnoreCase("chainsword"))
			return 8;
		else if (type2.equalsIgnoreCase("keyrink"))
			return 8;
		else if (type2.equalsIgnoreCase("tohandblunt"))
			return 8;
		else if (type2.equalsIgnoreCase("tohandstaff"))
			return 8;
		else if (type2.equalsIgnoreCase("tohandspear"))
			return 8;
		else if (type2.equalsIgnoreCase("garder"))
			return 27;
		else if (type2.equalsIgnoreCase("pair"))
			return 5;
		else if (type2.equalsIgnoreCase("fishing_rod"))
			return 8;
		

		return 0;
	}

	static public int getEquippedSlot(final String type2) {
		if (type2.equalsIgnoreCase("armor"))
			return 2;
		else if (type2.equalsIgnoreCase("cloak"))
			return 10;
		else if (type2.equalsIgnoreCase("t"))
			return 18;
		else if (type2.equalsIgnoreCase("glove"))
			return 20;
		else if (type2.equalsIgnoreCase("boot"))
			return 21;
		else if (type2.equalsIgnoreCase("helm"))
			return 22;
		else if (type2.equalsIgnoreCase("ring"))
			return 23;
		else if (type2.equalsIgnoreCase("necklace"))
			return 24;
		else if (type2.equalsIgnoreCase("shield") || type2.equalsIgnoreCase("garder"))
			return 25;
		else if (type2.equalsIgnoreCase("belt"))
			return 37;
		else if (type2.equalsIgnoreCase("earring"))
			return 40;
		return 1;
	}

	static public int getSize() {
		return list.size();
	}

	static public int getPoolSize() {
		int size = 0;
		for (String key : pool.keySet())
			size += pool.get(key).size();
		return size;
	}

	static public List<Item> getList() {
		synchronized (list) {
			return new ArrayList<Item>(list);
		}
	}

	/**
	 * 방수가 존재하는 막대아이템인지 확인하는 메서드.
	 * 
	 * @param item
	 * @return
	 */
	static public boolean isQuantityWand(ItemInstance item) {
		if (item == null || item.getItem() == null)
			return false;
		if (item instanceof EbonyWand || item instanceof EbonyWand2 || item instanceof MapleWand
				|| item instanceof PineWand || item instanceof ExpulsionWand)
			return true;
		return false;
	}

	public static List<Item> getPotion() {
		return potion;
	}

	public static void setPotion(List<Item> potion) {
		ItemDatabase.potion = potion;
	}

	static public boolean isWeaponToHand(final Item item) {
		return isWeaponToHand(item.getType2());
	}

	static public boolean isWeaponToHand(final String type2) {
		switch (type2) {
		case "bow":
		case "tohandsword":
		case "twohandsword":
		case "spear":
		case "tohandblunt":
		case "tohandstaff":
		case "chainsword":
		case "dualsword":
		case "tohandedoryu":
		case "claw":
		case "edoryu":
			return true;
		}
		return false;
	}
}
