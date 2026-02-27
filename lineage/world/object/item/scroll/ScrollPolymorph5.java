package lineage.world.object.item.scroll;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Poly;
import lineage.database.ItemDatabase;
import lineage.database.PolyDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_MessageYesNo;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.RankController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.BuffRing;
import lineage.world.object.magic.ShapeChange;

public class ScrollPolymorph5 extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ScrollPolymorph5();
		return item;
	}
		
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		int allRank = RankController.getAllRank(cha.getObjectId());
		int classRank = RankController.getRankClass(cha);
		List<String> info = new ArrayList<String>();
		info.clear();
		info.add(cha.getFast_poly() == null || cha.getFast_poly().equalsIgnoreCase("") || cha.getFast_poly().length() < 1 ? "빠른변신 없음" : cha.getFast_poly());
		
		if ((((allRank > 0 && allRank <= Lineage.rank_poly_all) || 
				(classRank > 0 && classRank <= Lineage.rank_poly_class)) && cha.getLevel() >= Lineage.rank_min_level2) || 
				Lineage.event_rank_poly || cha.getGm() > 0) {
			switch(cha.getClassType()) {
			case 0x00:
				if(cha.getClassSex() == 0)
					cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "monlistR", null, info));
				else
			cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "monlistRr", null, info));
					break;

				case 0x01:
					if(cha.getClassSex() == 0)
				cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "monlistRk", null, info));
					else
				cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "monlistRkk", null, info));
						break;
						
				case 0x02:
					if(cha.getClassSex() == 0)
				cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "monlistRe", null, info));
					else
				cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "monlistRee", null, info));
						break;
						
				case 0x03:
					if(cha.getClassSex() == 0)
				cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "monlistRm", null, info));
					else
				cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "monlistRmm", null, info));
						break;
				}
		}else{
			cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "monlist", null, info));
		}
	}
			
		@Override
		public void toClick(Character cha, String action, String type, ClientBasePacket cbp){// 여기에 도착해요

			Poly p = PolyDatabase.getPolyName(action);

			if(!action.equalsIgnoreCase("milkcow") && !action.equalsIgnoreCase("빠른 변신") && !action.equalsIgnoreCase("랭커 변신") && p == null) {
				return;
			}
			
			if (action != null && action.length() > 0) {
				if (action.equalsIgnoreCase("랭커 변신")) {
					action = PolyDatabase.toRankPolyMorph(cha, action);
					
					// 버그방지
					if (action.equalsIgnoreCase("랭커 변신"))
						return;
				}
			}
			if (action.equalsIgnoreCase("빠른 변신")) {
				// 버그방지
				if (cha.getFast_poly() == null || cha.getFast_poly().equalsIgnoreCase("") || cha.getFast_poly().length() < 1) {
					return;
				} else {
					action = cha.getFast_poly();
				}
			}

	         cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
	         
	         if (ShapeChange.init(cha, cha, PolyDatabase.getPolyName(action), 1200, this.getBress())) {
	            cha.getInventory().count(this, getCount()-1, true); 
	            if (!cha.getFast_poly().equalsIgnoreCase("Dragon Slayer") || !cha.getFast_poly().equalsIgnoreCase("Platinum death knight")
			    		|| !cha.getFast_poly().equalsIgnoreCase("girtas") || !cha.getFast_poly().equalsIgnoreCase("Doppelganger")
			    		|| !cha.getFast_poly().equalsIgnoreCase("zillian of moon") || !cha.getFast_poly().equalsIgnoreCase("Flame death knight")
			    		|| !cha.getFast_poly().equalsIgnoreCase("Platinum Baphomet")) {

					BuffController.remove(cha, BuffRing.class);
					}
	         }
			}
		}