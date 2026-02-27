package jsn_soft;

import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.object.object;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.SummonInstance;
import lineage.world.object.monster.Harphy;
import lineage.world.object.monster.Spartoi;
import lineage.world.object.monster.StoneGolem;
import lineage.world.object.monster.만드라고라;

/**
 * 로봇과 플레이어 자동사냥의 공통 로직을 담은 클래스.
 * 성능 최적화 및 로직 단일화를 위해 설계됨.
 */
public final class AutoHuntCommonLogic {

    /**
     * 대상(Target)이 공격 가능한 상태인지 확인.
     * 
     * @param actor       공격자 (PC 또는 로봇)
     * @param target      대상
     * @param searchRange 탐색 범위
     * @return 공격 가능 여부
     */
    public static boolean isAttackable(PcInstance actor, object target, int searchRange) {
        if (target == null || target.isDead() || target.isWorldDelete())
            return false;

        // 투명 상태 및 세이프티 존 확인
        if (target.isInvis() || World.isSafetyZone(target.getX(), target.getY(), target.getMap()))
            return false;

        // 거리 확인
        if (!Util.isDistance(actor.getX(), actor.getY(), actor.getMap(), target.getX(), target.getY(), target.getMap(),
                searchRange))
            return false;

        // 고저차 또는 벽 확인
        if (!Util.isAreaAttack(actor, target))
            return false;

        // 특수 몬스터 상태 확인 (숨음, 비행 등)
        if (target instanceof MonsterInstance) {
            if (isSpecialState((MonsterInstance) target))
                return false;

            // 다른 유저가 선점했는지 확인 (스틸 방지 로직 - 옵션에 따라 조절 가능)
            if (Lineage.robot_auto_pc_steal_prevent) {
                if (isAlreadyTargetedByOthers(actor, (MonsterInstance) target))
                    return false;
            }
        }

        return true;
    }

    /**
     * 몬스터의 특수 상태(숨어있거나 공격 불가능한 프레임 모드) 확인.
     */
    private static boolean isSpecialState(MonsterInstance mon) {
        int gfx = mon.getGfx();
        int mode = mon.getGfxMode();

        if (mon instanceof Spartoi && mode == 28 && gfx == 145)
            return true;
        if (mon instanceof Harphy && (mode == 44 || mode == 45))
            return true;
        if (mon instanceof 만드라고라 && mode == 4 && gfx == 11)
            return true;
        if (mon instanceof StoneGolem && ((mode == 4 && gfx == 49) || (mode == 11 && gfx == 145)))
            return true;

        return false;
    }

    /**
     * 이미 다른 유저에게 타겟팅 되었거나 공격받고 있는지 확인.
     */
    private static boolean isAlreadyTargetedByOthers(PcInstance actor, MonsterInstance mon) {
        if (mon.getAttackList() != null && mon.getAttackList().size() > 0) {
            object firstAttacker = mon.getAttackList().get(0);
            if (firstAttacker != null && firstAttacker.getObjectId() != actor.getObjectId()) {
                if (firstAttacker instanceof PcInstance) {
                    PcInstance other = (PcInstance) firstAttacker;
                    // 다른 유저가 이 몬스터를 계속 조준하고 있다면 양보
                    if (other.isAutoHunt() && other.getAutoTarget() != null
                            && other.getAutoTarget().getObjectId() == mon.getObjectId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 소환수나 펫인지 확인.
     */
    public static boolean isSummonOrPet(object target, PcInstance owner) {
        if (target instanceof SummonInstance) {
            SummonInstance summon = (SummonInstance) target;
            if (summon.getSummon().getMasterObjectId() == owner.getObjectId() ||
                    summon.getSummon().getMaster().getClanId() == owner.getClanId() ||
                    summon.getSummon().getMaster().getPartyId() == owner.getPartyId()) {
                return true;
            }
        }
        return false;
    }
}
