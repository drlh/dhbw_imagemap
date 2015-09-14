package zeichnenStart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class ZeichenPanel extends JPanel implements ActionListener,
		MouseListener, MouseMotionListener {
	public final static int ARROW_TOOL = 0;
	public final static int SQUARE_TOOL = 1;
	public final static int CIRCLE_TOOL = 2;
	public final static int XXX_TOOL = 3;
	private int tool = ARROW_TOOL;

	private Point startPoint = null;
	private Point currentPoint = null;
	private Rectangle currentRect = null;
	private Shape currentShape = null;
	private BasicStroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_MITER, 1.0f, new float[] { 1.0f, 1.0f }, 0.0f);
	private Vector<Shape> vec = new Vector<Shape>();
	private JPopupMenu popup = new JPopupMenu();
	private JMenuItem moveTop = new JMenuItem("Nach vorne");
	private JMenuItem moveBack = new JMenuItem("Nach hinten");
	private JMenuItem delete = new JMenuItem("Löschen");
	private JMenuItem info = new JMenuItem("Eigenschaften");

	private ZeichenWin win = null;

	public ZeichenPanel(ZeichenWin win) {
		this.win = win;
		setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);

		popup.add(moveTop);
		popup.add(moveBack);
		popup.addSeparator();
		popup.add(delete);
		popup.addSeparator();
		popup.add(info);

		moveTop.addActionListener(this);
		moveBack.addActionListener(this);
		delete.addActionListener(this);
		info.addActionListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int button = e.getButton();
		startPoint = e.getPoint();

		if (tool == ARROW_TOOL) {
			if (button == MouseEvent.BUTTON1) {
				for (int i = vec.size() - 1; i >= 0; i--) {
					Shape s = vec.get(i);
					Rectangle r = s.getR();
					if (s.contains(startPoint)) {
						System.out.println(r);
						currentShape = s;
						currentRect = r;
						break;
					}
				}
			}
			if (button == MouseEvent.BUTTON3) {
				for (int i = vec.size() - 1; i >= 0; i--) {
					Shape s = vec.get(i);
					Rectangle r = s.getR();
					if (s.contains(startPoint)) {
						System.out.println(r);
						currentShape = s;
						currentRect = r;
						popup.show(this, e.getX(), e.getY());
						break;
					}
				}

			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Color colors[] = new Color[] { Color.RED, Color.GREEN, Color.BLUE };
		int zz = (int) (Math.random() * 3);
		Rectangle r = calcRect();
		Shape s = null;

		switch (tool) {
		case SQUARE_TOOL:
			s = new RectShape(colors[zz], r.x, r.y, r.width, r.height);
			break;
		case CIRCLE_TOOL:
			s = new OvalShape(colors[zz], r.x, r.y, r.width, r.height);
			break;
		case XXX_TOOL:
			s = new TriangleShape(colors[zz], r.x, r.y, r.width, r.height);
			break;
		}

		if (s != null) {
			vec.add(s);
			repaint();
		}

		if (tool == ARROW_TOOL && currentPoint != null && currentRect != null) {
			int offX = currentPoint.x - startPoint.x;
			int offY = currentPoint.y - startPoint.y;
			r = (Rectangle) currentRect.clone();
			r.translate(offX, offY);
			currentShape.setR(r);
			repaint();
		}
		startPoint = null;
		currentPoint = null;
		currentRect = null;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D gr = (Graphics2D) g;
		for (int i = 0; i < vec.size(); i++) {
			Shape s = vec.get(i);
			s.draw(gr);
		}

		if ((tool == SQUARE_TOOL || tool == CIRCLE_TOOL || tool == XXX_TOOL)
				&& startPoint != null && currentPoint != null) {
			Rectangle r = calcRect();
			gr.setStroke(stroke);
			gr.drawRect(r.x, r.y, r.width, r.height);
		}

	}

	private Rectangle calcRect() {
		Rectangle r = new Rectangle();

		if (currentPoint != null && startPoint != null) {
			int width = currentPoint.x - startPoint.x;
			int height = currentPoint.y - startPoint.y;

			r = new Rectangle(startPoint.x, startPoint.y, width, height);
			r = normalizeRect(r);
		}
		return r;
	}

	private static Rectangle normalizeRect(Rectangle r) {
		if (r != null) {
			if (r.width < 0) {
				r.width = -r.width;
				r.x -= r.width;
			}
			if (r.height < 0) {
				r.height = -r.height;
				r.y -= r.height;
			}
		}
		return r;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		currentPoint = e.getPoint();

		if (tool == ARROW_TOOL && currentRect != null) {
			int offX = currentPoint.x - startPoint.x;
			int offY = currentPoint.y - startPoint.y;
			Rectangle r = (Rectangle) currentRect.clone();
			r.translate(offX, offY);
			currentShape.setR(r);
		}
		repaint();

	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	public void doSave() {
		JFileChooser chooser = new JFileChooser();

		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				File f = chooser.getSelectedFile();
				FileOutputStream fos = new FileOutputStream(f);
				ObjectOutputStream out = new ObjectOutputStream(fos);
				out.writeObject(vec);
				out.close();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	@SuppressWarnings("unchecked")
	public void doOpen() {
		JFileChooser chooser = new JFileChooser();

		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				File f = chooser.getSelectedFile();
				FileInputStream fis = new FileInputStream(f);
				ObjectInputStream in = new ObjectInputStream(fis);
				vec = (Vector<Shape>) in.readObject();
				in.close();
				fis.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}

	}

	public void doTool(int t) {
		tool = t;
		if (t > ARROW_TOOL) {
			setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		} else {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		if (src == moveTop) {
			vec.remove(currentShape);
			vec.add(currentShape);
			repaint();
		}

		if (src == moveBack) {
			vec.remove(currentShape);
			vec.add(0, currentShape);
			repaint();
		}

		if (src == info) {
			currentShape.showInfoWin(win);
		}

		if (src == delete) {
			vec.remove(currentShape);
			currentShape = null;
			currentRect = null;
			repaint();
		}
	}

}
