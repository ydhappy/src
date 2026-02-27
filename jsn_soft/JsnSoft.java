package jsn_soft;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.database.DatabaseConnection;
import lineage.share.TimeLine;
import lineage.world.object.instance.PcInstance;

public final class JsnSoft {
	
	static public String command;
	static public String Server_Name = "";
	
	// 여기에 이제 시작할게여
	// 수업의 목적.
	// .드랍 [아이템이름] 명령어 입력시 나오는 드랍창에서
	// 몬스터의 이름을 클릭시
	// 액션값을 받아 몬스터의 몹드랍을 출력시킨다.
	// 그러기위한 .드랍시에 들어가는 리스트.
	// 여러명이서 사용할 수 있도록 Map을 이용할 예정이다.
	public static Map<Long, List<String>> Monster_list = new HashMap<Long, List<String>>();
	

	
	
	/**
	 * 리니지에 사용되는 변수 초기화 함수.
	 * by_ Jsn_Soft
	 * 2021-02-14
	 */
	static public void init(){
		TimeLine.start("JsnSoft..");
		
		try {
			BufferedReader lnrr = new BufferedReader( new FileReader("JsnSoft.conf"));
			String line;
			while ( (line = lnrr.readLine()) != null){
				if(line.startsWith("#"))
					continue;
				
				int pos = line.indexOf("=");
				if(pos > 0){
					String key = line.substring(0, pos).trim();
					String value = line.substring(pos+1, line.length()).trim();
					
					if(key.equalsIgnoreCase("command"))
						command = String.valueOf(value);
					else if (key.equalsIgnoreCase("Server_Name"))
						Server_Name = String.valueOf(value);
				}
			}
			lnrr.close();
		} catch (Exception e) {
			lineage.share.System.printf("%s : init()\r\n", JsnSoft.class.toString());
			lineage.share.System.println(e);
		}
	
		TimeLine.end();
	}
	
	public static boolean isClanAccount(PcInstance pc) {
		Connection con = null; // 디비 mysql 새로운 연결 할때 그 연결
		//커넥션 빼고 st랑 rs 정의좀
		PreparedStatement st = null; // 디비 테이블 << 테이블 속에서 찾아야하니까
		ResultSet rs = null; // 테이블속 clanID 같은 그거있졍??ㅋㅋㅋ 아아
		try {
			int num = 0;
			con = DatabaseConnection.getLineage();
			// WHERE account_uid=? 필터 누르고pc.getAccountUid() 입력
			st = con.prepareStatement("SELECT * FROM characters WHERE account_uid=?");
			st.setLong(1, pc.getAccountUid());
			rs = st.executeQuery(); // 입력되서 나오는 목록
			if (rs.next()) { // 목록 1번부터 쭉내려가면서
				num += rs.getInt("clanID"); //클랜아이디만 검색해서 더하 어떻게 보면 포문비슷하네요 디비 포문이네요넹 요거 저도 잘몰라용ㅋㅋㅋㅋ구조만 이해한정도에용 네 ㅋ
			}
			if (num > 0)
				return false;
		} catch (Exception e) {
			lineage.share.System.printf("%s : isClanAccount(PcInstance pc)\r\n",
					JsnSoft.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(con, st, rs);
		}
	
		
		return true;
	}
}
