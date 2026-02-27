package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import lineage.bean.database.Skill;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.share.TimeLine;

public final class SkillDatabase {

	static private Map<Long, Skill> list;
	
	static public void init(Connection con){
		TimeLine.start("SkillDatabase..");
		
		if(list == null)
			list = new HashMap<Long, Skill>();
		synchronized (list) {
			list.clear();
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				st = con.prepareStatement("SELECT * FROM skill");
				rs = st.executeQuery();
				while(rs.next()){
					Skill s = new Skill();
					s.setUid(rs.getLong(1));
					s.setName(rs.getString(2));
					s.setSkillLevel(rs.getInt(3));
					s.setSkillNumber(rs.getInt(4));
					s.setMpConsume(rs.getInt(5));
					s.setHpConsume(rs.getInt(6));
					s.setItemConsume(rs.getInt(7));
					s.setItemConsumeCount(rs.getInt(8));
					s.setBuffDuration(rs.getInt(9));
					s.setMindmg(rs.getInt(10));
					s.setMaxdmg(rs.getInt(11));
					s.setId(rs.getInt(12));
					s.setCastGfx(rs.getInt(13));
					s.setRange(rs.getInt(14));
					s.setLawfulConsume(rs.getInt(15));
					s.setDelay(rs.getInt("delay"));
					s.setLock(rs.getString("if_lock"));
					s.setProbabilityDice(rs.getInt("probability_dice"));
					s.setProbabilityValue(rs.getInt("probability_value"));
					s.setPassive(rs.getInt("passive")==1);
					
					if(s.getSkillLevel() == 1)
						s.setPrice(100);
					else if(s.getSkillLevel() == 2)
						s.setPrice(400);
					else
						s.setPrice(900);
					if(rs.getString("element").equalsIgnoreCase("none"))
						s.setElement( Lineage.ELEMENT_NONE );
					else if(rs.getString("element").equalsIgnoreCase("wind"))
						s.setElement( Lineage.ELEMENT_WIND );
					else if(rs.getString("element").equalsIgnoreCase("water"))
						s.setElement( Lineage.ELEMENT_WATER );
					else if(rs.getString("element").equalsIgnoreCase("earth"))
						s.setElement( Lineage.ELEMENT_EARTH );
					else if(rs.getString("element").equalsIgnoreCase("fire"))
						s.setElement( Lineage.ELEMENT_FIRE );
					else if(rs.getString("element").equalsIgnoreCase("laser"))
						s.setElement( Lineage.ELEMENT_LASER );
					else if(rs.getString("element").equalsIgnoreCase("poison"))
						s.setElement( Lineage.ELEMENT_POISON );
					
					list.put(s.getUid(), s);
				}
				
			} catch (Exception e) {
				lineage.share.System.printf("%s : init(Connection con)\r\n", SkillDatabase.class.toString());
				lineage.share.System.println(e);
			} finally {
				DatabaseConnection.close(st, rs);
			}
		
			// 스킬 디비 로딩 알리기.
			PluginController.init(SkillDatabase.class, "init", con, list);
		}
		
		TimeLine.end();
	}
	
	static public Map<Long, Skill> getList() {
		synchronized (list) {
			return new HashMap<Long, Skill>(list);
		}
	}
	
	static public Skill find(final long uid) {
		synchronized (list) {
			return list.get(uid);
		}
	}
	
	static public Skill find(final int level, final int number) {
		synchronized (list) {
			for(Skill s : list.values()) {
				if(s.getSkillLevel()==level && s.getSkillNumber()==number)
					return s;
			}
			return null;
		}
	}
	
	static public int getSize(){
		return list.size();
	}
}
