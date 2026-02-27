package lineage.database;

import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jsn_soft.AutoHunt;
import jsn_soft.기란성버프강화;
import jsn_soft.윈성버프강화;
import jsn_soft.켄트성버프강화;
import cholong.npc.개경주장입구;
import lineage.bean.database.Npc;
import lineage.bean.lineage.Clan;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.thread.AiThread;
import lineage.world.World;
import lineage.world.controller.ClanController;
import lineage.world.controller.CommandController;
import lineage.world.controller.RankController;
import lineage.world.object.object;
import lineage.world.object.instance.NpcInstance;
import lineage.world.object.instance.ShopInstance;
import lineage.world.object.npc.Alfons;
import lineage.world.object.npc.Asha;
import lineage.world.object.npc.AttendanceCheck;
import lineage.world.object.npc.BossTimer;
import lineage.world.object.npc.ClanLevelNpc;
import lineage.world.object.npc.ClanMaker;
import lineage.world.object.npc.Clan_lord;
import lineage.world.object.npc.Doett;
import lineage.world.object.npc.DollMakeNpc;
import lineage.world.object.npc.Ellyonne;
import lineage.world.object.npc.Eris;
import lineage.world.object.npc.EtcNpc;
import lineage.world.object.npc.Feaena;
import lineage.world.object.npc.Frim;
import lineage.world.object.npc.Giran_dungeon_Telepoter;
import lineage.world.object.npc.GoddessAgata;
import lineage.world.object.npc.Horun;
import lineage.world.object.npc.Hurin;
import lineage.world.object.npc.Kima;
import lineage.world.object.npc.KuberaQuest;
import lineage.world.object.npc.KuberaQuest2;
import lineage.world.object.npc.Kuper;
import lineage.world.object.npc.Maid;
import lineage.world.object.npc.Manless0;
import lineage.world.object.npc.Manless10;
import lineage.world.object.npc.Morien;
import lineage.world.object.npc.Moum;
import lineage.world.object.npc.PvP_Rank_bronze;
import lineage.world.object.npc.Rank_bronze;
import lineage.world.object.npc.RingOfSummon;
import lineage.world.object.npc.Sedia;
import lineage.world.object.npc.ServerQuestNpc;
import lineage.world.object.npc.Siris;
import lineage.world.object.npc.TalkMovingNpc;
import lineage.world.object.npc.TalkNpc;
import lineage.world.object.npc.Theodor;
import lineage.world.object.npc.Uhelp;
import lineage.world.object.npc.마을책;
import lineage.world.object.npc.유리스;
import lineage.world.object.npc.경험치지급단;
import lineage.world.object.npc.이동책;
import lineage.world.object.npc.이동책2;
import lineage.world.object.npc.반지교환사;
import lineage.world.object.npc.자동물약;
import lineage.world.object.npc.장비스왑;
import lineage.world.object.npc.테베게이트;
import lineage.world.object.npc.Promot_npc;
import lineage.world.object.npc.YoungGunter;
import lineage.world.object.npc.Manless1;
import lineage.world.object.npc.Manless2;
import lineage.world.object.npc.Manless3;
import lineage.world.object.npc.Manless4;
import lineage.world.object.npc.Manless5;
import lineage.world.object.npc.Manless6;
import lineage.world.object.npc.Manless7;
import lineage.world.object.npc.Manless8;
import lineage.world.object.npc.Manless9;
import lineage.world.object.npc.buff.ArmorEnchanter;
import lineage.world.object.npc.buff.Curer;
import lineage.world.object.npc.buff.Hadesty;
import lineage.world.object.npc.buff.Haste;
import lineage.world.object.npc.buff.Pidor;
import lineage.world.object.npc.buff.PolymorphMagician;
import lineage.world.object.npc.buff.WeaponEnchanter;
import lineage.world.object.npc.buff.강화마법사;
import lineage.world.object.npc.craft.Alchemist;
import lineage.world.object.npc.craft.Alice;
import lineage.world.object.npc.craft.Anton;
import lineage.world.object.npc.craft.Arachne;
import lineage.world.object.npc.craft.Bamute;
import lineage.world.object.npc.craft.CraftTable;
import lineage.world.object.npc.craft.Detecter;
import lineage.world.object.npc.craft.DwarfAdelio;
import lineage.world.object.npc.craft.Ent;
import lineage.world.object.npc.craft.Est;
import lineage.world.object.npc.craft.Eveurol;
import lineage.world.object.npc.craft.Fairy;
import lineage.world.object.npc.craft.FairyQueen;
import lineage.world.object.npc.craft.FakeBarlog;
import lineage.world.object.npc.craft.Hector;
import lineage.world.object.npc.craft.Herbert;
import lineage.world.object.npc.craft.Infamous;
import lineage.world.object.npc.craft.Ivelviin;
import lineage.world.object.npc.craft.Jason;
import lineage.world.object.npc.craft.Joel;
import lineage.world.object.npc.craft.Julie;
import lineage.world.object.npc.craft.Koup;
import lineage.world.object.npc.craft.Ladar;
import lineage.world.object.npc.craft.Lapyahee;
import lineage.world.object.npc.craft.Lavienue;
import lineage.world.object.npc.craft.Lsmith;
import lineage.world.object.npc.craft.Luudiel;
import lineage.world.object.npc.craft.Moria;
import lineage.world.object.npc.craft.Narhen;
import lineage.world.object.npc.craft.Nerupa;
import lineage.world.object.npc.craft.Nose;
import lineage.world.object.npc.craft.Pan;
import lineage.world.object.npc.craft.Parum;
import lineage.world.object.npc.craft.PielEmental;
import lineage.world.object.npc.craft.Pierce;
import lineage.world.object.npc.craft.Pin;
import lineage.world.object.npc.craft.Rafons;
import lineage.world.object.npc.craft.Raidar;
import lineage.world.object.npc.craft.Randal;
import lineage.world.object.npc.craft.Reona;
import lineage.world.object.npc.craft.Rodeny;
import lineage.world.object.npc.craft.Rushi;
import lineage.world.object.npc.craft.Ryumiel;
import lineage.world.object.npc.craft.Sarsha;
import lineage.world.object.npc.craft.Urye;
import lineage.world.object.npc.craft.Vincent;
import lineage.world.object.npc.craft.Paruit;
import lineage.world.object.npc.craft.Lien;
import lineage.world.object.npc.craft.mubni;
import lineage.world.object.npc.craft.mubni2;
import lineage.world.object.npc.craft.마안합성사;
import lineage.world.object.npc.craft.수상한조련사;
import lineage.world.object.npc.craft.야히의분신;
import lineage.world.object.npc.craft.조우의돌골렘;
import lineage.world.object.npc.dwarf.Axellon;
import lineage.world.object.npc.dwarf.Bahof;
import lineage.world.object.npc.dwarf.Borgin;
import lineage.world.object.npc.dwarf.Denitz;
import lineage.world.object.npc.dwarf.Dorin;
import lineage.world.object.npc.dwarf.El;
import lineage.world.object.npc.dwarf.Garin;
import lineage.world.object.npc.dwarf.Gaul;
import lineage.world.object.npc.dwarf.Gotham;
import lineage.world.object.npc.dwarf.Haidrim;
import lineage.world.object.npc.dwarf.Hakim;
import lineage.world.object.npc.dwarf.Hirim;
import lineage.world.object.npc.dwarf.Jianku;
import lineage.world.object.npc.dwarf.Jidar;
import lineage.world.object.npc.dwarf.Juke;
import lineage.world.object.npc.dwarf.Kamu;
import lineage.world.object.npc.dwarf.Karim;
import lineage.world.object.npc.dwarf.Karudim;
import lineage.world.object.npc.dwarf.Kasham;
import lineage.world.object.npc.dwarf.Kriom;
import lineage.world.object.npc.dwarf.Kuhatin;
import lineage.world.object.npc.dwarf.Kuron;
import lineage.world.object.npc.dwarf.Kusian;
import lineage.world.object.npc.dwarf.Luke;
import lineage.world.object.npc.dwarf.Nodim;
import lineage.world.object.npc.dwarf.Ogi;
import lineage.world.object.npc.dwarf.Orclon;
import lineage.world.object.npc.dwarf.Pius;
import lineage.world.object.npc.dwarf.Rayearth;
import lineage.world.object.npc.dwarf.Sauram;
import lineage.world.object.npc.dwarf.Storage;
import lineage.world.object.npc.dwarf.Tarkin;
import lineage.world.object.npc.dwarf.Thram;
import lineage.world.object.npc.dwarf.Tigus;
import lineage.world.object.npc.dwarf.Timpukin;
import lineage.world.object.npc.dwarf.Tofen;
import lineage.world.object.npc.dwarf.Tulak;
import lineage.world.object.npc.etc.Betray;
import lineage.world.object.npc.etc.Cassiopeia;
import lineage.world.object.npc.etc.NewAdminNovice;
import lineage.world.object.npc.etc.NewBiegate;
import lineage.world.object.npc.etc.NewRegion;
import lineage.world.object.npc.event.Duo;
import lineage.world.object.npc.event.ExperiencePointsSupplier;
import lineage.world.object.npc.event.FishingBoy;
import lineage.world.object.npc.event.JewelCraftsman;
import lineage.world.object.npc.event.Keplisha;
import lineage.world.object.npc.event.Kusan;
import lineage.world.object.npc.event.Mary;
import lineage.world.object.npc.event.Premium_teleport;
import lineage.world.object.npc.event.Roro;
import lineage.world.object.npc.event.Yuno;
import lineage.world.object.npc.guard.PatrolGuard;
import lineage.world.object.npc.guard.SentryGuard;
import lineage.world.object.npc.inn.Elly;
import lineage.world.object.npc.inn.Enke;
import lineage.world.object.npc.inn.Lolia;
import lineage.world.object.npc.inn.Miranda;
import lineage.world.object.npc.inn.Molly;
import lineage.world.object.npc.inn.Sabin;
import lineage.world.object.npc.inn.Selena;
import lineage.world.object.npc.inn.Velisa;
import lineage.world.object.npc.kingdom.Biust;
import lineage.world.object.npc.kingdom.Colbert;
import lineage.world.object.npc.kingdom.Freckson;
import lineage.world.object.npc.kingdom.Halt;
import lineage.world.object.npc.kingdom.Hunt;
import lineage.world.object.npc.kingdom.Ishmael;
import lineage.world.object.npc.kingdom.Orville;
import lineage.world.object.npc.kingdom.Othmond;
import lineage.world.object.npc.kingdom.Potempin;
import lineage.world.object.npc.kingdom.SeghemAtuba;
import lineage.world.object.npc.kingdom.Vaiger;
import lineage.world.object.npc.kingdom.Kentu;
import lineage.world.object.npc.pet.Almon;
import lineage.world.object.npc.pet.Alri;
import lineage.world.object.npc.pet.Burz;
import lineage.world.object.npc.pet.Cove;
import lineage.world.object.npc.pet.Dick;
import lineage.world.object.npc.pet.Hans;
import lineage.world.object.npc.pet.Johnson;
import lineage.world.object.npc.pet.Kevin;
import lineage.world.object.npc.pet.Marbin;
import lineage.world.object.npc.pet.Mild;
import lineage.world.object.npc.pet.Pau;
import lineage.world.object.npc.pet.Pigret;
import lineage.world.object.npc.quest.Aanon;
import lineage.world.object.npc.quest.AdminNovice;
import lineage.world.object.npc.quest.Aras;
import lineage.world.object.npc.quest.Aria;
import lineage.world.object.npc.quest.Ataroze;
import lineage.world.object.npc.quest.Chico;
import lineage.world.object.npc.quest.Clorence;
import lineage.world.object.npc.quest.Cuse;
import lineage.world.object.npc.quest.Dh;
import lineage.world.object.npc.guard.Sandstorm;
import lineage.world.object.npc.quest.Black;
import lineage.world.object.npc.quest.Dilong;
import lineage.world.object.npc.quest.FairyPrincess;
import lineage.world.object.npc.quest.Galleon;
import lineage.world.object.npc.quest.Gatekeeper;
import lineage.world.object.npc.quest.GatekeeperAnt;
import lineage.world.object.npc.quest.Gerard;
import lineage.world.object.npc.quest.Gereng;
import lineage.world.object.npc.quest.Gilbert;
import lineage.world.object.npc.quest.Gion;
import lineage.world.object.npc.quest.Gunter;
import lineage.world.object.npc.quest.Heit;
import lineage.world.object.npc.quest.HelperNovice;
import lineage.world.object.npc.quest.Hob;
import lineage.world.object.npc.quest.Honin;
import lineage.world.object.npc.quest.Jaruman;
import lineage.world.object.npc.quest.Jem;
import lineage.world.object.npc.quest.Jerik;
import lineage.world.object.npc.quest.Jim;
import lineage.world.object.npc.quest.Kalbass;
import lineage.world.object.npc.quest.Kan;
import lineage.world.object.npc.quest.Karif;
import lineage.world.object.npc.quest.Lekman;
import lineage.world.object.npc.quest.Lelder;
import lineage.world.object.npc.quest.Liri;
import lineage.world.object.npc.quest.Lring;
import lineage.world.object.npc.quest.Lyra;
import lineage.world.object.npc.quest.Mack;
import lineage.world.object.npc.quest.Marba;
import lineage.world.object.npc.quest.Mark;
import lineage.world.object.npc.quest.Marshall;
import lineage.world.object.npc.quest.Meet;
import lineage.world.object.npc.quest.Randith;
import lineage.world.object.npc.quest.Gayle;
import lineage.world.object.npc.quest.OrcfBakumo;
import lineage.world.object.npc.quest.OrcfBuka;
import lineage.world.object.npc.quest.OrcfHuwoomo;
import lineage.world.object.npc.quest.OrcfKame;
import lineage.world.object.npc.quest.OrcfNoname;
import lineage.world.object.npc.quest.OrfcfNoa;
import lineage.world.object.npc.quest.Oshillia;
import lineage.world.object.npc.quest.Oth;
import lineage.world.object.npc.quest.Porikan;
import lineage.world.object.npc.quest.Richard;
import lineage.world.object.npc.quest.Ricky;
import lineage.world.object.npc.quest.Robiel;
import lineage.world.object.npc.quest.Ronde;
import lineage.world.object.npc.quest.Ruba;
import lineage.world.object.npc.quest.SearchAnt;
import lineage.world.object.npc.quest.Serian;
import lineage.world.object.npc.quest.Syria;
import lineage.world.object.npc.quest.Talass;
import lineage.world.object.npc.quest.Tio;
import lineage.world.object.npc.quest.Tuck;
import lineage.world.object.npc.quest.Touma;
import lineage.world.object.npc.quest.Uamulet;
import lineage.world.object.npc.quest.Yahee;
import lineage.world.object.npc.quest.Zero;
import lineage.world.object.npc.quest.지브릴;
import lineage.world.object.npc.shop.*;
import lineage.world.object.npc.skill.Siabeth;
import lineage.world.object.npc.skill.Siriss;
import lineage.world.object.npc.teleporter.Amisoo;
import lineage.world.object.npc.Brad;
import lineage.world.object.npc.teleporter.Servertel;
import lineage.world.object.npc.teleporter.akfgksmstja;
import lineage.world.object.npc.teleporter.gltzguard;
import lineage.world.object.npc.teleporter.gltztele;
import lineage.world.object.npc.teleporter.grtzguard;
import lineage.world.object.npc.teleporter.grtztele;
import lineage.world.object.npc.teleporter.Mobjtelee;
import lineage.world.object.npc.teleporter.ortzguard;
import lineage.world.object.npc.teleporter.ortztele;
import lineage.world.object.npc.teleporter.sktzguard;
import lineage.world.object.npc.teleporter.sktztele;
import lineage.world.object.npc.teleporter.telediad;
import lineage.world.object.npc.teleporter.Barnia;
import lineage.world.object.npc.teleporter.Coco;
import lineage.world.object.npc.teleporter.ColiseumManager;
import lineage.world.object.npc.teleporter.ColiseumManager1;
import lineage.world.object.npc.teleporter.Cspace;
import lineage.world.object.npc.teleporter.Deanos;
import lineage.world.object.npc.teleporter.Doria;
import lineage.world.object.npc.teleporter.Drist;
import lineage.world.object.npc.teleporter.Dungeon;
import lineage.world.object.npc.teleporter.Duvall;
import lineage.world.object.npc.teleporter.Edlin;
import lineage.world.object.npc.teleporter.ElementalObe;
import lineage.world.object.npc.teleporter.Elleris;
import lineage.world.object.npc.teleporter.Entgate;
import lineage.world.object.npc.teleporter.Enya;
import lineage.world.object.npc.teleporter.Escapefi;
import lineage.world.object.npc.teleporter.Esmereld;
import lineage.world.object.npc.teleporter.FieldOfHonor;
import lineage.world.object.npc.teleporter.FirstTeleporter;
import lineage.world.object.npc.teleporter.Illdrath;
import lineage.world.object.npc.teleporter.Ishtar;
import lineage.world.object.npc.teleporter.Karen;
import lineage.world.object.npc.teleporter.Kirius;
import lineage.world.object.npc.teleporter.Kiyari;
import lineage.world.object.npc.teleporter.Kun;
import lineage.world.object.npc.teleporter.Leslie;
import lineage.world.object.npc.teleporter.Lucas;
import lineage.world.object.npc.teleporter.Luck;
import lineage.world.object.npc.teleporter.Mammon;
import lineage.world.object.npc.teleporter.MarketGuard;
import lineage.world.object.npc.teleporter.Matt;
import lineage.world.object.npc.teleporter.Mun;
import lineage.world.object.npc.teleporter.Nober;
import lineage.world.object.npc.teleporter.Npcworld;
import lineage.world.object.npc.teleporter.Ober;
import lineage.world.object.npc.teleporter.OrcfbuWoo;
import lineage.world.object.npc.teleporter.Paul;
import lineage.world.object.npc.teleporter.Ribian;
import lineage.world.object.npc.teleporter.Riol;
import lineage.world.object.npc.teleporter.Sirius;
import lineage.world.object.npc.teleporter.Sky;
import lineage.world.object.npc.teleporter.Stanley;
import lineage.world.object.npc.teleporter.Stevie;
import lineage.world.object.npc.teleporter.Telefire;
import lineage.world.object.npc.teleporter.Trey;
import lineage.world.object.npc.teleporter.Usender;
import lineage.world.object.npc.teleporter.Wilma;
import lineage.world.object.npc.teleporter.Zeno;
import lineage.world.object.npc.teleporter.Forget;
import lineage.world.object.npc.teleporter.telegltz;
import lineage.world.object.npc.teleporter.telegrtz;
import lineage.world.object.npc.teleporter.teleortz;
import lineage.world.object.npc.teleporter.telesktz;
import lineage.world.object.npc.teleporter.결투장나가기;
import lineage.world.object.npc.teleporter.결투장입장;

public final class NpcSpawnlistDatabase {

	static private List<object> pool;
	static private List<object> list;
	static private List<ShopInstance> buyShopList;
	static private List<ShopInstance> sellShopList;
	static public ShopInstance sellShop;
	// 각정 상점
	static public object 잡화상점;
	static public object 매입상인;
	static public object 무기상점;
	static public object 방어구상점;
	static public object 마법서상점;
	static public object 주문서상점;
	static public object playcheck;
	static public object sellNpc;
	static public object quest;
	static public object quest2;
	static public object bosstime;
	static public object ring;
	// 장비 스왑
	static public object itemSwap;
	// 자동 물약
	static public object autoPotion;
	// 던전 북
	static public 이동책 사냥터이동책;
	// 던전 북
	static public 이동책2 사냥터이동책2;
	static public 마을책 마을이동책;
	
	static public object auto_hunt;

	
	static public void init(Connection con){
		TimeLine.start("NpcSpawnlistDatabase..");
		
		if(pool == null) {
			pool = new ArrayList<object>();
			list = new ArrayList<object>();
			buyShopList = new ArrayList<ShopInstance>();
			sellShopList = new ArrayList<ShopInstance>();
		}
		synchronized (list) {
			pool.clear();
			list.clear();
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				st = con.prepareStatement("SELECT * FROM npc_spawnlist");
				rs = st.executeQuery();
				while(rs.next()) {
					object o = toSpawnNpc(rs.getString("name"), rs.getString("npcName"), rs.getString("title"), rs.getInt("locX"), rs.getInt("locY"), rs.getInt("locMap"), rs.getInt("heading"), rs.getInt("respawn"));
					if(o != null)
						list.add(o);
				}
				
			} catch (Exception e) {
				lineage.share.System.printf("%s : init(Connection con)\r\n", NpcSpawnlistDatabase.class.toString());
				lineage.share.System.println(e);
				e.printStackTrace();
			} finally {
				DatabaseConnection.close(st, rs);
			}
		}
		playcheck= new AttendanceCheck();
		quest= new KuberaQuest();
		quest2= new KuberaQuest2();
		itemSwap = new 장비스왑();
		사냥터이동책 = new 이동책();
		사냥터이동책2 = new 이동책2();
		마을이동책 = new 마을책();
		autoPotion = new 자동물약();
		auto_hunt = new AutoHunt();
		bosstime =  new BossTimer();
		ring = new RingOfSummon();
		
		ring.setObjectId(ServerDatabase.nextEtcObjId());
		playcheck.setObjectId(ServerDatabase.nextEtcObjId());
		quest.setObjectId(ServerDatabase.nextEtcObjId());
		quest2.setObjectId(ServerDatabase.nextEtcObjId());
		itemSwap.setObjectId(ServerDatabase.nextEtcObjId());
		사냥터이동책.setObjectId(ServerDatabase.nextEtcObjId());
		사냥터이동책2.setObjectId(ServerDatabase.nextEtcObjId());
		마을이동책.setObjectId(ServerDatabase.nextEtcObjId());
		autoPotion.setObjectId(ServerDatabase.nextEtcObjId());
		auto_hunt.setObjectId(ServerDatabase.nextEtcObjId());
		bosstime.setObjectId(ServerDatabase.nextEtcObjId());
		
		TimeLine.end();
	}
	
	static public void close() {
		synchronized (list) {
			for(object o : list) {
				World.remove(o);
				o.clearList(true);
				o.setAiStatus(-2);
			}
		}
	}
	
	/**
	 * 중복코드 방지용.
	 * @param npc
	 * @param title
	 * @param x
	 * @param y
	 * @param map
	 * @param heading
	 * @param respawn
	 */
	static public object toSpawnNpc(String key, String npc, String title, int x, int y, int map, int heading, int respawn){
		Npc n = NpcDatabase.find(npc);
		if(n!=null){
			object o = newObject(n, newObject(n));
			o.setDatabaseKey(key);
			
			// 신규혈맹
			if (o instanceof Clan_lord) {
				Clan c = ClanController.find(Lineage.new_clan_name);
				
				if (c != null) {
					o.setClanId(c.getUid());
					o.setClanName(Lineage.new_clan_name);
				}
			}	
			o.setTitle( title );
			o.setHomeX( x );
			o.setHomeY( y );
			o.setHomeMap( map );
			o.setHomeHeading( heading );
			o.setHeading( heading );
			o.setReSpawnTime( respawn );
			o.toTeleport(o.getHomeX(), o.getHomeY(), o.getHomeMap(), false);
			
			n.getSpawnList().add( new int[]{x, y, map});
			if(n.isAi())
				AiThread.append(o);
			if (o instanceof Rank_bronze)
				RankController.rankBronze = o;
			
			if (o instanceof PvP_Rank_bronze)
				RankController.pvprankBronze = o;
			
			if (o instanceof SellShop)
				sellShop = (ShopInstance) o;
			
			if (o.getName().equalsIgnoreCase("마법서상점"))
				마법서상점 = o;
			if (o.getName().equalsIgnoreCase("잡화상점"))
				잡화상점 = o;
			if (o.getName().equalsIgnoreCase("무기상점"))
				무기상점 = o;
			if (o.getName().equalsIgnoreCase("방어구상점"))
				방어구상점 = o;
			if (o.getName().equalsIgnoreCase("주문서상점"))
				주문서상점 = o;
			
			if (n.getType().equalsIgnoreCase("sell shop2"))
				sellNpc = o;
			
			if(n.getType().equalsIgnoreCase("Shop") && o instanceof ShopInstance){
				buyShopList.add((ShopInstance) o);
				
			}else if(n.getType().equalsIgnoreCase("sell shop")){
				sellShopList.add((ShopInstance) o);
			}
			
			return o;
		}
		return null;
	}
	
	static public object newObject(Npc n, object o){
		if(n==null || o==null)
			return null;
		
		o.setObjectId( ServerDatabase.nextNpcObjId() );
		o.setName( n.getNameId() );
		o.setGfx( n.getGfx() );
		o.setGfxMode( n.getGfxMode() );
		o.setMaxHp(n.getHp()==0 ? 1 : n.getHp());
		o.setNowHp(n.getHp()==0 ? 1 : n.getHp());
		o.setLawful( n.getLawful() );
		o.setClassGfx( o.getGfx() );
		o.setClassGfxMode( o.getGfxMode() );
		o.setLight( n.getLight() );
		
		return o;
	}

	static public object newObject(Npc n){
		// 버그 방지.
		if(n==null)
			return null;
		
		Object o = PluginController.init(NpcSpawnlistDatabase.class, "newObject", n);
		if(o instanceof object)
			return (object)o;
		
		
		switch(n.getNameIdNumber()){
			case 240:		// 마을 보초 경비병
			case 1177:		// 레인져
			case 58240:		// 난쟁이 경비병
			case 360240:	// 켄트 경비병
			case 951240:	// 윈다우드 경비병
			case 1513240:	// 하이네 경비병
			case 1242240:	// 기란 경비병
				if(n.getType().equalsIgnoreCase("PatrolGuard"))
					return new PatrolGuard(n);
				else
					return new SentryGuard(n);
			case 269:	// 판도라
				return new Pandora(n);
			case 270:	// 군터
				return new Gunter(n);
			case 304:	// 발심
				return new Balshim(n);
			case 309:	// 도린
				return new Dorin(n);
			case 320:	// 선착장 관리인
				return new HarborMaster(n);
			case 332:	// 케티
				return new Catty(n);
			case 333:	// 룻
				return new Luth(n);
//			case 334:	// 카림
//				return new Karim(n);
			case 365:	// 아만
				return new Aaman(n);
			case 373:	// 고라
				return new Gora(n);
//			case 374:	// 게렝
//				return new Gereng(n);
			case 406:	// 오림
				return new Orim(n);
			case 432:	// 이스마엘
				return new Ishmael(n);
			case 445:	// 훈트
				return new Hunt(n);
			case 446:	// 애쉬톤
				return new TalkMovingNpc(n, "ashton1");
			case 447:	// 던컨
				return new TalkMovingNpc(n, "dunkan1");
			case 448:	// 무어
				return new TalkMovingNpc(n, "moor1");
			case 449:	// 카나
				return new TalkMovingNpc(n, "cana1");
			case 450:	// 로리아
				return new Lolia(n);
			case 451:	// 파르보
				return new TalkMovingNpc(n, "farbo1");
			case 453:	// 지타
				return new TalkMovingNpc(n, "rjyta1");
			case 454:	// 렝고
				return new TalkMovingNpc(n, "lengo1");
			case 455:	// 휜
				return new TalkMovingNpc(n, "fiin1");
			case 456:	// 쥬디스
				return new TalkMovingNpc(n, "judice1");
			case 458:	// 안딘
				return new Andyn(n);
			case 459:	// 이소리야
				return new Ysorya(n);
			case 468:	// 바호프
				return new Bahof(n);
			case 474:	// 문지기
				return new Gatekeeper(n);
			case 479:	// 우드포드
				return new TalkMovingNpc(n, "woodford1");
			case 480:	// 오포
				return new TalkMovingNpc(n, "ofo1");
			case 481:	// 로한
				return new TalkMovingNpc(n, "rohan1");
			case 483:	// 토미
				return new TalkMovingNpc(n, "tommy1");
			case 484:	// 라이라
				return new Lyra(n);
			case 485:	// 사니타
				return new TalkMovingNpc(n, "sanita1");
			case 486:	// 엘른
				return new TalkMovingNpc(n, "ellne1");
			case 487:	// 한나
				return new TalkMovingNpc(n, "hanna1");
			case 488:	// 세겜 아투바
				return new SeghemAtuba(n);
			case 568:	// 스람
				return new Thram(n);
			case 614:	// 존슨
				return new Johnson(n);
			case 5324:
				return new 수상한조련사(n);
//			case 615:	// 딕
//				return new Dick(n);
			case 737:	// 밥
				return new TalkMovingNpc(n, "bob1");
			case 738:	// 루카
				return new TalkMovingNpc(n, "ruka1");
			case 749:	// 네루파
				return new Nerupa(n);
			case 750:	// 엘
				return new El(n);
			case 751:	// 호런
				return new Horun();
			case 752:	// 아라크네
				return new Arachne(n);
			case 753:	// 판
				return new Pan(n);
			case 754:	// 페어리
				return new Fairy(n);
			case 755:	// 엔트
				return new Ent(n);
			case 805:	// 페어리 퀸
				return new FairyQueen(n);
			case 811:	// 나르엔
				return new Narhen(n);
			case 812:	// 도에트
				return new Doett(n);
			case 813:	// 후린달렌
				return new Hurin(n);
			case 820:	// 모리엔
				return new Morien(n);
			case 821:	// 테오도르
				return new Theodor(n);
			case 826:	// 라반
				return new TalkMovingNpc(n, "laban1");
			case 829: // 루카스
				return new Lucas(n);
			case 831:	// 스텐리
				return new Stanley(n);
			case 833:	// 파울
				return new Paul(n);
			case 838:	// 필립
				return new Philip(n);
			case 844:	// 루쿠
				return new Luke(n);
//			case 846:	// 헥터
//				return new Hector(n);
			case 848:	// 빈센트
				return new Vincent(n);
			case 849:	// 에버트
				return new Evert(n);
			case 850:	// 콜버트
				return new Colbert(n);
			case 851:	// 알폰스
				return new Alfons();
			case 854:	// 퍼킨
				return new Perkin(n);
			case 857:	// 란달
				return new Randal(n);
			case 858:	// 세실
				return new Cecil(n);
			case 859:	// 디오
				return new Dio(n);
			case 860:	// 안톤
				return new Anton(n);
			case 861:	// 데렉
				return new Derek(n);
			case 862:	// 모나
				return new TalkMovingNpc(n, "mona1");
			case 866:	// 마가렛
				return new Margaret(n);
			case 870:	// 제니
				return new TalkMovingNpc(n, "jenny1");
			case 872:	// 앨리스
				return new TalkMovingNpc(n, "alice1");
			case 873:	// 에블린
				return new TalkMovingNpc(n, "evelyn1");
			case 874:	// 토비아
				return new TalkMovingNpc(n, "tovia1");
			case 875:	// 리엘
				return new TalkMovingNpc(n, "leal1");
			case 876:	// 알다
				return new TalkMovingNpc(n, "alda1");
			case 878:	// 리나
				return new TalkMovingNpc(n, "lina1");
			case 880:	// 데이지
				return new TalkMovingNpc(n, "daisy1");
			case 881:	// 브리젯
				return new TalkMovingNpc(n, "bridget1");
			case 882:	// 타냐
				return new TalkMovingNpc(n, "tanya1");
			case 883:	// 다리아
				return new TalkMovingNpc(n, "daria1");
			case 884:	// 도리스
				return new TalkMovingNpc(n, "doris1");
			case 885:	// 트레이시
				return new TalkMovingNpc(n, "tracy1");
			case 886:	// 폴리
				return new Polly(n);
			case 891:	// 베리타
				return new Verita(n);
			case 909:	// 브루너
				return new TalkMovingNpc(n, "brunner1");
			case 910:	// 엘미나
				return new Elmina(n);
			case 911:	// 마빈
				return new Marbin(n);
			case 912:	// 벨리사
				return new Velisa(n);
			case 914:	// 게라드
				return new Gerard(n);
			case 915:	// 글렌
				return new Glen(n);
			case 916:	// 멜린
				return new Mellin(n);
			case 917:	// 아논
				return new Aanon(n);
			case 918:	// 미란다.
				return new Miranda(n);
			case 921:	// 오스몬드
				return new Othmond(n);
			case 927:	// 돼지
				return new TalkMovingNpc(n, "pig1");
			case 928:	// 암닭
				return new TalkMovingNpc(n, "hen1");
			case 929:	// 젖소
				return new TalkMovingNpc(n, "milkcow1");
			case 934:	// 아놋테
				return new TalkMovingNpc(n, "anotte1");
			case 935:	// 할트
				return new Halt(n);
			case 946:	// 트레이
				return new Trey(n);
			case 947:	// 메트
				return new Matt(n);
			case 948:	// 타르킨
				return new Tarkin(n);
			case 949:	// 고담
				return new Gotham(n);
			case 950:	// 보긴
				return new Borgin(n);
			case 955:	// 셀레나
				return new Selena(n);
			case 963:	// 한스
				return new Hans(n);
			case 964:	// 잭슨
				return new Jackson(n);
			case 965:	// 아슈르
				return new Ashur(n);
			case 1053:	// 라다르
				return new Ladar(n);
			case 1056:	// 줄리
				return new Julie(n);
			case 1057:	// 핀
				return new Pin(n);
			case 1058:	// 죠엘
				return new Joel(n);
			case 1068:	// 케이스
			case 1069:	// 해리슨
			case 1070:	// 후퍼
			case 1071:	// 콥
			case 1072:	// 번치
				return new Telefire(n);
			case 1073:	// 쿠하틴
				return new Kuhatin(n);
			case 1145:	// 에스트
				return new Est(n);
			case 1238:	// 오빌
				return new Orville(n);
			case 1246:	// 허버트
				return new Herbert(n);
			case 1248:	// 사우림
				return new Sauram(n);
			case 1249:	// 노딤
				return new Nodim(n);
			case 1250:	// 몰리
				return new Molly(n);
			case 1259:	// 말콤
				return new TalkMovingNpc(n, "malcom1");
			case 1260:	// 데이먼
				return new TalkMovingNpc(n, "damon1");
			case 1261:	// 타이러스
				return new TalkMovingNpc(n, "tyrus1");
			case 1262:	// 셔원
				return new TalkMovingNpc(n, "sherwin1");
			case 1263:	// 모란
				return new TalkMovingNpc(n, "moran1");
			case 1264:	// 페르디난드
				return new TalkMovingNpc(n, "ferdinand1");
			case 1265:	// 질레스
				return new TalkMovingNpc(n, "giles1");
			case 1266:	// 알드레드
				return new TalkMovingNpc(n, "aldred1");
			case 1267:	// 길리언
				return new TalkMovingNpc(n, "gulian1");
			case 1268:	// 마누스
				return new TalkMovingNpc(n, "manus1");
			case 1269:	// 피에르
				return new TalkMovingNpc(n, "pierre1");
			case 1271:	// 올리버
				return new TalkMovingNpc(n, "oliver1");
			case 1272:	// 어니스트
				return new TalkMovingNpc(n, "ernest1");
			case 1286:	// 워너
				return new Werner(n);
			case 1295:	// 버질
				return new Vergil(n);
			case 1298:	// 케빈
				return new Kevin(n);
			case 1299:	// 알몬
				return new Almon(n);
			case 1301:	// 메이어
				return new Mayer(n);
			case 1354:	// 윌마
				return new Wilma(n);
			case 1380:	// 토펜
				return new Tofen(n);
			case 1396:	// 페이퍼 맨
				return new TalkMovingNpc(n, "paperman");
			case 1434:	// 부아크
				return new Buakheu(n);
			case 1500:	// 결투장 안내원
				return new FieldOfHonor(n);
			case 1501:	// 굿맨
				return new TalkNpc("goodman", false);
			case 1502:	// 뉴트럴맨
				return new TalkNpc("neutralman", false);
			case 1503:	// 이블맨
				return new TalkNpc("evilman", false);
			case 1510:	// 루디엘
				return new Luudiel(n);
			case 1517:	// 리올
				return new Riol(n);
			case 1518:	// 데릭
				return new TalkMovingNpc(n, "derick1");
			case 1524:	// 아리나
				return new TalkMovingNpc(n, "arina1");
			case 1525:	// 안나벨
				return new TalkMovingNpc(n, "annabel1");
			case 1526:	// 펠릭스
				return new TalkMovingNpc(n, "felix1");
			case 1527:	// 오리엘
				return new TalkMovingNpc(n, "oriel1");
			case 1528:	// 버랜트
				return new TalkMovingNpc(n, "barent1");
			case 1529:	// 폴츠
				return new TalkMovingNpc(n, "paults1");
			case 1530:	// 스펜서
				return new TalkMovingNpc(n, "spencer1");
			case 1531:	// 게일
				return new TalkMovingNpc(n, "gale1");
			case 1551:	// 하이드림
				return new Haidrim(n);
			case 1578:	// 오크 산타
				return new OrcSanta(n);
			case 1591:	// 포템핀
				return new Potempin(n);
			case 1594:	// 베리
				return new Berry(n);
			case 1595:	// 랄프
				return new Ralf(n);
			case 1596:	// 레슬리
				return new Leslie(n);
			case 1598:	// 가빈
				return new TalkMovingNpc(n, "gavin1");
			case 1599:	// 데일리
				return new TalkMovingNpc(n, "daley1");
			case 1600:	// 아타라
				return new TalkMovingNpc(n, "atara1");
			case 1602:	// 프렉슨
				return new Freckson(n);
			case 1604:	// 엑셀론
				return new Axellon(n);
			case 1611:	// 크리옴
				return new Kriom(n);
			case 1637:	// 콜드
				return new Cold(n);
			case 1638:	// 아리에
				return new Arieh(n);
			case 1653:	// 에스메랄다
				return new Esmereld(n);
			case 1674:	// 신녀 아가타
				return new GoddessAgata();
			case 1684:	// 변신술사
				return new PolymorphMagician();
			case 1685:	// 방어구 강화사
				return new ArmorEnchanter();
			case 1686:	// 무기 강화사
				return new WeaponEnchanter();
			case 1724:	// 리온
				return new TalkNpc("rion1", false);
			case 1725:	// 커스
				return new TalkNpc("cuse1", false);
			case 1726:	// 키리스
				return new Kiris(n);
			case 1728:	// 루바
				return new Ruba(n);
			case 1729:  // 티오
				return new Tio(n);
			case 1731:	// 키요리
				return new Kiyari(n);
			case 1732:	// 파고
				return new Pago(n);
			case 1738:	// 스카이
				return new Sky(n);
			case 1772:	// 마일드
				return new Mild(n);
			case 1773:	// 키리우스
				return new Kirius(n);
			case 1775:	// 비우스
				return new Bius(n);
			case 1776:	// 만드라
				return new Mandra(n);
			case 1777:	// 데리안
				return new TalkMovingNpc(n, "derian1");
			case 1779:	// 바뤼에스
				return new Varyeth(n);
			case 1780:	// 엘뤼온
				return new Ellyonne();
			case 1781:	// 크리스터
				return new Kreister(n);
			case 1792:	// 비온
				return new TalkMovingNpc(n, "bion1");
			case 1793:	// 디마
				return new TalkMovingNpc(n, "dima1");
			case 1794:	// 루루
				return new TalkMovingNpc(n, "ruru1");
			case 1795:	// 데칸
				return new TalkMovingNpc(n, "dekan1");
			case 1796:	// 로터스
				return new TalkMovingNpc(n, "rotus1");
			case 1797:	// 가루가
				return new TalkMovingNpc(n, "garuga1");
			case 1823:	// 데푸리
				return new TalkNpc("defuri", false);
			case 1824:	// 티파니
				return new TalkNpc("tifany", false);
			case 1825:	// 로쿠
				return new TalkNpc("roku", false);
			case 1826:	// 타우스
				return new TalkNpc("taus", true);
			case 1827:	// 비얀
				return new TalkNpc("biyan", true);
			case 1828:	// 엔케
				return new Enke(n);
			case 1875:	// 린다
				return new Rinda(n);
			case 1876:	// 파고르
				return new Pagoru(n);
			case 1877:	// 디코
				return new Dico(n);
			case 1878:	// 히림
				return new Hirim(n);
			case 1898:	// 리비안
				return new Ribian(n);
			case 1932:	// 아시리스
				return new Escapefi(n);
//			case 1953:	// 디텍터
//				return new Detecter(n);
			case 1954:	// 치키
				return new Chiky(n);
			case 1955:	// 럭키
				return new Luck(n);
			case 1956:	// 티론
				return new Tilon(n);
			case 2002:	// 일루지나
				return new Illusina(n);
			case 2013:	// 짐
				return new Jim(n);
			case 2014:	// 수색개미
				return new SearchAnt();
			case 2015:	// 문지기개미
				return new GatekeeperAnt();
			case 2018:	// 페어리 프린세스
				return new FairyPrincess(n);
			case 2019:	// 디롱
				return new Dilong(n);
			case 2021:	// 마샤
				return new Marshall(n);
			case 2036:	// 제인
				return new Jane(n);
			case 2082:	// 잭-오-랜턴
				return new JackLantern(n);
			case 2092:	// 오크론
				return new Orclon(n);
			case 2121:	// 데프만
				return new Defman(n);
			case 2122:	// 라온
				return new Raon(n);
			case 2123:	// 파우
				return new Pau(n);
			case 2125:	// 오지
				return new Ogi(n);
			case 2126:	// 시리우스
				return new Sirius(n);
			case 2128:	// 비우스트
				return new Biust(n);
			case 2135:	// 마이키
				return new TalkMovingNpc(n, "mikey1");
			case 2138:	// 엘레아노
				return new TalkMovingNpc(n, "elleano1");
			case 2141:	// 마르엔
				return new TalkMovingNpc(n, "maren1");
			case 2143:	// 쉐리안
				return new TalkMovingNpc(n, "sheryan1");
			case 2144:	// 버클리
				return new TalkMovingNpc(n, "buckley1");
			case 2145:	// 존스
				return new TalkMovingNpc(n, "jones1");
			case 2146:	// 빔
				return new TalkMovingNpc(n, "bim1");
			case 2148:	// 로젠
				return new Rozen(n);
			case 2150:	// 막스
				return new TalkMovingNpc(n, "marx1");
			case 2151:	// 라파엘
				return new TalkMovingNpc(n, "rapael1");
			case 2152:	// 바바라
				return new TalkNpc("babara1", true);
			case 2153:	// 키드만
				return new TalkNpc("kidman1", true);
			case 2154:	// 아퀸
				return new TalkNpc("aquin1", true);
			case 2157:	// 켈빈
				return new TalkMovingNpc(n, "calvin1");
			case 2160:	// 리차드
				return new Richard();
			case 2161:	// 맥
				return new Mack(n);
			case 2163:	// 스빈
				return new Sabin(n);
			case 2228:	// 카루딤
				return new Karudim(n);
			case 2229:	// 쥬케
				return new Juke(n);
			case 2230:	// 팀프킨
				return new Timpukin(n);
			case 2231:	// 캐서린
				return new Catherine(n);
			case 2232:	// 샤루
				return new Sharu(n);
			case 2234:	// 엘레리스
				return new Elleris(n);
			case 2235:	// 마구스
				return new Magus(n);
			case 2236:	// 페가
				return new Fega(n);
			case 2237:	// 멜리사
				return new Melissa(n);
			case 2238:	// 혈맹집행인
				return new ClanMaker();
			case 2257:	// 조사원
				return new TalkMovingNpc(n, "searcherk4");
			case 2258:	// 헤이트
				return new Heit(n);
			case 2344:	// 쿠퍼
				return new Kuper();
			case 2430:	// 경비 대장
				return new Kan(n);
			case 2432:	// 론드
				return new Ronde(n);
			case 2442:	// 쿠프
				return new Koup(n);
			case 2491:	// 하데스티
				return new Hadesty();
			case 2492:	// 레이아스
				return new Rayearth(n);
			case 2493:	// 공간이동사 에냐
				return new Enya(n);
			case 2494:	// 스크와티
				return new Squalid(n);
			case 2496:	// 카렌
				return new Karen(n);
			case 2498:	// 칸의 경비병
				return new TalkNpc("kandum", false);
			case 2501:	// 론드의 암살대
				return new TalkNpc("rondedum", false);
			case 2544:	// 키마
				return new Kima();
			case 2550:	// 데린
				return new TalkMovingNpc(n, "derin1");
			case 2552:	// 세디아
				return new Sedia();
			case 2554:	// 피에로
				return new TalkMovingNpc(n, "pierot1"); 
			case 2556:	// 비숍
				return new TalkMovingNpc(n, "bishop1");
			case 2557:	// 피어스
				return new Pierce(n);
			case 2558:	// 그랑디크
				return new TalkMovingNpc(n, "grandik1");
			case 2560:	// 엘비엔느
				return new TalkMovingNpc(n, "ellvienue1");
			case 2561:	// 라뮤네
				return new TalkMovingNpc(n, "lamune1");
			case 2586:	// 디아노스
				return new Deanos(n);
			case 2608:	// 바무트
				return new Bamute(n);
			case 2690:	// 라비엔느
				return new Lavienue(n);
			case 2860:	// 조드
				return new Jode(n);
			case 2861:	// 롤코
				return new Rollko(n);
			case 2883:	// 레이더스
				return new Raidar(n);
			case 2899:	// 파심
				return new Pasim(n);
			case 2902:	// 라폰스
				return new Rafons(n);
			case 2929:	// 프랑코
				return new Franko(n);
			case 2934:	// 루즈
				return new TalkMovingNpc(n, "citizen1");
			case 2935:	// 벡터
				return new TalkMovingNpc(n, "citizen2");
			case 2936:	// 러스터
				return new TalkMovingNpc(n, "citizen3");
			case 2938:	// 세라티
				return new TalkMovingNpc(n, "citizen5");
			case 2941:	// 그레이
				return new TalkMovingNpc(n, "citizen8");
			case 2942:	// 케니히
				return new TalkMovingNpc(n, "citizen9");
			case 2943:	// 뮤라스
				return new TalkMovingNpc(n, "citizen10");
				// 노세-펫아이템제작상인
			case 3042:
				return new Nose(n);
			case 2995:	// 에이시느-펫목걸이판매상인
				return new Ashinnue(n);
			case 2985:	// 속박된 영혼
				return new FetteredSoul(n);
			case 2984: // 야히의 분신
				return new 야히의분신(n);
			case 2986:	// 사르샤
				return new Sarsha(n);
//			case 3053:	// 시장 텔레포터
//				return new MarketTeleporter(n);
//			case 3054:	// 시장 경비병
//				return new MarketGuard(n);
			case 3055:	// 쟌쿠
				return new Jianku(n);
			case 3070:	// 쿠론
				return new Kuron(n);
			case 3071:	// 투락
				return new Tulak(n);
			case 3072:	// 쿠샨
				return new Kusian(n);
			case 3118:	// 광부 오움
				return new Moum();
			case 3129:	// 가울
				return new Gaul(n);
			case 3131:	// 에마르트
				return new TraderEmart(n);
			case 3132:	// 가린
				return new Garin(n);
			case 3135:	// 레오나
				return new Reona(n);
			case 3161:	// 레딘
				return new TalkMovingNpc(n, "redin1");
			case 3164:	// 번스
				return new TalkMovingNpc(n, "burns1");
			case 3170:	// 얀스틴
				return new TalkMovingNpc(n, "yastin1");
			case 3120:	// 프림
				return new Frim();
			case 3223:	// 데니츠
				return new Denitz(n);
			case 3224:	// 카샴
				return new Kasham(n);
			case 3225:	// 유노
				return new Yuno(n);
			case 3237:	// 아덴 상단
				return new AdenChamber_of_Commerce(n);
			case 3316:	// 레크만
				return new Lekman(n);
			case 3317:	// 세리안
				return new Serian(n);
			case 3318:	// 리리
				return new Liri(n);
			case 3319:	// 기온
				return new Gion(n);
			case 3320:	// 시리아
				return new Syria(n);
			case 3321:	// 오실리아
				return new Oshillia(n);
			case 3322:	// 호닌
				return new Honin(n);
			case 3323:	// 치코
				return new Chico(n);
			case 3324:	// 홉
				return new Hob(n);
			case 3325:	// 터크
				return new Tuck(n);
			case 3326:	// 갈리온
				return new Galleon(n);
			case 3327:	// 길버트
				return new Gilbert(n);
			case 3328:	// 포리칸
				return new Porikan(n);
			case 3329:	// 제릭
				return new Jerik(n);
			case 3330:	// 자루만
				return new Jaruman(n);
			case 3414:	// 발록의 분신
				return new FakeBarlog(n);
			case 3415:	// 칼바스의 하인
				return new Kalbass(n);
			case 3427:	// 오베르
				return new Ober(n);
			case 3428:	// 니키
				return new Niki(n);
			case 3436:	// 듀발
				return new Duvall(n);
			case 3437:	// 듀란
				return new Duran(n);
			case 3438:	// 록산느
				return new Roxanne(n);
			case 3449:	// 아르카
				return new TalkMovingNpc(n, "arka1");
			case 3523:	// 고대의 정령
				return new PielEmental(n);
			case 3526:	// 쿠드
				return new Tigus(n);
			case 3527:	// 베릭
				return new Berik(n);
			case 3529:	// 듀론
				return new Duron(n);
			case 3530:	// 포니
				return new Foni(n);
			case 3531:	// 카미트
				return new TalkMovingNpc(n, "kamit1");
			case 3551:	// 정령의 오브
				return new ElementalObe(n);
			case 3561:	// 루디안
				return new TalkMovingNpc(n, "rudian1a");
			case 3563:	// 티구스
				return new Tigus(n);
			case 3564:	// 지다르
				return new Jidar(n);
			case 3565:	// 알리
				return new Alri(n);
			case 3594:	// 루니
				return new TalkMovingNpc(n, "rooney");
			case 3595:	// 로베르토
				return new TalkMovingNpc(n, "roberto");
			case 3596:	// 루푸스
				return new TalkMovingNpc(n, "lupus");
			case 3597:	// 카루
				return new TalkMovingNpc(n, "karu");
				
			//20180522 리자드맨 장로 추가
			case 3733:
				return new Lelder(n);
				
			case 3777:	// 야히
				return new Yahee(n);
			case 3947:	// 버려진땅
				return new Entgate(n);
			case 3948:	// 발록의 대장장이
				return new Lsmith(n);
			case 3950:	// 야히의 시종
				return new Uhelp();
			case 3951:	// 발록의 보좌관
				return new Lring(n);
			case 3989:	// 마르바
				return new Marba(n);
			case 3990:	// 아라스
				return new Aras(n);
			case 4109:	// 보석 세공사
				return new JewelCraftsman(n);
			case 4208:	// 장로 노나메
				return new OrcfNoname(n);
			case 4278:	// 조사단장 아투바 노아
				return new OrfcfNoa(n);
			case 4279:	// 두다마라 카메
				return new OrcfKame(n);
			case 4280:	// 네르가 바쿠모
				return new OrcfBakumo(n);
			case 4281:	// 네르가 후우모
				return new OrcfHuwoomo(n);
			case 4282:	// 두다마라 부카
				return new OrcfBuka(n);
			case 4340:	// 이리스
				return new Eris();
			case 4574:	// 아델리오
				return new DwarfAdelio(n);
			case 4577:	// 장로수행원 클로렌스
				return new Clorence(n);
			case 4614:	// 아타로제
				return new Ataroze(n);
			case 4641:	// 차원의 문
				return new Cspace(n);
			case 4645:	// 집정관
				return new Meet(n);
			case 4646:	// 흔들리는 자
				return new Betray();
			case 4647:	// 업의 관리자
				return new Infamous(n);
			case 4656:	// 두다마라 부우
				return new OrcfbuWoo(n);
			case 4691:	// 야히의 대장장이
				return new Alice(n);
			case 4692:	// 야히의 보좌관
				return new Uamulet(n);
			case 4693:	// 연구원
				return new Lapyahee(n);
			case 4735:	// 경험치 지급단
				return new ExperiencePointsSupplier(n);
			case 4818:	// 시종장 맘몬
				return new Mammon(n);
			case 4820:	// 제이프
				return new Jp(n);
			case 4823:	// 디에츠
				return new Dh(n);
			case 4850:	// 치안대장 아미수
				return new Amisoo(n);
			case 4881:	// 포와르
				return new Usender(n);
			case 4883:	// 샤이렌
				return new Sciairen(n);
			case 5031:
				return new 지브릴(n);
			case 5041:	// 전쟁물자 상인
				return new Mellisa(n);
			case 5108:	// 라쿠키
				return new Rakuki(n);
			case 5164:	// 아덴 기마 단원
				return new HorseSeller(n);
			case 5190:	// 오스틴
				return new Ostin(n);
			case 5199:	// 케플리샤
				return new Keplisha(n);
			case 5200:	// 샤론
				return new Sharon(n);
			case 5201:	// 쿠엔
				return new TalkNpc("kuen1", false);
			case 5202:	// 수상한 오크 상인 파룸
				return new Parum(n);
			case 5203:	// 유령의 집 관리인 듀오
				return new Duo(n);
			case 5309:	// 유령의 집 관리인 쿠산
				return new Kusan(n);
			case 5276:	// 낚시 꼬마
				return new FishingBoy(n);
			case 5277:	// 낚시 할아버지
				return new FishElder(n);
			case 5278:	// 알프레드
				return new Alfred(n);
			case 5290:	// 오로라
				return new TalkMovingNpc(n, "aurora1");
			case 5291:	// 베키
				return new TalkMovingNpc(n, "becky1");
			case 5293:	// 체이스
				return new TalkMovingNpc(n, "chase1");
			case 5294:	// 제리코
				return new TalkMovingNpc(n, "jericho1");
			case 5394:	// 마이진
				return new Maijin(n);
			case 5138:	// 수상한 잡화 상인
			case 5742:	// 수상한 변신술사
			case 5743:	// 수상한 무기 상인
			case 5744:	// 수상한 갑옷 상인
				return new Premium(n);
			case 5590:	// 회상의 촛불지기 로로
				return new Roro(n);
			case 5783:	// 회상의 촛불소녀 마리
				return new Mary(n);
			case 6084:	// 환술사 아샤
				return new Asha();
			case 6085:	// 용기사 피에나
				return new Feaena();
			case 7918:	// 유리에
				return new Urye(n);
			case 8432:	// 기란 결투장 관리인 입장
				return new 결투장입장(n);
			case 8433:	// 기란 결투장 관리인 나가기
				return new 결투장나가기(n);
			case 8447:	// 수련장 관리인
				return new AdminNovice(n);
			case 10926:	// 마법 전수자^로스티
				return new Siriss();
			case 11366:	// 마법서 판매상^인센스
				return new SMerchant(n);
			case 13349:	// 구슬 판매상^쥬비드
				return new Jubead(n);
			case 13409:	// 사냥터 관리인^노베르
				return new Nober(n);
			case 13818:	// 아이템 제작^조우의 불골렘
				return new Rushi(n);
			case 14024:	// 수상한 펫 관리인^피그렛
				return new Pigret(n);
			case 14026:	// 수상한 창고지기^피우스
				return new Pius(n);
			case 14027:	// 수상한 강화 마법사^피도르
				return new Pidor();
			case 14028:	// 수상한 장난감 상인^덩키
				return new Dungky(n);
			case 14029:	// 수상한 방문상인^피에로
				return new Piero(n);
			case 10952:	// 제작 테이블^방어구(천)
			case 10953:	// 제작 테이블^방어구(가죽)
			case 10954:	// 제작 테이블^무기/방어구(금속)
			case 10955:	// 제작 테이블^액세서리(보석)
			case 10977:	// 제작 테이블^룸티스의 수정구
			case 10978:	// 제작 테이블^스냅퍼의 수정구
			case 12331:	// 연금술사^무브니
				return new CraftTable(n);
			case 9142928:	// 게라드 용병단
				return new TalkNpc("sdummy1", false);
			case 14129:	// 도리아
				return new Doria(n);
			case 14132:	// 초보자 도우미^카시오페아
				return new Cassiopeia();
			case 14242:	// 정령마법 전수자^샤베스
				return new Siabeth();
			case 14514:	// 펫 관리인^버즈
				return new Burz(n);
			case 18995:	// 초보자 도우미^사냥터 관리인
				return new NewRegion(n);
			case 18963:	// 수련의 텔레포터^리키
				return new NewBiegate(n);
			case 14130:	// 통합 창고지기^토리
				return new Storage(n);
			case 14131:	// 마법 전수자^페이하스
				return new Siriss();
			case 15579:	// 행 베리
				return new HangVeri(n);
			case 20688:
				return new Dungeon(n);
			default:
						switch(n.getArrowGfx()) {
						//case 1005:	// 오토사냥을 위한 물약 상점
						//	return new HangVeri(n);
						case 1006:	// 아덴 상단
							return new SellShop(n);
						case 1007: // 다리안 [수렵이벤트]
							return new Illdrath(n);
						case 1008: // 프라운 [수렵이벤트]
							return new BuyShop(n);
						case 2497:	// 에들렌
							return new Edlin(n);
						/**
						 * 말하는 섬 (말섬)
						 * NPC 목록 [10~28]번
						 **/ 
						case 10: // 루카스 [텔레포터]
							return new Lucas(n);
						case 11: // 셀레나 [여관]
							return new Selena(n);
						case 12: // 라다르 [가죽세공]
							return new Ladar(n);
						case 13: // 케이스 [화전민]
							return new Telefire(n);
						case 14: // 도린 [창고]
							return new Dorin(n);
						case 15: // 라이엔 [뼈세공]
							return new Lien(n);
						case 16: // 존슨 [개장수]
							return new Johnson(n);
						case 17: // 발심 [오크부탁 잡화상인]
							return new Balshim(n);
						case 18: // 파린 [징박은 가죽세공]
							return new Pin(n);
						case 19: // 게렝 [마법전수자]
							return new Gereng(n);
						case 20: // 선착장 관리인 [글루딘 배 표상인]
							return new HarborMaster(n);
						case 21: // 판도라 [잡화상인]
							return new Pandora(n);
						case 22: // 토마 [대장장이]
							return new Touma(n);
						case 23: // 군터 [퀘스트]
							return new Gunter(n);
						case 24: // 쥬디스 [주민]
							return new TalkMovingNpc(n, "judice1");
						case 25: // 휜 [주민]
							return new TalkMovingNpc(n, "fiin1");
						case 26: // 렝고 [주민]
							return new TalkMovingNpc(n, "lengo1");
						case 27: // 에블린 [주민]
							return new TalkMovingNpc(n, "evelyn1");
						case 28: // 모나 [주민]
							return new TalkMovingNpc(n, "mona1");
						case 29: // 앨리스 [주민] 추가
							return new TalkMovingNpc(n, "alice1");
						case 1928:	// 젬
							return new Jem(n);
						case 2989:	// 흑기사 부대장
							return new Black(n);
							/**
							 * 글루디오 (글말)
							 * NPC 목록 [30~42]번
							 **/ 
						case 30: // 스티브 [텔레포트]
							return new Stevie(n);
						case 31: // 케티 [무기상]
							return new Catty(n);
						case 32: // 룻 [잡화상점]
							return new Luth(n);
						case 33: // 카림 [창고지기]
							return new Karim(n);
						case 34: // 후퍼 [화전민마을]
							return new Telefire(n);
						case 35: // 로리아 [여관]
							return new Lolia(n);
						case 36: // 아만 [슬라임]
							return new Aaman(n);
						case 37: // 고라 [슬라임]
						case 38: // 마후 [슬라임]
							return new Gora(n);
						case 39: // 선착장 관리인 [말섬배표]
							return new HarborMaster(n);
						case 40: // 파르보 [주민]
							return new TalkMovingNpc(n, "farbo1");
						case 41: // 지타 [주민]
							return new TalkMovingNpc(n, "jyta2");
						case 42: // 제인트 [주민]
							return new TalkMovingNpc(n, "jaint1");
							/**
							 * 켄트성 마을 (켄말)
							 * NPC 목록 [50~62]번
							 **/ 
						case 50: // 카나 [주민]
							return new TalkMovingNpc(n, "cana1");
						case 51: // 무어 [주민]
							return new TalkMovingNpc(n, "moor1");
						case 52: // 던컨 [주민]
							return new TalkMovingNpc(n, "dunkan1");
						case 53: // 스텐리 [텔레포트]
							return new Stanley(n);
						case 54: // 스람 [창고지기]
							return new Thram(n);
						case 55: // 딕 [펫관리인]
							return new Dick(n);
						case 56: // 이소리야 [잡화상]
							return new Ysorya(n);
						case 57: // 안딘 [무기상]
							return new Andyn(n);
						case 58: // 해리슨[화전민마을]
							return new Telefire(n);
						case 59: // 이스마엘 [시종장]
							return new Ishmael(n);
						case 60: // 바호프 [창고지기]
							return new Bahof(n);
						case 61: // 게일 [대장장이]
							return new Gayle(n);
						case 62: // 훈트 [용병대장]
							return new Hunt(n);
						case 1927:	// 제로
							return new Zero(n);
							/**
							 * 윈다우드 (윈말)
							 * NPC 목록 [70~81]번
							 **/ 
						case 70: // 트레이 [텔레포트]
							return new Trey(n);
						case 71: // 타르킨 [창고]
							return new Tarkin(n);
						case 72: // 콥 [화전민마을]
							return new Telefire(n);
						case 73: // 엘미나 [잡화상]
							return new Elmina(n);
						case 74: // 브루너
							return new TalkMovingNpc(n, "brunner1");
						case 75: // 벨리사 [여관대여]
							return new Velisa(n);
						case 76: // 바우먼 [마을주민]
							return new TalkMovingNpc(n, "bauman1");
						case 77: // 마빈 [펫관리인]
							return new Marbin(n);
						case 78: // 란디스 [대장장이]
							return new Randith(n);
						case 79: // 할트 [용병대장]
							return new Halt(n);
						case 80: // 오스몬드 [시종장]
							return new Othmond(n);
						case 81: // 보긴 [윈성]창고지기
							return new Borgin(n);
						case 82: // 데커 [주민]추가
							return new TalkMovingNpc(n, "deker1");
						case 2016:	// 아리아
							return new Aria(n);
							/**
							 * 은기사마을 (은말)
							 * NPC 목록 [90~103]번
							 **/ 
						case 90: // 게라드 [훈련교관]
							return new Gerard(n);
						case 91: // 고담 [창고지기]
							return new Gotham(n);
						case 92: // 글렌 [무기상]
							return new Glen(n);
						case 93: // 데니스 [주민]
							return new TalkMovingNpc(n, "denis1");
						case 94: // 메트 [텔레포트]
							return new Matt(n);
						case 95: // 멜린 [잡화상]
							return new Mellin(n);
						case 96: // 미란다 [여관대여]
							return new Miranda(n);
						case 97: // 번치 [화전민]
							return new Telefire(n);
						case 98: // 아논 [대장장이]
							return new Aanon(n);
						case 99: // 아놋테 [주민]
							return new TalkMovingNpc(n, "anotte1");
						case 100: // 애쉬톤 [주민]
							return new TalkMovingNpc(n, "ashton1");
						case 101: // 죠엘 [뼈세공]
							return new Joel(n);
						case 102: // 줄리 [가죽세공]
							return new Julie(n);
						case 103: // 핀 [대장장이도제]
							return new Pin(n);
						case 1925:	// 리키
							return new Ricky(n);
							/**
							 * 화전민마을 (화말)
							 * NPC 목록 [300~42]번
							 **/ 
						case 300: // 잭슨 [잡화상]
							return new Jackson(n);
						case 301: // 라이라 [토템상인]
							return new Lyra(n);
						case 303: // 쿠하틴 [창고지기]
							return new Kuhatin(n);
						case 304: // 이반 [튜견장]
							return new TalkMovingNpc(n, "dogfight1");
						case 305: // 튜링 [튜견장]
							return new TalkMovingNpc(n, "dogfight1");
						case 306: // 우드포드 [주민]
							return new TalkMovingNpc(n, "woodford1");
						case 307: // 오포 [주민]
							return new TalkMovingNpc(n, "ofo1");
						case 308: // 로한 [주민]
							return new TalkMovingNpc(n, "rohan1");
						case 309: // 토미 [주민]
							return new TalkMovingNpc(n, "tommy1");
						case 310: // 사니타 [주민]
							return new TalkMovingNpc(n, "sanita1");
						case 311: // 엘른 [주민]
							return new TalkMovingNpc(n, "ellne1");
						case 312: // 한나 [주민]
							return new TalkMovingNpc(n, "hanna1");
						case 313: // 밥 [주민]
							return new TalkMovingNpc(n, "bob1");
						case 314: // 한스 [펫관리인]
							return new Hans(n);
						case 315: // 다니엘
							return new telediad(n);
						case 316: // 파울
							return new Paul(n);
						case 317: // 켄투네루가 [용병대장]
							return new Kentu(n);
						case 318: // 세겜 아투바 [시종장]
							return new SeghemAtuba(n);
				        case 1003:	// 이스발 [잊섬텔레포터]
				        	//return new LostIslandTeleporter();
							return new Isvall(n);
						case 1926:	// 오스
							return new Oth(n);
						case 302:	// 로드니
							return new Rodeny(n);
							/**
							 * 요정의숲(요말)
							 * NPC 목록 [400~42]번
							 **/ 
						case 400: // 네루파 [아이템 ]
							return new Nerupa(n);
						case 401: // 엘 [창고지기]
							return new El(n);
						case 402: // 에스트 [지도제작]
							return new Est(n);
						case 403: // 나르엔 [플룻제작]
							return new Narhen(n);
						case 404: // 호런 [마법교관]
							return new Horun();
						case 405: // 도에트 [주민]
							return new Doett(n);
						case 406: // 후린달렌 [주민]
							return new Hurin(n);
						case 407: // 모리엔 [주민]
							return new Morien(n);
						case 408: // 테오도르 [주민]
							return new Theodor(n);
						case 409: // 비엘 [잡화상]
							return new biel(n);
						case 411: // 루디엘 [와퍼제작]
							return new Luudiel(n);
						case 2574:	// 로빈후드
							return new Robiel(n);
							/**
							 * 잊혀진 섬 (잊섬)
							 * NPC 목록 [500~227]번
							 **/ 
						case 500: // 티론
							return new Tilon(n);
						case 501: // 럭키
							return new Luck(n);
						case 502: // 아시리스
							return new Escapefi(n);
						case 505: // 파울로 [고대유산]
							return new Detecter();
						case 506: // 신녀 아가타
							return new GoddessAgata();
							/**
							 * 사막 (상점)
							 * NPC 목록 [500~227]번
							 **/ 
						case 503: // 아슈르 [잡화상점]
							return new Ashur(n);
						case 504: // 오림
							return new Orim(n);
						case 507: // 아미엘
							return new YoungGunter(n);
						case 509: // 파르츠 [쥬스제작]
							return new Paruit(n);
						case 2012:	// 마크
							return new Mark(n);
							/**
							 * 기란성주민 (기란)
							 * NPC 목록 [200~227]번
							 **/ 
						case 110: // 살리나 [주민]
							return new TalkMovingNpc(n, "shalina1");
						case 111: // 제시 [주민] [과일가게앞]
							return new TalkMovingNpc(n, "jessy1");
						case 112: // 네일 [주민] 
							return new TalkMovingNpc(n, "neil1");
						case 113: // 타냐 [주민]
							return new TalkMovingNpc(n, "tanya1");
						case 114: // 짹 [주민]
							return new TalkMovingNpc(n, "jack1");
						case 115: // 이올라 [주민]
							return new TalkMovingNpc(n, "iola1");
						case 116: // 머윈 [주민]
							return new TalkMovingNpc(n, "merwyn1");
						case 117: // 올리버 [주민]
							return new TalkMovingNpc(n, "oliver1");
						case 118: // 타이러스 [주민]
							return new TalkMovingNpc(n, "tyrus1");
						case 119: // 제니 [주민]
							return new TalkMovingNpc(n, "jenny1");
						case 120: // 알다 [주민]
							return new TalkMovingNpc(n, "alda1");
						case 121: // 어니스트 [주민]
							return new TalkMovingNpc(n, "ernest1");
						case 122: // 벨마 [주민] 헥터앞
							return new TalkMovingNpc(n, "velma1");
						case 123: // 토비아 [주민]
							return new TalkMovingNpc(n, "tovia1");
						case 124: // 모란 [주민]
							return new TalkMovingNpc(n, "moran1");
						case 125: // 브리젯 [주민]
							return new TalkMovingNpc(n, "bridget1");
						case 126: // 가쓰 [주민]
							return new TalkMovingNpc(n, "garth1");
						case 127: // 피에르 [주민]
							return new TalkMovingNpc(n, "pierre1");
						case 128: // 길리언 [주민]
							return new TalkMovingNpc(n, "gulian1");
						case 129: // 마누스 [주민]
							return new TalkMovingNpc(n, "manus1");
						case 130: // 만델 [주민]
							return new TalkMovingNpc(n, "mandel1");
						case 131: // 시드니 [주민]
							return new TalkMovingNpc(n, "sidney1");
						case 132: // 데이지 [주민]
							return new TalkMovingNpc(n, "daisy1");
						case 133: // 루크 [주민]
							return new TalkMovingNpc(n, "luke1");
						case 134: // 엘리자 [주민]
							return new TalkMovingNpc(n, "eliza1");
						case 135: // 알드레드 [주민]
							return new TalkMovingNpc(n, "aldred1");
						case 136: // 도리스 [주민]
							return new TalkMovingNpc(n, "doris1");
						case 137: // 샐리[주민] 경매장옆
							return new TalkMovingNpc(n, "sally1");
						case 138: // 테리[주민]
							return new TalkMovingNpc(n, "terry1");
						case 139: // 루카 [주민]
							return new TalkMovingNpc(n, "ruka1");
						case 141: // 부르노[주민]
							return new TalkMovingNpc(n, "bruno1");
						case 142: // 트레이시[주민]
							return new TalkMovingNpc(n, "tracy1");
						case 143: // 셔원 [주민]
							return new TalkMovingNpc(n, "sherwin1");
						case 144: // 빅터 [주민] 개경기장
							return new TalkMovingNpc(n, "victor1");
						case 145: // 제임스 [주민]
							return new TalkMovingNpc(n, "james1");
						case 146: // 헬렌 [주민]
							return new TalkMovingNpc(n, "helen1");
						case 147: // 다리아 [주민] 알폰스옆
							return new TalkMovingNpc(n, "daria1");
						case 148: // 넬슨 [주민]
							return new TalkMovingNpc(n, "nelson1");
						case 149: // 말콤 [주민]
							return new TalkMovingNpc(n, "malcom1");
						case 150: // 페르디난드 [주민]
							return new TalkMovingNpc(n, "ferdinand1");
						case 151: // 에반 [주민] 경매장옆
							return new TalkMovingNpc(n, "eban1");
						case 152: // 리나 [주민]
							return new TalkMovingNpc(n, "lina1");
						case 153: // 리엘 [주민]
							return new TalkMovingNpc(n, "leal1");
						case 154: // 질레스 [주민]
							return new TalkMovingNpc(n, "giles1");
						case 155: // 데이먼 [주민]
							return new TalkMovingNpc(n, "damon1");
						case 156: // 라반 [주민]
							return new TalkMovingNpc(n, "laban1");
						case 205: // 기란 경매인
							return new TalkNpc("auction1", true);
						//case 894:
						//	return new 호외소년(n);
						case 895: // 무브니
							return new mubni(n);
						case 896: // 지배부적 제작
							return new mubni2(n);
							/**
							 * 인나드 마을 (인마)
							 * NPC 목록 [200~42]번
							 **/ 
						case 880:	// 시리우스
							return new Sirius(n);
						case 700: // 커스 [대장장이]
							return new Cuse(n);
						case 701: // 베리 [잡화상]
							return new Berry(n);
						case 702: // 랄프 [무기상]
							return new Ralf(n);
						case 703: // 레슬리 [텔레포트]
							return new Leslie(n);
						case 704: // 엔케 [여관대여]
							return new Enke(n);
						case 705: // 엑셀론 [창고]
							return new Axellon(n);
						case 706: // 가빈 [주민]
							return new TalkMovingNpc(n, "gavin1");
						case 707: // 데일리 [주민]
							return new TalkMovingNpc(n, "daley1");
						case 708: // 아타라 [주민]
							return new TalkMovingNpc(n, "atara1");
						case 709: // 비온 [주민]
							return new TalkMovingNpc(n, "bion1");
						case 710: // 디마 [주민]
							return new TalkMovingNpc(n, "dima1");
						case 711: // 루루 [주민]
							return new TalkMovingNpc(n, "ruru1");
						case 712: // 데칸 [주민]
							return new TalkMovingNpc(n, "dekan1");
						case 713: // 로터스 [주민]
							return new TalkMovingNpc(n, "rotus1");
						case 714: // 가루가 [주민]
							return new TalkMovingNpc(n, "garuga1");
						case 715: // 코브 [펫관리인]
							return new Cove(n);
						case 716: // 만드라 [무기상]
							return new Mandra(n);
						case 717: // 마일드 [펫관리인]
							return new Mild(n);
						case 718: // 히림 [창고]
							return new Hirim(n);
						case 719:	// 하로
							return new TalkMovingNpc(n, "haro1");
							
							/**
							 * 무인엔피씨
							 * NPC 목록 [10000~]번
							 **/ 
						case 10000: // [무인]장사꾼
							return new Manless1(n);
						case 10001: // [무인]장사꾼1
							return new Manless2(n);
						case 10002: // [무인]장사꾼2
							return new Manless3(n);
						case 10003: // [무인]장사꾼3
							return new Manless4(n);
						case 10004: // [무인]장사꾼4
							return new Manless5(n);
						case 10005: // [무인]장사꾼5
							return new Manless6(n);
						case 10006: // [무인]장사꾼6
							return new Manless7(n);
						case 10007: // [무인]장사꾼7
							return new Manless8(n);
						case 10008: // [무인]장사꾼8
							return new Manless9(n);
						case 10009: // [무인]장사꾼9
							return new Manless10(n);
						case 10010: // [무인]장사꾼10
							return new Manless0(n);
							
						case 10017: // [무인]군주
							return new BuyShop(n);
						case 10018: // [무인]기사
							return new BuyShop(n);
						case 10019: // [무인]요정
							return new BuyShop(n);
						case 10020: // [무인]법사
							return new BuyShop(n);
							/**
							 * 깡촌서버 전용 NPC
							 * NPC 목록 [10100~]번
							 **/ 
						case 10100: // [보스이동]페이번
							return new BuyShop(n);
						case 10101: // [사냥터이동]데이즈
							return new BuyShop(n);
						case 10103: // 신규햘맹 군주
						case 10104: // 신규햘맹 공주
							return new Clan_lord();
							
							/**
							 * 기란성마을 (기란)
							 * NPC 목록 [200~42]번
							 **/ 
						case 200: // 윌마 [텔레포트]
							return new Wilma(n);
						case 201: // 디오 [보석상]
							return new Dio(n);
						case 202: // 메이어 [잡화상]
							return new Mayer(n);
						case 203: // 노딤 [창고지기]
							return new Nodim(n);
						case 204: // 사우림 [창고지기]
							return new Sauram(n);
						case 206: // 모리아 [연금술사]
							return new Moria(n);
						case 207: // 몰리 [여관대여]
							return new Molly(n);
						case 208: // 에버트 [옷감상인]
							return new Evert(n);
						case 209: // 버질 [방어구상인]
							return new Vergil(n);
						case 210: // 안톤 [공예사]
							return new Anton(n);
						case 211: // 마가렛 [식료품상인]
							return new Margaret(n);
						case 212: // 필립 [가죽상인]
							return new Philip(n);
						case 213: // 헥터 [대장장이]
							return new Hector(n);
						case 214: // 빈센트 [가죽세공사]
							return new Vincent(n);
						case 215: // 란달 [약품상인]
							return new Randal(n);
						case 216: // 데렉 [사냥꾼]
							return new Derek(n);
						case 217: // 허버트 [재단사]
							return new Herbert(n);
						case 218: // 케빈 [펫관리인]
							return new Kevin(n);
						case 219: // 알몬 [펫관리인]
							return new Almon(n);
						case 220: // 워너 [무기상인]
							return new Werner(n);
						case 221: // 베리타 [잡화상]
							return new Verita(n);
						case 222: // 알폰스 [거지]
							return new TalkMovingNpc(n, "alfons1");
						case 223: // 폴리 [티켓상인]
							return new Polly(n);
						case 224: // 세실 [티켓상인]
							return new Cecil(n);
						case 225: // 퍼킨 [티켓상인]
							return new Perkin(n);
						case 227: // 콜롯세움 관리인
						case 226: // 콜롯세움 부관리인
							return new ColiseumManager(n);
						case 333: // 크리스터[마법전수자]
							return new Kreister(n);
						case 2442: // 쿠프
							return new 마안합성사(n);
						case 5902: // 유리스
							return new 유리스();
						case 229:	// 마법사 멀린 (기란감옥텔)
							return new Giran_dungeon_Telepoter();
						case 480:	// 연금술사
							return new Alchemist(n);
						case 481: // 서버 텔레포트
							return new Servertel(n);
							/** 
							 * NPC 목록 [800]번
							 **/ 		
						case 800: // 콜롯세움 경비병
							return new ColiseumManager1(n);
						case 802: // 오렌 시장 경비병
							return new ortzguard(n);
						case 803: // 기란 시장 경비병
							return new grtzguard(n);
						case 804: // 글루딘 시장 경비병
							return new gltzguard(n);
						case 805: // 은기사 시장 경비병
							return new sktzguard(n);
						case 806:	// 오렌 공간 이동사
							return new teleortz(n);
						case 807:	// 기란 공간 이동사
							return new telegrtz(n);
						case 808:	// 글루딘 공간 이동사
							return new telegltz(n);
						case 809:	// 은기사 공간 이동사
							return new telesktz(n);
						case 810:	// 오렌 시장 엘버
							return new ortztele(n);
						case 811:	// 기란 시장 엘바
							return new grtztele(n);
						case 812:	// 글루딘 시장 메데
							return new gltztele(n);
						case 813:	// 은기사 시장 벨거
							return new sktztele(n);
						case 820:	// 공간의 일그러짐 (하얀)
							return new sktztele(n);
						case 821:	// 공간의 일그러짐 (파랑)
							return new sktztele(n);
						case 822:	// 공간의 일그러짐 (빨강)
							return new sktztele(n);
						case 823:	// 공간의 일그러짐 (초록)
							return new sktztele(n);
							/**
							 * 초보자존
							 **/
						case 825: // 일드라스
							return new Illdrath(n);
						case 826: // 이쉬타
							return new Ishtar(n);
						case 827: // 바르니아
							return new Barnia(n);
						case 828:	// 시리스
							return new Siriss();
						case 829:	// 프라운
							return new Fraoun(n);
						case 830:	// 요한
							return new Johan(n);
						case 831:	// 드리스트
							return new Drist(n);
						case 832:	// 제노
							return new Zeno(n);
						case 833:	// 리비안
							return new Ribian(n);
						case 834:	// 쿤
							return new Kun(n);
						case 835:	// 코코
							return new Coco(n);
						case 8446:	// 초보자 도우미
							return new HelperNovice(n);
						case 2238:	// 혈맹집행인
							return new ClanMaker();
						case 8447:	// 수련장 관리인
							return new AdminNovice(n);
							/** 
							 * 하이네 NPC 목록 [800]번
							 **/ 	
						case 836:	// 바이거 [용병대장]
							return new Vaiger(n);
						case 837:	// 하킴
							return new Hakim(n);
						case 838:	// 리올
							return new Riol(n);
						case 839:	// 시반
							return new Shivan(n);
						case 850:	// 브리트
							return new Britt(n);
						case 851:	// 앨리
							return new Elly(n);
						case 852:	// 엘란
							return new Elly(n);
						case 853:	// 데릭
							return new TalkMovingNpc(n, "derick1");
						case 854:	// 아리나
							return new TalkMovingNpc(n, "arina1");
						case 855:	// 안나벨
							return new TalkMovingNpc(n, "annabel1");
						case 856:	// 펠릭스
							return new TalkMovingNpc(n, "felix1");
						case 857:	// 오리엘
							return new TalkMovingNpc(n, "oriel1");
						case 858:	// 버랜트
							return new TalkMovingNpc(n, "barent1");
						case 859:	// 폴츠
							return new TalkMovingNpc(n, "paults1");
						case 860:	// 스펜서
							return new TalkMovingNpc(n, "spencer1");
						case 1488:	// 에브롤
							return new Eveurol(n);
						case 1621:  // 지브릴
							return new 지브릴(n);
							/** 
							 * NPC 목록 [800]번
							 **/ 	
						case 814: // 낚시 꼬마
						case 815: // 낚시 꼬마
							return new FishingBoy(n);
						case 816: // 낚시 할아버지
							return new FishElder(n);
						case 817: // 낚시터 아줌마
							return new BuyShop(n);
						case 840:	// 카무
							return new Kamu(n);
						case 841:	// 시장창고
							return new Mobjtelee(n);
						case 842:	// 커스
							return new TalkNpc("cuse1", false);
						case 843:	// 벨게터
							return new TalkNpc("belgeter", false);
						case 844:	// 루더
							return new TalkNpc("luder", false);
						case 845:	// 치료사
							return new Curer();
						case 846:	// 헤이스트사
							return new Haste();
						case 847:	// 초보 텔레포터
							return new FirstTeleporter(n);
						case 848:	// 이벨빈
							return new Ivelviin(n);
						case 849:	// 타라스
							return new Talass();
						case 870:	// 카리프
							return new Karif(n);
						case 871:	// 샤르나
							return new Sharna(n);
						case 881:	// 수상한
							return new Premium_teleport(n);
						case 882:	// 류미엘
							return new Ryumiel(n);
						case 228:	// 제이슨
							return new Jason(n);
						case 883: // 부식의수정 [텔레포터]
							return new akfgksmstja(n);
						case 890:	// 로즈
							return new Rose(n);
						case 891:	// 티나
							return new Tina(n);
						case 892:	// 샤샤
							return new Sasha(n);
						case 893: // 조우의 돌골렘
							return new 조우의돌골렘(n);
						case 897:	// 지옥 상점
							return new Orcshop(n);
						default:
						
							switch(n.getGfx()){
							case 1256:	// 아지트 시녀
								return new Maid();
							default:
						if(n.isAi()){
							if (n.getType().equalsIgnoreCase("promot_npc")) 
								return new Promot_npc(n);
							else
								return new NpcInstance(n);
						} else {
							if(n.getType().equalsIgnoreCase("buy shop")){
								return new BuyShop(n);
							}else if(n.getType().equalsIgnoreCase("sell shop")){
								return new SellShop(n);
							}else if(n.getType().equalsIgnoreCase("sell shop2")){
								return new SellShop2(n);
							}else if(n.getNameId().equalsIgnoreCase("혈맹 레벨 관리인")){
								return new ClanLevelNpc();
							}else if(n.getType().equalsIgnoreCase("인형 합성 관리인")){
								return new DollMakeNpc();
							}else if(n.getName().equalsIgnoreCase("서버퀘스트 관리인")){
								return new ServerQuestNpc();
							}else if(n.getName().equalsIgnoreCase("경험치 지급단")){
								return new 경험치지급단();
							}else if(n.getType().equalsIgnoreCase("경험치 복구")){
								return new GoddessAgata();
							}else if(n.getName().equalsIgnoreCase("개경주장입구")){
								return new 개경주장입구();
							}else if(n.getType().equalsIgnoreCase("shop")){
								return new Pandora(n);
							}else if(n.getName().equalsIgnoreCase("아테나")){
								return new Npcworld(n);
							}else if(n.getType().equalsIgnoreCase("Mun")){
								return new Mun(n);
							} else if(n.getName().equalsIgnoreCase("카림")) {
								return new Karim(n);
							} else if(n.getName().equalsIgnoreCase("시장 경비병")) {
								return new MarketGuard(n);
							}else if(n.getType().equalsIgnoreCase("giran")){
								return new 기란성버프강화();
							}else if(n.getType().equalsIgnoreCase("wind")){
								return new 윈성버프강화();
							}else if(n.getType().equalsIgnoreCase("kent")){
								return new 켄트성버프강화();
							}else if(n.getType().equalsIgnoreCase("ring_t")){
								return new 반지교환사();
							}else if(n.getType().equalsIgnoreCase("강화버프")){
								return new 강화마법사();
							}else if(n.getType().equalsIgnoreCase("테베제단")){
								return new 테베게이트(n);
							}else if(n.getType().equalsIgnoreCase("Brad")){
								return new Brad(n);
							}else if(n.getType().equalsIgnoreCase("rank bronze")) {
								return new Rank_bronze();
							} else if (n.getType().equalsIgnoreCase("pvp rank bronze")) {
								return new PvP_Rank_bronze();
							} else if (n.getType().equalsIgnoreCase("buy shop3")) {
								return new BuyShop3(n);
							}else{
								return new EtcNpc(n);
							}
						}
				}
		}
	}
}

	
	static public object getPool(Class<?> c){
		synchronized (pool) {
			object r_o = null;
			if(Lineage.memory_recycle) {
				for(object o : pool){
					if(o.getClass().toString().equals(c.toString())){
						r_o = o;
						break;
					}
				}
				if(r_o != null) {
					pool.remove(r_o);
				}
			}
//			lineage.share.System.println(NpcSpawnlistDatabase.class.toString()+" : pool.remove("+pool.size()+")");
			return r_o;
		}
	}
	
	static public void setPool(NpcInstance ni){
		ni.close();
		if(!Lineage.memory_recycle)
			return;
		synchronized (pool) {
			if(!pool.contains(ni))
				pool.add(ni);
		}
		
//		lineage.share.System.println(NpcSpawnlistDatabase.class.toString()+" : pool.add("+pool.size()+")");
	}
	
	static public int getPoolSize(){
		return pool.size();
	}
	
	static public int selectCount(Connection con){
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT COUNT(*) FROM npc_spawnlist");
			rs = st.executeQuery();
			if(rs.next())
				return rs.getInt(1);
		} catch (Exception e) {
			lineage.share.System.printf("%s : selectCount(Connection con)\r\n", NpcSpawnlistDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		return 0;
	}
	
	static public void storeSpwan(object o, Npc npc){
		Connection con = null;
		PreparedStatement stt = null;
		try{
			int x = o.getX();
			int y = o.getY();
			int mapid = o.getMap();
			int head = o.getHeading();
			con = DatabaseConnection.getLineage();
			stt = con.prepareStatement("INSERT INTO npc_spawnlist SET name=?,npcName=?,locX=?, locY=?, locMap=?,heading=?,title=?");
			stt.setString(1, npc.getName());
			stt.setString(2, npc.getName());
			stt.setInt(3, x);
			stt.setInt(4, y);
			stt.setInt(5, mapid);
			stt.setInt(6, head);
			stt.setString(7, "");
			stt.execute();
			NpcSpawnlistDatabase.toSpawnNpc(npc.getName(), npc.getName(), "", x, y, mapid, head, 0);
		} catch (Exception e) {
			lineage.share.System.printf("%s : 엔피씨등록에러", CommandController.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, stt);
		}
	}
	
	static public void insert(
			Connection con, final String name, final String npcName, final int locX, final int locY, final int locMap, 
			final int heading, final int respawn, final String title
		){
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("INSERT INTO npc_spawnlist SET name=?, npcName=?, locX=?, locY=?, locMap=?, heading=?, respawn=?, title=?");
			st.setString(1, name);
			st.setString(2, npcName);
			st.setInt(3, locX);
			st.setInt(4, locY);
			st.setInt(5, locMap);
			st.setInt(6, heading);
			st.setInt(7, respawn);
			st.setString(8, title);
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : insert()\r\n", NpcSpawnlistDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st);
		}
	}

	public static List<ShopInstance> getBuyShopList() {
		return buyShopList;
	}

	public static void setBuySopList(List<ShopInstance> buySopList) {
		NpcSpawnlistDatabase.buyShopList = buySopList;
	}

	public static List<ShopInstance> getSellShopList() {
		return sellShopList;
	}

	public static void setSellSopList(List<ShopInstance> sellSopList) {
		NpcSpawnlistDatabase.sellShopList = sellSopList;
	}
	
	static public void delete(Npc n) {
		PreparedStatement st = null;
		Connection con = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("DELETE FROM npc_spawnlist WHERE name=?");
			st.setString(1, n.getName());
			st.executeUpdate();
		} catch (Exception e) {
			lineage.share.System.printf("%s : delete(Npc n)\r\n", NpcSpawnlistDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st);
		}
	}
	
	static public object find(long objid) {
		synchronized (list) {
			for (object o : list) {
				if (o.getObjectId() == objid)
					return o;
			}
			return null;
		}
	}
}
