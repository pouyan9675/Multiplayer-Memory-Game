package com.nahed.pouyan.main.client;

import com.nahed.pouyan.main.HelperIO;
import com.nahed.pouyan.main.client.Listeners.ConnectionListener;
import com.nahed.pouyan.main.client.Listeners.DataListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
	
	public static final int PROTOCOL_SAVE = 1;
	public static final int PROTOCOL_SHOW = 2;
	public static final int PROTOCOL_EXIT = 3;
	
	private Socket socket;
	private String ip;
	private int port = 9696;

	public static final String AD_FILE_NAME = "tmp.jpg";
	public static final String AD_DIR = "downloads" + File.separator + "ads" + File.separator;
	public static final String AD_FULL_DIR = AD_DIR + AD_FILE_NAME;

	private ConnectionListener connectionListener;
	private DataListener dataListener;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	
	public Client(String value){
		ip = value;
	}
	
	
	public void connect(){
		Thread connectThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					socket = new Socket();
					socket.connect(new InetSocketAddress(ip, port), 2000);
					
					if(connectionListener != null){
						connectionListener.onConnect(socket);
					}
					outputStream = new DataOutputStream(socket.getOutputStream());
					inputStream = new DataInputStream(socket.getInputStream());
				} catch (IOException e) {
					if(connectionListener != null){
						connectionListener.onFailed();
					}
				}
			}
		});
		connectThread.start();
	}
	
	
	public void request(final String message, final int protocol){
		Thread requestThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					outputStream.writeInt(protocol);
					
					String scores;
					switch(protocol){
					case PROTOCOL_SAVE:
						outputStream.writeUTF(message);
						outputStream.flush();
						break;
						
					case PROTOCOL_SHOW:
						scores = inputStream.readUTF();
						dataListener.onScoreReceived(scores);
						getImages();
						break;
						
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if(protocol == PROTOCOL_EXIT){
						HelperIO.closeStream(inputStream);
						HelperIO.closeStream(outputStream);
						HelperIO.closeSocket(socket);
					}
				}
			}

		});
		requestThread.start();
	}
	
	public void getImages(){
		int len;
		byte[] buffer = new byte[64 * 1024];

		try {
			File dir = new File(AD_DIR);
			dir.mkdirs();
			FileOutputStream fileOutput = new FileOutputStream(new File(AD_FULL_DIR));
			long fileSize = inputStream.readLong();
			long downloadedSize = 0;
			while(downloadedSize < fileSize){
				len = inputStream.read(buffer);
				fileOutput.write(buffer, 0, len);
				fileOutput.flush();
				downloadedSize += len;
			}
			fileOutput.close();

			if(dataListener != null){
                dataListener.onImageReceived(AD_FILE_NAME);		//notify first image downloaded
            }
            
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setConnectionListener(ConnectionListener listener){
		connectionListener = listener;
	}
	
	public void setDataListener(DataListener listener){
		dataListener = listener;
	}
	
	public void setPort(int port){
		this.port = port;
	}
	
}
