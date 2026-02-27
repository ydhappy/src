package lineage.world.object.monster;

import lineage.bean.database.Monster;
import lineage.database.SpriteFrameDatabase;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectMode;
import lineage.world.object.Character;
import lineage.world.object.instance.MonsterInstance;

public class IceMonster extends MonsterInstance
{
	public static synchronized MonsterInstance clone(MonsterInstance mi, Monster m)
	{
		if (mi == null)
			mi = new IceMonster();
		return MonsterInstance.clone(mi, m);
	}

	public void toTeleport(int x, int y, int map, boolean effect){
		super.toTeleport(x, y, map, effect);
		toStay(true);
		return;
	}

	protected void toAiWalk(long time)
	{
		if (getGfxMode() == getClassGfxMode()) {
			super.toAiWalk(time);
			return;
		}


		this.ai_time = SpriteFrameDatabase.find(this.gfx, this.gfxMode + 0);
	}

	public void toDamage(Character cha, int dmg, int type, Object... opt)
	{
		super.toDamage(cha, dmg, type, opt);

		if (getGfxMode() != getClassGfxMode())
			toStay(false);
	}

	private void toStay(boolean recess) {
		if (!recess) {
			this.ai_time = SpriteFrameDatabase.find(this.gfx, this.gfxMode + 11);
			setGfxMode(getClassGfxMode());
			toSender(new S_ObjectAction( this, 11), false);
			toSender(new S_ObjectMode( this), false);
		} else {
			this.ai_time = SpriteFrameDatabase.find(this.gfx, this.gfxMode + 4);
			setGfxMode(4);
			toSender(new S_ObjectAction( this), false);
			toSender(new S_ObjectMode( this), false);
		}
	}
}


