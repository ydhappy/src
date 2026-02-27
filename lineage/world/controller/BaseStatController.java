package lineage.world.controller;

import java.util.HashMap;
import java.util.Map;

import lineage.bean.lineage.BaseStat;
import lineage.database.PolyDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_BaseStat;
import lineage.network.packet.server.S_CharacterStat;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.object.instance.PcInstance;

public class BaseStatController {

	static private Map<Long, BaseStat> list;
	
	static public void init() {
		TimeLine.start("BaseStatController..");
		
		list = new HashMap<Long, BaseStat>();
		
		TimeLine.end();
	}
	
	static public void close() {
		//
	}
	
	static public void toWorldOut(PcInstance pc) {
		toClose(pc);
	}
	
	/**
	 * 회상의 촛불 열기 처리 메서드.
	 * @param pc
	 */
	static public void toOpen(PcInstance pc) {
		// 버그 방지.
		if(contains(pc))
			return;
		// 관리목록에 등록.
		append(pc);
		// 회상의 땅으로 이동.
		pc.toTeleport(Util.random(32721, 32814), Util.random(32785, 32878), 5166, true);
		// 장비 해제. (중복소스를 넣기좀 그래서 젖소변신을 통한 장비해제처리로 구현.)
		PolyDatabase.toEquipped(pc, PolyDatabase.getName("젖소"));
		// 회상의 촛불 화면 전환.
		pc.toSender(S_BaseStat.clone(BasePacketPooling.getPool(S_BaseStat.class), find(pc)));	
	}
	
	/**
	 * 스탯분배 완료 요청처리 메서드.
	 */
	static public void toStat(PcInstance pc, int Str, int Int, int Wis, int Dex, int Con, int Cha) {
		BaseStat bs = find(pc);
		if(bs == null)
			return;
		//
		bs.setStr(Str);
		bs.setInt(Int);
		bs.setWis(Wis);
		bs.setDex(Dex);
		bs.setCon(Con);
		bs.setCha(Cha);
		bs.setType(2);
		if(bs.getStat() == 75)
			pc.toSender(S_BaseStat.clone(BasePacketPooling.getPool(S_BaseStat.class), bs));
		else
			toClose(pc);
	}
	
	/**
	 * 레벨업 요청처리 메서드.
	 */
	static public void toLevelUp(PcInstance pc, int type) {
		BaseStat bs = find(pc);
		if(bs == null)
			return;
		// 레벨업 시도중 최대레벨을 초과하려 한다면 종료 처리.
		if(bs.getLev() >= bs.getLevMax()) {
			toClose(pc);
			return;
		}
		//
		boolean is = true;
		switch(type) {
			case 1:
				if(bs.getStr()+bs.getLvStr()+bs.getElixirStr() < Lineage.stat_max)
					bs.setLvStr(bs.getLvStr() + 1);
				else
					is = false;
				break;
			case 2:
				if(bs.getInt()+bs.getLvInt()+bs.getElixirInt() < Lineage.stat_max)
					bs.setLvInt(bs.getLvInt() + 1);
				else
					is = false;
				break;
			case 3:
				if(bs.getWis()+bs.getLvWis()+bs.getElixirWis() < Lineage.stat_max)
					bs.setLvWis(bs.getLvWis() + 1);
				else
					is = false;
				break;
			case 4:
				if(bs.getDex()+bs.getLvDex()+bs.getElixirDex() < Lineage.stat_max)
					bs.setLvDex(bs.getLvDex() + 1);
				else
					is = false;
				break;
			case 5:
				if(bs.getCon()+bs.getLvCon()+bs.getElixirCon() < Lineage.stat_max)
					bs.setLvCon(bs.getLvCon() + 1);
				else
					is = false;
				break;
			case 6:
				if(bs.getCha()+bs.getLvCha()+bs.getElixirCha() < Lineage.stat_max)
					bs.setLvCha(bs.getLvCha() + 1);
				else
					is = false;
				break;
		}
		if(is) {
			for(int i=type==7?0:9 ; i<10 ; i++) {
				bs.setLev(bs.getLev() + 1);
				bs.setHp(bs.getHp() + CharacterController.toStatusUP(pc, bs.getCon()+bs.getLvCon()+bs.getElixirCon(), bs.getWis()+bs.getLvWis()+bs.getElixirWis(), true));
				bs.setMp(bs.getMp() + CharacterController.toStatusUP(pc, bs.getCon()+bs.getLvCon()+bs.getElixirCon(), bs.getWis()+bs.getLvWis()+bs.getElixirWis(), false));
				if(bs.getLev() >= bs.getLevMax())
					break;
			}
			// 덱스에 따른 ac 표현을 위한 연산.
			int total_dex = bs.getDex() + bs.getLvDex() + bs.getElixirDex();
			int gab = total_dex>=18 ? 4 : total_dex<=17 && total_dex>=16 ? 5 : total_dex<=15 && total_dex>=13 ? 6 : total_dex<=12 && total_dex>=10 ? 7 : 8;
			bs.setAc(bs.getLev() / gab);
		}
		//
		bs.setType(2);
		pc.toSender(S_BaseStat.clone(BasePacketPooling.getPool(S_BaseStat.class), bs));
	}
	
	/**
	 * 회상의 촛불 종료처리 메서드.
	 * @param pc
	 */
	static public void toClose(PcInstance pc) {
		BaseStat bs = find(pc);
		if(bs == null)
			return;
		//
		remove(pc);
		//
		if(bs.getLev() != bs.getLevMax())
			return;
		// 회상의 촛불 화면 전환
		bs.setType(3);
		pc.toSender(S_BaseStat.clone(BasePacketPooling.getPool(S_BaseStat.class), bs));
		// 스탯 잡아주기.
		pc.setMaxHp(bs.getHp());
		pc.setNowHp(bs.getHp());
		pc.setMaxMp(bs.getMp());
		pc.setNowMp(bs.getMp());
		pc.setStr(bs.getStr());
		pc.setCon(bs.getCon());
		pc.setDex(bs.getDex());
		pc.setInt(bs.getInt());
		pc.setCha(bs.getCha());
		pc.setWis(bs.getWis());
		if(bs.getLvStat()+1 == bs.getLevMax()-50) {
			pc.setLvStr(bs.getLvStr());
			pc.setLvDex(bs.getLvDex());
			pc.setLvCon(bs.getLvCon());
			pc.setLvWis(bs.getLvWis());
			pc.setLvInt(bs.getLvInt());
			pc.setLvCha(bs.getLvCha());
		}
		// 변경된 스탯 반영.
		pc.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), pc));
		// 글루딘 마을로 이동.
		pc.toTeleport(32628, 32798, 4, false);
		// 남은 스탯 알리기.
		pc.toLvStat(true);
	}
	
	static private void append(PcInstance pc) {
		synchronized (list) {
			list.put(pc.getObjectId(), new BaseStat(pc));
		}
	}
	
	static private void remove(PcInstance pc) {
		synchronized (list) {
			list.remove(pc.getObjectId());
		}
	}
	
	static private BaseStat find(PcInstance pc) {
		synchronized (list) {
			return list.get(pc.getObjectId());
		}
	}
	
	static private boolean contains(PcInstance pc) {
		synchronized (list) {
			return list.containsKey(pc.getObjectId());
		}
	}
}
