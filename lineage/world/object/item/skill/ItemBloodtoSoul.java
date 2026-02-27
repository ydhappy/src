package lineage.world.object.item.skill;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectGfx;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.magic.BodyToMind;
import lineage.world.object.magic.BreaveAvt;

public class ItemBloodtoSoul extends ItemInstance {

	private Skill skill;

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new ItemBloodtoSoul();
		item.setSkill(SkillDatabase.find(91));
		return item;
	}

	@Override
	public Skill getSkill() {
		return skill;
	}

	@Override
	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {

		if (cha.getClassType() == 0x02 && cha.getLevel() >= 30) {

			if (SkillController.isDelay(cha, skill)
					&& SkillController.isMagic(cha, skill, false)) {

				BodyToMind.init(cha, skill);
			}
		} else {
			ChattingController.toChatting(cha, "30레벨 이상 요정케릭이 사용 가능합니다.");
			return;
		}
	}
}