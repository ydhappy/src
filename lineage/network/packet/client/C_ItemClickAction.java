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
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CommandController;
import lineage.world.object.object;
import lineage.world.object.instance.BoardInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class C_ItemClickAction extends ClientBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, byte[] data,
			int length) {
		if (bp == null)
			bp = new C_ItemClickAction(data, length);
		else
			((C_ItemClickAction) bp).clone(data, length);
		return bp;
	}

	public C_ItemClickAction(byte[] data, int length) {
		clone(data, length);
	}

	@Override
	public BasePacket init(PcInstance pc) {// 일로 2번 거칩니
		// 버그 방지.
		if (pc == null || pc.isWorldDelete() || !isRead(4))
			return this;
		long objId = readD();
		ItemInstance item = pc.getInventory().value(objId);

		String action = readS();
		String type = readS();

		StringTokenizer st = new StringTokenizer(type);
		BoardInstance b = BackgroundDatabase.getRankBoard();
		BoardInstance b1 = BackgroundDatabase.getServerBoard();
		BoardInstance b2 = BackgroundDatabase.getTradeBoard();

		if (action.equalsIgnoreCase("rank")) {
			pc.setBoard(1);
			b.toClick(pc, null);
			return this;
		}
		if (action.equalsIgnoreCase("server")) {
			pc.setBoard(2);
			b1.toClick(pc, null);
			return this;
		}
		if (action.equalsIgnoreCase("a-trade")) {
			pc.setBoard(3);
			b2.toClick(pc, null);
			return this;
		}
		if (action.equalsIgnoreCase("전투켬")) {
			pc.toTalk(pc, action, type, this);
			return this;
		}
		if (action.equalsIgnoreCase("전투끔")) {
			pc.toTalk(pc, action, type, this);
			return this;
		}
		if (action.equalsIgnoreCase("파티메세지켬")) {
			pc.toTalk(pc, action, type, this);
			return this;
		}
		if (action.equalsIgnoreCase("파티메세지끔")) {
			pc.toTalk(pc, action, type, this);
			return this;
		}
		if (action.equalsIgnoreCase("자동칼질켬")) {
			pc.toTalk(pc, action, type, this);
			return this;
		}
		if (action.equalsIgnoreCase("자동칼질끔")) {
			pc.toTalk(pc, action, type, this);
			return this;
		}

		if (action.contains("idrop")) {
			// idrop을 뺀 나머지 숫자 1 2 3 4 순번을 지정해줍니당
			int num = Integer.valueOf(action.replace("idrop", ""));
			// 우리가 아까 만들었던 Map 을 불러와줍니당
			List<String> list = JsnSoft.Monster_list.get(pc.getObjectId());
			// 이제 클릭한 몬스터의 이름을 리스트에서 가져와줄게요
			String monstername = list.get(num - 1);
			// 이제 몬스터를 디비에서 찾아주고?
			Monster mon = MonsterDatabase.find(monstername);
			// 띄워줍니다!
			if (mon != null) {
				pc.toSender(S_MonDrop.clone(
						BasePacketPooling.getPool(S_MonDrop.class), mon));
				return this;
			}
		}
		if (action.contains("shoptel-")) {
			if (CommandController.ismarketLocation(pc)) {
				List<String> list = pc.getShopNameList(); // 오 잘만들었네여
				int shoptel_num = Integer.parseInt(action.replaceAll(
						"shoptel-", ""));
				pc.setSise_tel_name(list.get(shoptel_num));
				PersnalShopInstance pc_shop = World.findPersnalShop(pc
						.getSise_tel_name());
				// 일단 시세검색을 끝! 네 0일때는 제자리네요 넹 여기서 취향대로 글귀를 넣어도 되고 그렇습니당 ㅋ 홈xy를
				// 쓰는이유는
				// 이게 temp역할도 할수있어서 비교하기에 가장 좋아영 네 ㅋ
				if (pc_shop.getX() != 0 && pc_shop.getY() != 0
						&& pc_shop.getMap() != 0) {
					pc.setHomeX(pc_shop.getX());
					pc.setHomeY(pc_shop.getY());
					pc.setHomeMap(pc_shop.getMap());
				} else {
					pc.setHomeX(pc.getX());
					pc.setHomeY(pc.getY());
					pc.setHomeMap(pc.getMap());
				}
				pc.toTeleport(pc.getHomeX(), pc.getHomeY(), pc.getHomeMap(),
						false);
				return this;
			} else {
				// ChattingController.toChatting(pc, "글루딘 제제소 에서만 사용가능 합니다.",
				// Lineage.CHATTING_MODE_MESSAGE);
				return this;
			}

		} else {
			PersnalShopInstance pc_shop = World.findPersnalShop(type);
			if (item == null && pc_shop != null
					&& PersnalShopInstance.isCommand(action)) {
				if (type.contains("")) {
					type = type.replaceAll("", "");// 의 상점
					pc_shop.toTalk(pc, action, type, this);
					return this;
				}
			}
			if (item != null && (pc.getGm() > 0 || !pc.isTransparent()))
				item.toClick(pc, action, type, this);
		}

		return this;
	}
}