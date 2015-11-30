package PCRemoteServer;

import java.awt.*;
import java.io.*;
import static java.awt.event.KeyEvent.*;

import javax.microedition.io.*;

//This class creates a DataInputStream and listens for input
//After receiving input, it will perform the action associated with that input
public class InputListener implements Runnable {
	private static DataInputStream dataStream = null;
	private static StreamConnection connection = null;

	public InputListener(StreamConnection newConnection) {
		connection = newConnection;
	}

	//Starts a DataInputStream and then listens for input
	@Override
	public void run() {	

		try {
			dataStream = new DataInputStream(connection.openInputStream());
			System.out.println("Accepting input...");
		} catch (IOException e) {
			System.out.println("DataInputStream error:" + e.toString());
		}

		listen();
	}
	
	//Listens for input and calls robot class after processing input
	private void listen() {
		int intInput = 0;
		char charInput = '\0';

		while (true) {	
			if(Thread.interrupted()){
				System.out.println("interrupted");
				break;
			}
			
			try {
				if (dataStream != null) {
					while (dataStream.available() > 0) {						
						
						//Check for characters, send to KeyEvent translator
						if(dataStream.available() == 2){
							charInput = dataStream.readChar();							
							System.out.println("input: " + charInput);
							sendVK(charInput);							
						}
						
						//Check for integers, will be either mouse movement or click commands
						else if(dataStream.available() == 4){
							intInput = dataStream.readInt();
							System.out.println("input:" + intInput);									

							try {						
								//  Mouse Movement
								//  1111=MouseUp 2222=MouseRight 3333=MouseDown 4444=MouseLeft
								//  5555=MouseNW 6666=MouseNE 7777=MouseSW 8888=MouseSE
								if (intInput == 1111 || intInput == 2222 || intInput == 3333 || intInput == 4444 
										|| intInput == 5555 || intInput == 6666 || intInput == 7777 || intInput == 8888) {
									startRobot(3, intInput);
									
								// 1024=Left CLick  4096=Right Click
								} else if (intInput == 1024 || intInput == 4096) {
									startRobot(2, intInput);
								}							
							} catch (AWTException a) {
								System.out.println(a.getStackTrace());
								return;
							}
						}						
					}
				}
			} catch (IOException e) {
				System.out.println("DataStream error: " + e.toString());
				return;
			}			
		}
	}

	// This class will execute keyboard or mouse actions depending on the
	// parameters it receives
	// This class will take two parameters, the first an int to differentiate
	// between mouse or keyboard input, the second int being the constant field
	// value associated with an action
	// type 0 = lowercase/symbol keyboard input
	// type 1 = uppercase keyboard input
	// type 2 = mouse button
	// type 3 = mouse movement
	public static void startRobot(int type, int action) throws AWTException {
		try {			
			Robot r = new Robot();			
			
			switch (type){
				
				//Types keys
				case 0:
					r.keyPress(action);
					r.keyRelease(action);		
					break;
				
				//Types keys that would require the use of the shift key
				case 1: 
					r.keyPress(VK_SHIFT);
					r.keyPress(action);
					r.keyRelease(action);
					r.keyRelease(VK_SHIFT);
					break;

				//Clicks the mouse
				case 2:
					r.mousePress(action);
					r.mouseRelease(action);
					break;
					
				//Moves the mouse
				case 3:
					Point pos = mousePos();
					switch (action) {
						case 1111: // Mouse North
							r.mouseMove((int) pos.getX(), (int) pos.getY() - 10);
							break;
						case 2222: // Mouse East
							r.mouseMove((int) pos.getX() + 10, (int) pos.getY());
							break;
						case 3333: // Mouse South
							r.mouseMove((int) pos.getX(), (int) pos.getY() + 10);
							break;
						case 4444: // Mouse West
							r.mouseMove((int) pos.getX() - 10, (int) pos.getY());
							break;
						case 5555: // Mouse NW
							r.mouseMove((int) pos.getX() - 10, (int) pos.getY() - 10);
							break;
						case 6666: // Mouse NE
							r.mouseMove((int) pos.getX() + 10, (int) pos.getY() - 10);
							break;
						case 7777: // Mouse SW
							r.mouseMove((int) pos.getX() + 10, (int) pos.getY() + 10);
							break;
						case 8888: // Mouse SE
							r.mouseMove((int) pos.getX() - 10, (int) pos.getY() + 10);
							break;
					}
				break;
			}
		} catch (AWTException e) {
			System.out.println("Robot error: " + e.toString());
		}
	}

	// Returns mouse position, used to move cursor
	public static Point mousePos() {
		PointerInfo pI = MouseInfo.getPointerInfo();
		Point pos = pI.getLocation();
		return pos;
	}	
	
	//Converts character into an acceptable KeyEvent constant field variable
	//Might cause problems on other systems due to system-specific key codes
	public void sendVK(char c){	
		try{
		switch (c) {
	        case 'a': startRobot(0, VK_A); break;
	        case 'b': startRobot(0, VK_B); break;
	        case 'c': startRobot(0, VK_C); break;
	        case 'd': startRobot(0, VK_D); break;
	        case 'e': startRobot(0, VK_E); break;
	        case 'f': startRobot(0, VK_F); break;
	        case 'g': startRobot(0, VK_G); break;
	        case 'h': startRobot(0, VK_H); break;
	        case 'i': startRobot(0, VK_I); break;
	        case 'j': startRobot(0, VK_J); break;
	        case 'k': startRobot(0, VK_K); break;
	        case 'l': startRobot(0, VK_L); break;
	        case 'm': startRobot(0, VK_M); break;
	        case 'n': startRobot(0, VK_N); break;
	        case 'o': startRobot(0, VK_O); break;
	        case 'p': startRobot(0, VK_P); break;
	        case 'q': startRobot(0, VK_Q); break;
	        case 'r': startRobot(0, VK_R); break;
	        case 's': startRobot(0, VK_S); break;
	        case 't': startRobot(0, VK_T); break;
	        case 'u': startRobot(0, VK_U); break;
	        case 'v': startRobot(0, VK_V); break;
	        case 'w': startRobot(0, VK_W); break;
	        case 'x': startRobot(0, VK_X); break;
	        case 'y': startRobot(0, VK_Y); break;
	        case 'z': startRobot(0, VK_Z); break;
	        case 'A': startRobot(1, VK_A); break;
	        case 'B': startRobot(1, VK_B); break;
	        case 'C': startRobot(1, VK_C); break;
	        case 'D': startRobot(1, VK_D); break;
	        case 'E': startRobot(1, VK_E); break;
	        case 'F': startRobot(1, VK_F); break;
	        case 'G': startRobot(1, VK_G); break;
	        case 'H': startRobot(1, VK_H); break;
	        case 'I': startRobot(1, VK_I); break;
	        case 'J': startRobot(1, VK_J); break;
	        case 'K': startRobot(1, VK_K); break;
	        case 'L': startRobot(1, VK_L); break;
	        case 'M': startRobot(1, VK_M); break;
	        case 'N': startRobot(1, VK_N); break;
	        case 'O': startRobot(1, VK_O); break;
	        case 'P': startRobot(1, VK_P); break;
	        case 'Q': startRobot(1, VK_Q); break;
	        case 'R': startRobot(1, VK_R); break;
	        case 'S': startRobot(1, VK_S); break;
	        case 'T': startRobot(1, VK_T); break;
	        case 'U': startRobot(1, VK_U); break;
	        case 'V': startRobot(1, VK_V); break;
	        case 'W': startRobot(1, VK_W); break;
	        case 'X': startRobot(1, VK_X); break;
	        case 'Y': startRobot(1, VK_Y); break;
	        case 'Z': startRobot(1, VK_Z); break;
	        case '`': startRobot(0, VK_BACK_QUOTE); break;
	        case '0': startRobot(0, VK_0); break;
	        case '1': startRobot(0, VK_1); break;
	        case '2': startRobot(0, VK_2); break;
	        case '3': startRobot(0, VK_3); break;
	        case '4': startRobot(0, VK_4); break;
	        case '5': startRobot(0, VK_5); break;
	        case '6': startRobot(0, VK_6); break;
	        case '7': startRobot(0, VK_7); break;
	        case '8': startRobot(0, VK_8); break;
	        case '9': startRobot(0, VK_9); break;
	        case '-': startRobot(0, VK_MINUS); break;
	        case '=': startRobot(0, VK_EQUALS); break;
	        case '~': startRobot(1, VK_BACK_QUOTE); break;
	        case '!': startRobot(1, VK_1); break;
	        case '@': startRobot(1, VK_2); break;
	        case '#': startRobot(1, VK_3); break;
	        case '$': startRobot(1, VK_4); break;
	        case '%': startRobot(1, VK_5); break;
	        case '^': startRobot(1, VK_6); break;
	        case '&': startRobot(1, VK_7); break;
	        case '*': startRobot(1, VK_8); break;
	        case '(': startRobot(1, VK_9); break;
	        case ')': startRobot(1, VK_0); break;	        
	        case '_': startRobot(1, VK_MINUS); break;
	        case '+': startRobot(1, VK_EQUALS); break;
	        case '\t': startRobot(0, VK_TAB); break;
	        case '\n': startRobot(0, VK_ENTER); break;
	        case '[': startRobot(0, VK_OPEN_BRACKET); break;
	        case ']': startRobot(0, VK_CLOSE_BRACKET); break;
	        case '\\': startRobot(0, VK_BACK_SLASH); break;
	        case '{': startRobot(1, VK_OPEN_BRACKET); break;
	        case '}': startRobot(1, VK_CLOSE_BRACKET); break;
	        case '|': startRobot(1, VK_BACK_SLASH); break;
	        case ';': startRobot(0, VK_SEMICOLON); break;
	        case ':': startRobot(1, VK_SEMICOLON); break;
	        case '\'': startRobot(0, VK_QUOTE); break;
	        case '"': startRobot(0, VK_QUOTEDBL); break;
	        case ',': startRobot(0, VK_COMMA); break;
	        case '<': startRobot(0, VK_LESS); break;
	        case '.': startRobot(0, VK_PERIOD); break;
	        case '>': startRobot(0, VK_GREATER); break;
	        case '/': startRobot(0, VK_SLASH); break;
	        case '?': startRobot(1, VK_SLASH); break;
	        case ' ': startRobot(0, VK_SPACE); break;
	        default:
	            throw new IllegalArgumentException("Cannot type character " + c);
	        }	    
		}catch (AWTException e) {
			System.out.println("Key error on key " + c + " error: " + e.toString());
		}
	}
}
