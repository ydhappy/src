package lineage.gui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class ViewComposite extends Composite {

	private TabFolder tabFolder;
	
	private ServerInfoComposite serverInfoComposite;
	private ScreenRenderComposite screenRenderComposite;
	private ChattingComposite chattingComposite;
	private EnchantComposite enchantComposite;
	private int tabSelectIdx;
	private TabItem tabItem_3;
	private Composite logComposite;
	
	private ConnectComposite connectComposite;
	private GiveAndDropComposite giveComposite;
	private WarehouseComposite warehouseComposite;
	private SpeedHackComposite speedHackComposite;
	private TradeComposite tradeComposite;
	private MaeipComposite maeipcomposite;
	private DamageCheckComposite damageCheckComposite;
	private EnchantLostItemComposite enchantLostItemComposite;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ViewComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		
		tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("시스템");
		
		serverInfoComposite = new ServerInfoComposite(tabFolder, SWT.NONE);
		tabItem.setControl(serverInfoComposite);
		
		TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
		tabItem_1.setText("리니지");
		
		screenRenderComposite = new ScreenRenderComposite(tabFolder, SWT.NONE);
		tabItem_1.setControl(screenRenderComposite);
		
		TabItem tabItem_2 = new TabItem(tabFolder, SWT.NONE);
		tabItem_2.setText("채팅");
		
		chattingComposite = new ChattingComposite(tabFolder, SWT.NONE);
		tabItem_2.setControl(chattingComposite);
		
		tabItem_3 = new TabItem(tabFolder, SWT.NONE);
		tabItem_3.setText("채팅 기록");
		
		logComposite = new LogViewer(tabFolder, SWT.NONE);
		tabItem_3.setControl(logComposite);
		
		TabItem tabItem_4 = new TabItem(tabFolder, SWT.NONE);
		tabItem_4.setText("인첸트");
		
		enchantComposite = new EnchantComposite(tabFolder, SWT.NONE);
		tabItem_4.setControl(enchantComposite);
		
		TabItem tabItem_5 = new TabItem(tabFolder, SWT.NONE);
		tabItem_5.setText("아이템 드랍");
		
		giveComposite = new GiveAndDropComposite(tabFolder, SWT.NONE);
		tabItem_5.setControl(giveComposite);
		
		TabItem tabItem_6 = new TabItem(tabFolder, SWT.NONE);
		tabItem_6.setText("창고");
		
		warehouseComposite = new WarehouseComposite(tabFolder, SWT.NONE);
		tabItem_6.setControl(warehouseComposite);
		
		TabItem tabItem_7 = new TabItem(tabFolder, SWT.NONE);
		tabItem_7.setText("거래");
		
		tradeComposite = new TradeComposite(tabFolder, SWT.NONE);
		tabItem_7.setControl(tradeComposite);
		
		TabItem tabItem_8 = new TabItem(tabFolder, SWT.NONE);
		tabItem_8.setText("상점");
		
		maeipcomposite = new MaeipComposite(tabFolder, SWT.NONE);
		tabItem_8.setControl(maeipcomposite);
		
		TabItem tabItem_9 = new TabItem(tabFolder, SWT.NONE);
		tabItem_9.setText("접속자");
		
		connectComposite = new ConnectComposite(tabFolder, SWT.NONE);
		tabItem_9.setControl(connectComposite);
		
		TabItem tabItem_10 = new TabItem(tabFolder, SWT.NONE);
		tabItem_10.setText("데미지 확인");
		
		damageCheckComposite = new DamageCheckComposite(tabFolder, SWT.NONE);
		tabItem_10.setControl(damageCheckComposite);
		
		TabItem tabItem_11 = new TabItem(tabFolder, SWT.NONE);
		tabItem_11.setText("인첸트 복구");
		
		enchantLostItemComposite = new EnchantLostItemComposite(tabFolder, SWT.NONE);
		tabItem_11.setControl(enchantLostItemComposite);
	}
	
	public void toTimer(long time){
		tabSelectIdx = tabFolder.getSelectionIndex();
		
		// 서버정보 표현 갱신.
		if(tabSelectIdx == 0)
			serverInfoComposite.toUpdate();
		// 맵뷰어 랜더링 표현.
		if(tabSelectIdx == 1)
			screenRenderComposite.toUpdate();
	}
	
	public ScreenRenderComposite getScreenRenderComposite() {
		return screenRenderComposite;
	}
	
	public ChattingComposite getChattingComposite(){
		return chattingComposite;
	}
	
	public GiveAndDropComposite getGiveComposite() {
		return giveComposite;
	}
	
	public EnchantComposite getEnchantComposite() {
		return enchantComposite;
	}
	
	public WarehouseComposite getWarehouseComposite() {
		return warehouseComposite;
	}
	
	public TradeComposite getTradeComposite() {
		return tradeComposite;
	}
	
	public MaeipComposite getMaeipComposite() {
		return maeipcomposite;
	}
	
	public ConnectComposite getConnectComposite() {
		return connectComposite;
	}
	
	public DamageCheckComposite getDamageCheckComposite() {
		return damageCheckComposite;
	}
	
	public EnchantLostItemComposite getEnchantLostItemComposite() {
		return enchantLostItemComposite;
	}
	
	public SpeedHackComposite getSpeedHackComposite() {
		return speedHackComposite;
	}

	public int getTabSelectIdx(){
		return tabSelectIdx;
	}
}