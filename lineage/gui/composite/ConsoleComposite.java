package lineage.gui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.swtdesigner.SWTResourceManager;

import lineage.share.System;
import lineage.util.Util;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ConsoleComposite extends Composite {

	private StyledText styledText;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ConsoleComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		
		styledText = new StyledText(this, SWT.BORDER | SWT.V_SCROLL);
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		styledText.setEditable(false);
		styledText.setDoubleClickEnabled(false);
		
		Menu menu = new Menu(styledText);
		styledText.setMenu(menu);
		
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				styledText.setText("");
			}
		});
		menuItem.setText("화면청소");
		println("'서버 구동 방법' 시스템  > 서버 가동'");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public void println(Object o){
		print( String.format("%s\r\n", String.valueOf(o)) );
	}

	public void println(String msg){
		print( String.format("%s\r\n", msg) );
		//서버 가동시간 체크
		print( String.format("[%s]  ", Util.getLocaleString(System.currentTimeMillis(), true)));
	}
	
	public void print(Object o){
		print( String.valueOf(o) );
	}
	
	public void print(String msg){
		styledText.append( msg );
		styledText.setTopIndex(styledText.getVerticalBar().getMaximum());
	}
}
