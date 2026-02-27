package lineage.bean.lineage;

public class Book {
	private String location;
	private int x;
	private int y;
	private int map;
	private String type;			// robot
	private int clanUid;
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getMap() {
		return map;
	}
	public void setMap(int map) {
		this.map = map;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getClanUid() {
		return clanUid;
	}
	public void setClanUid(int clanUid) {
		this.clanUid = clanUid;
	}
}
