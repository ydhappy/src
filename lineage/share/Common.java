package lineage.share;

import java.util.ArrayList;
import java.util.List;


public final class Common {

	// 김진원, 정종훈
	static public String requestor = "";
	
	// 모든 쓰레드들의 휴식시간 ms
	static public final int THREAD_SLEEP	= 10;
	static public final int TIMER_SLEEP		= 1000;
	
	// 버퍼 사이즈
	static public final int BUFSIZE = 1024;
	// 문자열 사이즈
	static public final int STRSIZE = 20;
	
	// 문자열 인코딩 방식 지정
	// KSC5601, EUC-KR, UTF-8
	static public final String CHARSET = "EUC-KR";
	
	// 시스템 구동방식이 console 모드인지 gui 모드인지 확인하는 변수.
	//static public boolean system_config_console = true;
	static public boolean system_config_console = false;
	
	// 월드 접속시 표현될 메세지 정보.
	static public boolean SERVER_MESSAGE;				// 서버 메세지 표현할지 여부.
	static public List<String> SERVER_MESSAGE_LIST;		// 서버 메세지
	static public int SERVER_MESSAGE_TIME;				// 서버 메세지 주기적으로 표현할 시간값. 밀리세컨드 단위.
	// 서버다운할때 표현될 메세지.
	static public String SHUTDOWN_MESSAGE_FORMAT_HOUR;
	static public String SHUTDOWN_MESSAGE_FORMAT_MIN;
	static public String SHUTDOWN_MESSAGE_FORMAT_SEC;
	//
	static public List<String> BUFF_ROBOT_MENT;
	
	static public String OS_NAME;
	
	static public String CREDITOR;
	
	// 명령어 구분값.명령어변경.성환
	static public String COMMAND_TOKEN = ".";
	
	static public String HELPER_LINE = "**************************************************";
	
	static public void init(){
		TimeLine.start("Common..");
		
		OS_NAME = java.lang.System.getProperty("os.name");
		SERVER_MESSAGE = true;
		SERVER_MESSAGE_TIME = 1000 * 600;
		SERVER_MESSAGE_LIST = new ArrayList<String>();
		SHUTDOWN_MESSAGE_FORMAT_HOUR ="리니지가 %d시간 후 중단됩니다. 안전한 곳에서 종료하여 주시기 바랍니다.";
		SHUTDOWN_MESSAGE_FORMAT_MIN = "리니지가 %d분 후 중단됩니다. 안전한 곳에서 종료하여 주시기 바랍니다.";
		SHUTDOWN_MESSAGE_FORMAT_SEC = "리니지가 %d초 후 중단됩니다. 안전한 곳에서 종료하여 주시기 바랍니다.";
		BUFF_ROBOT_MENT = new ArrayList<String>();
		BUFF_ROBOT_MENT.add(String.format("버프 단돈 %d냥~!!", Lineage.robot_auto_buff_aden));
		BUFF_ROBOT_MENT.add("행복한 버프 받고 열렙하세요~");
		BUFF_ROBOT_MENT.add("자동버프중 앞에서 어택해주세요.");
		CREDITOR = "리니지";
//		if(GuiMain.CLIENT_MAX == 10) {
//			try {
//				SERVER_MESSAGE_LIST.add( new String(new byte[]{0xffffffeb, 0xffffffb2, 0xffffff84, 0xffffffea, 0xffffffb7, 0xffffffb8, 0x20, 0xffffffeb, 0xffffffb0, 0xffffff8f, 0x20, 0xffffffea, 0xffffffb0, 0xffffff9c, 0xffffffec, 0xffffff84, 0xffffffa0, 0xffffffec, 0xffffff82, 0xffffffac, 0xffffffed, 0xffffff95, 0xffffffad, 0xffffffec, 0xffffff9d, 0xffffff80, 0x20, 0xffffffeb, 0xffffffa9, 0xffffff94, 0xffffffec, 0xffffff8b, 0xffffffa0, 0xffffffec, 0xffffffa0, 0xffffff80, 0xffffffeb, 0xffffffa1, 0xffffff9c, 0x20, 0xffffffeb, 0xffffffb6, 0xffffff80, 0xffffffed, 0xffffff83, 0xffffff81, 0x20, 0xffffffeb, 0xffffff93, 0xffffff9c, 0xffffffeb, 0xffffffa6, 0xffffffbd, 0xffffffeb, 0xffffff8b, 0xffffff88, 0xffffffeb, 0xffffff8b, 0xffffffa4, 0x20, 0x5e, 0x5e, 0x2f}, "utf-8") );
//				SERVER_MESSAGE_LIST.add( new String(new byte[]{0x53, 0x65, 0x72, 0x76, 0x65, 0x72, 0x43, 0x6f, 0x72, 0x65, 0x20}, "utf-8") + GuiMain.SERVER_VERSION );
//				SERVER_MESSAGE_LIST.add( new String(new byte[]{0x20, 0x3a, 0x20, 0x62, 0x79, 0x20, 0x70, 0x73, 0x6a, 0x75, 0x6d, 0x70}, "utf-8") );
//				//  : sloweye@nate.com
//				SERVER_MESSAGE_LIST.add( new String(new byte[]{0x20, 0x3a, 0x20, 0x70, 0x73, 0x6a, 0x75, 0x6d, 0x70, 0x40, 0x70, 0x73, 0x6a, 0x75, 0x6d, 0x70, 0x2e, 0x63, 0x6f, 0x6d}, "utf-8") );
//			} catch (Exception e) {
//				//  : sloweye@nate.com
//				SERVER_MESSAGE_LIST.add( new String(new byte[]{0x20, 0x3a, 0x20, 0x70, 0x73, 0x6a, 0x75, 0x6d, 0x70, 0x40, 0x70, 0x73, 0x6a, 0x75, 0x6d, 0x70, 0x2e, 0x63, 0x6f, 0x6d}) );
//			}
//		}
		
		TimeLine.end();
	}
	
}
