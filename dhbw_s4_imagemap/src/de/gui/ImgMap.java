package de.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
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
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.util.*;

/**
 * Programm zum erstellen einer Image Map 
 * @author Leo Hellwich
 */
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
	private JMenuBar menuBar;
	private JMenu mnDatei;
	private JMenuItem mntmNeu;
	private JMenuItem mntmBeenden;
	private JMenuItem mntmNeuBild;
	private JMenuItem mntmHtmlSpeichern;
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
	private JToolBar toolBar_1;
	private JButton btnSave;

	// MISC
	Helper helper = new Helper();

	/**
	 * Constructur für Klasse ImgMap. Ruft die Methode init() auf.
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
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		handleClosing();

		/* Initial Bild */
		Graphics2D g = emptyImage.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 700, 500);
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

		btnSave = new JButton(
				new ImageIcon(getClass().getResource("/save.png")));
		toolBar_1.add(btnSave);

		createMenu();
		createToolbar();
		addListener();
		content();

	}

	/**
	 * Fängt das schließen des Festers auf
	 */
	private void handleClosing()
	{
		JFrame f = this;
		f.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent)
			{
				if (JOptionPane.showConfirmDialog(f,
						"Möchten sie das Programm wirklich beenden?",
						"Beenden", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
	}

	/**
	 * Methode zur Erstellung des Menüs
	 */
	private void createMenu()
	{
		int menuKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		menuBar = new JMenuBar();
		mnDatei = new JMenu("Datei");
		mnOptionen = new JMenu("Optionen");
		mnHilfe = new JMenu("Hilfe");
		mnLookAndFeel = new JMenu("Look and Feel");

		mntmNeu = new JMenuItem("Neu");
		mntmNeu.setAccelerator(KeyStroke.getKeyStroke('N', menuKeyMask));
		mntmNeuBild = new JMenuItem("Bild öffnen");
		mntmNeuBild.setAccelerator(KeyStroke
				.getKeyStroke('N', Event.SHIFT_MASK));
		mntmHtmlSpeichern = new JMenuItem("Html speichern");
		mntmHtmlSpeichern.setAccelerator(KeyStroke.getKeyStroke('S',
				menuKeyMask));
		mntmBeenden = new JMenuItem("Beenden");
		mntmBeenden.setAccelerator(KeyStroke.getKeyStroke('Q', menuKeyMask));

		mntmber = new JMenuItem("\u00DCber ...");

		rbtngLNF = new ButtonGroup();
		rdbtnmntmSystem = new JRadioButtonMenuItem("System");
		rdbtnmntmCrossplattform = new JRadioButtonMenuItem("Crossplattform");
		rdbtnmntmMotif = new JRadioButtonMenuItem("Motif");
		
		// Menübar
		setJMenuBar(menuBar);

		// Datei-Menü
		menuBar.add(mnDatei);

		mnDatei.add(mntmNeu);
		mnDatei.add(mntmNeuBild);
		mnDatei.add(new JSeparator());
		mnDatei.add(mntmHtmlSpeichern);
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

	/**
	 * Erstellt die Toolbar mit den Werkzeugen 
	 */
	private void createToolbar()
	{
		int menuKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

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

	/**
	 * Fügt den Komponenten auf dem Frame ihre benötigten Listener hinzu
	 */
	private void addListener()
	{
		// MENU / Datei
		mntmBeenden.addActionListener(this);
		mntmber.addActionListener(this);
		mntmHtmlSpeichern.addActionListener(this);
		mntmNeu.addActionListener(this);
		mntmNeuBild.addActionListener(this);
		
		mntmber.addActionListener(this);
		btnInfo.addActionListener(this);

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

		btnSave.addActionListener(this);
	}

	/**
	 * Löscht ein bereits geladenes Bild und entfernt alle markierten Bereiche
	 */
	private void doNeu()
	{
		this.mapPanel.flush();
	}

	/**
	 * Lädt ein Bild in den Zeichenbereich
	 */
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
	 * Schnittstelle zum Speichern der HTML-Map in eine Datei
	 */
	private void doSaveHTML()
	{
		String map = shapeList.getHTML(imgSrc, mapPanel.getWidth(),
				mapPanel.getHeight());
		helper.saveHtml(map, this);
	}

	/**
	 * Methode zur Erstellung des Statusbartextes des aktuellen Werkzeuges.
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
	 * Zeigt eine Nachricht für den Benutzer in der Statusleistenmitte an
	 * @param s Anzuzeigende Nachricht
	 */
	public void setStatusbarMessage(String s)
	{
		lblMessage.setText(",   \t\t" + s);
	}

	/**
	 * Zeig die aktuelle Mausposition an in der rechten Statusbar an
	 * @param s aktuelle Mausposition
	 */
	public void setStatusbarMouseposition(String s)
	{
		lblMouseposition.setText("Mausposition:" + s);
	}

	/**
	 * Zeigt den HTML-Code für die markierten Bereiche in einem seperaten Panel an
	 */
	public void setTextPanelHtml()
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

		if (src == this.btnNeu || src == this.mntmNeu)
			doNeu();
		if (src == this.btnNeuImg || src == this.mntmNeuBild)
			doLoadNewImage();
		if (src == this.btnSave || src == this.mntmHtmlSpeichern)
			doSaveHTML();
		if (src == this.mntmBeenden) {
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		if (src == this.mntmber || src == this.btnInfo) {
			JOptionPane.showMessageDialog(this, "Autor: Leo Hellwich");
		}

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

		/* Look & Feel */
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

}
