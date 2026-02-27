package lineage.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.MagicdollList;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterSpMr;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_ObjectSpeed;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.world.controller.ChattingController;
import lineage.world.object.instance.PcInstance;

public class MagicdollListDatabase {
	
	static private List<MagicdollList> list;
	
	static public void init(Connection con) {
		TimeLine.start("MagicdollListDatabase..");
		
		if(list == null)
			list = new ArrayList<MagicdollList>();
		else
			list.clear();

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement("SELECT * FROM magicdoll_list");
			rs = st.executeQuery();
			while(rs.next()){
				MagicdollList mdl = new MagicdollList();
				mdl.setItemName(rs.getString("item_name"));
				mdl.setMaterialName(rs.getString("material_name"));
				mdl.setMaterialCount(rs.getInt("material_count"));
				mdl.setDollName(rs.getString("doll_name"));
				mdl.setDollGfx(rs.getInt("doll_gfx"));
				mdl.setDollBuffType(rs.getString("doll_buff_type"));
				mdl.setDollBuffEffect(rs.getInt("doll_buff_effect"));
				mdl.setDollContinuous(rs.getInt("doll_continuous"));
				
				list.add( mdl );
			}
		} catch (Exception e) {
			lineage.share.System.printf("%s : init(Connection con)\r\n", MagicdollListDatabase.class.toString());
			lineage.share.System.println(e);
		} finally {
			DatabaseConnection.close(st, rs);
		}
		
		TimeLine.end();
	}
	
	static public MagicdollList find(String name) {
		for(MagicdollList mdl : list){
			if(mdl.getItemName().equalsIgnoreCase(name))
				return mdl;
		}
		return null;
	}
	
	/**
	 * 버프 타입별 버프 이팩트값 찾아서 리턴함.
	 * @param type
	 * @return
	 */
	static public int getBuffEffect(String type) {
		for(MagicdollList mdl : list){
			if(mdl.getDollBuffType().equalsIgnoreCase(type))
				return mdl.getDollBuffEffect();
		}
		return 0;
	}
	
	/**
	 * 매직인형 착용 및 해제시 옵션 처리 함수.
	 * @param pc
	 * @param mdl
	 * @param enabled
	 */
	static public void toOption(PcInstance pc, MagicdollList mdl, boolean enabled) {
		if (mdl == null)
			return;
		if (enabled) {
		
		// 1단계 마법인형
			if (mdl.getDollBuffType().equalsIgnoreCase("돌 골렘")) {
				pc.setMagicdollStoneGolem(enabled);
				pc.setDynamicReduction(pc.getDynamicReduction() + 1);
				ChattingController.toChatting(pc, "돌 골렘: 대미지 감소+1", Lineage.CHATTING_MODE_MESSAGE);
			} else if (mdl.getDollBuffType().equalsIgnoreCase("늑대인간")) {
				pc.setMagicdollWerewolf(enabled);
				ChattingController.toChatting(pc, "늑대인간: 근거리 공격 시 일정 확률로 추가 대미지+15", Lineage.CHATTING_MODE_MESSAGE);
			} else if (mdl.getDollBuffType().equalsIgnoreCase("버그베어")) {
				pc.setMagicdollBugbear(enabled);
				ChattingController.toChatting(pc, "버그베어: 소지 무게 증가+500", Lineage.CHATTING_MODE_MESSAGE);
			} else if (mdl.getDollBuffType().equalsIgnoreCase("크러스트시안")) {
				pc.setMagicdollCrustacean(enabled);
				ChattingController.toChatting(pc, "크러스트시안: 원거리 공격 시 일정 확률로 추가 대미지+15", Lineage.CHATTING_MODE_MESSAGE);
			} else if (mdl.getDollBuffType().equalsIgnoreCase("에티")) {
				pc.setMagicdollYeti(enabled);
				pc.setDynamicAc(pc.getDynamicAc() + 2);
				ChattingController.toChatting(pc, "에티: AC-2", Lineage.CHATTING_MODE_MESSAGE);
			} else if (mdl.getDollBuffType().equalsIgnoreCase("목각")) {
				pc.setMagicdollTree(enabled);
				pc.setDynamicHp(pc.getDynamicHp() + 50);
				ChattingController.toChatting(pc, "목각: 최대 HP+50", Lineage.CHATTING_MODE_MESSAGE);
				pc.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), pc));
			
				// 2단계 마법인형
			} else if (mdl.getDollBuffType().equalsIgnoreCase("서큐버스")) {
				pc.setMagicdollSuccubus(enabled);
				pc.setMagicdollTimeMpTic(64);
				pc.setMagicdollMpTic(15);
				ChattingController.toChatting(pc, "서큐버스: 64초당 MP 회복+15", Lineage.CHATTING_MODE_MESSAGE);
			} else if (mdl.getDollBuffType().equalsIgnoreCase("장로")) {
				pc.setMagicdollElder(enabled);
				pc.setMagicdollTimeMpTic(64);
				pc.setMagicdollMpTic(15);
				ChattingController.toChatting(pc, "장로: 64초당 MP 회복+15", Lineage.CHATTING_MODE_MESSAGE);
			} else if (mdl.getDollBuffType().equalsIgnoreCase("코카트리스")) {
				pc.setMagicdollCockatrice(enabled);
				pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() + 1);
				pc.setDynamicAddHitBow(pc.getDynamicAddHitBow() + 1);
				ChattingController.toChatting(pc, "코카트리스: 원거리 대미지+1, 원거리 명중+1", Lineage.CHATTING_MODE_MESSAGE);
			} else if (mdl.getDollBuffType().equalsIgnoreCase("눈사람")) {
				pc.setMagicdollSnowmanB(enabled);
				pc.setDynamicAddDmg(pc.getDynamicAddDmg() + 1);
				pc.setDynamicAddHit(pc.getDynamicAddHit() + 1);
				ChattingController.toChatting(pc, "눈사람: 근거리 대미지+1, 근거리 명중+1", Lineage.CHATTING_MODE_MESSAGE);
			} else if (mdl.getDollBuffType().equalsIgnoreCase("인어")) {
				pc.setMagicdollMermaid(enabled);
				pc.setDynamicExp(pc.getDynamicExp() + 0.05);
				ChattingController.toChatting(pc, "인어: 경험치 보너스+5%", Lineage.CHATTING_MODE_MESSAGE);
			} else if (mdl.getDollBuffType().equalsIgnoreCase("라바골렘")) {
				pc.setMagicdollLavaGolem(enabled);
				pc.setDynamicAddDmg(pc.getDynamicAddDmg() + 1);
				pc.setDynamicReduction(pc.getDynamicReduction() + 1);
				ChattingController.toChatting(pc, "라바 골렘: 근거리 대미지+1, 대미지 감소+1", Lineage.CHATTING_MODE_MESSAGE);
			
				// 3단계 마법인형
							} else if (mdl.getDollBuffType().equalsIgnoreCase("자이언트")) {
								pc.setMagicdollGiant(enabled);
								pc.setDynamicExp(pc.getDynamicExp() + 0.1);
								pc.setDynamicReduction(pc.getDynamicReduction() + 1);
								ChattingController.toChatting(pc, "자이언트: 경험치 보너스+10%, 대미지 감소+1", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("흑장로")) {
								pc.setMagicdollBlackElder(enabled);
								pc.setMagicdollTimeMpTic(64);
								pc.setMagicdollMpTic(15);
								ChattingController.toChatting(pc, "흑장로: 64초당 MP 회복+15, 일정확률 콜 라이트닝 발동", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("서큐버스퀸")) {
								pc.setMagicdollsuccubusQueen(enabled);
								pc.setMagicdollTimeMpTic(64);
								pc.setMagicdollMpTic(15);
								pc.setDynamicSp(pc.getDynamicSp() + 1);
								ChattingController.toChatting(pc, "서큐버스 퀸: 64초당 MP 회복+15, SP+1", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("드레이크")) {
								pc.setMagicdollDrake(enabled);
								pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() + 2);
								pc.setMagicdollTimeMpTic(64);
								pc.setMagicdollMpTic(6);
								ChattingController.toChatting(pc, "드레이크: 원거리 대미지+2, 64초당 MP 회복+6", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("킹 버그베어")) {
								pc.setMagicdollKingBugbear(enabled);
								pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() + 2);
								pc.setMagicdollTimeMpTic(64);
								pc.setMagicdollMpTic(10);
								ChattingController.toChatting(pc, "킹 버그베어: PvP 대미지 감소+2, 64초당 MP 회복+10", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("다이아몬드골렘")) {
								pc.setMagicdollDimondGolem(enabled);
								pc.setDynamicReduction(pc.getDynamicReduction() + 2);
								ChattingController.toChatting(pc, "다이아몬드 골렘: 대미지 감소+2", Lineage.CHATTING_MODE_MESSAGE);
			
								// 4단계 마법인형
							} else if (mdl.getDollBuffType().equalsIgnoreCase("리치")) {
								pc.setMagicdollLich(enabled);
								pc.setDynamicSp(pc.getDynamicSp() + 2);
								pc.setDynamicHp(pc.getDynamicHp() + 80);
								ChattingController.toChatting(pc, "리치: SP+2, 최대 HP+80", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("사이클롭스")) {
								pc.setMagicdollCyclops(enabled);
								pc.setDynamicAddDmg(pc.getDynamicAddDmg() + 2);
								pc.setDynamicAddHit(pc.getDynamicAddHit() + 2);
								pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() + 2);
								ChattingController.toChatting(pc, "사이클롭스: 근거리 대미지+2, 근거리 명중+2, PvP 대미지 감소+2", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("나이트발드")) {
								pc.setMagicdollKnightVald(enabled);
								pc.setDynamicAddDmg(pc.getDynamicAddDmg() + 2);
								pc.setDynamicAddHit(pc.getDynamicAddHit() + 2);
								pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() + 2);
								ChattingController.toChatting(pc, "나이트발드: 근거리 대미지+2, 근거리 명중+2, PvP 대미지+2", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("시어")) {
								pc.setMagicdollSeer(enabled);
								pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() + 4);
								pc.setMagicdollTimeHpTic(32);
								pc.setMagicdollHpTic(30);
								ChattingController.toChatting(pc, "시어: 원거리 대미지+4, 32초당 HP 회복+30", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("아이리스")) {
								pc.setMagicdollIris(enabled);
								pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() + 2);
								pc.setDynamicReduction(pc.getDynamicReduction() + 3);
								ChattingController.toChatting(pc, "아이리스: PvP 대미지+2, 대미지 감소+3", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("뱀파이어")) {
								pc.setMagicdollBampaear(enabled);
								pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() + 3);
								pc.setMagicdollTimeHpTic(32);
								pc.setMagicdollHpTic(30);
								ChattingController.toChatting(pc, "뱀파이어: PvP 대미지+3, 32초당 HP 회복+30", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("머미로드")) {
								pc.setMagicdollMummylord(enabled);
								pc.setDynamicSp(pc.getDynamicSp() + 2);
								pc.setMagicdollTimeMpTic(64);
								pc.setMagicdollMpTic(15);
								ChattingController.toChatting(pc, "머미로드: SP+2, 64초당 MP 회복+15", Lineage.CHATTING_MODE_MESSAGE);
								
								// 5단계 마법인형
							} else if (mdl.getDollBuffType().equalsIgnoreCase("데몬")) {
								pc.setMagicdollDemon(enabled);
								pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() + 5);
								pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() + 4);
								ChattingController.toChatting(pc, "데몬: PvP 대미지+5, PvP 대미지 감소+4", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("데스나이트")) {
								pc.setMagicdollDeathKnight(enabled);
								pc.setDynamicReduction(pc.getDynamicReduction() + 4);
								pc.setDynamicExp(pc.getDynamicExp() + 0.2);
								ChattingController.toChatting(pc, "데스나이트: 대미지 감소+4, 경험치 보너스+20%, 일정확률 헬파이어 발동", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("바란카")) {
								pc.setMagicdollBarancar(enabled);
								pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() + 3);
								pc.setDynamicReduction(pc.getDynamicReduction() + 3);
								ChattingController.toChatting(pc, "바란카: 대미지 감소+3, PvP 대미지 감소+3", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("타락")) {
								pc.setMagicdollTarak(enabled);
								pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() + 3);
								pc.setDynamicSp(pc.getDynamicSp() + 3);
								ChattingController.toChatting(pc, "타락: PvP 대미지 감소+3, SP+3", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("바포메트")) {
								pc.setMagicdollBaphomet(enabled);
								pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() + 3);
								pc.setDynamicSp(pc.getDynamicSp() + 2);
								pc.setDynamicReduction(pc.getDynamicReduction() + 1);
								ChattingController.toChatting(pc, "바포메트: PvP 대미지 감소+3, SP+2, 대미지 감소+1", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("얼음여왕")) {
								pc.setMagicdollIceQueen(enabled);
								pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() + 4);
								pc.setDynamicAddHitBow(pc.getDynamicAddHitBow() + 4);
								pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() + 3);
								ChattingController.toChatting(pc, "얼음여왕: 원거리 대미지+4, 원거리 명중+4, PvP 대미지 감소+3", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("커츠")) {
								pc.setMagicdollCuze(enabled);
								pc.setDynamicReduction(pc.getDynamicReduction() + 2);
								pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() + 2);
								pc.setDynamicAddDmg(pc.getDynamicAddDmg() + 3);
								pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() + 3);
								ChattingController.toChatting(pc, "커츠: 추가 대미지+3, 대미지 감소+2, PvP 대미지 감소+2", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("안타라스")) {
								pc.setMagicdollAntaras(enabled);
								pc.setDynamicReduction(pc.getDynamicReduction() + 5);
								pc.setDynamicExp(pc.getDynamicExp() + 0.25);
								pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() + 3);
								pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() + 2);
								pc.setDynamicAc(pc.getDynamicAc() + 2);
								pc.setMagicdollTimeMpTic(64);
								pc.setMagicdollMpTic(15);
								ChattingController.toChatting(pc, "안타라스: 대미지 감소+5, 경험치 보너스+25%, AC-2, PvP 대미지+3, PvP 대미지 감소+2, 64초당 MP 회복+15", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("파푸리온")) {
								pc.setMagicdollPapoorion(enabled);
								pc.setDynamicSp(pc.getDynamicSp() + 4);
								pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() + 2);
								pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() + 3);
								pc.setDynamicAc(pc.getDynamicAc() + 2);
								pc.setMagicdollTimeMpTic(64);
								pc.setMagicdollMpTic(5);
								ChattingController.toChatting(pc, "파푸리온: SP+4, AC-2, PvP 대미지+3, PvP 대미지 감소+2, 64초당 MP 회복+5", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("린드비오르")) {
								pc.setMagicdollLindvior(enabled);
								pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() + 4);
								pc.setDynamicAddHitBow(pc.getDynamicAddHitBow() + 5);
								pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() + 2);
								pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() + 3);
								pc.setDynamicAc(pc.getDynamicAc() + 2);
								pc.setMagicdollTimeMpTic(64);
								pc.setMagicdollMpTic(5);
								ChattingController.toChatting(pc, "린드비오르: 원거리 대미지+4, 원거리 명중+5, AC-2, PvP 대미지+3, PvP 대미지 감소+2, 64초당 MP 회복+5", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("발라카스")) {
								pc.setMagicdollValakas(enabled);
								pc.setDynamicAddDmg(pc.getDynamicAddDmg() + 4);
								pc.setDynamicAddHit(pc.getDynamicAddHit() + 5);
								pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() + 3);
								pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() + 2);
								pc.setDynamicAc(pc.getDynamicAc() + 2);
								pc.setMagicdollTimeMpTic(64);
								pc.setMagicdollMpTic(5);
								ChattingController.toChatting(pc, "발라카스: 근거리 대미지+4, 근거리 명중+5, AC-2, PvP 대미지+3, PvP 대미지 감소+2, 64초당 MP 회복+5", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("기르타스")) {
								pc.setMagicdollGirtas(enabled);
								pc.setDynamicExp(pc.getDynamicExp() + 0.5);
								pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() + 6);
								pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() + 4);
								pc.setDynamicHp(pc.getDynamicHp() + 200);
								pc.setDynamicReduction(pc.getDynamicReduction() + 3);
								ChattingController.toChatting(pc, "기르타스: 대미지 감소+3, 경험치 보너스+50%, 소지 무게 증가+3000, PvP 대미지+6, PvP 대미지 감소+4, 최대 HP+200, 일정확률 마법 발동", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("스파토이")) {
								pc.setMagicdollSpartoi(enabled);
								pc.setDynamicAddDmg(pc.getDynamicAddDmg() + 1);
								pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() + 2);
								ChattingController.toChatting(pc, "스파토이: 근거리 대미지+1, PvP 대미지 감소+2", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("허수아비")) {
								pc.setMagicdollCracker(enabled);
								pc.setDynamicAddHitBow(pc.getDynamicAddHitBow() + 1);
								pc.setDynamicAddHit(pc.getDynamicAddHit() + 1);
								pc.setDynamicHp(pc.getDynamicHp() + 50);
								pc.setDynamicMp(pc.getDynamicMp() + 30);
								ChattingController.toChatting(pc, "허수아비: 원거리 명중+1, 근거리 명중+1, HP+50, MP+30", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("그렘린")) {
								pc.setMagicdollGramlin(enabled);
								pc.setDynamicAddDmg(pc.getDynamicAddDmg() + 2);
								pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() + 2);
								pc.setDynamicReduction(pc.getDynamicReduction() + 2);
								pc.setDynamicExp(pc.getDynamicExp() + 0.1);
								pc.setDynamicSp(pc.getDynamicSp() + 1);
								pc.setMagicdollTimeMpTic(64);
								pc.setMagicdollMpTic(5);
								ChattingController.toChatting(pc, "그렘린: 근거리 대미지+2, 원거리 대미지+2, 대미지 감소+2, 경험치 보너스+10%, SP+1, 64초당 MP 회복+5", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("라미아")) {
								pc.setMagicdollRamia(enabled);
								ChattingController.toChatting(pc, "라미아: MP 회복+4, 일정 확률로 독 공격의 효과", Lineage.CHATTING_MODE_MESSAGE);
								
							} else if (mdl.getDollBuffType().equalsIgnoreCase("왕자")) {
								pc.setMagicdollroyal(enabled);
								pc.setDynamicHp(pc.getDynamicHp() + 30);
								ChattingController.toChatting(pc, "왕자: 최대 HP+30, 소지 무게 증가+300, 아데나 획득량+50%", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("남기사")) {
								pc.setMagicdollknight(enabled);
								pc.setDynamicHp(pc.getDynamicHp() + 30);
								ChattingController.toChatting(pc, "남기사: 최대 HP+30, 소지 무게 증가+300, 아데나 획득량+50%", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("남요정")) {
								pc.setMagicdollelf(enabled);
								pc.setDynamicHp(pc.getDynamicHp() + 30);
								ChattingController.toChatting(pc, "남요정: 최대 HP+30, 소지 무게 증가+300, 아데나 획득량+50%", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("남마법사")) {
								pc.setMagicdollwizard(enabled);
								pc.setDynamicHp(pc.getDynamicHp() + 30);
								ChattingController.toChatting(pc, "남마법사: 최대 HP+30, 소지 무게 증가+300, 아데나 획득량+50%", Lineage.CHATTING_MODE_MESSAGE);								
							} else if (mdl.getDollBuffType().equalsIgnoreCase("공주")) {
								pc.setMagicdollroyal2(enabled);
								pc.setDynamicHp(pc.getDynamicHp() + 30);
								ChattingController.toChatting(pc, "공주: 최대 HP+30, 소지 무게 증가+300, 아데나 획득량+50%", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("여기사")) {
								pc.setMagicdollknight2(enabled);
								pc.setDynamicHp(pc.getDynamicHp() + 30);
								ChattingController.toChatting(pc, "여기사: 최대 HP+30, 소지 무게 증가+300, 아데나 획득량+50%", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("여요정")) {
								pc.setMagicdollelf2(enabled);
								pc.setDynamicHp(pc.getDynamicHp() + 30);
								ChattingController.toChatting(pc, "여요정: 최대 HP+30, 소지 무게 증가+300, 아데나 획득량+50%", Lineage.CHATTING_MODE_MESSAGE);
							} else if (mdl.getDollBuffType().equalsIgnoreCase("여마법사")) {
								pc.setMagicdollwizard2(enabled);
								pc.setDynamicHp(pc.getDynamicHp() + 30);
								ChattingController.toChatting(pc, "여마법사: 최대 HP+30, 소지 무게 증가+300, 아데나 획득량+50%", Lineage.CHATTING_MODE_MESSAGE);
							}
						} else {
							// 1단계 마법인형
					if (mdl.getDollBuffType().equalsIgnoreCase("돌 골렘")) {
						pc.setMagicdollStoneGolem(enabled);
						pc.setDynamicReduction(pc.getDynamicReduction() - 1);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("늑대인간")) {
						pc.setMagicdollWerewolf(enabled);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("버그베어")) {
						pc.setMagicdollBugbear(enabled);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("크러스트시안")) {
						pc.setMagicdollCrustacean(enabled);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("에티")) {
						pc.setMagicdollYeti(enabled);
						pc.setDynamicAc(pc.getDynamicAc() - 2);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("목각")) {
						pc.setMagicdollTree(enabled);
						pc.setDynamicHp(pc.getDynamicHp() - 50);
						// 2단계 마법인형
					} else if (mdl.getDollBuffType().equalsIgnoreCase("서큐버스")) {
						pc.setMagicdollSuccubus(enabled);
						pc.setMagicdollTimeMpTic(0);
						pc.setMagicdollMpTic(0);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("장로")) {
						pc.setMagicdollElder(enabled);
						pc.setMagicdollTimeMpTic(0);
						pc.setMagicdollMpTic(0);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("코카트리스")) {
						pc.setMagicdollCockatrice(enabled);
						pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() - 1);
						pc.setDynamicAddHitBow(pc.getDynamicAddHitBow() - 1);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("눈사람")) {
						pc.setMagicdollSnowmanB(enabled);
						pc.setDynamicAddDmg(pc.getDynamicAddDmg() - 1);
						pc.setDynamicAddHit(pc.getDynamicAddHit() - 1);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("인어")) {
						pc.setMagicdollMermaid(enabled);
						pc.setDynamicExp(pc.getDynamicExp() - 0.05);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("라바골렘")) {
						pc.setMagicdollLavaGolem(enabled);
						pc.setDynamicAddDmg(pc.getDynamicAddDmg() - 1);
						pc.setDynamicReduction(pc.getDynamicReduction() - 1);
						// 3단계 마법인형
					} else if (mdl.getDollBuffType().equalsIgnoreCase("자이언트")) {
						pc.setMagicdollGiant(enabled);
						pc.setDynamicExp(pc.getDynamicExp() - 0.1);
						pc.setDynamicReduction(pc.getDynamicReduction() - 1);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("흑장로")) {
						pc.setMagicdollBlackElder(enabled);
						pc.setMagicdollTimeMpTic(0);
						pc.setMagicdollMpTic(0);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("서큐버스퀸")) {
						pc.setMagicdollsuccubusQueen(enabled);
						pc.setMagicdollTimeMpTic(0);
						pc.setMagicdollMpTic(0);
						pc.setDynamicSp(pc.getDynamicSp() - 1);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("드레이크")) {
						pc.setMagicdollDrake(enabled);
						pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() - 2);
						pc.setMagicdollTimeMpTic(0);
						pc.setMagicdollMpTic(0);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("킹 버그베어")) {
						pc.setMagicdollKingBugbear(enabled);
						pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() - 2);
						pc.setMagicdollTimeMpTic(0);
						pc.setMagicdollMpTic(0);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("다이아몬드골렘")) {
						pc.setMagicdollDimondGolem(enabled);
						pc.setDynamicReduction(pc.getDynamicReduction() - 2);
	
						// 4단계 마법인형
					} else if (mdl.getDollBuffType().equalsIgnoreCase("리치")) {
						pc.setMagicdollLich(enabled);
						pc.setDynamicSp(pc.getDynamicSp() - 2);
						pc.setDynamicHp(pc.getDynamicHp() - 80);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("사이클롭스")) {
						pc.setMagicdollCyclops(enabled);
						pc.setDynamicAddDmg(pc.getDynamicAddDmg() - 2);
						pc.setDynamicAddHit(pc.getDynamicAddHit() - 2);
						pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() - 2);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("나이트발드")) {
						pc.setMagicdollKnightVald(enabled);
						pc.setDynamicAddDmg(pc.getDynamicAddDmg() - 2);
						pc.setDynamicAddHit(pc.getDynamicAddHit() - 2);
						pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() - 2);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("시어")) {
						pc.setMagicdollSeer(enabled);
						pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() - 4);
						pc.setMagicdollTimeHpTic(0);
						pc.setMagicdollHpTic(0);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("아이리스")) {
						pc.setMagicdollIris(enabled);
						pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() - 2);
						pc.setDynamicReduction(pc.getDynamicReduction() - 3);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("뱀파이어")) {
						pc.setMagicdollBampaear(enabled);
						pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() - 3);
						pc.setMagicdollTimeHpTic(0);
						pc.setMagicdollHpTic(0);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("머미로드")) {
						pc.setMagicdollMummylord(enabled);
						pc.setDynamicSp(pc.getDynamicSp() - 2);
						pc.setMagicdollTimeMpTic(0);
						pc.setMagicdollMpTic(0);
						// 5단계 마법인형
					} else if (mdl.getDollBuffType().equalsIgnoreCase("데몬")) {
						pc.setMagicdollDemon(enabled);
						pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() - 5);
						pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() - 4);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("데스나이트")) {
						pc.setMagicdollDeathKnight(enabled);
						pc.setDynamicReduction(pc.getDynamicReduction() - 4);
						pc.setDynamicExp(pc.getDynamicExp() - 0.2);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("바란카")) {
						pc.setMagicdollBarancar(enabled);
						pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() - 3);
						pc.setDynamicReduction(pc.getDynamicReduction() - 3);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("타락")) {
						pc.setMagicdollTarak(enabled);
						pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() - 3);
						pc.setDynamicSp(pc.getDynamicSp() - 3);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("바포메트")) {
						pc.setMagicdollBaphomet(enabled);
						pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() - 3);
						pc.setDynamicSp(pc.getDynamicSp() - 2);
						pc.setDynamicReduction(pc.getDynamicReduction() - 1);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("얼음여왕")) {
						pc.setMagicdollIceQueen(enabled);
						pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() - 4);
						pc.setDynamicAddHitBow(pc.getDynamicAddHitBow() - 4);
						pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() - 3);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("커츠")) {
						pc.setMagicdollCuze(enabled);
						pc.setDynamicReduction(pc.getDynamicReduction() - 2);
						pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() - 2);
						pc.setDynamicAddDmg(pc.getDynamicAddDmg() - 3);
						pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() - 3);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("안타라스")) {
						pc.setMagicdollAntaras(enabled);
						pc.setDynamicReduction(pc.getDynamicReduction() - 5);
						pc.setDynamicExp(pc.getDynamicExp() - 0.25);
						pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() - 3);
						pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() - 2);
						pc.setDynamicAc(pc.getDynamicAc() - 2);
						pc.setMagicdollTimeMpTic(0);
						pc.setMagicdollMpTic(0);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("파푸리온")) {
						pc.setMagicdollPapoorion(enabled);
						pc.setDynamicSp(pc.getDynamicSp() - 4);
						pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() - 2);
						pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() - 3);
						pc.setDynamicAc(pc.getDynamicAc() - 2);
						pc.setMagicdollTimeMpTic(0);
						pc.setMagicdollMpTic(0);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("린드비오르")) {
						pc.setMagicdollLindvior(enabled);
						pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() - 4);
						pc.setDynamicAddHitBow(pc.getDynamicAddHitBow() - 5);
						pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() - 2);
						pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() - 3);
						pc.setDynamicAc(pc.getDynamicAc() - 2);
						pc.setMagicdollTimeMpTic(0);
						pc.setMagicdollMpTic(0);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("발라카스")) {
						pc.setMagicdollValakas(enabled);
						pc.setDynamicAddDmg(pc.getDynamicAddDmg() - 4);
						pc.setDynamicAddHit(pc.getDynamicAddHit() - 5);
						pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() - 3);
						pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() - 2);
						pc.setDynamicAc(pc.getDynamicAc() - 2);
						pc.setMagicdollTimeMpTic(0);
						pc.setMagicdollMpTic(0);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("기르타스")) {
						pc.setMagicdollGirtas(enabled);
						pc.setDynamicExp(pc.getDynamicExp() - 0.5);
						pc.setDynamicAddPvpDmg(pc.getDynamicAddPvpDmg() - 6);
						pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() - 4);
						pc.setDynamicHp(pc.getDynamicHp() - 200);
						pc.setDynamicReduction(pc.getDynamicReduction() - 3);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("스파토이")) {
						pc.setMagicdollSpartoi(enabled);
						pc.setDynamicAddDmg(pc.getDynamicAddDmg() - 1);
						pc.setDynamicAddPvpReduction(pc.getDynamicAddPvpReduction() - 2);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("허수아비")) {
						pc.setMagicdollCracker(enabled);
						pc.setDynamicAddHitBow(pc.getDynamicAddHitBow() - 1);
						pc.setDynamicAddHit(pc.getDynamicAddHit() - 1);
						pc.setDynamicHp(pc.getDynamicHp() - 50);
						pc.setDynamicMp(pc.getDynamicMp() - 30);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("그렘린")) {
						pc.setMagicdollGramlin(enabled);
						pc.setDynamicAddDmg(pc.getDynamicAddDmg() - 2);
						pc.setDynamicAddDmgBow(pc.getDynamicAddDmgBow() - 2);
						pc.setDynamicReduction(pc.getDynamicReduction() - 2);
						pc.setDynamicExp(pc.getDynamicExp() - 0.1);
						pc.setDynamicSp(pc.getDynamicSp() - 1);
						pc.setMagicdollTimeMpTic(0);
						pc.setMagicdollMpTic(0);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("라미아")) {
						pc.setMagicdollRamia(enabled);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("왕자")) {
						pc.setMagicdollroyal(enabled);
						pc.setDynamicHp(pc.getDynamicHp() - 30);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("남기사")) {
						pc.setMagicdollknight(enabled);
						pc.setDynamicHp(pc.getDynamicHp() - 30);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("남요정")) {
						pc.setMagicdollelf(enabled);
						pc.setDynamicHp(pc.getDynamicHp() - 30);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("남마법사")) {
						pc.setMagicdollwizard(enabled);
						pc.setDynamicHp(pc.getDynamicHp() - 30);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("공주")) {
						pc.setMagicdollroyal2(enabled);
						pc.setDynamicHp(pc.getDynamicHp() - 30);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("여기사")) {
						pc.setMagicdollknight2(enabled);
						pc.setDynamicHp(pc.getDynamicHp() - 30);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("여요정")) {
						pc.setMagicdollelf2(enabled);
						pc.setDynamicHp(pc.getDynamicHp() - 30);
					} else if (mdl.getDollBuffType().equalsIgnoreCase("여마법사")) {
						pc.setMagicdollwizard2(enabled);
						pc.setDynamicHp(pc.getDynamicHp() - 30);
					}
						}

						pc.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), pc));
						pc.toSender(S_CharacterSpMr.clone(BasePacketPooling.getPool(S_CharacterSpMr.class), pc));
					}
				}
