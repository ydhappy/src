package lineage.world.object.robot;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Skill;
import lineage.bean.lineage.Book;
import lineage.bean.lineage.Kingdom;
import lineage.database.SpriteFrameDatabase;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.BookController;
import lineage.world.controller.KingdomController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.GuardInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.NpcInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.RobotInstance;
import lineage.world.object.instance.SummonInstance;
import lineage.world.object.npc.kingdom.KingdomCastleTop;
import lineage.world.object.npc.kingdom.KingdomDoor;

public class WizardZoneRobotInstance extends PcRobotInstance {

	@Override
	public void toAi(long time) {
		// 필드에서만 체력 확인해서 귀환하기.
		if(World.isSafetyZone(getX(), getY(), getMap())==false && getHpPercent()<=GOTOHOME_FERCENT) {
			// 너무 도망을 잘 치기 때문에 확률적으로 처리.
			if(Util.random(0, 99) <= 30) {
				gotoHome(true);
				ai_time = SpriteFrameDatabase.find(getGfx(), getGfxMode()+0);
				return;
			}
		}
		// 공격자 목록 유효성 검사.
		for(object o : getAttackList()){
			if(!isAttack(o, false) || containsAstarList(o))
				removeAttackList(o);
		}
		//
		switch(getAiStatus()) {
			// 공격목록이 발생하면 공격모드로 변경
			case 0:
				if(getAttackListSize()>0)
					setAiStatus(1);
				// 변신상태가 변질되면
//				if(pcrobot_mode==PCROBOT_MODE.None && isBadPolymorph())
//					pcrobot_mode = PCROBOT_MODE.BadPolymorph;
				// 무게가 무거울경우.
				if(pcrobot_mode==PCROBOT_MODE.None && getInventory().isWeightPercent(82)==false)
					pcrobot_mode = PCROBOT_MODE.InventoryHeavy;
				break;
			// 전투 처리부분은 항상 타켓들이 공격가능한지 확인할 필요가 있음.
			case 1:
				// 무게가 무거울경우.
				if(pcrobot_mode==PCROBOT_MODE.None && getInventory().isWeightPercent(82)==false)
					pcrobot_mode = PCROBOT_MODE.InventoryHeavy;
				// 전투목록이 없을경우 랜덤워킹으로 변경.
				if(getAttackListSize() == 0)
					setAiStatus(0);
				break;
		}
		// 모드가 변경되면
		if(pcrobot_mode != PCROBOT_MODE.None)
			setAiStatus(0);
		//
		super.toAi(time);
	}
	
	@Override
	protected void toAiWalk(long time) {
		ai_time = SpriteFrameDatabase.find(gfx, gfxMode+0);
		//
		switch(pcrobot_mode) {
			case InventoryHeavy:
				toInventoryHeavy();
				return;
//			case BadPolymorph:
//				toBadPolymorph();
//				return;
		}
		// 버프 시전.
		List<Skill> skill_list = SkillController.find(this);
		if(toSkillHealHp(skill_list) || toSkillBuff(skill_list) || toSkillSummon(skill_list)) {
			ai_time = SpriteFrameDatabase.find(getGfx(), getGfxMode()+19);
			return;
		}
		// 
		if(World.isSafetyZone(getX(), getY(), getMap()) || KingdomController.isKingdomInsideLocation(this)) {
			// 사냥터 이동.
			List<Book> list = BookController.find(this);
			// 등록된 사냥터 없으면 무시.
			if(list.size() == 0)
				return;
			//
			List<Book> temp = new ArrayList<Book>();
			for(Book b : list) {
				if(b.getType()==null || !b.getType().equalsIgnoreCase("wizard_zone"))
					continue;
				temp.add(b);
			}
			//
			Book b = temp.get(Util.random(0, temp.size()-1));
			temp.clear();
			setHomeMap(b.getMap());
			int roop_cnt = 0;
			int lx = b.getX();
			int ly = b.getY();
			int loc = 8;
			do {
				lx = Util.random(b.getX()-(loc/2), b.getX()+(loc/2));
				ly = Util.random(b.getY()-(loc/2), b.getY()+(loc/2));
				if(roop_cnt++>100)
					break;
			}while(
					!World.isThroughObject(lx, ly+1, b.getMap(), 0) || 
					!World.isThroughObject(lx, ly-1, b.getMap(), 4) || 
					!World.isThroughObject(lx-1, ly, b.getMap(), 2) || 
					!World.isThroughObject(lx+1, ly, b.getMap(), 6) ||
					!World.isThroughObject(lx-1, ly+1, b.getMap(), 1) ||
					!World.isThroughObject(lx+1, ly-1, b.getMap(), 5) || 
					!World.isThroughObject(lx+1, ly+1, b.getMap(), 7) || 
					!World.isThroughObject(lx-1, ly-1, b.getMap(), 3) ||
					((b.getMap()==0 || b.getMap()==4) && World.isSafetyZone(lx, ly, b.getMap()))
				);
			setHomeX(lx);
			setHomeY(ly);
			super.toTeleport(lx, ly, b.getMap(), true);
		} else {
			// 타켓 찾기.
			for(object o : getInsideList(true)) {
				if(isAttack(o, false)) {
					// 공격목록에 등록.
					if(!containsAstarList(o))
						addAttackList(o);
				}
			}
			
		}
	}
	
	@Override
	protected void toAiAttack(long time) {
		ai_time = SpriteFrameDatabase.find(gfx, gfxMode+1);
		// 공격자 확인.
		object o = findDangerousObject();
		// 객체를 찾지못했다면 무시.
		if(o == null)
			return;
		//
		if(Util.isDistance(this, o, Lineage.SEARCH_LOCATIONRANGE-2) && Util.isAreaAttack(this, o)) {
			// 버프 시전.
			List<Skill> skill_list = SkillController.find(this);
			if(toSkillAttack(skill_list, o, false)) {
				ai_time = SpriteFrameDatabase.find(getGfx(), getGfxMode()+19);
				return;
			}
		}
	}
	
	@Override
	protected boolean isAttack(object o, boolean walk) {
		if(o == null)
			return false;
		if(o.isDead())
			return false;
		if(o.isWorldDelete())
			return false;
		if(o.getGm()>0)
			return false;
		if(o.isTransparent())
			return false;
		if(o.isBuffAbsoluteBarrier())
			return false;
		if(!Util.isDistance(this, o, Lineage.SEARCH_ROBOT_TARGET_LOCATION))
			return false;
		if(!(o instanceof Character))
			return false;
		if(o instanceof KingdomCastleTop || o instanceof KingdomDoor)
			return false;
		if(o instanceof RobotInstance || (o instanceof NpcInstance && !(o instanceof GuardInstance)))
			return false;
		if(o instanceof MonsterInstance && o.getGfxMode()!=o.getClassGfxMode())
			return false;
		if(o instanceof MonsterInstance && ((MonsterInstance)o).getBoss()!=null)
			return false;
		//
		Kingdom k = getClanId()>0 ? KingdomController.find(this) : null;
		if(k!=null && k.isWar()) {
			if(o instanceof GuardInstance)
				return false;
			if(KingdomController.isKingdomLocation(o, k.getUid()) == false)
				return false;
			return getClanId() != o.getClanId();
		} else {
			if(o instanceof PcInstance) {
				if(Lineage.server_version >= 163)
					return World.isNormalZone(getX(), getY(), getMap()) && containsAttackList(o);
				else
					return World.isNormalZone(getX(), getY(), getMap()) && containsAttackList(o);
			}
			if(o instanceof SummonInstance || o instanceof GuardInstance)
				return containsAttackList(o);
		}
		// 체크하려는 타켓이 다른 객체와 전투중이면 무시.
		// 랜덤워킹 시도중 체크일때만.
		if(walk && o instanceof MonsterInstance && !(o instanceof SummonInstance)) {
			MonsterInstance mi = (MonsterInstance)o;
			if(mi.getAttackList().size()!=0 && !(mi.getAttackList().get(0) instanceof RobotInstance))
				return false;
		}
		//
		return true;
	}

}
