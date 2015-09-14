package zeichnenStart;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.SoftBevelBorder;

public class InfoDialog extends JDialog implements ActionListener{
	private JPanel buttonPanel = new JPanel();
	private JButton ok = new JButton("‹bernehmen");
	private JButton close = new JButton("Schlieﬂen");
	private Shape shape;
	private JPanel infoPanel = new JPanel();
	private JLabel xLabel = new JLabel("x: ");
	private JTextField xText = new JTextField();
	private JTextField yText = new JTextField();
	private JLabel yLabel = new JLabel("y: ");
	private JLabel area = new JLabel();
	private JLabel umfang = new JLabel();
	private JButton colorButton = new JButton("Farbe");

	public InfoDialog(Frame frame, String title, boolean modal, Shape s) {
		super(frame, title, modal);

		shape = s;
		setLayout(new BorderLayout());
		buttonPanel.setBorder(new SoftBevelBorder(
				SoftBevelBorder.RAISED));
		add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.add(ok);
		buttonPanel.add(close);
		
		infoPanel.setLayout(null);
		add(infoPanel, BorderLayout.CENTER);

		xLabel.setBounds(10, 10, 30, 30);
		xText.setBounds(40, 10, 40, 30);
		xText.setText(""+s.getR().x);
		xText.setHorizontalAlignment(JTextField.RIGHT);
		infoPanel.add(xLabel);
		infoPanel.add(xText);
		
		yLabel.setBounds(10, 40, 30, 30);
		yText.setBounds(40, 40, 40, 30);
		yText.setText(""+s.getR().y);
		yText.setHorizontalAlignment(JTextField.RIGHT);
		infoPanel.add(yLabel);
		infoPanel.add(yText);
		
		double val = shape.calcArea();
		area.setText("Fl‰che: " + (int)val);
		area.setBounds(10, 70, 180, 30);
		umfang.setBounds(10, 100, 180, 30);
		infoPanel.add(area);
		
		val = shape.calcUmfang();
		umfang.setText("Umfang: " + (int)val);
		infoPanel.add(umfang);
		
		ok.addActionListener(this);
		close.addActionListener(this);
		
		colorButton.setBounds(10, 130, 80, 30);
		infoPanel.add(colorButton);
		colorButton.setBackground(shape.getColor());
		colorButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object	src = e.getSource();
		
		if (src == ok) {
			Rectangle r = shape.getR();
			r.x = Integer.parseInt(xText.getText());
			r.y = Integer.parseInt(yText.getText());
			
			shape.setR(r);
			shape.setColor(colorButton.getBackground());
			getOwner().repaint();
		}
		
		if (src == close) {
			setVisible(false);
			dispose();
		}
		
		if (src == colorButton) {
			Color newColor = JColorChooser.showDialog(this, "Farbauswahl", shape.getColor());
			
			if (newColor != null) {
				colorButton.setBackground(newColor);
			}
			
		}
	}
}
