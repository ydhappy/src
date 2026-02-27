package lineage.world.object.item;

import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_FishingPacket;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectMode;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CraftController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.ShapeChange;

public class FishingPole extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new FishingPole();
		return item;
	}
	
	//
	static private boolean autoFishing = true;		// 자동 낚시 여부.
	private int selectX;
	private int selectY;
	static private int delay = 12;				// 한턴 딜레이
	//
	static private String[][] fish_rate = {
		{"강한 물고기", "10"},
		{"어린 물고기", "60"},
		{"재빠른 물고기", "30"},
		{"반짝이는 비늘", "3"},
		{"초록 빛 나는 물고기", "10"},
		{"붉은 빛 나는 물고기", "10"},
		{"파란 빛 나는 물고기", "10"},
		{"흰 빛 나는 물고기", "10"},
		{"진귀한 거북이", "1"},
		{"신비한 날개깃털", "50"}
	};
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
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
					ItemInstance jeryo = cha.getInventory().findDbNameId(5247);
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
	/*	int x = cbp.readH();
		int y = cbp.readH();
		if(	World.get_map(x, y, cha.getMap())==16
				||
			(
				cha.getMap()==4 && x>=33436 && x<=32806 && y>=33422 && y<=32821
			)
				) {
			// 미끼 찾기.
			ItemInstance bait = cha.getInventory().findDbNameId(5247);
			if(bait != null) {
				if(cha.isFish()==false)
					cha.setFish(true);
				if(cha.getGfx()!=cha.getClassGfx())
					BuffController.remove(cha, ShapeChange.class);
				// 미끼 제거.
				cha.getInventory().count(bait, bait.getCount()-1, true);
				// 버프 등록.
				setX(x);
				setY(y);
				setNowTime(delay);
				BuffController.append(cha, this);
			} else {
				// \f1%0%s 부족합니다.
				cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 337, "미끼"));
			}
		}
	} */

	@Override
	public void toBuffStart(object o) {
		// 모드 변경.
		o.setGfxMode(71);
		o.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), o, getX(), getY()), true);
	}
	
	@Override
	public void toBuff(object o) {
		//
		if(autoFishing) { //20180524
			if(getNowTime() == 1) {
				if(o.isFish()==false)
					o.setFish(true);
				toFish();
				setNowTime(delay);
				// 재료 부족시 종료.
				ItemInstance jeryo = o.getInventory().findDbNameId(5247);
				if(jeryo != null){
					o.getInventory().count(jeryo, jeryo.getCount()-1, true);
					if (o instanceof PcInstance){
						PcInstance pc = (PcInstance)o;
						if(pc.getLevel()<55)
							pc.toExp(this, Util.random(1, 2));
						else{
							pc.toExp(this, Util.random(5, 10));
						}						
					}					
				}else {
					setNowTime(1);
					// \f1%0%s 부족합니다.
					o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 337, "미끼"));
					
					if(o.isFish()==true)
						o.setFish(false);
				}
			}
			return;
		}
		
		//
		if(Util.random(0, 5) >= getTime()) {
			BuffController.remove(o, getClass());
			o.toSender(S_FishingPacket.clone(BasePacketPooling.getPool(S_FishingPacket.class)));
		}
	}

	@Override
	public boolean toBuffStop(object o) {
		//
		o.setGfxModeClear();
		//
		o.toSender(S_ObjectMode.clone(BasePacketPooling.getPool(S_ObjectMode.class), o), true);
		o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 1136));
		//
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
		toBuffStop(o);
	}

	@Override
	public void toClickFinal(Character cha, Object... opt){
		// 버프 제거.
		BuffController.remove(cha, getClass());
		//
		toFish();
	}
	
	private void toFish() {
		// 확률 체크.
		if(Util.random(0, 7) >= getTime()) {
			// 땅에 아이템 드랍.
			for(String[] fish : fish_rate) {
				if(Integer.valueOf(fish[1]) <= Util.random(0, 100))
					continue;
				
				CraftController.toCraft(cha, ItemDatabase.find(fish[0]), 1, true, 0);
				break;
			}
		}
	}

}
