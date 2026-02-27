package jsn_soft;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.MonsterSpawnlistDatabase;
import lineage.database.NpcSpawnlistDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.client.C_ItemClick;
import lineage.network.packet.server.S_Html;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class Plugins_Of_Test {

	static public boolean toPlugins(object o, String msg, boolean isString) {
		// 기본적인 오류를 잡아줄 조건을 걸어줍니다.
		if (o == null)
			return false;
		// if (o == null) return false; << 일단 오류방지니까 음 두 널포인트 잡는거져? 맞아용
		if (!(o instanceof PcInstance)) // << 이거는 피씨만 사용하도록 유도하는거에용
			return false;
		// 언제나 오류가 날 수 있는 코드를 짤때는 혹은 불안정할때는 try문을 써주는건 필수에용
		try {
			// 플러그인은 피씨, 유저밖에 사용할 수 없으니 이문장이 기본적인 플러그인 들어가기 전의 기초뼈대입니당
			PcInstance pc = (PcInstance) o;

		
			String message = null; // 글자가 들어갈 변수
			long value = 0; // 숫자가 들어갈 변수
			ItemInstance item = null; // 숫자일 경우 혹은 글자일경우 아이템을 인벤에서 찾게해줄 변수
			object npc = null; // 숫자 혹은 글자일 경우 엔피씨를 데이터베이스에서 찾게해줄 변수

			// 글자일 경우
			if (isString) {

				// 글자일 경우엔 명령어식으로 만들어도 되용
				if (!msg.startsWith("."))// 무조건 .으로 시작 안하면 리턴 되고
					return false; // .으로 시작하지 않으면 리턴~ 바로 명령어!
			
				
				message = msg.replace(".", "");// 메세지는 ."" 이거고

				// 숫자일 경우
			} else {
				// 숫자일경우에는
				value = Long.valueOf(msg);
				// 아이템을 찾거나
				item = pc.getInventory().value(value);
				// 엔피씨를 찾거나
				if (item == null)
					npc = NpcSpawnlistDatabase.find(value);
				// 피씨를 찾거나
				if (npc == null)
					npc = World.findPc(value);
				// 몬스터를 찾거나
				if (npc == null)
					npc = MonsterSpawnlistDatabase.find(value);
			}

		
			if (message != null) {
			
				String[] m = message.split(" ");


			} else if (value != 0) {
				// 이렇게 걸러서 만들어주면?
				if (item != null) {
					Item i2 = ItemDatabase.find(item.getItem().getName());
					ItemInstance html = pc.getInventory().find(AutoHunt.class);
					if (AutoHuntController.getAction() != null
							&& AutoHuntController.getAction().equalsIgnoreCase("addsell")) {
						for (Item i : pc.sell_List) {
							if (i == i2) {
								ChattingController.toChatting(pc,
										String.format("\\fR중복된 아이템이 이미 존재합니다 : %s", i2.getName()),
										Lineage.CHATTING_MODE_MESSAGE);
								return true;
							}
						}
						pc.sell_List.add(i2);
//						AutoHuntDatabase.insertSellList(pc, i2);
						ChattingController.toChatting(pc, String.format("판매물품 추가완료 : %s", i2.getName()),
								Lineage.CHATTING_MODE_MESSAGE);
						AutoHuntController.setAction(null);
						pc.setAutocommand(false);
						List<String> info = new ArrayList<String>();
						info.clear();

						info.add(pc.isSellItem() ? "ON" : "OFF");

						if (pc.sell_List.size() > 0) {
							for (Item i : pc.sell_List) {
								info.add(i.getName());
							}
						}

						for (int i = 0; i < (20 - pc.sell_List.size()); i++) {
							info.add(" ");
						}

						pc.toSender(
								S_Html.clone(BasePacketPooling.getPool(S_Html.class), html, "autohunt3", null, info));
						pc.setAutocommand(false);
						return true;
						
					} else if (AutoHuntController.getAction() != null
							&& AutoHuntController.getAction().equalsIgnoreCase("delsell")) {
						if (pc.sell_List != null && pc.sell_List.contains(i2)) {
							pc.sell_List.remove(i2);
							ChattingController.toChatting(pc, String.format("판매물품 삭제완료 : %s", i2.getName()),
									Lineage.CHATTING_MODE_MESSAGE);
							AutoHuntController.setAction(null);
							pc.setAutocommand(false);
							List<String> info = new ArrayList<String>();
							info.clear();

							info.add(pc.isSellItem() ? "ON" : "OFF");

							if (pc.sell_List.size() > 0) {
								for (Item i : pc.sell_List) {
									info.add(i.getName());
								}
							}

							for (int i = 0; i < (20 - pc.sell_List.size()); i++) {
								info.add(" ");
							}

							pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), html, "autohunt3", null,
									info));
							pc.setAutocommand(false);
							return true;

						}else 
							ChattingController.toChatting(pc, String.format("등록 되지 않은 물품 : %s", i2.getName()),
									Lineage.CHATTING_MODE_MESSAGE);
					}

				}

			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
