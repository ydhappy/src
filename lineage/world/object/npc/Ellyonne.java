package lineage.world.object.npc;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.AdditionalFire;
import lineage.world.object.magic.AquaProtect;
import lineage.world.object.magic.BurningWeapon;
import lineage.world.object.magic.EarthBind;
import lineage.world.object.magic.EarthSkin;
import lineage.world.object.magic.ElementalFire;
import lineage.world.object.magic.ExoticVitalize;
import lineage.world.object.magic.EyeOfStorm;
import lineage.world.object.magic.FireWeapon;
import lineage.world.object.magic.IronSkin;
import lineage.world.object.magic.NaturesBlessing;
import lineage.world.object.magic.NaturesTouch;
import lineage.world.object.magic.PolluteWater;
import lineage.world.object.magic.Slow;
import lineage.world.object.magic.SoulOfFlame;
import lineage.world.object.magic.StormShot;
import lineage.world.object.magic.StrikerGale;
import lineage.world.object.magic.WaterLife;
import lineage.world.object.magic.WindShot;
import lineage.world.object.magic.WindWalk;

public class Ellyonne extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp){
		if(pc.getClassType() == 0x02)
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "ellyonne"));
		else
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "ellyonne2"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
		if(pc.getLevel()<30 || pc.getClassType()!=0x02){
			// 정령이 튕겨졌습니다.
			pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 451));
			return;
		}
		
		if(action.equalsIgnoreCase("fire")){
			if(pc.getAttribute() == 0){
				pc.setAttribute(Lineage.ELEMENT_FIRE);
				// 몸 구석구석으로 %s의 기운이 스며들어옵니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 679, "$1059"));
			}else{
				// 정령이 튕겨버렸습니다. 이미 다른 정령 속성이 부여되어 있습니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 449));
			}
		}else if(action.equalsIgnoreCase("water")){
			if(pc.getAttribute() == 0){
				pc.setAttribute(Lineage.ELEMENT_WATER);
				// 몸 구석구석으로 %s의 기운이 스며들어옵니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 679, "$1060"));
			}else{
				// 정령이 튕겨버렸습니다. 이미 다른 정령 속성이 부여되어 있습니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 449));
			}
		}else if(action.equalsIgnoreCase("air")){
			if(pc.getAttribute() == 0){
				pc.setAttribute(Lineage.ELEMENT_WIND);
				// 몸 구석구석으로 %s의 기운이 스며들어옵니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 679, "$1061"));
			}else{
				// 정령이 튕겨버렸습니다. 이미 다른 정령 속성이 부여되어 있습니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 449));
			}
		}else if(action.equalsIgnoreCase("earth")){
			if(pc.getAttribute() == 0){
				pc.setAttribute(Lineage.ELEMENT_EARTH);
				// 몸 구석구석으로 %s의 기운이 스며들어옵니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 679, "$1062"));
			}else{
				// 정령이 튕겨버렸습니다. 이미 다른 정령 속성이 부여되어 있습니다.
				pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 449));
			}
		}else{
			// 모든 정령마법 제거.
			// 19 : 3~7
			// 20 : 2~7
			// 21 : 2~7
			// 22 : 2~7
			for(int s=19 ; s<23 ; ++s){
				for(int n=(s==22?1:2) ; n<8 ; ++n){
					if(s==19 && n==2)
						continue;
					SkillController.remove(pc, s, n);
				}
			}
			SkillController.remove(pc, 22, 0);
//			for(int i=93 ; i<=112 ; ++i)
//				SkillController.remove(pc, i, false);
			pc.setAttribute(Lineage.ELEMENT_NONE);
			pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 678));
			// 버프중인 계열마법 제거.
			// fire
			BuffController.remove(pc, FireWeapon.class);
			BuffController.remove(pc, BurningWeapon.class);
			BuffController.remove(pc, ElementalFire.class);
			BuffController.remove(pc, SoulOfFlame.class);
			BuffController.remove(pc, AdditionalFire.class);
			// water
			BuffController.remove(pc, WaterLife.class);
			BuffController.remove(pc, NaturesTouch.class);
			BuffController.remove(pc, AquaProtect.class);
			BuffController.remove(pc, NaturesBlessing.class);
			BuffController.remove(pc, PolluteWater.class);
			// earth
			BuffController.remove(pc, EarthSkin.class);
			BuffController.remove(pc, EarthBind.class);
			BuffController.remove(pc, IronSkin.class);
			BuffController.remove(pc, ExoticVitalize.class);
			// wind
			BuffController.remove(pc, WindShot.class);
			BuffController.remove(pc, WindWalk.class);
			BuffController.remove(pc, EyeOfStorm.class);
			BuffController.remove(pc, StormShot.class);
			BuffController.remove(pc, StrikerGale.class);
		}
	}
}
