package com.nahed.pouyan.main.client;

import com.nahed.pouyan.main.client.Listeners.DataListener;
import com.nahed.pouyan.main.client.UI.ImageView;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class GameHighScore extends JDialog{
	
	private int windowWidth = 450;
	private int windowHeight = 550;
	
	private JLabel[] txtList = new JLabel[10];
	private Client client;
	
	private ImageView img1;
	
	public GameHighScore(Client client){
		this.client = client;
		
		setUndecorated(true);
		setAlwaysOnTop(true);
		getRootPane().setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, new Color(255, 90, 84)));
		setSize(windowWidth, windowHeight);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screen.width/2 - windowWidth/2, screen.height/2 - windowHeight/2);
		getContentPane().setLayout(null);
		getContentPane().setBackground(new Color(60, 60, 60));
		
		img1 = new ImageView();
		img1.setBounds(231, 40, 200, 400);
		img1.setBackground(new Color(60, 60, 60));
		getContentPane().add(img1);
		
		JLabel txtHighScore = new JLabel("High Scores");
		txtHighScore.setFont(new Font("Tahoma", Font.BOLD, 20));
		txtHighScore.setHorizontalAlignment(SwingConstants.CENTER);
		txtHighScore.setBounds(10, 11, 211, 26);
		txtHighScore.setForeground(Color.WHITE);
		getContentPane().add(txtHighScore);
		
		JLabel txt1 = new JLabel("");
		txt1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txt1.setBounds(20, 50, 201, 22);
		txt1.setForeground(Color.WHITE);
		txtList[0] = txt1;
		getContentPane().add(txt1);
		
		JLabel txt2 = new JLabel("");
		txt2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txt2.setForeground(Color.WHITE);
		txt2.setBounds(20, 83, 201, 22);
		txtList[1] = txt2;
		getContentPane().add(txt2);
		
		JLabel txt3 = new JLabel("");
		txt3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txt3.setForeground(Color.WHITE);
		txt3.setBounds(20, 119, 201, 22);
		txtList[2] = txt3;
		getContentPane().add(txt3);
		
		JLabel txt4 = new JLabel("");
		txt4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txt4.setForeground(Color.WHITE);
		txt4.setBounds(21, 152, 201, 22);
		txtList[3] = txt4;
		getContentPane().add(txt4);
		
		JLabel txt5 = new JLabel("");
		txt5.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txt5.setForeground(Color.WHITE);
		txt5.setBounds(20, 185, 201, 22);
		txtList[4] = txt5;
		getContentPane().add(txt5);
		
		JLabel txt6 = new JLabel("");
		txt6.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txt6.setForeground(Color.WHITE);
		txt6.setBounds(20, 219, 201, 22);
		txtList[5] = txt6;
		getContentPane().add(txt6);
		
		JLabel txt7 = new JLabel("");
		txt7.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txt7.setForeground(Color.WHITE);
		txt7.setBounds(20, 252, 201, 22);
		txtList[6] = txt7;
		getContentPane().add(txt7);
		
		JLabel txt8 = new JLabel("");
		txt8.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txt8.setForeground(Color.WHITE);
		txt8.setBounds(20, 285, 201, 22);
		txtList[7] = txt8;
		getContentPane().add(txt8);
		
		JLabel txt9 = new JLabel("");
		txt9.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txt9.setForeground(Color.WHITE);
		txt9.setBounds(20, 318, 201, 22);
		txtList[8] = txt9;
		getContentPane().add(txt9);
		
		JLabel txt10 = new JLabel("");
		txt10.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txt10.setForeground(Color.WHITE);
		txt10.setBounds(20, 351, 201, 22);
		txtList[9] = txt10;
		getContentPane().add(txt10);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dispose();
			}
		});
		btnExit.setBounds(20, 504, 201, 35);
		btnExit.setBackground(new Color(255, 90, 84));
		getContentPane().add(btnExit);
		
		JButton btnRefresh = new JButton("  Refresh");
		btnRefresh.setIcon(new ImageIcon("images\\refresh.png"));
		btnRefresh.setBounds(20, 419, 201, 74);
		btnRefresh.setBackground(new Color(27, 188, 155));
		btnRefresh.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getScores();
			}
		});
		getContentPane().add(btnRefresh);
		
		getScores();
		setVisible(true);
	}
	
	public void getScores(){
		final ArrayList<userData> users = new ArrayList<userData>();
		DataListener dataListener = new DataListener() {
			
			@Override
			public void onScoreReceived(String score) {
				if(!score.equals("")){
					String[] scores = score.split("-");
					for(int i=0; i<=scores.length-1; i++){
						userData user = new userData();
						String[] tmp = scores[i].split(":");
						user.name = tmp[0];
						user.score = Integer.parseInt(tmp[1]);
						users.add(user);
					}

					int index = 0;
					for(userData user : users){
						if(index == 10){		// Top 10
							break;
						}
						txtList[index].setText((index+1)+ ". " + user.name + "        " + user.score);
						index++;
					}
				} else {
					txtList[0].setText("No score has been submitted!");
				}

			}
			
			@Override
			public void onImageReceived(String fileName) {
				img1.setImage(Client.AD_FULL_DIR);
			}
		};
		client.setDataListener(dataListener);
		client.request(null, Client.PROTOCOL_SHOW);
	}
	
	private class userData {
		public String name;
		public int score;
	}
	
}
