package lineage.network.packet.client;

import java.sql.Connection;

import lineage.database.AccountDatabase;
import lineage.database.CharactersDatabase;
import lineage.database.DatabaseConnection;
import lineage.database.PolyDatabase;
import lineage.database.WarehouseDatabase;
import lineage.network.packet.BasePacket;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_Disconnect;
import lineage.network.packet.server.S_Message;
import lineage.plugin.PluginController;
import lineage.share.Lineage;
import lineage.world.World;
import lineage.world.controller.AgitController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.ClanController;
import lineage.world.controller.FishingController;
import lineage.world.controller.PartyController;
import lineage.world.controller.SummonController;
import lineage.world.controller.TradeController;
import lineage.world.controller.GiranClanLordController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.sp.CharacterSaveMarbles;
import lineage.world.object.magic.ShapeChange;
import lineage.world.object.npc.GoddessAgata;

public class C_Ask extends ClientBasePacket {
	
	static synchronized public BasePacket clone(BasePacket bp, byte[] data, int length){
		if(bp == null)
			bp = new C_Ask(data, length);
		else
			((C_Ask)bp).clone(data, length);
		return bp;
	}
	
	public C_Ask(byte[] data, int length){
		clone(data, length);
	}
	
	@Override
	public BasePacket init(PcInstance pc){
		// 버그 방지.
		if(!isRead(3) || pc==null || pc.isWorldDelete())
			return this;
		int type = readH();	// 구분자
		if(Lineage.server_version >= 360) {
			if (type != 479) {
				readD();
				type = readH();
				if (type == 0xffff)
					type = 622;
			}
		}
		int yn = readC();	// 승낙 여부
		
		switch(type){
			case 97:	// 가입
				ClanController.toJoinFinal(pc, yn==1);
				break;
			case 180:	// 변신
				String name = readS();
				if(name!=null && name.length()>0)
					// 시전 처리.
					ShapeChange.init(pc, pc, PolyDatabase.getPolyName(name), 7200, 1);
				break;
			case 217:	// 전쟁 선포 혈전 부분
				ClanController.toWarFinal(pc, yn==1);
				break;
			case 221:	// 항복 요청
				ClanController.toWarSubmissionFinal(pc, yn==1);
				break;
			case 252:	// 트레이드
				TradeController.toTrade(pc, yn==1);
				break;
			case 321:	// 부활 여부
				if(yn==1)
					pc.toRevivalFinal(null);
				break;
			case 325:	// 펫 이름 바꾸기
				if(pc.getSummon().getTempSi()==null || !pc.getSummon().getTempSi().getName().startsWith("$")){
					pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 326));
				}else{
					SummonController.updateName(pc.getSummon().getTempSi(), readS());
				}
				break;
			case 102:	// 파티
				PartyController.toParty(pc, yn==1, true);
				break;
			case 422:	// 파티
				PartyController.toParty(pc, yn==1, false);
				break;
			case 479:	// 새로운 스탯 추가
				boolean base = pc.toBaseStat(false);
				boolean lv = pc.toLvStat(false);
				boolean el = pc.toLvStatElixir(false);
				if(base || lv){// ){
					String stat = readS();
					if(PluginController.init(C_Ask.class, "init.479", pc, stat, base, lv) == null) {
						if(stat.equalsIgnoreCase("str")){
							if(base || pc.getStr()+pc.getLvStr()+pc.getElixirStr()<Lineage.stat_max) {
								if(base) {
									if(pc.getStr() < 20)
										pc.setStr(pc.getStr()+1);
									else
										ChattingController.toChatting(pc, "STR 베이스 능력치 최대값은 20 입니다. 다른 능력치를 선택해 주세요.", 20);
								} else {
									if(pc.getElixirStat() < pc.getElixirReset() )
										pc.setElixirStr(pc.getElixirStr()+1);
									else
										pc.setLvStr(pc.getLvStr()+1);
								}
							} else
								// \f1한 능력치의 최대값은 25 입니다. 다른 능력치를 선택해 주세요.
								ChattingController.toChatting(pc, String.format("한 능력치의 최대값은 %d 입니다. 다른 능력치를 선택해 주세요.", Lineage.stat_max), 20);
	//							pc.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 481));
						}else if(stat.equalsIgnoreCase("dex")){
							if(base || pc.getDex()+pc.getLvDex()+pc.getElixirDex()<Lineage.stat_max) {
								if(base) {
									if(pc.getDex() < 18)
										pc.setDex(pc.getDex()+1);
									else
										ChattingController.toChatting(pc, "DEX 베이스 능력치 최대값은 18 입니다. 다른 능력치를 선택해 주세요.", 20);
								} else {
									if(pc.getElixirStat() < pc.getElixirReset())
										pc.setElixirDex(pc.getElixirDex()+1);
									else
										pc.setLvDex(pc.getLvDex()+1);
								}
							} else
								ChattingController.toChatting(pc, String.format("한 능력치의 최대값은 %d 입니다. 다른 능력치를 선택해 주세요.", Lineage.stat_max), 20);
						}else if(stat.equalsIgnoreCase("con")){
							if(base || pc.getCon()+pc.getLvCon()+pc.getElixirCon()<25) {
								if(base) {
									if(pc.getCon() < 18)
										pc.setCon(pc.getCon()+1);
									else
										ChattingController.toChatting(pc, "CON 베이스 능력치 최대값은 18 입니다. 다른 능력치를 선택해 주세요.", 20);
								} else {
									if(pc.getElixirStat() < pc.getElixirReset())
										pc.setElixirCon(pc.getElixirCon()+1);
									else
										pc.setLvCon(pc.getLvCon()+1);
									
									/*Character cha = (Character)pc;
									switch(pc.getClassType()){
										case 0x00://군
											int hp1 = cha.getMaxHp()+ 47;
											cha.setMaxHp(hp1);
										break;
										case 0x01://기
											int hp2 = cha.getMaxHp()+ 53;
											cha.setMaxHp(hp2);
										break;
										case 0x02://요
											int hp3 = cha.getMaxHp()+ 38;
											cha.setMaxHp(hp3);
										break;
										case 0x03://법
											int hp4 = cha.getMaxHp()+ 28;
											cha.setMaxHp(hp4);
										break;
										case 0x04://다크
											int hp5 = cha.getMaxHp()+ 28;
											cha.setMaxHp(hp5);
										break;
										case 0x05://용기사
											int hp6 = cha.getMaxHp()+ 43;
											cha.setMaxHp(hp6);
										break;
									}*/
								}
							} else
								ChattingController.toChatting(pc, String.format("한 능력치의 최대값은 %d 입니다. 다른 능력치를 선택해 주세요.", Lineage.stat_max), 20);
						}else if(stat.equalsIgnoreCase("cha")){
							if(base || pc.getCha()+pc.getLvCha()+pc.getElixirCha()< Lineage.stat_max){//Lineage.stat_max) {
								if(base) {
									if(pc.getCha() < 18)
										pc.setCha(pc.getCha()+1);
									else
										ChattingController.toChatting(pc, "CHA 베이스 능력치 최대값은 18 입니다. 다른 능력치를 선택해 주세요.", 20);
								} else {
									if(pc.getElixirStat() < pc.getElixirReset() )
										pc.setElixirCha(pc.getElixirCha()+1);
									else
										pc.setLvCha(pc.getLvCha()+1);
								}
							} else
								ChattingController.toChatting(pc, String.format("한 능력치의 최대값은 %d 입니다. 다른 능력치를 선택해 주세요.", Lineage.stat_max), 20);
						}else if(stat.equalsIgnoreCase("wis")){
							if(base || pc.getWis()+pc.getLvWis()+pc.getElixirWis()<Lineage.stat_max) {
								if(base) {
									if(pc.getWis() < 18)
										pc.setWis(pc.getWis()+1);
									else
										ChattingController.toChatting(pc, "WIS 베이스 능력치 최대값은 18 입니다. 다른 능력치를 선택해 주세요.", 20);
								} else {
									if(pc.getElixirStat() < pc.getElixirReset())
										pc.setElixirWis(pc.getElixirWis()+1);
									else
										pc.setLvWis(pc.getLvWis()+1);
									/*
									Character cha = (Character)pc;

									switch(pc.getClassType()){
										case 0x00://군
									int mp1 = cha.getMaxMp()+ 10;
									cha.setMaxMp(mp1);
										break;
										case 0x01://기
									int mp2 = cha.getMaxMp()+ 10;
									cha.setMaxMp(mp2);
										break;
										case 0x02://요
									int mp3 = cha.getMaxMp()+ 26;
									cha.setMaxMp(mp3);
										break;
										case 0x03://법
	
									int mp4 = cha.getMaxMp()+ 50;
									cha.setMaxMp(mp4);
										break;
										case 0x04://법
									int mp5 = cha.getMaxMp()+ 40;
									cha.setMaxMp(mp5);
										break;
										case 0x05://법
									int mp6 = cha.getMaxMp()+ 10;
									cha.setMaxMp(mp6);
										break;
									}*/
								}
							} else
								ChattingController.toChatting(pc, String.format("한 능력치의 최대값은 %d 입니다. 다른 능력치를 선택해 주세요.", Lineage.stat_max), 20);
						}else{
							if(base || pc.getInt()+pc.getLvInt()+pc.getElixirInt()<Lineage.stat_max) {
								if(base) {
									if(pc.getInt() < 18)
										pc.setInt(pc.getInt()+1);
									else
										ChattingController.toChatting(pc, "INT 베이스 능력치 최대값은 18 입니다. 다른 능력치를 선택해 주세요.", 20);
								} else {
									if(pc.getElixirStat() < pc.getElixirReset())
										pc.setElixirInt(pc.getElixirInt()+1);
									else
										pc.setLvInt(pc.getLvInt()+1);
								}
							} else
								ChattingController.toChatting(pc, String.format("한 능력치의 최대값은 %d 입니다. 다른 능력치를 선택해 주세요.", Lineage.stat_max), 20);
						}
						
//						ChattingController.toChatting(pc, "\\fR 현재 스텟 ", 20);
//						ChattingController.toChatting(pc, String.format("\\fR Str[%d] Dex[%d] Con[%d] Int[%d] Cha[%d] Wis[%d]  ",
//						pc.getStr()+pc.getLvStr(),pc.getDex()+pc.getLvDex(),pc.getCon()+pc.getLvCon(),pc.getInt()+pc.getLvInt(),
//						pc.getCha()+pc.getLvCha(),pc.getWis()+pc.getLvWis()), 20);
						
						pc.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), pc));
						// 확인 및 창 띄우기.
						pc.toLvStat(true);
					}
				}
				break;
			case 512:	// 아지트 집 이름 변경.
				AgitController.toNameChange(pc, readS());
				break;
			case 622: //친구테이블
				if (pc.isSaveCharacter()) {
					if (yn == 0) {
						pc.setMarblesObjectId(0);
						pc.setSaveCharacter(false);
					} else if (yn == 1) {
						ItemInstance charMarbles = pc.getInventory().findByObjId(pc.getMarblesObjectId());
						if (charMarbles != null) {
							if (charMarbles instanceof CharacterSaveMarbles) {
								CharacterSaveMarbles csm = (CharacterSaveMarbles) charMarbles;
								if (!csm.isCharacterMarblesMake(pc)) {
									ChattingController.toChatting(pc, "이미 저장된 캐릭터 입니다.", Lineage.CHATTING_MODE_MESSAGE);
									System.out.println("이미 캐릭터저장구슬에 등록된 캐릭터가 동일한 아이템을 사용하려합니다. 확인바랍니다. [" + pc.getName() + "]");
									return this;
								}

								if (World.findPersnalShop(pc.getName()) != null) {
									ChattingController.toChatting(pc, "무인상점을 종료 후 사용해 주십시오.", Lineage.CHATTING_MODE_MESSAGE);
									return this;
								}

								if (pc.getClanId() != 0) {
									ChattingController.toChatting(pc, "혈맹 탈퇴 후 사용해 주십시오.", Lineage.CHATTING_MODE_MESSAGE);
									return this;
								}

								/**
								 * 저장된 캐릭터가 없이 빈 아이템이라면 캐릭터 저장
								 */
								csm.toUpdate(pc);
								ChattingController.toChatting(pc, "캐릭터 저장이 완료 되었습니다. 창고로 저장 되었습니다.", Lineage.CHATTING_MODE_MESSAGE);
								ChattingController.toChatting(pc, "잠시 후 클라이언트가 강제종료 됩니다.", Lineage.CHATTING_MODE_MESSAGE);

								WarehouseDatabase.insert(charMarbles, charMarbles.getObjectId(), charMarbles.getCount(), pc.getClient().getAccountUid(),
										Lineage.DWARF_TYPE_NONE);

								/**
								 * 캐릭터 종료시키고 해당 계정에 더이상 출력안되게 하기 아이템 창고로 이동시키기
								 */
								String change_account_name = csm.ACCOUNT_ADD_NAME + csm.getAccountName();
								int change_account_uid = csm.getAccountUid() + csm.ACCOUNT_ADD_VALUE;
								CharactersDatabase.getChangeAccountName(pc.getName(), change_account_name, change_account_uid);

								pc.getInventory().count(charMarbles, charMarbles.getCount() - 1, true);

								pc.setMarblesObjectId(0);
								pc.setSaveCharacter(false);
								pc.toWorldOut();
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								pc.toSender(S_Disconnect.clone(BasePacketPooling.getPool(S_Disconnect.class), 0x0A));
							}
						}
					}
				} else if (pc.isCharacterMarblesUse()) {
					if (yn == 0) {
						pc.setMarblesObjectId(0);
						pc.setCharacterMarblesUse(false);
					} else if (yn == 1) {
						ItemInstance charMarbles = pc.getInventory().findByObjId(pc.getMarblesObjectId());
						if (charMarbles != null) {
							if (charMarbles instanceof CharacterSaveMarbles) {
								CharacterSaveMarbles csm = (CharacterSaveMarbles) charMarbles;

								/*if (pc.getClient().getAccountId().equalsIgnoreCase(csm.getAccountName())) {
									ChattingController.toChatting(pc, "해당 캐릭터를 저장한 계정에서는 재등록할 수 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
									return this;
								}*/

								Connection con = null;
								try {
									con = DatabaseConnection.getLineage();
									if (AccountDatabase.getCharacterLength(con, pc.getClient().getAccountUid()) >= 6) {
										ChattingController.toChatting(pc, "계정에 비어있는 캐릭터 슬롯이 없습니다.", Lineage.CHATTING_MODE_MESSAGE);
										return this;
									}
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									DatabaseConnection.close(con);
								}

								csm.toDeleteDB();
								String change_account_name = pc.getClient().getAccountId();
								int change_account_uid = pc.getClient().getAccountUid();
								CharactersDatabase.getChangeAccountName(csm.getCharacterName(), change_account_name, change_account_uid);

								pc.getInventory().count(charMarbles, charMarbles.getCount() - 1, true);
								ChattingController.toChatting(pc, "이전이 완료 되었습니다. 클라이언트가 강제종료 됩니다.", Lineage.CHATTING_MODE_MESSAGE);;
								pc.setMarblesObjectId(0);
								pc.setCharacterMarblesUse(false);
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								pc.toSender(S_Disconnect.clone(BasePacketPooling.getPool(S_Disconnect.class), 0x0A));
							}
						}
					}
				}
				break;
			case 729:	// 콜 클렌
				ClanController.toCallClan(pc, yn == 1);
				break;
//			case 773: // 팀대전에 참여하시겠습니까? (y/N)잠시
//				TeamBattleController.toAsk(pc, yn == 1);
//				break;
			case 560: // 혈맹파티에 승락하시겠습니까??
				PartyController.toParty(pc, yn==1, true);
				break;
			case 771: // 변신이 해제됩니다. 계속 하시겠습니까? (y/N)
				FishingController.toAsk(pc, true);
				break;
//			case 778: // 자동낚시를 시작하시겠습니까? (y/N)
//				FishingController.startAutoFishing(pc, yn == 1);
//				break;
//			case 953:	// 파티 신청//여기 번호 그대로에요?아녕 그기존에있던 번호 쓰는데 안가서 쓸수잇는게
				
//	        case 954:	// 분배 파티 초대 한다~
//				PartyController.toParty(pc, yn==1);
//				break;

		}
		
		return this;
	}
}
