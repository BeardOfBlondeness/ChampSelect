package io.github.beardofblondeness.champselect.gui;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Frame extends JFrame {

	public Frame() {
		setSize(960, 540);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(640, 360));
		setVisible(true);
	}
}
