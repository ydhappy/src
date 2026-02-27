package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.QuizQuestion;
import lineage.share.System;
import lineage.share.TimeLine;

public class QuizQuestionDatabase
{
	private static List<QuizQuestion> list;

	public static void init(Connection paramConnection)
	{
		TimeLine.start("QuizQuestionDatabase..");
		list = new ArrayList<QuizQuestion>();
		PreparedStatement localPreparedStatement = null;
		ResultSet localResultSet = null;
		try
		{
			localPreparedStatement = paramConnection.prepareStatement("SELECT * FROM quiz_question");
			localResultSet = localPreparedStatement.executeQuery();
			int i = 1;
			while (localResultSet.next())
			{
				QuizQuestion localQuizQuestion = new QuizQuestion();
				localQuizQuestion.setId(i++);
				localQuizQuestion.setQuestion(localResultSet.getString("question"));
				localQuizQuestion.setAnswer(localResultSet.getString("answer"));
				list.add(localQuizQuestion);
			}
		}
		catch (Exception localException)
		{
			System.printf("%s : init(Connection con)\r\n", new Object[] { QuestPresentDatabase.class.toString() });
			System.println(localException);
		}
		finally
		{
			DatabaseConnection.close(localPreparedStatement, localResultSet);
		}
		TimeLine.end();
	}

	public static QuizQuestion find(int paramInt)
	{
		for (QuizQuestion localQuizQuestion : list) {
			if (localQuizQuestion.getId() == paramInt)
				return localQuizQuestion;
		}
		return null;
	}

	public static int getSize()
	{
		return list.size();
	}
}

/* Location:           D:\orim.jar
 * Qualified Name:     lineage.database.QuizQuestionDatabase
 * JD-Core Version:    0.6.0
 */
