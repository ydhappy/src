package lineage.world.object.instance;

import lineage.bean.lineage.Clan;
import lineage.database.ServerDatabase;
import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectAction;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.network.packet.server.S_Message;
import lineage.world.controller.CharacterController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.ClanController;
import lineage.world.controller.ClanLordController;
import lineage.world.object.Character;
import lineage.world.object.magic.GlowingAura;
import lineage.world.object.magic.ShiningAura;
import lineage.world.object.magic.EnchantDexterity;
import lineage.world.object.magic.EnchantMighty;

public class RobotClanInstance extends Character {

	public String _comment; // 광고 문구.
	private int show_comment; //
	private int buff_time; //
	public int classType;
	public int classSex;
	public String pc_name;
	public int royal_true;
	public int knight_true;
	public int elf_true;
	public int wizard_true;
	public int darkelf_true;
	public long pc_objid;
	public int join_level;
	
	public boolean skill_1st;
	public boolean skill_2nd;
	
	public int getRoyal_true() {
		return royal_true;
	}

	public void setRoyal_true(int royal_true) {
		this.royal_true = royal_true;
	}

	public int getKnight_true() {
		return knight_true;
	}

	public void setKnight_true(int knight_true) {
		this.knight_true = knight_true;
	}

	public int getElf_true() {
		return elf_true;
	}

	public void setElf_true(int elf_true) {
		this.elf_true = elf_true;
	}

	public int getWizard_true() {
		return wizard_true;
	}

	public void setWizard_true(int wizard_true) {
		this.wizard_true = wizard_true;
	}

	public int getDarkelf_true() {
		return darkelf_true;
	}

	public void setDarkelf_true(int darkelf_true) {
		this.darkelf_true = darkelf_true;
	}

	public long getPc_objid() {
		return pc_objid;
	}

	public void setPc_objid(long pc_objid) {
		this.pc_objid = pc_objid;
	}
	
	public int getJoin_level() {
		return join_level;
	}

	public void setJoin_level(int join_level) {
		this.join_level = join_level;
	}

	public RobotClanInstance(long pc_objId, String pc_name, int classType, int classSex, Clan c, int gfx, boolean buff1, boolean buff2) {

		this.pc_name = pc_name;
		this.classType = classType;
		this.classSex = classSex;

		setGfx(gfx);

		setObjectId(ServerDatabase.nextEtcObjId());
		setName(c.getName() + "혈맹");
		setClanId(c.getUid());
		setClanName(c.getName());
		setTitle("");
		setLawful(66536);
		setGfxMode(0);
		setMaxHp(1000000);
		setNowHp(1000000);
		setPc_objid(pc_objId);
		
		skill_1st = buff1;
		skill_2nd = buff2;
		
		CharacterController.toWorldJoin(this);
		
		ClanLordController.getList().add(this);
	}
	
	public void toDamage(Character cha, int dmg, int type, Object...opt){
		Clan c = ClanController.find(getClanName());
		
		if(cha instanceof PcInstance) {
			PcInstance pc = (PcInstance) cha;
			
			ChattingController.toChatting(pc, String.format("\\fU가입 최소레벨\\fT%d,\\fU군주:\\fT%s,\\fU기사:\\fT%s,\\fU요정:\\fT%s,\\fU법사:\\fT%s",
					getJoin_level(),
					getRoyal_true() == 1 ? "가" : "불",
					getKnight_true() == 1 ? "가" : "불",
					getElf_true() == 1 ? "가" : "불",
					getWizard_true() == 1 ? "가" : "불"
					), Lineage.CHATTING_MODE_MESSAGE);
			
			if(pc.getClanId() != 0) {
				ChattingController.toChatting(pc, "\\fW혈맹에 가입하지 않은 캐릭만 가능합니다.", Lineage.CHATTING_MODE_MESSAGE);
				ChattingController.toChatting(pc, "\\fW혈맹 탈퇴후에 가입하세요.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getLevel() < getJoin_level()) {
				ChattingController.toChatting(pc, String.format("\\fR%d레벨 이상만 가입가능합니다.", getJoin_level()), Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getClassType() == Lineage.LINEAGE_CLASS_ROYAL) {
				ChattingController.toChatting(pc, "\\fR군주는 혈맹 가입이 불가합니다", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
//			if(pc.getClassType() == Lineage.LINEAGE_CLASS_ROYAL && getRoyal_true() == 0) {
//				ChattingController.toChatting(pc, "\\fR군주는 가입받지 않습니다.", Lineage.CHATTING_MODE_MESSAGE);
//				return;
//			}
			
			if(pc.getClassType() == Lineage.LINEAGE_CLASS_KNIGHT && getKnight_true() == 0) {
				ChattingController.toChatting(pc, "\\fR기사는 가입받지 않습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getClassType() == Lineage.LINEAGE_CLASS_ELF && getElf_true() == 0) {
				ChattingController.toChatting(pc, "\\fR요정은 가입받지 않습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getClassType() == Lineage.LINEAGE_CLASS_WIZARD && getWizard_true() == 0) {
				ChattingController.toChatting(pc, "\\fR마법사는 가입받지 않습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
		}
		
		if(cha instanceof PcInstance) {
			PcInstance pc = (PcInstance) cha;
			pc.setRobot_clan_name(c.getName());
			ClanLordController.toJoin(pc);
		}
	}

	@Override
	public void close() {

		clearList(true);
		World.remove(this);
		ClanLordController.getList().remove(this);

		super.close();
		
		CharacterController.toWorldOut(this);
		
	}

	
	@Override
	public void toTimer(long time) {
		
		if(getNowHp() < getMaxHp()) {
			setNowHp(getMaxHp());
		}
		if (!isWorldDelete() && _comment != null && _comment.length() > 0) {
			if (show_comment++ % 20 == 0) {
				ChattingController.toChatting(this, String.format("%s", _comment), Lineage.CHATTING_MODE_SHOUT);
				toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), this, 68), true);
			}
		}
		
		Clan c = ClanController.find(getClanName());
		
		if (c != null && show_comment++ % 6 == 0) {
			ChattingController.toChatting(this, "자 우리혈맹 버프들어갑니다잉~", Lineage.CHATTING_MODE_SHOUT);
				for(PcInstance pc : c.getList()) {
					// 글로잉오라
					GlowingAura.onBuff(pc, SkillDatabase.find(15, 1));
					ChattingController.toChatting(pc, "\\fY글로잉 오라 AC+3,마방+2", 20);
					// 샤이닝오라
					ShiningAura.onBuff(pc, SkillDatabase.find(15, 2));
					ChattingController.toChatting(pc, "\\fY샤이닝 오라 PvP대미지+1,SP+1,리덕+2", 20);
			}
		}
		
	}
}