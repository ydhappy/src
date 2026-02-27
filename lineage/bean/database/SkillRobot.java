package lineage.bean.database;


public class SkillRobot extends Skill {
	private String type;

	public SkillRobot(Skill s) {
		setUid(s.getUid());
		setName(s.getName());
		setSkillLevel(s.getSkillLevel());
		setSkillNumber(s.getSkillNumber());
		setMpConsume(s.getMpConsume());
		setHpConsume(s.getHpConsume());
		setItemConsume(s.getItemConsume());
		setItemConsumeCount(s.getItemConsumeCount());
		setBuffDuration(s.getBuffDuration());
		setMindmg(s.getMindmg());
		setMaxdmg(s.getMaxdmg());
		setId(s.getId());
		setCastGfx(s.getCastGfx());
		setRange(s.getRange());
		setLawfulConsume(s.getLawfulConsume());
		setDelay(s.getDelay());
		setLock(s.getLock());
		setPrice(s.getPrice());
		setElement(s.getElement());
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
