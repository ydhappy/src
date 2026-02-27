package lineage.world.object.robot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage.bean.lineage.Useshop;
import lineage.database.DatabaseConnection;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ServerBasePacket;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.UserShopController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.magic.ShapeChange;


public class PcRobotShopInstance extends PcRobotInstance {

	// 상점 아이템 목록.
	private Useshop us;
	
	public Useshop getUseShop() {
		return us;
	}
	
	@Override
	public void close() {
		super.close();
	}

	@Override
	public int getGm(){
		return 1;
	}

	@Override
	public void toDamage(Character cha, int dmg, int type, Object...opt){ }

	@Override
	protected void toAiWalk(long time) { }
	
	@Override
	public void toWorldOut() {
		super.toWorldOut();

		if(us != null) {
			for(ItemInstance item : us.getBuy())
				ItemDatabase.setPool(item);
			for(ItemInstance item : us.getSell())
				ItemDatabase.setPool(item);
			UserShopController.setPool(us);
			us = null;
		}
	}
	
	@Override
	public void toWorldJoin() {
		super.toWorldJoin();

		//
		us = UserShopController.getPool();
		//
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getLineage();
			st = con.prepareStatement("SELECT * FROM _robot_shop WHERE name=? ORDER BY uid ASC");
			st.setString(1, getName());
			rs = st.executeQuery();
			while(rs.next()) {
				//
				ItemInstance item = ItemDatabase.newInstance(ItemDatabase.find(rs.getString("item")));
				if(item == null)
					continue;
				item.setDefinite(true);
				item.setEnLevel(rs.getInt("en"));
				item.setBress(rs.getInt("bress"));
				getInventory().append(item, false);
				//
				if(us.getMsg()==null || us.getMsg().length==0) {
					ServerBasePacket sbp = (ServerBasePacket)ServerBasePacket.clone(BasePacketPooling.getPool(ServerBasePacket.class), null);
					sbp.writeS( rs.getString("ment") );
					us.setMsg(sbp.getByte());
				}
				//
				if(rs.getString("buy").equalsIgnoreCase("true")) {
					item.setUsershopBuyPrice(rs.getInt("price"));
					item.setUsershopBuyCount(rs.getInt("count"));
					us.getBuy().add(item);
				}
				if(rs.getString("sell").equalsIgnoreCase("true")) {
					item.setUsershopSellPrice(Math.round(rs.getInt("price") * 0.75));
					item.setUsershopSellCount(rs.getInt("count"));
					us.getSell().add(item);
				}
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : toWorldJoin()\r\n", PcRobotShopInstance.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	}
	
	@Override
	public void toTeleport(final int x, final int y, final int map, final boolean effect) {
		//
		setHomeX(x);
		setHomeY(y);
		setHomeMap(map);
		// 착용중인 아이템 해제.
		if(getInventory().getSlot(8) != null)
			getInventory().getSlot(8).toClick(this, null);
		// 변신상태 해제.
		BuffController.remove(this, ShapeChange.class);
		//
		setGfxMode(70);
		//
		super.toTeleport(x, y, map, effect);
	}
	
}
