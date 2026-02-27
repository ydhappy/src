package lineage.world.object.item.wand;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Item;
import lineage.bean.database.Poly;
import lineage.database.ItemDatabase;
import lineage.database.PolyDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.RankController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.AbsoluteBarrier;
import lineage.world.object.magic.BuffRing;
import lineage.world.object.magic.ShapeChange;

public class MapleWand extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new MapleWand();
		return item;
	}

	@Override
	public ItemInstance clone(Item item){
		quantity = Util.random(5, 15);
		
		return super.clone(item);
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		int allRank = RankController.getAllRank(cha.getObjectId());
		int classRank = RankController.getRankClass(cha);
		
		// 앱솔상태 해제.
		if (cha.isBuffAbsoluteBarrier())
			BuffController.remove(cha, AbsoluteBarrier.class);
		
		if (cha.isFishing()) {
			ChattingController.toChatting(cha, "낚시중에는 변신할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		// 투망 상태에서는 사용할 수 없음.
		if(cha.isInvis()) {
			ChattingController.toChatting(cha, "아무일도 일어나지 않았습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 17), true);

		if(quantity<=0){
			// \f1아무일도 일어나지 않았습니다.
			cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
			//cha.getInventory().count(this, getCount()-1, true);
			return;
		}
		
		setQuantity(quantity-1);
		cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));

		long objid = cbp.readD();
		object o = null;
		if(objid==cha.getObjectId())
			o = cha;
		else
			o = cha.findInsideList(objid);
		
		// 사용자일경우 세이프존이 아닐때만 처리되게 하기.
		if(objid!=cha.getObjectId() && o instanceof PcInstance )//&& World.isSafetyZone(o.getX(), o.getY(), o.getMap()))
			return;
	//	if(cha.getClanId()<=0 || o.getClanId()!=cha.getClanId())
	//		return;
		
		// 확률 체크.
		boolean is = cha.getObjectId()==o.getObjectId();
		// 같은 혈맹인지 체크.
		if(cha.getClanId()>0 && cha.getClanId()!=o.getClanId())
			is = false;
		// 같은 파티인지 체크.
		if(cha.getPartyId()>0 && cha.getPartyId()!=o.getPartyId())
			is = false;
		if(cha.getObjectId() == objid){
			if(cha.getInventory().isRingOfPolymorphControl()){
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
			}else{
			ShapeChange.onBuff(cha, o, null, 7200, true, true);
            if (!cha.getFast_poly().equalsIgnoreCase("Dragon Slayer") || !cha.getFast_poly().equalsIgnoreCase("Platinum death knight")
		    		|| !cha.getFast_poly().equalsIgnoreCase("girtas") || !cha.getFast_poly().equalsIgnoreCase("Doppelganger")
		    		|| !cha.getFast_poly().equalsIgnoreCase("zillian of moon") || !cha.getFast_poly().equalsIgnoreCase("Flame death knight")
		    		|| !cha.getFast_poly().equalsIgnoreCase("Platinum Baphomet")) {

				BuffController.remove(cha, BuffRing.class);
				}
			}
		}
		else if (is || Util.random(1, 100) < getProbability(o)){
			if(o.getInventory().isRingOfPolymorphControl()){
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
			}else{
			ShapeChange.onBuff(cha, o, null, 7200, true, true);
            if (!cha.getFast_poly().equalsIgnoreCase("Dragon Slayer") || !cha.getFast_poly().equalsIgnoreCase("Platinum death knight")
		    		|| !cha.getFast_poly().equalsIgnoreCase("girtas") || !cha.getFast_poly().equalsIgnoreCase("Doppelganger")
		    		|| !cha.getFast_poly().equalsIgnoreCase("zillian of moon") || !cha.getFast_poly().equalsIgnoreCase("Flame death knight")
		    		|| !cha.getFast_poly().equalsIgnoreCase("Platinum Baphomet")) {

				BuffController.remove(cha, BuffRing.class);
				}
			}
		}
		}
	@Override
	public void toClick(Character cha, String action, String type, ClientBasePacket cbp){
		
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
		
			ShapeChange.init(cha, cha, PolyDatabase.getPolyName(action), 7200, this.getBress());
			cha.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
            if (!cha.getFast_poly().equalsIgnoreCase("Dragon Slayer") || !cha.getFast_poly().equalsIgnoreCase("Platinum death knight")
		    		|| !cha.getFast_poly().equalsIgnoreCase("girtas") || !cha.getFast_poly().equalsIgnoreCase("Doppelganger")
		    		|| !cha.getFast_poly().equalsIgnoreCase("zillian of moon") || !cha.getFast_poly().equalsIgnoreCase("Flame death knight")
		    		|| !cha.getFast_poly().equalsIgnoreCase("Platinum Baphomet")) {

				BuffController.remove(cha, BuffRing.class);
				}
	}
	private int getProbability(object target) {
		int prob = 30;
		int mr = 0;
		if(target!=null && target instanceof Character) {
			mr = SkillController.getMr((Character)target, false);
			if(mr>=50) {
				prob -= (mr-50);
			}
		}
	//	System.out.println("텟pro :"+prob+"m r: "+mr);
		return prob;
	}

}
