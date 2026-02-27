package lineage.world.object.monster;

import lineage.bean.database.Monster;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;

public class Circu extends MonsterInstance {
	
	static synchronized public MonsterInstance clone(MonsterInstance mi, Monster m){
		if(mi == null)
			mi = new Circu();
		return MonsterInstance.clone(mi, m);
	}
	/**
	 * 타 클레스 찾아서 공격목록에 넣는 함수.
	 */
	private boolean toSearchHuman(){
		boolean find = false;
		for(object o : getInsideList(true)){
			if(o instanceof PcInstance){
				PcInstance pc = (PcInstance)o;
				if(isAttack(pc, true) && pc.getClassType()!=0x02){
					addAttackList(pc);
					find = true;
				}
			}
		}
		return find;
	}
	


	protected void toAiWalk(long time, PcInstance pc){
		super.toAiWalk(time);
		if(toSearchHuman()){

	}
 }
}