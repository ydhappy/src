package lineage.world.object.item.ring;

import lineage.world.object.instance.ItemInstance;

public class 스냅퍼의체력반지 extends 스냅퍼의반지 {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if(item == null)
			item = new 스냅퍼의체력반지();
		return item;
	}
	
	@Override
	protected int getHp() {
		switch(getEnLevel()) {
			case 0:
				return 5;
			case 1:
				return 15;
			case 2:
				return 25;
			case 3:
				return getBress()==0 ? 40 : 35;
			case 4:
				return getBress()==0 ? 45 : 40;
			case 5:
				return getBress()==0 ? 50 : 45;
			case 6:
				return getBress()==0 ? 55 : 50;
			case 7:
				return getBress()==0 ? 60 : 55;
			case 8:
				return getBress()==0 ? 65 : 60;
			default:
				return getEnLevel()>=8 ? (getBress()==0 ? 70 : 65) : 0;
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
