package img;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.html.Option;

public class MahlprogrammWin extends JFrame implements ActionListener,
		ChangeListener, MouseListener {
	private JMenuBar menu = new JMenuBar();
	private JMenu file = new JMenu("Datei");
	private JMenuItem newItem = new JMenuItem("Neu");
	private JMenuItem openItem = new JMenuItem("Öffnen");
	private JMenuItem quitItem = new JMenuItem("Beenden");
	private JMenuItem saveItem = new JMenuItem("Speichern");
	private JMenuItem saveAsItem = new JMenuItem("Speichern unter");
	private JMenuItem printItem = new JMenuItem("Drucken");
	private JButton color = new JButton("Farbe");
	private JSlider stroke = new JSlider(0, 1000, 500);
	private JComboBox<String> strokeType = new JComboBox<String>();
	private JToolBar toolBar = new JToolBar();
	private MalPanel mp = new MalPanel();

	public MahlprogrammWin(String title) throws HeadlessException {
		super(title);

		buildWin();
	}

	private void buildWin() {
		buildMenu();

		color.setOpaque(true);

		strokeType.addItem("ohne");
		strokeType.addItem("rund");
		strokeType.addItem("eckig");

		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(color);
		toolBar.add(stroke);
		toolBar.add(strokeType);

		color.addActionListener(this);
		color.setBackground(Color.white);

		stroke.addChangeListener(this);

		strokeType.addActionListener(this);
		add(toolBar, BorderLayout.NORTH);
		add(mp, BorderLayout.CENTER);

	}

	private void buildMenu() {

		newItem.setAccelerator(KeyStroke.getKeyStroke('N',
				KeyEvent.CTRL_DOWN_MASK));
		saveAsItem.setAccelerator(KeyStroke.getKeyStroke('S',
				KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
		saveItem.setAccelerator(KeyStroke.getKeyStroke('S',
				KeyEvent.CTRL_DOWN_MASK));
		openItem.setAccelerator(KeyStroke.getKeyStroke('O',
				KeyEvent.CTRL_DOWN_MASK));
		quitItem.setAccelerator(KeyStroke.getKeyStroke('Q',
				KeyEvent.CTRL_DOWN_MASK));

		newItem.addActionListener(this);
		saveAsItem.addActionListener(this);
		saveItem.addActionListener(this);
		openItem.addActionListener(this);
		quitItem.addActionListener(this);

		menu.add(file);
		file.add(newItem);
		file.addSeparator();

		file.add(openItem);
		file.addSeparator();

		file.add(saveItem);
		file.add(saveAsItem);
		file.addSeparator();

		file.add(quitItem);

		setJMenuBar(menu);

	}

	private void doSave() {
		JFileChooser fc = new JFileChooser();
		if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			try {
				ImageIO.write(mp.getImg(), "png", f);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this,
						"Es ist ein Fehler aufgetreten: " + e);
			}

		}
	}

	private void doSaveAS() {
		// TODO Auto-generated method stub

	}

	private void doExit() {
		JOptionPane.showConfirmDialog(this, "Wollen Sie das Programm verlassen ohne zu speichern?", "Tschüss", JOptionPane.YES_NO_CANCEL_OPTION);
			
	}

	private void doOpen() {
		JFileChooser fc = new JFileChooser();
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			try {
				mp.setImg(ImageIO.read(f));
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this,
						"Es ist ein Fehler aufgetreten: " + e);
			}
		}
	}

	private void doNew() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		MahlprogrammWin win = new MahlprogrammWin("Neues Bild");
		win.setBounds(0, 0, 600, 620);
		win.setVisible(true);
		win.setDefaultCloseOperation(EXIT_ON_CLOSE);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Object src = e.getSource();
		if (src == stroke) {
			mp.setStroke((float) (stroke.getValue() / 10.0));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == newItem) {
			doNew();
		}
		if (src == openItem) {
			doOpen();
		}
		if (src == quitItem) {
			doExit();
		}
		if (src == saveAsItem) {
			doSaveAS();
		}
		if (src == saveItem) {
			doSave();
		}
		if (src == color) {
			Color chosenColor = JColorChooser.showDialog(this, "Farbe wählen:",
					Color.WHITE);
			color.setBackground(chosenColor);
			mp.setColor(chosenColor);

		}
		if (src == strokeType) {
			mp.setStrokeType(strokeType.getSelectedIndex());
		}

	}

}
