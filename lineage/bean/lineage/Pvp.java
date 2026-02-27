package lineage.bean.lineage;

public class Pvp {
	private int uid;
	private long objectId;
	private String name;
	private boolean kill;
	private boolean dead;
	private long target_objectId;
	private String target_name;
	private long pvp_date;

	public void close() {
		uid = 0;
		objectId = target_objectId = pvp_date = 0;
		name = target_name = null;
		kill = dead = false;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public long getObjectId() {
		return objectId;
	}
	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isKill() {
		return kill;
	}
	public void setKill(boolean kill) {
		this.kill = kill;
	}
	public boolean isDead() {
		return dead;
	}
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	public long getTargetObjectId() {
		return target_objectId;
	}
	public void setTargetObjectId(long target_objectId) {
		this.target_objectId = target_objectId;
	}
	public String getTargetName() {
		return target_name;
	}
	public void setTargetName(String target_name) {
		this.target_name = target_name;
	}
	public long getPvpDate() {
		return pvp_date;
	}
	public void setPvpDate(long pvp_date) {
		this.pvp_date = pvp_date;
	}
	
}
