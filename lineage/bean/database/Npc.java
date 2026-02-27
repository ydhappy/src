package lineage.bean.database;

import java.util.ArrayList;
import java.util.List;

public class Npc {
	private int NpcUid;
	private String Name;
	private String NameId;
	private String Type;
	private int NameIdNumber;
	private boolean Ai;
	private int Gfx;
	private int GfxMode;
	private int Hp;
	private int Lawful;
	private int Light;
	private int AreaAtk;
	private int arrowGfx;
	private int msgAtkTime;
	private int msgDieTime;
	private int msgSpawnTime;
	private int msgEscapeTime;
	private int msgWalkTime;
	private List<Shop> shop_list = new ArrayList<Shop>();
	private List<int[]> spawn_list = new ArrayList<int[]>();
	private List<Integer> craft_list = new ArrayList<Integer>();
	private List<String> msgAtk = new ArrayList<String>();
	private List<String> msgDie = new ArrayList<String>();
	private List<String> msgSpawn = new ArrayList<String>();
	private List<String> msgEscape = new ArrayList<String>();
	private List<String> msgWalk = new ArrayList<String>();

	public int getNpcUid() {
		return NpcUid;
	}

	public void setNpcUid(int id) {
		NpcUid = id;
	}
	
	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public String getNameId() {
		return NameId;
	}

	public void setNameId(String nameId) {
		NameId = nameId;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public int getNameIdNumber() {
		return NameIdNumber;
	}

	public void setNameIdNumber(int nameIdNumber) {
		NameIdNumber = nameIdNumber;
	}

	public boolean isAi() {
		return Ai;
	}

	public void setAi(boolean ai) {
		Ai = ai;
	}

	public int getGfx() {
		return Gfx;
	}

	public void setGfx(int gfx) {
		Gfx = gfx;
	}

	public int getGfxMode() {
		return GfxMode;
	}

	public void setGfxMode(int gfxMode) {
		GfxMode = gfxMode;
	}

	public int getHp() {
		return Hp;
	}

	public void setHp(int hp) {
		Hp = hp;
	}

	public int getLawful() {
		return Lawful;
	}

	public void setLawful(int lawful) {
		Lawful = lawful;
	}

	public int getLight() {
		return Light;
	}

	public void setLight(int light) {
		Light = light;
	}

	public int getAtkRange() {
		return AreaAtk;
	}

	public void setAreaAtk(int areaAtk) {
		AreaAtk = areaAtk;
	}

	public int getArrowGfx() {
		return arrowGfx;
	}

	public void setArrowGfx(int arrowGfx) {
		this.arrowGfx = arrowGfx;
	}

	public List<Shop> getShop_list() {
		return shop_list;
	}

	public List<int[]> getSpawnList() {
		return spawn_list;
	}
	
	public int getBuySize(){
		int size = 0;
		for( Shop s : shop_list ){
			if(s.isItemBuy())
				++size;
		}
		return size;
	}

	public Shop findShop(long uid){
		for( Shop s : shop_list ){
			if(s.getUid() == uid)
				return s;
		}
		return null;
	}
	
	public Shop findShopItem(int uid) {
		for( Shop s : shop_list ) {
			if(s.getUid() == uid)
				return s;
		}
		return null;
	}
	public Shop findShopItemId(final String name){
	      for( Shop s : shop_list ){
	         if(s.getItemName().equalsIgnoreCase(name))
	            return s;
	      }
	      return null;
	   }

	public Shop findShopItemId(String name, int bress, int enLevel){
		for( Shop s : shop_list ){
			if(s.getItemName().equalsIgnoreCase(name) && s.getItemBress()==bress && s.getItemEnLevel()==enLevel)
				return s;
		}
		return null;
	}
	public Shop findShopItemId(String name, int bress){
		for( Shop s : shop_list ){
			if(s.getItemName().equalsIgnoreCase(name) && s.getItemBress()==bress)
				return s;
		}
		return null;
	}

	public List<Integer> getCraftList() {
		return craft_list;
	}

	public int getMsgAtkTime() {
		return msgAtkTime;
	}

	public void setMsgAtkTime(int msgAtkTime) {
		this.msgAtkTime = msgAtkTime;
	}

	public int getMsgDieTime() {
		return msgDieTime;
	}

	public void setMsgDieTime(int msgDieTime) {
		this.msgDieTime = msgDieTime;
	}

	public int getMsgSpawnTime() {
		return msgSpawnTime;
	}

	public void setMsgSpawnTime(int msgSpawnTime) {
		this.msgSpawnTime = msgSpawnTime;
	}

	public int getMsgEscapeTime() {
		return msgEscapeTime;
	}

	public void setMsgEscapeTime(int msgEscapeTime) {
		this.msgEscapeTime = msgEscapeTime;
	}

	public int getMsgWalkTime() {
		return msgWalkTime;
	}

	public void setMsgWalkTime(int msgWalkTime) {
		this.msgWalkTime = msgWalkTime;
	}

	public List<String> getMsgAtk() {
		return msgAtk;
	}

	public List<String> getMsgDie() {
		return msgDie;
	}

	public List<String> getMsgSpawn() {
		return msgSpawn;
	}

	public List<String> getMsgEscape() {
		return msgEscape;
	}

	public List<String> getMsgWalk() {
		return msgWalk;
	}
}
