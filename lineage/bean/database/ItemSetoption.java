package lineage.bean.database;

import java.util.ArrayList;
import java.util.List;

public class ItemSetoption {
	private int uid;
	private String name;
	private List<String> itemNameIdList;
	private int count;
	private int add_hp;
	private int add_mp;
	private int add_str;
	private int add_dex;
	private int add_con;
	private int add_int;
	private int add_wis;
	private int add_cha;
	private int add_ac;
	private int add_mr;
	private int add_sp;
	private int add_red;
	private int add_hit;
	private int add_dmg;
	private int add_bow_hit;
	private int add_bow_dmg;
	private double add_exp;
	private int tic_hp;
	private int tic_mp;
	private int polymorph;
	private int windress;
	private int wateress;
	private int fireress;
	private int earthress;
	private int gm;
	private boolean haste;
	private boolean brave;
	private boolean wafer;
	
	public ItemSetoption() {
		itemNameIdList = new ArrayList<String>();
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

	public List<String> getNameIdList() {
		return itemNameIdList;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getAdd_hp() {
		return add_hp;
	}

	public void setAdd_hp(int add_hp) {
		this.add_hp = add_hp;
	}

	public int getAdd_mp() {
		return add_mp;
	}

	public void setAdd_mp(int add_mp) {
		this.add_mp = add_mp;
	}

	public int getAdd_str() {
		return add_str;
	}

	public void setAdd_str(int add_str) {
		this.add_str = add_str;
	}

	public int getAdd_dex() {
		return add_dex;
	}

	public void setAdd_dex(int add_dex) {
		this.add_dex = add_dex;
	}

	public int getAdd_con() {
		return add_con;
	}

	public void setAdd_con(int add_con) {
		this.add_con = add_con;
	}

	public int getAdd_int() {
		return add_int;
	}

	public void setAdd_int(int add_int) {
		this.add_int = add_int;
	}

	public int getAdd_wis() {
		return add_wis;
	}

	public void setAdd_wis(int add_wis) {
		this.add_wis = add_wis;
	}

	public int getAdd_cha() {
		return add_cha;
	}

	public void setAdd_cha(int add_cha) {
		this.add_cha = add_cha;
	}

	public int getAdd_ac() {
		return add_ac;
	}

	public void setAdd_ac(int add_ac) {
		this.add_ac = add_ac;
	}

	public int getAdd_mr() {
		return add_mr;
	}

	public void setAdd_mr(int add_mr) {
		this.add_mr = add_mr;
	}

	public int getAdd_sp() {
		return add_sp;
	}

	public void setAdd_sp(int add_sp) {
		this.add_sp = add_sp;
	}
	
	public int getAddRed() {
		return add_red;
	}
	public void setAddRed(int add_red) {
		this.add_red = add_red;
	}

	public int getAdd_hit() {
		return add_hit;
	}

	public void setAdd_hit(int add_hit) {
		this.add_hit = add_hit;
	}

	public int getAdd_dmg() {
		return add_dmg;
	}

	public void setAdd_dmg(int add_dmg) {
		this.add_dmg = add_dmg;
	}

	public int getAdd_bow_hit() {
		return add_bow_hit;
	}

	public void setAdd_bow_hit(int add_bow_hit) {
		this.add_bow_hit = add_bow_hit;
	}

	public int getAdd_bow_dmg() {
		return add_bow_dmg;
	}

	public void setAdd_bow_dmg(int add_bow_dmg) {
		this.add_bow_dmg = add_bow_dmg;
	}
	
	public double getAdd_exp() {
		return add_exp;
	}

	public void setAdd_exp(double add_exp) {
		this.add_exp = add_exp;
	}

	public int getTic_hp() {
		return tic_hp;
	}

	public void setTic_hp(int tic_hp) {
		this.tic_hp = tic_hp;
	}

	public int getTic_mp() {
		return tic_mp;
	}

	public void setTic_mp(int tic_mp) {
		this.tic_mp = tic_mp;
	}

	public int getPolymorph() {
		return polymorph;
	}

	public void setPolymorph(int polymorph) {
		this.polymorph = polymorph;
	}

	public int getWindress() {
		return windress;
	}

	public void setWindress(int windress) {
		this.windress = windress;
	}

	public int getWateress() {
		return wateress;
	}

	public void setWateress(int wateress) {
		this.wateress = wateress;
	}

	public int getFireress() {
		return fireress;
	}

	public void setFireress(int fireress) {
		this.fireress = fireress;
	}

	public int getEarthress() {
		return earthress;
	}

	public void setEarthress(int earthress) {
		this.earthress = earthress;
	}

	public int getGm() {
		return gm;
	}

	public void setGm(int gm) {
		this.gm = gm;
	}

	public boolean isHaste() {
		return haste;
	}

	public void setHaste(boolean haste) {
		this.haste = haste;
	}

	public boolean isBrave() {
		return brave;
	}

	public void setBrave(boolean brave) {
		this.brave = brave;
	}

	public boolean isWafer() {
		return wafer;
	}

	public void setWafer(boolean wafer) {
		this.wafer = wafer;
	}
}
