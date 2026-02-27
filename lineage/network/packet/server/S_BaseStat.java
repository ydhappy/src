package lineage.network.packet.server;

import lineage.bean.lineage.BaseStat;
import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.share.Lineage;
import lineage.world.object.instance.PcInstance;

public class S_BaseStat extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, BaseStat bs){
		if(bp == null)
			bp = new S_BaseStat(bs);
		else
			((S_BaseStat)bp).toClone(bs);
		return bp;
	}

	static synchronized public BasePacket clone(BasePacket bp, PcInstance pc){
		if(bp == null)
			bp = new S_BaseStat(pc);
		else
			((S_BaseStat)bp).toClone(pc);
		return bp;
	}

	static synchronized public BasePacket clone(BasePacket bp, int type){
		if(bp == null)
			bp = new S_BaseStat(type);
		else
			((S_BaseStat)bp).toClone(type);
		return bp;
	}
	
	public S_BaseStat(BaseStat bs){
		toClone(bs);
	}
	
	public S_BaseStat(PcInstance pc){
		toClone(pc);
	}
	
	public S_BaseStat(int type){
		toClone(type);
	}
	
	public void toClone(BaseStat bs){
		clear();
		writeC(Opcodes.S_OPCODE_BASESTAT);
		writeC(bs.getType());
		switch(bs.getType()) {
			case 1:	// 스탯분배
				writeH(bs.getHp());
				writeH(bs.getMp());
				writeC(bs.getAc());
				writeC(0x05);
				break;
			case 2:	// 레벨업
				writeC(bs.getLev());
				writeC(bs.getLevMax());
				writeH(bs.getHp());
				writeH(bs.getMp());
				writeC(bs.getAc());
				writeC(0x00);
				writeC(bs.getStr()+bs.getLvStr()+bs.getElixirStr());
				writeC(bs.getInt()+bs.getLvInt()+bs.getElixirInt());
				writeC(bs.getWis()+bs.getLvWis()+bs.getElixirWis());
				writeC(bs.getDex()+bs.getLvDex()+bs.getElixirDex());
				writeC(bs.getCon()+bs.getLvCon()+bs.getElixirCon());
				writeC(bs.getCha()+bs.getLvCha()+bs.getElixirCha());
				break;
			case 3:	// 종료
				break;
		}
	}
	
	public void toClone(PcInstance pc){
		clear();
		writeC(Opcodes.S_OPCODE_BASESTAT);
		writeC(4);
		switch(pc.getClassType()) {
			case 0x00:
				writeC( Lineage.royal_stat_int + Lineage.royal_stat_str * 16 );
				writeC( Lineage.royal_stat_dex + Lineage.royal_stat_wis * 16 );
				writeC( Lineage.royal_stat_cha + Lineage.royal_stat_con * 16 );
				break;
			case 0x01:
				writeC( Lineage.knight_stat_int + Lineage.knight_stat_str * 16 );
				writeC( Lineage.knight_stat_dex + Lineage.knight_stat_wis * 16 );
				writeC( Lineage.knight_stat_cha + Lineage.knight_stat_con * 16 );
				break;
			case 0x02:
				writeC( Lineage.elf_stat_int + Lineage.elf_stat_str * 16 );
				writeC( Lineage.elf_stat_dex + Lineage.elf_stat_wis * 16 );
				writeC( Lineage.elf_stat_cha + Lineage.elf_stat_con * 16 );
				break;
			case 0x03:
				writeC( Lineage.wizard_stat_int + Lineage.wizard_stat_str * 16 );
				writeC( Lineage.wizard_stat_dex + Lineage.wizard_stat_wis * 16 );
				writeC( Lineage.wizard_stat_cha + Lineage.wizard_stat_con * 16 );
				break;
			case 0x04:
				writeC( Lineage.darkelf_stat_int + Lineage.darkelf_stat_str * 16 );
				writeC( Lineage.darkelf_stat_dex + Lineage.darkelf_stat_wis * 16 );
				writeC( Lineage.darkelf_stat_cha + Lineage.darkelf_stat_con * 16 );
				break;
			case 0x05:
				writeC( Lineage.dragonknight_stat_int + Lineage.dragonknight_stat_str * 16 );
				writeC( Lineage.dragonknight_stat_dex + Lineage.dragonknight_stat_wis * 16 );
				writeC( Lineage.dragonknight_stat_cha + Lineage.dragonknight_stat_con * 16 );
				break;
			case 0x06:
				writeC( Lineage.blackwizard_stat_int + Lineage.blackwizard_stat_str * 16 );
				writeC( Lineage.blackwizard_stat_dex + Lineage.blackwizard_stat_wis * 16 );
				writeC( Lineage.blackwizard_stat_cha + Lineage.blackwizard_stat_con * 16 );
				break;
		}
		writeC( 0x00 );
	}
	
	public void toClone(int type) {
		clear();
		writeC(Opcodes.S_OPCODE_BASESTAT);
		writeC(type);
		switch(type) {
			case 0x16:
				writeD(0);
				writeH(0);
				writeD(5);
				writeD(0);
				writeH(1);
				break;
			case 0x0a:
				writeD(2);
				break;
			case 0x40:
				break;
			case 0x3f:
				writeD(01);
				break;
		}
	}

}
