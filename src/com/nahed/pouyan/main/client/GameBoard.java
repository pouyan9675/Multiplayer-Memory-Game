package com.nahed.pouyan.main.client;

import com.nahed.pouyan.main.HelperIO;
import com.nahed.pouyan.main.client.UI.Toast;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;

public class GameBoard extends JFrame {

	private int windowHeight = 400;
	private int windowWidth = 400;
	private final int n;
	
	private boolean firstChoose = true;
	private boolean mustWait;
	private JButton[] pressedBtns = new JButton[2];
	
	private ActionListener clickListener;
	private int userScore = 0;
	private String userName;
	private short winTimes = 0;
	private Client client;
	
	ArrayList<Integer> nums = new ArrayList<Integer>();
	
	private HashMap<JButton, Integer> btnList = new HashMap<JButton, Integer>();
	
	private JLabel txtScore;
	private JPanel panel;
	
	public static boolean soundOn = true;
	private final int SOUND_DING = 0;
	private final int SOUND_CLAP = 1;
	private final int SOUND_KNOCK = 2;
	
	public GameBoard(int table, String userName, final Client client){
		n = table;
		this.client = client;
		this.userName = userName;
		soundSetting();
		setNumbers();
		
		setTitle("Game Board");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		switch(n){
		case 2:
			windowHeight = windowWidth = 300;
			break;
			
		case 4:
			windowHeight = windowWidth = 400;
			break;
			
		case 10:
			windowHeight = windowWidth = (n-1) * 7 + n * 75;
			break;
			
		default:
			windowHeight = windowWidth = (n-1) * 8 + n * 80;
		}
		int x = screenSize.width/2 - windowWidth/2;
		int y = screenSize.height/2 - windowHeight/2;
		setBounds(x, y, windowWidth, windowHeight);
		setResizable(false);
		setUndecorated(true);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Border marginBorder = BorderFactory.createEmptyBorder(15, 10, 15, 10);
		Border matteBoder = BorderFactory.createMatteBorder(2, 2, 0, 2, Color.WHITE);
		
		GridLayout bgLayout = new GridLayout(n, n, 10, 10);
		panel = new JPanel();
		panel.setLayout(bgLayout);
		panel.setBorder(BorderFactory.createCompoundBorder(matteBoder, BorderFactory.createEmptyBorder(5, 10, 10, 10)));
		panel.setBackground(new Color(60, 60, 60));
		
		///////////// Score Board //////////
		
		JPanel header = new JPanel();
		header.setLayout(new GridLayout(1,3,10,0));
		header.setBorder(BorderFactory.createCompoundBorder(matteBoder, marginBorder));
		header.setBackground(new Color(60, 60, 60));
		
		JButton btnNew = new JButton("New Game");
		btnNew.setBackground(new Color(255, 90, 84));
		btnNew.setForeground(Color.WHITE);
		btnNew.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				newGame();
			}
		});
		btnNew.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.WHITE));
		
		JButton btnHighScore = new JButton("High Score");
		btnHighScore.setBackground(new Color(255, 90, 84));
		btnHighScore.setForeground(Color.WHITE);
		btnHighScore.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new GameHighScore(client);
			}
		});
		btnHighScore.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.WHITE));
		
		switch(n){			// setting font consider to board size
		case 4:
			btnNew.setFont(new Font("Arial", Font.PLAIN, 11));
			btnHighScore.setFont(new Font("Arial", Font.PLAIN, 11));
			break;
			
		case 6:
			btnNew.setFont(new Font("Arial", Font.PLAIN, 14));
			btnHighScore.setFont(new Font("Arial", Font.PLAIN, 14));
			break;
			
		case 8:
			btnNew.setFont(new Font("Arial", Font.PLAIN, 16));
			btnHighScore.setFont(new Font("Arial", Font.PLAIN, 16));
			break;
			
		}
		
		
		
		txtScore = new JLabel("Score : 0", SwingConstants.CENTER);
		txtScore.setForeground(Color.WHITE);
		txtScore.setBorder(BorderFactory.createDashedBorder(Color.WHITE));
		
		header.add(txtScore);
		header.add(btnNew);
		header.add(btnHighScore);
		
		JButton btnMenu = new JButton("Menu");
		btnMenu.setForeground(Color.WHITE);
		btnMenu.setBackground(new Color(255, 90, 84));
		btnMenu.setBorder(null);
		btnMenu.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.WHITE));
		btnMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				new GameMenu(client);
			}
		});
		
		///////////////////////////////////
		
		add(panel, BorderLayout.CENTER);
		add(header, BorderLayout.NORTH);
		add(btnMenu, BorderLayout.SOUTH);
		
		clickListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				JButton btn = (JButton) event.getSource();
				int value = btnList.get(btn);
				
				if(mustWait || btn.equals(pressedBtns[0])){		// blocking clicking and clicking on a button twice
					return;
				}
				
																				// Buttons click listener
				btn.setIcon(new ImageIcon("images\\" + value + ".png"));
				if(firstChoose){
					pressedBtns[0] = btn;
				} else {
					mustWait = true;
					pressedBtns[1] = btn;
					
					if(btnList.get(pressedBtns[0]).equals(btnList.get(pressedBtns[1]))){	// Buttons values are equal
						winAction();
						mustWait = false;
					} else {
						Timer cleanTimer = new Timer(1000, new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent event) {
								pressedBtns[0].setIcon(null);		//return buttons icons
								pressedBtns[1].setIcon(null);
								
								userScore--;
								txtScore.setText("Score : " + userScore);

								mustWait = false;
							}
						});
						cleanTimer.setRepeats(false);
						playSound(SOUND_KNOCK);
						cleanTimer.start();
					}
					
				}
				firstChoose = !firstChoose;
			}
		};
		
		for(int i=0; i<n*n; i++){								// Making and Adding buttons
			JButton btn = new JButton();
			btn.setBackground(new Color(0, 199, 156));
			btn.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE));
			panel.add(btn);
			btnList.put(btn, nums.get(i));
		}
		
		setVisible(true);
		
		previewValues();
	}
	
	
	int counter;
	public void previewValues(){
		counter = 1;
		final Set<JButton> keys = btnList.keySet();
		
		for(JButton btn : keys){					// Preview icons
			int value = btnList.get(btn);
			btn.setIcon(new ImageIcon("images\\" + value + ".png"));
		}
		
		Timer show = new Timer(5000, new ActionListener() {		// Sleep UI thread for 5 secs and then will perform actionPerformed
			
			@Override
			public void actionPerformed(ActionEvent event) {
				
				for(JButton btn : keys){			// Hiding icons
					btn.setIcon(null);
					btn.addActionListener(clickListener);
				}
				
			}
		});
		show.setRepeats(false);
		show.start();
	}
	
	
	public void setNumbers(){						// Initialize numbers random
		int[] numbers = new int[n*n];
		
		for(int i=0; i<n*n/2; i++){
			numbers[i] = i+1;
			numbers[n*n - i - 1] = i+1;
		}
		
		for(int i=0; i<numbers.length; i++){
			nums.add(numbers[i]);
		}
		
		Collections.shuffle(nums);
	}
	
	public void newGame(){
		userScore = 0;
		firstChoose = true;
		winTimes = 0;
		btnList.clear();
		nums.clear();
		txtScore.setText("Score : 0");
		mustWait = false;
		
		panel.removeAll();	// Clearing buttons panel
		
		setNumbers();
		
		for(int i=0; i<n*n; i++){
			JButton btn = new JButton();
			btn.setBackground(new Color(0, 199, 156));
			btn.addActionListener(clickListener);
			btn.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE));
			panel.add(btn);
			btnList.put(btn, nums.get(i));
		}
		
		previewValues();
	}
	
	private void soundSetting(){
		File config = new File("config.gse");	//game sound configuration file
		if(config.exists()){
			DataInputStream in = null;
			try {
				in = new DataInputStream(new FileInputStream(config));
				soundOn = in.readBoolean();
			} catch (FileNotFoundException e) {} catch (IOException e) {} finally {
				HelperIO.closeStream(in);
			}
		} else {								// if not exists creating file and writing true on it
			DataOutputStream out = null;
			try {
				out = new DataOutputStream(new FileOutputStream(config));
				out.writeBoolean(true);
				out.flush();
			} catch (FileNotFoundException e) {} catch (IOException e) {} finally {
				HelperIO.closeStream(out);
			}
		}
	}
	
	private void playSound(int type){
		
		if(!soundOn){
			return;
		}
		
		try {
			
			switch(type){					// Creating and playing sounds
				case SOUND_DING:
					AudioInputStream dingStream = AudioSystem.getAudioInputStream(new File("sounds\\ding.wav"));
					Clip dingClip = AudioSystem.getClip();
					dingClip.open(dingStream);
					dingClip.start();
					break;
					
				case SOUND_CLAP:
					AudioInputStream clapStream = AudioSystem.getAudioInputStream(new File("sounds\\clap.wav"));
					Clip clapClip = AudioSystem.getClip();
					clapClip.open(clapStream);
					clapClip.start();
					break;
					
				case SOUND_KNOCK:
					AudioInputStream knockStream = AudioSystem.getAudioInputStream(new File("sounds\\knock.wav"));
					Clip knockClip = AudioSystem.getClip();
					knockClip.open(knockStream);
					knockClip.start();
					break;
			}
			
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}
	
	public void winAction(){
		pressedBtns[0].setVisible(false);					// Hiding pairs
		pressedBtns[1].setVisible(false);
		
		userScore += n;
		txtScore.setText("Score : " + userScore);			// Updating user's score
		
		if(++winTimes == n*n/2){
			client.request(userName + ":" + userScore, Client.PROTOCOL_SAVE);		// send score and save it on server
			new Toast("You Won, Your score will be submit.", Toast.LENGHT_LONG);
			playSound(SOUND_CLAP);
			return;
		}
		playSound(SOUND_DING);
	}
	
}
