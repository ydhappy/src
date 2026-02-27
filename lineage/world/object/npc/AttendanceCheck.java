package lineage.world.object.npc;

import java.util.ArrayList;
import java.util.List;

import lineage.database.AccountDatabase;
import lineage.database.ItemDatabase;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.ClientBasePacket;
import lineage.network.packet.server.S_Html;
import lineage.share.Lineage;
import lineage.share.Lineage_Balance;
import lineage.world.controller.ChattingController;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

public class AttendanceCheck extends object {

	@Override
	public void toTalk(PcInstance pc, ClientBasePacket cbp) {
		showHtml(pc);
	}
	
	public void showHtml(PcInstance pc){
			List<String> ynlist = new ArrayList<String>();

			ynlist.clear();
			ynlist.add(String.format("%d", pc.getDayptime() ));
			ynlist.add(String.format("%d", pc.getDaycount() ));
			
		
		
			switch (pc.getDaycount()) {
			
	            case 1:  
		            ynlist.add(String.format("%s",Lineage_Balance.dayc1));
		            ynlist.add(String.format("%d개",Lineage_Balance.daycc1));
		            break;
	            case 2:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc2));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc2));
		            	break;
	            case 3:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc3));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc3));
	            		break;
	            case 4:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc4));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc4));
	  
	              break;
	            case 5:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc5));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc5));
	  
	              break;
	            case 6:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc6));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc6));
	  
	              break;
	            case 7:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc7));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc7));
	  
	              break;
	            case 8:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc8));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc8));
	  
	              break;
	            case 9:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc9));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc9));
	  
	              break;
	            case 10:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc10));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc10));
	  
	              break;
	            case 11:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc11));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc11));
	  
	              break;
	            case 12:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc12));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc12));
	  
	              break;
	            case 13:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc13));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc13));
	  
	              break;
	            case 14:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc14));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc14));
	  
	              break;
	            case 15:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc15));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc15));
	  
	              break;
	            case 16:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc16));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc16));
	  
	              break;
	            case 17:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc17));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc17));
	  
	              break;
	            case 18:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc18));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc18));
	  
	              break;
	            case 19:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc19));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc19));
	  
	              break;
	            case 20:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc20));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc20));
	  
	              break;
	            case 21:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc21));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc21));
	  
	              break;
	            case 22:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc22));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc22));
	  
	              break;
	            case 23:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc23));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc23));
	  
	              break;
	            case 24:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc24));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc24));
	  
	              break;
	            case 25:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc25));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc25));
	  
	              break;
	            case 26:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc26));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc26));
	  
	              break;
	            case 27:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc27));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc27));
	  
	              break;
	            case 28:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc28));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc28));
	  
	              break;
	            case 29:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc29));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc29));
	  
	              break;
	            case 30:  
	              	ynlist.add(String.format("%s",Lineage_Balance.dayc30));
	            	ynlist.add(String.format("%d개",Lineage_Balance.daycc30));
	            	
	            	break;
	            default: 
	              break;
        }
			ynlist.add(String.format("%s", pc.getDaycheck() == 0? "[X]" : "[O]"));
			pc.toSender(S_Html.clone(BasePacketPooling.getPool(S_Html.class), this, "playcheck", null, ynlist));
		
	}
	
	@Override
	public void toTalk(PcInstance pc, String action, String type, ClientBasePacket cbp, Object...opt){

		if (action.equalsIgnoreCase("playcheck-checkp")) {
			
			if(pc.getDaycheck() == 1){
				ChattingController.toChatting(pc, "오늘은 이미 출석체크를 하였습니다.", Lineage.CHATTING_MODE_MESSAGE);
				return;
			}
			
			if(pc.getDayptime()>= Lineage_Balance.dayc){
				if(pc.getDaycheck() == 0 ){

					if(pc.getDaycount() == Lineage_Balance.lastday ){
						ChattingController.toChatting(pc, "출석체크를 전부 완료 하셨습니다.", Lineage.CHATTING_MODE_MESSAGE);
						return;
					}
		
					switch (pc.getDaycount()) {
		            case 1:  
			            ItemInstance ii = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc1));
						ii.setCount(Lineage_Balance.daycc1);
						pc.toGiveItem(null, ii, ii.getCount());
			            	break;
		            case 2:  
		               	ItemInstance ii2 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc2));
		               	ii2.setCount(Lineage_Balance.daycc2);
						pc.toGiveItem(null, ii2, ii2.getCount());
			            break;
		            case 3:  
		           		ItemInstance ii3 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc3));
		           		ii3.setCount(Lineage_Balance.daycc3);
		        		pc.toGiveItem(null, ii3, ii3.getCount());
		            	break;
		            case 4:  
		      			ItemInstance ii4 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc4));
	           			ii4.setCount(Lineage_Balance.daycc4);
	        			pc.toGiveItem(null, ii4, ii4.getCount());
	            		break;
		            case 5:  
			      		ItemInstance ii5 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc5));
		           		ii5.setCount(Lineage_Balance.daycc5);
		        		pc.toGiveItem(null, ii5, ii5.getCount());
		            	break;
		            case 6:  
		      			ItemInstance ii6 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc6));
	           			ii6.setCount(Lineage_Balance.daycc6);
	        			pc.toGiveItem(null, ii6, ii6.getCount());
	            		break;
		            case 7:  
		      			ItemInstance ii7 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc7));
	           			ii7.setCount(Lineage_Balance.daycc7);
	        			pc.toGiveItem(null, ii7, ii7.getCount());
	            		break;
		            case 8:  
		 		      	ItemInstance ii8 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc8));
		 	           	ii8.setCount(Lineage_Balance.daycc8);
		 	        	pc.toGiveItem(null, ii8, ii8.getCount());
		 	            break;
		            case 9:  
		 		      	ItemInstance ii9 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc9));
		 	           	ii9.setCount(Lineage_Balance.daycc9);
		 	        	pc.toGiveItem(null, ii9, ii9.getCount());
		 	            break; 
		            case 10:  
			 		    ItemInstance ii10 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc10));
			 	        ii10.setCount(Lineage_Balance.daycc10);
			 	        pc.toGiveItem(null, ii10, ii10.getCount());
			 	        break;   
		            case 11:  
		 		      	ItemInstance ii11 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc11));
		 	           	ii11.setCount(Lineage_Balance.daycc11);
		 	        	pc.toGiveItem(null, ii11, ii11.getCount());
		 	            break; 
		            case 12:  
		 		      	ItemInstance ii12 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc12));
		 	           	ii12.setCount(Lineage_Balance.daycc12);
		 	        	pc.toGiveItem(null, ii12, ii12.getCount());
		 	            break; 
		            case 13:  
		 		      	ItemInstance ii13 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc13));
		 	           	ii13.setCount(Lineage_Balance.daycc13);
		 	        	pc.toGiveItem(null, ii13, ii13.getCount());
		 	            break; 
		            case 14:  
		 		      	ItemInstance ii14 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc14));
		 	           	ii14.setCount(Lineage_Balance.daycc14);
		 	        	pc.toGiveItem(null, ii14, ii14.getCount());
		 	            break; 
		            case 15:  
		 		      	ItemInstance ii15 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc15));
		 	           	ii15.setCount(Lineage_Balance.daycc15);
		 	        	pc.toGiveItem(null, ii15, ii15.getCount());
		 	            break; 
		            case 16:  
				 		ItemInstance ii16 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc16));
				 	    ii16.setCount(Lineage_Balance.daycc16);
				 	    pc.toGiveItem(null, ii16, ii16.getCount());
				 	    break; 
		            case 17:  
		 		      	ItemInstance ii17 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc17));
		 	           	ii17.setCount(Lineage_Balance.daycc17);
		 	        	pc.toGiveItem(null, ii17, ii17.getCount());
		 	            break;   
		            case 18:  
		 		      	ItemInstance ii18 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc18));
		 	           	ii18.setCount(Lineage_Balance.daycc18);
		 	        	pc.toGiveItem(null, ii18, ii18.getCount());
		 	            break;  
		            case 19:  
		 		      	ItemInstance ii19 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc19));
		 	           	ii19.setCount(Lineage_Balance.daycc19);
		 	        	pc.toGiveItem(null, ii19, ii19.getCount());
		 	            break;     
		            case 20:  
		 		      	ItemInstance ii20 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc20));
		 	           	ii20.setCount(Lineage_Balance.daycc20);
		 	        	pc.toGiveItem(null, ii20, ii20.getCount());
		 	            break;  
		            case 21:  
		 		      	ItemInstance ii21 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc21));
		 	           	ii21.setCount(Lineage_Balance.daycc21);
		 	        	pc.toGiveItem(null, ii21, ii21.getCount());
		 	            break;   
		            case 22:  
		 		      	ItemInstance ii22 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc22));
		 	           	ii22.setCount(Lineage_Balance.daycc22);
		 	        	pc.toGiveItem(null, ii22, ii22.getCount());
		 	            break;  
		            case 23:  
		 		      	ItemInstance ii23 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc23));
		 	           	ii23.setCount(Lineage_Balance.daycc23);
		 	        	pc.toGiveItem(null, ii23, ii23.getCount());
		 	            break;   
		            case 24:  
		 		      	ItemInstance ii24 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc24));
		 	           	ii24.setCount(Lineage_Balance.daycc24);
		 	        	pc.toGiveItem(null, ii24, ii24.getCount());
		 	            break;
		            case 25:  
		 		      	ItemInstance ii25 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc25));
		 	           	ii25.setCount(Lineage_Balance.daycc25);
		 	        	pc.toGiveItem(null, ii25, ii25.getCount());
		 	            break;  
		            case 26:  
		 		      	ItemInstance ii26 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc26));
		 	           	ii26.setCount(Lineage_Balance.daycc26);
		 	        	pc.toGiveItem(null, ii26, ii26.getCount());
		 	            break;  
		            case 27:  
		 		      	ItemInstance ii27 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc27));
		 	           	ii27.setCount(Lineage_Balance.daycc27);
		 	        	pc.toGiveItem(null, ii27, ii27.getCount());
		 	            break;  
		            case 28:  
		 		      	ItemInstance ii28 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc28));
		 	           	ii28.setCount(Lineage_Balance.daycc28);
		 	        	pc.toGiveItem(null, ii28, ii28.getCount());
		 	            break; 
		            case 29:  
		 		      	ItemInstance ii29 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc29));
		 	           	ii29.setCount(Lineage_Balance.daycc29);
		 	        	pc.toGiveItem(null, ii29, ii29.getCount());
		 	            break;  
		            case 30:  
		 		      	ItemInstance ii30 = ItemDatabase.newInstance(ItemDatabase.find(Lineage_Balance.dayc30));
		 	           	ii30.setCount(Lineage_Balance.daycc30);
		 	        	pc.toGiveItem(null, ii30, ii30.getCount());
		 	            break;   
			            default: 
			              break;
					}
					pc.setDaycheck(1);
					pc.setDaycount(pc.getDaycount()+1);
					AccountDatabase.updateDaycp(1,pc.getAccountUid());
					AccountDatabase.updateDaycount(pc.getDaycount(),pc.getAccountUid());
					AccountDatabase.updateDaycheck(pc.getAccountUid());
					ChattingController.toChatting(pc, String.format("%d일차 출석체크가 완료 되었습니다.", pc.getDaycount()-1), Lineage.CHATTING_MODE_MESSAGE);
					ChattingController.toChatting(pc, String.format("%d일차 출석체크 보상이 지급되었습니다. ", pc.getDaycount()-1), Lineage.CHATTING_MODE_MESSAGE);
				}
			}else{
				ChattingController.toChatting(pc, String.format("출석체크 완료까지 %d초 남았습니다.", Lineage_Balance.dayc - pc.getDayptime()), Lineage.CHATTING_MODE_MESSAGE);
			}
		}
		
		showHtml(pc);
	}
}