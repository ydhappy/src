package lineage.world.object.magic;

import lineage.bean.database.MonsterSkill;
import lineage.bean.database.Skill;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_InventoryCount;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MonsterInstance;

public class WeaponBreak {

	static public void init(Character cha, Skill skill, long object_id){
		object o = cha.findInsideList(object_id);
		if(o != null){
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
			if(SkillController.isMagic(cha, skill, true)) {
				if(o.isBuffSoulOfFlame()==false && SkillController.isFigure(cha, o, skill, true, false))
					onBuff(cha, o, skill.getCastGfx());
				// 투망상태 해제
				Detection.onBuff(cha);
			}
		}
	}
	
	/**
	 * 몬스터가 사용중
	 */
	static public void init(MonsterInstance mi, object o, MonsterSkill ms, int action, int effect){
		if(action != -1)
			mi.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), mi, action), false);
		if(SkillController.isMagic(mi, ms, true) && SkillController.isFigure(mi, o, ms, true, false))
			onBuff(mi, o, effect);
	}
	
	static public void onBuff(Character cha, object o, int effect) {
		// 처리
		if(o.getInventory() != null){
			ItemInstance item = o.getInventory().getSlot(8);
			if(item!=null && item.getItem().isCanbedmg()){
				item.setDurability(item.getDurability() + Util.random(3, 5));
				if(effect > 0)
					o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, effect), true);
				if(Lineage.server_version<=144){
					o.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), item));
					o.toSender(S_InventoryCount.clone(BasePacketPooling.getPool(S_InventoryCount.class), item));
				}else{
					o.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), item));
				}
				// \f1당신의 %0%s 손상되었습니다.
				o.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 268, item.toString()));
				// 공격당한거 알리기.
				o.toDamage(cha, 0, 2);
				return;
			}
		}
		// \f1마법이 실패했습니다.
		cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 280));
	}
}
