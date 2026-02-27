package lineage.world.object.item.etc;

import lineage.bean.database.Monster;
import lineage.database.MonsterDatabase;
import lineage.network.LineageClient;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_InventoryCount;
import lineage.network.packet.server.S_InventoryEquipped;
import lineage.network.packet.server.S_InventoryStatus;
import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SummonController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PetInstance;

public class GoldMonster extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new GoldMonster();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		if(cha instanceof PetInstance) {
			PetInstance pet = (PetInstance)cha;
			// gfx변로 분리하여 하이펫 객체 불러오기
			Monster mon = getHighPet(pet.getClassGfx());
			// 펫 진화하기.
			if(mon != null) {				
				pet.setMonster( mon );
				if(pet.getName().startsWith("$"))
					pet.setName(mon.getNameId());
				pet.setMaxHp(pet.getMaxHp() );
				pet.setMaxMp(pet.getMaxMp() );
				pet.setNowHp(pet.getMaxHp());
				pet.setNowMp(pet.getMaxMp());
				pet.setLevel(1);
				pet.setExp(1);
				pet.setGfx(mon.getGfx());
				pet.setGfxMode(mon.getGfxMode());
				pet.setClassGfx(mon.getGfx());
				pet.setClassGfxMode(mon.getGfxMode());
				pet.toTeleport(pet.getX(), pet.getY(), pet.getMap(), false);
				// 펫 목걸이 변경하기.
				pet.getCollar().toUpdate(pet);
				// 목걸이 정보 다시 갱신.
				if(Lineage.server_version<=144){
					pet.getSummon().getMaster().toSender(S_InventoryEquipped.clone(BasePacketPooling.getPool(S_InventoryEquipped.class), pet.getCollar()));
					pet.getSummon().getMaster().toSender(S_InventoryCount.clone(BasePacketPooling.getPool(S_InventoryCount.class), pet.getCollar()));
				}else{
					pet.getSummon().getMaster().toSender(S_InventoryStatus.clone(BasePacketPooling.getPool(S_InventoryStatus.class), pet.getCollar()));
				}
			} else {
				ChattingController.toChatting(cha, "진화를 할 수 없습니다.", 0);
			}
		} else {
			super.toClick(cha, cbp);
		}
	}

	private Monster getHighPet(int gfx) {
		switch(gfx) {
			case 3199:	// 늑대
			case 96:
				return MonsterDatabase.find("슈퍼 울프"); // 4822 돌연변이 늑대인간
			case 3132:	// 도베르만
			case 931:
				return MonsterDatabase.find("슈퍼 도베르만"); //에바왕국 악령
			case 3184:	// 세퍼드
			case 936:
				return MonsterDatabase.find("슈퍼 세퍼드"); //맘몬
			case 3143:	// 비글
			case 938:
				return MonsterDatabase.find("슈퍼 비글"); //켈베로스 951
			case 3107:	// 허스키
			case 2145:	// 허스키
				return MonsterDatabase.find("슈퍼 허스키"); //샤벨타이거
			case 3182:	// 세인트 버나드
			case 929:	// 세인트 버나드
				return MonsterDatabase.find("슈퍼 세인트 버나드");	//오벨리스크
			case 3154:	// 열혈토끼
			case 2734:	// 열혈토끼	
				return MonsterDatabase.find("슈퍼 래빗"); //맘보 토끼
			case 3188:	// 곰
			case 1642:	// 곰			
				return MonsterDatabase.find("슈퍼 베어"); //칠흑의 돌골램
			case 3156:	// 여우
			case 1540:	// 여우
				return MonsterDatabase.find("슈퍼 폭스"); //카샨
			case 3178:	// 고양이
			case 3134:	// 고양이
				return MonsterDatabase.find("슈퍼 캣"); // 2752 꿈 혼령
			case 6322: //아기캥거루
			case 6325: //아기캥거루
				return MonsterDatabase.find("슈퍼 캥거루"); //환웅
			case 6314: //아기 팬더
			case 6310: //아기 팬더
				return MonsterDatabase.find("슈퍼 판다곰"); //대 흑장로
			case 4133: //라쿤
			case 4038: //라쿤
				return MonsterDatabase.find("슈퍼 라쿤"); //시페릿
			case 4582: //아기 진돗개
			case 5065: //아기 진돗개
				return MonsterDatabase.find("슈퍼 진돗개"); // 이프리트
			case 5089: //호랑이
			case 4542: //호랑이
				return MonsterDatabase.find("슈퍼 타이거"); //핏빛 데스나이트
		}
		return null;
	}
	
}
