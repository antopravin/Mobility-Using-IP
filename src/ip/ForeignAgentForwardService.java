package ip;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
/**
 * (Provides Foreign Agent functionality between Foreign Agent and Mobile End host)
 * Forwards Care of Address to Home Agent
 * @author antopravin
 *
 */
public class ForeignAgentForwardService{
	StringBuffer sendPacket = new StringBuffer();
	boolean exit = false;
	String ipPacket = "";
	private DataInputStream din;
	private DataOutputStream dout;
	private Socket endHostClientSocket = null;
	Scanner scanner = new Scanner(System.in);
	/**
	 * Socket reference variable control transfer
	 * @param endHostClientSocket
	 */
	public ForeignAgentForwardService(Socket endHostClientSocket) {
		try {
			this.endHostClientSocket = endHostClientSocket;
			din = new DataInputStream(this.endHostClientSocket.getInputStream());
			dout = new DataOutputStream(this.endHostClientSocket.getOutputStream());
		} catch(IOException ioe) {
			System.out.println("IO Exception ForeignAgentForwardService() : " + ioe.getMessage());
		}
	}
/**
 * Provides the necessary service to the End host Server
 * MESSAGE_TYPE = Registration - Forwards the HomeAddress to Foreign Agent 
 */
	public void processIPPacket() {
		do {
			System.out.println("Enter your choice :");
			System.out.println("1.Registration");
			System.out.println("2.IPTunnel");
			int caseValue = scanner.nextInt();
			switch(caseValue) {
			case 1:
				ipPacket = readFromSocket();
				if(ipPacket.equals("exit")) {
					//Closes the connection when client sends the string 'exit'
					exit = true;
					closeSocket();
					break;
				}
				System.out.println("IP Packet : \n" + ipPacket +"\n");
				//System.out.println("substring :" + (ipPacket.substring(0,1)));
				String type = ipPacket.substring(0,1);
				if (type.equals(AppConstants.MESSAGETYPE_REGISTRATION)) {
					System.out.println("Message Type : " + AppConstants.REGISTRATION+"\n"); 
//					AppConstants.MESSAGETYPE = AppConstants.REGISTRATION;
//					AppConstants.CARE_OF_ADDRESS = (ipPacket.substring(16, ipPacket.length())).trim();
//					System.out.println("Care of Address : " + AppConstants.CARE_OF_ADDRESS + "\n");
//					System.out.println("Sending regisrtation details to Home agent");
//					sendPacket.append("REGISTRATION SUCCESSSFUL \n");
//					sendPacket.append("MOBILE HOST DETAILS FROM HOME AGENT \n");
//					sendPacket.append("STATIC IP ADDRESS : " + AppConstants.STATIC_IP_ADDRESS + " \n");
//					sendPacket.append("CARE OF ADDRESS : " + AppConstants.CARE_OF_ADDRESS +"\n");
//					sendPacket.append("From now Packets destined to : "+ AppConstants.STATIC_IP_ADDRESS +" will be forwarded to "+ AppConstants.CARE_OF_ADDRESS +" \n");
					AppConstants.FOREIGNAGENT_TO_HOMEAGENT = ipPacket.toString();
					PropertyFileApp.writeProperty("IPPACKET", AppConstants.FOREIGNAGENT_TO_HOMEAGENT);
					System.out.println("Care Of Address Forwarded to HomeAgent" + AppConstants.FOREIGNAGENT_TO_HOMEAGENT);
		
				} 
			break;
			case 2:
				 	AppConstants.MESSAGETYPE = AppConstants.IPTUNNEL;
					System.out.println("Message Type : " + AppConstants.REGISTRATION);
					// ipTunnelProcess(ipPacket);
				//	System.out.println("reading from propertyfile");
					String ipFile = PropertyFileApp.readProperty("IPTUNNEL");
					//System.out.println("Details from property file : " + ipFile);
						if ((ipFile.substring(0, 1)).equals(AppConstants.MESSAGETYPE_IPTUNNEL)) {
							if(ipFile != null) {
								//System.out.println("into if condition");
								//System.out.println("IP Packet : \n" + ipPacket +"\n");
								writeToSocket(ipFile);
							// ipTunnelProcess(ipPacket);
						}
					}	
			break;
			}
			
		}while(!exit);
	}
	/**
	 * Reads from socket
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
	 * Writes to Socket
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
	 * Closes the socket
	 */
	public void closeSocket() {
		System.out.println("Server is closing socket for client:" + endHostClientSocket.getLocalSocketAddress());
		try {
			din.close();
			dout.close();
			endHostClientSocket.close();
		} catch(IOException ioe) {
			System.out.println("IO Exception while closing clientSocket : " + ioe.getMessage());
		}
	}


}
