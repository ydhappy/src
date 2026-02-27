package lineage.bean.lineage;

public class QuestToday {
	private long uid;
	private String name;
	private String type;
	private int levelMin;
	private int levelMax;
	private String targetName;
	private String targetItem;
	private int targetCount;
	private String rewardItem;
	private int rewardCount;
	private int rewardBless;
	private int rewardEnchant;
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getLevelMin() {
		return levelMin;
	}
	public void setLevelMin(int levelMin) {
		this.levelMin = levelMin;
	}
	public int getLevelMax() {
		return levelMax;
	}
	public void setLevelMax(int levelMax) {
		this.levelMax = levelMax;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public String getTargetItem() {
		return targetItem;
	}
	public void setTargetItem(String targetItem) {
		this.targetItem = targetItem;
	}
	public int getTargetCount() {
		return targetCount;
	}
	public void setTargetCount(int targetCount) {
		this.targetCount = targetCount;
	}
	public String getRewardItem() {
		return rewardItem;
	}
	public void setRewardItem(String rewardItem) {
		this.rewardItem = rewardItem;
	}
	public int getRewardCount() {
		return rewardCount;
	}
	public void setRewardCount(int rewardCount) {
		this.rewardCount = rewardCount;
	}
	public int getRewardBless() {
		return rewardBless;
	}
	public void setRewardBless(String rewardBless) {
		if(rewardBless.equalsIgnoreCase("축"))
			this.rewardBless = 0;
		if(rewardBless.equalsIgnoreCase("보통"))
			this.rewardBless = 1;
		if(rewardBless.equalsIgnoreCase("저주"))
			this.rewardBless = 2;
	}
	public int getRewardEnchant() {
		return rewardEnchant;
	}
	public void setRewardEnchant(int rewardEnchant) {
		this.rewardEnchant = rewardEnchant;
	}
	@Override
	public String toString() {
		return String.format("QuestToday:uid(%d),name(%s)", getUid(), getName());
	}
}
