package ip;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SendingHostService {
	private DataInputStream din;
	private DataOutputStream dout;
	String ipPacket = "";
	boolean exit = false;
	private Socket sendingHostClientSocket = null;
		public SendingHostService(Socket sendingHostClientSocket) {
		try {
			this.sendingHostClientSocket = sendingHostClientSocket;
			din = new DataInputStream(this.sendingHostClientSocket.getInputStream());
			dout = new DataOutputStream(this.sendingHostClientSocket.getOutputStream());
		} catch(IOException ioe) {
			System.out.println("IO Exception sendingHostClientSocket child thread : " + ioe.getMessage());
		}	
	}

	public void processIPPacket() {
		do {
			ipPacket = readFromSocket();
			if(ipPacket.equals("exit")) {
				exit = true;
				closeSocket();
				break;
			}
			//System.out.println("IP Packet : \n" + ipPacket +"\n");
			//System.out.println("substring :" + (ipPacket.substring(0,1)));
			String type = ipPacket.substring(0,1);
			if (type.equals(AppConstants.MESSAGETYPE_IPTUNNEL)) {
				System.out.println("Message Type : " + AppConstants.IPTUNNEL+"\n"); 
				AppConstants.SENDINGHOST_TO_HOMEAGENT = ipPacket.toString();
				PropertyFileApp.writeProperty("IPTUNNEL", ipPacket);
				System.out.println("IP Packet transferred successfully HomeAgent" + AppConstants.FOREIGNAGENT_TO_HOMEAGENT);
			} 
		}while(!exit);
		
	}
	/**
	 * Read from socket
	 * @return messageRead
	 */
	public String readFromSocket() {
		String messageRead = null;
		try {
			messageRead =  din.readUTF();
		} catch (IOException e) {
			System.out.println("Error while reading from socket : " + e.getMessage());
		}
		return messageRead;
	}
	/**
	 * Write to Socket
	 * @param messageToWrite
	 */
	public void writeToSocket(String messageToWrite) {
		try {
			dout.writeUTF(messageToWrite);
		} catch (IOException e) {
			System.out.println("Error while reading from socket : " + e.getMessage());
		}
	}
	/**
	 * Close the Socket
	 */
	public void closeSocket() {
		System.out.println("Server is closing socket for client:" + sendingHostClientSocket.getLocalSocketAddress());
		try {
			din.close();
			dout.close();
			sendingHostClientSocket.close();
		} catch(IOException ioe) {
			System.out.println("IO Exception while closing homeAgent : " + ioe.getMessage());
		}
	}

}
