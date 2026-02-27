package lineage.bean.database;

public class Warehouse {
	private int uid;
	private int accountUid;
	private int clanId;
	private int invId;
	private int petId;
	private int letterId;
	private String name;
	private String 구분1;
	private String 구분2;
	private int type;
	private int gfxid;
	private long count;
	private int quantity;
	private int en;
	private boolean definite;
	private int bress;
	private int durability;
	private int time;
	private String option;
	private int enEarth;
	private int enWater;
	private int enFire;
	private int enWind;
	private int add_min_dmg;
	private int add_max_dmg;
	private int add_str;
	private int add_dex;
	private int add_con;
	private int add_int;
	private int add_wiz;
	private int add_cha;
	private int add_mana;
	private int add_hp;
	private int add_manastell;
	private int add_hpstell;
	private int one;
	private int two;
	private int three;
	private int four;
	private long soul_cha;
	
	public void clear() {
		enWind = enFire = enWater = enEarth = uid = accountUid = clanId = invId = petId = letterId = type = gfxid = quantity = en = bress = durability = time = 0;
		add_min_dmg = add_max_dmg = add_str = add_dex = add_con = add_int = add_wiz = add_cha = add_mana = add_hp = add_manastell = add_hpstell= one = two = three = four = 0;
		count = soul_cha = 0;
		option = name = 구분1 = 구분2 = null;
		definite = false;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getAccountUid() {
		return accountUid;
	}
	public void setAccountUid(int accountUid) {
		this.accountUid = accountUid;
	}
	public int getClanId() {
		return clanId;
	}
	public void setClanId(int clanId) {
		this.clanId = clanId;
	}
	public int getInvId() {
		return invId;
	}
	public void setInvId(int invId) {
		this.invId = invId;
	}
	public int getPetId() {
		return petId;
	}
	public void setPetId(int petId) {
		this.petId = petId;
	}
	public int getLetterId() {
		return letterId;
	}
	public void setLetterId(int letterId) {
		this.letterId = letterId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String get구분1() {
		return 구분1;
	}
	public void set구분1(String 구분1) {
		this.구분1 = 구분1;
	}
	public String get구분2() {
		return 구분2;
	}
	public void set구분2(String 구분2) {
		this.구분2 = 구분2;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getGfxid() {
		return gfxid;
	}
	public void setGfxid(int gfxid) {
		this.gfxid = gfxid;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		if(quantity < 0)
			quantity = 0;
		this.quantity = quantity;
	}
	public int getEn() {
		return en;
	}
	public void setEn(int en) {
		this.en = en;
	}
	public boolean isDefinite() {
		return definite;
	}
	public void setDefinite(boolean definite) {
		this.definite = definite;
	}
	public int getBress() {
		return bress;
	}
	public void setBress(int bress) {
		this.bress = bress;
	}
	public int getDurability() {
		return durability;
	}
	public void setDurability(int durability) {
		this.durability = durability;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public int getEnEarth() {
		return enEarth;
	}
	public void setEnEarth(int enEarth) {
		this.enEarth = enEarth;
	}
	public int getEnWater() {
		return enWater;
	}
	public void setEnWater(int enWater) {
		this.enWater = enWater;
	}
	public int getEnFire() {
		return enFire;
	}
	public void setEnFire(int enFire) {
		this.enFire = enFire;
	}
	public int getEnWind() {
		return enWind;
	}
	public void setEnWind(int enWind) {
		this.enWind = enWind;
	}

	public int getAdd_Min_Dmg(){
		return add_min_dmg;
	}
	public void setAdd_Min_Dmg(int add_min_dmg){
		this.add_min_dmg = add_min_dmg;
	}
	public int getAdd_Max_Dmg(){
		return add_max_dmg;
	}
	public void setAdd_Max_Dmg(int add_max_dmg){
		this.add_max_dmg = add_max_dmg;
	}
	public int getAdd_Str(){
		return add_str;
	}
	public void setAdd_Str(int add_str){
		this.add_str = add_str;
	}
	public int getAdd_Dex(){
		return add_dex;
	}
	public void setAdd_Dex(int add_dex){
		this.add_dex = add_dex;
	}
	public int getAdd_Con(){
		return add_con;
	}
	public void setAdd_Con(int add_con){
		this.add_con = add_con;
	}
	public int getAdd_Int(){
		return add_int;
	}
	public void setAdd_Int(int add_int){
		this.add_int = add_int;
	}
	public int getAdd_Wiz(){
		return add_wiz;
	}
	public void setAdd_Wiz(int add_wiz){
		this.add_wiz = add_wiz;
	}
	public int getAdd_Cha(){
		return add_cha;
	}
	public void setAdd_Cha(int add_cha){
		this.add_cha = add_cha;
	}

	public int getAdd_Mana(){
		return add_mana;
	}
	public void setAdd_Mana(int add_mana){
		this.add_mana = add_mana;
	}
	public int getAdd_Hp(){
		return add_hp;
	}
	public void setAdd_Hp(int add_hp){
		this.add_hp = add_hp;
	}
	public int getAdd_Manastell(){
		return add_manastell;
	}
	public void setAdd_Manastell(int add_manastell){
		this.add_manastell = add_manastell;
	}
	public int getAdd_Hpstell(){
		return add_hpstell;
	}
	public void setAdd_Hpstell(int add_hpstell){
		this.add_hpstell = add_hpstell;
	}
	public int getOne(){
		return one;
	}
	public void setOne(int one){
		this.one = one;
	}
	public int getTwo(){
		return two;
	}
	public void setTwo(int two){
		this.two = two;
	}
	public int getThree(){
		return three;
	}
	public void setThree(int three){
		this.three = three;
	}
	public int getFour(){
		return four;
	}
	public void setFour(int four){
		this.four = four;
	}
	public long getSoul_Cha(){
		return soul_cha;
	}
	public void setSoul_Cha(int i){
		this.soul_cha = i;
	}
}
