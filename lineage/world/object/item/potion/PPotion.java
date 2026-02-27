package lineage.world.object.item.potion;


import lineage.bean.lineage.StatDice;
import lineage.database.CharactersDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_CharacterHp;
import lineage.network.packet.server.S_CharacterMp;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.controller.BuffController;
import lineage.world.controller.CharacterController;
import lineage.world.controller.ChattingController;

import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class PPotion extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new PPotion();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		PcInstance pc = (PcInstance)cha;
		
		if(pc.stat_clear == true) {
			ChattingController.toChatting(pc, "\\fY스텟초기화 중에는 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
		}
		for(int i=0 ; i<=Lineage.SLOT_NONE ; ++i){
			if(pc.getInventory().getSlot(i)!=null){
				ChattingController.toChatting(pc, "\\fY착용중인 아이템이 있어 사용할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
			return;
			}
		}

		BuffController.removeAll(cha);


		// 아이템 수량 갱신
		cha.getInventory().count(this, getCount()-1, true);
		
		int i2;
		int i3;
		switch(cha.getClassType()){
		case 0:
			i2 = Lineage.royal_hp;
			i3 = Lineage.royal_mp;
			break;
		case 1:
			i2 = Lineage.knight_hp;
			i3 = Lineage.knight_mp;
			break;
		case 2:
			i2 = Lineage.elf_hp;
			i3 = Lineage.elf_mp;
			break;
		case 3:
			i2 = Lineage.wizard_hp;
			i3 = Lineage.wizard_mp;
			break;
		case 4:
			i2 = Lineage.darkelf_hp;
			i3 = Lineage.darkelf_mp;
			break;
		case 5:
			i2 = Lineage.dragonknight_hp;
			i3 = Lineage.dragonknight_mp;
			break;
		default:
			i2 = Lineage.blackwizard_hp;
			i3 = Lineage.blackwizard_mp;
			break;
		}

		cha.setMaxHp(i2);
		cha.setMaxMp(i3);
		int i4=0;
		int i5=0;
		int i6=1;
		for(int i7=1; i7 < cha.getLevel(); i7++){
			i4 += CharacterController.toStatusUP(cha, true);
			i5 += CharacterController.toStatusUP(cha, false);
			i6++;
		}
		int i7 = getMaxHp() + i4;
		int i8 = getMaxMp() + i5;
		switch(cha.getClassType()){
			case 0:
			if (i7 >= Lineage.royal_max_hp)
				i7 = Lineage.royal_max_hp;
			if (i8 < Lineage.royal_max_mp)
				break;
			i8 = Lineage.royal_max_mp;
			break;
		case 1:
			if (i7 >= Lineage.knight_max_hp)
				i7 = Lineage.knight_max_hp;
			if (i8 < Lineage.knight_max_mp)
				break;
			i8 = Lineage.knight_max_mp;
			break;
		case 2:
			if (i7 >= Lineage.elf_max_hp)
				i7 = Lineage.elf_max_hp;
			if (i8 < Lineage.elf_max_mp)
				break;
			i8 = Lineage.elf_max_mp;
			break;
		case 3:
			if (i7 >= Lineage.wizard_max_hp)
				i7 = Lineage.wizard_max_hp;
			if (i8 < Lineage.wizard_max_mp)
				break;
			i8 = Lineage.wizard_max_mp;
			break;
		case 4:
			if (i7 >= Lineage.darkelf_max_hp)
				i7 = Lineage.darkelf_max_hp;
			if (i8 < Lineage.darkelf_max_mp)
				break;
			i8 = Lineage.darkelf_max_mp;
			break;
		case 5:
			if (i7 >= Lineage.dragonknight_max_hp)
				i7 = Lineage.dragonknight_max_hp;
			if (i8 < Lineage.dragonknight_max_mp)
				break;
			i8 = Lineage.dragonknight_max_mp;
			break;
		case 6:
			if (i7 >= Lineage.blackwizard_max_hp)
				i7 = Lineage.blackwizard_max_hp;
			if (i8 < Lineage.blackwizard_max_mp)
				break;
			i8 = Lineage.blackwizard_max_mp;
		}

		cha.setMaxHp(i7);
		cha.setMaxMp(i8);

		cha.toSender(S_CharacterHp.clone(BasePacketPooling.getPool(S_CharacterHp.class), cha));
		cha.toSender(S_CharacterMp.clone(BasePacketPooling.getPool(S_CharacterMp.class), cha));
		
		try
		{
			CharactersDatabase.saveCharacterStatus((PcInstance)cha);
		}
		catch (Exception localException)
		{
		}
		

		cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, getItem().getEffect()), true);
	}

}
