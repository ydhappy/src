package lineage.world.object.item.ring;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.share.Lineage;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemArmorInstance;
import lineage.world.object.instance.ItemInstance;

public class 스냅퍼의반지 extends ItemArmorInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if(item == null)
			item = new 스냅퍼의반지();
		return item;
	}
	
	@Override
	protected void toEnchantOption(Character cha, boolean sendPacket) {
		//
		int new_mp = getMp();
		int new_ac = getAc();
		int new_hp = getHp();
		int new_addweight = getAddWeight();
		int new_dmg = getDmg();
		int new_stun = getStun();
		int new_pvpDmg = getPvpDmg();
		int new_ticHp = getTicHp();
		int new_ticMp = getTicMp();
		int new_sp = getSp();
		int new_hit = getHit();
		int new_mr = getMr();
		if(isEquipped() && !cha.isWorldDelete()){
			// 이전에 세팅값 빼기.
			cha.setDynamicHp(cha.getDynamicHp()-getDynamicHp());
			cha.setDynamicMp(cha.getDynamicMp()-getDynamicMp());
			cha.setDynamicAc(cha.getDynamicAc()-getDynamicAc());
			cha.setDynamicAddWeight(cha.getDynamicAddWeight()-getDynamicAddWeight());
			cha.setDynamicAddDmg(cha.getDynamicAddDmg()-getDynamicAddDmg());
			cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow()-getDynamicAddDmgBow());
			cha.setDynamicStunDefense(cha.getDynamicStunDefense()-getDynamicStunDefense());
			cha.setDynamicAddPvpDmg(cha.getDynamicAddPvpDmg()-getDynamicAddPvpDmg());
			cha.setDynamicTicHp(cha.getDynamicTicHp()-getDynamicTicHp());
			cha.setDynamicTicMp(cha.getDynamicTicMp()-getDynamicTicMp());
			cha.setDynamicSp(cha.getDynamicSp()-getDynamicSp());
			cha.setDynamicAddHit(cha.getDynamicAddHit()-getDynamicAddHit());
			cha.setDynamicAddHitBow(cha.getDynamicAddHitBow()-getDynamicAddHitBow());
			cha.setDynamicMr(cha.getDynamicMr()-getDynamicMr());
			// 인첸에따른 새로운값 적용.
			cha.setDynamicHp(cha.getDynamicHp()+new_hp);
			cha.setDynamicMp(cha.getDynamicMp()+new_mp);
			cha.setDynamicAc(cha.getDynamicAc()+new_ac);
			cha.setDynamicAddWeight(cha.getDynamicAddWeight()+new_addweight);
			cha.setDynamicAddDmg(cha.getDynamicAddDmg()+new_dmg);
			cha.setDynamicAddDmgBow(cha.getDynamicAddDmgBow()+new_dmg);
			cha.setDynamicStunDefense(cha.getDynamicStunDefense()+new_stun);
			cha.setDynamicAddPvpDmg(cha.getDynamicAddPvpDmg()+new_pvpDmg);
			cha.setDynamicTicHp(cha.getDynamicTicHp()+new_ticHp);
			cha.setDynamicTicMp(cha.getDynamicTicMp()+new_ticMp);
			cha.setDynamicSp(cha.getDynamicSp()+new_sp);
			cha.setDynamicAddHit(cha.getDynamicAddHit()+new_hit);
			cha.setDynamicAddHitBow(cha.getDynamicAddHitBow()+new_hit);
			cha.setDynamicMr(cha.getDynamicMr()+new_mr);
		}
		setDynamicHp(new_hp);
		setDynamicMp(new_mp);
		setDynamicAc(new_ac);
		setDynamicAddWeight(new_addweight);
		setDynamicAddDmg(new_dmg);
		setDynamicAddDmgBow(new_dmg);
		setDynamicStunDefense(new_stun);
		setDynamicAddPvpDmg(new_pvpDmg);
		setDynamicTicHp(new_ticHp);
		setDynamicTicMp(new_ticMp);
		setDynamicSp(new_sp);
		setDynamicAddHit(new_hit);
		setDynamicAddHitBow(new_hit);
		setDynamicMr(new_mr);
		if(sendPacket) {
			if(Lineage.server_version <= 144)
				cha.toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), this));
			else
				cha.toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), this));
		}
		//
	}
	
	protected int getMr() {
		return 0;
	}
	
	protected int getHit() {
		return 0;
	}
	
	protected int getSp() {
		return 0;
	}
	
	protected int getTicHp() {
		return 0;
	}
	
	protected int getTicMp() {
		return 0;
	}
	
	protected int getMp() {
		return 0;
	}
	
	protected int getPvpDmg() {
		switch(getEnLevel()) {
			case 7:
				return 1;
			case 8:
				return 2;
			default:
				return getEnLevel()>=8 ? 2 : 0;
		}
	}
	
	protected int getStun() {
		switch(getEnLevel()) {
			case 6:
				return 5;
			case 7:
				return 7;
			case 8:
				return 9;
			default:
				return getEnLevel()>=8 ? 9 : 0;
		}
	}
	
	protected int getDmg() {
		switch(getEnLevel()) {
			case 4:
				return getBress()==0 ? 1 : 0;
			case 5:
				return getBress()==0 ? 2 : 1;
			case 6:
				return getBress()==0 ? 3 : 2;
			case 7:
				return getBress()==0 ? 4 : 3;
			case 8:
				return getBress()==0 ? 5 : 4;
			default:
				return getEnLevel()>=8 ? (getBress()==0 ? 5 : 4) : 0;
		}
	}
	
	protected int getAddWeight() {
		return 360;
	}
	
	protected int getHp() {
		return 0;
	}
	
	protected int getAc() {
		return 0;
	}

}
