package lineage.bean.lineage;

public class Dungeon {
	private int uid;
	private int time;
	private long updateTime;
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		if(time < 0)
			time = 0;
		this.time = time;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
}
