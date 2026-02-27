package lineage.world.object.item.skill;

import lineage.bean.database.Skill;
import lineage.database.SkillDatabase;
import lineage.network.packet.ClientBasePacket;
import lineage.util.Util;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.magic.ReturnToNature;

public class ItemReturntona extends ItemInstance {
private Skill skill;
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ItemReturntona();
		item.setSkill( SkillDatabase.find(110));
		return item;
	}

	@Override
	public Skill getSkill(){
		return skill;	
	}
	@Override
	public void setSkill(Skill skill){
		this.skill = skill;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp){

		if(isClassCheck(cha)){
		//	cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
			long objid = cbp.readD();
			object o = null;
			if(objid==cha.getObjectId())
				o = cha;
			else
				o = cha.findInsideList(objid);
			
		if(o!=null &&SkillController.isDelay(cha, skill) && Util.isDistance(cha, o, 8) && SkillController.isMagic(cha, skill, true)) {
				ReturnToNature.init(cha , skill, objid);
			}
		}
	}
}