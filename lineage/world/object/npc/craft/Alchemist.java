/*
1회에 1000아데나
마법촉매 부족 - alchemy11
마법 촉매를 추출해내려면 매개체가 되는 속성석과 재료비가 필요합니다. 준비가 되시면 다시 와 주십시오. 
검은혈혼 부족 - alchemy12
검은 혈흔과 분리에 필요한 비용을 가지고 오셔야 속성석을 분리해드릴 수가 있습니다. 
 */

package lineage.world.object.npc.craft;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.lineage.Craft;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.world.controller.CraftController;
import lineage.world.object.instance.CraftInstance;
import lineage.world.object.instance.PcInstance;

	public class Alchemist extends CraftInstance {

		public Alchemist(Npc npc) {
			super(npc);

		}

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "alchemy1"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object... opt) {
		switch(action.charAt(0)) {
			case 'a':	// 마프르의 유산 1개		마력의 돌 20개
				toCraft(pc, ItemDatabase.find("마프르의 유산"), ItemDatabase.find("마력의 돌"), 20, 1);
				break;
			case 'b':	// 마프르의 유산 10개
				toCraft(pc, ItemDatabase.find("마프르의 유산"), ItemDatabase.find("마력의 돌"), 20, 10);
				break;
			case 'c':	// 마프르의 유산 50개
				toCraft(pc, ItemDatabase.find("마프르의 유산"), ItemDatabase.find("마력의 돌"), 20, 50);
				break;

			case 'j':	// 사이하의 유산 1개		마력의 돌 25개
				toCraft(pc, ItemDatabase.find("사이하의 유산"), ItemDatabase.find("마력의 돌"), 25, 1);
				break;
			case 'k':	// 사이하의 유산 10개
				toCraft(pc, ItemDatabase.find("사이하의 유산"), ItemDatabase.find("마력의 돌"), 25, 10);
				break;
			case 'l':	// 사이하의 유산 50개
				toCraft(pc, ItemDatabase.find("사이하의 유산"), ItemDatabase.find("마력의 돌"), 25, 50);
				break;

			case 's':	// 에바의 유산 1개		마력의 돌 30개
				toCraft(pc, ItemDatabase.find("에바의 유산"), ItemDatabase.find("마력의 돌"), 30, 1);
				break;
			case 't':	// 에바의 유산 10개
				toCraft(pc, ItemDatabase.find("에바의 유산"), ItemDatabase.find("마력의 돌"), 30, 10);
				break;
			case 'u':	// 에바의 유산 50개
				toCraft(pc, ItemDatabase.find("에바의 유산"), ItemDatabase.find("마력의 돌"), 30, 50);
				break;

			case 'B':	// 파아그리오의 유산 1개		마력의 돌 25개
				toCraft(pc, ItemDatabase.find("파아그리오의 유산"), ItemDatabase.find("마력의 돌"), 25, 1);
				break;
			case 'C':	// 파아그리오의 유산 10개
				toCraft(pc, ItemDatabase.find("파아그리오의 유산"), ItemDatabase.find("마력의 돌"), 25, 10);
				break;
			case 'D':	// 파아그리오의 유산 50개
				toCraft(pc, ItemDatabase.find("파아그리오의 유산"), ItemDatabase.find("마력의 돌"), 25, 50);
				break;

			case 'K':	// 검은 혈흔에서 4종류의 속성석을 분리 1개
				toCraft(pc, 1);
				break;
			case 'L':	// 검은 혈흔에서 4종류의 속성석을 분리 10개
				toCraft(pc, 5);
				break;
			case 'M':	// 검은 혈흔에서 4종류의 속성석을 분리 50개
				toCraft(pc, 10);
				break;
			case 'O':	// 검은 혈흔에서 4종류의 속성석을 분리 1개
				toCraft2(pc, 1);
				break;
			case 'P':	// 검은 혈흔에서 4종류의 속성석을 분리 10개
				toCraft2(pc, 5);
				break;
			case 'Q':	// 검은 혈흔에서 4종류의 속성석을 분리 50개
				toCraft2(pc, 10);
				break;
		}
	}
	
	private void toCraft(PcInstance pc, Item jeryo, Item item, int give_count, int count) {
		List<Craft> l = new ArrayList<Craft>();
		l.add( new Craft(jeryo, count) );
		l.add( new Craft(ItemDatabase.find("아데나"), 1000 * count) );
		if(CraftController.isCraft(pc, l, false)) {
			// 재료 제거
			CraftController.toCraft(pc, l);
			// 제작 아이템 지급.
			CraftController.toCraft(this, pc, item, give_count*count, true, 0, 0, 1);
		} else {
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "alchemy11"));
		}
		l.clear();
		l = null;
	}
	
	private void toCraft(PcInstance pc, int count) {
		List<Craft> l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("무기 마법 주문서"), 3 * count) );
		l.add( new Craft(ItemDatabase.find("마물의 기운"), count) );
		if(CraftController.isCraft(pc, l, false)) {
			// 재료 제거
			CraftController.toCraft(pc, l);
			// 제작 아이템 지급.
			CraftController.toCraft(this, pc, ItemDatabase.find("생명의 나뭇잎"), count, true, 0, 0, 1);

		} else {
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "alchemy12"));
		}
		l.clear();
		l = null;
	}
	
	private void toCraft2(PcInstance pc, int count) {
		List<Craft> l = new ArrayList<Craft>();
		l.add( new Craft(ItemDatabase.find("생명의 나뭇잎"), count) );
		l.add( new Craft(ItemDatabase.find("무기 마법 주문서"), count, 0, 0) );
		l.add( new Craft(ItemDatabase.find("갑옷 마법 주문서"), count, 0, 0) );
		if(CraftController.isCraft(pc, l, false)) {
			// 재료 제거
			CraftController.toCraft(pc, l);
			// 제작 아이템 지급.
			CraftController.toCraft(this, pc, ItemDatabase.find("생명의 나뭇잎"), count, true, 0, 0, 0);

		} else {
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "alchemy122"));
		}
		l.clear();
		l = null;
	}
}

