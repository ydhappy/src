package lineage.bean.database;

public class LifeLostItem {
	private String item;
	private double nomalChance;
	private double bressChance;
	private double bressContinueChance;
	private String itemName;
	private int bress;
	private int en;
	private long minCount;
	private long maxCount;
	
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public double getNomalChance() {
		return nomalChance;
	}
	public void setNomalChance(double nomalChance) {
		this.nomalChance = nomalChance;
	}
	public double getBressChance() {
		return bressChance;
	}
	public void setBressChance(double bressChance) {
		this.bressChance = bressChance;
	}
	public double getBressContinueChance() {
		return bressContinueChance;
	}
	public void setBressContinueChance(double bressContinueChance) {
		this.bressContinueChance = bressContinueChance;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public int getBress() {
		return bress;
	}
	public void setBress(int bress) {
		this.bress = bress;
	}
	public int getEn() {
		return en;
	}
	public void setEn(int en) {
		this.en = en;
	}
	public long getMinCount() {
		return minCount;
	}
	public void setMinCount(long minCount) {
		this.minCount = minCount;
	}
	public long getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(long maxCount) {
		this.maxCount = maxCount;
	}
}
