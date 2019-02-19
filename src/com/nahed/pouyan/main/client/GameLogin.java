package com.nahed.pouyan.main.client;

import com.nahed.pouyan.main.client.Listeners.ConnectionListener;
import com.nahed.pouyan.main.client.UI.Toast;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class GameLogin {
	
	private JPanel contentPane;
	private JTextField edtName;
	private JTextField edtN;
	private JTextField edtIP;
	private JFrame frame;
	
	private final String PATTERN = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";	// Pattern for ip address
	
	public GameLogin(){
		frame = new JFrame();
		frame.setSize(300, 300);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setBackground(new Color(60, 60, 60));
		contentPane.setLayout(null);
		
		JLabel txtName = new JLabel("Your Name: (For HighScore)");
		txtName.setHorizontalAlignment(SwingConstants.CENTER);
		txtName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtName.setBounds(10, 23, 264, 20);
		txtName.setForeground(new Color(255, 255, 255));
		contentPane.add(txtName);
		
		edtName = new JTextField();
		edtName.setHorizontalAlignment(SwingConstants.CENTER);
		edtName.setBounds(10, 54, 264, 20);
		contentPane.add(edtName);
		edtName.setColumns(10);
		
		JLabel txtDifficulty = new JLabel("Enter N: (Difficulty)");
		txtDifficulty.setHorizontalAlignment(SwingConstants.CENTER);
		txtDifficulty.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtDifficulty.setBounds(10, 106, 264, 14);
		txtDifficulty.setForeground(new Color(255, 255, 255));
		contentPane.add(txtDifficulty);
		
		edtN = new JTextField("6");
		edtN.setHorizontalAlignment(SwingConstants.CENTER);
		edtN.setBounds(10, 131, 264, 20);
		contentPane.add(edtN);
		edtN.setColumns(10);
		
		JLabel txtIP = new JLabel("Enter Server IP:");
		txtIP.setHorizontalAlignment(SwingConstants.CENTER);
		txtIP.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtIP.setBounds(10, 183, 264, 14);
		txtIP.setForeground(new Color(255, 255, 255));
		contentPane.add(txtIP);
		
		edtIP = new JTextField("127.0.0.1");
		edtIP.setHorizontalAlignment(SwingConstants.CENTER);
		edtIP.setBounds(10, 208, 264, 20);
		contentPane.add(edtIP);
		edtIP.setColumns(10);
		
		JButton btnStart = new JButton("Start");
		btnStart.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnStart.setBackground(new Color(27, 188, 155));
		btnStart.setForeground(Color.WHITE);
		btnStart.setBounds(10, 260, 125, 23);
		btnStart.setBorder(null);
		btnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				
				final String userName = edtName.getText();					// Checking user's name not to be null
				userName.replace(":", "");
				userName.replace("-", "");
				if(userName == null || userName.length() == 0){
					new Toast("Please enter your name", Toast.LENGHT_SHORT);
					return;
				}
				
				final int n = checkDifficulty();							// Checking n not to be null
				if(n == -1){
					return;
				}
				
				String ip = edtIP.getText();								// Checking ip not be null
				if(ip == null || ip.length() == 0){
					new Toast("Please enter your ip", Toast.LENGHT_SHORT);
					return;
				}
				
				if(!validateIP(ip)){										// Validating ip address
					new Toast("Wrong IP Address", Toast.LENGHT_SHORT);
					return;
				}
				
				final Client client = new Client("127.0.0.1");				// Trying to connect
				client.setConnectionListener(new ConnectionListener() {		// Interface for connect time and failed time
					
					@Override
					public void onConnect(Socket socket) {
						new Toast("Connected to server", Toast.LENGHT_SHORT);
						disposeAnimation(frame);
						new GameBoard(n, userName, client);
					}

					@Override
					public void onFailed() {
						System.out.println("Connection time out...");
						new Toast("Connection time out", Toast.LENGHT_SHORT);
					}
				});
				client.connect();
			}
		});
		contentPane.add(btnStart);
		
		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnExit.setBounds(151, 260, 123, 23);
		btnExit.setBorder(null);
		btnExit.setForeground(Color.WHITE);
		btnExit.setBackground(new Color(255, 90, 84));
		btnExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				frame.dispose();
				System.exit(0);
			}
		});
		contentPane.add(btnExit);
		
		frame.setLocation(400, -350);
		frame.setVisible(true);
		startAnimation(frame);
	}
	
	
	private void disposeAnimation(final JFrame frame){							// Closing windows animation
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final int x = screenSize.width/2 - 150;
		final int finalY = -350;
		Thread animThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				int y = screenSize.height/2 - 150;
				while(y > finalY){
					y -= 5;
					try {
						Thread.sleep(3);
					} catch (InterruptedException e) {}
					frame.setLocation(x, y);
				}
				frame.dispose();
			}
		});
		animThread.start();
	}
	
	private void startAnimation(final JFrame frame){						// Showing dialog animation
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final int x = screenSize.width/2 - 150;
		final int finalY = screenSize.height/2 - 150;
		Thread animThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				int y = -350;
				while(y < finalY){
					y += 5;
					try {
						Thread.sleep(3);
					} catch (InterruptedException e) {}
					frame.setLocation(x, y);
				}
			}
		});
		animThread.start();
	}
	
	private int checkDifficulty(){
		int difficulty = 0;
		try{
			difficulty = Integer.parseInt(edtN.getText());
		} catch (NumberFormatException e) {
			new Toast("Enter a valid number for N", Toast.LENGHT_SHORT);
			edtN.setText("");
			return -1;
		}
		
		if(difficulty != 2 && difficulty != 4 && difficulty != 6 && difficulty != 8 & difficulty != 10){
			new Toast("Enter 2, 4 , 6 , 8 or 10 for N", Toast.LENGHT_SHORT);
			edtN.setText("");
			return -1;
		}
		
		return difficulty;
	}

	private boolean validateIP(String ip){
	      Pattern pattern = Pattern.compile(PATTERN);
	      Matcher matcher = pattern.matcher(ip);
	      return matcher.matches();             		// Return true if it's a valid ip address
	}
	
	public static void main(String[] args) {
		new GameLogin();
	}

}
