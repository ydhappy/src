package lineage.world.object.item.wand;

import lineage.bean.database.Item;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectAttack;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.DamageController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.AbsoluteBarrier;
import lineage.world.object.magic.EraseMagic;

public class EbonyWand extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new EbonyWand();
		return item;
	}

	@Override
	public ItemInstance clone(Item item){
		quantity = Util.random(5, 15);
		
		return super.clone(item);
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		// 앱솔상태 해제.
		if (cha.isBuffAbsoluteBarrier())
			BuffController.remove(cha, AbsoluteBarrier.class);
		object o = null;
		long obj_id = cbp.readD();
		int x = cbp.readH();
		int y = cbp.readH();
		long time = System.currentTimeMillis();
	
//		if (cha.get투망시간() > time ) {
//			ChattingController.toChatting(cha, "투망딜레이:1초", Lineage.CHATTING_MODE_MESSAGE);
//			return;
//		}
		// 투망 상태에서는 사용할 수 없음.
		if(cha.isInvis()) {
			ChattingController.toChatting(cha, "아무일도 일어나지 않았습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		// 수량 확인.
		if(quantity<=0) {
		//	cha.getInventory().count(this, getCount()-1, true);
//			// 방향 전환.
//			cha.setHeading( Util.calcheading(cha, x, y) );
			// \f1아무일도 일어나지 않았습니다.
			if(cha.getClassGfx() != cha.getGfx())
				cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 20+17), true);
			else
				cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 17), true);
			cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
			return;
		}
		// 수량 하향
		setQuantity(quantity-1);
		cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));
		// 객체 찾기.
		if(obj_id == cha.getObjectId())
			o = cha;
		else
			o = cha.findInsideList(obj_id);
		
//		System.out.println("1:" + getItem().getEffect());
		// 처리.
		if(o == null){
//			System.out.println(cha.getName());
			if(cha.getClassGfx() != cha.getGfx())
			{
				cha.toSender(S_ObjectAttack.clone(BasePacketPooling.getPool(S_ObjectAttack.class), cha, 20+17, getItem().getEffect(), x, y), cha instanceof PcInstance);
			}
			else
			{
				cha.toSender(S_ObjectAttack.clone(BasePacketPooling.getPool(S_ObjectAttack.class), cha, 17, getItem().getEffect(), x, y), cha instanceof PcInstance);
			}
		
		}else{
			//
			int dmg = Util.random(1, cha.getTotalStr());
			if(World.isSafetyZone(o.getX(), o.getY(), o.getMap()))
				dmg = 0;
			if(World.isSafetyZone(cha.getX(), cha.getY(), cha.getMap()))
				dmg = 0;			
			//장애물 리턴
			if(!Util.isAreaAttack(cha, o))
				dmg = 0;
			
			
			if(cha.getClassGfx() != cha.getGfx())	
			{//20 + 17
				cha.toSender(S_ObjectAttack.clone(BasePacketPooling.getPool(S_ObjectAttack.class), cha, o, 20+17, dmg, 10, false, false, o.getX(), o.getY(), true), true);
			}
			else
			{// 17
				cha.toSender(S_ObjectAttack.clone(BasePacketPooling.getPool(S_ObjectAttack.class), cha, o, 17, dmg, 10, false, false, o.getX(), o.getY(), true), true);
			}			
			DamageController.toDamage(cha, o, dmg, 2);
			
			if (o.isBuffEraseMagic())//20181028
				BuffController.remove(o, EraseMagic.class);
			//
//			EnergyBolt.toBuff(cha, o, SkillDatabase.find(1, 3), 17, getItem().getEffect(), 30);
		}
	
		// 수량이 0개일때 제거
		if (getQuantity() < 1)
			cha.getInventory().count(this, getCount() - 1, true);
	}
}