package zeichnenStart;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;


public class ZeichenWin extends JFrame implements ActionListener {
	private JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);
	private JToggleButton	arrowButton = new JToggleButton( new ImageIcon("arrow.gif"));
	private JToggleButton	squareButton = new JToggleButton( new ImageIcon("square.gif"));
	private JToggleButton	circleButton = new JToggleButton(new ImageIcon("circle.gif"));
	private JToggleButton	polyButton = new JToggleButton(new ImageIcon("poly.gif"));	
	
	private ZeichenPanel zeichenPanel = new ZeichenPanel(this);
	private JMenuBar	menuBar  = new JMenuBar();
	private JMenu		fileMenu = new JMenu("Datei");
	private JMenuItem	openItem = new JMenuItem("Öffnen");
	private JMenuItem	saveItem = new JMenuItem("Sichern");
	private JMenuItem	quitItem = new JMenuItem("Beenden");
	
	public ZeichenWin(String title) throws HeadlessException {
		super(title);

		buildWindow();
	}


	private void buildWindow() {
		buildMenuBar();
		buildToolBar();
		
		add(zeichenPanel);
	}


	private void buildToolBar() {
		ButtonGroup	group = new ButtonGroup();
		group.add(arrowButton);
		group.add(squareButton);
		group.add(circleButton);
		group.add(polyButton);
		squareButton.setSelected(true);
		
		add(toolbar, BorderLayout.NORTH);
		toolbar.add(arrowButton);
		toolbar.add(squareButton);
		toolbar.add(circleButton);
		toolbar.add(polyButton);
		
		arrowButton.addActionListener(this);
		squareButton.addActionListener(this);
		circleButton.addActionListener(this);
		polyButton.addActionListener(this);
		
	}


	private void buildMenuBar() {
		menuBar.add(fileMenu);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.addSeparator();
		fileMenu.add(quitItem);
		
		openItem.addActionListener(this);
		openItem.setAccelerator(
				KeyStroke.getKeyStroke('O', InputEvent.CTRL_MASK));
		saveItem.addActionListener(this);
		quitItem.addActionListener(this);
		quitItem.setAccelerator(
				KeyStroke.getKeyStroke('Q', InputEvent.CTRL_MASK));
		setJMenuBar(menuBar);
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		Object	src = e.getSource();
		
		if (src == arrowButton) {
			zeichenPanel.doTool(ZeichenPanel.ARROW_TOOL);
		}
		
		if (src == squareButton) {
			zeichenPanel.doTool(ZeichenPanel.SQUARE_TOOL);
		}

		if (src == circleButton) {
			zeichenPanel.doTool(ZeichenPanel.CIRCLE_TOOL);
		}
		
		if (src == polyButton) {
			zeichenPanel.doTool(ZeichenPanel.XXX_TOOL);
		}
		
		if (src == openItem) {
			zeichenPanel.doOpen();
		}
		
		if (src == saveItem) {
			zeichenPanel.doSave();
		}
		
		if (src == quitItem) {
			System.exit(0);
		}
		
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ZeichenWin	win = new ZeichenWin("Zeichne Vektor-Grafik");
		
		win.setBounds(0, 0, 400, 400);
		win.setVisible(true);
		win.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}



}
