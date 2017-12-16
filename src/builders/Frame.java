package builders;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Frame extends JFrame {

	public Frame() {
		setSize(1000, 600);
		setLocationRelativeTo(null);
		setResizable(false);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setBackground();
		
	}
	
	public void setBackground() {
		JLabel marker = new JLabel();
		marker.setBackground(Color.BLACK);
		marker.setOpaque(true);
		add(marker);
	}
}
