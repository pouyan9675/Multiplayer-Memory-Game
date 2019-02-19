package com.nahed.pouyan.main.client.Listeners;

import java.net.Socket;

public interface ConnectionListener {
	
	public void onConnect(Socket socket);
	public void onFailed();
	
}
