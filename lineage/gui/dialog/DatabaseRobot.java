package lineage.gui.dialog;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

import lineage.bean.database.Exp;
import lineage.bean.event.ReloadRobot;
import lineage.database.ExpDatabase;
import lineage.gui.GuiMain;
import lineage.share.Lineage;
import lineage.thread.EventThread;
import lineage.util.SpriteTools;
import lineage.world.World;
import lineage.world.controller.DamageController;
import lineage.world.controller.SkillController;
import lineage.world.object.instance.ItemInstance;
import lineage.world.object.instance.PcInstance;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.swtdesigner.SWTResourceManager;

public class DatabaseRobot {

	static private Shell shell;
	// 해당 창에 타이틀 명
	static private String title;
	//
	private static Composite composite_10;
	private static Composite 귀걸이;
	private static Composite 투구;
	private static Composite 목걸이;
	private static Composite 티셔츠;
	private static Composite 갑옷;
	private static Composite 망토;
	private static Composite 반지1;
	private static Composite 벨트;
	private static Composite 방패;
	private static Composite 무기;
	private static Composite 장갑;
	private static Composite 반지2;
	private static Composite 부츠;
	private static Label textlevel;
	private static Label texthp;
	private static Label textmp;
	private static Label textweight;
	private static Label textfood;
	private static Label textstr;
	private static Label textdex;
	private static Label textcon;
	private static Label textint;
	private static Label textwis;
	private static Label textcha;
	private static Label textmagiclevel;
	private static Label textmagicbonus;
	private static Label textlawful;
	private static Label textearth;
	private static Label textwater;
	private static Label textfire;
	private static Label textwind;
	private static Label texter;
	private static Composite inventory;
	private static Slider slider;
	private static Button btnNewButton;
	
	static {
		title = "[데이타베이스] 로봇";
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	static public void open() {
		//
		shell = new Shell(GuiMain.shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.MAX);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
		shell.setSize(640, 480);
		shell.setText(title);
		GridLayout gl_shell = new GridLayout(1, false);
		gl_shell.verticalSpacing = 0;
		gl_shell.marginWidth = 0;
		gl_shell.marginHeight = 0;
		gl_shell.horizontalSpacing = 0;
		shell.setLayout(gl_shell);
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Memory");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(composite);
		composite.setLayout(new GridLayout(2, false));
		
		final List list = new List(composite, SWT.BORDER | SWT.V_SCROLL);
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//
				for(Control c : inventory.getChildren())
					c.dispose();
				투구.getChildren()[0].setBackgroundImage(null);
				귀걸이.getChildren()[0].setBackgroundImage(null);
				목걸이.getChildren()[0].setBackgroundImage(null);
				티셔츠.getChildren()[0].setBackgroundImage(null);
				갑옷.getChildren()[0].setBackgroundImage(null);
				망토.getChildren()[0].setBackgroundImage(null);
				반지1.getChildren()[0].setBackgroundImage(null);
				반지2.getChildren()[0].setBackgroundImage(null);
				벨트.getChildren()[0].setBackgroundImage(null);
				장갑.getChildren()[0].setBackgroundImage(null);
				방패.getChildren()[0].setBackgroundImage(null);
				무기.getChildren()[0].setBackgroundImage(null);
				부츠.getChildren()[0].setBackgroundImage(null);
				//
				String name = list.getItem( list.getSelectionIndex() );
				PcInstance pc = (PcInstance)list.getData(name);
				//
				Image img = SpriteTools.get(pc.getClassGfx(), 5, 0);
				if(img != null) {
					BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
					Graphics g = bi.getGraphics();
					g.drawImage(img, 0, 0, null);
					g.dispose();
					composite_10.setSize(img.getWidth(null), img.getHeight(null));
					composite_10.setBackgroundImage( new org.eclipse.swt.graphics.Image(null, convertToSWT(bi)) );
				}
				//
				for(int i=1 ; i<=Lineage.SLOT_AMULET5 ; ++i) {
					img = null;
					Composite c = null;
					ItemInstance item = pc.getInventory().getSlot(i);
					if(item != null) {
						img = SpriteTools.getTbt(item.getItem().getInvGfx());
						if(i == 1)
							c = 투구;
						else if(i == 12)
							c = 귀걸이;
						else if(i == 10)
							c = 목걸이;
						else if(i == 3)
							c = 티셔츠;
						else if(i == 2)
							c = 갑옷;
						else if(i == 4)
							c = 망토;
						else if(i == 18)
							c = 반지1;
						else if(i == 11)
							c = 벨트;
						else if(i == 6)
							c = 장갑;
						else if(i == 7)
							c = 방패;
						else if(i == 8)
							c = 무기;
						else if(i == 5)
							c = 부츠;
					}
					if(img != null) {
						BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
						Graphics g = bi.getGraphics();
						g.drawImage(img, 0, 0, null);
						g.dispose();
						
						c.getChildren()[0].setSize(img.getWidth(null), img.getHeight(null));
						c.getChildren()[0].setBackgroundImage( new org.eclipse.swt.graphics.Image(null, convertToSWT(bi)) );
						c.getChildren()[0].setToolTipText( item.toStringDB() );
					}
				}
				//
				Exp exp = ExpDatabase.find(pc.getLevel());
				double a = exp.getBonus() - exp.getExp();
				double b = pc.getExp() - a;
				textlevel.setText(String.format("%d %.2f%%", pc.getLevel(), (b/exp.getExp())*100));
				texthp.setText(String.format("%d/%d", pc.getNowHp(), pc.getTotalHp()));
				textmp.setText(String.format("%d/%d", pc.getNowMp(), pc.getTotalMp()));
				textweight.setText(String.valueOf(Math.round(pc.getInventory().getWeightPercent())));
				textfood.setText(String.valueOf((int)(((double)pc.getFood()/(double)Lineage.MAX_FOOD)*100)));
				textstr.setText(String.valueOf(pc.getTotalStr()));
				textdex.setText(String.valueOf(pc.getTotalDex()));
				textcon.setText(String.valueOf(pc.getTotalCon()));
				textint.setText(String.valueOf(pc.getTotalInt()));
				textwis.setText(String.valueOf(pc.getTotalWis()));
				textcha.setText(String.valueOf(pc.getTotalCha()));
				textmagiclevel.setText(String.valueOf(SkillController.getMagicLevel(pc)));
				textmagicbonus.setText(String.valueOf(SkillController.getMagicBonus(pc)));
				textlawful.setText(String.valueOf(pc.getLawful()-Lineage.NEUTRAL));
				textearth.setText(String.valueOf(pc.getTotalEarthress()));
				textwater.setText(String.valueOf(pc.getTotalWaterress()));
				textfire.setText(String.valueOf(pc.getTotalFireress()));
				textwind.setText(String.valueOf(pc.getTotalWindress()));
				//texter.setText(String.valueOf(DamageController.getEr(pc));
				//
				for(ItemInstance item : pc.getInventory().getList())
					addInventoryItem( item );
			}
		});
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		readRobot(list);
		
		TabFolder tabFolder_1 = new TabFolder(composite, SWT.NONE);
		tabFolder_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		
		TabItem tbtmNewItem_3 = new TabItem(tabFolder_1, SWT.NONE);
		tbtmNewItem_3.setText("슬롯창");
		
		Composite composite_3 = new Composite(tabFolder_1, SWT.NONE);
		tbtmNewItem_3.setControl(composite_3);
		GridLayout gl_composite_3 = new GridLayout(1, false);
		gl_composite_3.marginHeight = 10;
		gl_composite_3.marginWidth = 10;
		composite_3.setLayout(gl_composite_3);
		
		Composite composite_4 = new Composite(composite_3, SWT.NONE);
		composite_4.setLayout(new FormLayout());
		GridData gd_composite_4 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_4.heightHint = 208;
		gd_composite_4.widthHint = 139;
		composite_4.setLayoutData(gd_composite_4);
		composite_4.setBackgroundMode(SWT.INHERIT_DEFAULT);
		composite_4.setBackgroundImage(SWTResourceManager.getImage("images/bg_inventory_slot.png"));
		
		귀걸이 = new Composite(composite_4, SWT.NONE);
		GridLayout gl_귀걸이 = new GridLayout(1, false);
		gl_귀걸이.marginWidth = 0;
		gl_귀걸이.marginHeight = 0;
		귀걸이.setLayout(gl_귀걸이);
		FormData fd_귀걸이 = new FormData();
		fd_귀걸이.bottom = new FormAttachment(0, 45);
		fd_귀걸이.right = new FormAttachment(0, 46);
		fd_귀걸이.top = new FormAttachment(0, 14);
		fd_귀걸이.left = new FormAttachment(0, 13);
		귀걸이.setLayoutData(fd_귀걸이);
		
		Composite composite_12 = new Composite(귀걸이, SWT.NONE);
		
		투구 = new Composite(composite_4, SWT.NONE);
		GridLayout gl_투구 = new GridLayout(1, false);
		gl_투구.marginWidth = 0;
		gl_투구.marginHeight = 0;
		투구.setLayout(gl_투구);
		FormData fd_투구 = new FormData();
		fd_투구.bottom = new FormAttachment(0, 39);
		fd_투구.right = new FormAttachment(0, 85);
		fd_투구.top = new FormAttachment(0, 8);
		fd_투구.left = new FormAttachment(0, 52);
		투구.setLayoutData(fd_투구);
		
		Composite composite_13 = new Composite(투구, SWT.NONE);
		
		목걸이 = new Composite(composite_4, SWT.NONE);
		GridLayout gl_목걸이 = new GridLayout(1, false);
		gl_목걸이.marginHeight = 0;
		gl_목걸이.marginWidth = 0;
		목걸이.setLayout(gl_목걸이);
		FormData fd_목걸이 = new FormData();
		fd_목걸이.bottom = new FormAttachment(0, 45);
		fd_목걸이.right = new FormAttachment(0, 124);
		fd_목걸이.top = new FormAttachment(0, 14);
		fd_목걸이.left = new FormAttachment(0, 91);
		목걸이.setLayoutData(fd_목걸이);
		
		Composite composite_14 = new Composite(목걸이, SWT.NONE);
		
		티셔츠 = new Composite(composite_4, SWT.NONE);
		GridLayout gl_티셔츠 = new GridLayout(1, false);
		gl_티셔츠.marginHeight = 0;
		gl_티셔츠.marginWidth = 0;
		티셔츠.setLayout(gl_티셔츠);
		FormData fd_티셔츠 = new FormData();
		fd_티셔츠.bottom = new FormAttachment(0, 82);
		fd_티셔츠.right = new FormAttachment(0, 46);
		fd_티셔츠.top = new FormAttachment(0, 51);
		fd_티셔츠.left = new FormAttachment(0, 13);
		티셔츠.setLayoutData(fd_티셔츠);
		
		Composite composite_15 = new Composite(티셔츠, SWT.NONE);
		
		갑옷 = new Composite(composite_4, SWT.NONE);
		GridLayout gl_갑옷 = new GridLayout(1, false);
		gl_갑옷.marginHeight = 0;
		gl_갑옷.marginWidth = 0;
		갑옷.setLayout(gl_갑옷);
		FormData fd_갑옷 = new FormData();
		fd_갑옷.bottom = new FormAttachment(0, 82);
		fd_갑옷.right = new FormAttachment(0, 85);
		fd_갑옷.top = new FormAttachment(0, 51);
		fd_갑옷.left = new FormAttachment(0, 52);
		갑옷.setLayoutData(fd_갑옷);
		
		Composite composite_16 = new Composite(갑옷, SWT.NONE);
		
		망토 = new Composite(composite_4, SWT.NONE);
		GridLayout gl_망토 = new GridLayout(1, false);
		gl_망토.marginHeight = 0;
		gl_망토.marginWidth = 0;
		망토.setLayout(gl_망토);
		FormData fd_망토 = new FormData();
		fd_망토.bottom = new FormAttachment(0, 82);
		fd_망토.right = new FormAttachment(0, 124);
		fd_망토.top = new FormAttachment(0, 51);
		fd_망토.left = new FormAttachment(0, 91);
		망토.setLayoutData(fd_망토);
		
		Composite composite_17 = new Composite(망토, SWT.NONE);
		
		반지1 = new Composite(composite_4, SWT.NONE);
		GridLayout gl_반지1 = new GridLayout(1, false);
		gl_반지1.marginHeight = 0;
		gl_반지1.marginWidth = 0;
		반지1.setLayout(gl_반지1);
		FormData fd_반지1 = new FormData();
		fd_반지1.bottom = new FormAttachment(0, 119);
		fd_반지1.right = new FormAttachment(0, 41);
		fd_반지1.top = new FormAttachment(0, 88);
		fd_반지1.left = new FormAttachment(0, 8);
		반지1.setLayoutData(fd_반지1);
		
		Composite composite_18 = new Composite(반지1, SWT.NONE);
		
		벨트 = new Composite(composite_4, SWT.NONE);
		GridLayout gl_벨트 = new GridLayout(1, false);
		gl_벨트.marginHeight = 0;
		gl_벨트.marginWidth = 0;
		벨트.setLayout(gl_벨트);
		FormData fd_벨트 = new FormData();
		fd_벨트.bottom = new FormAttachment(0, 125);
		fd_벨트.right = new FormAttachment(0, 86);
		fd_벨트.top = new FormAttachment(0, 94);
		fd_벨트.left = new FormAttachment(0, 53);
		벨트.setLayoutData(fd_벨트);
		
		Composite composite_19 = new Composite(벨트, SWT.NONE);
		
		방패 = new Composite(composite_4, SWT.NONE);
		GridLayout gl_방패 = new GridLayout(1, false);
		gl_방패.marginHeight = 0;
		gl_방패.marginWidth = 0;
		방패.setLayout(gl_방패);
		FormData fd_방패 = new FormData();
		fd_방패.bottom = new FormAttachment(0, 119);
		fd_방패.right = new FormAttachment(0, 129);
		fd_방패.top = new FormAttachment(0, 88);
		fd_방패.left = new FormAttachment(0, 96);
		방패.setLayoutData(fd_방패);
		
		Composite composite_20 = new Composite(방패, SWT.NONE);
		
		무기 = new Composite(composite_4, SWT.NONE);
		FormData fd_무기 = new FormData();
		fd_무기.bottom = new FormAttachment(0, 151);
		fd_무기.right = new FormAttachment(0, 41);
		fd_무기.top = new FormAttachment(0, 120);
		fd_무기.left = new FormAttachment(0, 8);
		무기.setLayoutData(fd_무기);
		GridLayout gl_무기 = new GridLayout(1, false);
		gl_무기.marginHeight = 0;
		gl_무기.marginWidth = 0;
		무기.setLayout(gl_무기);
		
		Composite composite_11 = new Composite(무기, SWT.NONE);
		
		장갑 = new Composite(composite_4, SWT.NONE);
		GridLayout gl_장갑 = new GridLayout(1, false);
		gl_장갑.marginHeight = 0;
		gl_장갑.marginWidth = 0;
		장갑.setLayout(gl_장갑);
		FormData fd_장갑 = new FormData();
		fd_장갑.bottom = new FormAttachment(0, 183);
		fd_장갑.right = new FormAttachment(0, 41);
		fd_장갑.top = new FormAttachment(0, 152);
		fd_장갑.left = new FormAttachment(0, 8);
		장갑.setLayoutData(fd_장갑);
		
		Composite composite_21 = new Composite(장갑, SWT.NONE);
		
		반지2 = new Composite(composite_4, SWT.NONE);
		GridLayout gl_반지2 = new GridLayout(1, false);
		gl_반지2.marginHeight = 0;
		gl_반지2.marginWidth = 0;
		반지2.setLayout(gl_반지2);
		FormData fd_반지2 = new FormData();
		fd_반지2.bottom = new FormAttachment(0, 151);
		fd_반지2.right = new FormAttachment(0, 129);
		fd_반지2.top = new FormAttachment(0, 120);
		fd_반지2.left = new FormAttachment(0, 96);
		반지2.setLayoutData(fd_반지2);
		
		Composite composite_22 = new Composite(반지2, SWT.NONE);
		
		부츠 = new Composite(composite_4, SWT.NONE);
		GridLayout gl_부츠 = new GridLayout(1, false);
		gl_부츠.marginHeight = 0;
		gl_부츠.marginWidth = 0;
		부츠.setLayout(gl_부츠);
		FormData fd_부츠 = new FormData();
		fd_부츠.bottom = new FormAttachment(0, 183);
		fd_부츠.right = new FormAttachment(0, 86);
		fd_부츠.top = new FormAttachment(0, 152);
		fd_부츠.left = new FormAttachment(0, 53);
		부츠.setLayoutData(fd_부츠);
		
		Composite composite_23 = new Composite(부츠, SWT.NONE);
		
		composite_10 = new Composite(composite_3, SWT.NONE);
		composite_10.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		
		TabItem tbtmNewItem_4 = new TabItem(tabFolder_1, SWT.NONE);
		tbtmNewItem_4.setText("스탯창");
		
		Composite composite_5 = new Composite(tabFolder_1, SWT.NONE);
		tbtmNewItem_4.setControl(composite_5);
		GridLayout gl_composite_5 = new GridLayout(1, false);
		gl_composite_5.marginHeight = 10;
		gl_composite_5.marginWidth = 10;
		composite_5.setLayout(gl_composite_5);
		
		Composite composite_6 = new Composite(composite_5, SWT.NONE);
		composite_6.setBackgroundMode(SWT.INHERIT_DEFAULT);
		GridData gd_composite_6 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_6.heightHint = 208;
		gd_composite_6.widthHint = 139;
		composite_6.setLayoutData(gd_composite_6);
		composite_6.setBackgroundImage(SWTResourceManager.getImage("images/bg_character_stat.png"));
		composite_6.setBounds(0, 0, 64, 64);
		
		textlevel = new Label(composite_6, SWT.NONE);
		textlevel.setFont(SWTResourceManager.getFont("돋움", 7, SWT.BOLD));
		textlevel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textlevel.setBounds(75, 9, 56, 15);
		textlevel.setText("99 100.00%");
		
		texthp = new Label(composite_6, SWT.NONE);
		texthp.setAlignment(SWT.CENTER);
		texthp.setForeground(new Color(null, 247, 189, 247));
		texthp.setFont(SWTResourceManager.getFont("돋움", 7, SWT.BOLD));
		texthp.setBounds(65, 26, 70, 15);
		texthp.setText("2000/2000");
		
		textmp = new Label(composite_6, SWT.NONE);
		textmp.setAlignment(SWT.CENTER);
		textmp.setFont(SWTResourceManager.getFont("돋움", 7, SWT.BOLD));
		textmp.setForeground(new Color(null, 189, 222, 255));
		textmp.setBounds(65, 40, 70, 15);
		textmp.setText("2000/2000");
		
		textweight = new Label(composite_6, SWT.NONE);
		textweight.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textweight.setFont(SWTResourceManager.getFont("굴림체", 8, SWT.BOLD));
		textweight.setAlignment(SWT.CENTER);
		textweight.setBounds(85, 56, 44, 12);
		textweight.setText("0%");
		
		textfood = new Label(composite_6, SWT.NONE);
		textfood.setAlignment(SWT.CENTER);
		textfood.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textfood.setFont(SWTResourceManager.getFont("굴림체", 8, SWT.BOLD));
		textfood.setBounds(85, 69, 44, 12);
		textfood.setText("0%");
		
		textstr = new Label(composite_6, SWT.NONE);
		textstr.setAlignment(SWT.CENTER);
		textstr.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textstr.setFont(SWTResourceManager.getFont("굴림체", 8, SWT.BOLD));
		textstr.setBounds(31, 88, 35, 12);
		textstr.setText("00");
		
		textdex = new Label(composite_6, SWT.NONE);
		textdex.setAlignment(SWT.CENTER);
		textdex.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textdex.setFont(SWTResourceManager.getFont("굴림체", 8, SWT.BOLD));
		textdex.setBounds(31, 101, 35, 12);
		textdex.setText("00");
		
		textcon = new Label(composite_6, SWT.NONE);
		textcon.setAlignment(SWT.CENTER);
		textcon.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textcon.setFont(SWTResourceManager.getFont("굴림체", 8, SWT.BOLD));
		textcon.setBounds(31, 114, 35, 12);
		textcon.setText("00");
		
		textint = new Label(composite_6, SWT.NONE);
		textint.setAlignment(SWT.CENTER);
		textint.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textint.setFont(SWTResourceManager.getFont("굴림체", 8, SWT.BOLD));
		textint.setBounds(94, 88, 35, 12);
		textint.setText("00");
		
		textwis = new Label(composite_6, SWT.NONE);
		textwis.setAlignment(SWT.CENTER);
		textwis.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textwis.setFont(SWTResourceManager.getFont("굴림체", 8, SWT.BOLD));
		textwis.setBounds(94, 101, 35, 12);
		textwis.setText("00");
		
		textcha = new Label(composite_6, SWT.NONE);
		textcha.setAlignment(SWT.CENTER);
		textcha.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textcha.setFont(SWTResourceManager.getFont("굴림체", 8, SWT.BOLD));
		textcha.setBounds(94, 114, 35, 12);
		textcha.setText("00");
		
		textmagiclevel = new Label(composite_6, SWT.NONE);
		textmagiclevel.setAlignment(SWT.CENTER);
		textmagiclevel.setForeground(new Color(null, 189, 222, 255));
		textmagiclevel.setFont(SWTResourceManager.getFont("굴림체", 8, SWT.BOLD));
		textmagiclevel.setBounds(66, 133, 25, 12);
		textmagiclevel.setText("0");
		
		textmagicbonus = new Label(composite_6, SWT.NONE);
		textmagicbonus.setAlignment(SWT.CENTER);
		textmagicbonus.setForeground(new Color(null, 189, 222, 255));
		textmagicbonus.setFont(SWTResourceManager.getFont("굴림체", 8, SWT.BOLD));
		textmagicbonus.setBounds(66, 146, 25, 12);
		textmagicbonus.setText("0");
		
		textlawful = new Label(composite_6, SWT.NONE);
		textlawful.setAlignment(SWT.CENTER);
		textlawful.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textlawful.setFont(SWTResourceManager.getFont("굴림체", 8, SWT.BOLD));
		textlawful.setBounds(98, 147, 35, 12);
		textlawful.setText("0");
		
		textearth = new Label(composite_6, SWT.NONE);
		textearth.setAlignment(SWT.CENTER);
		textearth.setForeground(new Color(null, 197, 239, 181));
		textearth.setFont(SWTResourceManager.getFont("굴림체", 8, SWT.BOLD));
		textearth.setBounds(37, 167, 19, 12);
		textearth.setText("0");
		
		textwater = new Label(composite_6, SWT.NONE);
		textwater.setAlignment(SWT.CENTER);
		textwater.setForeground(new Color(null, 189, 222, 255));
		textwater.setFont(SWTResourceManager.getFont("굴림체", 8, SWT.BOLD));
		textwater.setBounds(37, 180, 19, 12);
		textwater.setText("0");
		
		textfire = new Label(composite_6, SWT.NONE);
		textfire.setAlignment(SWT.CENTER);
		textfire.setForeground(new Color(null, 255, 206, 206));
		textfire.setFont(SWTResourceManager.getFont("굴림체", 8, SWT.BOLD));
		textfire.setBounds(104, 167, 19, 12);
		textfire.setText("0");
		
		textwind = new Label(composite_6, SWT.NONE);
		textwind.setAlignment(SWT.CENTER);
		textwind.setForeground(new Color(null, 189, 189, 189));
		textwind.setFont(SWTResourceManager.getFont("굴림체", 8, SWT.BOLD));
		textwind.setBounds(104, 180, 19, 12);
		textwind.setText("0");
		
		texter = new Label(composite_6, SWT.NONE);
		texter.setAlignment(SWT.CENTER);
		texter.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		texter.setFont(SWTResourceManager.getFont("굴림체", 8, SWT.BOLD));
		texter.setBounds(27, 193, 22, 12);
		texter.setText("0");
		
		TabItem tbtmNewItem_5 = new TabItem(tabFolder_1, SWT.NONE);
		tbtmNewItem_5.setText("인벤토리창");
		
		Composite composite_7 = new Composite(tabFolder_1, SWT.NONE);
		tbtmNewItem_5.setControl(composite_7);
		GridLayout gl_composite_7 = new GridLayout(2, false);
		gl_composite_7.verticalSpacing = 0;
		gl_composite_7.horizontalSpacing = 0;
		gl_composite_7.marginHeight = 10;
		gl_composite_7.marginWidth = 10;
		composite_7.setLayout(gl_composite_7);
		
		Composite composite_8 = new Composite(composite_7, SWT.NONE);
		composite_8.setLayout(null);
		composite_8.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		composite_8.setBounds(0, 0, 64, 64);
		
		inventory = new Composite(composite_8, SWT.NONE);
		inventory.setBackgroundMode(SWT.INHERIT_DEFAULT);
		inventory.setBackgroundImage(SWTResourceManager.getImage("images/bg_inventory.png"));
		inventory.setBounds(0, 0, 137, 1701);
		inventory.setLayout(null);
		
		slider = new Slider(composite_7, SWT.VERTICAL);
		slider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				inventory.setLocation(0, (~slider.getSelection())+1);
			}
		});
		slider.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		slider.setMaximum(inventory.getBounds().height - 365);
		slider.setThumb(1);
		
		TabItem tbtmNewItem_1 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_1.setText("DataBase");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem_1.setControl(composite_1);
		composite_1.setLayout(new GridLayout(1, false));
		
		btnNewButton = new Button(composite_1, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
				messageBox.setText( "경고" );
				messageBox.setMessage("정말 디비를 다시 읽겠습니까?");
				if(messageBox.open() == SWT.YES) {
					EventThread.append(ReloadRobot.clone(EventThread.getPool(ReloadRobot.class)));
					shell.close();
				}
			}
		});
		btnNewButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		btnNewButton.setText("리로드");

		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!GuiMain.display.readAndDispatch()) 
				GuiMain.display.sleep();
		}
	}
	
	/**
	 * 
	 * @param tree
	 */
	private static void readRobot(List list) {
		for(PcInstance pc : World.getRobotList()) {
			list.add(pc.getName());
			list.setData(pc.getName(), pc);
		}
	}
	
	/**
	 * 
	 * @param bufferedImage
	 * @return
	 */
	private static ImageData convertToSWT(BufferedImage bufferedImage) {
		if (bufferedImage.getColorModel() instanceof DirectColorModel) {
			DirectColorModel colorModel = (DirectColorModel) bufferedImage.getColorModel();
			PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(), colorModel.getBlueMask());
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[3];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					int pixel = palette.getPixel(new RGB(pixelArray[0], pixelArray[1], pixelArray[2]));
					data.setPixel(x, y, pixel);
				}
			}
			return data;
		} else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
			IndexColorModel colorModel = (IndexColorModel)bufferedImage.getColorModel();
			int size = colorModel.getMapSize();
			byte[] reds = new byte[size];
			byte[] greens = new byte[size];
			byte[] blues = new byte[size];
			colorModel.getReds(reds);
			colorModel.getGreens(greens);
			colorModel.getBlues(blues);
			RGB[] rgbs = new RGB[size];
			for (int i = 0; i < rgbs.length; i++) {
				rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
			}
			PaletteData palette = new PaletteData(rgbs);
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
			data.transparentPixel = colorModel.getTransparentPixel();
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					data.setPixel(x, y, pixelArray[0]);
				}
			}
			return data;
		}
		return null;
	}
	
	private static void addInventoryItem(ItemInstance item) {
		int width = 33;
		int height = 33;
		
		int length = inventory.getChildren().length;
		int x = 1 + (1*(length%4)) + (length%4 * width);
		int y = 1 + (1*(length/4)) + (length/4 * height);
		
		Composite c = new Composite(inventory, SWT.NONE);
		c.setBounds(x, y, width, height);
		
		Image img = SpriteTools.getTbt(item.getItem().getInvGfx());
		if(img != null) {
			BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics g = bi.getGraphics();
			g.drawImage(img, 0, 0, null);
			g.dispose();
			
			c.setSize(img.getWidth(null), img.getHeight(null));
			c.setBackgroundImage( new org.eclipse.swt.graphics.Image(null, convertToSWT(bi)) );
		}
		
		c.setToolTipText( item.toStringDB() );
	}
}
