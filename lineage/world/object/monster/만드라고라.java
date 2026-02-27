package lineage.world.object.monster;

import lineage.bean.database.Monster;
import lineage.database.SpriteFrameDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectMode;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.Detection;


public class 만드라고라 extends MonsterInstance {

	static synchronized public MonsterInstance clone(MonsterInstance mi, Monster m) {
		if (mi == null)
			mi = new 만드라고라();
		return MonsterInstance.clone(mi, m);
	}

	@Override
	public void toTeleport(final int x, final int y, final int map, final boolean effect) {
		// 땅속에 박힌 모드로 변경.
		// standby
		setGfxMode(14);
		// 처리
		super.toTeleport(x, y, map, effect);
		return;
	}

	@Override
	protected void toAiWalk(long time) {
		if (getGfxMode() == getClassGfxMode()) {
			super.toAiWalk(time);
			return;
		}
		//
		ai_time = SpriteFrameDatabase.find(gfx, gfxMode + Lineage.GFX_MODE_WALK);
		//
		if (isPlayerInside())
			toStay(false);
	}

	@Override
	public void toAiAttack(long time) {
		if (getGfxMode() == getClassGfxMode()) {
			super.toAiAttack(time);
			return;
		}
		//
		ai_time = SpriteFrameDatabase.find(gfx, gfxMode + Lineage.GFX_MODE_WALK);
	}

	@Override
	public void toMagic(Character cha, Class<?> c) {
		if (getGfxMode() == getClassGfxMode() || !c.toString().equals(Detection.class.toString()))
			return;
		if(isDead() || isWorldDelete())
			return;

		toStay(false);
		addAttackList(cha);
	}

	@Override
	public boolean isAttack(Character cha, boolean magic) {
		if (getGfxMode() != getClassGfxMode())
			return false;
		return super.isAttack(cha, magic);
	}

	private void toStay(boolean recess) {
		if (!recess) {
			ai_time = SpriteFrameDatabase.find(gfx, gfxMode + Lineage.GFX_MODE_RISE);
			setGfxMode(getClassGfxMode());
			toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), this, 4), false);
			toSender(S_ObjectMode.clone(BasePacketPooling.getPool(S_ObjectMode.class), this), false);
		} else {
			ai_time = SpriteFrameDatabase.find(gfx, gfxMode + 11);
			setGfxMode(14);
			toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), this, 11), false);
			toSender(S_ObjectMode.clone(BasePacketPooling.getPool(S_ObjectMode.class), this), false);
		}
	}

	/**
	 * 주변에사용자가존재하는지 확인.
	 * 
	 * @return
	 */
	private boolean isPlayerInside() {
		for (object o : getInsideList(true)) {
			if (o instanceof PcInstance && Util.isDistance(this, o, 2)) {
				toDamage((PcInstance) o, 0, Lineage.ATTACK_TYPE_DIRECT);
				return true;
			}
		}
		return false;
	}

}
