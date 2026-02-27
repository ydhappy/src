package lineage.world.object.monster;

import java.util.List;

import lineage.bean.database.Monster;
import lineage.share.Lineage;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;

public class Gremlin extends Slime {
	
	static synchronized public MonsterInstance clone(MonsterInstance mi, Monster m){
		if(mi == null)
			mi = new Gremlin();
		return MonsterInstance.clone(mi, m);
	}
	private int remove_item_time;
	@Override
	public void toAiAttack(long time) {
		// 도망모드로 전환.
		setAiStatus( 5 );
	}
	@Override
	public void toAi(long time){
		super.toAi(time);
		//아데나가 널이 아니니 있으면 삭제한다.
		if(inv.findAden()!=null){
			inv.getList().clear();
		}
		
		// 인벤토리에 있는 아이템 소화시키기.
		if(++remove_item_time >= 5){
			remove_item_time = 0;
			List<ItemInstance> list = inv.getList();
			if(list.size() > 0){
				ItemInstance temp = list.get(0);
				if(temp != null)
					inv.count(temp, 0, false);
			}
		}
	}
}
