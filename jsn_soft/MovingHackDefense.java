package jsn_soft;

import lineage.gui.GuiMain;
import lineage.share.Common;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.BackgroundInstance;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.MagicDollInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.npc.background.DeathEffect;
import lineage.world.object.npc.background.Firewall;
import lineage.world.object.npc.background.LifeStream;
import lineage.world.object.npc.background.Switch;
import lineage.world.object.npc.background.door.Door;
import lineage.world.object.npc.kingdom.KingdomCastleTop;
import lineage.world.object.npc.kingdom.KingdomCrown;
import lineage.world.object.npc.kingdom.KingdomDoor;
import lineage.world.object.robot.PcRobotInstance;

public class MovingHackDefense {

	// 뚫어 핵 테스트용 소스 이동부분에 넣으면 된다.
//	heading = Util.calcheading(this, o);
//	int x = Util.getXY(getHeading(), true)+this.getX();
//	int y = Util.getXY(getHeading(), false)+this.getY();
//	toMoving(null, x, y, getHeading(), false);

	static public boolean HackDefense(object o, int x, int y, int map) {
		if (!Lineage.move_hack_defence)
			return false;
		// 버그 방지
		if (o == null)
			return false;
		// 운영자라면 성공
		if (o.getGm() > 0)
			return true;
		// 로봇이라면 성공
		if (o instanceof PcRobotInstance)
			return true;

		if (!(o instanceof PcInstance))
			return true;

		PcInstance pc = (PcInstance) o;
		// 자동사냥중이라면 성공
		if (pc.isAutoHunt())
			return true;
		boolean result = false;
		// 매 이동시마다 체크
//				result = true;
		for (object oo : o.getInsideList()) {
			if (Lineage.move_hack_defence_log && oo != null && oo.getX() == x && oo.getY() == y && oo.getMap() == map) {
				System.out.println("캐릭터이름: " + oo.getName());
				System.out.println("동일 오브젝트 체크 여부: " + (oo.getObjectId() != o.getObjectId()));
				System.out.println("제외 캐릭터 여부: " + !(뚫어핵체크제외객체(oo)));
				System.out.println("죽었는지 여부: " + !oo.isDead());
				System.out.println("월드 아웃 여부: " + !oo.isWorldDelete());
				System.out.println("투명 여부: " + !oo.isInvis());
				System.out.println("무빙 여부: " + oo.isMove());
			}
			if (oo != null && oo.getObjectId() != o.getObjectId() && oo.getX() == x && oo.getY() == y
					&& oo.getMap() == map && !(뚫어핵체크제외객체(oo)) && !oo.isDead() && !oo.isWorldDelete() && !oo.isInvis()
					&& oo.getNowHp() > 0 && !o.isTransparent()) {
				result = false;
				
				if (뚫어핵체크제외객체(oo))
					result = true;
				if (oo.isDead())
					result = true;
				if (oo.isWorldDelete())
					result = true;
				if (oo.isInvis())
					result = true;
				if (oo.getNowHp() <= 0)
					result = true;
				if (oo.isMove())
					result = true;
				if (oo.isTeleport())
					result = true;
			}
		}

		if (!World.isMapdynamic(x, y, map))
			result = true;

		if (!World.getMapObject(x, y, map))
			result = true;

		if (!result) {
			HackDefenseLog(pc, x, y);
			ChattingController.toChatting(o, "뚫어 방지, 제자리로 돌아갑니다.", Lineage.CHATTING_MODE_MESSAGE);
		}
//		System.out.println("통과 못해서 걸림");
		return result;
	}

	public static boolean HackDefenseLog(PcInstance pc, int x, int y) {
		if (!Lineage.move_hack_defence)
			return false;
		
		boolean result = true;
		object target = null;

		if (result && target == null) {
			pc.setSpeedHackWarningCounting(pc.getSpeedHackWarningCounting() + 1);

//			ChattingController.toChatting(pc, String.format("[뚫어핵 의심] %d분간 캐릭터 마비.", (120 / 60)),
//					Lineage.CHATTING_MODE_MESSAGE);

//				FrameSpeedOverStun.init(pc, 5);
			if (!Common.system_config_console) {
				long time = System.currentTimeMillis();
				String timeString = Util.getLocaleString(time, true);

				String log = String.format("[%s]\t [뚫어핵 의심]\t [캐릭터: %s]\t [뚫어핵 횟수: %d회]\t [GFX: %d]\t", timeString,
						pc.getName(), pc.getSpeedHackWarningCounting(), pc.getGfx());

				GuiMain.display.asyncExec(new Runnable() {
					@Override
					public void run() {
						GuiMain.getViewComposite().getSpeedHackComposite().toLog(log);
					}
				});
			}
			return false;
		}
		return true;
	}

	public boolean 뚫어핵체크제외Gfx(object o) {
		if (o == null)
			return true;

		return o.getGfx() == 1284;
	}

	public boolean 뚫어핵체크객체(object o) {
		if (o == null)
			return false;

		return o instanceof object;
	}

	static public boolean 뚫어핵체크제외객체(object o) {
		if (o == null)
			return true;

		return o instanceof ItemInstance || (o instanceof BackgroundInstance && !(o instanceof Door))
				|| o instanceof KingdomDoor || o instanceof KingdomCastleTop || o instanceof KingdomCrown
				|| o instanceof Switch || o instanceof Firewall || o instanceof LifeStream
				|| o instanceof MagicDollInstance || o instanceof DeathEffect || (o instanceof Door && o.getGfxMode() == 28);
	}
}
