package ip;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
/**
 * Provides Home Agent service to the mobility IP implementation
 * @author antopravin
 *
 */
public class HomeAgentService {
	StringBuffer sendPacket = new StringBuffer();
	boolean exit = false;
	String ipPacket = "";
	private DataInputStream din;
	private DataOutputStream dout;
	private Socket homeAgent = null;
	Scanner scanner = new Scanner(System.in);
	public HomeAgentService(Socket homeAgent) {
		try {
			this.homeAgent = homeAgent;
			din = new DataInputStream(this.homeAgent.getInputStream());
			dout = new DataOutputStream(this.homeAgent.getOutputStream());
		} catch(IOException ioe) {
			System.out.println("IO Exception HomeAgentService child thread : " + ioe.getMessage());
		}		
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
		System.out.println("Server is closing socket for client:" + homeAgent.getLocalSocketAddress());
		try {
			din.close();
			dout.close();
			homeAgent.close();
		} catch(IOException ioe) {
			System.out.println("IO Exception while closing homeAgent : " + ioe.getMessage());
		}
	}
/**
 * Provides the HOME AGENT SERVICE to mobility IP implementation
 */
	public void processIPPacket() {
		do {
			System.out.println("Enter your choice :");
			System.out.println("1.Registration");
			System.out.println("2.IPTunnel(Forward IP Packet to Foreign Agent)");
			int caseValue = scanner.nextInt();
			switch(caseValue) {
			case 1:
				ipPacket = readFromSocket();
				if(ipPacket.equals("exit")) {
					exit = true;
					closeSocket();
					break;
				}
				System.out.println("IP Packet From Foreign Agent : \n" + ipPacket +"\n");
				//System.out.println("substring :" + (ipPacket.substring(0,1)));
				String type = ipPacket.substring(0,1);
				if (type.equals(AppConstants.MESSAGETYPE_REGISTRATION)) {
					System.out.println("Message Type : " + AppConstants.REGISTRATION+"\n"); 
					AppConstants.MESSAGETYPE = AppConstants.REGISTRATION;
					AppConstants.CARE_OF_ADDRESS = (ipPacket.substring(16, ipPacket.length())).trim();
					System.out.println("Care of Address : " + AppConstants.CARE_OF_ADDRESS + "\n");
					System.out.println("Received regisrtation details from foreign agent");
					sendPacket.append("REGISTRATION SUCCESSSFUL \n");
					sendPacket.append("MOBILE HOST DETAILS FROM HOME AGENT \n");
					sendPacket.append("STATIC IP ADDRESS : " + AppConstants.STATIC_IP_ADDRESS + " \n");
					sendPacket.append("CARE OF ADDRESS : " + AppConstants.CARE_OF_ADDRESS +"\n");
					sendPacket.append("From now Packets destined to : "+ AppConstants.STATIC_IP_ADDRESS +" will be forwarded to "+ AppConstants.CARE_OF_ADDRESS +" \n");
					System.out.println(sendPacket);
					writeToSocket(sendPacket.toString());
		
				}
			break;
			case 2:
				//System.out.println("reading from propertyfile");
				String ipFile = PropertyFileApp.readProperty("IPTUNNEL");
				//System.out.println("Details from property file : " + ipFile);
				if ((ipFile.substring(0, 1)).equals(AppConstants.MESSAGETYPE_IPTUNNEL)) {
					if(ipFile != null) {
						//System.out.println("into if condition");
						System.out.println("IP Packet : \n" + ipFile +"\n");
						writeToSocket(ipFile);
					// ipTunnelProcess(ipPacket);
					}
				}	
			break;
			}
			
		}while(!exit);
	}

}
