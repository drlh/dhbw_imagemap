package de.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.util.*;

public class ImgMap extends JFrame implements ActionListener
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
	private JMenuBar menuBar = new JMenuBar();
	private JMenu mnDatei = new JMenu("Datei");
	private JMenuItem mntmNeu = new JMenuItem("Neu");
	private JSeparator separator = new JSeparator();
	private JMenuItem mntmBeenden = new JMenuItem("Beenden");
	private JMenu mnOptionen = new JMenu("Optionen");
	private JMenu mnHilfe = new JMenu("Hilfe");
	private JMenuItem mntmber = new JMenuItem("\u00DCber ...");
	private final JMenu mnLookAndFeel = new JMenu("Look and Feel");
	private final ButtonGroup rbtngLNF = new ButtonGroup();
	private final JRadioButtonMenuItem rdbtnmntmSystem = new JRadioButtonMenuItem(
			"System");
	private final JRadioButtonMenuItem rdbtnmntmCrossplattform = new JRadioButtonMenuItem(
			"Crossplattform");
	private final JRadioButtonMenuItem rdbtnmntmMotif = new JRadioButtonMenuItem(
			"Motif");

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
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

		createMenu();
		createToolbar();
		content();
		addListener();
	}

	/**
	 * Methode zur Erstellung des Menüs
	 */
	private void createMenu()
	{
		setJMenuBar(menuBar);

		// Datei-Menü
		menuBar.add(mnDatei);
		mnDatei.add(mntmNeu);
		mnDatei.add(separator);
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
		btnNeu = new JButton(new ImageIcon("img/new.png"));
		btnNeuImg = new JButton("Bild laden");
		
		btnArrow = new JButton("Auswahl");
		btnRectangle = new JButton("Rect");
		btnCircle = new JButton("Circle");
		
		btnHref = new JButton("Link");
		btnInfo = new JButton("About");
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

	private void doLoadNewImage()
	{
		FileNameExtensionFilter filter = new FileNameExtensionFilter("jpg",
				"jpeg", "png", "gif", "bmp", "tif");
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(filter);

		int result = fc.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			imgSrc = file.getAbsolutePath(); // THIS WAS THE PROBLEM
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
		lblMessage.setText("	, Nachricht: " + s);
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

		/* Neues Bild laden */
		if (src == this.btnNeuImg) {
			doLoadNewImage();
			return;
		}

		/* Werkzeug: Pfeil */
		if (src == this.btnArrow) {
			tool = ARROW;
			return;
		}

		/* Werkzeug: Rechteck */
		if (src == this.btnRectangle) {
			tool = RECTANGLE;
			return;
		}

		/* Werkzeug: Kreis */
		if (src == this.btnCircle) {
			tool = CIRCLE;
			return;
		}

		/* Werkzeug: Link */
		if (src == this.btnHref) {
			tool = LINK;
			return;
		}

		/* 		 */
		if (src == this.rdbtnmntmCrossplattform) {
			helper.changeLookAndFeel(Helper.LF_CROSS, this);
			return;
		}
		if (src == this.rdbtnmntmSystem) {
			helper.changeLookAndFeel(Helper.LF_SYSTEM, this);
			return;
		}
		if (src == this.rdbtnmntmMotif) {
			helper.changeLookAndFeel(Helper.LF_MOTIF, this);
			return;
		}

		/* aktualisiert das Werkzeug in der Statusbar */
		setStatusbarWerkzeug();

	}
}
