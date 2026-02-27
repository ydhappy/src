package lineage.bean.database;

public class WebAuction extends Warehouse {
	// web_auction
	private int webPriceS;
	private int webPriceD;
	private long webEndDate;
	private boolean webIsSell;
	// web_auction_log
	private int auctionUid;
	private int biddingPrice;
	// 공통
	private long insertDate;
	private String note;
	
	@Override
	public void clear() {
		super.clear();
		insertDate = webEndDate = webPriceD = webPriceS = auctionUid = biddingPrice = 0;
		note = null;
		webIsSell = false;
	}
	public int getWebPriceS() {
		return webPriceS;
	}
	public void setWebPriceS(int webPriceS) {
		this.webPriceS = webPriceS;
	}
	public int getWebPriceD() {
		return webPriceD;
	}
	public void setWebPriceD(int webPriceD) {
		this.webPriceD = webPriceD;
	}
	public long getWebEndDate() {
		return webEndDate;
	}
	public void setWebEndDate(long webEndDate) {
		this.webEndDate = webEndDate;
	}
	public boolean isWebIsSell() {
		return webIsSell;
	}
	public void setWebIsSell(boolean webIsSell) {
		this.webIsSell = webIsSell;
	}
	public int getAuctionUid() {
		return auctionUid;
	}
	public void setAuctionUid(int auctionUid) {
		this.auctionUid = auctionUid;
	}
	public int getBiddingPrice() {
		return biddingPrice;
	}
	public void setBiddingPrice(int biddingPrice) {
		this.biddingPrice = biddingPrice;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public long getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(long insertDate) {
		this.insertDate = insertDate;
	}
	
}
