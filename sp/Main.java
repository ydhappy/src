package sp;

import lineage.gui.GuiMain;
import lineage.plugin.PluginController;
import lineage.share.Common;

public class Main {

	public static void main(String[] args) {
		//
		PluginController.setPlugin(new Plugins());
		//
		Common.system_config_console = false;
		if(Common.system_config_console)
			lineage.Main.init();
		else
			GuiMain.open();
	}

}

/*
invGfx 사용 안하는 번호 목록.
 : 160, 161, 162, 164, 165, 169, 173, 174, 175, 176, 178, 179, 180
 : 181, 182, 183, 185~234, 263, 266, 268, 269, 270, 271, 273, 274, 277, 278, 280, 282, 283, 284, 285
 : 286, 288, 289, 290, 294, 295, 296, 297, 299, 300, 301, 310, 323, 331, 333~434, 439~440, 442~443, 458, 469, 
 : 480~486, 488~505, 511~516, 518, 521~525, 528~531, 533, 534, 537, 538, 543~546, 551~555, 
 : 558~590, 592, 596~607 
l1zero@naver.com
rkskekfk1

산타판도라 배경나무 1995
산타판도라 13377
*/
