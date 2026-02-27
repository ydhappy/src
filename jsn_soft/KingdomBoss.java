package jsn_soft;

import lineage.bean.database.Monster;

public class KingdomBoss {
	
	private String name;
	private int[] time;
	private boolean overlap;
	private int kingdomUid;;
	private Monster mon;
	
	public Monster getMon() {
		return mon;
	}
	public void setMon(Monster mon) {
		this.mon = mon;
	}
	public int getKingdomUid() {
		return kingdomUid;
	}
	public void setKingdomUid(int kingdomUid) {
		this.kingdomUid = kingdomUid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int[] getTime() {
		return time;
	}
	public void setTime(int[] time) {
		this.time = time;
	}
	public boolean isOverlap() {
		return overlap;
	}
	public void setOverlap(boolean overlap) {
		this.overlap = overlap;
	}

}
