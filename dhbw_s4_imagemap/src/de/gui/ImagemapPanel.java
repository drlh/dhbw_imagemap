package de.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import de.util.ShapeList;
import de.util.shape.Circle;
import de.util.shape.Rect;
import de.util.shape.Shape;

@SuppressWarnings("serial")
public class ImagemapPanel extends JPanel implements MouseListener,
		MouseMotionListener
{

	private ImgMap imgmap;
	private Image img;
	private ShapeList shapeList;
	private Shape currentShape;
	private BasicStroke stroke;

	private Point startP = null;
	private Point aktuellerP = null;

	/**
	 * Create the panel.
	 * 
	 * @param lblStatusbar
	 * @param img
	 * @param shapeList
	 */
	public ImagemapPanel(ImgMap im, Image img, ShapeList shapeList)
	{
		this.imgmap = im;
		this.img = img;
		this.shapeList = shapeList;
		this.stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 1.0f, new float[] { 1.0f, 1.0f }, 0.0f);
		this.setSize(img.getWidth(null), img.getHeight(null));
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.setVisible(true);

	}

	public void setImage(Image img)
	{
		this.img = img;
		this.setSize(img.getWidth(null), img.getHeight(null));
		revalidate();
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

		if ((imgmap.tool == ImgMap.RECTANGLE || imgmap.tool == ImgMap.CIRCLE )
				&& startP != null && aktuellerP != null) {
			Rectangle r = berechneRect();
			g2.setStroke(stroke);
			g2.setColor(Color.BLACK);
			g2.drawRect(r.x, r.y, r.width, r.height);
		}
	}

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

	@Override
	public void mouseClicked(MouseEvent e)
	{

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
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		Rectangle re = berechneRect();
		Shape s = null;

		switch (imgmap.tool) {
		case ImgMap.RECTANGLE:
			s = new Rect(re.x, re.y, re.width, re.height);
			break;
		case ImgMap.CIRCLE:
			s = new Circle(re.x, re.y, re.width, re.height);
			break;
		}
		
		if (s != null) {
			shapeList.addShape(s);
			repaint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		aktuellerP = e.getPoint();
		
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		Point p = e.getPoint();
		imgmap.setStatusbarText("X: " + p.getX() + ", Y: " + p.getY());
	}

}
