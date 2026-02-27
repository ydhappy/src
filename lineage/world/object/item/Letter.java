package lineage.world.object.item;

import java.sql.Connection;

import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_LetterRead;
import lineage.share.Lineage;
import lineage.world.controller.LetterController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class Letter extends ItemInstance {

	private long uid;
	private String from;
	private String to;
	private String subject;
	private String memo;
	private long date;
	private String dateString;
	private boolean open;		// 편지지를 한번이라도 연람했는지 여부.
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Letter();
		return item;
	}
	
	@Override
	public void close(){
		super.close();
		
		date = uid = 0;
		from = to = subject = memo = dateString = null;
		open = false;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
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

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	@Override
	public long getLetterUid() {
		return uid;
	}

	@Override
	public void setLetterUid(long uid) {
		this.uid = uid;
	}
	
	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	@Override
	public void toWorldJoin(Connection con, PcInstance pc){
		super.toWorldJoin(con, pc);
		LetterController.read(con, this);
	}
	
//	@Override
//	public void toClick(Character cha, ClientBasePacket cbp){
//		if(getItem().getInvGfx()==464){
//			// 새편지 작성
//			cbp.readH();
//			String to = cbp.readS();
//			String subject = cbp.readSS();
//			String memo = cbp.readSS();
//
//			// 수량 하향
//			cha.getInventory().count(this, getCount()-1, true);
//			// 편지작성한거 처리.
//			LetterController.toLetter(cha.getName(), to, subject, memo, 0, Lineage.server_version<300);
//		}else{
//			// 읽거나 않읽은 편지
//			if(getItem().getInvGfx()==465)
//				//읽은편지지로 변경.
//				item = ItemDatabase.find("편지지 - 읽은 편지");
//			// 편지창 뛰우기.
//			cha.toSender(S_LetterRead.clone(BasePacketPooling.getPool(S_LetterRead.class), this));
//		}
//	}
	
	@Override
	   public void toClick(Character cha, ClientBasePacket cbp){
	      if(getItem().getNameIdNumber() == 1075) {
	         if(getItem().getInvGfx()==464){
	            // 새편지 작성
	            cbp.readH();
	            String to = cbp.readS();
	            String subject = cbp.readSS();
	            String memo = cbp.readSS();
	   
	            // 수량 하향
	            cha.getInventory().count(this, getCount()-1, true);
	            // 편지작성한거 처리.
	            LetterController.toLetter(cha.getName(), to, subject, memo, 0);
	         }else{
	            // 읽거나 않읽은 편지
	            if(getItem().getInvGfx()==465)
	               //읽은편지지로 변경.
	               item = ItemDatabase.find("편지지 - 읽은 편지");
	            // 편지창 뛰우기.
	            cha.toSender(S_LetterRead.clone(BasePacketPooling.getPool(S_LetterRead.class), this));
	         }
	      }else if(getItem().getNameIdNumber() == 1146) {
	    	  
	         if(getItem().getInvGfx()==464){
	            // 새편지 작성
	            cbp.readH();
	            String to = cbp.readS();      // 혈맹 이름
	            String subject = cbp.readSS();   // 제목
	            String memo = cbp.readSS();      // 내용
	   
	            // 수량 하향
	            cha.getInventory().count(this, getCount()-1, true);
	            // 편지작성한거 처리.
	            LetterController.toPledgeLetter(cha.getName(), to, subject, memo, true);
	         }else{
	            super.toClick(cha, cbp);
	         }
	      }
	   }
}
