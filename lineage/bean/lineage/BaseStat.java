package lineage.bean.lineage;

import lineage.share.Lineage;
import lineage.world.object.instance.PcInstance;

public class BaseStat extends StatDice {
	private int LvStr;
	private int LvInt;
	private int LvWis;
	private int LvDex;
	private int LvCon;
	private int LvCha;
	private int ElixirStr;
	private int ElixirInt;
	private int ElixirWis;
	private int ElixirDex;
	private int ElixirCon;
	private int ElixirCha;
	private int Hp;
	private int Mp;
	private int Lev;
	private int LevMax;
	private int Ac;
	private int ClassType;
	public BaseStat(PcInstance pc) {
		//
		setType(1);
		//
		ClassType = pc.getClassType();
		Lev = 1;
		LevMax = pc.getLevel();
		ElixirStr = pc.getElixirStr();
		ElixirInt = pc.getElixirInt();
		ElixirWis = pc.getElixirWis();
		ElixirDex = pc.getElixirDex();
		ElixirCon = pc.getElixirCon();
		ElixirCha = pc.getElixirCha();
		
		Ac = 266;
		switch(ClassType) {
			case 0x00:
				Hp = Lineage.royal_hp;
				Mp = Lineage.royal_mp;
				break;
			case 0x01:
				Hp = Lineage.knight_hp;
				Mp = Lineage.knight_mp;
				break;
			case 0x02:
				Hp = Lineage.elf_hp;
				Mp = Lineage.elf_mp;
				break;
			case 0x03:
				Hp = Lineage.wizard_hp;
				Mp = Lineage.wizard_mp;
				break;
			case 0x04:
				Hp = Lineage.darkelf_hp;
				Mp = Lineage.darkelf_mp;
				break;
			case 0x05:
				Hp = Lineage.dragonknight_hp;
				Mp = Lineage.dragonknight_mp;
				break;
			case 0x06:
				Hp = Lineage.blackwizard_hp;
				Mp = Lineage.blackwizard_mp;
				break;
		}
	}
	public int getLvStr() {
		return LvStr;
	}
	public void setLvStr(int lvStr) {
		LvStr = lvStr;
	}
	public int getLvInt() {
		return LvInt;
	}
	public void setLvInt(int lvInt) {
		LvInt = lvInt;
	}
	public int getLvWis() {
		return LvWis;
	}
	public void setLvWis(int lvWis) {
		LvWis = lvWis;
	}
	public int getLvDex() {
		return LvDex;
	}
	public void setLvDex(int lvDex) {
		LvDex = lvDex;
	}
	public int getLvCon() {
		return LvCon;
	}
	public void setLvCon(int lvCon) {
		LvCon = lvCon;
	}
	public int getLvCha() {
		return LvCha;
	}
	public void setLvCha(int lvCha) {
		LvCha = lvCha;
	}
	public int getElixirStr() {
		return ElixirStr;
	}
	public void setElixirStr(int elixirStr) {
		ElixirStr = elixirStr;
	}
	public int getElixirInt() {
		return ElixirInt;
	}
	public void setElixirInt(int elixirInt) {
		ElixirInt = elixirInt;
	}
	public int getElixirWis() {
		return ElixirWis;
	}
	public void setElixirWis(int elixirWis) {
		ElixirWis = elixirWis;
	}
	public int getElixirDex() {
		return ElixirDex;
	}
	public void setElixirDex(int elixirDex) {
		ElixirDex = elixirDex;
	}
	public int getElixirCon() {
		return ElixirCon;
	}
	public void setElixirCon(int elixirCon) {
		ElixirCon = elixirCon;
	}
	public int getElixirCha() {
		return ElixirCha;
	}
	public void setElixirCha(int elixirCha) {
		ElixirCha = elixirCha;
	}
	public int getHp() {
		return Hp;
	}
	public void setHp(int hp) {
		Hp = hp;
	}
	public int getMp() {
		return Mp;
	}
	public void setMp(int mp) {
		Mp = mp;
	}
	public int getLev() {
		return Lev;
	}
	public void setLev(int lev) {
		Lev = lev;
	}
	public int getLevMax() {
		return LevMax;
	}
	public void setLevMax(int levMax) {
		LevMax = levMax;
	}
	public int getAc() {
		return Ac;
	}
	public void setAc(int ac) {
		Ac = ac;
	}
	public int getClassType() {
		return ClassType;
	}
	public void setClassType(int classtype) {
		ClassType = classtype;
	}
	public int getLvStat(){
		return getLvStr()+getLvDex()+getLvCon()+getLvWis()+getLvInt()+getLvCha();
	}
}
