package lineage.world.object.npc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Monster;
import lineage.bean.database.Npc;
import lineage.bean.database.Skill;
import lineage.bean.database.SummonList;
import lineage.bean.lineage.Summon;
import lineage.database.MonsterDatabase;
import lineage.database.MonsterSpawnlistDatabase;
import lineage.database.NpcDatabase;
import lineage.database.PolyDatabase;
import lineage.database.ServerDatabase;
import lineage.database.SummonListDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.thread.AiThread;
import lineage.util.Util;
import lineage.world.World;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SummonController;
import lineage.world.controller.SummonController.TYPE;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.instance.SoldierInstance;
import lineage.world.object.instance.SummonInstance;
import lineage.world.object.magic.ShapeChange;

public class RingOfSummon extends object {
	private static final SummonList sl = null;
	protected String Summon_name;

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "summonlist"));
	}

	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){
	    summonMonster(pc, action);
	}
	
	public void summonMonster(PcInstance pc, String action) {
	    String Summon_name = null;
	    int Summon_level = 0;

	    if (Lineage.server_version > 144) {
	        switch (action) {
			case "7": 
				if(pc.getLevel()>=28)
				Summon_name = "서먼_늑대인간";
				Summon_level = 28;
				break;
			case "263": 
				if(pc.getLevel()>=28)
				Summon_name = "서먼_해골 도끼병";
				Summon_level = 28;
				break;
				
			case "8": 
				if(pc.getLevel()>=32)
				Summon_name = "서먼_해골";
				Summon_level = 28;
				break;
			case "264": 
				if(pc.getLevel()>=32)
				Summon_name = "서먼_라이칸 스로프";
				Summon_level = 28;
				break;
				
			case "9": 
				if(pc.getLevel()>=36)
				Summon_name = "서먼_스파토이";
				Summon_level = 28;
				break;
			case "265": 
				if(pc.getLevel()>=36)
				Summon_name = "서먼_좀비 엘모어 병사";
				Summon_level = 28;
				break;
				
			case "10": 
				if(pc.getLevel()>=40)
				Summon_name = "서먼_버그베어";
				Summon_level = 28;
				break;
			case "266": 
				if(pc.getLevel()>=40)
				Summon_name = "서먼_미노타우르스 (도끼)";
				Summon_level = 28;
				break;
				
			case "11": 
				if(pc.getLevel()>=44)
				Summon_name = "서먼_킹 버그베어";
				Summon_level = 28;
				break;
			case "267": 
				if(pc.getLevel()>=44)
				Summon_name = "서먼_해골 근위병";
				Summon_level = 28;
				break;
				
			case "12": 
				if(pc.getLevel()>=48)
				Summon_name = "서먼_해골 돌격병";
				Summon_level = 28;
				break;
			case "268": 
				if(pc.getLevel()>=48)
				Summon_name = "서먼_가고일";
				Summon_level = 28;
				break;
				
			case "13": 
				if(pc.getLevel()>=52)
				Summon_name = "서먼_에틴";
				Summon_level = 28;
				break;
			case "270": 
				if(pc.getLevel()>=52)
				Summon_name = "서먼_사이클롭스";
				Summon_level = 28;
				break;
	        }
	    }

	    if (Summon_name == null) {
	        return;
	    }

	    MonsterInstance mi = MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find(Summon_name));
	    if (mi != null) {
	        mi.setLevel(Summon_level);
	        mi.setX(pc.getX() - 1);
	        mi.setY(pc.getY() - 1);
	        mi.setMap(pc.getMap());
	        
	        //일반 서먼
	        if (SummonController.toNormalsummon(pc, 60 * 60, Summon_name, null)) {
	            // 성공적으로 소환된 경우
	        } else {
	            // 소환 실패 처리
	        }
	    }
	}
	}
	