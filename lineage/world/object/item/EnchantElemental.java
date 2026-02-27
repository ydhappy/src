package lineage.world.object.item;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_Message;
import lineage.util.Util;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;

public class EnchantElemental extends ItemInstance {
	
	/**
	 * 인첸트를 진행할지 여부.
	 * @param item
	 * @return
	 */
	protected boolean isEnchant(ItemInstance item){
		if(item == null)
			return false;
		// 봉인 확인.
		if(item.getBress() < 0)
			return false;
		// 다른속성 인첸은 바로 성공시키기.
		switch(getItem().getNameIdNumber()) {
			case 5725:
				if(item.getEnWind() == 0)
					return true;
				break;
			case 5726:
				if(item.getEnEarth() == 0)
					return true;
				break;
			case 5727:
				if(item.getEnWater() == 0)
					return true;
				break;
			case 5728:
				if(item.getEnFire() == 0)
					return true;
				break;
		}
		//
		int enElement = item.getEnWind()+item.getEnEarth()+item.getEnWater()+item.getEnFire();
		// 5단계 이상 무시.
		if(enElement >= 5)
			return false;
		// 인첸 레벨별 최대 단계 확인.
	/*	if(item.getEnLevel()<=9 && enElement>=3)
			return false;
		if(item.getEnLevel()<=10 && enElement>=4)
			return false;*/
		
		return true;
	}

	/**
	 * 인첸트를 처리할 메서드.
	 */
	protected int toEnchant(Character cha, ItemInstance item){
		// 기본 인첸 가능여부 판단.
		if( !isEnchant(item) ){
			// \f1 아무것도 일어나지 않았습니다.
			cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 79));
			return -127;
		}
		
		return toEnchantNew(cha, item);
	}

	/**
	 * 인첸트를 처리할 메서드.
	 */
	private int toEnchantNew(Character cha, ItemInstance item) {
		//
		int enElement = item.getEnWind()+item.getEnEarth()+item.getEnWater()+item.getEnFire();
		int rate = 30;
		if(enElement == 2)
			rate = 20;
		if(enElement == 3)
			rate = 15;
		if(enElement == 4)
			rate = 10;
		else
			rate = 5;
		//
		if(Util.random(0, 100) >= rate) {
			// 인챈트: %0에 힘이 스며들지 못했습니다.
			cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 1411, item.toStringDB()));
			return 0;
		} else {
			if(enElement <= 5)
				// 인챈트: %0에 영롱한 대자연의 힘이 스며듭니다.
				cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 1410, item.toStringDB()));
			else
				// 인챈트: %0에 찬란한 대자연의 힘이 스며듭니다.
				cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 3296, item.toStringDB()));
			//
			switch(getItem().getNameIdNumber()) {
				case 5725:
					item.setEnWind(item.getEnWind() + 1);
					item.setEnEarth(0);
					item.setEnWater(0);
					item.setEnFire(0);
					break;
				case 5726:
					item.setEnWind(0);
					item.setEnEarth(item.getEnEarth() + 1);
					item.setEnWater(0);
					item.setEnFire(0);
					break;
				case 5727:
					item.setEnWind(0);
					item.setEnEarth(0);
					item.setEnWater(item.getEnWater() + 1);
					item.setEnFire(0);
					break;
				case 5728:
					item.setEnWind(0);
					item.setEnEarth(0);
					item.setEnWater(0);
					item.setEnFire(item.getEnFire() + 1);
					break;
			}
		}
		return item.getEnWind()+item.getEnEarth()+item.getEnWater()+item.getEnFire();
	}
}
