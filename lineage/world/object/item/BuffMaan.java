package lineage.world.object.item;

import lineage.bean.lineage.Buff;
import lineage.bean.lineage.BuffInterface;
import lineage.database.ItemDatabase;
import lineage.database.SkillDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.magic.MaanBirth;
import lineage.world.object.magic.MaanBirthDelay;
import lineage.world.object.magic.MaanEarth;
import lineage.world.object.magic.MaanEarthDelay;
import lineage.world.object.magic.MaanFire;
import lineage.world.object.magic.MaanFireDelay;
import lineage.world.object.magic.MaanLife;
import lineage.world.object.magic.MaanLifeDelay;
import lineage.world.object.magic.MaanShape;
import lineage.world.object.magic.MaanShapeDelay;
import lineage.world.object.magic.MaanWatar;
import lineage.world.object.magic.MaanWatarDelay;
import lineage.world.object.magic.MaanWind;
import lineage.world.object.magic.MaanWindDelay;

public class BuffMaan extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new BuffMaan();
		return item;
	}
	
	public void toClick(Character cha, ClientBasePacket cbp) {
		int jeryo_count = 1000;
		ItemInstance jeryo = cha.getInventory().find(ItemDatabase.find("결정체"));
		if (cha.getInventory() != null) {		
			if (getItem().getName().contains("수룡")) {
				if (checkBuff(cha)) {
					MaanWatar.init(cha, SkillDatabase.find(711));
					// 결정체 제거.
					cha.getInventory().count(jeryo, jeryo.getCount() - jeryo_count, true);
					MaanWatarDelay.init(cha, SkillDatabase.find(718));
				}
			} else if (getItem().getName().contains("풍룡")) {
				if (checkBuff(cha)) {
					MaanWind.init(cha, SkillDatabase.find(712));
					// 결정체 제거.
					cha.getInventory().count(jeryo, jeryo.getCount() - jeryo_count, true);
					MaanWindDelay.init(cha, SkillDatabase.find(719));
				}
			} else if (getItem().getName().contains("지룡")) {
				if (checkBuff(cha)) {
					MaanEarth.init(cha, SkillDatabase.find(713));
					// 결정체 제거.
					cha.getInventory().count(jeryo, jeryo.getCount() - jeryo_count, true);
					MaanEarthDelay.init(cha, SkillDatabase.find(720));
				}
			} else if (getItem().getName().contains("화룡")) {
				if (checkBuff(cha)) {
				MaanFire.init(cha, SkillDatabase.find(714));
				// 결정체 제거.
				cha.getInventory().count(jeryo, jeryo.getCount() - jeryo_count, true);
				MaanFireDelay.init(cha, SkillDatabase.find(721));
				}
			} else if (getItem().getName().contains("탄생")) {
			if (checkBuff(cha)) {
					MaanBirth.init(cha, SkillDatabase.find(715));
					// 결정체 제거.
					cha.getInventory().count(jeryo, jeryo.getCount() - jeryo_count, true);
					MaanBirthDelay.init(cha, SkillDatabase.find(722));
				}
			} else if (getItem().getName().contains("형상")) {
				if (checkBuff(cha)) {
					MaanShape.init(cha, SkillDatabase.find(716));
					// 결정체 제거.
					cha.getInventory().count(jeryo, jeryo.getCount() - jeryo_count, true);
					MaanShapeDelay.init(cha, SkillDatabase.find(723));
				}
			} else if (getItem().getName().contains("생명")) {
				if (checkBuff(cha)) {
					MaanLife.init(cha, SkillDatabase.find(717));
					// 결정체 제거.
					cha.getInventory().count(jeryo, jeryo.getCount() - jeryo_count, true);
					MaanLifeDelay.init(cha, SkillDatabase.find(724));
				}
			}
		}
	}
	
	public boolean checkDelay(Character cha, int uid) {
		BuffInterface b = BuffController.find(cha, SkillDatabase.find(uid));
		if (b != null && b.getTime() > 0) {
			if (b.getTime() / 3600 > 0) {
				ChattingController.toChatting(cha, String.format("%s: %d시간 %d분 %d초 후 사용 가능합니다.", getItem().getName(), b.getTime() / 3600, b.getTime() % 3600 / 60, b.getTime() % 3600 % 60), Lineage.CHATTING_MODE_MESSAGE);
			} else if (b.getTime() % 3600 / 60 > 0) {
				ChattingController.toChatting(cha, String.format("%s: %d분 %d초 후 사용 가능합니다.", getItem().getName(), b.getTime() % 3600 / 60, b.getTime() % 3600 % 60), Lineage.CHATTING_MODE_MESSAGE);
			} else {
				ChattingController.toChatting(cha, String.format("%s: %d초 후 사용 가능합니다.", getItem().getName(), b.getTime() % 3600 % 60), Lineage.CHATTING_MODE_MESSAGE);
			}
			return false;
		}
		return true;
	}
	
	public boolean checkBuff(Character cha) {
		// 결정체 확인.
				int jeryo_count = 1000;
				ItemInstance jeryo = cha.getInventory().find(ItemDatabase.find("결정체"));
				if(jeryo==null || jeryo.getCount()<jeryo_count) {
					String msg = "";
					if (jeryo == null)
						msg = String.format("%s(%d)가 부족합니다.", "결정체", jeryo_count);
					else
						msg = String.format("%s(%d)가 부족합니다.", "결정체", jeryo_count - jeryo.getCount());
					ChattingController.toChatting(cha, msg, Lineage.CHATTING_MODE_MESSAGE);
					return false;
				}
				
		Buff buff = BuffController.find(cha);
		
		if (buff != null) {
			for (BuffInterface b : buff.getList()) {
				if (b != null && b.getTime() > 0) {
					if (b.getSkill().getUid() >= 718 && b.getSkill().getUid() <= 724) {
						if (b.getTime() / 3600 > 0) {
							ChattingController.toChatting(cha, String.format("%s: %d시간 %d분 %d초가 남았습니다.", b.getSkill().getName(), b.getTime() / 3600, b.getTime() % 3600 / 60, b.getTime() % 3600 % 60), Lineage.CHATTING_MODE_MESSAGE);
						} else if (b.getTime() % 3600 / 60 > 0) {
							ChattingController.toChatting(cha, String.format("%s: %d분 %d초가 남았습니다.", b.getSkill().getName(), b.getTime() % 3600 / 60, b.getTime() % 3600 % 60), Lineage.CHATTING_MODE_MESSAGE);
						} else {
							ChattingController.toChatting(cha, String.format("%s: %d초가 남았습니다.", b.getSkill().getName(), b.getTime() % 3600 % 60), Lineage.CHATTING_MODE_MESSAGE);
						}
						return false;
					}
				}
			}
		}	
		return true;
	}
}
