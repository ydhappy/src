package lineage.world.object.magic;

import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.world.object.Character;
import lineage.world.object.object;

public class Magic implements BuffInterface {

	protected Character cha;	// 시전자
	protected Skill skill;		// 마법종류
	protected long time_end;	// 버프 종료될 시간값.
	
	public Magic(Character cha, Skill skill){
		this.cha = cha;
		this.skill = skill;
	}

	@Override
	public Skill getSkill() {
		return skill;
	}
	
	@Override
	public void setSkill(Skill skill){
		this.skill = skill;
	}

	@Override
	public void setTime(int time) {
		if (time > 0)
			time_end = System.currentTimeMillis() + (time * 1000);
		else
			time_end = time;
	}

	@Override
	public int getTime() {
		if(time_end > 0) {
			long time = time_end - System.currentTimeMillis();
			return (int) (time / 1000);
		} else {
			return (int)time_end;
		}
	}

	@Override
	public void setCharacter(Character cha) {
		this.cha = cha;
	}

	@Override
	public Character getCharacter() {
		return cha;
	}

	@Override
	public boolean isBuff(object o, long time) {
		if(time_end >= 0)
			return time_end >= time;
		return true;
	}

	@Override
	public void close() {
		cha = null;
		skill = null;
		time_end = 0;
	}

	@Override
	public void toBuffStart(object o) {
		// TODO Auto-generated method stub
	}

	@Override
	public void toBuffUpdate(object o) {
		// TODO Auto-generated method stub
	}

	@Override
	public void toBuff(object o) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean toBuffStop(object o) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean equal(BuffInterface bi) {
		// 헤이스트와 그레이트 헤이스트는 같은 스킬로 간주.
		if (this instanceof Haste && bi instanceof Haste)
			return true;
		// 스킬 uid가 같다면 같은 스킬로 간주.
		return getSkill().getUid() == bi.getSkill().getUid();
	}

	@Override
	public void setTime(int time, boolean restart) {
		// TODO Auto-generated method stub

	}

	@Override
	public int toDamagePlus(Character cha, object target) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void toWorldOut(object o){
		// TODO Auto-generated method stub
	}

	@Override
	public int toDamageReduction(object attacker, object target) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int toDamageAddRate(object attacker, object target) {
		// TODO Auto-generated method stub
		return 0;
	}
}
