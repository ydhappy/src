package lineage.world.object.monster.event;

import lineage.bean.database.Monster;
import lineage.database.ItemDatabase;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.controller.EventController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;

public class Wanted extends MonsterInstance {
	
	static synchronized public MonsterInstance clone(MonsterInstance mi, Monster m){
		if(mi == null)
			mi = new Wanted();
		return MonsterInstance.clone(mi, m);
	}
	/**
	 * 타 클레스 찾아서 공격목록에 넣는 함수.
	 */
	private boolean toSearchHuman(){
		boolean find = false;
		for(object o : getInsideList()){
			if(o instanceof PcInstance){
				PcInstance pc = (PcInstance)o;
				if(isAttack(pc, true)){
					addAttackList(pc);
					find = true;
				}
			}
		}
		return find;
	}

	public void toAiAttack(long time) {
		// 도망모드로 전환.
		setAiStatus(Lineage.AI_STATUS_ESCAPE);
	}
	
	@Override
	public void toTeleport(final int x, final int y, final int map, final boolean effect) {
		// 이벤트 관리목록에 등록.
		EventController.appendbountyMonster(this);
		// 스폰.
		super.toTeleport(x, y, map, effect);
	}
	
	@Override
	protected void toAiWalk(long time){
		super.toAiWalk(time);
		//타 클레스 찾기.
		if(toSearchHuman())
		ChattingController.toChatting(this, "건방진 녀석들! 너희들이 날 잡을 수 있을 것 같으냐!", Lineage.CHATTING_MODE_NORMAL);
	}
	
	
	@Override
	protected void toAiDead(long time){
		// 할로윈 이벤트 관리목록에서 제거.
		EventController.removebountyMonster(this);
		
		super.toAiDead(time);
	}
	
	@Override
	public void toDamage(Character cha, int dmg, int type, Object... opt) {
		setDead(true);
		setAiStatus(Lineage.AI_STATUS_DEAD);
		// 죽을때 외치기
		ChattingController.toChatting(this, "크으윽! 내가... 내 수집품이...", Lineage.CHATTING_MODE_NORMAL);
	}
}