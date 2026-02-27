package lineage.world.object.item.etc;


import lineage.bean.database.Item;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_LetterNotice;
import lineage.network.packet.server.S_Message;
import lineage.share.Common;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;

import lineage.database.ItemDatabase;
import lineage.database.ServerDatabase;
import lineage.world.object.instance.PcInstance;
import lineage.world.object.item.MagicDoll;
import lineage.world.controller.ChattingController;
import lineage.world.controller.MagicDollController;
import lineage.share.Lineage;


public class Mix extends ItemInstance {

	static synchronized public ItemInstance clone(ItemInstance item){
		if(item == null)
			item = new Mix();
		return item;
	}
	
	@Override
	public void toClick(Character cha, ClientBasePacket cbp){
		
		if(this.item.getName().indexOf("미스릴 실") > -1){
			int array[] = {5,1};
			for(ItemInstance ii : cha.getInventory().getList()){
				if(ii.getItem().getNameId().equalsIgnoreCase("$767") && ii.getCount()>=5){
					
					int temp = 0;
					int[] temp1 = array;
					temp1[temp] = (int)(temp1[temp] - ii.getCount());
					if(array[0]<0)
						array[0]=0;					
				}else if(ii.getItem().getNameId().equalsIgnoreCase("$766") && ii.getCount()>=1){
					int temp = 1;
					int[] temp1 = array;
					temp1[temp] = (int)(temp1[temp]-ii.getCount());
					if(array[1]<0)
						array[1]=0;
				}
			}
			if(array[0]==0 && array[1]==0){
				ItemInstance item[] = new ItemInstance[2];
			//	int i=0;
				for(ItemInstance ii : cha.getInventory().getList()){
					if(ii.getItem().getName().equals("미스릴")){
						cha.getInventory().append(ii, -5L);
					}else if(ii.getItem().getName().equals("실")){
						cha.getInventory().append(ii, -1L);
					//	item[i] = ii;
					//	i++;
					}
				}				
				cha.getInventory().remove(item[0], true);
				cha.getInventory().remove(item[1], true);				
				cha.getInventory().count(this, getCount()-1L, true);
				
				ItemInstance j = cha.getInventory().find("미스릴 실",true);				
				ItemInstance jj = ItemDatabase.newInstance( ItemDatabase.find("미스릴 실"));
				if(j!=null){
					long cnt = j.getCount();
					cha.getInventory().count(j, cnt+1L,true);
				}else{
				//	ItemInstance iii = ItemDatabase.newInstance(jj);
					jj.setObjectId(ServerDatabase.nextItemObjId());
					cha.getInventory().append(jj, true);
				}
				/*
				Item it = ItemDatabase.find2("미스릴실");
				if(it != null){
					ItemInstance iii = ItemDatabase.newInstance(it);
					iii.setObjectId(ServerDatabase.nextItemObjId());
					
					iii.setDefinite(false);
					cha.getInventory().append(iii, 1L);
					ItemDatabase.setPool(iii);
				}*/
				
			}else{
				//재료부족
				ChattingController.toChatting(cha, "필요한 재료는 미스릴 5개와 실 1개 입니다.", 20);
			}
		}else if(this.item.getName().indexOf("미스릴 판금") > -1){
			int array[] = {1,50};
			for(ItemInstance ii : cha.getInventory().getList()){
				if(ii.getItem().getNameId().equalsIgnoreCase("$774") && ii.getCount()>=1){
					int temp = 0;
					int[] temp1 = array;
					temp1[temp] = (int)(temp1[temp] - ii.getCount());
					if(array[0]<0)
						array[0]=0;					
				}else if(ii.getItem().getNameId().equalsIgnoreCase("$767") && ii.getCount()>=50){
					int temp = 1;
					int[] temp1 = array;
					temp1[temp] = (int)(temp1[temp]-ii.getCount());
					if(array[1]<0)
						array[1]=0;
				}
			}
			if(array[0]==0 && array[1]==0){
				ItemInstance item[] = new ItemInstance[2];
			//	int i=0;
				for(ItemInstance ii : cha.getInventory().getList()){
					if(ii.getItem().getName().equals("미스릴")){
						cha.getInventory().append(ii, -50L);
					}else if(ii.getItem().getName().equals("아라크네의 허물")){
						cha.getInventory().append(ii, -1L);
					//	item[i] = ii;
					//	i++;
					}
				}				
				cha.getInventory().remove(item[0], true);
				cha.getInventory().remove(item[1], true);				
				cha.getInventory().count(this, getCount()-1L, true);	
				ItemInstance j = cha.getInventory().find("미스릴 판금",true);				
				ItemInstance jj = ItemDatabase.newInstance( ItemDatabase.find("미스릴 판금"));
				if(j!=null){
					long cnt = j.getCount();
					cha.getInventory().count(j, cnt+1L,true);
				}else{
					jj.setObjectId(ServerDatabase.nextItemObjId());
					cha.getInventory().append(jj, true);
				}
			/*	Item it = ItemDatabase.find2("미스릴판금");
				if(it != null){
					ItemInstance iii = ItemDatabase.newInstance(it);
					iii.setDefinite(false);
					cha.getInventory().append(iii, 1L);
					ItemDatabase.setPool(iii);
				}	*/			
			}else{
				//재료부족
				ChattingController.toChatting(cha, "필요한 재료는 미스릴 50개와 허물 1개 입니다.", 20);
			}
			
		}else if(this.item.getName().indexOf("오리하르콘 판금") > -1){
			int array[] = {1,30};
			for(ItemInstance ii : cha.getInventory().getList()){
				if(ii.getItem().getNameId().equalsIgnoreCase("$774") && ii.getCount()>=1){
					int temp = 0;
					int[] temp1 = array;
					temp1[temp] = (int)(temp1[temp] - ii.getCount());
					if(array[0]<0)
						array[0]=0;					
				}else if(ii.getItem().getNameId().equalsIgnoreCase("$771") && ii.getCount()>=30){
					int temp = 1;
					int[] temp1 = array;
					temp1[temp] = (int)(temp1[temp]-ii.getCount());
					if(array[1]<0)
						array[1]=0;
				}
			}
			if(array[0]==0 && array[1]==0){
				ItemInstance item[] = new ItemInstance[2];
			//	int i=0;
				for(ItemInstance ii : cha.getInventory().getList()){
					if(ii.getItem().getName().equals("아라크네의 허물")){
						cha.getInventory().append(ii, -1L);
					}else if(ii.getItem().getName().equals("오리하루콘")){
						cha.getInventory().append(ii, -30L);
					//	item[i] = ii;
					//	i++;
					}
				}				
				cha.getInventory().remove(item[0], true);
				cha.getInventory().remove(item[1], true);				
				cha.getInventory().count(this, getCount()-1L, true);
				
				ItemInstance j = cha.getInventory().find("오리하루콘 판금",true);				
				ItemInstance jj = ItemDatabase.newInstance( ItemDatabase.find("오리하루콘 판금"));
				if(j!=null){
					long cnt = j.getCount();
					cha.getInventory().count(j, cnt+1L,true);
				}else{
					jj.setObjectId(ServerDatabase.nextItemObjId());
					cha.getInventory().append(jj, true);
				}
				
			/*	Item it = ItemDatabase.find2("오리하루콘판금");
				if(it != null){
					ItemInstance iii = ItemDatabase.newInstance(it);
					iii.setDefinite(false);
					cha.getInventory().append(iii, 1L);
					ItemDatabase.setPool(iii);
				}	*/			
			}else{
				//재료부족
				ChattingController.toChatting(cha, "필요한 재료는 허물1개와 오리하루콘30개 입니다.", 20);
			}
			
		}else if(this.item.getName().indexOf("판의 뿔") > -1){
			int array[] = {1,10};
			for(ItemInstance ii : cha.getInventory().getList()){
				if(ii.getItem().getNameId().equalsIgnoreCase("$770") && ii.getCount()>=1){
					int temp = 0;
					int[] temp1 = array;
					temp1[temp] = (int)(temp1[temp] - ii.getCount());
					if(array[0]<0)
						array[0]=0;					
				}else if(ii.getItem().getNameId().equalsIgnoreCase("$771") && ii.getCount()>=10){
					int temp = 1;
					int[] temp1 = array;
					temp1[temp] = (int)(temp1[temp]-ii.getCount());
					if(array[1]<0)
						array[1]=0;
				}
			}
			if(array[0]==0 && array[1]==0){
				ItemInstance item[] = new ItemInstance[2];
			//	int i=0;
				for(ItemInstance ii : cha.getInventory().getList()){
					if(ii.getItem().getName().equals("엔트의 껍질")){
						cha.getInventory().append(ii, -1L);
					}else if(ii.getItem().getName().equals("오리하루콘")){
						cha.getInventory().append(ii, -10L);
					//	item[i] = ii;
					//	i++;
					}
				}				
				cha.getInventory().remove(item[0], true);
				cha.getInventory().remove(item[1], true);				
				cha.getInventory().count(this, getCount()-1L, true);	
				ItemInstance j = cha.getInventory().find("판의 뿔",true);				
				ItemInstance jj = ItemDatabase.newInstance( ItemDatabase.find("판의 뿔"));
				if(j!=null){
					long cnt = j.getCount();
					cha.getInventory().count(j, cnt+1L,true);
				}else{
					jj.setObjectId(ServerDatabase.nextItemObjId());
					cha.getInventory().append(jj, true);
				}
				/*Item it = ItemDatabase.find2("판의뿔");
				if(it != null){
					ItemInstance iii = ItemDatabase.newInstance(it);
					iii.setDefinite(false);
					cha.getInventory().append(iii, 1L);
					ItemDatabase.setPool(iii);
				}		*/		
			}else{
				//재료부족
				ChattingController.toChatting(cha, "필요한 재료는 껍질 1개와 오리하루콘 10개 입니다.", 20);
			}
			
		}else if(this.item.getName().indexOf("오리하루콘") > -1){
			int array[] = {10};
			for(ItemInstance ii : cha.getInventory().getList()){
				if(ii.getItem().getNameId().equalsIgnoreCase("$767") && ii.getCount()>=10){
					int temp = 0;
					int[] temp1 = array;
					temp1[temp] = (int)(temp1[temp] - ii.getCount());
					if(array[0]<0)
						array[0]=0;					
				}
			}
			if(array[0]==0){
				ItemInstance item[] = new ItemInstance[1];
			//	int i=0;
				for(ItemInstance ii : cha.getInventory().getList()){
					if(ii.getItem().getName().equals("미스릴")){
						cha.getInventory().append(ii, -10L);
					}
				}				
				cha.getInventory().remove(item[0], true);					
				cha.getInventory().count(this, getCount()-1L, true);				
				/*Item it = ItemDatabase.find2("오리하루콘");
				if(it != null){
					ItemInstance iii = ItemDatabase.newInstance(it);
					iii.setDefinite(false);
					cha.getInventory().append(iii, 1L);
					ItemDatabase.setPool(iii);
				}*/
				ItemInstance j = cha.getInventory().find("오리하루콘",true);				
				ItemInstance jj = ItemDatabase.newInstance( ItemDatabase.find("오리하루콘"));
				if(j!=null){
					long cnt = j.getCount();
					cha.getInventory().count(j, cnt+1L,true);
				}else{
					jj.setObjectId(ServerDatabase.nextItemObjId());
					cha.getInventory().append(jj, true);
				}
			}else{
				//재료부족
				ChattingController.toChatting(cha, "필요한 재료는 미스릴 10개 입니다.", 20);
			}	
			
		}else if(this.item.getName().indexOf("페어리 날개") > -1){
			int array[] = {5,40};
			for(ItemInstance ii : cha.getInventory().getList()){
				if(ii.getItem().getNameId().equalsIgnoreCase("$772") && ii.getCount()>=5){
					int temp = 0;
					int[] temp1 = array;
					temp1[temp] = (int)(temp1[temp] - ii.getCount());
					if(array[0]<0)
						array[0]=0;					
				}else if(ii.getItem().getNameId().equalsIgnoreCase("$768") && ii.getCount()>=40){
					int temp = 1;
					int[] temp1 = array;
					temp1[temp] = (int)(temp1[temp]-ii.getCount());
					if(array[1]<0)
						array[1]=0;
				}
			}
			if(array[0]==0 && array[1]==0){
				ItemInstance item[] = new ItemInstance[2];
			//	int i=0;
				for(ItemInstance ii : cha.getInventory().getList()){
					if(ii.getItem().getName().equals("미스릴 실")){
						cha.getInventory().append(ii, -5L);
					}else if(ii.getItem().getName().equals("페어리 더스트")){
						cha.getInventory().append(ii, -40L);
					//	item[i] = ii;
					//	i++;
					}
				}				
				cha.getInventory().remove(item[0], true);
				cha.getInventory().remove(item[1], true);				
				cha.getInventory().count(this, getCount()-1L, true);	
				
				ItemInstance j = cha.getInventory().find("페어리의 날개",true);				
				ItemInstance jj = ItemDatabase.newInstance( ItemDatabase.find("페어리의 날개"));
				if(j!=null){
					long cnt = j.getCount();
					cha.getInventory().count(j, cnt+1L,true);
				}else{
					jj.setObjectId(ServerDatabase.nextItemObjId());
					cha.getInventory().append(jj, true);
				}
				
				/*
				Item it = ItemDatabase.find2("페어리의날개");
				if(it != null){
					ItemInstance iii = ItemDatabase.newInstance(it);
					iii.setDefinite(false);
					cha.getInventory().append(iii, 1L);
					ItemDatabase.setPool(iii);
				}				*/
			}else{
				//재료부족
				ChattingController.toChatting(cha, "필요한 재료는 미스릴 실5개와 페어리더스트 40개 입니다.", 20);
			}
		}else if(this.item.getName().indexOf("아라크네의 허물") > -1){
			int array[] = {3};
			for(ItemInstance ii : cha.getInventory().getList()){
				if(ii.getItem().getNameId().equalsIgnoreCase("$770") && ii.getCount()>=3){
					int temp = 0;
					int[] temp1 = array;
					temp1[temp] = (int)(temp1[temp] - ii.getCount());
					if(array[0]<0)
						array[0]=0;					
				}
			}
			if(array[0]==0){
				ItemInstance item[] = new ItemInstance[1];
			//	int i=0;
				for(ItemInstance ii : cha.getInventory().getList()){
					if(ii.getItem().getName().equals("엔트의 껍질")){
						cha.getInventory().append(ii, -3L);
					}
				}				
				cha.getInventory().remove(item[0], true);					
				cha.getInventory().count(this, getCount()-1L, true);
				ItemInstance j = cha.getInventory().find("아라크네의 허물",true);				
				ItemInstance jj = ItemDatabase.newInstance( ItemDatabase.find("아라크네의 허물"));
				if(j!=null){
					long cnt = j.getCount();
					cha.getInventory().count(j, cnt+1L,true);
				}else{
					jj.setObjectId(ServerDatabase.nextItemObjId());
					cha.getInventory().append(jj, true);
				}
				/*Item it = ItemDatabase.find2("아라크네의허물");
				if(it != null){
					ItemInstance iii = ItemDatabase.newInstance(it);
					iii.setDefinite(false);
					cha.getInventory().append(iii, 1L);
					ItemDatabase.setPool(iii);
				}*/				
			}else{
				//재료부족
				ChattingController.toChatting(cha, "필요한 재료는 엔트의 껍질 3개 입니다.", 20);
			}	
		}else if(this.item.getName().indexOf("실") > -1){
			int array[] = {1};
			for(ItemInstance ii : cha.getInventory().getList()){
				if(ii.getItem().getNameId().equalsIgnoreCase("$760") && ii.getCount()>=1){
					int temp = 0;
					int[] temp1 = array;
					temp1[temp] = (int)(temp1[temp] - ii.getCount());
					if(array[0]<0)
						array[0]=0;					
				}
			}
			if(array[0]==0){
				ItemInstance item[] = new ItemInstance[1];
			//	int i=0;
				for(ItemInstance ii : cha.getInventory().getList()){
					if(ii.getItem().getName().equals("판의 갈기털")){
						cha.getInventory().append(ii, -1L);
					}
				}				
				cha.getInventory().remove(item[0], true);					
				cha.getInventory().count(this, getCount()-1L, true);		
				ItemInstance j = cha.getInventory().find("실",true);				
				ItemInstance jj = ItemDatabase.newInstance( ItemDatabase.find("실"));
				if(j!=null){
					long cnt = j.getCount();
					cha.getInventory().count(j, cnt+1L,true);
				}else{
					jj.setObjectId(ServerDatabase.nextItemObjId());
					cha.getInventory().append(jj, true);
				}
				
				/*Item it = ItemDatabase.find2("실");
				if(it != null){
					ItemInstance iii = ItemDatabase.newInstance(it);
					iii.setDefinite(false);
					cha.getInventory().append(iii, 1L);
					ItemDatabase.setPool(iii);
				}	*/			
			}else{
				//재료부족
				ChattingController.toChatting(cha, "필요한 재료는 판의 갈기털 1개 입니다.", 20);
			}	
			
		}else if(this.item.getName().indexOf("엔트의 껍질") > -1){
			int array[] = {1};
			for(ItemInstance ii : cha.getInventory().getList()){
				if(ii.getItem().getNameId().equalsIgnoreCase("$764") && ii.getCount()>=1){
					int temp = 0;
					int[] temp1 = array;
					temp1[temp] = (int)(temp1[temp] - ii.getCount());
					if(array[0]<0)
						array[0]=0;					
				}
			}
			if(array[0]==0){
				ItemInstance item[] = new ItemInstance[1];
			//	int i=0;
				for(ItemInstance ii : cha.getInventory().getList()){
					if(ii.getItem().getName().equals("버섯포자의 즙")){
						cha.getInventory().append(ii, -1L);
					}
				}				
				cha.getInventory().remove(item[0], true);					
				cha.getInventory().count(this, getCount()-1L, true);	
				ItemInstance j = cha.getInventory().find("엔트의 껍질",true);				
				ItemInstance jj = ItemDatabase.newInstance( ItemDatabase.find("엔트의 껍질"));
				if(j!=null){
					long cnt = j.getCount();
					cha.getInventory().count(j, cnt+1L,true);
				}else{
					jj.setObjectId(ServerDatabase.nextItemObjId());
					cha.getInventory().append(jj, true);
				}
				/*Item it = ItemDatabase.find2("엔트의껍질");
				if(it != null){
					ItemInstance iii = ItemDatabase.newInstance(it);
					iii.setDefinite(false);
					cha.getInventory().append(iii, 1L);
					ItemDatabase.setPool(iii);
				}	*/			
			}else{
				//재료부족
				ChattingController.toChatting(cha, "필요한 재료는 버섯포자의 즙 1개 입니다.", 20);
			}	
			
		}else if(this.item.getName().indexOf("미스릴") > -1){
			int array[] = {1};
			for(ItemInstance ii : cha.getInventory().getList()){
				if(ii.getItem().getNameId().equalsIgnoreCase("$761") && ii.getCount()>=1){
					int temp = 0;
					int[] temp1 = array;
					temp1[temp] = (int)(temp1[temp] - ii.getCount());
					if(array[0]<0)
						array[0]=0;					
				}
			}
			if(array[0]==0){
				ItemInstance item[] = new ItemInstance[1];
			//	int i=0;
				for(ItemInstance ii : cha.getInventory().getList()){
					if(ii.getItem().getName().equals("미스릴 원석")){
						cha.getInventory().append(ii, -1L);
					}
				}				
				cha.getInventory().remove(item[0], true);					
				cha.getInventory().count(this, getCount()-1L, true);				
				/*Item it = ItemDatabase.find2("미스릴");
				if(it != null){
					ItemInstance iii = ItemDatabase.newInstance(it);
					iii.setDefinite(false);
					cha.getInventory().append(iii, 20L);
					ItemDatabase.setPool(iii);
				}		*/		
				ItemInstance j = cha.getInventory().find("미스릴",true);				
				ItemInstance jj = ItemDatabase.newInstance( ItemDatabase.find("미스릴"));
				if(j!=null){
					long cnt = j.getCount();
					cha.getInventory().count(j, cnt+20L,true);
				}else{
					jj.setObjectId(ServerDatabase.nextItemObjId());
					cha.getInventory().append(jj, true);
					cha.getInventory().count(jj, 20L, true);
					//cha.getInventory().count(jj, 20L, true);
				}
			}else{
				//재료부족
				ChattingController.toChatting(cha, "필요한 재료는 미스릴 원석 1개 입니다.", 20);
			}			
		}else if(this.item.getName().indexOf("페어리 더스트") > -1){
			int array[] = {1};
			for(ItemInstance ii : cha.getInventory().getList()){
				if(ii.getItem().getNameId().equalsIgnoreCase("$762") && ii.getCount()>=1){
					int temp = 0;
					int[] temp1 = array;
					temp1[temp] = (int)(temp1[temp] - ii.getCount());
					if(array[0]<0)
						array[0]=0;					
				}
			}
			if(array[0]==0){
				ItemInstance item[] = new ItemInstance[1];
			//	int i=0;
				for(ItemInstance ii : cha.getInventory().getList()){
					if(ii.getItem().getName().equals("정령의 돌")){
						cha.getInventory().append(ii, -1L);
					}
				}				
				cha.getInventory().remove(item[0], true);					
				cha.getInventory().count(this, getCount()-1L, true);
				ItemInstance j = cha.getInventory().find("페어리 더스트",true);				
				ItemInstance jj = ItemDatabase.newInstance( ItemDatabase.find("페어리 더스트"));
				if(j!=null){
					long cnt = j.getCount();
					cha.getInventory().count(j, cnt+20L,true);
				}else{
					jj.setObjectId(ServerDatabase.nextItemObjId());
					cha.getInventory().append(jj, true);
					cha.getInventory().count(jj, 20L, true);
				}			
			}else{
				//재료부족
				ChattingController.toChatting(cha, "필요한 재료는 정령의 돌 1개 입니다.", 20);
			}
		}
	}
}
