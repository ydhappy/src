package lineage.world.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_ObjectChatting;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.share.TimeLine;

import lineage.world.World;
import lineage.world.object.object;


public final class TimerController3 {

	static private Calendar calendar;
	static private List<object> list_illusion;

	static public boolean mun = false;

	static public void init() {

		TimeLine.start("TimerController3...");

		calendar = Calendar.getInstance();
		list_illusion = new ArrayList<object>();

		TimeLine.end();
	}

	@SuppressWarnings("deprecation")
	static public void toTimer(long time) {

		PluginController.init(TimerController3.class, "toTimer", time);

		calendar.setTimeInMillis(time);
		Date date = calendar.getTime();
		// 겟 데이 = 일요일 0 월요일 1 화요일2 수요일3 목요일4 금요일5 토요일6
		int week = date.getDay();
		int hour = date.getHours();
		int min = date.getMinutes();
		int sec = date.getSeconds();
		
		if (Lineage.autohunt_onoff && hour == 17) {
			if( min == 49 &&  sec == 59 ){
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class),
						String.format("피크 타임 시작!")));
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class),
						String.format("[자동사냥] 종료 10분 전입니다.")));
				
			}
			if( min == 59 &&  sec == 59 ){
		
				Lineage.autohunt_onoff = false;
				
			}

		}
		
		if (!Lineage.autohunt_onoff&&hour == 22) {
			if(min == 50 &&  sec == 59 ){
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class),
						String.format("[자동사냥] 가능시간 10분 전입니다.")));
				
			}
			
			if(min == 59 &&  sec == 59 ){
				Lineage.autohunt_onoff = true;
			}

		}

		if (Lineage.event_mon) {
			if (min == 30 && sec == 18 || min == 00 && sec == 18) {
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class),
						String.format("\\fS수렵 이벤트가 진행중입니다.")));
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class),
						String.format("\\fS수렵대상: 토끼, 사슴, 멧돼지, 곰, 여우")));
			}
		}

		if (Lineage.event_christmas) {
			if (min == 30 && sec == 20 || min == 00 && sec == 20)
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class),
						"\\fS양말이벤트가 진행중입니다."));
		}

		if (Lineage.event_poly) {
			if (min == 30 && sec == 23 || min == 00 && sec == 23)
				World.toSender(S_ObjectChatting.clone(BasePacketPooling.getPool(S_ObjectChatting.class),
						"\\fS변신이벤트가 진행중입니다."));
		}
	}
}