package lineage.world.controller;

import lineage.database.ItemDatabase;
import lineage.database.PolyDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Html;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.ShapeChange;
import lineage.world.object.npc.event.Keplisha;

public class LuckyController {

	static private int[] BAD_POLY;			// 변신 리스트
	static private int[] GOOD_POLY;
	static private int[] PERFECT_POLY;
	static private int ADEN;				// 운세볼때 사용될 아덴
	static private enum TODAYLUCKY {			// 운세 종류
		Bad,
		Good,
		Perfect
	};
	
	static public void init() {
		TimeLine.start("LuckyController..");
		
		//
		BAD_POLY = new int[]{ 6181, 6185, 6189, 6193, 6197, 6201, 6205, 6209, 6213, 6217, 6221, 6225, 6229 };
		GOOD_POLY = new int[]{ 6180, 6184, 6188, 6192, 6196, 6200, 6204, 6208, 6212, 6216, 6220, 6224, 6228, 6232, 6234, 6236, 6238, 6240, 6242 };
		PERFECT_POLY = new int[]{ 6183, 6191, 6195, 6199, 6203, 6207, 6211, 6215, 6219, 6223, 6227, 6231, 6232, 6234, 6236, 6238, 6240, 6242 };
		ADEN = 1000;
		
		TimeLine.end();
	}
	
	static public void close() {
		
	}
	
	/**
	 * 케플리샤 클릭시 호출됨.
	 * @param npc
	 * @param pc
	 */
	static public void toLucky(Keplisha npc, PcInstance pc) {
		// 항아리 체크.
		if(pc.getInventory().findDbNameId(5204) == null) {
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), npc, "keplisha1"));
			return;
		}
		// 구슬 체크.
		if(pc.getInventory().findDbNameId(5205) != null) {
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), npc, "keplisha2"));
			return;
		}
		// 부적 체크.
		if(pc.getInventory().findDbNameId(5206) != null) {
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), npc, "keplisha3"));
			return;
		}
		// 이도저도 아니면.
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), npc, "keplisha4"));
	}
	
	/**
	 * 케플리샤 클릭 후 액션 취할때 호출됨.
	 * @param npc
	 * @param pc
	 * @param action
	 */
	static public void toLuckyFinal(Keplisha npc, PcInstance pc, String action) {
		if(action.equalsIgnoreCase("0")) {
			// 항아리 생성
			CraftController.toCraft(npc, pc, ItemDatabase.find(5204), 1, true, 0, 0, 1);
			// 케플리샤와 영혼의 계약을 맺는다.
			// keplisha4 또는 keplisha7
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), npc, "keplisha7"));
		}
		if(action.equalsIgnoreCase("1")) {
			// 후원금을 내고 운세를 본다
			// 아데나 체크
			if(pc.getInventory().isAden(ADEN, true)) {
				// 운세 보여주기 나중에 세밀하게 세팅
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), npc, "horosb38"));
				// 구슬 찾기.
				ItemInstance bead = pc.getInventory().findDbNameId(5205);
				if(bead != null) {
					// 구슬 삭제
					pc.getInventory().count(bead, bead.getCount()-1, true);
					// 부적 생성
					CraftController.toCraft(npc, pc, ItemDatabase.find(5206), 1, true, 0, 0, 1);
				}
			} else {
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), npc, "keplisha8"));
			}
		}
		if(action.equalsIgnoreCase("2")) {
			// 케플리샤의 축복을 받는다.
			if(pc.getGfx() != pc.getClassGfx()) {
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), npc, "keplisha9"));
			} else {
				ItemInstance charm = pc.getInventory().findDbNameId(5206);
				if(charm != null) {
					// 부적 삭제
					pc.getInventory().count(charm, charm.getCount()-1, true);
					// 변신
					toPoly(pc);
					// 변신후 마지막 npc창 이부분도 세밀하게 세팅해야됨.
					pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), npc, "horomon23"));
				}
			}
		}
		if(action.equalsIgnoreCase("3")) {
			// 옹기를 깨뜨려 계약을 파기한다.
		}
	}
	
	static private void toPoly(PcInstance pc) {
		int gfx = GOOD_POLY[Util.random(0, GOOD_POLY.length)];
		ShapeChange.init(pc, pc, PolyDatabase.getPolyGfx(gfx), -1, 1);
	}
}
