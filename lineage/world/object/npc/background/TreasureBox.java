package lineage.world.object.npc.background;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectAttack;
import lineage.network.packet.server.S_ObjectMode;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.CharacterController;
import lineage.world.controller.CraftController;
import lineage.world.object.Character;
import lineage.world.object.instance.BackgroundInstance;

public class TreasureBox extends BackgroundInstance {
	
	private int SLEEP_TIME = 60 * 10;	// 10분
	private int current_time = 0;
	
	public TreasureBox(){
		CharacterController.toWorldJoin(this);
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		// 닫혀있을때만 처리.
		if(gfxMode == 29) {
			// 상자 열기.
			toOn();
			toSend();
			// 말섬1층 트랩상자
			if(getX()==32707 && getY()==32796 && getMap()==1) {
				int dmg = Util.random(10, 20);
				toSender(S_ObjectAttack.clone(BasePacketPooling.getPool(S_ObjectAttack.class), this, cha, 102, dmg, SkillDatabase.find(6, 4).getCastGfx(), false, false, cha.getX(), cha.getY(), true), false);
				return;
			}
			// 아이템 지급
			int count = 0;
			Item item = null;
			int rand = Util.random(0, 100);
			if(rand==0){
				// 젤
				count = Util.random(1, 3);
				item = ItemDatabase.find(249);
			}else if(rand == 100){
				// 데이
				count = Util.random(1, 3);
				item = ItemDatabase.find(244);
			}else if(rand > 50){
				// 아데나
				count = Util.random(100, 1000);
				item = ItemDatabase.find(4);
				if(item == null)
					item = ItemDatabase.find(4);
			}else{
				// 신비한 날개깃털
				count = Util.random(1, 10);
				item = ItemDatabase.find(5116);
			}
			CraftController.toCraft(cha, item, count, true, 0);
			// 확율적으로 금빛열쇠 지급.
			if(getMap()==1 && Util.random(0, 99) <= Util.random(0, cha.getLevel()/2))
				CraftController.toCraft(this, cha, ItemDatabase.find("금빛 열쇠"), 1, true, 0, 0, 1);
		}
	}
	
	@Override
	public void toTimer(long time){
		if(current_time++ >= SLEEP_TIME){
			current_time = 0;
			// 상자 닫기.
			toOff();
			toSend();
		}
	}
	
	public void toOn(){
		setGfxMode( 28 );
	}
	
	public void toOff(){
		setGfxMode( 29 );
	}
	
	public void toSend(){
		toSender(S_ObjectMode.clone(BasePacketPooling.getPool(S_ObjectMode.class), this), false);
	}

}
