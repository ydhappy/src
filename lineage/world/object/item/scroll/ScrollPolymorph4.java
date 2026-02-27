package lineage.world.object.item.scroll;

import lineage.bean.database.Poly;
import lineage.database.ItemDatabase;
import lineage.database.PolyDatabase;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.BuffRing;
import lineage.world.object.magic.MaanWatar;
import lineage.world.object.magic.ShapeChange;

public class ScrollPolymorph4 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ScrollPolymorph4();
		return item;
	}
		
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){

		if(cha.getInventory().find(ItemDatabase.find("변신 지배 반지")) != null){
			cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "monlistsRing"));
		}
	}
			
		@Override
		public void toClick(Character cha, String action, String type, ClientBasePacket cbp){// 여기에 도착해요
			if (cha.isSetPoly) {
				ChattingController.toChatting(cha, "세트 변신 때문에 다른 변신을 하실 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			Poly p = PolyDatabase.getPolyName(action);

			boolean hasRingPoly = false;
			switch(p.getPolyName()) {
				case "Platinum death knight":
				case "Doppelganger":
				case "Flame death knight":
				case "zillian of moon":
				case "Dragon Slayer":
				case "Platinum Baphomet":
				case "girtas":
					hasRingPoly = true;
					break;
			}
			if(hasRingPoly && cha instanceof PcInstance) {
				if(!cha.getInventory().has지배변반()) {
					ChattingController.toChatting(cha, String.format("변신 지배 반지를 소지 해야 변신이 가능 합니다."), Lineage.CHATTING_MODE_MESSAGE);
					return;
				}
			}
	         cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
	         if (ShapeChange.init(cha, cha, PolyDatabase.getPolyName(action), 3600, this.getBress())) {

	 	 		if (cha.getInventory() != null) {
					BuffRing.onBuff(cha, SkillDatabase.find(507));
		 		}

		}
		}
	}