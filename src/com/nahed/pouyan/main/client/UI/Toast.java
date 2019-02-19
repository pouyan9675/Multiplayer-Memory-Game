package com.nahed.pouyan.main.client.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Toast extends JDialog {
	
	public static final int LENGHT_SHORT = 3000;
	public static final int LENGHT_LONG = 5000;
	int miliseconds;
	
    public Toast(String toastString, int time) {
        this.miliseconds = time;
        setUndecorated(true);
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        getContentPane().add(panel, BorderLayout.CENTER);

        JLabel toastLabel = new JLabel("");
        toastLabel.setText(toastString);
        toastLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        toastLabel.setForeground(Color.WHITE);

        setBounds(100, 100, toastLabel.getPreferredSize().width + 35, 31);


        setAlwaysOnTop(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int y = dim.height/2 - getSize().height/2;
        int half = y/2;
        setLocation(dim.width/2 - getSize().width/2, y + half);
        panel.add(toastLabel);
        setOpacity(0);
        setVisible(true);
        
        final Thread disposeThread = new Thread(){
            public void run() {
                try {
                    Thread.sleep(miliseconds);
                    float opa = 1;
    				while(opa > 0.05f){
    					opa -= 0.05f;
    					setOpacity(opa);
    					try {
    						Thread.sleep(30);
    					} catch (InterruptedException e) {}
    				}
                    dispose();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        
        Thread showThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				float opa = 0;
				while(opa < 0.95f){
					opa += 0.05;
					setOpacity(opa);
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {}
				}
				disposeThread.start();
			}
		});
        showThread.start();
    }
    

}
