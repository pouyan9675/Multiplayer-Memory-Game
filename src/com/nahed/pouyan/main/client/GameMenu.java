package com.nahed.pouyan.main.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JToggleButton;

public class GameMenu extends JDialog{
	
	public GameMenu(final Client client){
		
		setSize(250, 160);
		setAlwaysOnTop(true);
		getRootPane().setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, new Color(255, 90, 84)));
		getContentPane().setLayout(null);
		getContentPane().setBackground(new Color(75, 75, 75));
		
		JLabel txtGameSound = new JLabel("Game Sound");
		txtGameSound.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtGameSound.setBounds(10, 71, 101, 14);
		txtGameSound.setForeground(Color.WHITE);
		getContentPane().add(txtGameSound);
		
		final JToggleButton tglSound = new JToggleButton("ON");
		File conf = new File("config.gse");									// Opening audio setting file and configure
		if(conf.exists()){
			try {
				DataInputStream in = new DataInputStream(new FileInputStream(conf));
				boolean sound = in.readBoolean();
				GameBoard.soundOn = sound;
				in.close();
				if(sound){
					tglSound.setSelected(true);
				} else {
					tglSound.setSelected(false);
				}
			} catch (FileNotFoundException e) {} catch (IOException e) {}
		}
		tglSound.setBounds(183, 64, 57, 31);
		tglSound.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent event) {
				DataOutputStream out = null;
				try {
					out = new DataOutputStream(new FileOutputStream("config.gse"));
					if(event.getStateChange() == ItemEvent.SELECTED){
						GameBoard.soundOn = true;
						out.writeBoolean(true);
					} else {
						GameBoard.soundOn = false;
						out.writeBoolean(false);
					}
				} catch (FileNotFoundException e) {} catch (IOException e) {} finally {
					try {
						out.flush();
						out.close();
					} catch (IOException e) {}
				}
				System.out.println(GameBoard.soundOn);
			}
		});
		getContentPane().add(tglSound);
		
		JButton btnExit = new JButton("Exit Game");
		btnExit.setBounds(10, 118, 230, 31);
		btnExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				client.request(null, Client.PROTOCOL_EXIT);
				System.exit(0);
			}
		});
		getContentPane().add(btnExit);
		
		JButton btnBack = new JButton("Back");
		btnBack.setBounds(10, 11, 230, 31);
		getContentPane().add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				dispose();
			}
		});
		getContentPane().add(btnExit);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = screenSize.width;
		int y = screenSize.height;
		
		setLocation(x/2 - 125, y/2 - 80);
		
		setUndecorated(true);
		setVisible(true);
	}
	
}
