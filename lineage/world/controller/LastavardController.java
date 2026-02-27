package lineage.world.controller;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.lineage.Lastavard;
import lineage.bean.lineage.LastavardRaid;
import lineage.database.BackgroundDatabase;
import lineage.database.MonsterDatabase;
import lineage.database.MonsterSpawnlistDatabase;
import lineage.share.TimeLine;
import lineage.world.object.monster.LastavardBoss;
import lineage.world.object.monster.LastavardDoorMan;
import lineage.world.object.npc.background.door.LastavardDoor;

public class LastavardController {

	static private List<Lastavard> list;	// 관리중인 라스타바드 목록.
	
	static public void init() {
		TimeLine.start("LastavardController..");

		try {
			// http://lincom.whis.co.kr/?c=14/51&mod=spr&p=66
			list = new ArrayList<Lastavard>();
			// 1층 집회장 9시 문지기
			Lastavard lastavard = new Lastavard(451);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4735, null), 32785, 32809, lastavard.getMap(), 4, 4735);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32785, 32813, 4);
			synchronized (list) {
				list.add(lastavard);
			}
			// 1층 집회장 12시 문지기
			lastavard = new Lastavard(451);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4711, null), 32813, 32833, lastavard.getMap(), 6, 4711);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32809, 32832, 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 1층 돌격대 훈련장
			lastavard = new Lastavard(452);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4711, null), 32811, 32836, lastavard.getMap(), 6, 4711);
			lastavard.appendBoss((LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("여단장 다크펜서"), true), 32784, 32836, lastavard.getMap(), 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 1층 마수군왕의 집무실
			lastavard = new Lastavard(453);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4734, null), 32741, 32851, lastavard.getMap(), 6, 4734);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32739, 32850, 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 1층 마수군왕의 집무실 12시 문지기
			lastavard = new Lastavard(453);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4711, null), 32793, 32852, lastavard.getMap(), 6, 4711);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32790, 32852, 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 1층 마수 소환실
			lastavard = new Lastavard(456);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4734, null), 32792, 32795, lastavard.getMap(), 6, 4734);
			lastavard.appendBoss((LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("신관장 바운티"), true), 32761, 32820, lastavard.getMap(), 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 1층 마수군왕의 집무실 9시 문지기
			lastavard = new Lastavard(453);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4713, null), 32768, 32830, lastavard.getMap(), 4, 4713);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32765, 32833, 4);
			synchronized (list) {
				list.add(lastavard);
			}
			// 1층 야수 조련실
			lastavard = new Lastavard(454);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4735, null), 32749, 32855, lastavard.getMap(), 4, 4735);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32749, 32858, 4);
			synchronized (list) {
				list.add(lastavard);
			}
			// 1층 야수 조련실 12시 문지기
			lastavard = new Lastavard(454);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4711, null), 32813, 32822, lastavard.getMap(), 6, 4711);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32811, 32821, 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 1층 야수 훈련장
			lastavard = new Lastavard(455);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4734, null), 32801, 32862, lastavard.getMap(), 6, 4734);
			lastavard.appendBoss((LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("마수단장 카이바르"), true), 32758, 32823, lastavard.getMap(), 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 1층 마수군왕의 집무실 11시 문지기
			lastavard = new Lastavard(453);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4734, null), 32807, 32832, lastavard.getMap(), 6, 4734);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32805, 32831, 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 1층 마수군왕의 집무실
			lastavard = new Lastavard(453);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4734, null), 32749, 32736, lastavard.getMap(), 6, 4734);
			lastavard.appendBoss((LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("마수군왕 바란카"), true), 32838, 32759, lastavard.getMap(), 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 지하통로
			lastavard = new Lastavard(491);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4734, null), 32711, 32860, lastavard.getMap(), 6, 4734);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32709, 32859, 6);
			synchronized (list) {
				list.add(lastavard);
			}
			lastavard = new Lastavard(491);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4711, null), 32733, 32860, lastavard.getMap(), 6, 4711);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32730, 32860, 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 지하 결투장
			lastavard = new Lastavard(495);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4713, null), 32773, 32785, lastavard.getMap(), 6, 4713);
			lastavard.appendBoss((LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("암살단장 블레이즈"), true), 32762, 32845, lastavard.getMap(), 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 지하 통제실
			lastavard = new Lastavard(493);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4713, null), 32746, 32723, lastavard.getMap(), 4, 4713);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32746, 32726, 4);
			synchronized (list) {
				list.add(lastavard);
			}
			// 지하 훈련장
			lastavard = new Lastavard(490);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4711, null), 32731, 32810, lastavard.getMap(), 6, 4711);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32683, 32830, 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 암살군왕의 집무실
			lastavard = new Lastavard(492);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4735, null), 32843, 32847, lastavard.getMap(), 4, 4735);
			lastavard.appendBoss((LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("암살군왕 슬레이브"), true), 32833, 32819, lastavard.getMap(), 4);
			synchronized (list) {
				list.add(lastavard);
			}
			// 2층 정령 소환실
			lastavard = new Lastavard(464);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4711, null), 32806, 32829, lastavard.getMap(), 6, 4711);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32803, 32829, 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 2층 정령 서식지
			lastavard = new Lastavard(465);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4711, null), 32811, 32808, lastavard.getMap(), 6, 4711);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32790, 32810, 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 2층 암흑정령 연구실
			lastavard = new Lastavard(466);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4734, null), 32792, 32835, lastavard.getMap(), 6, 4734);
			lastavard.appendBoss((LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("신관장 바운티"), true), 32771, 32828, lastavard.getMap(), 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 2층 흑마법 수련장
			lastavard = new Lastavard(460);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4711, null), 32814, 32819, lastavard.getMap(), 6, 4711);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32748, 32858, 4);
			synchronized (list) {
				list.add(lastavard);
			}
			// 2층 흑마법 연구실
			lastavard = new Lastavard(461);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4734, null), 32769, 32803, lastavard.getMap(), 6, 4734);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32748, 32858, 4);
			synchronized (list) {
				list.add(lastavard);
			}
			lastavard = new Lastavard(461);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4735, null), 32782, 32812, lastavard.getMap(), 4, 4735);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32746, 32806, 4);
			synchronized (list) {
				list.add(lastavard);
			}
			lastavard = new Lastavard(461);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4735, null), 32688, 32837, lastavard.getMap(), 4, 4735);
			lastavard.appendBoss((LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("마법단장 카르미엘"), true), 32841, 32820, lastavard.getMap(), 6);
			synchronized (list) {
				list.add(lastavard);
			}
			lastavard = new Lastavard(461);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4711, null), 32695, 32797, lastavard.getMap(), 6, 4711);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32691, 32797, 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 2층 마령군왕의 집무실
			lastavard = new Lastavard(462);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4711, null), 32813, 32865, lastavard.getMap(), 6, 4711);
			lastavard.appendBoss((LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("마령군왕 라이아"), true), 32804, 32838, lastavard.getMap(), 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 2층 마령군왕의 서재
			lastavard = new Lastavard(463);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4735, null), 32734, 32854, lastavard.getMap(), 6, 4734);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32731, 32853, 6);
			synchronized (list) {
				list.add(lastavard);
			}
			lastavard = new Lastavard(463);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4735, null), 32783, 32815, lastavard.getMap(), 6, 4734);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32781, 32814, 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 3층 용병 훈련장
			lastavard = new Lastavard(472);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4735, null), 32748, 32806, lastavard.getMap(), 6, 4734);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32745, 32804, 6);
			synchronized (list) {
				list.add(lastavard);
			}
			lastavard = new Lastavard(472);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4711, null), 32808, 32799, lastavard.getMap(), 6, 4711);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32806, 32798, 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 3층 데빌로드 용병실
			lastavard = new Lastavard(477);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4735, null), 32804, 32857, lastavard.getMap(), 4, 4735);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32805, 32859, 4);
			synchronized (list) {
				list.add(lastavard);
			}
			lastavard = new Lastavard(477);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4732, null), 32772, 32807, lastavard.getMap(), 6, 4732);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32776, 32807, 2);
			synchronized (list) {
				list.add(lastavard);
			}
			lastavard = new Lastavard(477);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4713, null), 32746, 32788, lastavard.getMap(), 6, 4713);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32746, 32790, 4);
			synchronized (list) {
				list.add(lastavard);
			}
			// 3층 데빌로드 제단
			lastavard = new Lastavard(471);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4735, null), 32767, 32851, lastavard.getMap(), 4, 4735);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32768, 32853, 4);
			synchronized (list) {
				list.add(lastavard);
			}
			lastavard = new Lastavard(471);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4713, null), 32783, 32786, lastavard.getMap(), 4, 4713);
			lastavard.appendBoss((LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("용병대장 메파이스토"), true), 32790, 32816, lastavard.getMap(), 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 3층 악령제단
			lastavard = new Lastavard(470);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4734, null), 32751, 32839, lastavard.getMap(), 6, 4734);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32749, 32838, 6);
			synchronized (list) {
				list.add(lastavard);
			}
			lastavard = new Lastavard(470);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4713, null), 32817, 32806, lastavard.getMap(), 4, 4713);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32865, 32843, 5);
			synchronized (list) {
				list.add(lastavard);
			}
			// 3층 명법군의 훈련장
			lastavard = new Lastavard(473);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4734, null), 32794, 32832, lastavard.getMap(), 6, 4734);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32791, 32831, 6);
			synchronized (list) {
				list.add(lastavard);
			}
			lastavard = new Lastavard(473);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4732, null), 32779, 32805, lastavard.getMap(), 4, 4732);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32784, 32804, 2);
			synchronized (list) {
				list.add(lastavard);
			}
			lastavard = new Lastavard(473);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4734, null), 32885, 32816, lastavard.getMap(), 4, 4734);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32738, 32827, 4);
			synchronized (list) {
				list.add(lastavard);
			}
			lastavard = new Lastavard(473);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4713, null), 32833, 32792, lastavard.getMap(), 4, 4713);
			lastavard.appendBoss((LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("명법단장 크리퍼스"), true), 32922, 32847, lastavard.getMap(), 4);
			synchronized (list) {
				list.add(lastavard);
			}
			// 3층 통제구역
			lastavard = new Lastavard(478);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4711, null), 32750, 32812, lastavard.getMap(), 6, 4711);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32745, 32812, 6);
			synchronized (list) {
				list.add(lastavard);
			}
			lastavard = new Lastavard(478);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4713, null), 32735, 32786, lastavard.getMap(), 4, 4713);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32733, 32788, 4);
			synchronized (list) {
				list.add(lastavard);
			}
			// 3층 중앙 통제실
			lastavard = new Lastavard(476);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4735, null), 32780, 32850, lastavard.getMap(), 4, 4735);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32780, 32852, 4);
			synchronized (list) {
				list.add(lastavard);
			}
			lastavard = new Lastavard(476);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4711, null), 32805, 32800, lastavard.getMap(), 4, 4711);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32801, 32800, 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 3층 명법군왕의 집무실
			lastavard = new Lastavard(475);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4734, null), 32705, 32849, lastavard.getMap(), 6, 4734);
			lastavard.appendDoorMan((LastavardDoorMan)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 친위대")), 32702, 32851, 6);
			synchronized (list) {
				list.add(lastavard);
			}
			lastavard = new Lastavard(475);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4713, null), 32780, 32807, lastavard.getMap(), 4, 4713);
			lastavard.appendBoss((LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("명법군왕 헬바인"), true), 32781, 32843, lastavard.getMap(), 6);
			synchronized (list) {
				list.add(lastavard);
			}
			// 4층 케이나 집무실
			LastavardRaid lastavard_raid = new LastavardRaid(530);
			lastavard_raid.appendBoss((LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("대법관 케이나"), true), 32863, 32839, lastavard_raid.getMap(), 6);
			lastavard_raid.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4711, null), 32877, 32817, lastavard_raid.getMap(), 6, 4711);
			synchronized (list) {
				list.add(lastavard_raid);
			}
			// 
			LastavardBoss key = (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("대법관 비아타스"), true);
			lastavard_raid.appendBoss(key, 32757, 32739, 531, 4);
			lastavard_raid.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4711, null), 32789, 32738, 531, 6, 4711);
			for(int i=0 ; i<7 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("팬텀 나이트"), true), 32753, 32742+i, 531, 0);
			for(int i=0 ; i<7 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 조련사"), true), 32755, 32742+i, 531, 0);
			for(int i=0 ; i<7 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("핏빛 기사"), true), 32757, 32742+i, 531, 0);
			for(int i=0 ; i<7 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("어둠의 복수자"), true), 32759, 32742+i, 531, 0);
			//
			key = (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("대법관 바로메스"), true);
			lastavard_raid.appendBoss(key, 32792, 32789, 531, 0);
			lastavard_raid.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4735, null), 32774, 32815, 531, 4, 4735);
			for(int i=0 ; i<7 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 조련사"), true), 32790+i, 32780, 531, 4);
			for(int i=0 ; i<7 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 근위대"), true), 32787, 32783+i, 531, 2);
			for(int i=0 ; i<7 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 근위대1"), true), 32798, 32783+i, 531, 6);
			for(int i=0 ; i<7 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 조련사1"), true), 32789+i, 32792, 531, 0);
			//
			key = (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("대법관 엔디아스"), true);
			lastavard_raid.appendBoss(key, 32845, 32856, 531, 6);
			lastavard_raid.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4711, null), 32851, 32872, 531, 6, 4711);
			for(int i=0 ; i<14 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 조련사"), true), 32841-i, 32855, 531, 2);
			for(int i=0 ; i<14 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("팬텀 나이트"), true), 32841-i, 32857, 531, 2);
			for(int i=0 ; i<14 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 근위대"), true), 32841-i, 32859, 531, 2);
			// 4층 이데아 집무실
			lastavard_raid = new LastavardRaid(532);
			key = (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("대법관 이데아"), true);
			lastavard_raid.appendBoss(key, 32790, 32811, lastavard_raid.getMap(), 6);
			lastavard_raid.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4713, null), 32777, 32791, lastavard_raid.getMap(), 4, 4713);
			for(int i=0 ; i<7 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 조련사"), true), 32787-i, 32808, lastavard_raid.getMap(), 2);
			for(int i=0 ; i<7 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 근위대"), true), 32787-i, 32810, lastavard_raid.getMap(), 2);
			for(int i=0 ; i<7 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 근위대1"), true), 32787-i, 32812, lastavard_raid.getMap(), 2);
			for(int i=0 ; i<7 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 근위대2"), true), 32787-i, 32814, lastavard_raid.getMap(), 2);
			synchronized (list) {
				list.add(lastavard_raid);
			}
			//
			key = (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("대법관 티아메스"), true);
			lastavard_raid.appendBoss(key, 32871, 32896, 533, 6);
			lastavard_raid.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4735, null), 32824, 32885, 533, 4, 4735);
			for(int i=0 ; i<7 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 조련사"), true), 32860-i, 32900, 533, 2);
			for(int i=0 ; i<7 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 조련사"), true), 32860-i, 32898, 533, 2);
			for(int i=0 ; i<7 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("핏빛 기사"), true), 32860-i, 32896, 533, 2);
			for(int i=0 ; i<7 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("핏빛 기사"), true), 32860-i, 32894, 533, 2);
			//
			key = (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("대법관 라미아스"), true);
			lastavard_raid.appendBoss(key, 32786, 32891, 533, 6);
			lastavard_raid.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4735, null), 32765, 32856, 533, 4, 4735);
			for(int i=0 ; i<9 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 조련사"), true), 32770, 32888+i, 533, 2);
			for(int i=0 ; i<7 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 근위대1"), true), 32774+i, 32896, 533, 0);
			for(int i=0 ; i<7 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 근위대2"), true), 32774+i, 32887, 533, 4);
			for(int i=0 ; i<9 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 조련사1"), true), 32768, 32888+i, 533, 2);
			//
			key = (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("대법관 바로드"), true);
			lastavard_raid.appendBoss(key, 32754, 32798, 533, 4);
			lastavard_raid.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4713, null), 32727, 32797, 533, 4, 4713);
			for(int i=0 ; i<10 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 조련사"), true), 32749+i, 32802, 533, 0);
			for(int i=0 ; i<10 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 근위대1"), true), 32749+i, 32804, 533, 0);
			for(int i=0 ; i<10 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 근위대2"), true), 32749+i, 32806, 533, 0);
			for(int i=0 ; i<10 ; ++i)
				lastavard_raid.appendGroupList(key, (LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("라스타바드 조련사1"), true), 32749+i, 32808, 533, 0);
			// 4층 장로 회의장
			lastavard = new Lastavard(534);
			lastavard.appendDoor((LastavardDoor)BackgroundDatabase.toObject(null, 4734, null), 32879, 32842, lastavard.getMap(), 6, 4734);
			lastavard.appendBoss((LastavardBoss)MonsterSpawnlistDatabase.newInstance(MonsterDatabase.find("부제사장 카산드라"), true), 32865, 32821, lastavard.getMap(), 6);
			synchronized (list) {
				list.add(lastavard);
			}
		} catch (Exception e) {
		}
		
		// 스폰
		synchronized (list) {
			for(Lastavard l : list)
				l.init();
		}
		
		TimeLine.end();
	}
	
	static public void close() {
		synchronized (list) {
			for(Lastavard l : list)
				l.close(true);
			list.clear();
		}
	}
	
	static public void toTimer(long time) {
		synchronized (list) {
			for(Lastavard lastavard : list)
				try {
					lastavard.toTimer(time);
				} catch (Exception e) {
					System.out.println("toTimer(long time) : " + e);
				}
		}
	}
	
}
