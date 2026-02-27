package lineage.bean.lineage;

public class Ship {
	// 메모용
	private String note;
	// 티켓 체크할 좌표 정보.
	private int checkX;
	private int checkY;
	private int checkMap;
	private int gotoX;
	private int gotoY;
	private int gotoMap;
	// 배표 정보
	private int ticketNameId;
	// 탑승 시간
	private int[] inTime;
	// 하차할 좌표 정보.
	private int[] outX;
	private int[] outY;
	private int outMap;
	//
	public Ship(String note, int checkX, int checkY, int checkMap, int gotoX, int gotoY, int gotoMap, int ticketNameId, int[] inTime, int[] outX, int[] outY, int outMap) {
		this.note = note;
		this.checkX = checkX;
		this.checkY = checkY;
		this.checkMap = checkMap;
		this.gotoX = gotoX;
		this.gotoY = gotoY;
		this.gotoMap = gotoMap;
		this.ticketNameId = ticketNameId;
		this.inTime = inTime;
		this.outX = outX;
		this.outY = outY;
		this.outMap = outMap;
	}
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getCheckX() {
		return checkX;
	}
	public void setCheckX(int checkX) {
		this.checkX = checkX;
	}
	public int getCheckY() {
		return checkY;
	}
	public void setCheckY(int checkY) {
		this.checkY = checkY;
	}
	public int getCheckMap() {
		return checkMap;
	}
	public void setCheckMap(int checkMap) {
		this.checkMap = checkMap;
	}
	public int getGotoX() {
		return gotoX;
	}
	public void setGotoX(int gotoX) {
		this.gotoX = gotoX;
	}
	public int getGotoY() {
		return gotoY;
	}
	public void setGotoY(int gotoY) {
		this.gotoY = gotoY;
	}
	public int getGotoMap() {
		return gotoMap;
	}
	public void setGotoMap(int gotoMap) {
		this.gotoMap = gotoMap;
	}
	public int getTicketNameId() {
		return ticketNameId;
	}
	public void setTicketNameId(int ticketNameId) {
		this.ticketNameId = ticketNameId;
	}
	public int[] getInTime() {
		return inTime;
	}
	public void setInTime(int[] inTime) {
		this.inTime = inTime;
	}

	public int[] getOutX() {
		return outX;
	}

	public void setOutX(int[] outX) {
		this.outX = outX;
	}

	public int[] getOutY() {
		return outY;
	}

	public void setOutY(int[] outY) {
		this.outY = outY;
	}

	public int getOutMap() {
		return outMap;
	}

	public void setOutMap(int outMap) {
		this.outMap = outMap;
	}
	
}
