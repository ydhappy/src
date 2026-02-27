package lineage.world.object.magic;

import lineage.bean.database.MonsterSkill;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.database.ItemDatabase;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectPolyIcon;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.magic.item.Blue;
import lineage.world.object.magic.item.Bravery;
import lineage.world.object.magic.item.Criminal;
import lineage.world.object.magic.item.Eva;
import lineage.world.object.magic.item.Wafer;
import lineage.world.object.magic.item.Wisdom;
import lineage.world.object.magic.monster.CurseGhoul;

public class Cancellation {

	static public void init(Character cha, Skill skill, long object_id){
		// 초기화
		object o = null;
		// 타겟 찾기
		if(object_id == cha.getObjectId())
			o = cha;
		else
			o = cha.findInsideList( object_id );
		// 처리
		if(o != null){
			cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, 19), true);
			if(SkillController.isMagic(cha, skill, true)) {
				// 투망상태 해제
				Detection.onBuff(cha);
				
				if(cha.getObjectId()==o.getObjectId() || SkillController.isFigure(cha, o, skill, true, SkillController.isClan(cha, o))  || Lineage.server_version<=144) {
					onBuff(o, skill.getCastGfx());
					// 공격당한거 알리기.
					o.toDamage(cha, 0, 2, Cancellation.class);
					//
					if(o.isBuffCriminal() && !cha.isBuffCriminal())
						Criminal.onBuff(cha, SkillDatabase.find(206));
				} else {
					// \f1마법이 실패했습니다.
					cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 280));
				}
			}
		}
	}
	
	/**
	 * 몬스터가 사용중
	 */
	static public void init(MonsterInstance mi, object o, MonsterSkill ms, int action, int effect){

		mi.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), mi, action), false);
		
		if(SkillController.isMagic(mi, ms, true) && SkillController.isFigure(mi, o, ms, false, false))
			onBuff(o, 870);
	}
	
	static public void onBuff(object o, int effect){
		boolean hasRing = false;
		if(o instanceof PcInstance) {
			hasRing = o.getInventory().has지배변반();
		}
		if(effect > 0)
			o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, effect), true);
		
		// 라이트
		BuffController.remove(o, Light.class);
		// 쉴드
		BuffController.remove(o, Shield.class);
		// 홀리 웨폰
		BuffController.remove(o, HolyWeapon.class);
		// 커스:포이즌
		BuffController.remove(o, CursePoison.class);
		// 디크리즈 웨이트
		BuffController.remove(o, DecreaseWeight.class);
		// 커스: 블라인드
		// 다크니스
		// 다크블라인드
		BuffController.remove(o, CurseBlind.class);
		// 인첸트 덱스터리
		BuffController.remove(o, EnchantDexterity.class);
		// 슬로우
		// 매스 슬로우
		// 인탱글
		// 윈드 셰클
		BuffController.remove(o, Slow.class);
		// 카운터 매직
		BuffController.remove(o, CounterMagic.class);
		// 메디테이션
		BuffController.remove(o, Meditation.class);
		// 버서커스
		BuffController.remove(o, Berserks.class);
		// 커스: 패럴라이즈
		BuffController.remove(o, CurseParalyze.class);
		// 인첸트 마이티
		BuffController.remove(o, EnchantMighty.class);
		// 헤이스트
		if(o.getInventory()==null || !o.getInventory().isSetOptionHaste())
			BuffController.remove(o, Haste.class);
		// 아이스 랜스
		BuffController.remove(o, IceLance.class);
		// 디지즈
		BuffController.remove(o, Disease.class);
		// 사일런스
		BuffController.remove(o, Silence.class);
		// 포그 오브 슬리프
//		BuffController.remove(o, 66, false);
		// 세이프 체인지
		if(!hasRing && ShapeChange.is변신을하거나해제해도되는가(o, null, true)) {
			BuffController.remove(o, ShapeChange.class);
		}
		if(hasRing) {
			if (BuffController.find(o, ShapeChange.class) != null) {
		        o.toSender(new S_ObjectEffect(o, 15846), true);
		    }
		}
		// 이뮨 투 암
		BuffController.remove(o, ImmuneToHarm.class);
		// 디케이 포션
		BuffController.remove(o, DecayPotion.class);
		// 앱솔루트 배리어
//		BuffController.remove(o, AbsoluteBarrier.class);
		// 홀리 워크
		// 무빙악셀레이션
		BuffController.remove(o, HolyWalk.class);
//		// 어드밴스 스피릿
//		BuffController.remove(o, AdvanceSpirit.class);

		// 레지스트매직
		BuffController.remove(o, ResistMagic.class);
		// 클리어마인드
		BuffController.remove(o, ClearMind.class);
		// 레지스트엘리멘탈
		BuffController.remove(o, ResistElemental.class);
		// 프로텍션프롬엘리멘탈
//				BuffController.remove(o, 92, false);
		// 파이어웨폰
		BuffController.remove(o, FireWeapon.class);
		// 윈드샷
		BuffController.remove(o, WindShot.class);
		// 윈드 워크
		BuffController.remove(o, WindWalk.class);
		// 브레스 오브 파이어
		BuffController.remove(o, BlessOfFire.class);
		// 아이오브스톰
		BuffController.remove(o, EyeOfStorm.class);
		// 어스바인드
		BuffController.remove(o, EarthBind.class);
		// 에어리어오브사일런스
//		BuffController.remove(o, 105, false);
		// 버닝웨폰
		BuffController.remove(o, BurningWeapon.class);
		// 스톰샷
		BuffController.remove(o, StormShot.class);
		// 아이언스킨
		BuffController.remove(o, IronSkin.class);
		// 폴루트 워터
		BuffController.remove(o, PolluteWater.class);
		// 카운터미러
		BuffController.remove(o, CounterMirror.class);
		// 이레이즈매직
		BuffController.remove(o, EraseMagic.class);
		// 소울 오브 프레임
		BuffController.remove(o, SoulOfFlame.class);
		// 아쿠아 프로텍트
		BuffController.remove(o, AquaProtect.class);
		// 엔조틱바이탈라이즈
		BuffController.remove(o, ExoticVitalize.class);
		// 어디셔널 파이어
		BuffController.remove(o, AdditionalFire.class);
		// 어스스킨 제거
		BuffController.remove(o, EarthSkin.class);

		// 글로잉오라
		BuffController.remove(o, GlowingAura.class);
		// 샤이닝오라
		BuffController.remove(o, ShiningAura.class);

		// 용기
		if(o.getInventory()==null || !o.getInventory().isSetOptionBrave())
			BuffController.remove(o, Bravery.class);
		// 아쿠아물약 (에바물약)
		BuffController.remove(o, Eva.class);
		// 지혜물약
		BuffController.remove(o, Wisdom.class);
		// 마력회복물약
		BuffController.remove(o, Blue.class);
		// 와퍼
		if(o.getInventory()==null || !o.getInventory().isSetOptionWafer())
			BuffController.remove(o, Wafer.class);
		// 구울 독
		BuffController.remove(o, CurseGhoul.class);
		
		// 인비지마법 제거.
		BuffController.remove(o, InvisiBility.class);
		
		// 카운터 배리어
		BuffController.remove(o, CounterBarrier.class);
		// 솔리드 캐리지
		BuffController.remove(o, SolidCarriage.class);
		// 바운스 어택
		BuffController.remove(o, BounceAttack.class);
		
		// 쉐도우팽
		BuffController.remove(o, ShadowFang.class);
		// 언케니닷지
		BuffController.remove(o, UncannyDodge.class);
		// 더블브레이크
		BuffController.remove(o, DoubleBreak.class);
		// 인첸트베놈
		BuffController.remove(o, EnchantVenom.class);
		// 드레스이베이젼
		BuffController.remove(o, DressEvasion.class);
		// 드레스덱스터리티
		BuffController.remove(o, DressDexterity.class);
		// 드레스마이티
		BuffController.remove(o, DressMighty.class);
		// 쉐도우아머
		BuffController.remove(o, ShadowArmor.class);
		// 베놈레지스트
		BuffController.remove(o, VenomResist.class);
		
	}
	
}
