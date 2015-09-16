package de.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.MouseInputListener;

import de.util.ShapeList;
import de.util.shape.Circle;
import de.util.shape.Rect;
import de.util.shape.Shape;

import java.awt.Component;
import java.awt.event.MouseAdapter;

import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class ImagemapPanel extends JPanel implements MouseListener,
		MouseMotionListener, ActionListener
{
	private final int COPY = 1;
	private final int PASTE = 2;
	private final int CUT = 3;
	private final int DEL = 4;
	private int popupSelection = 0;

	private ImgMap imgmap;
	private Image img;
	private ShapeList shapeList;
	private BasicStroke stroke;

	private Point startP = null;
	private Point aktuellerP = null;
	private Shape aktuellerShape = null;
	private Shape cloneShape = null;
	private Point cloneP = null;
	private Rectangle aktuelleR = null;
	private JPopupMenu popupMenu;

	private JMenuItem mntmKopieren;
	private JMenuItem mntmEinfgen;
	private JMenuItem mntmAusschneiden;
	private JMenuItem mntmLschen;

	/**
	 * Konstruktor für das Bildpanel
	 * 
	 * @param im
	 *            Übergeordnete ImgMap Klasse
	 * @param img
	 *            Bild, welches gezeihnet werden soll
	 * @param shapeList
	 *            Liste mit den einzuzeichnenden Bereichen
	 */
	public ImagemapPanel(ImgMap im, Image img, ShapeList shapeList)
	{
		this.imgmap = im;
		this.img = img;
		this.shapeList = shapeList;
		this.stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 1.0f, new float[] { 1.0f, 1.0f }, 0.0f);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.addMouseMotionListener(this);
		this.addMouseListener(this);

		popupMenu = new JPopupMenu();
		mntmKopieren = new JMenuItem("Kopieren");
		mntmEinfgen = new JMenuItem("Einf\u00FCgen");
		mntmAusschneiden = new JMenuItem("Ausschneiden");
		mntmLschen = new JMenuItem("L\u00F6schen");

		addPopup(this, popupMenu);
		popupMenu.add(mntmKopieren);
		popupMenu.add(mntmEinfgen);
		popupMenu.add(mntmAusschneiden);
		popupMenu.add(mntmLschen);

		mntmKopieren.addActionListener(this);
		mntmEinfgen.addActionListener(this);
		mntmAusschneiden.addActionListener(this);
		mntmLschen.addActionListener(this);

		mntmEinfgen.setEnabled(false);

		this.setVisible(true);
	}

	/**
	 * Zeichnet ein Bild in die Zeichenfläche
	 * 
	 * @param img
	 *            zu zeichnendes Bild
	 */
	public void setImage(Image img)
	{
		this.img = img;
		this.setPreferredSize(new Dimension((int) img.getWidth(null), (int) img
				.getHeight(null)));
		revalidate();
		repaint();
	}

	/**
	 * Errechnet das Shape, welches aus zei Punkten entsteht
	 * 
	 * @return Rectangle
	 */
	private Rectangle berechneRect()
	{
		Rectangle r = new Rectangle();

		if (aktuellerP != null && startP != null) {
			int width = aktuellerP.x - startP.x;
			int height = aktuellerP.y - startP.y;

			r = new Rectangle(startP.x, startP.y, width, height);
			r = ausgleichRect(r);
		}
		return r;
	}

	/**
	 * Normalisiert das eingezeichnete Objekt.
	 * @param re zu normalisierendes Objekt
	 * @return Gibt das normiliserte Object zurück
	 */
	private Rectangle ausgleichRect(Rectangle re)
	{
		if (re != null) {
			if (re.width < 0) {
				re.width = -re.width;
				re.x -= re.width;
			}
			if (re.height < 0) {
				re.height = -re.height;
				re.y -= re.height;
			}
		}
		return re;
	}

	/**
	 * Methode zum verschieben des SHapes bei gedrückter Maustaste
	 * @param r zu verschiebende Rectangle eines Shapes
	 */
	private void verschiebeRect(Rectangle r)
	{
		if (imgmap.tool == ImgMap.ARROW && aktuelleR != null
				&& aktuellerP != null) {
			int offX = aktuellerP.x - startP.x;
			int offY = aktuellerP.y - startP.y;
			r = (Rectangle) aktuelleR.clone();
			r.translate(offX, offY);
			aktuellerShape.setShape(r);
			repaint();
		}
	}

	/**
	 * Fügt einem ausgewähten Shape einen Html-Link hinzu
	 */
	private void setLink()
	{
		if (aktuellerShape != null) {
			String s = JOptionPane.showInputDialog("Link: ", "http://");
			aktuellerShape.setHref(s);

			this.imgmap.tool = ImgMap.ARROW;
			this.imgmap.setStatusbarWerkzeug();
			this.imgmap.setStatusbarMessage("");
		}
	}

	/**
	 * Entfernt das aktuelle Bild und löscht alle eingezeichneten Bereiche
	 */
	public void flush()
	{
		shapeList.flush();
		this.img = new BufferedImage(700, 500, BufferedImage.TYPE_INT_RGB);
		repaint();
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		if (img != null) {
			g2.drawImage(img, 0, 0, null);
		}
		if (shapeList.size() > 0) {
			for (int i = 0; i < shapeList.size(); i++) {
				shapeList.getShape(i).draw(g2);
			}
		}

		if ((imgmap.tool == ImgMap.RECTANGLE || imgmap.tool == ImgMap.CIRCLE)
				&& startP != null && aktuellerP != null) {
			Rectangle r = berechneRect();
			g2.setStroke(stroke);
			g2.setColor(Color.BLACK);
			g2.drawRect(r.x, r.y, r.width, r.height);
		}

		/* Aufbau des HTML-Codes */
		imgmap.setTextPanelHtml();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON3) {

			for (int i = shapeList.size() - 1; i >= 0; i--) {
				Shape shape = shapeList.getShape(i);
				Rectangle re = shape.getShape();
				if (shape.contains(startP)) {
					System.out.println("Drin");
					aktuellerShape = shape;
					aktuelleR = re;
					cloneP = e.getPoint();
					cloneShape = aktuellerShape;
					popupMenu.show(this, e.getX(), e.getY());
					break;
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{

	}

	@Override
	public void mouseExited(MouseEvent e)
	{

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		startP = e.getPoint();

		if (imgmap.tool == ImgMap.ARROW || imgmap.tool == ImgMap.LINK) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				for (int i = 0; i < shapeList.size(); i++) {
					Shape shape = shapeList.getShape(i);
					Rectangle re = shape.getShape();
					if (shape.contains(startP)) {
						System.out.println("Drin");
						aktuellerShape = shape;
						aktuelleR = re;
					}
				}
			}

		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		System.out.println("released");
		Rectangle re = berechneRect();
		Shape s = null;

		switch (imgmap.tool) {
		case ImgMap.RECTANGLE:
			s = new Rect(re.x, re.y, re.width, re.height);
			break;
		case ImgMap.CIRCLE:
			s = new Circle(re.x, re.y, re.width, re.height);
			break;
		case ImgMap.LINK:
			setLink();
			break;
		}

		if (e.getButton() == MouseEvent.BUTTON3) {
			for (int i = shapeList.size() - 1; i >= 0; i--) {

				Shape shape = shapeList.getShape(i);
				Rectangle re1 = shape.getShape();
				if (shape.contains(startP)) {
					System.out.println("Drin");
					aktuellerShape = shape;
					aktuelleR = re1;
					cloneP = e.getPoint();
					cloneShape = aktuellerShape;
					popupMenu.show(this, e.getX(), e.getY());
					break;
				}
			}
		}

		if (s != null) {
			shapeList.addShape(s);
			repaint();
		}

		/* Verschiebt das gezeichnete Obj, falls verschoben */
		verschiebeRect(aktuelleR);

		startP = null;
		aktuellerP = null;
		aktuelleR = null;
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		aktuellerP = e.getPoint();

		verschiebeRect(aktuelleR);

		imgmap.setStatusbarMouseposition("X: " + aktuellerP.getX() + ", Y: "
				+ aktuellerP.getY());

		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		Point p = e.getPoint();
		cloneP = p;
		imgmap.setStatusbarMouseposition("X: " + p.getX() + ", Y: " + p.getY());
	}

	private static void addPopup(Component component, final JPopupMenu popup)
	{
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e)
			{
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e)
			{
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e)
			{
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();

		if (src == mntmKopieren) {
			popupSelection = COPY;
			cloneShape = aktuellerShape;
			mntmEinfgen.setEnabled(true);
		}
		if (src == mntmEinfgen) {
			popupSelection = PASTE;

			Rectangle r = cloneShape.getShape();
			r.setBounds(cloneP.x, cloneP.y, (int) r.getWidth(),
					(int) r.getHeight());

			cloneShape.setShape(r);
			shapeList.addShape(cloneShape);

			mntmEinfgen.setEnabled(false);
			cloneShape = null;
			repaint();
		}
		if (src == mntmAusschneiden) {
			popupSelection = CUT;
			shapeList.deleteShape(aktuellerShape);
			mntmEinfgen.setEnabled(true);
			repaint();
		}
		if (src == mntmLschen) {
			System.out.println("löschen");
			System.out.println(cloneShape.toString());
			shapeList.deleteShape(cloneShape);
			mntmEinfgen.setEnabled(false);
			repaint();
		}

	}
}
