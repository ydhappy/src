package jsn_soft;

import java.util.ArrayList;
import java.util.List;

import lineage.world.object.instance.PcInstance;

public class Effect {
	/**
	 * 보스에게 아데나를 받은 캐릭터들에게 이팩트를 보내는것을 목표로한 test 클래스이다
	 * 
	 * 해당리스트에 있는 피씨에게 피씨가 접속중일시 특정 이팩트를 보내는것이 주 목적이며
	 * 
	 * 그 이외로 추가 & 활용할 수 있는 여러가지 기능들을 만들어보면서 코드에 대한 기초를 다질 계획이다
	 * 
	 * by 스무디 & 헬스보이
	 */

	// 해당 변수는 스테틱 퍼블릭 직역하면 어디에서도 공유가능한 입니당
	static public List<PcInstance> pc_list_static = new ArrayList<PcInstance>();

	// 해당 변수는 프리베이트 스테틱 직역하면 어디에서도 공유불가능한 (해당클레스에서만공유가능한) 이렇게 직역이 가능합니당

	// 그러면 3번째 스태틱을 빼려면..?
	private List<PcInstance> pc_list_private;

	// 해당 클래스의 이름을 가진. 매소드가 하나 필요해용
	public Effect() {
		pc_list_private = new ArrayList<PcInstance>();
	}

	// 게터 리스트를 불러오는거에요 List<PcInstance> pc_list = getPc_list_private();
	// 위와 같은 식으로 사용이가능하죵
	public List<PcInstance> getPc_list_private() {
		return pc_list_private;
	}

	// 셋터 이것은 고정한다 지정한다 이렇게 가능해요 위에서 선언한 List<PcInstance> pc_list =
	// getPc_list_private();
	// 위의 것을 불러와 setPc_list_private(pc_list); 같은 방식으로 사용을하죠
	public void setPc_list_private(List<PcInstance> pc_list_private1) {
		pc_list_private = pc_list_private1;
	}

	// 추가하는 매소드 append 만들어볼게용
	public void append(PcInstance pc) {
		if (!pc_list_private.contains(pc))
			pc_list_private.add(pc);
	}

	// 제거하는 메소드
	public void remove(PcInstance pc) {
		if (pc_list_private.contains(pc))
			pc_list_private.remove(pc);
	}

	// 포함된지 확인하는 매소드! contains 이걸 하나 더 만들어준다 생각하면 되용
	public boolean contains(PcInstance pc) {
		for (PcInstance p : pc_list_private) {
			if (p == pc || pc.equals(p)) 
			return true;
		}
		return false;
	}

	// 클리어하는 매소드
	public void clear() {
		pc_list_private.clear();
	}
	
	// 이팩트를 보낼애들을 선별하는 매소드
	public void sendEffect(List<PcInstance> pc_list) { }
	
	// 이팩트를 보내는 매소드
	public void sendEffect(PcInstance pc) { }
}
