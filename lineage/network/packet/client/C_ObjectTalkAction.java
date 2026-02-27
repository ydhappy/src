package lineage.network.packet.client;

import java.util.List;
import java.util.StringTokenizer;

import jsn_soft.JsnSoft;
import jsn_soft.S_MonDrop;
import lineage.bean.database.Monster;
import lineage.database.BackgroundDatabase;
import lineage.database.MonsterDatabase;
import lineage.database.NpcSpawnlistDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.persnal_shop.PersnalShopInstance;
import lineage.share.Lineage;
import lineage.share.Lineage_Balance;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CommandController;
import lineage.world.object.object;
import lineage.world.object.item.던전북;
import lineage.world.object.item.인첸트복구주문서;
import lineage.world.object.item.all_night.환상지배부적;
import lineage.world.object.instance.BoardInstance;
import lineage.world.object.instance.PcInstance;

public class C_ObjectTalkAction extends ClientBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, byte[] data,
			int length) {
		if (bp == null)
			bp = new C_ObjectTalkAction(data, length);
		else
			((C_ObjectTalkAction) bp).clone(data, length);
		return bp;
	}

	public C_ObjectTalkAction(byte[] data, int length) {
		clone(data, length);
	}

	@Override
	public BasePacket init(PcInstance pc) {// 1번 거칩니다. 제일먼저 우선순위 1 만약 없다
		// 버그 방지.
		if (pc == null || pc.isWorldDelete() || !isRead(4))
			return this;

		long objId = readD();
		String action = readS();
		String type = readS();
		object o = pc.findInsideList(objId);

		// System.out.println(action);
		StringTokenizer st = new StringTokenizer(type);
		BoardInstance b1 = BackgroundDatabase.getServerBoard();
		BoardInstance b2 = BackgroundDatabase.getTradeBoard();

		if (action.equalsIgnoreCase("shop3")) {
			object shop = NpcSpawnlistDatabase.sellNpc;
			if (shop != null) {
				shop.toTalk(pc, null);
				pc.setTempShop(shop);
			}
			return this;
		}
		
		// f1 자동칼질
		if (action.equalsIgnoreCase("pc_auto_attack_on")) {
			CommandController.자동칼질(pc, true);
			return this;
		}

		// f1 자동칼질
		if (action.equalsIgnoreCase("pc_auto_attack_off")) {
			CommandController.자동칼질(pc, false);
			return this;
		}
		
		if (action.equalsIgnoreCase("characterInfo")) {
			CommandController.characterInfo(pc, st);
			return this;
		}
		
		if (action.equalsIgnoreCase("serverInfo")) {
			CommandController.serverInfo(pc, st);
			return this;
		}
		
		if (action.equalsIgnoreCase("inven1")) {
			CommandController.inventorySetting(pc, true);
			return this;
		}
		if (action.equalsIgnoreCase("server_NO")) {
			pc.setBoard(2);
			b1.toClick(pc, null);
			return this;
		}

		if (action.equalsIgnoreCase("a-trade_NO")) {
			pc.setBoard(3);
			b2.toClick(pc, null);
			return this;
		}
		// 보스 시간표
		if (action.contains("bossList-")) {
			NpcSpawnlistDatabase.bosstime.toTalk(pc, action, type, this);
			return this;
		}

		// 퀘스트
		/*if (action.contains("kquest-")) {
			if (pc.getQuestChapter() >= Lineage.lastquest) {
				ChattingController.toChatting(pc, "퀘스트를 전부 완료했습니다.", Lineage.CHATTING_MODE_MESSAGE);
			} else {
				NpcSpawnlistDatabase.quest.toTalk(pc, action, type, this);
				return this;
			}
			return this;
		}*/
		// 랜덤퀘스트
		if (action.contains("kquest2-")) {
			NpcSpawnlistDatabase.quest2.toTalk(pc, action, type, this);
			return this;
		}
		
		// 출석체크
		if (action.contains("playcheck-")) {
			if(pc.getDaycount() >= Lineage_Balance.lastday ){
				ChattingController.toChatting(pc, "출석체크를 전부 완료하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}else{
				NpcSpawnlistDatabase.playcheck.toTalk(pc, action, type, this);
				return this;
			}
			return this;
		}
		
		// f1 마크
		if (action.equalsIgnoreCase("pc_clan_mark_on")) {
			CommandController.마크(pc, true);
			return this;
		}
		
		// f1 마크
		if (action.equalsIgnoreCase("pc_clan_mark_off")) {
			CommandController.마크(pc, false);
			return this;
		}
		
		// f1 파티드랍메세지
		if (action.equalsIgnoreCase("pc_party_msg_on")) {
			CommandController.파티드랍메세지(pc, true);
			return this;
		}
		
		// f1 파티드랍메세지
		if (action.equalsIgnoreCase("pc_party_msg_off")) {
			CommandController.파티드랍메세지(pc, false);
			return this;
		}
		if (action.equalsIgnoreCase("outop")) {
			NpcSpawnlistDatabase.autoPotion.toTalk(pc, null);
			return this;
		}
		if (action.equalsIgnoreCase("itemS")) {
			NpcSpawnlistDatabase.itemSwap.toTalk(pc, null);
			return this;
		}
		// f1 작업
		if (action.equalsIgnoreCase("pc_dungeon_book_NO")) {
			if (NpcSpawnlistDatabase.사냥터이동책 != null) {
				NpcSpawnlistDatabase.사냥터이동책.toTalk(pc, null);
			}

			return this;
		}
		if (action.equalsIgnoreCase("pc_dungeon_book_NO2")) {
			if (NpcSpawnlistDatabase.사냥터이동책2 != null) {
				NpcSpawnlistDatabase.사냥터이동책2.toTalk(pc, null);
			}

			return this;
		}
		if (action.equalsIgnoreCase("pc_town_book_NO")) {
			if (NpcSpawnlistDatabase.마을이동책 != null) {
				NpcSpawnlistDatabase.마을이동책.toTalk(pc, null);
			}

			return this;
		}
		if (action.equalsIgnoreCase("autohunt")) {
			if (NpcSpawnlistDatabase.auto_hunt != null) {
				NpcSpawnlistDatabase.auto_hunt.toClick(pc, null);
			}

			return this;
		}
		
		if (pc.getInventory() != null) {
			던전북 book = pc.getInventory().is던전북(pc, objId);
			if (book != null) {
				book.toTalk(pc, action, type, this);
				return this;
			}
		}
		
		if (pc.getInventory() != null) {
			환상지배부적 book = pc.getInventory().is환상지배부적(pc, objId);
			if (book != null) {
				book.toTalk(pc, action, type, this);
				return this;
			}
		}
		if (pc.getInventory() != null) {		
			인첸트복구주문서 enchant = pc.getInventory().is인첸트복구주문서(pc, objId);
			if (enchant != null) {
				enchant.toTalk(pc, action, type, this);
				return this;
			}
		}
		if (NpcSpawnlistDatabase.사냥터이동책 != null
				&& objId == NpcSpawnlistDatabase.사냥터이동책.getObjectId()) {
			NpcSpawnlistDatabase.사냥터이동책.toTalk(pc, action, type, this);
			return this;
		}
		if (NpcSpawnlistDatabase.사냥터이동책2 != null
				&& objId == NpcSpawnlistDatabase.사냥터이동책2.getObjectId()) {
			NpcSpawnlistDatabase.사냥터이동책2.toTalk(pc, action, type, this);
			return this;
		}
		if (NpcSpawnlistDatabase.마을이동책 != null
				&& objId == NpcSpawnlistDatabase.마을이동책.getObjectId()) {
			NpcSpawnlistDatabase.마을이동책.toTalk(pc, action, type, this);
			return this;
		}
		// 자동물약
		if (objId == NpcSpawnlistDatabase.autoPotion.getObjectId()) {
			NpcSpawnlistDatabase.autoPotion.toTalk(pc, action, type, this);
			return this;
		}
		
		if (objId == NpcSpawnlistDatabase.auto_hunt.getObjectId()) {
			NpcSpawnlistDatabase.auto_hunt.toClick(pc, action, type, this);
			return this;
		}

		if (action.contains("idrop")) {
			// idrop을 뺀 나머지 숫자  1 2 3 4 순번을 지정해줍니당
			int num = Integer.valueOf(action.replace("idrop", ""));
			// 우리가 아까 만들었던 Map 을 불러와줍니당
			List<String> list = JsnSoft.Monster_list.get(pc.getObjectId());
			// 이제 클릭한 몬스터의 이름을 리스트에서 가져와줄게요
			String monstername = list.get(num -1);
			// 이제 몬스터를 디비에서 찾아주고?
			Monster mon = MonsterDatabase.find(monstername);
			// 띄워줍니다!
			if (mon != null) {
				pc.toSender(S_MonDrop.clone(BasePacketPooling.getPool(S_MonDrop.class), mon));
			}
		}

		if (action.contains("shoptel-")) {
			if (CommandController.ismarketLocation1(pc)) {
				List<String> list = pc.getShopNameList(); // 오 잘만들었네여
				int shoptel_num = Integer.parseInt(action.replaceAll("shoptel-", ""));
				pc.setSise_tel_name(list.get(shoptel_num));
				PersnalShopInstance pc_shop = World.findPersnalShop(pc.getSise_tel_name());
				// 일단 시세검색을 끝! 네 0일때는 제자리네요 넹 여기서 취향대로 글귀를 넣어도 되고 그렇습니당 ㅋ 홈xy를 쓰는이유는
				// 이게 temp역할도 할수있어서 비교하기에 가장 좋아영 네 ㅋ
				if (pc_shop.getX() != 0 && pc_shop.getY() != 0 && pc_shop.getMap() != 0) {
					pc.setHomeX(pc_shop.getX());
					pc.setHomeY(pc_shop.getY());
					pc.setHomeMap(pc_shop.getMap());
				} else {
					pc.setHomeX(pc.getX());
					pc.setHomeY(pc.getY());
					pc.setHomeMap(pc.getMap());
				}
				pc.toTeleport(pc.getHomeX(), pc.getHomeY(), pc.getHomeMap(), false);
			} else {
				ChattingController.toChatting(pc, "시장에서만 사용하실 수 있습니다.", Lineage.CHATTING_MODE_MESSAGE);
			}

		} else {
			if (action.contains("save swap") || action.contains("index_") || action.equalsIgnoreCase("equip item")
					|| action.equalsIgnoreCase("remove swap") ) {
				object swap = NpcSpawnlistDatabase.itemSwap;
				swap.toTalk(pc, action, type, this, this);
			}
			PersnalShopInstance pc_shop = World.findPersnalShop(type);
			if (o == null && pc_shop != null && PersnalShopInstance.isCommand(action)) {
				if (type.contains("")) {
					type = type.replaceAll("", "");// 의 상점
					pc_shop.toTalk(pc, action, type, this);
					return this;
				}
			}
			if (o != null && (pc.getGm() > 0 || !pc.isTransparent())) {
				if (action.equalsIgnoreCase("buy")) {

					o.toTalk(pc, action, type, this);
				} else {
					o.toTalk(pc, action, type, this);
				}

			}
		}

		return this;
	}
}