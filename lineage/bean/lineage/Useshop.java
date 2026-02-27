package lineage.bean.lineage;

import java.util.ArrayList;
import java.util.List;

import lineage.world.object.instance.ItemInstance;

public class Useshop {
	
	private List<ItemInstance> buy;		// 판매 목록
	private List<ItemInstance> sell;	// 구입 목록
	private byte[] msg;		// 광고글
	
	public Useshop(){
		buy = new ArrayList<ItemInstance>();
		sell = new ArrayList<ItemInstance>();
	}
	
	public void close(){
		buy.clear();
		sell.clear();
		msg = null;
	}

	public byte[] getMsg() {
		return msg;
	}

	public void setMsg(byte[] msg) {
		this.msg = msg;
	}

	public List<ItemInstance> getBuy() {
		return buy;
	}

	public List<ItemInstance> getSell() {
		return sell;
	}
	
	public ItemInstance find(ItemInstance target, boolean buy) {
		List<ItemInstance> temp = buy ? this.buy : this.sell;
		for(ItemInstance item : temp) {
			if(item.getItem().getName().equalsIgnoreCase(target.getItem().getName()) && item.getBress()==target.getBress() && item.getEnLevel()==target.getEnLevel())
				return item;
		}
		return null;
	}
}
