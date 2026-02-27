package lineage.world.object.item.cook;

import lineage.database.SkillDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Message;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.object.Character;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.magic.item.CookCommon;
import lineage.world.object.magic.item.CookExp;

public class Cook extends ItemInstance {
	
	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Cook();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp) {
		// 버섯스프 일경우 food체크 100%일때만 복용됨.
		if(getItem().getNameIdNumber()==5221 && getItem().getNameIdNumber()==49285221 && cha.getFood()!=Lineage.MAX_FOOD) {
			ChattingController.toChatting(cha, "배부른 상태가 100%일때만 섭취할 수 있습니다.", 20);
			return;
		}
		// 버프 적용.
		toCookBuff(cha);
		// food상태 만땅
		cha.setFood(Lineage.MAX_FOOD);
		// 메세지
		cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 76, getName()));
		// 수량제거
		cha.getInventory().count(this, getCount()-1, true);
	}
	
	private void toCookBuff(Character cha) {
		switch(getItem().getNameIdNumber()) {
			case 4932:		// 괴물눈 스테이크
			case 49284932:	// 환상의 괴물눈 스테이크
			case 5218:		// 곰고기 구이
			case 49285218:	// 환상의 곰고기 구이
			case 4929:		// 씨호떡
			case 49284929:	// 환상의 씨호떡
			case 5219:		// 개미다리 치즈구이
			case 49285219:	// 환상의 개미다리 치즈구이
			case 4930:		// 과일 샐러드
			case 49284930:	// 환상의 과일 샐러드
			case 4931:		// 과일 탕수육
			case 49284931:	// 환상의 과일 탕수육
			case 5220:		// 멧돼지 꼬치 구이
			case 49285220:	// 환상의 멧돼지 꼬치 구이
				
			/*case 5221:		// 버섯 스프
			case 49285221:	// 환상의 버섯 스프*/
				BuffController.remove(cha, CookCommon.class);
				BuffController.append(cha, CookCommon.clone(BuffController.getPool(CookCommon.class), SkillDatabase.find(216), getItem()));
				break;
		case 5221:		// 버섯 스프
			case 49285221:	// 환상의 버섯 스프
				BuffController.remove(cha, CookExp.class);
				BuffController.append(cha, CookExp.clone(BuffController.getPool(CookExp.class), SkillDatabase.find(217), getItem()));
				break;
		}
	}

}
