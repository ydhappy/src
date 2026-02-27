package lineage.plugin;

import java.sql.Connection;
import java.util.Map;
import java.util.StringTokenizer;

import lineage.bean.database.Item;
import lineage.bean.database.Npc;
import lineage.bean.database.Skill;
import lineage.bean.event.Timer;
import lineage.database.ItemDatabase;
import lineage.database.NpcSpawnlistDatabase;
import lineage.database.SkillDatabase;
import lineage.network.packet.client.C_ItemClick;
import lineage.network.packet.client.C_ItemDrop;
import lineage.share.Lineage;
import lineage.world.controller.AutoHuntCheckController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.CommandController;
import lineage.world.controller.DamageController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.ItemWeaponInstance;
import lineage.world.object.instance.MonsterInstance;
import lineage.world.object.instance.PcInstance;

final public class Plugins implements Plugin {

	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public Object init(Class<?> c, Object... opt) {
		try {
			//
			
			if( c.toString().equalsIgnoreCase(lineage.Main.class.toString()) ){
				if(opt[0].equals("toLoading")){
					//
				}
				if(opt[0].equals("toDelete")){
					//
				}
			}
			
			if( c.toString().equalsIgnoreCase(ItemDatabase.class.toString()) ){
				if(opt[0].equals("newInstance")){
					Item item = (Item)opt[1];
					if(item.getType2().startsWith("[test] ")){
						String key = item.getType2().substring(0, 6).trim();
						String value = item.getType2().substring(key.length()).trim();
					}
				}
			}
			
			if( c.toString().equalsIgnoreCase(NpcSpawnlistDatabase.class.toString()) ){
				if(opt[0].equals("newObject")){
					Npc npc = (Npc)opt[1];
					//
				}
			}
			
			if( c.toString().equalsIgnoreCase(ItemWeaponInstance.class.toString()) ){
				if(opt[0].equals("toDamage")){
					ItemWeaponInstance weapon = (ItemWeaponInstance)opt[1];
					Character cha = (Character)opt[2];
					object o = (object)opt[3];
					//
				}
			}
			
			if( c.toString().equalsIgnoreCase(PcInstance.class.toString()) ){
				if(opt[0].equals("toWorldJoin")){
					PcInstance pc = (PcInstance)opt[1];
					//
				}
			}
			
			if( c.toString().equalsIgnoreCase(SkillDatabase.class.toString()) ){
				if(opt[0].equals("init")){
					Connection con = (Connection)opt[1];
					Map<Integer, Skill> list = (Map<Integer, Skill>)opt[2];
					//
				}
			}
			
			if( c.toString().equalsIgnoreCase(CommandController.class.toString()) ){
				if(opt[0].equals("toCommand")){
					object o = (object)opt[1];
					String cmd = (String)opt[2];
					StringTokenizer st = (StringTokenizer)opt[3];
				}
			}
			
			if( c.toString().equalsIgnoreCase(C_ItemClick.class.toString()) ){
				if(opt[0].equals("init")){
					C_ItemClick cic = (C_ItemClick)opt[1];
					PcInstance pc = (PcInstance)opt[2];
					ItemInstance item = (ItemInstance)opt[3];
				}
			}
			
			if( c.toString().equalsIgnoreCase(C_ItemDrop.class.toString()) ){
				if(opt[0].equals("init")){
					C_ItemDrop cid = (C_ItemDrop)opt[1];
					PcInstance pc = (PcInstance)opt[2];
					ItemInstance item = (ItemInstance)opt[3];
				}
			}
			
			if( c.toString().equalsIgnoreCase(DamageController.class.toString()) ){
				if(opt[0].equals("getDamage")){
					Character cha = (Character)opt[1];
					object target = (object)opt[2];
					boolean bow = (Boolean)opt[3];
					ItemInstance weapon = (ItemInstance)opt[4];
					ItemInstance arrow = (ItemInstance)opt[5];
				}
			}
			
			if( c.toString().equalsIgnoreCase(DamageController.class.toString()) ){
				if(opt[0].equals("DmgWeaponFigure")){
					boolean bow = (Boolean)opt[1];
					ItemInstance weapon = (ItemInstance)opt[2];
					ItemInstance arrow = (ItemInstance)opt[3];
					boolean Small = (Boolean)opt[4];
				}
			}
			
			if( c.toString().equalsIgnoreCase(DamageController.class.toString()) ){
				if(opt[0].equals("DmgFigure")){
					Character cha = (Character)opt[1];
					boolean bow = (Boolean)opt[2];
				}
			}
			
			if( c.toString().equalsIgnoreCase(MonsterInstance.class.toString()) ){
				if(opt[0].equals("toDamage.손상처리")){
					Character cha = (Character)opt[1];
				}
			}
			
			if( c.toString().equalsIgnoreCase(Timer.class.toString()) ){
				if(opt[0].equals("toTimer")){
					Long time = (Long)opt[1];
				}
			}
		
			
			
		} catch (Exception e) { }
		
		return null;
	}
	
}
