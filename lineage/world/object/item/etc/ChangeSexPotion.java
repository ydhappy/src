package lineage.world.object.item.etc;

import java.sql.Connection;
import java.sql.PreparedStatement;

import lineage.database.CharactersDatabase;
import lineage.database.DatabaseConnection;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Disconnect;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.network.packet.server.S_ObjectGfx;
import lineage.share.Lineage;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class ChangeSexPotion extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new ChangeSexPotion();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		if( !isClick(cha) )
			return;

		// 패킷처리
		if(getItem().getEffect() > 0)
			cha.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), cha, getItem().getEffect()), true);
		// 성별 전환.
		int gfx = 0;
		int sex = 0;
		switch(cha.getClassType()){
			case 0x00:
				gfx = cha.getClassGfx()==Lineage.royal_male_gfx ? Lineage.royal_female_gfx : Lineage.royal_male_gfx;
				sex = cha.getClassGfx()==Lineage.royal_male_gfx ? 1 : 0;
				break;
			case 0x01:
				gfx = cha.getClassGfx()==Lineage.knight_male_gfx ? Lineage.knight_female_gfx : Lineage.knight_male_gfx;
				sex = cha.getClassGfx()==Lineage.knight_male_gfx ? 1 : 0;
				break;
			case 0x02:
				gfx = cha.getClassGfx()==Lineage.elf_male_gfx ? Lineage.elf_female_gfx : Lineage.elf_male_gfx;
				sex = cha.getClassGfx()==Lineage.elf_male_gfx ? 1 : 0;
				break;
			case 0x03:
				gfx = cha.getClassGfx()==Lineage.wizard_male_gfx ? Lineage.wizard_female_gfx : Lineage.wizard_male_gfx;
				sex = cha.getClassGfx()==Lineage.wizard_male_gfx ? 1 : 0;
				break;
			case 0x04:
				gfx = cha.getClassGfx()==Lineage.darkelf_male_gfx ? Lineage.darkelf_female_gfx : Lineage.darkelf_male_gfx;
				sex = cha.getClassGfx()==Lineage.darkelf_male_gfx ? 1 : 0;
				break;
			case 0x06:
				gfx = cha.getClassGfx()==Lineage.blackwizard_male_gfx ? Lineage.blackwizard_female_gfx : Lineage.blackwizard_male_gfx;
				sex = cha.getClassGfx()==Lineage.blackwizard_male_gfx ? 1 : 0;
				break;
			case 0x05:
				gfx = cha.getClassGfx()==Lineage.dragonknight_male_gfx ? Lineage.dragonknight_female_gfx : Lineage.dragonknight_male_gfx;
				sex = cha.getClassGfx()==Lineage.dragonknight_male_gfx ? 1 : 0;
				break;
		}
		// 변신상태일땐 변경하면 안되기때문에 삼항식 적용.
		cha.setGfx(cha.getGfx()==cha.getClassGfx() ? gfx : cha.getGfx());
		// 클레스 고유값은 변경.
		cha.setClassGfx(gfx);
		cha.setClassSex(sex);
		// 패킷으로 변경된거 표현.
		cha.toSender(S_ObjectGfx.clone(BasePacketPooling.getPool(S_ObjectGfx.class), cha), true);
		// 디비 변경.
		CharactersDatabase.updateCharacterSex(cha.getName(), sex);
		

		// 아이템 수량 갱신
		cha.getInventory().count(this, getCount()-1, true);
	}

}
