package com.nahed.pouyan.main.server;

import com.nahed.pouyan.main.HelperIO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Server extends Thread{
	
	public static final int PROTOCOL_SAVE = 1;
	public static final int PROTOCOL_SHOW = 2;
	public static final int PROTOCOL_EXIT = 3;

	private int port = 9696;
	private Socket socket;

	private final String SCORES_DIR = "server" + File.separator + "Scores.pnd";
	private final String ADVERTISEMENT_DIR = "server" + File.separator + "ads" + File.separator + "1.jpg";

	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	DataOutputStream fileOutput;
	
	public Server(){
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);
			
			while(true){
				socket = serverSocket.accept();
				System.out.println("User logged in...");
				Server clientConnection = new Server(socket);
				clientConnection.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Server(Socket socket){
		this.socket = socket;
	}
	
	
	@Override
	public void run() {
		super.run();
		
		try {
			inputStream = new DataInputStream(socket.getInputStream());
			outputStream = new DataOutputStream(socket.getOutputStream());
			
			loop:
			while(true){
				int type = inputStream.readInt();
				switch(type){
					case PROTOCOL_SAVE:
						saveRequest();
						break;
					
					case PROTOCOL_SHOW:
						showRequest();
						break;
						
					case PROTOCOL_EXIT:
						HelperIO.closeStream(inputStream);
						HelperIO.closeStream(outputStream);
						HelperIO.closeSocket(socket);
						break loop;
				}
			}
		} catch (IOException e) {
			System.out.println("User disconnected...");
		}
		
	}
	
	private void showRequest() {
		System.out.println("Scores Request");
		try {
			String scores = "";
			File file = new File(SCORES_DIR);
			if(file.exists() && file.length() > 0){
				DataInputStream fileInput = new DataInputStream(new FileInputStream(new File(SCORES_DIR)));
				scores = fileInput.readUTF();
				fileInput.close();
				outputStream.writeUTF(scores);
				outputStream.flush();
			}
			
			////sending scores finished
			int len;
			byte[] buffer = new byte[64 * 1024];

			DataInputStream fileInput = new DataInputStream(new FileInputStream(new File(ADVERTISEMENT_DIR)));
			outputStream.writeLong(new File(ADVERTISEMENT_DIR).length());
			while((len = fileInput.read(buffer)) > 0){
				outputStream.write(buffer, 0, len);
				outputStream.flush();
			}
			fileInput.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	private void saveRequest(){
		System.out.println("Save Request");
		try {
			String msg = inputStream.readUTF();
			
			String scores = "";
			File file = new File(SCORES_DIR);				// Reading last scores
			if(file.exists() && file.length() > 0){
				DataInputStream fileInput = new DataInputStream(new FileInputStream(new File(SCORES_DIR)));
				scores = fileInput.readUTF();
				fileInput.close();
			}
			scores += msg;							// Merge scores with user score
			System.out.println("Split String : " + scores);
			String[] pairs = scores.split("-");								// phrasing scores
			
			ArrayList<userData> scoreList = new ArrayList<userData>();
			for(String pair : pairs){
				String[] tmp = pair.split(":");
				userData user = new userData();
				user.score = Integer.parseInt(tmp[1]);
				user.name = tmp[0];
				scoreList.add(user);
			}
			
			Collections.sort(scoreList, new Comparator<userData>() {		// Sorting
	
				@Override
				public int compare(userData a, userData b) {
					if (a.score >= b.score) {
			            return -1;	//true
			        } else {
			            return 1;	//false
			        }
				}
				
			});
		
			scores = "";
			for(int i=0; i<scoreList.size(); i++){		//Merging sorted scores in a string
				
				userData user = scoreList.get(i);
				String name = user.name;
				int score = user.score;
				
				scores += name + ":" + score + "-";
				
				if(i==10){			// Not to save more than 10 scores
					break;
				}
			}
			
			System.out.println(scores);
			
			fileOutput = new DataOutputStream(new FileOutputStream(new File(SCORES_DIR)));
			fileOutput.writeUTF(scores);
			HelperIO.closeStream(fileOutput);
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private class userData {
		public String name;
		public int score;
	}
	
	public static void main(String[] args) {
		new Server();
	}
	
}
