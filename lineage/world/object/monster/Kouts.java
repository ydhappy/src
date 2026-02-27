package lineage.world.object.monster;

import lineage.bean.database.Monster;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;

public class Kouts extends MonsterInstance {
	
	static synchronized public MonsterInstance clone(MonsterInstance mi, Monster m){
		if(mi == null)
			mi = new Kouts();
		return MonsterInstance.clone(mi, m);
	}

	/**
	 * 클레스 찾아서 공격목록에 넣는 함수.
	 */
	private boolean toSearchHuman(){
		boolean find = false;
		for(object o : getInsideList(true)){
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

	@Override
	protected void toAiWalk(long time){
		super.toAiWalk(time);
		
		if(toSearchHuman())
			ChattingController.toChatting(this, "...한번 보고 상대방의 실력을 파악하지 못하다니, 불쌍한 자들...", Lineage.CHATTING_MODE_SHOUT);
	}
	
	@Override
	public boolean isAttack(Character cha, boolean magic) {
		if(getGfxMode() != getClassGfxMode())
			return false;
		return super.isAttack(cha, magic);
	}
}