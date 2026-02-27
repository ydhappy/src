package lineage.world.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.bean.database.MagicdollList;
import lineage.bean.lineage.Doll;
import lineage.database.ItemDatabase;
import lineage.database.MagicdollListDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.thread.AiThread;
import lineage.world.World;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MagicDollInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.RobotInstance;
import lineage.world.object.item.MagicDoll;

public class MagicDollController {
	
	static private Map<PcInstance, Doll> list;
	static private List<Doll> pool;
	static private List<MagicDollInstance> pool_md;
	
	static public void init() {
		TimeLine.start("MagicDollController..");
		
		list = new HashMap<PcInstance, Doll>();
		pool = new ArrayList<Doll>();
		pool_md = new ArrayList<MagicDollInstance>();
		
		TimeLine.end();
	}
	
	/**
	 * 월드 접속시 호출됨.
	 * @param pc
	 */
	static public void toWorldJoin(PcInstance pc) {
		Doll d = find(pc);
		if(d == null) {
			d = getPool();
			append(pc, d);
		}
		d.setPcInstance(pc);
	}
	
	/**
	 * 월드 아웃시 호출됨.
	 * @param pc
	 */
	static public void toWorldOut(PcInstance pc) {
		Doll d = find(pc);
		if(d == null)
			return;
		
		for(MagicDollInstance mdi : d.getListValue())
			mdi.toAiThreadDelete();
		remove(pc);
		setPool(d);
	}
	
	/**
	 * 마법인형 활성화 처리 함수.
	 * @param pc
	 * @param md
	 */
	static public void toEnabled(PcInstance pc, MagicDoll md) {
		Doll d = find(pc);
		if(d == null)
			return;
		
		// 중복소환체크 및 디비정보 확인.
		MagicDollInstance mdi = d.find(md);
		MagicdollList mdl = MagicdollListDatabase.find(md.getItem().getName());
		if(mdi!=null || mdl==null)
			return;

		// 최대갯수 확인.
		if(getSize(pc) >= Lineage.item_magicdoll_max) {
			ChattingController.toChatting(pc, "더이상 마법인형을 소환할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		// 같은 속성에 마법인형은 무시하기.
		if(d.isDoll(md)) {
			 ChattingController.toChatting(pc, "같은 종류에 마법인형은 중복소환이 불가능 합니다.", Lineage.CHATTING_MODE_MESSAGE);
			 return;
		}
		
		if (pc.isInvis()) {
			ChattingController.toChatting(pc, "투명상태에는 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		
		if((pc instanceof RobotInstance) == false) {
			if (mdl.getMaterialCount() > 0) {
				// 재료 확인.
				ItemInstance item = pc.getInventory().find(ItemDatabase.find(mdl.getMaterialName()));
				if (item == null || item.getCount() < mdl.getMaterialCount()) {
					String msg = "";
					if (item == null)
						msg = String.format("%s(%d)가 부족합니다.", mdl.getMaterialName(), mdl.getMaterialCount());
					else
						msg = String.format("%s(%d)가 부족합니다.", mdl.getMaterialName(),
								mdl.getMaterialCount() - item.getCount());
					ChattingController.toChatting(pc, msg, Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
				// 재료 제거.
				pc.getInventory().count(item, item.getCount() - mdl.getMaterialCount(), true);
			}
		}
		// 
		md.setEquipped(true);
		mdi = MagicDollInstance.clone(getPoolMd(), d, mdl);
		mdi.setObjectId(ServerDatabase.nextNpcObjId());
		mdi.setOwnName(pc.getName());
		mdi.setName(mdl.getDollName());
		mdi.setGfx(mdl.getDollGfx());
		mdi.setClassGfx(mdl.getDollGfx());
		pc.setMagicDoll(md);
		pc.setMagicDollinstance(mdi);
		// 스폰 처리.
		mdi.toTeleport(pc.getX(), pc.getY(), pc.getMap(), false);
		// 이팩트 표현.
		mdi.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), mdi, Lineage.doll_teleport_effect), false);

		// 인공지능 등록.
		AiThread.append(mdi);
		// 관리목록에 등록.
		d.append(md, mdi);
		// 마법인형 아이콘 표현.
	//	pc.toSender(S_BuffMagicDoll.clone(BasePacketPooling.getPool(S_BuffMagicDoll.class), mdl.getDollContinuous()));
		// 기능별 옵션 처리.
		MagicdollListDatabase.toOption(pc, mdl, true);
		
//		pc.toTeleport(pc.getX(), pc.getY(), pc.getMap(), true);
	}
	
	/**
	 * 마법인형 비활성화 처리 함수.
	 * @param pc
	 * @param md
	 */
	static public void toDisable(PcInstance pc, MagicDoll md, boolean change) {
		Doll d = find(pc);
		if(d == null)
			return;
		
		//
		MagicDollInstance mdi = d.find(md);
		MagicdollList mdl = MagicdollListDatabase.find(md.getItem().getName());
		if(mdi==null || mdl==null)
			return;

		// 이팩트 표현.
		mdi.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), mdi, Lineage.doll_teleport_effect), false);
		//
		md.setEquipped(false);
		// 
		mdi.toAiThreadDelete();
		
		if (change) {
			pc.setMagicDoll(null);
			pc.setMagicDollinstance(null);
		}
	
		//
	
		// 마법인형 아이콘 표현.
		//pc.toSender(S_BuffMagicDoll.clone(BasePacketPooling.getPool(S_BuffMagicDoll.class), 0));
		// 기능별 옵션 처리.
		MagicdollListDatabase.toOption(pc, mdl, false);
		
		pc.setChkDoll(false);
		mdi.clearList(true);
		World.remove(mdi);
		d.clear();
	}
	
	/**
	 * 주기적으로 호출됨.
	 * @param time
	 * @param temp
	 */
	static public void toTimer(long time, List<Object> temp) {
		synchronized (list) {
			temp.clear();
			temp.addAll(list.values());
		}
		for (Object o : temp) {
			Doll doll = (Doll) o;
			//
			try {
				doll.toTimer(time);
			} catch (Exception e) {
			}
			//
			try {
				for (MagicDoll md : doll.getListKey()) {
					MagicDollInstance mdi = doll.find(md);
					if (mdi != null && (mdi.getTimeEnd() <= 0 || mdi.getTimeEnd() >= time)) {
						if (doll.getMaster() != null && (doll.getMaster().isInvis() || doll.getMaster().isBuffInvisiBility())) {
							
							toDisable(doll.getMaster(), md, true);
						}
						continue;
					}
					toDisable(doll.getMaster(), md, true);
				}
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * 마법인형 소환갯수 리턴.
	 * @param pc
	 * @return
	 */
	static public int getSize(PcInstance pc) {
		Doll d = find(pc);
		return d==null ? 0 : d.getSize();
	}

	/**
	 * 관리중인 객체 전체 마스터에 좌표로 텔레포트.
	 * @param cha
	 */
	static public void toTeleport(PcInstance pc){
		Doll d = find(pc);
		if(d == null)
			return;
		
		for(MagicDollInstance mdi : d.getListValue())
			mdi.toTeleport(pc.getX(), pc.getY(), pc.getMap(), true);
	}
	
	/**
	 * 사용자가 죽엇을때 호출됨.
	 * @param pc
	 */
	static public void toDead(PcInstance pc) {
		Doll d = find(pc);
		if(d == null)
			return;
		
		for(MagicDoll md : d.getListKey()) {
			md.setEquipped(false);
			MagicdollListDatabase.toOption(pc, MagicdollListDatabase.find(md.getItem().getName()), false);
		}
		for(MagicDollInstance mdi : d.getListValue())
			mdi.toAiThreadDelete();
		pc.setMagicDollinstance(null);
		d.clear();
	}
	
	/**
	 * 관리중인 마법인형을 감추게 할지 여부를 처리함.
	 * @param pc
	 * @param hidden
	 */
	static public void setHidden(PcInstance pc, boolean hidden) {
		Doll d = find(pc);
		if(d == null)
			return;
		
		for(MagicDollInstance mdi : d.getListValue())
			mdi.setInvis(hidden);
	}
	
	static private MagicDollInstance getPoolMd() {
		MagicDollInstance mdi = null;
		synchronized (pool_md) {
			if(pool_md.size() > 0){
				mdi = pool_md.get(0);
				pool_md.remove(0);
			}
		}
		return mdi;
	}
	
	static public void setPoolMd(MagicDollInstance mdi) {
		mdi.close();
		synchronized (pool_md) {
			pool_md.add(mdi);
		}
	}
	
	static private Doll getPool() {
		Doll d = null;
		synchronized (pool) {
			if(Lineage.memory_recycle && pool.size() > 0){
				d = pool.get(0);
				pool.remove(0);
			}else{
				d = new Doll();
			}
		}
		return d;
	}
	
	static private void setPool(Doll d) {
		d.close();
		if(!Lineage.memory_recycle)
			return;
		synchronized (pool) {
			if(pool.contains(d) == false)
				pool.add(d);
		}
	}
	
	static public Doll find(PcInstance pc) {
		synchronized (list) {
			return list.get(pc);
		}
	}
	
	static private void append(PcInstance pc, Doll d) {
		synchronized (list) {
			list.put(pc, d);
		}
	}
	
	static private void remove(PcInstance pc) {
		synchronized (list) {
			list.remove(pc);
		}
	}
	
}
