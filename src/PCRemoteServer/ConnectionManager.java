package PCRemoteServer;

import java.io.*;
import javax.bluetooth.*;
import javax.microedition.io.*;

public class ConnectionManager {
	private static StreamConnectionNotifier streamConnNotifier;
	private static StreamConnection connection;
	Thread listenThread = null;	
	
	//Accepts connections, then creates a listener thread once connection is successfully created
	public void accept() {	
		UUID uuid = new UUID("976a7076d72811e3bab41a514932ac01", false);
		//UUID uuid = UUID.fromString("976a7076-d728-11e3-bab4-1a514932ac01");
		String connectionString = "btspp://localhost:" + uuid + ";name=PCRemote Server";

		try {
			streamConnNotifier = (StreamConnectionNotifier) Connector.open(connectionString);
		} catch (IOException e) {
			System.out.println("connector.open error: " + e.toString());
		}
		
		//Connect to remote device and display is address to the console
		while (true) {			
			try {
				System.out.println("\n Server running. Awaiting connection...");
				connection = streamConnNotifier.acceptAndOpen();

				RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
				System.out.println("Remote device address: "
						+ dev.getBluetoothAddress());				
			} catch (IOException e) {
				System.out.println("Connection Error:" + e.toString());
				return;
			}			
			
			//Interrupts previously opened InputListener threads, if any
			if(listenThread!= null){
				try{
					listenThread.interrupt();					
				} catch (SecurityException e) {
					System.out.println("Interrupt error: " + e.toString());
				}
			}			
			
			//Creates and runs new InputListener thread with the newly opened connection
			listenThread = new Thread(new InputListener(connection), "listener");
			listenThread.start();					
			System.out.println("thread started");
		}
	}

}