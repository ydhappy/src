package lineage.world.object.npc.craft;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.lineage.Craft;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_Message;
import lineage.world.controller.BuffController;
import lineage.world.controller.CraftController;
import lineage.world.object.instance.CraftInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.ShapeChange;

public class Urye extends CraftInstance {
	
	public Urye(Npc npc){
		super(npc);
		
		Item i = ItemDatabase.find("어두운 하딘의 일기장");
		if(i != null){
			craft_list.put("d", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("하딘의 일기:1월 1일"), 1) );
			l.add( new Craft(ItemDatabase.find("하딘의 일기:2월 24일"), 1) );
			l.add( new Craft(ItemDatabase.find("하딘의 일기:2월 25일"), 1) );
			l.add( new Craft(ItemDatabase.find("하딘의 일기:5월 5일"), 1) );
			l.add( new Craft(ItemDatabase.find("하딘의 일기:6월 2일"), 1) );
			l.add( new Craft(ItemDatabase.find("하딘의 일기:6월 9일"), 1) );
			l.add( new Craft(ItemDatabase.find("하딘의 일기:8월 9일"), 1) );
			l.add( new Craft(ItemDatabase.find("하딘의 일기:8월 19일"), 1) );
			l.add( new Craft(ItemDatabase.find("하딘의 일기:10월 12일"), 1) );
			l.add( new Craft(ItemDatabase.find("하딘의 일기:11월 10일"), 1) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("시공의 구슬");
		if(i != null){
			craft_list.put("a", i);
			craft_list.put("b", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("아데나"), 10000) );
			l.add( new Craft(ItemDatabase.find("시공의 구슬"), 1) );
			list.put(i, l);
		}
		
		i = ItemDatabase.find("오림의 일기장");
		if(i != null){
			craft_list.put("e", i);
			
			List<Craft> l = new ArrayList<Craft>();
			l.add( new Craft(ItemDatabase.find("오림의 일기 6/14"), 1) );
			l.add( new Craft(ItemDatabase.find("오림의 일기 6/16"), 1) );
			l.add( new Craft(ItemDatabase.find("오림의 일기 6/18"), 1) );
			l.add( new Craft(ItemDatabase.find("오림의 일기 6/21"), 1) );
			l.add( new Craft(ItemDatabase.find("오림의 일기 6/22"), 1) );
			l.add( new Craft(ItemDatabase.find("오림의 일기 6/25"), 1) );
			l.add( new Craft(ItemDatabase.find("오림의 일기 7/05"), 1) );
			l.add( new Craft(ItemDatabase.find("오림의 일기 7/17"), 1) );
			l.add( new Craft(ItemDatabase.find("오림의 일기 7/18"), 1) );
			l.add( new Craft(ItemDatabase.find("오림의 일기 8/05"), 1) );
			l.add( new Craft(ItemDatabase.find("오림의 일기 8/08"), 1) );
			l.add( new Craft(ItemDatabase.find("오림의 일기 8/09"), 1) );
			l.add( new Craft(ItemDatabase.find("오림의 일기 8/10"), 1) );
			l.add( new Craft(ItemDatabase.find("오림의 일기 8/11"), 1) );
			l.add( new Craft(ItemDatabase.find("오림의 일기 8/12"), 1) );
			l.add( new Craft(ItemDatabase.find("오림의 일기 8/13"), 1) );
			l.add( new Craft(ItemDatabase.find("오림의 일기 8/14"), 1) );
			l.add( new Craft(ItemDatabase.find("오림의 일기 8/15"), 1) );
			list.put(i, l);
		}
	}
	
	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		if(pc.getInventory().findDbNameId(12831) == null) {
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "j_html01"));
		} else {
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "j_html00"));
		}
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt) {
		if(action.equalsIgnoreCase("a")) {
			Item craft = craft_list.get(action);
			List<Craft> l = list.get(craft);
			if(CraftController.isCraft(pc, l, true)) {
				CraftController.toCraft(pc, l);
				BuffController.remove(pc, ShapeChange.class);
				pc.setHeading(5);
				pc.toTeleport(32743, 32854, 9100, true);
			} else {
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "j_html02"));
			}
		} else if(action.equalsIgnoreCase("b")) {
			Item craft = craft_list.get(action);
			List<Craft> l = list.get(craft);
			if(CraftController.isCraft(pc, l, true)) {
				CraftController.toCraft(pc, l);
				BuffController.remove(pc, ShapeChange.class);
				pc.setHeading(5);
				pc.toTeleport(32740, 32855, 9202, true);
			} else {
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "j_html02"));
			}
		} else if(action.equalsIgnoreCase("c") && pc.getInventory().findDbNameId(12831)==null) {
			ItemInstance temp = ItemDatabase.newInstance(ItemDatabase.find(12831));
			pc.getInventory().append(temp, 1);
			pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 143, getName(), temp.toString()));
			ItemDatabase.setPool(temp);
		} else if(action.equalsIgnoreCase("d") || action.equalsIgnoreCase("e")) {
			Item craft = craft_list.get(action);
			List<Craft> l = list.get(craft);
			if(CraftController.isCraft(pc, l, true)) {
				CraftController.toCraft(pc, l);
				CraftController.toCraft(this, pc, craft, 1, true, 0, 0, 1);
			} else {
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "j_html06"));
			}
		}
	}

}
