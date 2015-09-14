package img;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MalPanel extends JPanel implements MouseListener,
		MouseMotionListener {

	private float stroke;
	private Color color;
	private int strokeType;
	private Point lp;
	private int imgX = 1000;
	private int imgY = 800;

	public int getImgX() {
		return imgX;
	}

	public void setImgX(int imgX) {
		this.imgX = imgX;
		repaint();
	}

	public int getImgY() {
		return imgY;
	}

	public void setImgY(int inty) {
		this.imgY = inty;
		repaint();
	}

	private BufferedImage img = new BufferedImage(imgX, imgY,
			BufferedImage.TYPE_INT_ARGB);
	private long lastEdit = 0;

	public MalPanel() {
		super();
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point p = e.getPoint();
		Graphics2D g = (Graphics2D) img.getGraphics();

		if (lp != null) {
			BasicStroke str = new BasicStroke(stroke, strokeType,
					BasicStroke.CAP_ROUND);
			g.setStroke(str);
			g.setColor(color);
			g.drawLine(lp.x, lp.y, p.x, p.y);
		}
		lp = p;
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		lp = null;

		lastEdit = System.currentTimeMillis();
	}

	public long getLastEdit() {
		return lastEdit;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private void print() {

	}

	public void doNew(int x, int y) {
		setImgX(x);
		setImgY(y);
	}

	public void doPrint() {
		PrinterJob printJob = PrinterJob.getPrinterJob();

		if (printJob.printDialog()) { // Druckerauswahl & Papierformat etc
			printJob.setPrintable((Printable) this);// MalPanel muss printable
													// implementieren
			try {
				printJob.print();
			} catch (PrinterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, e,
						"Es ist ein Fehler beim Drucken aufgetreten:",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	public int print(Graphics g, PageFormat pf, int pageIndex)
			throws PrinterException {
		// TODO Auto-generated method stub
		super.print(g);
		if (pageIndex == 0) {
			double width = pf.getWidth();
			double height = pf.getHeight();
			int imgWidth = img.getWidth();
			int imgHeight = img.getHeight();
			double scale = Math.min(width / imgWidth, height / imgHeight);

			imgWidth *= scale;
			imgHeight *= scale;

			int x = (int) ((width - imgWidth) / 2 + pf.getImageableX());
			int y = (int) ((height - imgHeight) / 2 + pf.getImageableY());

			g.drawImage(img, x, y, imgWidth, imgHeight, null);
			return Printable.PAGE_EXISTS;

		} else {
			return Printable.NO_SUCH_PAGE;
		}

	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, null);
	}

	public float getStroke() {
		return stroke;
	}

	public void setStroke(float staerke) {
		this.stroke = staerke;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getStrokeType() {
		return strokeType;
	}

	public void setStrokeType(int strokeType) {
		this.strokeType = strokeType;
	}

	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
		repaint();
	}
}
