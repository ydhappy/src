package lineage.world.object.item.etc;

import lineage.database.MonsterDatabase;
import lineage.database.MonsterSpawnlistDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.thread.AiThread;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;

public class OmanSoul extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new OmanSoul();
		return item;
	}

	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){

		if(cha.getMap()>=101 && cha.getMap()<=105){
		
		MonsterInstance mon = null;

		if(this.item.getName().indexOf("제니스") > -1){
		//	World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, 20, String.format("\\fT%s님께서 소막 지역에 영혼석을 사용하였습니다.", cha.getName())));
			
			mon = MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("왜곡의 제니스 퀸"));
			if(mon != null){
				mon.setHomeX(cha.getX()+1);
				mon.setHomeY(cha.getY()+1);
				mon.setHomeMap(cha.getMap());
				mon.setHeading(cha.getHeading());
				mon.toTeleport(cha.getX(), cha.getY(), cha.getMap(), false);
				mon.readDrop();
				AiThread.append(mon);
			}
		}else if(this.item.getName().indexOf("시어") > -1){
		//	World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, 20, String.format("\\fT%s님께서 소막 지역에 영혼석을 사용하였습니다.", cha.getName())));

			mon = MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("불신의 시어"));
			if(mon != null){
				mon.setHomeX(cha.getX()+1);
				mon.setHomeY(cha.getY()+1);
				mon.setHomeMap(cha.getMap());
				mon.setHeading(cha.getHeading());
				mon.toTeleport(cha.getX(), cha.getY(), cha.getMap(), false);
				mon.readDrop();
				AiThread.append(mon);
			}			
		}else if(this.item.getName().indexOf("뱀파이어") > -1){
		//	World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, 20, String.format("\\fT%s님께서 소막 지역에 영혼석을 사용하였습니다.", cha.getName())));

			mon = MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("공포의 뱀파이어"));
			if(mon != null){
				mon.setHomeX(cha.getX()+1);
				mon.setHomeY(cha.getY()+1);
				mon.setHomeMap(cha.getMap());
				mon.setHeading(cha.getHeading());
				mon.toTeleport(cha.getX(), cha.getY(), cha.getMap(), false);
				mon.readDrop();
				AiThread.append(mon);
			}			
		}else if(this.item.getName().indexOf("좀비로드") > -1){
		//	World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, 20, String.format("\\fT%s님께서 소막 지역에 영혼석을 사용하였습니다.", cha.getName())));

			mon = MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("죽음의 좀비 로드"));
			if(mon != null){
				mon.setHomeX(cha.getX()+1);
				mon.setHomeY(cha.getY()+1);
				mon.setHomeMap(cha.getMap());
				mon.setHeading(cha.getHeading());
				mon.toTeleport(cha.getX(), cha.getY(), cha.getMap(), false);
				mon.readDrop();
				AiThread.append(mon);
			}			
					
		}else if(this.item.getName().indexOf("쿠거") > -1){
		//	World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, 20, String.format("\\fT%s님께서 소막 지역에 영혼석을 사용하였습니다.", cha.getName())));

			mon = MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("지옥의 쿠거"));
			if(mon != null){
				mon.setHomeX(cha.getX()+1);
				mon.setHomeY(cha.getY()+1);
				mon.setHomeMap(cha.getMap());
				mon.setHeading(cha.getHeading());
				mon.toTeleport(cha.getX(), cha.getY(), cha.getMap(), false);
				mon.readDrop();
				AiThread.append(mon);
			}			
		}
		cha.getInventory().count(this, getCount()-1, true);
		World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), null, 20, String.format("\\fT 오만의 탑에서 영혼석을 사용하였습니다.")));
	}else{
		
		ChattingController.toChatting(cha, "\\fT 오만의 탑에서 영혼석을 사용하실 수 있습니다.");
		return;
	}
	}
	}
