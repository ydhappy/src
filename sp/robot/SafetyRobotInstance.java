package sp.robot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Exp;
import lineage.bean.database.Item;
import lineage.bean.lineage.Book;
import lineage.bean.lineage.Buff;
import lineage.database.DatabaseConnection;
import lineage.database.ExpDatabase;
import lineage.database.ItemDatabase;
import lineage.database.MagicdollListDatabase;
import lineage.database.ServerDatabase;
import lineage.database.SpriteFrameDatabase;
import lineage.share.Lineage;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.BookController;
import lineage.world.controller.BuffController;
import lineage.world.controller.CharacterController;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.item.potion.BraveryPotion;
import lineage.world.object.item.potion.ElvenWafer;
import lineage.world.object.item.potion.HastePotion;
import lineage.world.object.magic.Haste;
import lineage.world.object.magic.item.Bravery;
import lineage.world.object.magic.item.Wafer;
import lineage.world.object.robot.PcRobotInstance;

public class SafetyRobotInstance extends PcRobotInstance {

	public static void init(List<PcRobotInstance> list_pc) {
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM _robot WHERE type='safety'");
			rs = st.executeQuery();
			while(rs.next()) {
				String name = rs.getString("name");
				//
				PcRobotInstance temp = null;
				for(PcRobotInstance pc : list_pc) {
					if(pc.getName().equalsIgnoreCase(name)) {
						temp = pc;
						break;
					}
				}
				if(temp != null)
					list_pc.remove(temp);
				//
				temp = new SafetyRobotInstance();
				temp.setObjectId( ServerDatabase.nextEtcObjId() );
				temp.setName(rs.getString("name"));
				temp.setTypeRobot(rs.getString("type"));
				temp.setAc(rs.getInt("ac"));
				temp.setStr(rs.getInt("str"));
				temp.setDex(rs.getInt("dex"));
				temp.setCon(rs.getInt("con"));
				temp.setWis(rs.getInt("wis"));
				temp.setInt(rs.getInt("inter"));
				temp.setCha(rs.getInt("cha"));
				temp.setX(rs.getInt("locX"));
				temp.setY(rs.getInt("locY"));
				temp.setMap(rs.getInt("locMAP"));
				temp.setHeading(Util.random(0, 7));
				temp.setTitle(rs.getString("title"));
				temp.setLawful(rs.getInt("lawful"));
				temp.setClanId(rs.getInt("clanID"));
				if(rs.getString("class").equalsIgnoreCase("군주")) {
					temp.setClassType(0x00);
					temp.setClassGfx(rs.getString("sex").equalsIgnoreCase("남자") ? Lineage.royal_male_gfx : Lineage.royal_female_gfx);
					temp.setGfx(temp.getClassGfx());
					temp.setMaxHp(Lineage.royal_hp);
					temp.setMaxMp(Lineage.royal_mp);
				}else if(rs.getString("class").equalsIgnoreCase("기사")) {
					temp.setClassType(0x01);
					temp.setClassGfx( rs.getString("sex").equalsIgnoreCase("남자") ? Lineage.knight_male_gfx : Lineage.knight_female_gfx );
					temp.setGfx(temp.getClassGfx());
					temp.setMaxHp(Lineage.knight_hp);
					temp.setMaxMp(Lineage.knight_mp);
				}else if(rs.getString("class").equalsIgnoreCase("요정")) {
					temp.setClassType(0x02);
					temp.setClassGfx( rs.getString("sex").equalsIgnoreCase("남자") ? Lineage.elf_male_gfx : Lineage.elf_female_gfx );
					temp.setGfx(temp.getClassGfx());
					temp.setMaxHp(Lineage.elf_hp);
					temp.setMaxMp(Lineage.elf_mp);
				}else if(rs.getString("class").equalsIgnoreCase("마법사")) {
					temp.setClassType(0x03);
					temp.setClassGfx( rs.getString("sex").equalsIgnoreCase("남자") ? Lineage.wizard_male_gfx : Lineage.wizard_female_gfx );
					temp.setGfx(temp.getClassGfx());
					temp.setMaxHp(Lineage.wizard_hp);
					temp.setMaxMp(Lineage.wizard_mp);
				}else if(rs.getString("class").equalsIgnoreCase("다크엘프")) {
					temp.setClassType(0x04);
					temp.setClassGfx( rs.getString("sex").equalsIgnoreCase("남자") ? Lineage.darkelf_male_gfx : Lineage.darkelf_female_gfx );
					temp.setGfx(temp.getClassGfx());
					temp.setMaxHp(Lineage.darkelf_hp);
					temp.setMaxMp(Lineage.darkelf_mp);
				}else if(rs.getString("class").equalsIgnoreCase("용기사")) {
					temp.setClassType(0x05);
					temp.setClassGfx( rs.getString("sex").equalsIgnoreCase("남자") ? Lineage.dragonknight_male_gfx : Lineage.dragonknight_female_gfx );
					temp.setGfx(temp.getClassGfx());
					temp.setMaxHp(Lineage.dragonknight_hp);
					temp.setMaxMp(Lineage.dragonknight_mp);
				}else if(rs.getString("class").equalsIgnoreCase("환술사")) {
					temp.setClassType(0x06);
					temp.setClassGfx( rs.getString("sex").equalsIgnoreCase("남자") ? Lineage.blackwizard_male_gfx : Lineage.blackwizard_female_gfx );
					temp.setGfx(temp.getClassGfx());
					temp.setMaxHp(Lineage.blackwizard_hp);
					temp.setMaxMp(Lineage.blackwizard_mp);
				}
				if(rs.getInt("gfx") >= 0)
					temp.setGfx( rs.getInt("gfx") );
				//
				Exp e = ExpDatabase.find(rs.getInt("level"));
				int hp = 0;
				int mp = 0;
				for(int i=temp.getLevel()+1 ; i<e.getLevel() ; ++i) {
					hp += CharacterController.toStatusUP(temp, true);
					mp += CharacterController.toStatusUP(temp, false);
				}
				temp.setMaxHp(temp.getMaxHp() + hp);
				temp.setMaxMp(temp.getMaxMp() + mp);
				temp.setNowHp(temp.getMaxHp());
				temp.setNowMp(temp.getMaxMp());
				temp.setLevel(e.getLevel());
				temp.setExp(e.getBonus()-e.getExp());
				temp.setWeapon(ItemDatabase.find(rs.getString("item_weapon_name")));
				temp.setWeaponEn(rs.getInt("item_weapon_en"));
				temp.setDynamicMr(rs.getInt("mr"));
				temp.setDynamicSp(rs.getInt("sp"));
				temp.setFood(Lineage.MAX_FOOD);
				temp.setAttribute(Util.random(1, 4));
				temp.setMdl( MagicdollListDatabase.find(rs.getString("magicdoll")) );

				//
				list_pc.add(temp);
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(List<PcRobotInstance> list_pc)\r\n", SafetyRobotInstance.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}
	
	private Book book;
	private int location_range = 1;		// 휴식로봇이 기억된 좌표에서 얼마만큼에 거리를 여유로 둘건지 설정.

	@Override
	public void close(){
		super.close();
		
		book = null;
	}
	
	@Override
	public void toWorldJoin() {
		super.toWorldJoin();
		
		// safety 기억목록만 남기기.
		List<Book> list = BookController.find(this);
		List<Book> remove = new ArrayList<Book>();
		for(Book b : list) {
			if(b.getType().equalsIgnoreCase("safety"))
				continue;
			remove.add(b);
		}
		for(Book b : remove)
			list.remove(b);
		remove.clear();
	}
	
	@Override
	public void toAi(long time) {
		synchronized (sync_ai) {
			// 일반 적인 인공지능 패턴
			// 랜덤워킹, 죽은거체크, 시체유지, 도망가기, 스폰멘트, 죽을때멘트, 공격할때멘트
			switch(getAiStatus()){
				case -1:
					break;
				case 0:
					try { toAiWalk(time); } catch(Exception e) {}
					break;
				case 1:
					toAiAttack(time);
					break;
				case 2:
					toAiDead(time);
					break;
				case 3:
					toAiCorpse(time);
					break;
				case 4:
					toAiSpawn(time);
					break;
				case 5:
					toAiEscape(time);
					break;
				case 6:
					toAiPickup(time);
					break;
				default:
					ai_time = SpriteFrameDatabase.find(gfx, gfxMode+0);
					break;
			}
		}

		// 
		if(!isWorldDelete() && !isDead()) {
			// 무기 착용.
			if(getInventory().getSlot(8) == null)
				if(getInventory().find(weapon, weaponEn)!=null)
					getInventory().find(weapon, weaponEn).toClick(this, null);
		}
	}
	
	@Override
	protected void toAiWalk(long time) {
		ai_time = SpriteFrameDatabase.find(gfx, gfxMode+0);
		
		if(World.isSafetyZone(getX(), getY(), getMap())) {
			
			if(toBuffPotion()){
				return;
			}
			
			// 마을
			if(book == null) {
				// 새로운 위치 잡기.
				List<Book> list = BookController.find(this);
				// 등록된 사냥터 없으면 무시.
				if(list.size() == 0)
					return;
				//
				book = list.get( Util.random(0, list.size()-1) );
			} else {
				// 기억된 북 위치범위내에 있는지 확인.
				if(Util.isDistance(getX(), getY(), getMap(), book.getX(), book.getY(), book.getMap(), Util.random(location_range, Lineage.SEARCH_LOCATIONRANGE/2))) {
					// 범위 안
					// 휴식
					// 일정확률 방향 전환.
					if(Util.random(0, 100) == 0)
						setHeading(Util.random(0, 7));
					if(Util.random(0, 100) == 0)
						book = null;
				} else {
					// 범위 밖
					if(toMoving(null, book.getX(), book.getY(), getHeading(), true)) {
						// 이동가능
						toMoving(book.getX(), book.getY(), getHeading());
					} else {
						// 이동불가능
						// 마을로 귀환.
						//	: 귀환하면서 book도 초기화되므로 다른 위치를 선택하도록 유도.
//						toTeleport(book.getX(), book.getY(), book.getMap(), true);
						gotoHome(true);
					}
				}
			}
		} else {
			// 필드
			gotoHome(true);
		}
	}

	@Override
	protected void gotoHome(boolean isCheck) {
		book = null;
		location_range = Util.random(1, Lineage.SEARCH_LOCATIONRANGE/2);
		toTeleport(getHomeX(), getHomeY(), getHomeMap(), isDead()==false);
	}
	
	private boolean toBuffPotion() {
		//
		Buff b = BuffController.find(this);
		if(b == null)
			return false;
		// 촐기 복용.
		if(b.find(Haste.class) == null) {
			Item i = ItemDatabase.find("초록 물약");
			getInventory().append(ItemDatabase.newInstance(i), false);
			
			ItemInstance item = getInventory().find(HastePotion.class);

			if(item!=null && item.isClick(this)) {
				item.toClick(this, null);
				return true;
			}
		}
		// 용기 복용.
		if(getClassType()==0x01 && b.find(Bravery.class) == null) {
			Item i = ItemDatabase.find("용기의 물약");
			getInventory().append(ItemDatabase.newInstance(i), false);
			ItemInstance item = getInventory().find(BraveryPotion.class);
			if(item!=null && item.isClick(this)) {
				item.toClick(this, null);
				return true;
			}
		}
		// 엘븐와퍼 복용.
		if(getClassType()==0x02 && b.find(Wafer.class) == null) {
			Item i = ItemDatabase.find("엘븐 와퍼");
			getInventory().append(ItemDatabase.newInstance(i), false);
			ItemInstance item = getInventory().find(ElvenWafer.class);
			if(item!=null && item.isClick(this)) {
				item.toClick(this, null);
				return true;
			}
		}
		return false;
	}
}
