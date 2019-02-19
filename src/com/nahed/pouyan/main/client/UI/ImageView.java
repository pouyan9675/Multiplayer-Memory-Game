package com.nahed.pouyan.main.client.UI;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImageView extends JPanel {
	
	private Image img;
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, getWidth(), getHeight(), this);		// Rendering images on jpanel
	}
	
	public void getScale(){
		img = img.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);	// Scaling image to jpanel
	}
	
	public void setImage(String dir){
		try {
			img = ImageIO.read(new File(dir));
			getScale();
			repaint();
		} catch (IOException e) {
			System.out.println("IO Exception : ImageView.class");
		}
	}
}
