package lineage.bean.lineage;

import java.util.ArrayList;
import java.util.List;

import lineage.bean.database.Skill;
import lineage.share.Log;
import lineage.world.controller.BuffController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.magic.FrameSpeedOverStun;
import lineage.world.object.magic.Haste;
import lineage.world.object.magic.item.Wafer;

public class Buff {
	private List<BuffInterface> list;
	private object o;
	
	/**
	 * 생성자
	 * @param o
	 */
	public Buff(object o){
		this.o = o;
		list = new ArrayList<BuffInterface>();
	}
	
	/**
	 * 메모리 초기화 구간
	 */
	public void close(){
		removeAll();
		o = null;
	}
	
	/**
	 * 동적 생성후 버프와 연결될 객체 연결 함수
	 * @param o
	 */
	public void setObject(object o){
		this.o = o;
	}
	
	public object getObject(){
		return o;
	}
	
	public List<BuffInterface> getList(){
		synchronized (list) {
			return new ArrayList<BuffInterface>( list );
		}
	}
	
	private void removeList(BuffInterface bi){
		synchronized (list) {
			list.remove(bi);
		}
	}
	
	private void appendList(BuffInterface bi){
		synchronized (list) {
			list.add(bi);
		}
	}
	
	public void clearList() {
		synchronized (list) {
			list.clear();
		}
	}
	
	/**
	 * 버프적용된 객체 월드아웃 되는거 알리기.
	 */
	public void toWorldOut() {
		for(BuffInterface bi : getList())
			bi.toWorldOut(o);
	}
	
	private boolean isLog = true, isLog2 = true;
	/**
	 * 타이머가 주기적으로 호출함. BuffController를 거쳐서 여기옴
	 */
	public void toTimer(long time) {
		// 처리
		List<BuffInterface> list = getList();
//		if(list.size() == 0 )
//			System.out.println("Buff Size : "+list.size());
		boolean isNonZero = false;
		if(list.size() !=0 )
			isNonZero = true;
		
		object object = o;
		for (BuffInterface bi : list) {
			if (bi == null || object == null) {
				continue;
			}
			
			try {
//				System.out.println("bi : "+bi+" o : "+o.getName());
				if (bi.isBuff(object, time)) {
					bi.toBuff(object);
				} else {

					// 강제 종료 요청했다는거 알리기
					try {
						bi.toBuffEnd(object);			// 메디테이션에서 null포인트 에러남. 아직 이유를 모르겠음
												
					} catch (Exception e) {
						if (isLog) {
							isLog = false;
							Log.toError(e);
						}
						//lineage.share.System.printf("%s : toTimer(long time).toBuffEnd %s\r\n", Buff.class.toString(), bi.toString());
						//lineage.share.System.println(e);
					}
					
					// 목록에서 제거.
					removeList(bi);
					
					// 풀에 등록.
					if (!(bi instanceof ItemInstance)) {
						BuffController.setPool(bi);
					}
				}
			} catch (Exception e) {
				if (isLog2) {
					isLog2 = false;
					Log.toError(e);
				}
//				lineage.share.System.printf("%s : toTimer(long time) %s\r\n", Buff.class.toString(), bi.toString());
//				lineage.share.System.println(e);
				
				try {
					if (!(bi instanceof ItemInstance))
						BuffController.setPool(bi);
				} catch (Exception e2) { }
				try {
					if (object != null) {
						bi.toBuffEnd(object);
					}
				} catch (Exception e2) { }
				removeList(bi);
			}
		}

		//버프리스트가 없을경우 루프에서 제거
		if(list.size() == 0 && isNonZero) {
//			System.out.println("Buff RemoveOnly : "+o);
			if (o != null) {
				BuffController.removeOnly(o);
			}
		}
		// lineage.share.System.println(Buff.class+" : toTimer() "+list.size());
	}
	
	/**
	 * 버프 등록 처리 함수.
	 * @param bi
	 */
	public void append(BuffInterface bi){
		BuffInterface find_bi = find(bi);
		if(find_bi != null){
			// 시간 맞추기
			find_bi.setTime(bi.getTime());
			find_bi.setSkill(bi.getSkill());
			// 동일한 버프가 존재할경우 적용되어있는 버프에는 알려주기만함.
			find_bi.toBuffUpdate(o);
			// 등록 추가하려고 했던 버프는 필요가 없어지므로 다시 풀에 등록.
			if(!(find_bi instanceof ItemInstance))
				BuffController.setPool(bi);
		}else{
			// 신규 버프 등록 처리 구간
			bi.toBuffStart(o);
			appendList(bi);
		}
	}
	
	/**
	 * 동일한 객체에 버프를 찾아서 제거하는 함수
	 * @param c
	 */
	public void remove(Class<?> c){
		BuffInterface bi = find(c);
		if(bi != null){
			// 관리목록에서 제거
			removeList(bi);
			// 찾았을경우 강제 종료 요청했다는거 알리기
			bi.toBuffStop(o);
			// 풀에 넣고
			if(!(bi instanceof ItemInstance))
				BuffController.setPool(bi);
			
//			lineage.share.System.println(bi.toString());
		}
	}
	
	public void toDead(boolean world_out) {
		if(world_out) {
			removeAll();
		} else {
			//
			List<BuffInterface> list = getList();
			clearList();
			//
			for(BuffInterface b : list){
				// 강제 종료 요청했다는거 알리기
				if(b.toBuffStop(o)) {
					// 풀에 넣고
					if(!(b instanceof ItemInstance))
						BuffController.setPool(b);
				} else {
					// 정지에 실패한건 다시 관리목록에 등록.
					appendList(b);
				}
			}
			list.clear();
		}
	}
	
	public void removeAll() {
		//
		List<BuffInterface> list = getList();
		clearList();
		//
		for(BuffInterface b : list){
			// 강제 종료 요청했다는거 알리기
			b.toBuffStop(o);
			// 풀에 넣고
			if(!(b instanceof ItemInstance))
				BuffController.setPool(b);
		}
		list.clear();
	}
	
	/**
	 * 매개변수 버프와 일치하는 같은 종류의 버프가 있는지 확인하는 함수.
	 *  : Skill 이 지정되어 있지 않을경우 아이템으로 생각하면 되며, 이럴경우 해당 객체의 고유값인 해쉬값으로 이용해 같은지 확인해서 리턴.
	 *    마법의 플룻 참고.
	 * @param bi
	 * @return
	 */
	private BuffInterface find(BuffInterface bi){
		for(BuffInterface b : getList()){
			if(b.getSkill()==null || bi.getSkill()==null){
				if(b.hashCode()==bi.hashCode())
					return b;
			}else{
				if(b.getClass().toString().equals(bi.getClass().toString()))
					return b;
			}
		}
		return null;
	}
	
	/**
	 * 동일한 버프가 존재하는지 확인해서 꺼내는 함수.
	 * @param c
	 * @return
	 */
	public BuffInterface find(Class<?> c){
		for(BuffInterface b : getList()){
			if(b.getClass().toString().equals(c.toString()))
				return b;
		}
		return null;
	}
	
	/**
	 * 스킬 고유 uid 값으로 적용된 버프찾기.
	 * @param uid
	 * @return
	 */
	public BuffInterface find(Skill s) {
		// 윈드워크시전이라면 와퍼로 확인하기.
		if(s.getSkillLevel()==19 && s.getSkillNumber()==5)
			return find(Wafer.class);
		//
		for(BuffInterface b : getList()) {
			if(b.getSkill() == null)
				continue;
			if(s.getSkillLevel()==b.getSkill().getSkillLevel() && s.getSkillNumber()==b.getSkill().getSkillNumber())
				return b;
			// 헤이스트와 그레이트헤이스트는 동일 취급해야함.
			if(s.getSkillLevel()==6 && s.getSkillNumber()==2 && b.getClass().toString().equals(Haste.class.toString()))
				return b;
			if(s.getSkillLevel()==7 && s.getSkillNumber()==5 && b.getClass().toString().equals(Haste.class.toString()))
				return b;
		}
		return null;
	}
	
}
