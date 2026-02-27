package lineage.world.object.monster;

import lineage.bean.database.Monster;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;

public class Nancy extends MonsterInstance {
	
	static synchronized public MonsterInstance clone(MonsterInstance mi, Monster m){
		if(mi == null)
			mi = new Nancy();
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
	@Override
	protected void toAiWalk(long time){
		super.toAiWalk(time);
		
		//타 클레스 찾기.
		if(toSearchHuman())
		ChattingController.toChatting(this, "어서 나와 위대한 마력의 소유자에게 용서를 빌라!", Lineage.CHATTING_MODE_SHOUT);
	}
	
	@Override
	public boolean isAttack(Character cha, boolean magic) {
		if(getGfxMode() != getClassGfxMode())
			return false;
		return super.isAttack(cha, magic);
	}
}