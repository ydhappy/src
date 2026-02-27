package lineage.world.object.item.quest;

import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class 신성한에바의물 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 신성한에바의물();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if( !isClick(cha) )
			return;
		
		// 이팩트 표현
		if(getItem().getEffect() > 0)
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, getItem().getEffect()), true);
		// 버프 처리
		lineage.world.object.magic.item.신성한에바의물.init(cha, SkillDatabase.find(207).getBuffDuration());
		// 아이템 수량 갱신
		cha.getInventory().count(this, getCount()-1, true);
	}

}
