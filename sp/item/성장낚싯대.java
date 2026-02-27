package sp.item;

import lineage.database.BackgroundDatabase;
import lineage.database.ItemDatabase;
import lineage.database.PolyDatabase;
import lineage.database.ServerDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectGfx;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CraftController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.BackgroundInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.magic.ShapeChange;
import sp.background.성장낚시찌;

public class 성장낚싯대 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new 성장낚싯대();
		return item;
	}
	
	private boolean autoFishing = true;	// 자동 낚시 여부.
	static private int delay = 15;				// 한턴 딜레이
	static private double exp = 5000;
	private BackgroundInstance bi;
	private int selectX;
	private int selectY;
	private String[][] items = {
			{
				"신비한 날개깃털",	
				"20"
			},
	};

	public 성장낚싯대() {
		bi = 성장낚시찌.clone(BackgroundDatabase.getPool(성장낚시찌.class));
		bi.setObjectId(ServerDatabase.getEtc_objid());
		bi.setName("성장낚시찌");
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
		int x_s = 33424;	// x 최소값32809
		int x_e = 33434;	// x 최대값32818
		int y_s = 32808;	// y 최소값33425
		int y_e = 32819;	// y 최대값33434
		//
		if(x>=x_s && x<=x_e && y>=y_s && y<=y_e) {
			if(cha.getInventory().getSlot(8) == null) {
				if(Util.isDistance(x, y, cha.getMap(), cha.getX(), cha.getY(), cha.getMap(), Lineage.SEARCH_LOCATIONRANGE/3)) {
					// 재료 확인.
					ItemInstance jeryo = cha.getInventory().findDbItemId(1696);
					if(jeryo != null) {
						setNowTime(10);
						selectX = x;
						selectY = y;
						cha.getInventory().count(jeryo, jeryo.getCount()-1, true);
						BuffController.append(this, this);
					} else {
						// \f1%0%s 부족합니다.
						cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 337, "미끼"));
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
		bi.setGfx(6299);	// list.spr에서 type값을 12로 변경해야 합니다. 102.type(12)	그래야 멀리서 클릭이 가능함.
		((성장낚시찌)bi).toTeleport(cha, this, selectX, selectY, cha.getMap(), false);
		//
		ShapeChange.onBuff(cha, cha, PolyDatabase.getPolyName("fishing poly"), -1, false, true);
	}
	
	@Override
	public void toBuff(object o) {
		//
		if(autoFishing) {
			if(getNowTime() == 1) {
				toExpGive(cha);
				appendItem();
				setNowTime(delay);
				// 재료 부족시 종료.
				ItemInstance jeryo = cha.getInventory().findDbItemId(1696);
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
	
	private boolean toExpGive(object o){
		if(o == null){
			return false;
		} else {
			o.toExp(o, exp);
			return true;
		}
	}
}
