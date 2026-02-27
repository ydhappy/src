package lineage.world.object.item.potion;

import lineage.bean.lineage.Kingdom;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.KingdomController;
import lineage.world.controller.WantedController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.AbsoluteBarrier;

public class HealingPotion extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new HealingPotion();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if( !isClick(cha) )
			return;
		if (item.getLevelMin() > 0 && item.getLevelMin() > cha.getLevel()) {
			// cha.toSender(new SItemLevelFails(item.Level));
			// 672 : 이 아이템은 %d레벨 이상이 되어야 사용할 수 있습니다.
			ChattingController.toChatting(cha, String.format("이 아이템은 %d레벨 이상이 되어야 사용할 수 있습니다.", item.getLevelMin()), Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		if (item.getLevelMax() > 0 && item.getLevelMax() < cha.getLevel()) {
			// 673 : 이 아이템은 %d레벨 이하일때만 사용할 수 있습니다.
			ChattingController.toChatting(cha, String.format("이 아이템은 %d레벨 이하일 때만 사용할 수 있습니다.", item.getLevelMax()), Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
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
		
		// 공성존 인지 확인.
		Kingdom kingdom = KingdomController.findKingdomLocation(cha);
		// 성존이고 공성중일경우 힐링포션 안빠지도록
		if (kingdom != null && kingdom.isWar() ) {
			
		}else {
			if (getItem() != null && !getItem().getType2().equalsIgnoreCase("무한 물약"))
				// 아이템 수량 갱신
				cha.getInventory().count(this, getCount() - 1, true);
		}
//		
//		// 아이템 수량 갱신
//		cha.getInventory().count(this, getCount()-1, true);
	}
	
}
