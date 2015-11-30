package PCRemoteServer;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.bluetooth.*;

public class PCRemoteServer{
	private static String address = "";
	
	//Creates tray icon for the app
	public static void createTrayIcon(){
		if(!SystemTray.isSupported()){
			System.out.println("System Tray not supported.");
			return;
		}
		final PopupMenu pop = new PopupMenu();
		URL url = System.class.getResource("/images/ic_launcher.png");
		Image img = Toolkit.getDefaultToolkit().getImage(url);		
		final TrayIcon trayIcon = new TrayIcon(img, "PCR Server");
		trayIcon.setImageAutoSize(true);
		final SystemTray tray = SystemTray.getSystemTray();
		
		MenuItem title = new MenuItem("PCRemote Server");
		MenuItem mac = new MenuItem("Mac Address:");
		MenuItem addr = new MenuItem(address);
		MenuItem close = new MenuItem("Close Server");
		
		pop.add(title);
		pop.add(mac);
		pop.add(addr);
		pop.add(close);
		
		trayIcon.setPopupMenu(pop);
		
		try{
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon couldn't be added");
			return;
		}
		
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tray.remove(trayIcon);
				System.exit(0);
			}
		});
	}	
	
	public static void main(String[] args) throws IOException {
		//Prints local computer info to console, and obtains MAC address to display in System Tray menu
		try{			
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			System.out.println("Address: " + localDevice.getBluetoothAddress());
			String rawAddress = localDevice.getBluetoothAddress();
			for(int i =0; i<rawAddress.length(); i++){
				if(i%2==0 && i!=0)
					address+=':';
				address+=rawAddress.charAt(i);
			}
			System.out.println("Name: " + localDevice.getFriendlyName());
		} catch (IOException e) {
			System.out.println("Local Device error: " + e.toString());
		}			
		
		
		ConnectionManager cm = new ConnectionManager();		
		
		//Creates system tray icon
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createTrayIcon();
            }
        });		
		
		//Starts the ConnectionManager to listen for incoming connections
		cm.accept();
		
	}
}