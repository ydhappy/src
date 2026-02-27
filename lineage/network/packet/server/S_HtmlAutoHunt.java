package lineage.network.packet.server;

import java.util.List;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class S_HtmlAutoHunt extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, PcInstance pc, object o, String html) {
		if (bp == null)
			bp = new S_HtmlAutoHunt(pc, o, html);
		else
			((S_HtmlAutoHunt) bp).clone(pc, o, html);
		return bp;
	}

	static synchronized public BasePacket clone(BasePacket bp, PcInstance pc, object o, String html, String request,
			List<?> list) {
		if (bp == null)
			bp = new S_HtmlAutoHunt(pc, o, html, request, list);
		else
			((S_HtmlAutoHunt) bp).clone(pc, o, html, request, list);
		return bp;
	}

	public S_HtmlAutoHunt(PcInstance pc, object o, String html) {
		clone(pc, o, html);
	}

	public S_HtmlAutoHunt(PcInstance pc, object o, String html, String request, List<?> list) {
		clone(pc, o, html, request, list);
	}

	public void clone(PcInstance pc, object o, String html) {
		clear();
		try {
			writeC(Opcodes.S_OPCODE_SHOWHTML);
			writeD(o.getObjectId());
			writeS(html);
			writeH(2);
			long hour = pc.getAutoTime() / 60 / 60;
			long min = pc.getAutoTime() / 60 % 60;
			writeS(String.valueOf("  " + (hour > 0 ? hour + "시간 " : " ") + min + "분"));

			String statusString = "대기중";
			switch (pc.getAutoStatus()) {
				case 1:
					statusString = "사냥터 이동/탐색";
					break;
				case 2:
					statusString = "전투 진행중";
					break;
				case 4:
					statusString = "마을 정비(상점)";
					break;
			}
			writeS(statusString);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void clone(PcInstance pc, object o, String html, String request, List<?> list) {
		// System.out.println(list.size());
		return;
	}
}
