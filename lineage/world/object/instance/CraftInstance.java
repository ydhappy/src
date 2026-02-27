package lineage.world.object.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.lineage.Craft;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_HyperText;
import lineage.share.Lineage;
import lineage.world.controller.CraftController;

public class CraftInstance extends NpcInstance {
	protected Map<Item, List<Craft>> list;			// 제작될아이템(item) 과 연결된 재료 목록
	protected Map<String, List<Craft>> list2; // 제작될아이템의 액션값(item의) 과 연결된 재료 목록
	protected Map<String, Item> craft_list;			// 요청청 문자(action)와 연결될 제작될아이템(item)
	protected List<String> temp_request_list;		// hyper_text 패킷 그릴때 이용되는 변수.
	protected Map<Item, List<Craft>> eachItemList = new HashMap<Item, List<Craft>>();; // 제작될아이템(item) 과 연결된 재료 목록 (해당 리스트의 포함된 아이템중 하나만 있으면 됨)
	
	public CraftInstance(Npc npc){
		super(npc);
		craft_list = new HashMap<String, Item>();
		list = new HashMap<Item, List<Craft>>();
		temp_request_list = new ArrayList<String>();
		list2 = new HashMap<String, List<Craft>>();
	}
	
	@Override
	public Npc getNpc() {
		return npc;
	}

	public Map<Item, List<Craft>> getList() {
		return list;
	}

	public void setList(Map<Item, List<Craft>> list) {
		this.list = list;
	}

	public Map<String, List<Craft>> getList2() {
		return list2;
	}

	public void setList2(Map<String, List<Craft>> list2) {
		this.list2 = list2;
	}

	public Map<String, Item> getCraftList() {
		return craft_list;
	}

	public void setCraftList(Map<String, Item> craft_list) {
		this.craft_list = craft_list;
	}

	public List<String> getTempRequestList() {
		return temp_request_list;
	}

	public void setTempRequestList(List<String> temp_request_list) {
		this.temp_request_list = temp_request_list;
	}
	
	@Override
	public void setNowHp(int nowHp){
		if(this instanceof GuardInstance)
			super.setNowHp(nowHp);
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		Item craft = craft_list.get(action);
		if (craft != null) {
			//
			List<Craft> list_temp = list2.get(action);
			if (list_temp == null)
				list_temp = list.get(craft);
			// 재료 확인.
			if (CraftController.isCraft(pc, list_temp, true)) {
				// 제작 가능한 최대값 추출.
				int max = CraftController.getMax(pc, list_temp);
				if (Lineage.server_version <= 144)
					toFinal(pc, action, max);
				else
					// 패킷 처리.
					pc.toSender(new S_HyperText( this, "request", action, 0, 1, 1, max, temp_request_list));
			}
		}
	}
	
	@Override
	public void toHyperText(PcInstance pc, ClientBasePacket cbp){
		int count = (int)cbp.readD();
		if(count < 0 || count > 2000000000)
			return;
		cbp.readC();
		String action = cbp.readS();
		
		toFinal(pc, action, count);
	}
	
	/**
	 * 제작처리 마지막 부분.
	 *  : 중복코드 방지용
	 * @param pc
	 * @param action
	 * @param count
	 */
	private void toFinal(PcInstance pc, String action, long count){
		Item craft = craft_list.get(action);
//		System.out.println("FInalAction : "+action);
		if (craft != null) {
			//
			List<Craft> list_temp = eachItemList.get(craft) == null ?list2.get(action) : eachItemList.get(craft); 
			if (list_temp == null)
				list_temp = list.get(craft);
			//
			int max = CraftController.getMax(pc, list_temp);
			if (count > 0 && max > 0 && count <= max) {
				// 재료 제거
				for (int i = 0; i < count; ++i)
					CraftController.toCraft(pc, list_temp);
				// 제작 아이템 지급.
				int jegop = craft.getListCraft().get(action) == null ? 0 : craft.getListCraft().get(action);
				int en = craft.getListCraftEn().get(action) == null ? 0 : craft.getListCraftEn().get(action);
				int grade = craft.getListCraftGrade().get(action) == null ? 0 : craft.getListCraftGrade().get(action);
				int bress = craft.getListCraftBress().get(action) == null ? 1 : craft.getListCraftBress().get(action);
				if (jegop == 0)
					CraftController.toCraft(this, pc, craft, count, true, en, grade, bress);
				else
					CraftController.toCraft(this, pc, craft, count * jegop, true, en, grade, bress);
			}
		}
	}
}

