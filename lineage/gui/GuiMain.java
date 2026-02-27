package lineage.gui;

import java.sql.Connection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

import com.swtdesigner.SWTResourceManager;

import jsn_soft.Gui_System;
import lineage.Main;
import lineage.bean.event.SaveDatabase;
import lineage.bean.lineage.Kingdom;
import lineage.database.BackgroundDatabase;
import lineage.database.BadIpDatabase;
import lineage.database.DatabaseConnection;
import lineage.database.ItemBundleDatabase;
import lineage.database.ItemDatabase;
import lineage.database.ItemSetoptionDatabase;
import lineage.database.ItemSkillDatabase;
import lineage.database.ItemTeleportDatabase;
import lineage.database.MagicdollListDatabase;
import lineage.database.TimeDungeonDatabase;
import lineage.database.MonsterBossSpawnlistDatabase;
import lineage.database.MonsterDatabase;
import lineage.database.MonsterDropDatabase;
import lineage.database.MonsterSkillDatabase;
import lineage.database.MonsterSpawnlistDatabase;
import lineage.database.NpcCraftDatabase;
import lineage.database.NpcDatabase;
import lineage.database.NpcShopDatabase;
import lineage.database.NpcSpawnlistDatabase;
import lineage.database.NpcTeleportDatabase;
import lineage.database.PolyDatabase;
import lineage.database.SkillDatabase;
import lineage.gui.composite.ConsoleComposite;
import lineage.gui.composite.ViewComposite;
import lineage.gui.dialog.DatabaseRobot;
import lineage.gui.dialog.PlayerItemAppend;
import lineage.plugin.PluginController;
import lineage.share.GameSetting;
import lineage.share.Lineage;
import lineage.share.Lineage_Balance;
import lineage.util.Shutdown;
import lineage.world.World;
import lineage.world.controller.CommandController;
import lineage.world.controller.EventController;
import lineage.world.controller.KingdomController;
import lineage.world.controller.LastavardController;
import lineage.world.controller.NoticeController;
import lineage.world.controller.ScriptController;
import lineage.world.object.instance.PcInstance;

public final class GuiMain {

	// gui 컴포넌트들.
	static public Display display;
	static public Shell shell;
	static private ViewComposite viewComposite;
	static private ConsoleComposite consoleComposite;
	static private MenuItem menu_system_1_item_1; // 서버가동
	static private MenuItem menu_system_1_item_2; // 서버종료
	static private MenuItem menu_system_1_item_3; // 서버저장
	static private MenuItem menuItem; // 이벤트
	static private MenuItem menuItem_1; // 변신 이벤트
	static private MenuItem menuItem_2; // 명령어
	static private MenuItem menuItem_5; // 자동버프 이벤트
	static private MenuItem menuItem_7; // 환상 이벤트
	static private MenuItem menuItem_8; // 크리스마스 이벤트
	static private MenuItem menuItem_9; // 할로윈 이벤트
	static private MenuItem menuItem_10; // 토템 이벤트
	static private MenuItem menuItem_11; // 쿠작 이벤트
	static private MenuItem menuItem_12; // 랭킹변신 이벤트

	static private MenuItem mntmNewItem_2;
	static private MenuItem mntmNewItem_9;
	static private MenuItem mntmNewItem_10;
	static private MenuItem mntmNewItem_11;
	static private MenuItem mntmNewItem_12;
	static private MenuItem mntmNewItem_13;
	static private MenuItem mntmNewItem_14;
	static private MenuItem mntmNewItem_15;
	static private MenuItem mntmNewItem_16;
	static private MenuItem mntmNewItem_17;
	static private MenuItem mntmNewItem_18;
	static private MenuItem mntmNewItem_19;
	static private MenuItem mntmNewItem_20;
	static private MenuItem mntmNewItem_21;
	static private MenuItem mntmNewItem_22;
	static private MenuItem mntmNewItem_24;
	static private MenuItem mntmNewItem_25;
	static private MenuItem mntmNewItem_26;
	static private MenuItem mntmNewItem_27;
	static private MenuItem mntmNewItem_28;
	static private MenuItem mntmNewItem_29;
	static private MenuItem mntmNewItem_38;
	static private MenuItem mntmNewSubmenu;

	// 서버팩 버전
	static public final String SERVER_VERSION = "1.63";

	// 클라이언트 접속 최대치값.
	static public int CLIENT_MAX = 2000;
	
	// 간지용 포인트
	static private Label cpu;
	static private Label memory;
	static private Label thread;
	static private Label usercount;

	/**
	 * Open the window.
	 * 
	 * @wbp.parser.entryPoint
	 */
	static public void open() {
		display = Display.getDefault();
		shell = new Shell();
		shell.setSize(800, 700);
		shell.setText(String.format("Server Version %s", SERVER_VERSION));
		shell.setImage(SWTResourceManager.getImage("images/icon.ico"));
		GridLayout gl_shell = new GridLayout(2, false);
		gl_shell.verticalSpacing = 0;
		gl_shell.horizontalSpacing = 0;
		gl_shell.marginHeight = 0;
		gl_shell.marginWidth = 0;
		shell.setLayout(gl_shell);

		// 간지용 포인트
		Composite composite = new Composite(shell, SWT.NONE);
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite.widthHint = 784;
		gd_composite.heightHint = 31;
		composite.setLayoutData(gd_composite);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		composite.setBounds(0, 524, 784, 37);

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		lblNewLabel.setFont(SWTResourceManager.getFont("맑은 고딕", 11, SWT.BOLD));
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblNewLabel.setBounds(233, 8, 68, 19);
		lblNewLabel.setText("Memory: ");

		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblNewLabel_1.setFont(SWTResourceManager.getFont("맑은 고딕", 11, SWT.BOLD));
		lblNewLabel_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblNewLabel_1.setBounds(96, 8, 38, 19);
		lblNewLabel_1.setText("CPU: ");

		Label lblNewLabel_2 = new Label(composite, SWT.NONE);
		lblNewLabel_2.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblNewLabel_2.setFont(SWTResourceManager.getFont("맑은 고딕", 11, SWT.BOLD));
		lblNewLabel_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblNewLabel_2.setBounds(469, 8, 68, 17);
		lblNewLabel_2.setText("Thread: ");

		memory = new Label(composite, SWT.NONE);
		memory.setFont(SWTResourceManager.getFont("맑은 고딕", 11, SWT.BOLD));
		memory.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		memory.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		memory.setBounds(307, 8, 128, 19);
		memory.setText(String.format("%d", Gui_System.getUsedMemoryMB()) + "MB/"
				+ String.format("%d", Gui_System.getTotalMemoryMB()) + "MB");

		cpu = new Label(composite, SWT.NONE);
		cpu.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		cpu.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		cpu.setFont(SWTResourceManager.getFont("맑은 고딕", 11, SWT.BOLD));
		cpu.setBounds(140, 8, 56, 19);
		cpu.setText(String.format("%.0f%%", Gui_System.getUseCpu()));

		thread = new Label(composite, SWT.NONE);
		thread.setFont(SWTResourceManager.getFont("맑은 고딕", 11, SWT.BOLD));
		thread.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		thread.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		thread.setBounds(543, 8, 46, 19);
		thread.setText(String.format("%d", Gui_System.getThread()));

		Label lblNewLabel_6 = new Label(composite, SWT.NONE);
		lblNewLabel_6.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblNewLabel_6.setFont(SWTResourceManager.getFont("맑은 고딕", 11, SWT.BOLD));
		lblNewLabel_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lblNewLabel_6.setBounds(629, 8, 38, 19);
		lblNewLabel_6.setText("User: ");

		usercount = new Label(composite, SWT.NONE);
		usercount.setFont(SWTResourceManager.getFont("맑은 고딕", 11, SWT.BOLD));
		usercount.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		usercount.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		usercount.setBounds(673, 8, 56, 17);
		usercount.setText("0");
		new Label(shell, SWT.NONE);

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem menu_system = new MenuItem(menu, SWT.CASCADE);
		menu_system.setText("시스템");

		Menu menu_system_1 = new Menu(menu_system);
		menu_system.setMenu(menu_system_1);

		menu_system_1_item_1 = new MenuItem(menu_system_1, SWT.NONE);
		menu_system_1_item_1.setText("서버 가동");

		menu_system_1_item_2 = new MenuItem(menu_system_1, SWT.CASCADE);
		menu_system_1_item_2.setText("서버 종료");
		menu_system_1_item_2.setEnabled(false);
		
		Menu serverOffMenu = new Menu(menu_system_1_item_2);
		menu_system_1_item_2.setMenu(serverOffMenu);
		
		MenuItem serverOffMenu_1 = new MenuItem(serverOffMenu, SWT.CHECK);
		MenuItem serverOffMenu_2 = new MenuItem(serverOffMenu, SWT.CHECK);
		MenuItem serverOffMenu_3 = new MenuItem(serverOffMenu, SWT.CHECK);
		MenuItem serverOffMenu_4 = new MenuItem(serverOffMenu, SWT.CHECK);
		MenuItem serverOffMenu_5 = new MenuItem(serverOffMenu, SWT.CHECK);
		MenuItem serverOffMenu_6 = new MenuItem(serverOffMenu, SWT.CHECK);
		serverOffMenu_6.setEnabled(false);
		
		new MenuItem(menu_system_1, SWT.SEPARATOR);
		
		serverOffMenu_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lineage.share.System.println("서버가 즉시 종료됩니다...");
				Main.close();
				serverOffMenu_1.setSelection(true);
				serverOffMenu_2.setSelection(false);
				serverOffMenu_3.setSelection(false);
				serverOffMenu_4.setSelection(false);
				serverOffMenu_5.setSelection(false);
				serverOffMenu_6.setSelection(false);
				menu_system_1_item_2.setEnabled(false);
				
				serverOffMenu_1.setEnabled(false);
				serverOffMenu_2.setEnabled(false);
				serverOffMenu_3.setEnabled(false);
				serverOffMenu_4.setEnabled(false);
				serverOffMenu_5.setEnabled(false);
				serverOffMenu_6.setEnabled(false);
			}
		});
		serverOffMenu_1.setText("1.   즉시 서버 종료");
				
		serverOffMenu_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (Shutdown.getInstance() != null)
					Shutdown.getInstance().is_shutdown = false;
				
				new Thread(Shutdown.getInstance(10)).start();
				serverOffMenu_1.setSelection(false);
				serverOffMenu_2.setSelection(true);
				serverOffMenu_3.setSelection(false);
				serverOffMenu_4.setSelection(false);
				serverOffMenu_5.setSelection(false);
				serverOffMenu_6.setSelection(false);
				
				serverOffMenu_6.setEnabled(true);
				
				serverOffMenu_1.setEnabled(false);
				serverOffMenu_2.setEnabled(false);
				serverOffMenu_3.setEnabled(false);
				serverOffMenu_4.setEnabled(false);
				serverOffMenu_5.setEnabled(false);
			}
		});
		serverOffMenu_2.setText("2.   10초 후 서버 종료");
		
		serverOffMenu_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (Shutdown.getInstance() != null)
					Shutdown.getInstance().is_shutdown = false;
				
				new Thread(Shutdown.getInstance(60)).start();
				serverOffMenu_1.setSelection(false);
				serverOffMenu_2.setSelection(false);
				serverOffMenu_3.setSelection(true);
				serverOffMenu_4.setSelection(false);
				serverOffMenu_5.setSelection(false);
				serverOffMenu_6.setSelection(false);
				
				serverOffMenu_6.setEnabled(true);
				
				serverOffMenu_1.setEnabled(false);
				serverOffMenu_2.setEnabled(false);
				serverOffMenu_3.setEnabled(false);
				serverOffMenu_4.setEnabled(false);
				serverOffMenu_5.setEnabled(false);
			}
		});
		serverOffMenu_3.setText("3.   1분 후 서버 종료");	
		
		serverOffMenu_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (Shutdown.getInstance() != null)
					Shutdown.getInstance().is_shutdown = false;
				
				new Thread(Shutdown.getInstance(60 * 5)).start();
				serverOffMenu_1.setSelection(false);
				serverOffMenu_2.setSelection(false);
				serverOffMenu_3.setSelection(false);
				serverOffMenu_4.setSelection(true);
				serverOffMenu_5.setSelection(false);
				serverOffMenu_6.setSelection(false);
				
				serverOffMenu_6.setEnabled(true);
				
				serverOffMenu_1.setEnabled(false);
				serverOffMenu_2.setEnabled(false);
				serverOffMenu_3.setEnabled(false);
				serverOffMenu_4.setEnabled(false);
				serverOffMenu_5.setEnabled(false);
			}
		});
		serverOffMenu_4.setText("4.   5분 후 서버 종료");	
		
		serverOffMenu_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (Shutdown.getInstance() != null)
					Shutdown.getInstance().is_shutdown = false;
				
				new Thread(Shutdown.getInstance(60 * 10)).start();
				serverOffMenu_1.setSelection(false);
				serverOffMenu_2.setSelection(false);
				serverOffMenu_3.setSelection(false);
				serverOffMenu_4.setSelection(false);
				serverOffMenu_5.setSelection(true);
				serverOffMenu_6.setSelection(false);
				
				serverOffMenu_6.setEnabled(true);
				
				serverOffMenu_1.setEnabled(false);
				serverOffMenu_2.setEnabled(false);
				serverOffMenu_3.setEnabled(false);
				serverOffMenu_4.setEnabled(false);
				serverOffMenu_5.setEnabled(false);
			}
		});
		serverOffMenu_5.setText("5.   10분 후 서버 종료");
				
		serverOffMenu_6.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (Shutdown.getInstance() != null)
					Shutdown.getInstance().is_shutdown = false;
				
				serverOffMenu_1.setSelection(false);
				serverOffMenu_2.setSelection(false);
				serverOffMenu_3.setSelection(false);
				serverOffMenu_4.setSelection(false);
				serverOffMenu_5.setSelection(false);
				serverOffMenu_6.setSelection(false);
				
				serverOffMenu_1.setEnabled(true);
				serverOffMenu_2.setEnabled(true);
				serverOffMenu_3.setEnabled(true);
				serverOffMenu_4.setEnabled(true);
				serverOffMenu_5.setEnabled(true);
				serverOffMenu_6.setEnabled(false);
			}
		});
		serverOffMenu_6.setText("6.   서버 종료 취소");

		menu_system_1_item_3 = new MenuItem(menu_system_1, SWT.NONE);
		menu_system_1_item_3.setEnabled(false);
		menu_system_1_item_3.setText("서버 저장");

		new MenuItem(menu_system_1, SWT.SEPARATOR);

		MenuItem menuItem_6 = new MenuItem(menu_system_1, SWT.NONE);
		menuItem_6.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final Tray tray = display.getSystemTray();
				if (tray != null) {
					// 현재 윈도우 감추기.
					shell.setVisible(false);
					// 트레이 활성화.
					final TrayItem item = new TrayItem(tray, SWT.NONE);
					item.setToolTipText(String.format("%s : %d", SERVER_VERSION, Lineage.server_version));
					item.setImage(SWTResourceManager.getImage("images/icon.ico"));
					// 이벤트 등록.
					item.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							item.dispose();
							shell.setVisible(true);
							shell.setFocus();
						}
					});
				}
			}
		});
		menuItem_6.setText("트레이 모드");

		MenuItem menu_lineage = new MenuItem(menu, SWT.CASCADE);
		menu_lineage.setText("리니지");

		Menu menu_2 = new Menu(menu_lineage);
		menu_lineage.setMenu(menu_2);

		menuItem = new MenuItem(menu_2, SWT.CASCADE);
		menuItem.setEnabled(false);
		menuItem.setText("이벤트");

		Menu menu_1 = new Menu(menuItem);
		menuItem.setMenu(menu_1);

		menuItem_1 = new MenuItem(menu_1, SWT.CHECK);
		menuItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EventController.toPoly(menuItem_1.getSelection());
			}
		});
		menuItem_1.setText("변신 이벤트");
		
		menuItem_12 = new MenuItem(menu_1, SWT.CHECK);
		menuItem_12.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EventController.toPoly2(menuItem_12.getSelection());
			}
		});
		menuItem_12.setText("랭킹변신 이벤트");

		menuItem_5 = new MenuItem(menu_1, SWT.CHECK);
		menuItem_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EventController.toBuff(menuItem_5.getSelection());
			}
		});
		menuItem_5.setText("자동버프 이벤트");

		menuItem_7 = new MenuItem(menu_1, SWT.CHECK);
		menuItem_7.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EventController.toIllusion(menuItem_7.getSelection());
			}
		});
		menuItem_7.setText("수렵 이벤트");

		menuItem_8 = new MenuItem(menu_1, SWT.CHECK);
		menuItem_8.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EventController.toChristmas(menuItem_8.getSelection());
			}
		});
		menuItem_8.setText("크리스마스 이벤트");

		menuItem_9 = new MenuItem(menu_1, SWT.CHECK);
		menuItem_9.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//EventController.toHalloween(menuItem_9.getSelection());
				EventController.tobounty(menuItem_9.getSelection());

			}
		});
		menuItem_9.setText("할로윈 이벤트");
		
		menuItem_11 = new MenuItem(menu_1, SWT.CHECK);
		menuItem_11.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EventController.tobounty(menuItem_11.getSelection());
			}
		});
		menuItem_11.setText("현상범 쿠작 이벤트");

		new MenuItem(menu_1, SWT.SEPARATOR);
		
		menuItem_10 = new MenuItem(menu_1, SWT.CHECK);
		menuItem_10.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EventController.toTotem(menuItem_10.getSelection());
			}
		});
		menuItem_10.setText("토템 이벤트");

		menuItem_2 = new MenuItem(menu_2, SWT.CASCADE);
		menuItem_2.setEnabled(false);
		menuItem_2.setText("명령어");

		Menu menu_4 = new Menu(menuItem_2);
		menuItem_2.setMenu(menu_4);
		
		MenuItem menuItem_1 = new MenuItem(menu_4, SWT.CHECK);
		menuItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CommandController.serverOpenWait();
				menuItem_1.setSelection(true);
			}
		});
		menuItem_1.setText("서버 오픈대기");
		
		MenuItem menuItem_2 = new MenuItem(menu_4, SWT.NONE);
		menuItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CommandController.serverOpen();
				menuItem_1.setSelection(false);
			}
		});
		menuItem_2.setText("서버 오픈");

		MenuItem menuItem_3 = new MenuItem(menu_4, SWT.NONE);
		menuItem_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CommandController.toBuffAll(null);
			}
		});
		menuItem_3.setText("올버프");

		MenuItem menuItem_4 = new MenuItem(menu_4, SWT.NONE);
		menuItem_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CommandController.toWorldItemClear(null);
			}
		});
		menuItem_4.setText("청소");

		MenuItem mntmNewItem_23 = new MenuItem(menu_4, SWT.NONE);
		mntmNewItem_23.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PlayerItemAppend.open();
			}
		});
		mntmNewItem_23.setText("아이템지급");
		
		new MenuItem(menu_4, SWT.SEPARATOR);
		
		MenuItem menu_system_2 = new MenuItem(menu_4, SWT.NONE);
		menu_system_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (PcInstance pc : World.getPcList())
					pc.toCharacterSave();

				lineage.share.System.println("데이터 저장 완료");
			}
		});
		menu_system_2.setText("데이터 저장");
		

		mntmNewItem_2 = new MenuItem(menu_2, SWT.CASCADE);
		mntmNewItem_2.setEnabled(false);
		mntmNewItem_2.setText("공성전 관리");

		Menu menu_5 = new Menu(mntmNewItem_2);
		mntmNewItem_2.setMenu(menu_5);

		MenuItem mntmNewItem_6 = new MenuItem(menu_5, SWT.CASCADE);
		mntmNewItem_6.setText("켄트성");

		Menu menu_6 = new Menu(mntmNewItem_6);
		mntmNewItem_6.setMenu(menu_6);

		mntmNewItem_9 = new MenuItem(menu_6, SWT.NONE);
		mntmNewItem_9.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toKingdomControll(1, mntmNewItem_9, mntmNewItem_10, true);
			}
		});
		mntmNewItem_9.setText("시작");

		mntmNewItem_10 = new MenuItem(menu_6, SWT.NONE);
		mntmNewItem_10.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toKingdomControll(1, mntmNewItem_9, mntmNewItem_10, false);
			}
		});
		mntmNewItem_10.setText("종료");

		MenuItem mntmNewItem_3 = new MenuItem(menu_5, SWT.CASCADE);
		mntmNewItem_3.setText("오크 요새");

		Menu menu_7 = new Menu(mntmNewItem_3);
		mntmNewItem_3.setMenu(menu_7);

		mntmNewItem_11 = new MenuItem(menu_7, SWT.NONE);
		mntmNewItem_11.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toKingdomControll(2, mntmNewItem_11, mntmNewItem_12, true);
			}
		});
		mntmNewItem_11.setText("시작");

		mntmNewItem_12 = new MenuItem(menu_7, SWT.NONE);
		mntmNewItem_12.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toKingdomControll(2, mntmNewItem_11, mntmNewItem_12, false);
			}
		});
		mntmNewItem_12.setText("종료");

		MenuItem mntmNewItem_4 = new MenuItem(menu_5, SWT.CASCADE);
		mntmNewItem_4.setText("윈다우드 성");

		Menu menu_8 = new Menu(mntmNewItem_4);
		mntmNewItem_4.setMenu(menu_8);

		mntmNewItem_13 = new MenuItem(menu_8, SWT.NONE);
		mntmNewItem_13.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toKingdomControll(3, mntmNewItem_13, mntmNewItem_14, true);
			}
		});
		mntmNewItem_13.setText("시작");

		mntmNewItem_14 = new MenuItem(menu_8, SWT.NONE);
		mntmNewItem_14.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toKingdomControll(3, mntmNewItem_13, mntmNewItem_14, false);
			}
		});
		mntmNewItem_14.setText("종료");

		MenuItem mntmNewItem_5 = new MenuItem(menu_5, SWT.CASCADE);
		mntmNewItem_5.setText("기란 성");

		Menu menu_9 = new Menu(mntmNewItem_5);
		mntmNewItem_5.setMenu(menu_9);

		mntmNewItem_15 = new MenuItem(menu_9, SWT.NONE);
		mntmNewItem_15.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toKingdomControll(4, mntmNewItem_15, mntmNewItem_16, true);
			}
		});
		mntmNewItem_15.setText("시작");

		mntmNewItem_16 = new MenuItem(menu_9, SWT.NONE);
		mntmNewItem_16.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toKingdomControll(4, mntmNewItem_15, mntmNewItem_16, false);
			}
		});
		mntmNewItem_16.setText("종료");

		mntmNewSubmenu = new MenuItem(menu_2, SWT.CASCADE);
		mntmNewSubmenu.setEnabled(false);
		mntmNewSubmenu.setText("설정");

		Menu menu_14 = new Menu(mntmNewSubmenu);
		mntmNewSubmenu.setMenu(menu_14);

		final MenuItem mntmLineageconf = new MenuItem(menu_14, SWT.NONE);
		mntmLineageconf.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox = new MessageBox(GuiMain.shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
				messageBox.setText("경고");
				messageBox.setMessage(mntmLineageconf.getText() + "을 다시 읽겠습니까?");
				if (messageBox.open() == SWT.YES)
					Lineage.init(true);
					//Lineage.init();
			}
		});
		mntmLineageconf.setText("lineage.conf");
		
		final MenuItem mntmLineageconf_b = new MenuItem(menu_14, SWT.NONE);
		mntmLineageconf_b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox = new MessageBox(GuiMain.shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
				messageBox.setText("경고");
				messageBox.setMessage(mntmLineageconf_b.getText() + "을 다시 읽겠습니까?");
				if (messageBox.open() == SWT.YES)
					Lineage_Balance.init();
			}
		});
		mntmLineageconf_b.setText("lineage_balance.conf");

		final MenuItem mntmNewItem_33 = new MenuItem(menu_14, SWT.NONE);
		mntmNewItem_33.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox = new MessageBox(GuiMain.shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
				messageBox.setText("경고");
				messageBox.setMessage(mntmNewItem_33.getText() + "을 다시 읽겠습니까?");
				if (messageBox.open() == SWT.YES) {
					NoticeController.close();
					NoticeController.init();
				}
			}
		});
		mntmNewItem_33.setText("notice.txt");

		final MenuItem mntmNewItem_40 = new MenuItem(menu_14, SWT.NONE);
		mntmNewItem_40.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox = new MessageBox(GuiMain.shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
				messageBox.setText("경고");
				messageBox.setMessage(mntmNewItem_40.getText() + "을 다시 읽겠습니까?");
				if (messageBox.open() == SWT.YES) {
					GameSetting.load();
				}
			}
		});
		mntmNewItem_40.setText("GameSetting.properties");

		final MenuItem mntmNewItem_41 = new MenuItem(menu_14, SWT.NONE);
		mntmNewItem_41.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox = new MessageBox(GuiMain.shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
				messageBox.setText("경고");
				messageBox.setMessage(mntmNewItem_41.getText() + "을 다시 읽겠습니까?");
				if (messageBox.open() == SWT.YES) {
					Connection con = null;
					try {
						con = DatabaseConnection.getLineage();
						BadIpDatabase.init(con);
					} catch (Exception e2) {
					} finally {
						DatabaseConnection.close(con);
					}
				}
			}
		});
		mntmNewItem_41.setText("Bad_Ip");

		final MenuItem mntmNewItem_39 = new MenuItem(menu_14, SWT.NONE);
		mntmNewItem_39.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox = new MessageBox(GuiMain.shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
				messageBox.setText("경고");
				messageBox.setMessage(mntmNewItem_39.getText() + "을 다시 읽겠습니까?");
				if (messageBox.open() == SWT.YES) {
					ScriptController.close();
					ScriptController.init();
				}
			}
		});
		mntmNewItem_39.setText("scripts");

		MenuItem menu_database = new MenuItem(menu, SWT.CASCADE);
		menu_database.setText("데이타베이스");

		Menu menu_13 = new Menu(menu_database);
		menu_database.setMenu(menu_13);

		mntmNewItem_29 = new MenuItem(menu_13, SWT.NONE);
		mntmNewItem_29.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DatabaseRobot.open();
			}
		});
		mntmNewItem_29.setText("Robot");
		mntmNewItem_29.setEnabled(false);

		mntmNewItem_26 = new MenuItem(menu_13, SWT.NONE);
		mntmNewItem_26.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox = new MessageBox(GuiMain.shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
				messageBox.setText("경고");
				messageBox.setMessage(mntmNewItem_26.getText() + "을 다시 읽겠습니까?");
				if (messageBox.open() == SWT.YES) {
					Connection con = null;
					try {
						con = DatabaseConnection.getLineage();
						BackgroundDatabase.close();
						BackgroundDatabase.init(con);
					} catch (Exception e2) {
					} finally {
						DatabaseConnection.close(con);
					}
				}
			}
		});
		mntmNewItem_26.setText("Background");
		mntmNewItem_26.setEnabled(false);

		mntmNewItem_27 = new MenuItem(menu_13, SWT.NONE);
		mntmNewItem_27.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox = new MessageBox(GuiMain.shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
				messageBox.setText("경고");
				messageBox.setMessage(mntmNewItem_27.getText() + "을 다시 읽겠습니까?");
				if (messageBox.open() == SWT.YES) {
					Connection con = null;
					try {
						con = DatabaseConnection.getLineage();
						ItemDatabase.init(con);
						ItemSetoptionDatabase.init(con);
						ItemSkillDatabase.init(con);
						ItemBundleDatabase.init(con);
						ItemTeleportDatabase.init(con);
						MagicdollListDatabase.init(con);
					} catch (Exception e2) {
					} finally {
						DatabaseConnection.close(con);
					}
				}
			}
		});
		mntmNewItem_27.setText("Item");
		mntmNewItem_27.setEnabled(false);

		mntmNewItem_24 = new MenuItem(menu_13, SWT.NONE);
		mntmNewItem_24.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox = new MessageBox(GuiMain.shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
				messageBox.setText("경고");
				messageBox.setMessage(mntmNewItem_24.getText() + "을 다시 읽겠습니까?");
				if (messageBox.open() == SWT.YES) {
					Connection con = null;
					try {
						con = DatabaseConnection.getLineage();
						NpcDatabase.init(con);
						NpcShopDatabase.init(con);
						NpcCraftDatabase.init(con);
						NpcTeleportDatabase.init(con);
						NpcSpawnlistDatabase.close();
						NpcSpawnlistDatabase.init(con);
					} catch (Exception e2) {
					} finally {
						DatabaseConnection.close(con);
					}
				}
			}
		});
		mntmNewItem_24.setText("Npc");
		mntmNewItem_24.setEnabled(false);

		mntmNewItem_25 = new MenuItem(menu_13, SWT.NONE);
		mntmNewItem_25.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox = new MessageBox(GuiMain.shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
				messageBox.setText("경고");
				messageBox.setMessage(mntmNewItem_25.getText() + "을 다시 읽겠습니까?");
				if (messageBox.open() == SWT.YES) {
					Connection con = null;
					try {
						con = DatabaseConnection.getLineage();
						MonsterDatabase.init(con);
						MonsterDropDatabase.init(con);
						MonsterSkillDatabase.init(con);
						MonsterSpawnlistDatabase.close();
						MonsterSpawnlistDatabase.init(con);
						MonsterBossSpawnlistDatabase.init(con);
					} catch (Exception e2) {
					} finally {
						DatabaseConnection.close(con);
					}
				}
			}
		});
		mntmNewItem_25.setText("Monster");
		mntmNewItem_25.setEnabled(false);

		mntmNewItem_28 = new MenuItem(menu_13, SWT.NONE);
		mntmNewItem_28.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox = new MessageBox(GuiMain.shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
				messageBox.setText("경고");
				messageBox.setMessage(mntmNewItem_28.getText() + "을 다시 읽겠습니까?");
				if (messageBox.open() == SWT.YES) {
					Connection con = null;
					try {
						con = DatabaseConnection.getLineage();
						SkillDatabase.init(con);
					} catch (Exception e2) {
					} finally {
						DatabaseConnection.close(con);
					}
				}
			}
		});
		mntmNewItem_28.setText("Skill");
		mntmNewItem_28.setEnabled(false);
		
		mntmNewItem_38 = new MenuItem(menu_13, SWT.NONE);
		mntmNewItem_38.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox = new MessageBox(GuiMain.shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
				messageBox.setText("경고");
				messageBox.setMessage(mntmNewItem_38.getText() + "을 다시 읽겠습니까?");
				if (messageBox.open() == SWT.YES) {
					Connection con = null;
					try {
						con = DatabaseConnection.getLineage();
						PolyDatabase.close();
						PolyDatabase.init(con);
					} catch (Exception e2) {
					} finally {
						DatabaseConnection.close(con);
					}
				}
			}
		});
		mntmNewItem_38.setText("Poly");
		mntmNewItem_38.setEnabled(false);
		
		MenuItem menu_execute = new MenuItem(menu, SWT.CASCADE);
		menu_execute.setText("실행");
		
		Menu menu_15 = new Menu(menu_execute);
		menu_execute.setMenu(menu_15);
		
		MenuItem lineageConf = new MenuItem(menu_15, SWT.NONE);
		lineageConf.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {		
				try {
					Runtime.getRuntime().exec("C:/WINDOWS/system32/notepad.exe " + System.getProperty("user.dir") + "/lineage.conf");
				} catch (Exception e2) { }
			}
		});
		lineageConf.setText("lineage.conf 실행");
		
		MenuItem lineageBalanceConf = new MenuItem(menu_15, SWT.NONE);
		lineageBalanceConf.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {		
				try {
					Runtime.getRuntime().exec("C:/WINDOWS/system32/notepad.exe " + System.getProperty("user.dir") + "/lineage_balance.conf");
				} catch (Exception e2) { }
			}
		});
		lineageBalanceConf.setText("lineage_balance.conf 실행");
		
		MenuItem gamesettingProperties = new MenuItem(menu_15, SWT.NONE);
		gamesettingProperties.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {		
				try {
					Runtime.getRuntime().exec("C:/WINDOWS/system32/notepad.exe " + System.getProperty("user.dir") + "/GameSetting.properties");
				} catch (Exception e2) { }
			}
		});
		gamesettingProperties.setText("GameSetting.properties 실행");
		
		MenuItem socketConf = new MenuItem(menu_15, SWT.NONE);
		socketConf.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {		
				try {
					Runtime.getRuntime().exec("C:/WINDOWS/system32/notepad.exe " + System.getProperty("user.dir") + "/socket.conf");
				} catch (Exception e2) { }
			}
		});
		socketConf.setText("socket.conf 실행");
		
		MenuItem mySqlConf = new MenuItem(menu_15, SWT.NONE);
		mySqlConf.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {		
				try {
					Runtime.getRuntime().exec("C:/WINDOWS/system32/notepad.exe " + System.getProperty("user.dir") + "/mysql.conf");
				} catch (Exception e2) { }
			}
		});
		mySqlConf.setText("mysql.conf 실행");
		
		MenuItem noticeTxt = new MenuItem(menu_15, SWT.NONE);
		noticeTxt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {		
				try {
					Runtime.getRuntime().exec("C:/WINDOWS/system32/notepad.exe " + System.getProperty("user.dir") + "/notice.txt");
				} catch (Exception e2) { }
			}
		});
		noticeTxt.setText("notice.txt 실행");
		
		MenuItem npcPromotionConf = new MenuItem(menu_15, SWT.NONE);
		npcPromotionConf.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {		
				try {
					Runtime.getRuntime().exec("C:/WINDOWS/system32/notepad.exe " + System.getProperty("user.dir") + "/npc_promotion.conf");
				} catch (Exception e2) { }
			}
		});
		npcPromotionConf.setText("npc_promotion.conf 실행");

		MenuItem menu_help = new MenuItem(menu, SWT.CASCADE);
		menu_help.setText("연락처");
		
		Menu menu_3 = new Menu(menu_help);
		menu_help.setMenu(menu_3);
		
		MenuItem nateon = new MenuItem(menu_3, SWT.NONE);
		nateon.setText("이메일: 미지정");
		
		MenuItem mntmNewItem_1 = new MenuItem(menu_3, SWT.NONE);
		mntmNewItem_1.setText("홈페이지: 미지정");
		
		viewComposite = new ViewComposite(shell, SWT.NONE);
		viewComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		consoleComposite = new ConsoleComposite(shell, SWT.NONE);
		GridData gd_consoleComposite = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd_consoleComposite.heightHint = 140;
		consoleComposite.setLayoutData(gd_consoleComposite);

		// 이벤트 등록.
		menu_system_1_item_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 서버 정보 로드.
				Main.init();
				// 맵뷰어 랜더링 시작.
				viewComposite.getScreenRenderComposite().start();
				// 정보 변경.
				menu_system_1_item_1.setEnabled(false);
			}
		});
		menu_system_1_item_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//
				if (Main.running == false) {
					toMessageBox("로딩중입니다..");
					return;
				}
				//
				menu_system_1_item_2.setEnabled(false);
				menu_system_1_item_3.setEnabled(false);
				//
				close();
			}
		});
		menu_system_1_item_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//
				if (Main.running == false) {
					toMessageBox("로딩중입니다..");
					return;
				}
				//
				MessageBox messageBox = new MessageBox(GuiMain.shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
				messageBox.setText("경고");
				messageBox.setMessage("정말 저장 하시겠습니까?");
				if (messageBox.open() == SWT.YES)
					new Thread(new SaveDatabase()).start();
			}
		});

		// 매니저를 윈도우화면 가운데 좌표로 변경.
		shell.setBounds((display.getBounds().width / 2) - (shell.getBounds().width / 2),
				(display.getBounds().height / 2) - (shell.getBounds().height / 2), shell.getBounds().width,
				shell.getBounds().height);

		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			try {
				if (!display.readAndDispatch())
					display.sleep();
			} catch (Exception e) {
			}
		}

		Main.close();
	}

	static public ViewComposite getViewComposite() {
		return viewComposite;
	}

	static public ConsoleComposite getConsoleComposite() {
		return consoleComposite;
	}

	static public void toTimer(long time) {
		// 뷰어 처리.
		viewComposite.toTimer(time);
		// 검은창 처리
		usercount.setText(String.format("%d", World.getUserSize()));
		memory.setText(String.format("%d", Gui_System.getUsedMemoryMB()) + "MB/"
				+ String.format("%d", Gui_System.getTotalMemoryMB()) + "MB");
		thread.setText(String.format("%d", Gui_System.getThread()));
		cpu.setText(String.format("%.0f%%", Gui_System.getUseCpu()));
		// 초기화 안된 상태.
		if (!menuItem.isEnabled()) {
			// 메뉴 활성화.
			menuItem.setEnabled(true);
			menuItem_2.setEnabled(true);
			mntmNewItem_2.setEnabled(true);
			mntmNewItem_24.setEnabled(true);
			mntmNewItem_25.setEnabled(true);
			mntmNewItem_26.setEnabled(true);
			mntmNewItem_27.setEnabled(true);
			mntmNewItem_28.setEnabled(true);
			mntmNewItem_29.setEnabled(true);
			mntmNewItem_38.setEnabled(true);
			mntmNewSubmenu.setEnabled(true);
			menu_system_1_item_2.setEnabled(true);
			menu_system_1_item_3.setEnabled(true);
			// Lineage 설정 정보 갱신
			menuItem_1.setSelection(Lineage.event_poly);
			menuItem_12.setSelection(Lineage.event_rank_poly);
			menuItem_5.setSelection(Lineage.event_buff);
			menuItem_7.setSelection(Lineage.event_illusion);
			menuItem_8.setSelection(Lineage.event_christmas);
			menuItem_9.setSelection(Lineage.event_halloween);
			menuItem_10.setSelection(Lineage.event_lyra);
			menuItem_11.setSelection(Lineage.event_kujak);

			//
			Kingdom k = KingdomController.find(1);
			if (k != null) {
				mntmNewItem_9.setEnabled(!k.isWar());
				mntmNewItem_10.setEnabled(k.isWar());
			}
			k = KingdomController.find(2);
			if (k != null) {
				mntmNewItem_11.setEnabled(!k.isWar());
				mntmNewItem_12.setEnabled(k.isWar());
			}
			k = KingdomController.find(3);
			if (k != null) {
				mntmNewItem_13.setEnabled(!k.isWar());
				mntmNewItem_14.setEnabled(k.isWar());
			}
			k = KingdomController.find(4);
			if (k != null) {
				mntmNewItem_15.setEnabled(!k.isWar());
				mntmNewItem_16.setEnabled(k.isWar());
			}
			k = KingdomController.find(5);
			if (k != null) {
				mntmNewItem_17.setEnabled(!k.isWar());
				mntmNewItem_18.setEnabled(k.isWar());
			}
			k = KingdomController.find(6);
			if (k != null) {
				mntmNewItem_19.setEnabled(!k.isWar());
				mntmNewItem_20.setEnabled(k.isWar());
			}
			k = KingdomController.find(7);
			if (k != null) {
				mntmNewItem_21.setEnabled(!k.isWar());
				mntmNewItem_22.setEnabled(k.isWar());
			}
			//
			shell.setText(String.format("Server Version %s", SERVER_VERSION, Lineage.server_version));
		}
	}

	static private void toKingdomControll(int uid, MenuItem a, MenuItem b, boolean isStart) {
		Kingdom k = KingdomController.find(uid);
		if (k == null) {
			toMessageBox("요청하신 Kingdom 정보가 존재하지 않습니다.");
			return;
		}
		if (PluginController.init(GuiMain.class, "toKingdomController", uid, a, b, isStart, k) == null) {
			if (isStart)
				// 현재 시간을 강제로 기입. 그럼 지가 알아서 공성 시작함.
				k.setWarDay(System.currentTimeMillis());
			else
				k.setWarDayEnd(System.currentTimeMillis());
			//
			a.setEnabled(!a.isEnabled());
			b.setEnabled(!b.isEnabled());
		}
	}

	/**
	 * 경고창 띄울때 사용.
	 * 
	 * @param msg
	 */
	static public void toMessageBox(final String msg) {
		toMessageBox(SERVER_VERSION, msg);
	}

	static public void toMessageBox(final String title, final String msg) {
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING);
		messageBox.setText(String.format("경고 :: %s", title));
		messageBox.setMessage(msg);
		messageBox.open();
	}

	static public void close() {
		new Thread(Shutdown.getInstance()).start();
	}
}
