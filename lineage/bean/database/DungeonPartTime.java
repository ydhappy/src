package lineage.bean.database;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DungeonPartTime {

	private int uid;
	private String name;
	private int time;
	private List<String> timeSeek;
	private List<Integer> list;
	private long updateTime;
	private String timeReset;
	private String messageOut;	// 시간다됫을때 멘트

	public DungeonPartTime() {
		list = new ArrayList<Integer>();
		timeSeek = new ArrayList<String>();
	}

	public void close() {
		updateTime = uid = time = 0;
		messageOut = timeReset = name = null;
		list.clear();
		timeSeek.clear();
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		if(time < 0)
			time = 0;
		this.time = time;
	}

	public List<Integer> getList() {
		return list;
	}

	public void setList(List<Integer> list) {
		this.list = list;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public List<String> getTimeSeek() {
		return timeSeek;
	}

	public void setTimeSeek(String timeSeek) {
		StringTokenizer st = new StringTokenizer(timeSeek, "|");
		while(st.hasMoreTokens())
			appendTimeSeek( st.nextToken().trim() );
	}

	public void appendTimeSeek(String timeSeek) {
		if(timeSeek.length() > 0)
			this.timeSeek.add( timeSeek );
	}

	public String getTimeReset() {
		return timeReset;
	}

	public void setTimeReset(String timeReset) {
		this.timeReset = timeReset;
	}

	public String getMessageOut() {
		return messageOut;
	}

	public void setMessageOut(String messageOut) {
		this.messageOut = messageOut;
	}

}
