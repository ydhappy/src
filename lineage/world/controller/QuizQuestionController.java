package lineage.world.controller;

import java.util.Timer;
import java.util.TimerTask;

import lineage.database.QuizQuestionDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.share.System;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.object.instance.PcInstance;

public class QuizQuestionController
{
	private boolean quizStarted = false;
	private int quizIndex = 0;
	private int quizCount = 0;
	private static final Timer timer = new Timer();
	private static QuizQuestionController _instance;
	private QuizQuestion quizQuestion;
	private boolean isQuiz = false;

	public boolean isQuizStarted()
	{
		return this.quizStarted;
	}

	public void setQuizStarted(boolean paramBoolean)
	{
		this.quizStarted = paramBoolean;
	}

	public int getQuizIndex()
	{
		return this.quizIndex;
	}

	public void setQuizIndex(int paramInt)
	{
		this.quizIndex = paramInt;
	}

	public int getQuizCount()
	{
		return this.quizCount;
	}

	public void setQuizCount(int paramInt)
	{
		this.quizCount = paramInt;
	}

	public static synchronized QuizQuestionController getInstance()
	{
		if (_instance == null)
			_instance = new QuizQuestionController();
		return _instance;
	}

	public void StartQuizQuestion(int paramInt1, int paramInt2) {
		if (!this.isQuiz) {
			this.quizCount = paramInt1;
			this.quizQuestion = new QuizQuestion();
			timer.scheduleAtFixedRate(this.quizQuestion, 3000L, 60000 * paramInt2);
			this.isQuiz = true;
		}
	}

	public void StopQuizQuestion()
	{
		if (this.isQuiz)
		{
			System.printf("%s : StopQuizQuestion()\r\n", new Object[] { QuizQuestionController.class.toString() });
			this.quizQuestion.cancel();
			this.isQuiz = false;
		}
	}

	private class QuizQuestion extends TimerTask
	{
		private QuizQuestion() {
		}
	
		public void run()
		{
			try
			{
				if (QuizQuestionController.this.quizCount == 0)
				{
					QuizQuestionController.this.StopQuizQuestion();
					return;
				}
				int i = Util.random(1, QuizQuestionDatabase.getSize());
				for (PcInstance localPcInstance : World.getPcList()) {
					if (localPcInstance.getGm() > 0)
						ChattingController.toChatting(localPcInstance, "\\fY퀴즈정답 : " + QuizQuestionDatabase.find(i).getAnswer(), 20);
				}
				Thread.sleep(1000L);
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "[공지] 잠시후 채팅 퀴즈가 진행됩니다."));
				Thread.sleep(1000L);
				ChattingController.setGlobal(false);
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "[공지] 3초후 문제 출제 빠른순서 1등만 추첨합니다."));
				Thread.sleep(5000L);
				setQuizIndex(i);
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), QuizQuestionDatabase.find(i).getQuestion()));
				Thread.sleep(1000L);
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "[공지] 정답을 생각 하실시간 3초 행운을... "));
				Thread.sleep(1000L);
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "[공지] 답변을 쓰실 카운트 3 "));
				Thread.sleep(1000L);
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "[공지] 답변을 쓰실 카운트 2 "));
				Thread.sleep(1000L);
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "[공지] 답변을 쓰실 카운트 1 "));
				Thread.sleep(1000L);
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class), "[공지] 행운의 정답자는? 고고싱~ "));
				Thread.sleep(500L);
				ChattingController.setGlobal(true);
				setQuizStarted(true);
				StopQuizQuestion();
			}
			catch (Exception localException) {
				setQuizStarted(false);
			}
		}
	}
}

/* Location:           D:\orim.jar
 * Qualified Name:     lineage.world.controller.QuizQuestionController
 * JD-Core Version:    0.6.0
 */
