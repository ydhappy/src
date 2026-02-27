package lineage.world.object.monster;

import lineage.bean.database.Monster;
import lineage.database.SpriteFrameDatabase;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectMode;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.instance.MonsterInstance;

public class Lindvior extends MonsterInstance {

	static synchronized public MonsterInstance clone(MonsterInstance mi, Monster m){
		if(mi == null)
			mi = new Lindvior();
		return MonsterInstance.clone(mi, m);
	}
	
	@Override
	public void toAiAttack(long time) {
		// // hp가 30%미만이면 도망모드로 변환.
		// if(getNowHp()<=getTotalHp()*0.3 && Util.random(0, 100)>80) {
		// //
		// setGfxMode(44);
		// toSender(new S_ObjectAction(
		// this), false);
		// setGfxMode(11);
		// toSender(new S_ObjectMode(
		// this), false);
		// //
		// setAiStatus( Lineage.AI_STATUS_ESCAPE );
		// return;
		// }
		super.toAiAttack(time);
	}
	
	@Override
	public void toAiEscape(long time) {
		//
		ai_time = SpriteFrameDatabase.find(gfx, gfxMode + Lineage.GFX_MODE_WALK);
		boolean isSafeHp = getNowHp() > getTotalHp() * 0.35;
		// hp가 안전해지거나 일정확률적으로 돌아오기.
		if (isSafeHp || Util.random(0, 100) <= 5) {
			//
			toTeleport(getX(), getY(), getMap(), false);
			//
			setGfxMode(45);
			toSender(new S_ObjectAction( this), false);
			setGfxMode(getClassGfxMode());
			toSender(new S_ObjectMode( this), false);
			//
			setAiStatus(Lineage.AI_STATUS_ATTACK);
			return;
		}
	}
	
	@Override
	public boolean isAttack(Character cha, boolean magic) {
		if (getGfxMode() != getClassGfxMode())
			return false;
		return super.isAttack(cha, magic);
	}

}
