package lineage.world.object.item.potion;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.util.Util;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.item.newacc.Earring;
import lineage.world.object.item.newacc.Earring2;
import lineage.world.object.magic.AbsoluteBarrier;

public class InpHealingPotion extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new InpHealingPotion();
		return item;
	}
	
	@Override
		public void toClick(Character cha, ClientBasePacket cbp){
			if( !isClick(cha) )
				return;

			// 앱솔상태 해제.
			if(cha.isBuffAbsoluteBarrier())
				BuffController.remove(cha, AbsoluteBarrier.class);
			// 패킷처리
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, getItem().getEffect()), true);
			// \f1기분이 좋아졌습니다.
			if(cha.isAutoPotionMent() && Lineage.healingpotion_message)
				cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 77));
			
			// 체력 상승
			int hp = Util.random(getItem().getDmgMin(), getItem().getDmgMax());
			if(this.getBress()==0){
				hp = (int) (hp * 1.3);
			}else if(this.getBress()==2){
				hp = hp / 3;
			}
		//	hp += hp*(cha.getDynamicHealingPercent()*0.01);
		//	hp += hp*(cha.getEarring()*cha.getEarring2());
		//	hp += cha.getDynamicHealing();
			if(cha.isBuffPolluteWater())
				hp = hp / 2;
			cha.setNowHp(cha.getNowHp() + hp);
			// 아이템 수량 갱신
		}

}
