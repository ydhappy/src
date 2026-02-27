package lineage.world.object.npc.buff;

import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_SoundEffect;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class 강화마법사 extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "bs_01"));
		pc.toSender(S_SoundEffect.clone(BasePacketPooling.getPool(S_SoundEffect.class), 27796)); 
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		if (action.equalsIgnoreCase("a")) {
			if (pc.getInventory().isAden(10000, true)) {
				//파이어웨폰
				lineage.world.object.magic.FireWeapon.onBuff(pc, SkillDatabase.find(19, 3));
				//어벤
				//lineage.world.object.magic.AdvanceSpirit.onBuff(pc, SkillDatabase.find(10, 7));
				//글로잉 웨폰
				//lineage.world.object.magic.GlowingAura.onBuff(pc, SkillDatabase.find(15, 1));
				//샤이닝 실드
				//lineage.world.object.magic.ShiningAura.onBuff(pc, SkillDatabase.find(15, 2));
				//어스 스킨
				lineage.world.object.magic.EarthSkin.onBuff(pc, SkillDatabase.find(19, 6));
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
			} else {
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
				
			}
		}else if(action.equalsIgnoreCase("b")){
			if (pc.getInventory().isAden(10000, true)) {
				//윈드샷
				lineage.world.object.magic.WindShot.onBuff(pc, SkillDatabase.find(19, 4));
				//어벤
				//lineage.world.object.magic.AdvanceSpirit.onBuff(pc, SkillDatabase.find(10, 7));
				//글로잉 웨폰
				//lineage.world.object.magic.GlowingAura.onBuff(pc, SkillDatabase.find(15, 1));
				//샤이닝 실드
				//lineage.world.object.magic.ShiningAura.onBuff(pc, SkillDatabase.find(15, 2));
				//어스 스킨
				lineage.world.object.magic.EarthSkin.onBuff(pc, SkillDatabase.find(19, 6));
				pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, ""));
			} else {
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 189));
			}
		}
	}
}