package lineage.world.object.item.wand;

import lineage.bean.database.Item;
import lineage.database.ItemPinewandDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.thread.AiThread;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.AbsoluteBarrier;

public class PineWand extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new PineWand();
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
		if (cha.isFishing()) {
			ChattingController.toChatting(cha, "낚시중에는 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		if(quantity<=0){
			cha.getInventory().count(this, getCount()-1, true);
			// \f1아무일도 일어나지 않았습니다.
			cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
			
			return;
		}
		
		if(World.isSafetyZone(cha.getX(), cha.getY(), cha.getMap()) || World.isCombatZone(cha.getX(), cha.getY(), cha.getMap())){
			ChattingController.toChatting(cha, "마을에서는 사용이 불가능 합니다.", 20);
			return;
		}
		
		
		
		setQuantity(quantity-1);
		cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));

		int x = cha.getX();
		int y = cha.getY();
		if(World.isThroughObject(x, y, cha.getMap(), cha.getHeading())){
			x += Util.getXY(cha.getHeading(), true);
			y += Util.getXY(cha.getHeading(), false);
		}

		MonsterInstance mon = ItemPinewandDatabase.newPineWandMonsterInstance();
		if(mon != null){
			mon.setHomeX(x);
			mon.setHomeY(y);
			mon.setHomeMap(cha.getMap());
			mon.setHeading(cha.getHeading());
			mon.toTeleport(x, y, cha.getMap(), false);
			mon.setPineWand(true);
			if(Lineage.monster_summon_item_drop)
				mon.readDrop();

			AiThread.append(mon);
		}
	}

}
