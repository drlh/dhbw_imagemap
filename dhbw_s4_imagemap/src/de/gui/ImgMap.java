package de.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.Box;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.JTabbedPane;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.JScrollPane;
import javax.swing.JLabel;

import java.awt.Button;

public class ImgMap extends JFrame implements ActionListener{

	private JPanel contentPane;
	private Dimension framesize = new Dimension(800, 600);
	
	private String imgSrc = "";
	private BufferedImage emptyImage = new BufferedImage(500, 400, BufferedImage.TYPE_INT_RGB);
	
	
	//	MENU
	private JMenuBar menuBar = new JMenuBar();
	private JMenu mnDatei = new JMenu("Datei");
	private JMenuItem mntmNeu = new JMenuItem("Neu");
	private JSeparator separator = new JSeparator();
	private JMenuItem mntmBeenden = new JMenuItem("Beenden");
	private JMenu mnOptionen = new JMenu("Optionen");
	private JMenu mnHilfe = new JMenu("Hilfe");
	private JMenuItem mntmber = new JMenuItem("\u00DCber ...");
	
	//	TOOLBAR
	private JToolBar toolBar = new JToolBar();
	private JButton btnNeuImg = new JButton("Bild laden");
	
	//TABS
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private JScrollPane sP_image;
	private JScrollPane sP_html = new JScrollPane();
	
	//	STATUS
	private final JLabel lblStatusbar = new JLabel("New label");
	
	// 	IMAGEPANEL
	ImagemapPanel mapPanel;
	private final JButton btnCircle = new JButton("Circle");
	private final JButton btnNeu = new JButton("neu");
	private final JButton btnRectangle = new JButton("Rectangle");
	private final JButton btnPoly = new JButton("Poly");
	
	/**
	 * Create the frame.
	 */
	public ImgMap() 
	{
		init();
		content();
	}
	
	/**
	 * Initialisierungsmethode des Hauptfensters
	 */
	private void init()
	{
		this.setSize(framesize);
		this.setLocationRelativeTo(null);
		this.setTitle("ImageMap Editor");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/**
		 * @author noch zu machen
		 */
		 mapPanel = new ImagemapPanel(this, lblStatusbar, null);
		 sP_image = new JScrollPane(mapPanel);
	}
	
	/**
	 * Methode zur Unterbringung des Inhalts
	 */
	private void content()
	{
		menu();
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		//Toolbar
		btnNeuImg.addActionListener(this);
		
		toolBar.add(btnNeu);
		toolBar.add(btnNeuImg);
		toolBar.add(Box.createHorizontalStrut(10));
		toolBar.add(btnCircle);
		toolBar.add(btnRectangle);
		toolBar.add(btnPoly);
		contentPane.add(toolBar, BorderLayout.NORTH);
		//Tabs
		tabbedPane.addTab("New tab", null, sP_image, null);
		tabbedPane.addTab("New tab", null, sP_html, null);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		//Statusbar
		contentPane.add(lblStatusbar, BorderLayout.SOUTH);
	}
	/**
	 * Methode zur Erstellung des Menüs
	 */
	private void menu()
	{
		setJMenuBar(menuBar);
		
		//Datei-Menü
		menuBar.add(mnDatei);
		mnDatei.add(mntmNeu);
		mnDatei.add(separator);
		mnDatei.add(mntmBeenden);
		
		//Optionsmenü
		menuBar.add(mnOptionen);
		
		//Hilfe-Menü
		menuBar.add(mnHilfe);
		mnHilfe.add(mntmber);
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImgMap frame = new ImgMap();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == this.btnNeuImg) {
			JOptionPane.showMessageDialog(this, "hi");
		}
		
	}
}
