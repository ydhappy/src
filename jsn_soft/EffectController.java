package jsn_soft;

import java.util.List;

import lineage.world.object.instance.PcInstance;

public class EffectController extends Effect {
	private int effectCount;

	public EffectController() {
		effectCount = 0;
	}

	public void sendEffect(List<PcInstance> pc_list) {
	
		// 피씨 리스트만큼 반복문을 돌려서 피씨리스트를 분해한다
		for (PcInstance pc : pc_list) {
			// 피씨리스트안에 피씨가 존재한다면
			// 그리고 아부지클레스의 피씨리스트와 들어온 피씨리스트가 같다면
			if (pc != null && getPc_list_private().equals(pc_list)) {
				// 이팩트를 보낸 횟수를 증가시킨다
				setEffectCount(getEffectCount() + 1);
				// 그리고 이팩트를 보낸다
				sendEffect(pc);
			}
		}
		return;

	}

	@Override
	public void sendEffect(PcInstance pc) {
		// 이곳에서 이팩트 처리가 끝나면
	
		if (pc != null && pc != getPc_list_private()) {
			remove(pc);
			setEffectCount(0);
		}
		return;
	}

	// pc_list_private 에서 해당 pc를 빼주어야 한다

	// 요거 가능하시겠어용? ㅋㅋㅋㅋ 가능안해도 해봐야죠 ㅋㅋ 두개에 메소드만 채워주면 되는거 아니에요? 맞아용! 해놀게요
	// 그리구 나서 시간 되시면 @Override 이게 아부지꺼 라는 마크에용 이런거 만들어보면서 이해하시면 더 좋을거에용 아 근데 그럼 저게
	// 아부지꺼면 잠시
	public int getEffectCount() {
		return effectCount;
	}

	public void setEffectCount(int effectCount) {
		this.effectCount = effectCount;
	}
}
