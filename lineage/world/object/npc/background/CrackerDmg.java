package lineage.world.object.npc.background;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectHeading;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.instance.BackgroundInstance;
import lineage.world.object.instance.PcInstance;

public class CrackerDmg extends BackgroundInstance {
	
	@Override
	public void toDamage(Character cha, int dmg, int type, Object...opt){
		if(cha instanceof PcInstance){
			PcInstance pc = (PcInstance)cha;
			if(pc.getLevel()<5)
				pc.toExp(this, Util.random(2, 4));
		}
		setHeading(++heading);
	}

	@Override
	public void setHeading(int heading){
		if(Lineage.server_version <= 163){
			if(heading > 2)
				heading = 0;
		}
		super.setHeading(heading);
		
		toSender(S_ObjectHeading.clone(BasePacketPooling.getPool(S_ObjectHeading.class), this), false);
	}
	
}
