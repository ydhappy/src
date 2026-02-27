package lineage.network.packet.server;

import lineage.network.packet.BasePacket;
import lineage.network.packet.Opcodes;
import lineage.network.packet.ServerBasePacket;
import lineage.world.object.object;

public class S_ObjectEffect extends ServerBasePacket {

	static synchronized public BasePacket clone(BasePacket bp, object o, int effect){
		if(bp == null)
			bp = new S_ObjectEffect(o, effect);
		else
			((S_ObjectEffect)bp).toClone(o, effect);
		return bp;
	}
	
	public S_ObjectEffect(object o, int effect){
		toClone(o, effect);
	}
	
	public S_ObjectEffect() {
		//
	}
	
	public void toClone(object o, int effect){
		clear();
		writeC(Opcodes.S_OPCODE_EFFECT);
		// 캐릭터 위치를 써주고
		writeD(o.getObjectId());
		// 이팩트 번호를 써주면
		writeH(effect);
		
		
		// 클라는 캐릭터 위치에
		// 이팩트 번호에맞는 스프리트를 실행한다
		// 실행이 끝나면 없어져야하는 이미지가
		// 오브젝트로 남아 길을막는다.
		// 대부분의 길막은
		// 이미지가 끝나기전.
		// 클라에서 오브잭트 즉 캐릭터가 바뀌거나
		// 이동하거나
		// 위치가 바뀌게되면
		// 생기게 된다
		// 이유는 오브젝트 아이디에 맞춰서 실행이 끝나야하는
		// 이미지가 위치가 바뀜으로서 이동을 함으로서
		// 오브젝트를 인식 못하기때문에
		// 멈추기 때문이다.
		// 라는 결론이 나왔습니다 ㅋㅋㅋ 그럼방안은?
		// 1초 딜레이죠 이미지가 끝날때 까지
		// 기다려주는거에요모든마법에요?
		// 음 버프류만일까요? 한번 해볼께요
	}
	
}
