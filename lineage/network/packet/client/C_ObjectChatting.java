package lineage.network.packet.client;

import java.util.Collections;
import java.util.List;

import lineage.bean.database.FirstInventory;
import lineage.bean.database.Item;
import lineage.database.ItemDatabase;
import lineage.database.QuizQuestionDatabase;
import lineage.database.ServerMessageDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.controller.QuizQuestionController;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class C_ObjectChatting extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_ObjectChatting(data, length);
		else
			((C_ObjectChatting)bp).clone(data, length);
		return bp;
	}
	
	public C_ObjectChatting(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc){
		// 버그 방지.
		if(pc==null || pc.isWorldDelete())
			return this;
		
		int mode = readC();
		String msg = readS();
		try
		{
			if ((mode == 3) && (QuizQuestionController.getInstance().isQuizStarted()) && (msg.replaceAll(" ", "").trim().indexOf(QuizQuestionDatabase.find(QuizQuestionController.getInstance().getQuizIndex()).getAnswer().replaceAll(" ", "").trim()) > -1))
				try
			{
					QuizQuestionController.getInstance().setQuizStarted(false);
					World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), String.format(ServerMessageDatabase.find(10).getMessage(), new Object[] { pc.getName() })));
					pc.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), pc, 2, ServerMessageDatabase.find(11).getMessage()));
					List<FirstInventory> localList = Lineage.quiz_question_present;
					if (localList != null)
					{
						Collections.shuffle(localList);
						for (FirstInventory localFirstInventory : localList) {
							Item localItem = ItemDatabase.find(localFirstInventory.getName());
							if (localItem != null)
							{
								ItemInstance localItemInstance = ItemDatabase.newInstance(localItem);
								localItemInstance.setCount(localFirstInventory.getCount());
								pc.toGiveItem(null, localItemInstance, localFirstInventory.getCount());
							}
						}
					}
					if (QuizQuestionController.getInstance().getQuizCount() == 0)
						QuizQuestionController.getInstance().StopQuizQuestion();
			}
			catch (Exception localException2)
			{
				lineage.share.System.printf("%s : Quiz Answer\r\n", new Object[] { C_ObjectChatting.class.toString() });
				lineage.share.System.println(localException2);
			}
		}
		catch (Exception localException3)
		{
			lineage.share.System.printf("%s : init(PcInstance pc)\r\n", new Object[] { C_ObjectChatting.class.toString() });
			lineage.share.System.println(localException3);
		}
		if(pc.getGm()>0 || !pc.isTransparent())
			ChattingController.toChatting(pc, msg, mode);
		
		return this;
	}
}
