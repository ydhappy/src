package lineage.world.object.item.sp;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.ServerBasePacket;
import lineage.network.packet.client.C_StatDice;
import lineage.network.packet.server.S_CharacterStat;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.CharacterController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class Base_Stat_Clear extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Base_Stat_Clear();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		
		if(!World.isSafetyZone(cha.getX(), cha.getY(), cha.getMap())){
			ChattingController.toChatting(cha, String.format("마을에서 사용 가능합니다."), 20);
			return;
		}
		PcInstance pc  = (PcInstance)cha;
		if(pc.stat_clear==true){
			ChattingController.toChatting(cha, String.format("스텟 초기화 중에는 사용 하실 수 없습니다."), 20);
			return;
		}
		
		// 베이스스탯 재 설정.
		ServerBasePacket sbp = (ServerBasePacket)ServerBasePacket.clone(BasePacketPooling.getPool(ServerBasePacket.class), null);
		sbp.writeC(0);
		sbp.writeC(cha.getClassType());

		C_StatDice.clone(BasePacketPooling.getPool(C_StatDice.class), sbp.getByte(), sbp.getSize()).init(pc.getClient());
		cha.setStr(pc.getClient().getStatDice().getStr());
		cha.setDex(pc.getClient().getStatDice().getDex());
		cha.setCon(pc.getClient().getStatDice().getCon());
		cha.setWis(pc.getClient().getStatDice().getWis());
		cha.setInt(pc.getClient().getStatDice().getInt());
		cha.setCha(pc.getClient().getStatDice().getCha());
		
	//	cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
		
	/*	int Str = cha.getStr()+cha.getLvStr();
		int Dex = cha.getDex()+cha.getLvDex();
		int Con = cha.getCon()+cha.getLvCon();
		int Wis = cha.getWis()+cha.getLvWis();
		int Int = cha.getInt()+cha.getLvInt();
		int Cha = cha.getCha()+cha.getLvCha();*/
		//두개의 값이 만약에 스텟맥스보다 크다면
		
		// hp&mp 초기화
		switch(cha.getClassType()) {
			case Lineage.LINEAGE_CLASS_ROYAL:
				cha.setMaxHp(Lineage.royal_hp);
				cha.setMaxMp(Lineage.royal_mp);
				cha.setNowHp(Lineage.royal_hp);
				cha.setNowMp(Lineage.royal_mp);
				break;
			case Lineage.LINEAGE_CLASS_ELF:
				cha.setMaxHp(Lineage.elf_hp);
				cha.setMaxMp(Lineage.elf_mp);
				cha.setNowHp(Lineage.elf_hp);
				cha.setNowMp(Lineage.elf_mp);
				break;
			case Lineage.LINEAGE_CLASS_KNIGHT:
				cha.setMaxHp(Lineage.knight_hp);
				cha.setMaxMp(Lineage.knight_mp);
				cha.setNowHp(Lineage.knight_hp);
				cha.setNowMp(Lineage.knight_mp);
				break;
			case Lineage.LINEAGE_CLASS_WIZARD:
				cha.setMaxHp(Lineage.wizard_hp);
				cha.setMaxMp(Lineage.wizard_mp);
				cha.setNowHp(Lineage.wizard_hp);
				cha.setNowMp(Lineage.wizard_mp);
				break;
			case Lineage.LINEAGE_CLASS_DARKELF:
				cha.setMaxHp(Lineage.darkelf_hp);
				cha.setMaxMp(Lineage.darkelf_mp);
				cha.setNowHp(Lineage.darkelf_hp);
				cha.setNowMp(Lineage.darkelf_mp);
				break;
		}
		
		

		// hp&mp 세팅
		int hp=0, mp=0;
		for(int i=1 ; i<pc.getLevel() ; ++i) {
			hp += CharacterController.toStatusUP(pc, true);
			mp += CharacterController.toStatusUP(pc, false);
		}
		int new_hp = pc.getMaxHp()+hp;
		int new_mp = pc.getMaxMp()+mp;

		switch(pc.getClassType()){
			case 0x00:
				if(new_hp>=Lineage.royal_max_hp && Lineage.royal_max_hp>0)
					new_hp = Lineage.royal_max_hp;
				if(new_mp >= Lineage.royal_max_mp && Lineage.royal_max_mp>0)
					new_mp = Lineage.royal_max_mp;
				break;
			case 0x01:
				if(new_hp >= Lineage.knight_max_hp && Lineage.knight_max_hp>0)
					new_hp = Lineage.knight_max_hp;
				if(new_mp >= Lineage.knight_max_mp && Lineage.knight_max_mp>0)
					new_mp = Lineage.knight_max_mp;
				break;
			case 0x02:
				if(new_hp >= Lineage.elf_max_hp && Lineage.elf_max_hp>0)
					new_hp = Lineage.elf_max_hp;
				if(new_mp >= Lineage.elf_max_mp && Lineage.elf_max_mp>0)
					new_mp = Lineage.elf_max_mp;
				break;
			case 0x03:
				if(new_hp >= Lineage.wizard_max_hp && Lineage.wizard_max_hp>0)
					new_hp = Lineage.wizard_max_hp;
				if(new_mp >= Lineage.wizard_max_mp && Lineage.wizard_max_mp>0)
					new_mp = Lineage.wizard_max_mp;
				break;
			case 0x04:
				if(new_hp >= Lineage.darkelf_max_hp && Lineage.darkelf_max_hp>0)
					new_hp = Lineage.darkelf_max_hp;
				if(new_mp >= Lineage.darkelf_max_mp && Lineage.darkelf_max_mp>0)
					new_mp = Lineage.darkelf_max_mp;
				break;
			case 0x05:
				if(new_hp >= Lineage.dragonknight_max_hp && Lineage.dragonknight_max_hp>0)
					new_hp = Lineage.dragonknight_max_hp;
				if(new_mp >= Lineage.dragonknight_max_mp && Lineage.dragonknight_max_mp>0)
					new_mp = Lineage.dragonknight_max_mp;
				break;
			case 0x06:
				if(new_hp >= Lineage.blackwizard_max_hp && Lineage.blackwizard_max_hp>0)
					new_hp = Lineage.blackwizard_max_hp;
				if(new_mp >= Lineage.blackwizard_max_mp && Lineage.blackwizard_max_mp>0)
					new_mp = Lineage.blackwizard_max_mp;
				break;
		}
		
		pc.setMaxHp( new_hp );
		pc.setMaxMp( new_mp );
		pc.setNowHp( pc.getNowHp()+hp );
		pc.setNowMp( pc.getNowMp()+mp );
		
		
		ChattingController.toChatting(pc, String.format("베이스 스텟이 재편성 되었습니다."), 20);
		
		cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
	
		// 아이템 수량 갱신
		cha.getInventory().count(this, getCount()-1, true);
	}

}
