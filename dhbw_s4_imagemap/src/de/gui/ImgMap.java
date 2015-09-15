package de.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.util.*;

public class ImgMap extends JFrame implements ActionListener, WindowListener
{

	// CONSTANTS - Tools
	public final static int ARROW = 0;
	public final static int RECTANGLE = 1;
	public final static int CIRCLE = 2;
	public final static int LINK = 4;
	public int tool = 0;

	// CONTENTPANE
	private JPanel contentPane;
	private Dimension framesize = new Dimension(800, 600);

	private String imgSrc = "";
	private BufferedImage emptyImage = new BufferedImage(700, 500,
			BufferedImage.TYPE_INT_RGB);
	private Image image;

	// SHAPES
	private ShapeList shapeList = new ShapeList();

	// MENU
	private JMenuBar menuBar;
	private JMenu mnDatei;
	private JMenuItem mntmNeu;
	private JMenuItem mntmBeenden;
	private JMenuItem mntmNeuBild;
	private JMenu mnOptionen;
	private JMenu mnHilfe;
	private JMenuItem mntmber;
	private JMenu mnLookAndFeel;
	private ButtonGroup rbtngLNF;
	private JRadioButtonMenuItem rdbtnmntmSystem;
	private JRadioButtonMenuItem rdbtnmntmCrossplattform;
	private JRadioButtonMenuItem rdbtnmntmMotif;

	// TOOLBAR
	private JToolBar toolBar = new JToolBar();
	private JButton btnNeuImg;
	private JButton btnNeu;
	private JButton btnArrow;
	private JButton btnRectangle;
	private JButton btnCircle;
	private JButton btnHref;
	private JButton btnInfo;

	// TABS
	private JTabbedPane tabbedPane;
	private JScrollPane sP_image;
	private JScrollPane sP_html;

	// IMAGEPANEL
	ImagemapPanel mapPanel;
	private final JTextArea txtAHtml = new JTextArea();
	private final JPanel panelStatusbar = new JPanel();
	private final JLabel lblWerkzeug = new JLabel(" ");
	private final JLabel lblMessage = new JLabel(" ");
	private final JLabel lblMouseposition = new JLabel(" ");

	// MISC
	Helper helper = new Helper();
	private JToolBar toolBar_1;
	private JButton btnSave;

	/**
	 * Create the frame.
	 */
	public ImgMap()
	{
		init();
	}

	/**
	 * Initialisierungsmethode des Hauptfensters
	 */
	private void init()
	{
		this.setSize(framesize);
		this.setLocationRelativeTo(null);
		this.setTitle("ImageMap Editor");
		this.setDefaultCloseOperation(endProgramm());

		/* Initial Bild */
		image = emptyImage;

		mapPanel = new ImagemapPanel(this, image, shapeList);
		sP_image = new JScrollPane(mapPanel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sP_image.setPreferredSize(new Dimension(this.getWidth(), this
				.getHeight()));

		sP_html = new JScrollPane(txtAHtml,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sP_html.setPreferredSize(new Dimension(this.getWidth(), this
				.getHeight()));
		
		toolBar_1 = new JToolBar();
		sP_html.setColumnHeaderView(toolBar_1);
		
		btnSave = new JButton("Save");
		toolBar_1.add(btnSave);

		createMenu();
		createToolbar();
		addListener();
		content();
	}

	/**
	 * Methode zur Erstellung des Menüs
	 */
	private void createMenu()
	{
		menuBar = new JMenuBar();
		mnOptionen = new JMenu("Optionen");
		mnHilfe = new JMenu("Hilfe");
		mnLookAndFeel = new JMenu("Look and Feel");

		mnDatei = new JMenu("Datei");
		mntmNeu = new JMenuItem("Neu");
		mntmNeuBild = new JMenuItem("Bild öffnen");
		mntmBeenden = new JMenuItem("Beenden");

		mntmber = new JMenuItem("\u00DCber ...");

		rbtngLNF = new ButtonGroup();
		rdbtnmntmSystem = new JRadioButtonMenuItem("System");
		rdbtnmntmCrossplattform = new JRadioButtonMenuItem("Crossplattform");
		rdbtnmntmMotif = new JRadioButtonMenuItem("Motif");

		setJMenuBar(menuBar);

		// Datei-Menü
		menuBar.add(mnDatei);
		mnDatei.add(mntmNeu);
		mnDatei.add(mntmNeuBild);
		mnDatei.add(new JSeparator());
		mnDatei.add(mntmBeenden);

		// Optionsmenü
		menuBar.add(mnOptionen);

		mnOptionen.add(mnLookAndFeel);

		mnLookAndFeel.add(rdbtnmntmSystem);
		mnLookAndFeel.add(rdbtnmntmCrossplattform);
		mnLookAndFeel.add(rdbtnmntmMotif);

		rbtngLNF.add(rdbtnmntmCrossplattform);
		rbtngLNF.add(rdbtnmntmMotif);
		rbtngLNF.add(rdbtnmntmSystem);

		// Hilfe-Menü
		menuBar.add(mnHilfe);
		mnHilfe.add(mntmber);
	}

	private void createToolbar()
	{
		btnNeu = new JButton(new ImageIcon(getClass().getResource("/new.png")));
		btnNeu.setToolTipText("Neues Projekt");
		btnNeuImg = new JButton(new ImageIcon(getClass().getResource(
				"/picture.png")));
		btnNeuImg.setToolTipText("Bild laden");

		btnArrow = new JButton(new ImageIcon(getClass().getResource(
				"/arrow.png")));
		btnArrow.setToolTipText("Auswahl Werkzeug");
		btnRectangle = new JButton(new ImageIcon(getClass().getResource(
				"/rect.png")));
		btnRectangle.setToolTipText("Rechteck zeichnen");
		btnCircle = new JButton(new ImageIcon(getClass().getResource(
				"/ellipse.png")));
		btnCircle.setToolTipText("Ellipse zeichnen");

		btnHref = new JButton(new ImageIcon(getClass().getResource(
				"/commands.png")));
		btnHref.setToolTipText("Link zum Bereich hinzufügen");
		btnInfo = new JButton(
				new ImageIcon(getClass().getResource("/help.png")));
		btnInfo.setToolTipText("Über ...");
	}

	/**
	 * Methode zur Unterbringung des Inhalts
	 */
	private void content()
	{
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// Statusbar
		panelStatusbar.setLayout(new BorderLayout(0, 0));
		panelStatusbar.add(lblWerkzeug, BorderLayout.WEST);
		panelStatusbar.add(lblMessage, BorderLayout.CENTER);
		panelStatusbar.add(lblMouseposition, BorderLayout.EAST);
		contentPane.add(panelStatusbar, BorderLayout.SOUTH);

		// Toolbar
		toolBar.add(btnNeu);
		toolBar.add(btnNeuImg);
		toolBar.add(Box.createHorizontalStrut(12));
		toolBar.add(btnArrow);
		toolBar.add(btnRectangle);
		toolBar.add(btnCircle);
		toolBar.add(Box.createHorizontalStrut(12));
		toolBar.add(btnHref);
		toolBar.add(btnInfo);
		contentPane.add(toolBar, BorderLayout.NORTH);

		// Tabs
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Image Map", sP_image);
		tabbedPane.addTab("HTML", sP_html);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

	}

	private void addListener()
	{
		// MENU / Option / Look & Feel
		rdbtnmntmCrossplattform.addActionListener(this);
		rdbtnmntmMotif.addActionListener(this);
		rdbtnmntmSystem.addActionListener(this);

		// TOOLBAR
		btnNeu.addActionListener(this);
		btnNeuImg.addActionListener(this);
		btnArrow.addActionListener(this);
		btnCircle.addActionListener(this);
		btnRectangle.addActionListener(this);
		btnHref.addActionListener(this);
		btnInfo.addActionListener(this);
	}

	/**
	 * 
	 */
	private void doNeu()
	{
		this.mapPanel.flush();
	}

	private void doLoadNewImage()
	{
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Bilder",
				"jpg", "jpeg", "png", "gif", "bmp", "tif");
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(filter);

		int result = fc.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			imgSrc = file.getAbsolutePath();

			try {
				if (filter.accept(file)) {
					image = ImageIO.read(file);
					System.out.println(imgSrc);
					mapPanel.setImage(image);
				} else {
					JOptionPane
							.showMessageDialog(
									this,
									"Es sind nur Dateien folgender Typen zugelassen:\n .jpg, .jpeg, .png, .gif, .bmp, .tif",
									"Falscher Dateityp",
									JOptionPane.WARNING_MESSAGE);
				}

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Beendet das Programm nach einer Abfrage
	 */
	private int endProgramm()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Methode zur Erstellung des Statusbartextes des aktuellen Werkzeuges
	 * 
	 * @param mouseposition
	 */
	public void setStatusbarWerkzeug()
	{
		String s = "Werkzeug: ";
		switch (tool) {
		case ARROW:
			s += "Auswahl";
			break;
		case RECTANGLE:
			s += "Rechteck";
			break;
		case CIRCLE:
			s += "Kreis";
			break;
		case LINK:
			s += "Link setzen";
			break;
		}
		lblWerkzeug.setText(s);
	}

	/**
	 * 
	 * @param s
	 */
	public void setStatusbarMessage(String s)
	{
		lblMessage.setText(",   \t\t" + s);
	}

	public void setStatusbarMouseposition(String s)
	{
		lblMouseposition.setText("Mausposition:" + s);
	}

	public void readFromVectorHtmlText()
	{
		int panelW = this.mapPanel.getWidth();
		int panelH = this.mapPanel.getHeight();

		String s = shapeList.getHTML(imgSrc, panelW, panelH);
		this.txtAHtml.setText(s);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();

		/* Neues Project und neues Bild */
		if (src == this.btnNeu)
			doNeu();

		if (src == this.btnNeuImg)
			doLoadNewImage();

		/* Werkzeuge */
		if (src == this.btnArrow)
			tool = ARROW;

		if (src == this.btnRectangle)
			tool = RECTANGLE;

		if (src == this.btnCircle)
			tool = CIRCLE;

		if (src == this.btnHref) {
			tool = LINK;
			setStatusbarMessage("Bitte wählen sie einen Shape aus!");
		}

		/* 		 */
		if (src == this.rdbtnmntmCrossplattform) {
			helper.changeLookAndFeel(Helper.LF_CROSS, this);
		}
		if (src == this.rdbtnmntmSystem) {
			helper.changeLookAndFeel(Helper.LF_SYSTEM, this);
		}
		if (src == this.rdbtnmntmMotif) {
			helper.changeLookAndFeel(Helper.LF_MOTIF, this);
		}

		/* aktualisiert das Werkzeug in der Statusbar */
		setStatusbarWerkzeug();

	}

	@Override
	public void windowActivated(WindowEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e)
	{
		// TODO Auto-generated method stub
		
	}
}
