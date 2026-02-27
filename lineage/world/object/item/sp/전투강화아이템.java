package lineage.world.object.item.sp;

import lineage.bean.database.Skill;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.magic.sp.마력증강마법;
import lineage.world.object.magic.sp.전투강화마법;
import lineage.world.object.magic.sp.체력증강마법;


public class 전투강화아이템 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 전투강화아이템();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if(BuffController.find(cha).find(전투강화마법.class) != null) {
			ChattingController.toChatting(cha, "이미 전투 강화 효과를 받고 있어, 사용할 수 없습니다.", 20);
			return;
		}
		Skill s = SkillDatabase.find(10001);
		if(s != null)
			//제거
			BuffController.remove(cha, 체력증강마법.class);
			//제거
			BuffController.remove(cha, 마력증강마법.class);
			// 스킬 적용.
			BuffController.append(cha, 전투강화마법.clone(BuffController.getPool(전투강화마법.class), cha, s, s.getBuffDuration()));
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, getItem().getEffect()), true);
		
		// 아이템 수량 갱신
		cha.getInventory().count(this, getCount()-1, true);
	}

}

