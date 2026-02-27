package sp.item;

import lineage.database.BackgroundDatabase;
import lineage.database.ItemDatabase;
import lineage.database.PolyDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.network.packet.server.S_ObjectGfx;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CraftController;
import lineage.world.controller.FishingController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.BackgroundInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.magic.ShapeChange;
import sp.background.낚시찌;

public class 낚싯대 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 낚싯대();
		return item;
	}
	
	private boolean autoFishing = true;	// 자동 낚시 여부.
	static private int delay = 20;				// 한턴 딜레이
	private BackgroundInstance bi;
	private int selectX;
	private int selectY;
	private String[][] items = {
			{
				"작은 은빛 베리아나",	
				"20"
			},
	};

	public 낚싯대() {
		bi = 낚시찌.clone(BackgroundDatabase.getPool(낚시찌.class));
		bi.setObjectId(ServerDatabase.getEtc_objid());
		bi.setName("낚시찌");
	}
	
	@Override
	public void close(){
		super.close();
		
		if(bi != null)
			bi.toTalk(null, null);
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		long objId = cbp.readD();
		int x = cbp.readH();
		int y = cbp.readH();
		
		// 낚시할 범위 설정.
		int x_s = 32797;	// x 최소값32809
		int x_e = 32804;	// x 최대값32818
		int y_s = 32791;	// y 최소값33425
		int y_e = 32811;	// y 최대값33434
		//
		if(x>=x_s && x<=x_e && y>=y_s && y<=y_e) {
			if(cha.getInventory().getSlot(8) == null) {
				if(Util.isDistance(x, y, cha.getMap(), cha.getX(), cha.getY(), cha.getMap(), Lineage.SEARCH_LOCATIONRANGE/3)) {
					// 재료 확인.
					ItemInstance jeryo = cha.getInventory().find(ItemDatabase.find(Lineage.fish_rice));
					if(jeryo != null) {
						setNowTime(10);
						selectX = x;
						selectY = y;
						cha.getInventory().count(jeryo, jeryo.getCount()-1, true);
						BuffController.append(this, this);
					} else {
						// \f1%0%s 부족합니다.
						cha.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null,
								Lineage.CHATTING_MODE_MESSAGE, String.format("(%s)가 충분치 않습니다.", Lineage.fish_rice)));
					}
				} else {
					ChattingController.toChatting(cha, "너무 먼곳에 던질 수 없습니다.", 20);
				}
			} else
				ChattingController.toChatting(cha, "무장을 해제하신 후 다시 시도해 주십시오.", 20);
		} else {
			ChattingController.toChatting(cha, "그곳에서는 낚시를 할 수 없습니다.", 20);
		}
	}
	
	@Override
	public void toBuffStart(object o) {
		cha.setFishStartHeading(cha.getHeading());
		cha.setFishing(true);
		cha.setFishingTime(System.currentTimeMillis());
		FishingController.setFishEffect(cha);
		ChattingController.toChatting(cha, "낚시를 시작합니다.", Lineage.CHATTING_MODE_MESSAGE);
	}
	
	@Override
	public void toBuff(object o) {
		//
		if(autoFishing) {
			if(getNowTime() == 1) {
				appendItem();
				setNowTime(delay);
				// 재료 부족시 종료.
				ItemInstance jeryo = cha.getInventory().findDbNameId(5247);
				if(jeryo != null)
					cha.getInventory().count(jeryo, jeryo.getCount()-1, true);
				else
					setNowTime(1);
			}
		}
		//
		if(!Util.isDistance(cha, bi, Lineage.SEARCH_LOCATIONRANGE/3))
			setNowTime(1);
	}

	@Override
	public boolean toBuffStop(object o) {
		//
		bi.setGfx(6385);
		bi.toSender(S_ObjectGfx.clone(BasePacketPooling.getPool(S_ObjectGfx.class), bi), false);
		bi.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), bi, 0), false);
		// 아이템 지급.
		appendItem();
		//
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
		//
		if(bi != null)
			bi.toTalk(null, null);
		//
		BuffController.remove(cha, ShapeChange.class);
	}
	
	private boolean appendItem() {
		if(Util.random(0, 100) < 100-(getNowTime()*10)){
			int idx = 0;
			int count = 100;
			do {
				if (count-- < 0) {
					break;
				}
				String[] item_data = items[idx++%items.length];
				if(Util.random(0, 100) <= Util.random(0, Integer.valueOf(item_data[1]))) {
					CraftController.toCraft(cha, ItemDatabase.find(item_data[0]), 1, true, 0);
					return true;
				}
			} while(true);
		}
		return false;
	}

}
