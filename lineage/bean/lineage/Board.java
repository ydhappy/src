package lineage.bean.lineage;

import lineage.util.Util;

public class Board {
	private long uid;
	private String type;
	private String accountId;
	private String name;
	private String subject;
	private String memo;
	private long days;
	
	private int adena;
	private int money;
	private int step;
	private int sellaccountuid;
	private String buyAccountId;
	private int buyAccountUid;
	private int buyObjId;
	private String buyName;
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getSellAccountUid() {
		return sellaccountuid;
	}
	public void setSellAccountId(int sellaccountuid) {
		this.sellaccountuid = sellaccountuid;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getDays() {
		return days;
	}
	public void setDays(long days) {
		this.days = days;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String toStringDays(){
		int y = Util.getYear(days)-100;
		int m = Util.getMonth(days);
		int d = Util.getDate(days);
		
		StringBuffer sb = new StringBuffer();
		if(y<10)
			sb.append("0");
		sb.append(y);
		if(m<10)
			sb.append("0");
		sb.append(m);
		if(d<10)
			sb.append("0");
		sb.append(d);
		
		return sb.toString();
	}

	public int getAdena() {
		return adena;
	}

	public void setAdena(int adena) {
		this.adena = adena;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getBuyAccountId() {
		return buyAccountId;
	}

	public void setBuyAccountId(String buyAccountId) {
		this.buyAccountId = buyAccountId;
	}

	public int getBuyObjId() {
		return buyObjId;
	}
	

	public void setBuyObjId(int buyObjId) {
		this.buyObjId = buyObjId;
	}
	
	public int getbuyAccountUid() {
		return buyAccountUid;
	}
	

	public void setbuyAccountUid(int buyAccountUid) {
		this.buyAccountUid = buyAccountUid;
	}

	public String getBuyName() {
		return buyName;
	}

	public void setBuyName(String buyName) {
		this.buyName = buyName;
	}
}
