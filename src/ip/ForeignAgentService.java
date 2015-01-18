package ip;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ForeignAgentService extends Thread{
	DataOutputStream dout;
	DataInputStream din;
	int caseValue =0;
	StringBuffer sendPacket = new StringBuffer();
	Scanner scanner = new Scanner(System.in);
	String ipPacket;
	Socket foreignAgent;
	/**
	 * Spawns a new thread for this client
	 * @param foreignAgent
	 */
	public ForeignAgentService(Socket foreignAgent){
		try {
			this.foreignAgent = foreignAgent;
			//System.out.println("hi");
			din = new DataInputStream(foreignAgent.getInputStream());
			dout = new DataOutputStream(foreignAgent.getOutputStream());
			start();
			try {
				this.join();
			} catch (InterruptedException e) {
				System.out.println("Main Thread Interrupted Ex in Foreign Agent " + e.getMessage());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	 * Write to socket
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
	 * Close the socket
	 */
	public void closeSocket() {
		System.out.println("Server is closing socket for client:" + foreignAgent.getLocalSocketAddress());
		try {
			din.close();
			dout.close();
			foreignAgent.close();
		} catch(IOException ioe) {
			System.out.println("IO Exception while closing clientSocket : " + ioe.getMessage());
		}
	}
/**
 * Provides the foreign agent functionality b/w Home Agent and Foreign Agent
 */
	public void run() {
		boolean exit = false;
		try {
				do {
					System.out.println("----------------------------MENU-----------------------------");
					System.out.println("1.Forward Registration Details to HomeAgent");
					System.out.println("2.Forward IP PACKET to end host");
					System.out.println("3.Exit");
					System.out.println("Please enter your choice :");
					caseValue = scanner.nextInt();
					switch(caseValue) {
					case 1:
						String ipPacket = PropertyFileApp.readProperty("IPPACKET");
						//System.out.println("IP Packet from prop file :"+ipPacket);
						if(ipPacket != null) {
							//System.out.println("into if condition");
							System.out.println("IP Packet : \n" + ipPacket +"\n");
							//System.out.println("substring :" + (ipPacket.substring(0,1)));
							String type = ipPacket.substring(0,1);
								if (type.equals(AppConstants.MESSAGETYPE_REGISTRATION)) {
//									System.out.println("Message Type : " + AppConstants.REGISTRATION+"\n"); 
//									AppConstants.MESSAGETYPE = AppConstants.REGISTRATION;
//									AppConstants.CARE_OF_ADDRESS = (ipPacket.substring(16, ipPacket.length())).trim();
//									System.out.println("Care of Address : " + AppConstants.CARE_OF_ADDRESS + "\n");
//									System.out.println("Sending regisrtation details to foreign agent");
//									sendPacket.append("REGISTRATION SUCCESSSFUL \n");
//									sendPacket.append("MOBILE HOST DETAILS FROM HOME AGENT \n");
//									sendPacket.append("STATIC IP ADDRESS : " + AppConstants.STATIC_IP_ADDRESS + " \n");
//									sendPacket.append("CARE OF ADDRESS : " + AppConstants.CARE_OF_ADDRESS +"\n");
//									sendPacket.append("From now Packets destined to : "+ AppConstants.STATIC_IP_ADDRESS +" will be forwarded to "+ AppConstants.CARE_OF_ADDRESS +" \n");
//									writeToSocket(sendPacket.toString());
									writeToSocket(ipPacket);
									System.out.println("sent to homeagent");
								}
						}
						else {
							//System.out.println("in else AppConstants.FOREIGNAGENT_TO_HOMEAGENT " +AppConstants.FOREIGNAGENT_TO_HOMEAGENT);
							writeToSocket("Information not received from End Host");
							System.out.println("Information not received from End Host");
						}
					break;
					case 2:
						ipPacket = readFromSocket();
						if(ipPacket.equals("exit")) {
							exit = true;
							closeSocket();
							break;
						}
						System.out.println("IP Packet : \n" + ipPacket +"\n");
						System.out.println("substring :" + (ipPacket.substring(0,1)));
						String type = ipPacket.substring(0,1);
						if (type.equals(AppConstants.MESSAGETYPE_IPTUNNEL)) {
							System.out.println("Message Type : " + AppConstants.IPTUNNEL+"\n"); 
							//AppConstants.SENDINGHOST_TO_HOMEAGENT = ipPacket.toString();
							PropertyFileApp.writeProperty("IPTUNNEL", ipPacket);
							System.out.println("IP Packet transferred successfully End Host" + AppConstants.FOREIGNAGENT_TO_HOMEAGENT);
						} 
					break;
					case 3:
						exit = true;
						try {
							dout.writeUTF("exit");
							foreignAgent.close();
						} catch (IOException e) {
							System.out.println("Error while writing into socket : " +e.getMessage());
						}
					break;
					default:
					System.out.println("Kindly select correct choice");	
					}
			}while(!exit);
		}catch(Exception ex){
			System.out.println("Exception occurred in Foreign Agent" + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
