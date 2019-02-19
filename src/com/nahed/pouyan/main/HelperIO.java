package com.nahed.pouyan.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HelperIO {
	
	public static void closeStream(InputStream stream){
		try {
			if(stream != null){
				stream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeStream(OutputStream stream){
		try {
			if(stream != null){
				stream.flush();
				stream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeStream(DataInputStream stream){
		try {
			if(stream != null){
				stream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeStream(DataOutputStream stream){
		try {
			if(stream != null){
				stream.flush();
				stream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeSocket(Socket socket){
		try {
			if(socket != null){
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
