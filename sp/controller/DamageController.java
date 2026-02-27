package sp.controller;

import lineage.database.MagicdollListDatabase;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.BurningSlash;
import lineage.world.object.magic.CursePoison;

public class DamageController {

	/**
	 * 카운터배리어 처리 메서드.
	 * @param cha
	 * @param target
	 * @param bow
	 * @param weapon
	 * @param arrow
	 * @param dmg
	 * @return
	 */
	static public Object toCounterBarrier(Character cha, object target, boolean bow, ItemInstance weapon, ItemInstance arrow, double dmg) {
		//			if(bow==false && target.isBuffCounterBarrier() && Util.random(0, cha.getLevel())<Util.random(0, target.getLevel())) {
		if(bow==false && (weapon!=null || cha instanceof MonsterInstance) && target.isBuffCounterBarrier() && Util.random(0, 99)<10) {
			if(dmg > 0) {
				if(cha instanceof MonsterInstance) {
					lineage.world.controller.DamageController.toDamage((Character)target, cha, (int)(dmg*1.5), 0);
				} else {
					if(target instanceof MonsterInstance)
						// 데미지에 25%만 적용하기.
						lineage.world.controller.DamageController.toDamage((Character)target, cha, (int)((weapon.getItem().getDmgMax()+weapon.getEnLevel()+weapon.getItem().getAddDmg())*2.0), 0);
					else
						// (큰몹+인첸+추타) * 2
						lineage.world.controller.DamageController.toDamage((Character)target, cha, (int)((weapon.getItem().getDmgMax()+weapon.getEnLevel()+weapon.getItem().getAddDmg())*3), 0);
				}
				//						// 데미지에 50%만 적용하기.
				//						toDamage((Character)target, cha, (int)(dmg*0.5), 0);
				// 카운터베리어 반사시 이팩트를 표현함. 임시소스 추후 일팩 참고해서 가져와야함.
				//					int effectId = SkillDatabase.find(12, 2).getCastGfx();
				target.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), target, 10710), true);
				return true;
			}
		}
		
		return null;
	}

	static public double DmgPlus(Character cha, ItemInstance item, object target, boolean bow){
		double dmg = 0;
		// 대상이 유저일경우 pvp동적 데미지 추가.
		if(target instanceof PcInstance) {
			dmg += cha.getDynamicAddPvpDmg();
		}
		//
		if(item != null){
			ItemWeaponInstance weapon = (ItemWeaponInstance)item;
			// 인첸트 웨폰
			if(weapon.isBuffEnchantWeapon())
				dmg += 1;
			// 블레스웨폰 상태라면 랜타 데미지 2 추가
			if(weapon.isBuffBlessWeapon())
				dmg += Util.random(0, 2);
			// 쉐도우팽
			if(weapon.isBuffShadowFang())
				dmg += 5;
		}
		// 홀리써클 상태라면 랜타 데미지 2 추가
		//		if(buff->findMagic(52))
		//			dmg += Common::random(0, 2);
		// 글로잉오라
		if(cha.isBuffGlowingAura())
			dmg += 1;
		// 인첸트베놈
		if(cha.isBuffEnchantVenom() && Util.random(0, 99)<20)
			BuffController.append(target, CursePoison.clone(BuffController.getPool(CursePoison.class), cha, SkillDatabase.find(2, 2), 32));
		// 마법인형 : 라미아
		if(cha.isMagicdollRamia() && Util.random(0, 99)<10)
			BuffController.append(target, CursePoison.clone(BuffController.getPool(CursePoison.class), cha, SkillDatabase.find(2, 2), 10));

		// 장거리 공격일때.
		if(bow){
			dmg += cha.getTotalAddDmgBow();
			// 아이오브스톰
			if(cha.isBuffEyeOfStorm())
				dmg += 2;
			// 스톰샷
			if(cha.isBuffStormShot())
				dmg += 5;
			// 스트라이커게일
		//	if(target.isBuffStrikerGale())
		//		dmg += Util.random(5, 10);
			// 근접 공격일때.
		}else{
			dmg += cha.getTotalAddDmg();
			// 버서커스
			if(cha.isBuffBerserks())
				dmg += 5;
			// 마법인형 : 늑대인간 (근거리 대미지 +15)
			// 마법인형 : 크러스트시안 (근거리 대미지 +15)
			if((cha.isMagicdollWerewolf() || cha.isMagicdollCrustacean()) && Util.random(0, 99) <= 3){
				dmg += 15;
				int effect = MagicdollListDatabase.getBuffEffect(cha.isMagicdollWerewolf() ? "늑대인간" : "크러스트시안");
				if(effect > 0)
					cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, effect), true);
			}
		/*	if(item != null) {
				// 소울 오브 프레임 상태라면 근거리 무기의 최대 대미지를 적용.
				if(cha.isBuffSoulOfFlame())
					dmg += item.getItem().getDmgMax();
			}*/
			// 파이어웨폰 근거리 무기 +4 적용.
			if(cha.isBuffFireWeapon())
				dmg += 4;
			// 버닝웨폰 근거리 무기 +6 적용.
			if(cha.isBuffBurningWeapon())
				dmg += 6;
			// 버닝 슬래쉬
			dmg += SkillController.toDamagePlus(BurningSlash.class, cha, target);
		}
		// 홀리웨폰 상태라면 랜타 데미지 2 추가
		if(cha.isBuffHolyWeapon()){
			if(target instanceof MonsterInstance){
				MonsterInstance mon = (MonsterInstance)target;
				if(mon.getMonster().getResistanceUndead()<0)
					dmg += 2;
			}
		}

		// 클레스별 데미지 추가 성환이
		/*switch(cha.getClassType()) {
			case 0x00:
				dmg += 8;
				break;
			case 0x02:
				if(bow)
					dmg += 0;
				else
					dmg += 5;
				break;
			case 0x01:
				dmg += 3;
				break;
			case 0x03:
				dmg += 17;
				break;
			case 0x04:
				dmg += 15;
				break;
		}*/
		
		// 고정멤버 추타적용.
		if(cha instanceof PcInstance) {
			PcInstance use = (PcInstance)cha;
			if(고정신청관리.isGojung(use.getClient().getAccountId()))
				dmg += 1;
		}
		return dmg;
	}

	static public double DmgWeaponFigure(boolean bow, ItemInstance weapon, ItemInstance arrow, boolean Small){
		double dmg = 0;
		dmg += weapon.getEnLevel();
		dmg += weapon.getItem().getAddDmg();
		// 축복무기 특화
		if(weapon.getBress()==0 && Util.random(0, 100)<=30)
			dmg += Util.random(0, 2);
		// 상급무기 특화.
		if(weapon.getEnLevel()>8)
			dmg += Util.random(0, dmg*(weapon.getEnLevel()-7)) * 0.05;
		//			dmg += Util.random(0, dmg*(weapon.getEnLevel()-7));
		// 하이드형 추가데미지 부분.
		//	: dmg ~ (dmg+en) = 50%만 반영
		//	: 설정데미지 ~ (설정데미지+무기인첸트) = 랜덤 추출한값에 50%만 데미지를 더한다.
		if(weapon.getItem().getHideAddDmg() > 0)
			dmg += Util.random(weapon.getItem().getHideAddDmg(), weapon.getItem().getHideAddDmg()+weapon.getEnLevel()) * 0.1;
		
		//인첸트 대미지 추가
		if(weapon.getItem().getSafeEnchant()==0 && weapon.getItem().getEnchantDmg() >0){
			dmg += Util.random(weapon.getItem().getEnchantDmg(), weapon.getItem().getEnchantDmg()+weapon.getEnLevel()) * 0.1;
		}else if(weapon.getItem().getSafeEnchant()==6 && weapon.getItem().getEnchantDmg()>0 && weapon.getEnLevel()>6){
			dmg += Util.random(weapon.getItem().getEnchantDmg(), weapon.getItem().getEnchantDmg()+weapon.getEnLevel()) * 0.1;
		}
		
		// 인첸에 따른 뎀지 증가
		if(weapon.getEnLevel() > 7)
			dmg += 2;
		//성환. 10검인첸 데미지 추가
		if(weapon.getEnLevel() == 10)
			dmg += 2;

		// 기본 데미지 연산값 저장 변수
		int d = 0;
		// 큰몹 및 작은몹 구분하여 무기의 기본 데미지 추출
		if(Small) d += weapon.getItem().getDmgMin();
		else d += weapon.getItem().getDmgMax();

		// 활일경우 화살에 데미지 추출
		if( bow ) {
			// 화살이 있을경우.
			if(arrow != null){
				if(Small)
					d += arrow.getItem().getDmgMin();
				else
					d += arrow.getItem().getDmgMax();
			}
			// 사이하활일 경우 마법 데미지 추가
			if(weapon.getItem().getNameIdNumber()==1821)
				d += Util.random(0, 6);
			
		}

		// 추출된 무기데미지를 랜덤값으로 추출 [기본]
		if(d > 0)
			d = Util.random(d == 0  ? 2 : d-5 , d);

		return dmg + d;
	}

}
