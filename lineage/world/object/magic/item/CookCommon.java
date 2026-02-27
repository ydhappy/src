package lineage.world.object.magic.item;

import lineage.bean.database.Item;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterSpMr;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_Cook;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.magic.Magic;

public class CookCommon extends Magic {
	
	static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, Item item) {
		if(bi == null)
			bi = new CookCommon(skill);
		bi.setSkill(skill);
		bi.setTime(skill.getBuffDuration());
		((CookCommon)bi).item = item;
		return bi;
	}

	public CookCommon(Skill skill) {
		super(null, skill);
	}
	
	protected Item item;

	@Override
	public void toBuffStart(object o) {
		//
		toBuffUpdate(o);
	}
	
	@Override
	public void toBuffUpdate(object o) {
		//
		toOption(o, true);
	}

	@Override
	public boolean toBuffStop(object o) {
		toBuffEnd(o);
		return true;
	}

	@Override
	public void toBuffEnd(object o) {
		//
		toOption(o, false);
	}
	
	/**
	 * 버프 적용 및 해제 처리.
	 * @param active
	 */
	private void toOption(object o, boolean active) {
		if(o instanceof Character) {
			Character cha = (Character)o;
			switch(item.getNameIdNumber()) {
				case 49284932:	// 환상의 괴물눈 스테이크
					cha.setDynamicReduction(cha.getDynamicReduction() + (active ? 5 : -5));
				case 4932:		// 괴물눈 스테이크
					cha.setDynamicEarthress(cha.getDynamicEarthress() + (active ? 10 : -10));
					cha.setDynamicWaterress(cha.getDynamicWaterress() + (active ? 10 : -10));
					cha.setDynamicFireress(cha.getDynamicFireress() + (active ? 10 : -10));
					cha.setDynamicWindress(cha.getDynamicWindress() + (active ? 10 : -10));
					break;
				case 49285218:	// 환상의 곰고기 구이
					cha.setDynamicReduction(cha.getDynamicReduction() + (active ? 5 : -5));
				case 5218:		// 곰고기 구이
					cha.setDynamicHp(cha.getDynamicHp() + (active ? 30 : -30));
					break;
				case 49284929:	// 환상의 씨호떡
					cha.setDynamicReduction(cha.getDynamicReduction() + (active ? 5 : -5));
				case 4929:		// 씨호떡
					cha.setDynamicTicMp(cha.getDynamicTicMp() + (active ? 3 : -3));
					break;
				case 49285219:	// 환상의 개미다리 치즈구이
					cha.setDynamicReduction(cha.getDynamicReduction() + (active ? 5 : -5));
				case 5219:		// 개미다리 치즈구이
					cha.setDynamicAc(cha.getDynamicAc() + (active ? 1 : -1));
					break;
				case 49284930:	// 환상의 과일 샐러드
					cha.setDynamicReduction(cha.getDynamicReduction() + (active ? 5 : -5));
				case 4930:		// 과일 샐러드
					cha.setDynamicMp(cha.getDynamicMp() + (active ? 20 : -20));
					break;
				case 49284931:	// 환상의 과일 탕수육
					cha.setDynamicReduction(cha.getDynamicReduction() + (active ? 5 : -5));
				case 4931:		// 과일 탕수육
					cha.setDynamicTicHp(cha.getDynamicTicHp() + (active ? 3 : -3));
					break;
				case 49285220:	// 환상의 멧돼지 꼬치 구이
					cha.setDynamicReduction(cha.getDynamicReduction() + (active ? 5 : -5));
				case 5220:		// 멧돼지 꼬치 구이
					cha.setDynamicMr(cha.getDynamicMr() + (active ? 5 : -5));
					break;
				case 49285221:	// 환상의 버섯 스프
					cha.setDynamicReduction(cha.getDynamicReduction() + (active ? 5 : -5));
				case 5221:		// 버섯 스프
					cha.setDynamicExp(cha.getDynamicExp() + (active ? 0.1D : -0.1D));
					break;
			}
			//
			cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
			cha.toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), cha));
			//
			cha.toSender(S_Cook.clone(BasePacketPooling.getPool(S_Cook.class), cha, item, getTime()));
		}
	}
	
}
