package lineage.world.object.item.scroll;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_CharacterStat;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class ScrollResurrection extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item) {
		if (item == null)
			item = new ScrollResurrection();
		return item;
	}

	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		object o = cha.findInsideList(cbp.readD());
		if (o != null) {
			o.toRevival(cha);
			if (this.getBress() == 0) {
				PcInstance pc = (PcInstance) o;
				o.setTempHp(o.getMaxHp());
			
				if (pc.getLostExp() > 0.0D && !pc.deadtrue) {
			
					pc.setExp(pc.getExp() + pc.getLostExp());
					pc.setLostExp(0.0D);
					pc.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), pc));
					ChattingController.toChatting(pc, "불꽃의 힘이 파도처럼 치솟아 오릅니다. 경험치가 복구 되었습니다.", 20);
				
				}

				else
					ChattingController.toChatting(pc, "더이상 복구할경험치가 없습니다.", 20);
			}
		}

		cha.getInventory().count(this, getCount() - 1, true);

		// 아이템 수량 갱신
		
	}
}
