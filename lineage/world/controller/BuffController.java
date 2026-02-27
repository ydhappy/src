package lineage.world.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.bean.database.Skill;
import lineage.bean.lineage.Buff;
import lineage.bean.lineage.BuffInterface;
import lineage.bean.lineage.Clan;
import lineage.database.ItemDatabase;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.potion.HealingPotion;
import lineage.world.object.item.potion.InpHealingPotion;
import lineage.world.object.magic.BraveAura;
import lineage.world.object.magic.BreaveAvt;
import lineage.world.object.magic.GlowingAura;
import lineage.world.object.magic.GreaseAvt;
import lineage.world.object.magic.ShiningAura;

public final class BuffController {

	static private List<Buff> pool_buff;
	static private List<BuffInterface> pool;
	static private Map<object, Buff> list;
	static private Object sync;

	/**
	 * 초기화 함수.
	 */
	static public void init(){
		TimeLine.start("BuffController..");

		pool_buff = new ArrayList<Buff>();
		pool = new ArrayList<BuffInterface>();
		list = new HashMap<object, Buff>();
		sync = new Object();

		TimeLine.end();
	}

	/**
	 * 월드 접속시 호출됨.
	 * @param o
	 */
	static public void toWorldJoin(object o){
		Buff b = find(o);
		if(b == null){
			b = getPool(o);
			synchronized (list) {
				list.put(o, b);
			}
		}
	}

	/**
	 * 월드에서 나갈때 호출됨.
	 * @param o
	 */
	static public void toWorldOut(object o, boolean close) {
		if(close) {
			// 버프관리 제거 처리.
			Buff b = null;
			synchronized (list) {
				b = list.remove(o);
			}
			if(b != null)
				setPool(b);
		} else {
			// 월드 나간다는거 알리기.
			Buff b = find(o);
			if(b != null)
				b.toWorldOut();
			
			for(ItemInstance item : o.getInventory().getList()){
				Buff t = find(item);
				if(t != null){
					t.removeAll();
				}
			}
		}
	}

	/**
	 * 버프처리할 목록에 등록요청 처리하는 함수
	 * @param o		: 대상자
	 * @param bi	: 버프 객체
	 */
	static public void append(object o, BuffInterface bi){
		Buff b = find(o);
		if(b == null){
			b = getPool(o);
			synchronized (list) {
				list.put(o, b);
			}
		}
		b.append(bi);
	}

	/**
	 * 시간제 아이템을 위한 걸로 무조건적으로 교체한다
	 */
	static public void appendOnly(object o, BuffInterface bi) {
		Buff b = new Buff(o);
		b.append(bi);
		list.put(o, b);
	}
	
	/**
	 * 버프 강제 제거 요청 처리 함수.
	 * @param o
	 * @param c
	 */
	static public void remove(object o, Class<?> c) {
		Buff b = find(o);
		if(b != null)
			b.remove(c);
	}
	
	static public void removeOnly(object o) {
		Buff b = find(o);
		if (b != null) {
			b.clearList();
		}
		list.remove(o);
	}

	static public void removeBuff(object o, Class<?> c) {
		remove(o, c);
		list.remove(o);
	}
	
	/**
	 * 객체가 죽을시 호출됨.
	 *  :  Character.toReset 에서 사용중.
	 * @param o
	 */
	static public void toDead(object o, boolean world_out) {
		Buff b = find(o);
		if(b != null)
			b.toDead(world_out);
	}

	/**
	 * 버프 강제로 제거 요청 처리 함수.
	 *  : 적용된 버프 전체 해당됨.
	 * @param o
	 * @param revival
	 */
	static public void removeAll(object o){
		Buff b = find(o);
		if(b != null)
			b.removeAll();
	}

	/**
	 * 타이머에서 주기적으로 호출함.
	 */
	static public void toTimer(long time) {
		synchronized (sync) {
			// 등록된 목록 순회.
			Collection<Buff> buffList = getList();
			for (Buff b : buffList) {
//				System.out.println("c : "+b.getObject().getName()+" c : "+count++);
				try {
					b.toTimer(time);
				} catch (Exception e) {
					lineage.share.System.printf("%s : toTimer(long time)\r\n", BuffController.class.toString());
					lineage.share.System.println(e);
				}
			}
		}
	}

	/**
	 * 버프객체 재사용 관리 함수
	 * @param c
	 * @return
	 */
	static public BuffInterface getPool(Class<?> c){
		BuffInterface bi = null;

		if (Lineage.memory_recycle) {
			for (BuffInterface b : pool) {
				if (b.getClass().toString().equals(c.toString())) {
					bi = b;
					break;
				}
			}

			if (bi != null) {
				pool.remove(bi);
			}
		}
		// lineage.share.System.println("remove : "+pool.size());
		return bi;
	}

	/**
	 * 버프객체 재사용 관리 함수
	 * @param bi
	 */
	static public void setPool(BuffInterface bi){
		try {
			bi.close();
		} catch (Exception e) {
			lineage.share.System.printf("%s : setPool(BuffInterface bi)\r\n", BuffController.class.toString());
			lineage.share.System.println(e);
		}
		if (!Lineage.memory_recycle)
			return;
		synchronized (pool) {
			if (!pool.contains(bi))
				pool.add(bi);
		}

		// lineage.share.System.println("append : "+pool.size());
	}

	/**
	 * 버프 관리자 재사용 관리 함수
	 * @return
	 */
	static public Buff getPool(object o){
		if (!Lineage.memory_recycle)
			return new Buff(o);
		Buff b = null;
		synchronized (pool_buff) {
			if (pool_buff.size() > 0) {
				b = pool_buff.get(0);
				pool_buff.remove(0);
				b.setObject(o);
			} else {
				b = new Buff(o);
			}
		}

		// lineage.share.System.println("remove : "+pool_buff.size());
		return b;
	}

	/**
	 * 버프 관리자 재사용 관리 함수
	 * @param b
	 */
	static private void setPool(Buff b) {
		b.close();
		if (!Lineage.memory_recycle)
			return;
		synchronized (pool_buff) {
			if (!pool_buff.contains(b))
				pool_buff.add(b);
		}

		// lineage.share.System.println("append : "+pool_buff.size());
	}

	/**
	 * 버프 찾기
	 */
	static public Buff find(object o){
		synchronized (list) {
			return list.get(o);
		}
	}
	
	/**
	 * 스킬정보로 버프 찾기
	 * @param o
	 * @param s
	 * @return
	 */
	static public BuffInterface find(object o, Skill s) {
		Buff b = find(o);
		if(b == null)
			return null;
		return b.find(s);
	}
	
	/**
	 * 클레스명으로 버프 찾기
	 * 
	 * @param o
	 * @param s
	 * @return
	 */
	static public BuffInterface find(object o, Class<?> c) {
		Buff b = find(o);
		if (b == null)
			return null;
		return b.find(c);
	}

	static private List<Buff> getList(){
		synchronized (list) {
			return new ArrayList<Buff>( list.values() );
		}
	}

	static public int getPoolSize(){
		return pool.size();
	}

	static public int getPoolBuffSize(){
		return pool_buff.size();
	}
	
	public static void toRoyal(PcInstance pc){
		Buff buff = BuffController.find(pc);
		for(BuffInterface b : buff.getList()) {
			if(b.getSkill().getUid() == 82 && b.getTime()<=13){
				GlowingAura.init(pc, SkillDatabase.find(15, 1));//82
			}
			else if(b.getSkill().getUid() == 83 && b.getTime()<=9){
				ShiningAura.init(pc, SkillDatabase.find(15, 2));//83
			}
		}
	}
	
	/*20억원 */
	public static void toAden(PcInstance pc){
		Buff aden20 = BuffController.find(pc);
		if(aden20 != null){
			if(pc.getInventory().isAden(2000000000L,false)){
				ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find("1억원 금괴"));			
				if(ii != null){
					ii.setCount(1);
					ii.setDefinite(true);
					pc.getInventory().append(ii, 1);
					ItemDatabase.setPool(ii);	
					pc.getInventory().isAden(100000000L, true);
				}
			}
		}
	}
	public static void toBuffend(PcInstance o){
		Buff buff = BuffController.find(o);
		if(buff != null) {
		for(BuffInterface b : buff.getList()) {
			if(b.getSkill() != null)
				if(b.getTime()==10){
					ChattingController.toChatting(o, String.format("%s %d초 남았습니다.", b.getSkill().getName(), b.getTime()), 20);
				}
				if(b.getTime()==5){
					ChattingController.toChatting(o, String.format("%s %d초 남았습니다.", b.getSkill().getName(), b.getTime()), 20);
				}
				
				if(b.getTime()==1){
					o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, 4593), true);
				}
			}
		}
	}
	/*브레이브아바타*/
	public static void toBAVT(PcInstance pc){
		Buff bavt = BuffController.find(pc);
		if(bavt != null && pc.getClanId() != 0){			
			if(pc.getClassType()==0x00 && pc.isBuffBreaveAvt()==true){ //군주일때 , 브레이브 오라 켜져있을 떄
				// 혈맹원 추출.
				Clan c = ClanController.find(pc);
				
				if(c != null){
					for(PcInstance use : c.getList()){
						if(Util.isDistance(pc,use, 18)){
							if(use.isBuffBreaveAvt()==false){
								use.setBuffBreaveAvt(true);
								Skill skill = SkillDatabase.find(777);
								BuffController.append(use, BreaveAvt.clone(BuffController.getPool(BreaveAvt.class), skill, skill.getBuffDuration()));
							}
						}else{
							use.setBuffBreaveAvt(false);
							BuffController.remove(use, BreaveAvt.class);
						}
					}
				}
			}else if(pc.isBuffBreaveAvt()==true){
				Clan c = ClanController.find(pc);
				PcInstance royal = c.getRoyal();
				if( royal == null){
					BuffController.remove(pc, BreaveAvt.class);					
				}
			}
		}		
	}
	public static int toFindacbuff(PcInstance pc, int b_ac){
		Buff buffac = BuffController.find(pc);
		for(BuffInterface b : buffac.getList()){
			if (b.getSkill() == null)
				return 0;
			if(b.getSkill().getUid()==3){
				b_ac += 2;			
			}else if(b.getSkill().getUid()==83){
				b_ac += 8;
			}else if(b.getSkill().getUid()==96){
				b_ac += 6;
			}else if(b.getSkill().getUid()==104){
				b_ac += 7;			
			}else if(b.getSkill().getUid()==112){
				b_ac += 10;
			}else if(b.getSkill().getUid()==128){
				b_ac += 3;
			}
			//빼기
			else if(b.getSkill().getUid()==56){
				b_ac -= 12;
			}else if(b.getSkill().getUid()==55){
				b_ac -= 10;
			}
		}
		return b_ac;
	}

	public static void toGAVT(PcInstance pc){
		Buff gavt = BuffController.find(pc);
		if(gavt != null && pc.getClanId() != 0){
			if(pc.getClassType()==0x00 && pc.isBuffGreaseAvt()==true){ //군주일때 , 브레이브 오라 켜져있을 떄
				// 혈맹원 추출.
				Clan c = ClanController.find(pc);
				if(c != null){
					for(PcInstance use : c.getList()){
						if(Util.isDistance(pc,use, 18)){
							if(use.isBuffGreaseAvt()==false){
								use.setBuffGreaseAvt(true);
								Skill skill = SkillDatabase.find(778);
								BuffController.append(use, GreaseAvt.clone(BuffController.getPool(GreaseAvt.class), skill, skill.getBuffDuration()));
							}
						}else{
							use.setBuffGreaseAvt(false);
							BuffController.remove(use, GreaseAvt.class);
						}
					}
				}
			}else if(pc.isBuffGreaseAvt()==true){
				Clan c = ClanController.find(pc);
				PcInstance royal = c.getRoyal();
				if( royal == null){
					BuffController.remove(pc, GreaseAvt.class);					
				}
			}
		}		
	}
	
	static public int addBuffTime(object cha, Skill skill) {
		int time = 0;
		BuffInterface bi = find(cha, skill);

		if (bi != null)
			time += bi.getTime();

		if (time > 3600)
			time = 3600;

		return time;
	}

		/*
		
		//버프물약 찾아서 사용하는 함수
		Buff buff = BuffController.find(pc);
		for(BuffInterface b : buff.getList()){
			
			switch(pc.getClassType()){
			case 0:
				if(b.getSkill().getUid() == 504 && b.getTime()<=5){
				 ItemInstance use3 = pc.getInventory().find(ItemDatabase.find("강화 악마의 피"));
				 if(use3 == null){
					 ChattingController.toChatting(pc, "강화 악마의 피가 부족하여 자동물약이 중지되었습니다.", 20);
						pc.setAuto(false);
				 }else{
					if(use3.isClick(pc)){
							use3.toClick(pc, null);
						}
					}
			 }
				break;
			case 1:
				if(b.getSkill().getUid() == 506 && b.getTime()<=5){
					 ItemInstance use5 = pc.getInventory().find(ItemDatabase.find("강화 용기의 물약"));
					 if(use5 == null){
						 ChattingController.toChatting(pc, "강화용기의물약이 부족하여 자동물약이 중지되었습니다.", 20);
							pc.setAuto(false);
					 }else{
						if(use5.isClick(pc)){
							use5.toClick(pc, null);
							}
						}
					 
				 	}
				break;
			case 2:
				if(b.getSkill().getUid() == 503 && b.getTime()<=5){
					 ItemInstance use2 = pc.getInventory().find(ItemDatabase.find("강화 엘븐 와퍼"));
					 if(use2 == null){
						 ChattingController.toChatting(pc, "강화엘븐와퍼가 부족하여 자동물약이 중지되었습니다.", 20);
							pc.setAuto(false);
					 }else{
						if(use2.isClick(pc)){
								use2.toClick(pc, null);
							}
						}
				 }
				break;
			case 3:
				if(b.getSkill().getUid() == 505 && b.getTime()<=5){
					 ItemInstance use4 = pc.getInventory().find(ItemDatabase.find("강화 위스키"));
					 if(use4 == null){
						 ChattingController.toChatting(pc, "강화 위스키가 부족하여 자동물약이 중지되었습니다.", 20);
							pc.setAuto(false);
					 }else{
						if(use4.isClick(pc)){
							use4.toClick(pc, null);
							}
						}
				 }
				if(b.getSkill().getUid() == 502 && b.getTime()<=5){
					 ItemInstance use1 = pc.getInventory().find(ItemDatabase.find("농축 지혜의 물약"));
					 if(use1 == null){						 
					 }else{
						if(use1.isClick(pc)){
								use1.toClick(pc, null);
							}
						}
				 }
				break;
			}
			
			if(b.getSkill().getUid() == 501 && b.getTime()<=5){
				 ItemInstance use = pc.getInventory().find(ItemDatabase.find("고대의 파란 물약"));
				 if(use == null){
				 }else{
					if(use.isClick(pc)){
							use.toClick(pc, null);
						}
					}
			 }
			if(b.getSkill().getUid() == 43 && b.getTime()<=5){
				 ItemInstance use = pc.getInventory().find(ItemDatabase.find("강화 초록 물약"));				 
				 if(use == null){
					 ChattingController.toChatting(pc, "강화초록물약이 부족하여 자동물약이 중지되었습니다.", 20);
						pc.setAuto(false);
				 }else{
					if(use.isClick(pc)){
							use.toClick(pc, null);
						}
					}
			 }
		 
		}*/
	}


