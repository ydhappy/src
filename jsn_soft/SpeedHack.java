package jsn_soft;

import lineage.bean.database.Skill;
import lineage.bean.database.SpriteFrame;
import lineage.database.HackNoCheckDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class SpeedHack {

	static public boolean isAutoMoveTime(PcInstance pc) {
		if(HackNoCheckDatabase.isHackCheck(pc)) {
			return true;
		}
		long temp = System.currentTimeMillis() - pc.getAutoTimeMove();
		if (pc.isLock()) {
			return false;
		}
		//if (pc.getGfx() == 3126) {
		//	return true;
		//}
		
		
		long interval = (long)(getGfxFrameTime(pc, pc.getGfx(), pc.getGfxMode() + Lineage.GFX_MODE_WALK)
				* Lineage.speed_check_walk_frame_rate);
//				System.out.println(pc.getGfx());
//		System.out.println(temp); // 측정 됨
//		System.out.println(interval);
		if (temp < interval) {
		
			pc.speedCheck(pc.getGfxMode() + Lineage.GFX_MODE_WALK, temp, interval);
			return false;	
		}
		if (temp >= interval) {
			pc.setAutoTimeMove(System.currentTimeMillis());
			return true;
		}
		return false;
	}
	
	static public boolean isAttackTime(PcInstance pc, int action, boolean triple) {

		
		pc.setLastAttackMotion(action);

		long frame = (long) (getGfxFrameTime(pc, pc.getGfx(), action) * (HackNoCheckDatabase.isHackCheck(pc) ? 0.2 : pc.isAutoAttack ? 0 : Lineage.speed_check_attack_frame_rate));
		long skillframe = (long) (getGfxFrameTime(pc, pc.getGfx(), pc.getLastSkillMotion())
				* (pc.getLastSkillMotion() == Lineage.GFX_MODE_SPELL_DIRECTION ? 2 : 1.7));
		System.out.println(frame);
//		System.out.println(skillframe);
//		System.out.println(pc.getGfx());
		return isAttack(pc, frame, skillframe, triple);
	}

	static public boolean isMagicTime(PcInstance pc, int action) {
		if(HackNoCheckDatabase.isHackCheck(pc)) {
			return true;
		}
	
		pc.setLastSkillMotion(action);
//		System.out.println(action);
		if (!HackNoCheckDatabase.isHackCheck(pc)) {
		long frame = (long) (getGfxFrameTime(pc, pc.getGfx(), action)
				* (action == Lineage.GFX_MODE_SPELL_DIRECTION ? Lineage.speed_check_dir_magic_frame_rate : Lineage.speed_check_no_dir_magic_frame_rate));
		return isSkill(pc, frame);
		}
		else {  
			long frame = (long) (getGfxFrameTime(pc, pc.getGfx(), action)
					* (action == Lineage.GFX_MODE_SPELL_DIRECTION ? 1.2 : 1));
			return isSkill(pc, frame);
		}
		
	}

	static public boolean isAttack(PcInstance pc, long frame, long skillframe, boolean triple) {
		long time = System.currentTimeMillis();
//		System.out.println(time);
//		System.out.println(pc.getAttackTime());
//		System.out.println(frame);
//		System.out.println(skillframe);
//		System.out.println(pc.getSkillTime());
		if (pc.isLock())
			return false;

		if (pc.getSkillTime() != 0 && pc.getSkillTime() > time)
			return false;

		if (pc.getAttackTime() == 0) {
			pc.setAttackTime(time + frame);
			return true;
		}

		if (pc.getAttackTime() >= time && !triple) {
			return false;
		}

		if (pc.getAttackTime() < time || triple) {
			pc.setAttackTime(time + frame + (triple ? 500 : 0));
			return true;
		}
		return false;
	}

	static public boolean isSkill(PcInstance pc, long frame) {
		long time = System.currentTimeMillis();
//		System.out.println(time);
//		System.out.println(frame);

		if (pc.isLock()) {
			return false;
		}

		if (pc.getSkillTime() == 0) {
			pc.setSkillTime(time + frame);
			return true;
		}

		if (pc.getSkillTime() >= time) {
			return false;
		}

		if (pc.getSkillTime() < time) {
			pc.setSkillTime(time + frame);
			return true;
		}

		return false;
	}
	
	/**
	 * gfx에 해당하는action값에 프레임값을 리턴함. 쿨타임 설정용으로 사용.
	 */
	static public int getGfxFrameTime(object o, int gfx, int action) {
		SpriteFrame spriteFrame = SpriteFrameDatabase.getList().get(gfx);

		if (spriteFrame != null) {
			double frame = 0;
			Integer gfxFrame = spriteFrame.getList().get(action);

			if (gfxFrame != null)
				frame = gfxFrame.intValue();
			else
				return 1000;

			// 일반적인 촐기 용기 안한 상태
			if (o.getSpeed() == 0 && !o.isBrave())
				frame *= 42;
			// 촐기 또는 용기 상태
			else if ((o.getSpeed() == 1 && !o.isBrave()) || (o.getSpeed() == 0 && o.isBrave()))
				frame *= 31.5;
			// 촐기 용기 둘다
			else if (o.getSpeed() == 1 && o.isBrave())
				frame *= 23.5;
			// 슬로우 걸렸을 시, 촐기 용기 안한 상태
			else if (o.getSpeed() == 2 && !o.isBrave())
				frame *= 81;
			// 슬로우 걸렸을 시, 촐기 안한 상태
			else if (o.getSpeed() == 2 && o.isBrave())
				frame *= 61;
			
			
			frame /= 40;
			return (int) (frame);
		}
		// 해당하는 모드가 없을경우 1초로 정의
		return 1000;
	}

	/**
	 * 스킬 사용시 모션을 몇번을 개산해야할지 계산해주는 함수.
	 */
	static public int getSkillMotion(Skill skill) {
		switch ((int)skill.getUid()) {
		case 4: // 에너지 볼트
		case 6: // 아이스 대거
		case 7: // 윈드 커터
		case 10: // 칠 터치
		case 15: // 파이어 애로우
		case 16: // 쇼크스턴
		case 17: // 라이트닝
		case 22: // 프로즌 클라우드
		case 25: // 파이어볼
		case 28: // 뱀파이어릭 터치
		case 30: // 어스 재일
		case 34: // 콜 라이트닝
		case 38: // 콘 오브 콜드
		case 45: // 이럽션
		case 46: // 선 버스트
		case 50: // 아이스 랜스
		case 53: // 토네이도
		case 59: // 블리자드
		case 62: // 어스 퀘이크
		case 70: // 파이어 스톰
		case 74: // 미티어 스트라이크
		case 77: // 디스인티그레이트
		case 115: // 트리플 애로우
			return Lineage.GFX_MODE_SPELL_DIRECTION;
		}
		return Lineage.GFX_MODE_SPELL_NO_DIRECTION;
	}
}
