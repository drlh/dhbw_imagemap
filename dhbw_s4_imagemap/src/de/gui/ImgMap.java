package de.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
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

import de.util.ShapeList;
import de.util.shape.Shape;

import java.awt.Button;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class ImgMap extends JFrame implements ActionListener
{

	// CONSTANTS - Tools
	public final static int ARROW = 0;
	public final static int RECTANGLE = 1;
	public final static int CIRCLE = 2;
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

	// TOOLBAR
	private JToolBar toolBar = new JToolBar();
	private JButton btnNeuImg = new JButton("Bild laden");

	// TABS
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private JScrollPane sP_image;
	private JScrollPane sP_html = new JScrollPane();

	// STATUS
	private final JLabel lblStatusbar = new JLabel("New label");

	// IMAGEPANEL
	ImagemapPanel mapPanel;
	private final JButton btnNeu = new JButton("neu");
	private final JButton btnArrow = new JButton("Auswahl");
	private final JButton btnRectangle = new JButton("Rect");
	private final JButton btnCircle = new JButton("Circle");

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

		/* Initial Bild */
		Graphics2D g = emptyImage.createGraphics();
		g.setColor(Color.gray);
		g.fillRect(0, 0, 700, 500);
		image = emptyImage;

		mapPanel = new ImagemapPanel(this, image, shapeList);
		sP_image = new JScrollPane(mapPanel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sP_image.setPreferredSize(new Dimension(image.getWidth(null), image
				.getHeight(null)));
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

		// Toolbar
		btnNeu.addActionListener(this);
		btnNeuImg.addActionListener(this);
		btnArrow.addActionListener(this);
		btnCircle.addActionListener(this);
		btnRectangle.addActionListener(this);

		toolBar.add(btnNeu);
		toolBar.add(btnNeuImg);
		toolBar.add(Box.createHorizontalStrut(10));
		toolBar.add(btnArrow);
		toolBar.add(btnRectangle);
		toolBar.add(btnCircle);
		contentPane.add(toolBar, BorderLayout.NORTH);
		// Tabs
		tabbedPane.addTab("Image Map", sP_image);
		tabbedPane.addTab("HTML", sP_html);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		// Statusbar
		contentPane.add(lblStatusbar, BorderLayout.SOUTH);
	}

	/**
	 * Methode zur Erstellung des Menüs
	 */
	private void menu()
	{
		setJMenuBar(menuBar);

		// Datei-Menü
		menuBar.add(mnDatei);
		mnDatei.add(mntmNeu);
		mnDatei.add(separator);
		mnDatei.add(mntmBeenden);

		// Optionsmenü
		menuBar.add(mnOptionen);

		// Hilfe-Menü
		menuBar.add(mnHilfe);
		mnHilfe.add(mntmber);
	}

	/**
	 * Methode zur Erstellung des Statusbartextes
	 * @param mouseposition
	 */
	public void setStatusbarText(String mouseposition)
	{
		String s = "Werkzeug: ";
		switch (tool) {
		case ARROW:
			s += "Pfeil";
			break;
		case RECTANGLE:
			s += "Rechteck";
			break;
		case CIRCLE:
			s += "Kreis";
			break;
		}
		s += ", Mausposition: " + mouseposition;
		lblStatusbar.setText(s);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
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
		/* Neues Bild laden */
		if (e.getSource() == this.btnNeuImg) {
			JFileChooser fc = new JFileChooser();
			int result = fc.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				imgSrc = file.getAbsolutePath(); // THIS WAS THE PROBLEM
				try {
					image = ImageIO.read(file);
					System.out.println(imgSrc);
					mapPanel.setImage(image);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}//Neues Bild
		
		/* Werkzeug: Pfeil */
		if (e.getSource() == this.btnArrow) {
			tool = ARROW;
		}//Werkzeug Pfeil
		
		/* Werkzeug: Rechteck */
		if (e.getSource() == this.btnRectangle) {
			tool = RECTANGLE;
		}//Werkzeug Rechteck
		
		/* Werkzeug: Kreis */
		if (e.getSource() == this.btnCircle) {
			tool = CIRCLE;
		}//Werkzeug Kreis
	

	}
}
