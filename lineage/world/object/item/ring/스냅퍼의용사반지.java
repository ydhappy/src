package lineage.world.object.item.ring;

import lineage.world.object.instance.ItemInstance;

public class 스냅퍼의용사반지 extends 스냅퍼의반지 {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if(item == null)
			item = new 스냅퍼의용사반지();
		return item;
	}
	
	@Override
	protected int getHit() {
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
	
	@Override
	protected int getHp() {
		switch(getEnLevel()) {
			case 3:
				return getBress()==0 ? 10 : 5;
			case 4:
				return getBress()==0 ? 15 : 10;
			case 5:
				return getBress()==0 ? 20 : 15;
			case 6:
				return getBress()==0 ? 25 : 20;
			case 7:
				return getBress()==0 ? 30 : 25;
			case 8:
				return 30;
			default:
				return getEnLevel()>=8 ? 30 : 0;
		}
	}

	@Override
	protected int getTicHp() {
		return 2;
	}

	@Override
	protected int getAc() {
		switch(getEnLevel()) {
			case 0:
				return getBress()==0 ? 2 : 1;
			case 1:
				return getBress()==0 ? 3 : 2;
			case 2:
				return getBress()==0 ? 4 : 3;
			case 3:
				return getBress()==0 ? 5 : 4;
			case 4:
			case 5:
			case 6:
				return getBress()==0 ? 6 : 5;
			case 7:
			case 8:
				return getBress()==0 ? 7 : 5;
			default:
				return getEnLevel()>=8 ? (getBress()==0 ? 7 : 5) : 0;
		}
	}

}
