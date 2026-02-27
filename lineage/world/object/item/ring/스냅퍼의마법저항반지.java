package lineage.world.object.item.ring;

import lineage.world.object.instance.ItemInstance;

public class 스냅퍼의마법저항반지 extends 스냅퍼의반지 {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if(item == null)
			item = new 스냅퍼의마법저항반지();
		return item;
	}
	
	@Override
	protected int getHp() {
		switch(getEnLevel()) {
			case 1:
				return 15;
			case 2:
				return 20;
			case 3:
				return getBress()==0 ? 30 : 25;
			case 4:
				return getBress()==0 ? 35 : 30;
			case 5:
				return getBress()==0 ? 40 : 35;
			case 6:
				return getBress()==0 ? 45 : 40;
			case 7:
				return getBress()==0 ? 50 : 45;
			case 8:
				return 50;
			default:
				return getEnLevel()>=8 ? 50 : 0;
		}
	}
	
	@Override
	protected int getMr() {
		switch(getEnLevel()) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				return 7;
			case 6:
				return getBress()==0 ? 8 : 7;
			case 7:
				return getBress()==0 ? 9 : 7;
			case 8:
				return getBress()==0 ? 10 : 7;
			default:
				return getEnLevel()>=8 ? (getBress()==0 ? 10 : 7) : 0;
		}
	}

	@Override
	protected int getAc() {
		switch(getEnLevel()) {
			case 0:
			case 1:
				return getBress()==0 ? 2 : 1;
			case 2:
				return getBress()==0 ? 3 : 2;
			case 3:
				return getBress()==0 ? 4 : 3;
			case 4:
			case 5:
			case 6:
				return getBress()==0 ? 5 : 4;
			case 7:
			case 8:
				return getBress()==0 ? 6 : 4;
			default:
				return getEnLevel()>=8 ? (getBress()==0 ? 6 : 4) : 0;
		}
	}

}
