package lineage.world.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage.bean.lineage.Craft;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_CraftTable;
import lineage.share.Lineage;
import lineage.share.TimeLine;
import lineage.util.Util;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.npc.craft.CraftTable;

public class CraftTableController {

	static private Map<Integer, Craft> craft_table_list;
	static private Map<Craft, List<Craft>> craft_table_needitem_list;
	
	static public void init(){
		TimeLine.start("CraftTableController..");

		craft_table_list = new HashMap<Integer, Craft>();
		craft_table_needitem_list = new HashMap<Craft, List<Craft>>();
		//
		craft_table_list.put(702, new Craft(ItemDatabase.find("거대 여왕 개미의 은빛 날개"), 1));
		craft_table_needitem_list.put(craft_table_list.get(702), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(702)).add(new Craft(ItemDatabase.find("에르자베의 알"), 100));
		craft_table_needitem_list.get(craft_table_list.get(702)).add(new Craft(ItemDatabase.find("은색의 망토"), 1));
		craft_table_needitem_list.get(craft_table_list.get(702)).add(new Craft(ItemDatabase.find("백금원석조각"), 100));
		craft_table_needitem_list.get(craft_table_list.get(702)).add(new Craft(ItemDatabase.find("마물의 기운"), 400));
		//
		craft_table_list.put(703, new Craft(ItemDatabase.find("거대 여왕 개미의 금빛 날개"), 1));
		craft_table_needitem_list.put(craft_table_list.get(703), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(703)).add(new Craft(ItemDatabase.find("에르자베의 알"), 100));
		craft_table_needitem_list.get(craft_table_list.get(703)).add(new Craft(ItemDatabase.find("은색의 망토"), 1));
		craft_table_needitem_list.get(craft_table_list.get(703)).add(new Craft(ItemDatabase.find("황금원석조각"), 100));
		craft_table_needitem_list.get(craft_table_list.get(703)).add(new Craft(ItemDatabase.find("마물의 기운"), 800));
		//
		craft_table_list.put(704, new Craft(ItemDatabase.find("명법군왕의 망토"), 1));
		craft_table_needitem_list.put(craft_table_list.get(704), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(704)).add(new Craft(ItemDatabase.find("영혼석: 헬바인"), 1));
		craft_table_needitem_list.get(craft_table_list.get(704)).add(new Craft(ItemDatabase.find("명법군왕의 인장"), 10));
		craft_table_needitem_list.get(craft_table_list.get(704)).add(new Craft(ItemDatabase.find("다크엘프 영혼의 결정체"), 1000));
		craft_table_needitem_list.get(craft_table_list.get(704)).add(new Craft(ItemDatabase.find("신관의 망토"), 1));
		craft_table_needitem_list.get(craft_table_list.get(704)).add(new Craft(ItemDatabase.find("마물의 기운"), 500));
		//
		craft_table_list.put(705, new Craft(ItemDatabase.find("마령군왕의 로브"), 1));
		craft_table_needitem_list.put(craft_table_list.get(705), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(705)).add(new Craft(ItemDatabase.find("영혼석: 라이아"), 1));
		craft_table_needitem_list.get(craft_table_list.get(705)).add(new Craft(ItemDatabase.find("마령군왕의 인장"), 10));
		craft_table_needitem_list.get(craft_table_list.get(705)).add(new Craft(ItemDatabase.find("다크엘프 영혼의 결정체"), 1000));
		craft_table_needitem_list.get(craft_table_list.get(705)).add(new Craft(ItemDatabase.find("신관의 로브"), 1));
		craft_table_needitem_list.get(craft_table_list.get(705)).add(new Craft(ItemDatabase.find("마물의 기운"), 500));
		//
		craft_table_list.put(706, new Craft(ItemDatabase.find("마수군왕의 부츠"), 1));
		craft_table_needitem_list.put(craft_table_list.get(706), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(706)).add(new Craft(ItemDatabase.find("영혼석: 바란카"), 1));
		craft_table_needitem_list.get(craft_table_list.get(706)).add(new Craft(ItemDatabase.find("마수군왕의 인장"), 10));
		craft_table_needitem_list.get(craft_table_list.get(706)).add(new Craft(ItemDatabase.find("다크엘프 영혼의 결정체"), 1000));
		craft_table_needitem_list.get(craft_table_list.get(706)).add(new Craft(ItemDatabase.find("무관의 부츠"), 1));
		craft_table_needitem_list.get(craft_table_list.get(706)).add(new Craft(ItemDatabase.find("마물의 기운"), 1000));
		//
		craft_table_list.put(707, new Craft(ItemDatabase.find("암살군왕의 장갑"), 1));
		craft_table_needitem_list.put(craft_table_list.get(707), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(707)).add(new Craft(ItemDatabase.find("영혼석: 슬레이브"), 1));
		craft_table_needitem_list.get(craft_table_list.get(707)).add(new Craft(ItemDatabase.find("마수군왕의 인장"), 10));
		craft_table_needitem_list.get(craft_table_list.get(707)).add(new Craft(ItemDatabase.find("다크엘프 영혼의 결정체"), 1000));
		craft_table_needitem_list.get(craft_table_list.get(707)).add(new Craft(ItemDatabase.find("무관의 장갑"), 1));
		craft_table_needitem_list.get(craft_table_list.get(707)).add(new Craft(ItemDatabase.find("마물의 기운"), 1000));
		//
		craft_table_list.put(708, new Craft(ItemDatabase.find("타락의 로브"), 1));
		craft_table_needitem_list.put(craft_table_list.get(708), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(708)).add(new Craft(ItemDatabase.find("영혼석: 타락"), 1));
		craft_table_needitem_list.get(craft_table_list.get(708)).add(new Craft(ItemDatabase.find("타락의 악마서 1권"), 6));
		craft_table_needitem_list.get(craft_table_list.get(708)).add(new Craft(ItemDatabase.find("검은 혈흔"), 1000));
		craft_table_needitem_list.get(craft_table_list.get(708)).add(new Craft(ItemDatabase.find("마물의 기운"), 500));
		//
		craft_table_list.put(709, new Craft(ItemDatabase.find("타락의 망토"), 1));
		craft_table_needitem_list.put(craft_table_list.get(709), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(709)).add(new Craft(ItemDatabase.find("영혼석: 타락"), 1));
		craft_table_needitem_list.get(craft_table_list.get(709)).add(new Craft(ItemDatabase.find("타락의 악마서 2권"), 6));
		craft_table_needitem_list.get(craft_table_list.get(709)).add(new Craft(ItemDatabase.find("검은 혈흔"), 1000));
		craft_table_needitem_list.get(craft_table_list.get(709)).add(new Craft(ItemDatabase.find("마물의 기운"), 500));
		//
		craft_table_list.put(710, new Craft(ItemDatabase.find("타락의 장갑"), 1));
		craft_table_needitem_list.put(craft_table_list.get(710), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(710)).add(new Craft(ItemDatabase.find("영혼석: 타락"), 1));
		craft_table_needitem_list.get(craft_table_list.get(710)).add(new Craft(ItemDatabase.find("타락의 악마서 3권"), 6));
		craft_table_needitem_list.get(craft_table_list.get(710)).add(new Craft(ItemDatabase.find("검은 혈흔"), 1000));
		craft_table_needitem_list.get(craft_table_list.get(710)).add(new Craft(ItemDatabase.find("마물의 기운"), 1000));
		//
		craft_table_list.put(711, new Craft(ItemDatabase.find("타락의 부츠"), 1));
		craft_table_needitem_list.put(craft_table_list.get(711), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(711)).add(new Craft(ItemDatabase.find("영혼석: 타락"), 1));
		craft_table_needitem_list.get(craft_table_list.get(711)).add(new Craft(ItemDatabase.find("타락의 악마서 4권"), 6));
		craft_table_needitem_list.get(craft_table_list.get(711)).add(new Craft(ItemDatabase.find("검은 혈흔"), 1000));
		craft_table_needitem_list.get(craft_table_list.get(711)).add(new Craft(ItemDatabase.find("마물의 기운"), 1000));
		//
		craft_table_list.put(714, new Craft(ItemDatabase.find("카스파의 모자"), 1));
		craft_table_needitem_list.put(craft_table_list.get(714), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(714)).add(new Craft(ItemDatabase.find("영혼석: 네크로맨서"), 1));
		craft_table_needitem_list.get(craft_table_list.get(714)).add(new Craft(ItemDatabase.find("네크로맨서의 수정구"), 3));
		craft_table_needitem_list.get(craft_table_list.get(714)).add(new Craft(ItemDatabase.find("데몬의 투구"), 1));
		craft_table_needitem_list.get(craft_table_list.get(714)).add(new Craft(ItemDatabase.find("화룡 비늘"), 3));
		craft_table_needitem_list.get(craft_table_list.get(714)).add(new Craft(ItemDatabase.find("마물의 기운"), 200));
		//
		craft_table_list.put(715, new Craft(ItemDatabase.find("메르키오르의 모자"), 1));
		craft_table_needitem_list.put(craft_table_list.get(715), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(715)).add(new Craft(ItemDatabase.find("영혼석: 네크로맨서"), 1));
		craft_table_needitem_list.get(craft_table_list.get(715)).add(new Craft(ItemDatabase.find("네크로맨서의 수정구"), 3));
		craft_table_needitem_list.get(craft_table_list.get(715)).add(new Craft(ItemDatabase.find("데몬의 투구"), 1));
		craft_table_needitem_list.get(craft_table_list.get(715)).add(new Craft(ItemDatabase.find("수룡 비늘"), 3));
		craft_table_needitem_list.get(craft_table_list.get(715)).add(new Craft(ItemDatabase.find("마물의 기운"), 200));
		//
		craft_table_list.put(716, new Craft(ItemDatabase.find("발터자르의 모자"), 1));
		craft_table_needitem_list.put(craft_table_list.get(716), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(716)).add(new Craft(ItemDatabase.find("영혼석: 네크로맨서"), 1));
		craft_table_needitem_list.get(craft_table_list.get(716)).add(new Craft(ItemDatabase.find("네크로맨서의 수정구"), 3));
		craft_table_needitem_list.get(craft_table_list.get(716)).add(new Craft(ItemDatabase.find("데몬의 투구"), 1));
		craft_table_needitem_list.get(craft_table_list.get(716)).add(new Craft(ItemDatabase.find("지룡 비늘"), 3));
		craft_table_needitem_list.get(craft_table_list.get(716)).add(new Craft(ItemDatabase.find("마물의 기운"), 200));
		//
		craft_table_list.put(717, new Craft(ItemDatabase.find("세마의 모자"), 1));
		craft_table_needitem_list.put(craft_table_list.get(717), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(717)).add(new Craft(ItemDatabase.find("영혼석: 네크로맨서"), 1));
		craft_table_needitem_list.get(craft_table_list.get(717)).add(new Craft(ItemDatabase.find("네크로맨서의 수정구"), 3));
		craft_table_needitem_list.get(craft_table_list.get(717)).add(new Craft(ItemDatabase.find("데몬의 투구"), 1));
		craft_table_needitem_list.get(craft_table_list.get(717)).add(new Craft(ItemDatabase.find("풍룡 비늘"), 3));
		craft_table_needitem_list.get(craft_table_list.get(717)).add(new Craft(ItemDatabase.find("마물의 기운"), 200));
		//
		craft_table_list.put(712, new Craft(ItemDatabase.find("오림의 목걸이"), 1));
		craft_table_needitem_list.put(craft_table_list.get(712), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(712)).add(new Craft(ItemDatabase.find("영혼석: 네크로맨서"), 1));
		craft_table_needitem_list.get(craft_table_list.get(712)).add(new Craft(ItemDatabase.find("네크로맨서의 수정구"), 10));
		craft_table_needitem_list.get(craft_table_list.get(712)).add(new Craft(ItemDatabase.find("최고급 루비"), 10));
		craft_table_needitem_list.get(craft_table_list.get(712)).add(new Craft(ItemDatabase.find("최고급 에메랄드"), 10));
		craft_table_needitem_list.get(craft_table_list.get(712)).add(new Craft(ItemDatabase.find("최고급 사파이어"), 10));
		craft_table_needitem_list.get(craft_table_list.get(712)).add(new Craft(ItemDatabase.find("최고급 다이아몬드"), 10));
		craft_table_needitem_list.get(craft_table_list.get(712)).add(new Craft(ItemDatabase.find("마물의 기운"), 500));
		//
		craft_table_list.put(713, new Craft(ItemDatabase.find("세마의 반지"), 1));
		craft_table_needitem_list.put(craft_table_list.get(713), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(713)).add(new Craft(ItemDatabase.find("영혼석: 네크로맨서"), 1));
		craft_table_needitem_list.get(craft_table_list.get(713)).add(new Craft(ItemDatabase.find("네크로맨서의 수정구"), 10));
		craft_table_needitem_list.get(craft_table_list.get(713)).add(new Craft(ItemDatabase.find("최고급 루비"), 10));
		craft_table_needitem_list.get(craft_table_list.get(713)).add(new Craft(ItemDatabase.find("최고급 에메랄드"), 10));
		craft_table_needitem_list.get(craft_table_list.get(713)).add(new Craft(ItemDatabase.find("최고급 사파이어"), 10));
		craft_table_needitem_list.get(craft_table_list.get(713)).add(new Craft(ItemDatabase.find("최고급 다이아몬드"), 10));
		craft_table_needitem_list.get(craft_table_list.get(713)).add(new Craft(ItemDatabase.find("마물의 기운"), 500));
		//
		craft_table_list.put(718, new Craft(ItemDatabase.find("지령의 블랙미스릴 화살"), 10));
		craft_table_needitem_list.put(craft_table_list.get(718), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(718)).add(new Craft(ItemDatabase.find("블랙미스릴 화살"), 10));
		craft_table_needitem_list.get(craft_table_list.get(718)).add(new Craft(ItemDatabase.find("결정체"), 10));
		//
		craft_table_list.put(719, new Craft(ItemDatabase.find("수령의 블랙미스릴 화살"), 10));
		craft_table_needitem_list.put(craft_table_list.get(719), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(719)).add(new Craft(ItemDatabase.find("블랙미스릴 화살"), 10));
		craft_table_needitem_list.get(craft_table_list.get(719)).add(new Craft(ItemDatabase.find("결정체"), 10));
		//
		craft_table_list.put(720, new Craft(ItemDatabase.find("풍령의 블랙미스릴 화살"), 10));
		craft_table_needitem_list.put(craft_table_list.get(720), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(720)).add(new Craft(ItemDatabase.find("블랙미스릴 화살"), 10));
		craft_table_needitem_list.get(craft_table_list.get(720)).add(new Craft(ItemDatabase.find("결정체"), 10));
		//
		craft_table_list.put(721, new Craft(ItemDatabase.find("화령의 블랙미스릴 화살"), 10));
		craft_table_needitem_list.put(craft_table_list.get(721), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(721)).add(new Craft(ItemDatabase.find("블랙미스릴 화살"), 10));
		craft_table_needitem_list.get(craft_table_list.get(721)).add(new Craft(ItemDatabase.find("결정체"), 10));
		//
		craft_table_list.put(722, new Craft(ItemDatabase.find("신성한 마법 방어 투구"), 1, 6, 1));
		craft_table_needitem_list.put(craft_table_list.get(722), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(722)).add(new Craft(ItemDatabase.find("마법 방어 투구"), 2, 7, 1));
		//
		craft_table_list.put(723, new Craft(ItemDatabase.find("신성한 마법 방어 투구"), 1, 7, 1));
		craft_table_needitem_list.put(craft_table_list.get(723), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(723)).add(new Craft(ItemDatabase.find("마법 방어 투구"), 2, 8, 1));
		//
		craft_table_list.put(724, new Craft(ItemDatabase.find("신성한 마법 방어 투구"), 1, 8, 1));
		craft_table_needitem_list.put(craft_table_list.get(724), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(724)).add(new Craft(ItemDatabase.find("마법 방어 투구"), 2, 9, 1));
		//
		craft_table_list.put(725, new Craft(ItemDatabase.find("신성한 마법 방어 투구"), 1, 9, 1));
		craft_table_needitem_list.put(craft_table_list.get(725), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(725)).add(new Craft(ItemDatabase.find("마법 방어 투구"), 2, 10, 1));
		//
		craft_table_list.put(823, new Craft(ItemDatabase.find("오만의 탑 2층 이동 주문서"), 1));
		craft_table_needitem_list.put(craft_table_list.get(823), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(823)).add(new Craft(ItemDatabase.find("오만의 탑 1층 이동 주문서"), 2));
		craft_table_needitem_list.get(craft_table_list.get(823)).add(new Craft(ItemDatabase.find("아데나"), 300));
		//
		craft_table_list.put(824, new Craft(ItemDatabase.find("오만의 탑 3층 이동 주문서"), 1));
		craft_table_needitem_list.put(craft_table_list.get(824), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(824)).add(new Craft(ItemDatabase.find("오만의 탑 2층 이동 주문서"), 2));
		craft_table_needitem_list.get(craft_table_list.get(824)).add(new Craft(ItemDatabase.find("아데나"), 300));
		//
		craft_table_list.put(825, new Craft(ItemDatabase.find("오만의 탑 4층 이동 주문서"), 1));
		craft_table_needitem_list.put(craft_table_list.get(825), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(825)).add(new Craft(ItemDatabase.find("오만의 탑 3층 이동 주문서"), 2));
		craft_table_needitem_list.get(craft_table_list.get(825)).add(new Craft(ItemDatabase.find("아데나"), 300));
		//
		craft_table_list.put(826, new Craft(ItemDatabase.find("오만의 탑 5층 이동 주문서"), 1));
		craft_table_needitem_list.put(craft_table_list.get(826), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(826)).add(new Craft(ItemDatabase.find("오만의 탑 4층 이동 주문서"), 2));
		craft_table_needitem_list.get(craft_table_list.get(826)).add(new Craft(ItemDatabase.find("아데나"), 300));
		//
		craft_table_list.put(827, new Craft(ItemDatabase.find("오만의 탑 6층 이동 주문서"), 1));
		craft_table_needitem_list.put(craft_table_list.get(827), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(827)).add(new Craft(ItemDatabase.find("오만의 탑 5층 이동 주문서"), 2));
		craft_table_needitem_list.get(craft_table_list.get(827)).add(new Craft(ItemDatabase.find("아데나"), 300));
		//
		craft_table_list.put(828, new Craft(ItemDatabase.find("오만의 탑 7층 이동 주문서"), 1));
		craft_table_needitem_list.put(craft_table_list.get(828), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(828)).add(new Craft(ItemDatabase.find("오만의 탑 6층 이동 주문서"), 2));
		craft_table_needitem_list.get(craft_table_list.get(828)).add(new Craft(ItemDatabase.find("아데나"), 300));
		//
		craft_table_list.put(829, new Craft(ItemDatabase.find("오만의 탑 8층 이동 주문서"), 1));
		craft_table_needitem_list.put(craft_table_list.get(829), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(829)).add(new Craft(ItemDatabase.find("오만의 탑 7층 이동 주문서"), 2));
		craft_table_needitem_list.get(craft_table_list.get(829)).add(new Craft(ItemDatabase.find("아데나"), 300));
		//
		craft_table_list.put(830, new Craft(ItemDatabase.find("오만의 탑 9층 이동 주문서"), 1));
		craft_table_needitem_list.put(craft_table_list.get(830), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(830)).add(new Craft(ItemDatabase.find("오만의 탑 8층 이동 주문서"), 2));
		craft_table_needitem_list.get(craft_table_list.get(830)).add(new Craft(ItemDatabase.find("아데나"), 300));
		//
		craft_table_list.put(831, new Craft(ItemDatabase.find("오만의 탑 10층 이동 주문서"), 1));
		craft_table_needitem_list.put(craft_table_list.get(831), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(831)).add(new Craft(ItemDatabase.find("오만의 탑 9층 이동 주문서"), 2));
		craft_table_needitem_list.get(craft_table_list.get(831)).add(new Craft(ItemDatabase.find("아데나"), 300));
		//
		craft_table_list.put(832, new Craft(ItemDatabase.find("오만의 탑 정상 이동 주문서"), 1));
		craft_table_needitem_list.put(craft_table_list.get(832), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(832)).add(new Craft(ItemDatabase.find("오만의 탑 10층 이동 주문서"), 2));
		craft_table_needitem_list.get(craft_table_list.get(832)).add(new Craft(ItemDatabase.find("아데나"), 300));
		//
		craft_table_list.put(833, new Craft(ItemDatabase.find("변이된 오만의 탑 1층 이동 부적"), 1));
		craft_table_needitem_list.put(craft_table_list.get(833), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(833)).add(new Craft(ItemDatabase.find("오만의 탑 1층 이동 주문서"), 1000));
		craft_table_needitem_list.get(craft_table_list.get(833)).add(new Craft(ItemDatabase.find("오만의 탑 1층 이동 부적"), 1));
		//
		craft_table_list.put(834, new Craft(ItemDatabase.find("변이된 오만의 탑 2층 이동 부적"), 1));
		craft_table_needitem_list.put(craft_table_list.get(834), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(834)).add(new Craft(ItemDatabase.find("오만의 탑 2층 이동 주문서"), 900));
		craft_table_needitem_list.get(craft_table_list.get(834)).add(new Craft(ItemDatabase.find("오만의 탑 2층 이동 부적"), 1));
		//
		craft_table_list.put(835, new Craft(ItemDatabase.find("변이된 오만의 탑 3층 이동 부적"), 1));
		craft_table_needitem_list.put(craft_table_list.get(835), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(835)).add(new Craft(ItemDatabase.find("오만의 탑 3층 이동 주문서"), 800));
		craft_table_needitem_list.get(craft_table_list.get(835)).add(new Craft(ItemDatabase.find("오만의 탑 3층 이동 부적"), 1));
		//
		craft_table_list.put(836, new Craft(ItemDatabase.find("변이된 오만의 탑 4층 이동 부적"), 1));
		craft_table_needitem_list.put(craft_table_list.get(836), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(836)).add(new Craft(ItemDatabase.find("오만의 탑 4층 이동 주문서"), 700));
		craft_table_needitem_list.get(craft_table_list.get(836)).add(new Craft(ItemDatabase.find("오만의 탑 4층 이동 부적"), 1));
		//
		craft_table_list.put(837, new Craft(ItemDatabase.find("변이된 오만의 탑 5층 이동 부적"), 1));
		craft_table_needitem_list.put(craft_table_list.get(837), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(837)).add(new Craft(ItemDatabase.find("오만의 탑 5층 이동 주문서"), 600));
		craft_table_needitem_list.get(craft_table_list.get(837)).add(new Craft(ItemDatabase.find("오만의 탑 5층 이동 부적"), 1));
		//
		craft_table_list.put(838, new Craft(ItemDatabase.find("변이된 오만의 탑 6층 이동 부적"), 1));
		craft_table_needitem_list.put(craft_table_list.get(838), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(838)).add(new Craft(ItemDatabase.find("오만의 탑 6층 이동 주문서"), 500));
		craft_table_needitem_list.get(craft_table_list.get(838)).add(new Craft(ItemDatabase.find("오만의 탑 6층 이동 부적"), 1));
		//
		craft_table_list.put(839, new Craft(ItemDatabase.find("변이된 오만의 탑 7층 이동 부적"), 1));
		craft_table_needitem_list.put(craft_table_list.get(839), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(839)).add(new Craft(ItemDatabase.find("오만의 탑 7층 이동 주문서"), 400));
		craft_table_needitem_list.get(craft_table_list.get(839)).add(new Craft(ItemDatabase.find("오만의 탑 7층 이동 부적"), 1));
		//
		craft_table_list.put(840, new Craft(ItemDatabase.find("변이된 오만의 탑 8층 이동 부적"), 1));
		craft_table_needitem_list.put(craft_table_list.get(840), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(840)).add(new Craft(ItemDatabase.find("오만의 탑 8층 이동 주문서"), 300));
		craft_table_needitem_list.get(craft_table_list.get(840)).add(new Craft(ItemDatabase.find("오만의 탑 8층 이동 부적"), 1));
		//
		craft_table_list.put(841, new Craft(ItemDatabase.find("변이된 오만의 탑 9층 이동 부적"), 1));
		craft_table_needitem_list.put(craft_table_list.get(841), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(841)).add(new Craft(ItemDatabase.find("오만의 탑 9층 이동 주문서"), 200));
		craft_table_needitem_list.get(craft_table_list.get(841)).add(new Craft(ItemDatabase.find("오만의 탑 9층 이동 부적"), 1));
		//
		craft_table_list.put(842, new Craft(ItemDatabase.find("변이된 오만의 탑 10층 이동 부적"), 1));
		craft_table_needitem_list.put(craft_table_list.get(842), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(842)).add(new Craft(ItemDatabase.find("오만의 탑 10층 이동 주문서"), 100));
		craft_table_needitem_list.get(craft_table_list.get(842)).add(new Craft(ItemDatabase.find("오만의 탑 10층 이동 부적"), 1));
		//
		craft_table_list.put(843, new Craft(ItemDatabase.find("혼돈의 오만의 탑 1층 이동 부적"), 1));
		craft_table_needitem_list.put(craft_table_list.get(843), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(843)).add(new Craft(ItemDatabase.find("봉인된 오만의 탑 1층 이동 부적"), 1));
		craft_table_needitem_list.get(craft_table_list.get(843)).add(new Craft(ItemDatabase.find("오만의 탑 1층 이동 부적"), 1));
		//
		craft_table_list.put(844, new Craft(ItemDatabase.find("혼돈의 오만의 탑 2층 이동 부적"), 1));
		craft_table_needitem_list.put(craft_table_list.get(844), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(844)).add(new Craft(ItemDatabase.find("봉인된 오만의 탑 2층 이동 부적"), 1));
		craft_table_needitem_list.get(craft_table_list.get(844)).add(new Craft(ItemDatabase.find("오만의 탑 2층 이동 부적"), 1));
		//
		craft_table_list.put(845, new Craft(ItemDatabase.find("혼돈의 오만의 탑 3층 이동 부적"), 1));
		craft_table_needitem_list.put(craft_table_list.get(845), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(845)).add(new Craft(ItemDatabase.find("봉인된 오만의 탑 3층 이동 부적"), 1));
		craft_table_needitem_list.get(craft_table_list.get(845)).add(new Craft(ItemDatabase.find("오만의 탑 3층 이동 부적"), 1));
		//
		craft_table_list.put(846, new Craft(ItemDatabase.find("혼돈의 오만의 탑 4층 이동 부적"), 1));
		craft_table_needitem_list.put(craft_table_list.get(846), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(846)).add(new Craft(ItemDatabase.find("봉인된 오만의 탑 4층 이동 부적"), 1));
		craft_table_needitem_list.get(craft_table_list.get(846)).add(new Craft(ItemDatabase.find("오만의 탑 4층 이동 부적"), 1));
		//
		craft_table_list.put(847, new Craft(ItemDatabase.find("혼돈의 오만의 탑 5층 이동 부적"), 1));
		craft_table_needitem_list.put(craft_table_list.get(847), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(847)).add(new Craft(ItemDatabase.find("봉인된 오만의 탑 5층 이동 부적"), 1));
		craft_table_needitem_list.get(craft_table_list.get(847)).add(new Craft(ItemDatabase.find("오만의 탑 5층 이동 부적"), 1));
		//
		craft_table_list.put(848, new Craft(ItemDatabase.find("혼돈의 오만의 탑 6층 이동 부적"), 1));
		craft_table_needitem_list.put(craft_table_list.get(848), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(848)).add(new Craft(ItemDatabase.find("봉인된 오만의 탑 6층 이동 부적"), 1));
		craft_table_needitem_list.get(craft_table_list.get(848)).add(new Craft(ItemDatabase.find("오만의 탑 6층 이동 부적"), 1));
		//
		craft_table_list.put(849, new Craft(ItemDatabase.find("혼돈의 오만의 탑 7층 이동 부적"), 1));
		craft_table_needitem_list.put(craft_table_list.get(849), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(849)).add(new Craft(ItemDatabase.find("봉인된 오만의 탑 7층 이동 부적"), 1));
		craft_table_needitem_list.get(craft_table_list.get(849)).add(new Craft(ItemDatabase.find("오만의 탑 7층 이동 부적"), 1));
		//
		craft_table_list.put(850, new Craft(ItemDatabase.find("혼돈의 오만의 탑 8층 이동 부적"), 1));
		craft_table_needitem_list.put(craft_table_list.get(850), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(850)).add(new Craft(ItemDatabase.find("봉인된 오만의 탑 8층 이동 부적"), 1));
		craft_table_needitem_list.get(craft_table_list.get(850)).add(new Craft(ItemDatabase.find("오만의 탑 8층 이동 부적"), 1));
		//
		craft_table_list.put(851, new Craft(ItemDatabase.find("혼돈의 오만의 탑 9층 이동 부적"), 1));
		craft_table_needitem_list.put(craft_table_list.get(851), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(851)).add(new Craft(ItemDatabase.find("봉인된 오만의 탑 9층 이동 부적"), 1));
		craft_table_needitem_list.get(craft_table_list.get(851)).add(new Craft(ItemDatabase.find("오만의 탑 9층 이동 부적"), 1));
		//
		craft_table_list.put(852, new Craft(ItemDatabase.find("혼돈의 오만의 탑 10층 이동 부적"), 1));
		craft_table_needitem_list.put(craft_table_list.get(852), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(852)).add(new Craft(ItemDatabase.find("봉인된 오만의 탑 10층 이동 부적"), 1));
		craft_table_needitem_list.get(craft_table_list.get(852)).add(new Craft(ItemDatabase.find("오만의 탑 10층 이동 부적"), 1));
		//
		craft_table_list.put(928, new Craft(ItemDatabase.find("룸티스의 붉은빛 귀걸이"), 1, 3, 0));
		craft_table_needitem_list.put(craft_table_list.get(928), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(928)).add(new Craft(ItemDatabase.find("룸티스의 붉은빛 귀걸이"), 2, 3, 1));
		//
		craft_table_list.put(929, new Craft(ItemDatabase.find("룸티스의 붉은빛 귀걸이"), 1, 4, 0));
		craft_table_needitem_list.put(craft_table_list.get(929), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(929)).add(new Craft(ItemDatabase.find("룸티스의 붉은빛 귀걸이"), 2, 4, 1));
		//
		craft_table_list.put(930, new Craft(ItemDatabase.find("룸티스의 붉은빛 귀걸이"), 1, 5, 0));
		craft_table_needitem_list.put(craft_table_list.get(930), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(930)).add(new Craft(ItemDatabase.find("룸티스의 붉은빛 귀걸이"), 2, 5, 1));
		//
		craft_table_list.put(931, new Craft(ItemDatabase.find("룸티스의 붉은빛 귀걸이"), 1, 6, 0));
		craft_table_needitem_list.put(craft_table_list.get(931), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(931)).add(new Craft(ItemDatabase.find("룸티스의 붉은빛 귀걸이"), 2, 6, 1));
		//
		craft_table_list.put(932, new Craft(ItemDatabase.find("룸티스의 붉은빛 귀걸이"), 1, 7, 0));
		craft_table_needitem_list.put(craft_table_list.get(932), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(932)).add(new Craft(ItemDatabase.find("룸티스의 붉은빛 귀걸이"), 2, 7, 1));
		//
		craft_table_list.put(933, new Craft(ItemDatabase.find("룸티스의 붉은빛 귀걸이"), 1, 8, 0));
		craft_table_needitem_list.put(craft_table_list.get(933), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(933)).add(new Craft(ItemDatabase.find("룸티스의 붉은빛 귀걸이"), 2, 8, 1));
		//
		craft_table_list.put(934, new Craft(ItemDatabase.find("룸티스의 푸른빛 귀걸이"), 1, 3, 0));
		craft_table_needitem_list.put(craft_table_list.get(934), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(934)).add(new Craft(ItemDatabase.find("룸티스의 푸른빛 귀걸이"), 2, 3, 1));
		//
		craft_table_list.put(935, new Craft(ItemDatabase.find("룸티스의 푸른빛 귀걸이"), 1, 4, 0));
		craft_table_needitem_list.put(craft_table_list.get(935), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(935)).add(new Craft(ItemDatabase.find("룸티스의 푸른빛 귀걸이"), 2, 4, 1));
		//
		craft_table_list.put(936, new Craft(ItemDatabase.find("룸티스의 푸른빛 귀걸이"), 1, 5, 0));
		craft_table_needitem_list.put(craft_table_list.get(936), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(936)).add(new Craft(ItemDatabase.find("룸티스의 푸른빛 귀걸이"), 2, 5, 1));
		//
		craft_table_list.put(937, new Craft(ItemDatabase.find("룸티스의 푸른빛 귀걸이"), 1, 6, 0));
		craft_table_needitem_list.put(craft_table_list.get(937), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(937)).add(new Craft(ItemDatabase.find("룸티스의 푸른빛 귀걸이"), 2, 6, 1));
		//
		craft_table_list.put(938, new Craft(ItemDatabase.find("룸티스의 푸른빛 귀걸이"), 1, 7, 0));
		craft_table_needitem_list.put(craft_table_list.get(938), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(938)).add(new Craft(ItemDatabase.find("룸티스의 푸른빛 귀걸이"), 2, 7, 1));
		//
		craft_table_list.put(939, new Craft(ItemDatabase.find("룸티스의 푸른빛 귀걸이"), 1, 8, 0));
		craft_table_needitem_list.put(craft_table_list.get(939), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(939)).add(new Craft(ItemDatabase.find("룸티스의 푸른빛 귀걸이"), 2, 8, 1));
		//
		craft_table_list.put(940, new Craft(ItemDatabase.find("룸티스의 보랏빛 귀걸이"), 1, 3, 0));
		craft_table_needitem_list.put(craft_table_list.get(940), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(940)).add(new Craft(ItemDatabase.find("룸티스의 보랏빛 귀걸이"), 2, 3, 1));
		//
		craft_table_list.put(941, new Craft(ItemDatabase.find("룸티스의 보랏빛 귀걸이"), 1, 4, 0));
		craft_table_needitem_list.put(craft_table_list.get(941), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(941)).add(new Craft(ItemDatabase.find("룸티스의 보랏빛 귀걸이"), 2, 4, 1));
		//
		craft_table_list.put(942, new Craft(ItemDatabase.find("룸티스의 보랏빛 귀걸이"), 1, 5, 0));
		craft_table_needitem_list.put(craft_table_list.get(942), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(942)).add(new Craft(ItemDatabase.find("룸티스의 보랏빛 귀걸이"), 2, 5, 1));
		//
		craft_table_list.put(943, new Craft(ItemDatabase.find("룸티스의 보랏빛 귀걸이"), 1, 6, 0));
		craft_table_needitem_list.put(craft_table_list.get(943), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(943)).add(new Craft(ItemDatabase.find("룸티스의 보랏빛 귀걸이"), 2, 6, 1));
		//
		craft_table_list.put(944, new Craft(ItemDatabase.find("룸티스의 보랏빛 귀걸이"), 1, 7, 0));
		craft_table_needitem_list.put(craft_table_list.get(944), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(944)).add(new Craft(ItemDatabase.find("룸티스의 보랏빛 귀걸이"), 2, 7, 1));
		//
		craft_table_list.put(945, new Craft(ItemDatabase.find("룸티스의 보랏빛 귀걸이"), 1, 8, 0));
		craft_table_needitem_list.put(craft_table_list.get(945), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(945)).add(new Craft(ItemDatabase.find("룸티스의 보랏빛 귀걸이"), 2, 8, 1));
		//
		craft_table_list.put(946, new Craft(ItemDatabase.find("스냅퍼의 체력 반지"), 1, 3, 0));
		craft_table_needitem_list.put(craft_table_list.get(946), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(946)).add(new Craft(ItemDatabase.find("스냅퍼의 체력 반지"), 2, 3, 1));
		//
		craft_table_list.put(947, new Craft(ItemDatabase.find("스냅퍼의 체력 반지"), 1, 4, 0));
		craft_table_needitem_list.put(craft_table_list.get(947), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(947)).add(new Craft(ItemDatabase.find("스냅퍼의 체력 반지"), 2, 4, 1));
		//
		craft_table_list.put(948, new Craft(ItemDatabase.find("스냅퍼의 체력 반지"), 1, 5, 0));
		craft_table_needitem_list.put(craft_table_list.get(948), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(948)).add(new Craft(ItemDatabase.find("스냅퍼의 체력 반지"), 2, 5, 1));
		//
		craft_table_list.put(949, new Craft(ItemDatabase.find("스냅퍼의 체력 반지"), 1, 6, 0));
		craft_table_needitem_list.put(craft_table_list.get(949), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(949)).add(new Craft(ItemDatabase.find("스냅퍼의 체력 반지"), 2, 6, 1));
		//
		craft_table_list.put(950, new Craft(ItemDatabase.find("스냅퍼의 체력 반지"), 1, 7, 0));
		craft_table_needitem_list.put(craft_table_list.get(950), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(950)).add(new Craft(ItemDatabase.find("스냅퍼의 체력 반지"), 2, 7, 1));
		//
		craft_table_list.put(951, new Craft(ItemDatabase.find("스냅퍼의 체력 반지"), 1, 8, 0));
		craft_table_needitem_list.put(craft_table_list.get(951), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(951)).add(new Craft(ItemDatabase.find("스냅퍼의 체력 반지"), 2, 8, 1));
		//
		craft_table_list.put(952, new Craft(ItemDatabase.find("스냅퍼의 마법저항 반지"), 1, 3, 0));
		craft_table_needitem_list.put(craft_table_list.get(952), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(952)).add(new Craft(ItemDatabase.find("스냅퍼의 마법저항 반지"), 2, 3, 1));
		//
		craft_table_list.put(953, new Craft(ItemDatabase.find("스냅퍼의 마법저항 반지"), 1, 4, 0));
		craft_table_needitem_list.put(craft_table_list.get(953), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(953)).add(new Craft(ItemDatabase.find("스냅퍼의 마법저항 반지"), 2, 4, 1));
		//
		craft_table_list.put(954, new Craft(ItemDatabase.find("스냅퍼의 마법저항 반지"), 1, 5, 0));
		craft_table_needitem_list.put(craft_table_list.get(954), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(954)).add(new Craft(ItemDatabase.find("스냅퍼의 마법저항 반지"), 2, 5, 1));
		//
		craft_table_list.put(955, new Craft(ItemDatabase.find("스냅퍼의 마법저항 반지"), 1, 6, 0));
		craft_table_needitem_list.put(craft_table_list.get(955), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(955)).add(new Craft(ItemDatabase.find("스냅퍼의 마법저항 반지"), 2, 6, 1));
		//
		craft_table_list.put(956, new Craft(ItemDatabase.find("스냅퍼의 마법저항 반지"), 1, 7, 0));
		craft_table_needitem_list.put(craft_table_list.get(956), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(956)).add(new Craft(ItemDatabase.find("스냅퍼의 마법저항 반지"), 2, 7, 1));
		//
		craft_table_list.put(957, new Craft(ItemDatabase.find("스냅퍼의 마법저항 반지"), 1, 8, 0));
		craft_table_needitem_list.put(craft_table_list.get(957), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(957)).add(new Craft(ItemDatabase.find("스냅퍼의 마법저항 반지"), 2, 8, 1));
		//
		craft_table_list.put(958, new Craft(ItemDatabase.find("스냅퍼의 집중 반지"), 1, 3, 0));
		craft_table_needitem_list.put(craft_table_list.get(958), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(958)).add(new Craft(ItemDatabase.find("스냅퍼의 집중 반지"), 2, 3, 1));
		//
		craft_table_list.put(959, new Craft(ItemDatabase.find("스냅퍼의 집중 반지"), 1, 4, 0));
		craft_table_needitem_list.put(craft_table_list.get(959), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(959)).add(new Craft(ItemDatabase.find("스냅퍼의 집중 반지"), 2, 4, 1));
		//
		craft_table_list.put(960, new Craft(ItemDatabase.find("스냅퍼의 집중 반지"), 1, 5, 0));
		craft_table_needitem_list.put(craft_table_list.get(960), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(960)).add(new Craft(ItemDatabase.find("스냅퍼의 집중 반지"), 2, 5, 1));
		//
		craft_table_list.put(961, new Craft(ItemDatabase.find("스냅퍼의 집중 반지"), 1, 6, 0));
		craft_table_needitem_list.put(craft_table_list.get(961), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(961)).add(new Craft(ItemDatabase.find("스냅퍼의 집중 반지"), 2, 6, 1));
		//
		craft_table_list.put(962, new Craft(ItemDatabase.find("스냅퍼의 집중 반지"), 1, 7, 0));
		craft_table_needitem_list.put(craft_table_list.get(962), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(962)).add(new Craft(ItemDatabase.find("스냅퍼의 집중 반지"), 2, 7, 1));
		//
		craft_table_list.put(963, new Craft(ItemDatabase.find("스냅퍼의 집중 반지"), 1, 8, 0));
		craft_table_needitem_list.put(craft_table_list.get(963), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(963)).add(new Craft(ItemDatabase.find("스냅퍼의 집중 반지"), 2, 8, 1));
		//
		craft_table_list.put(964, new Craft(ItemDatabase.find("스냅퍼의 마나 반지"), 1, 3, 0));
		craft_table_needitem_list.put(craft_table_list.get(964), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(964)).add(new Craft(ItemDatabase.find("스냅퍼의 마나 반지"), 2, 3, 1));
		//
		craft_table_list.put(965, new Craft(ItemDatabase.find("스냅퍼의 마나 반지"), 1, 4, 0));
		craft_table_needitem_list.put(craft_table_list.get(965), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(965)).add(new Craft(ItemDatabase.find("스냅퍼의 마나 반지"), 2, 4, 1));
		//
		craft_table_list.put(966, new Craft(ItemDatabase.find("스냅퍼의 마나 반지"), 1, 5, 0));
		craft_table_needitem_list.put(craft_table_list.get(966), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(966)).add(new Craft(ItemDatabase.find("스냅퍼의 마나 반지"), 2, 5, 1));
		//
		craft_table_list.put(967, new Craft(ItemDatabase.find("스냅퍼의 마나 반지"), 1, 6, 0));
		craft_table_needitem_list.put(craft_table_list.get(967), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(967)).add(new Craft(ItemDatabase.find("스냅퍼의 마나 반지"), 2, 6, 1));
		//
		craft_table_list.put(968, new Craft(ItemDatabase.find("스냅퍼의 마나 반지"), 1, 7, 0));
		craft_table_needitem_list.put(craft_table_list.get(968), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(968)).add(new Craft(ItemDatabase.find("스냅퍼의 마나 반지"), 2, 7, 1));
		//
		craft_table_list.put(969, new Craft(ItemDatabase.find("스냅퍼의 마나 반지"), 1, 8, 0));
		craft_table_needitem_list.put(craft_table_list.get(969), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(969)).add(new Craft(ItemDatabase.find("스냅퍼의 마나 반지"), 2, 8, 1));
		//
		craft_table_list.put(970, new Craft(ItemDatabase.find("스냅퍼의 회복 반지"), 1, 3, 0));
		craft_table_needitem_list.put(craft_table_list.get(970), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(970)).add(new Craft(ItemDatabase.find("스냅퍼의 회복 반지"), 2, 3, 1));
		//
		craft_table_list.put(971, new Craft(ItemDatabase.find("스냅퍼의 회복 반지"), 1, 4, 0));
		craft_table_needitem_list.put(craft_table_list.get(971), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(971)).add(new Craft(ItemDatabase.find("스냅퍼의 회복 반지"), 2, 4, 1));
		//
		craft_table_list.put(972, new Craft(ItemDatabase.find("스냅퍼의 회복 반지"), 1, 5, 0));
		craft_table_needitem_list.put(craft_table_list.get(972), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(972)).add(new Craft(ItemDatabase.find("스냅퍼의 회복 반지"), 2, 5, 1));
		//
		craft_table_list.put(973, new Craft(ItemDatabase.find("스냅퍼의 회복 반지"), 1, 6, 0));
		craft_table_needitem_list.put(craft_table_list.get(973), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(973)).add(new Craft(ItemDatabase.find("스냅퍼의 회복 반지"), 2, 6, 1));
		//
		craft_table_list.put(974, new Craft(ItemDatabase.find("스냅퍼의 회복 반지"), 1, 7, 0));
		craft_table_needitem_list.put(craft_table_list.get(974), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(974)).add(new Craft(ItemDatabase.find("스냅퍼의 회복 반지"), 2, 7, 1));
		//
		craft_table_list.put(975, new Craft(ItemDatabase.find("스냅퍼의 회복 반지"), 1, 8, 0));
		craft_table_needitem_list.put(craft_table_list.get(975), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(975)).add(new Craft(ItemDatabase.find("스냅퍼의 회복 반지"), 2, 8, 1));
		//
		craft_table_list.put(976, new Craft(ItemDatabase.find("스냅퍼의 지혜 반지"), 1, 3, 0));
		craft_table_needitem_list.put(craft_table_list.get(976), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(976)).add(new Craft(ItemDatabase.find("스냅퍼의 지혜 반지"), 2, 3, 1));
		//
		craft_table_list.put(977, new Craft(ItemDatabase.find("스냅퍼의 지혜 반지"), 1, 4, 0));
		craft_table_needitem_list.put(craft_table_list.get(977), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(977)).add(new Craft(ItemDatabase.find("스냅퍼의 지혜 반지"), 2, 4, 1));
		//
		craft_table_list.put(978, new Craft(ItemDatabase.find("스냅퍼의 지혜 반지"), 1, 5, 0));
		craft_table_needitem_list.put(craft_table_list.get(978), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(978)).add(new Craft(ItemDatabase.find("스냅퍼의 지혜 반지"), 2, 5, 1));
		//
		craft_table_list.put(979, new Craft(ItemDatabase.find("스냅퍼의 지혜 반지"), 1, 6, 0));
		craft_table_needitem_list.put(craft_table_list.get(979), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(979)).add(new Craft(ItemDatabase.find("스냅퍼의 지혜 반지"), 2, 6, 1));
		//
		craft_table_list.put(980, new Craft(ItemDatabase.find("스냅퍼의 지혜 반지"), 1, 7, 0));
		craft_table_needitem_list.put(craft_table_list.get(980), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(980)).add(new Craft(ItemDatabase.find("스냅퍼의 지혜 반지"), 2, 7, 1));
		//
		craft_table_list.put(981, new Craft(ItemDatabase.find("스냅퍼의 지혜 반지"), 1, 8, 0));
		craft_table_needitem_list.put(craft_table_list.get(981), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(981)).add(new Craft(ItemDatabase.find("스냅퍼의 지혜 반지"), 2, 8, 1));
		//
		craft_table_list.put(982, new Craft(ItemDatabase.find("스냅퍼의 용사 반지"), 1, 3, 0));
		craft_table_needitem_list.put(craft_table_list.get(982), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(982)).add(new Craft(ItemDatabase.find("스냅퍼의 용사 반지"), 2, 3, 1));
		//
		craft_table_list.put(983, new Craft(ItemDatabase.find("스냅퍼의 용사 반지"), 1, 4, 0));
		craft_table_needitem_list.put(craft_table_list.get(983), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(983)).add(new Craft(ItemDatabase.find("스냅퍼의 용사 반지"), 2, 4, 1));
		//
		craft_table_list.put(984, new Craft(ItemDatabase.find("스냅퍼의 용사 반지"), 1, 5, 0));
		craft_table_needitem_list.put(craft_table_list.get(984), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(984)).add(new Craft(ItemDatabase.find("스냅퍼의 용사 반지"), 2, 5, 1));
		//
		craft_table_list.put(985, new Craft(ItemDatabase.find("스냅퍼의 용사 반지"), 1, 6, 0));
		craft_table_needitem_list.put(craft_table_list.get(985), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(985)).add(new Craft(ItemDatabase.find("스냅퍼의 용사 반지"), 2, 6, 1));
		//
		craft_table_list.put(986, new Craft(ItemDatabase.find("스냅퍼의 용사 반지"), 1, 7, 0));
		craft_table_needitem_list.put(craft_table_list.get(986), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(986)).add(new Craft(ItemDatabase.find("스냅퍼의 용사 반지"), 2, 7, 1));
		//
		craft_table_list.put(987, new Craft(ItemDatabase.find("스냅퍼의 용사 반지"), 1, 8, 0));
		craft_table_needitem_list.put(craft_table_list.get(987), new ArrayList<Craft>());
		craft_table_needitem_list.get(craft_table_list.get(987)).add(new Craft(ItemDatabase.find("스냅퍼의 용사 반지"), 2, 8, 1));
		
		TimeLine.end();
	}

	/**
	 * 제작테이블 전체 목록을 요청한 클라에게 전송처리하는 메서드.
	 * @param pc
	 * @param cbp
	 * @param type
	 */
	static public void toCraftTableData(PcInstance pc, ClientBasePacket cbp, int type) {
		// craftinfo.dat 파일 분석이 필요함.
		cbp.readH();
		pc.toSender(S_CraftTable.clone(BasePacketPooling.getPool(S_CraftTable.class), type+1, cbp.readC()));
	}
	
	/**
	 * 제작테이블과 연결된 npc에 제작가능한 목록을 전송처리하는 메서드.
	 * @param pc
	 * @param cbp
	 * @param type
	 */
	static public void toCraftTableList(PcInstance pc, ClientBasePacket cbp, int type) {
		if(pc.temp_object_1==null || !Util.isDistance(pc, pc.temp_object_1, Lineage.SEARCH_LOCATIONRANGE/2) || !(pc.temp_object_1 instanceof CraftTable))
			// npc와 거리가 멀다는 메세지 출력.
			pc.toSender(S_CraftTable.clone(BasePacketPooling.getPool(S_CraftTable.class), type+1, false));
		else
			// 제작창 띄우리 처리.
			pc.toSender(S_CraftTable.clone(BasePacketPooling.getPool(S_CraftTable.class), type+1, true, pc.temp_object_1));
	}
	
	/**
	 * 제작테이블과 연결된 npc에 제작아이템을 선택 요청 처리하는 메서드.
	 * @param pc
	 * @param cbp
	 * @param type
	 */
	static public void toCraftTableSubmit(PcInstance pc, ClientBasePacket cbp, int type) {
		//
		cbp.readC();
		long npcObjectId = cbp.read4(cbp.read4size());
		cbp.readC();
		int craftNumber = cbp.read4(cbp.read4size());
		cbp.readC();
		int count = cbp.read4(cbp.read4size());
		// 버그 확인.
		if (count < 1 || count > 100)
			pc.toSender(S_CraftTable.clone(BasePacketPooling.getPool(S_CraftTable.class), 0x3b));
		else if(pc.temp_object_1==null || !Util.isDistance(pc, pc.temp_object_1, Lineage.SEARCH_LOCATIONRANGE/2) || !(pc.temp_object_1 instanceof CraftTable) || pc.temp_object_1.getObjectId()!=npcObjectId)
			pc.toSender(S_CraftTable.clone(BasePacketPooling.getPool(S_CraftTable.class), 0x39, false));
		else {
			Craft item = craft_table_list.get(craftNumber);
			if(item == null)
				ChattingController.toChatting(pc, "제작할 수 없습니다.", 20);
			else {
				List<Craft> need_item = craft_table_needitem_list.get(item);
				if(need_item == null)
					ChattingController.toChatting(pc, "제작할 수 없습니다.", 20);
				else {
					int max_count = CraftController.getMax(pc, need_item);
					if(max_count<=0 || count>max_count || item.getItem()==null)
						ChattingController.toChatting(pc, "제작할 수 없습니다.", 20);
					else {
						// 재료 제거
						for(int i=0 ; i<count ; ++i)
							CraftController.toCraft(pc, need_item);
						// 제작 아이템 지급.
						CraftController.toCraft(pc.temp_object_1, pc, item.getItem(), count*item.getCount(), true, item.getEn(), 0, item.getBless());
					}
					pc.toSender(S_CraftTable.clone(BasePacketPooling.getPool(S_CraftTable.class), 0x3b));
				}
			}
		}
	}
	
}
