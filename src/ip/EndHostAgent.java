package ip;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Creates a new thread to communicate with the End Host Server
 * @author antopravin
 *
 */
public class EndHostAgent extends Thread {
	DataOutputStream dout;
	DataInputStream din;
	int caseValue =0;
	Scanner scanner = new Scanner(System.in);
	String ipPacket;
	Socket endHostClientSocket;
	
/**
 * Spawns a new thread
 * @param endHostClientSocket
 */
	public EndHostAgent(Socket endHostClientSocket) {
		try {
			this.endHostClientSocket = endHostClientSocket;
			//System.out.println("hi");
			din = new DataInputStream(endHostClientSocket.getInputStream());
			dout = new DataOutputStream(endHostClientSocket.getOutputStream());
			start();
			try {
				this.join();
			} catch (InterruptedException e) {
				System.out.println("Main Thread Interrupted Ex in EndHostAgent " + e.getMessage());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * Provides the functionalities of the end host 
	 * 1)Registration - Its currently in foreign network and so registers the care of address with the home agent via foreign agent.
	 * 2)IPTunnel - Receives the packet destined for the host by a direct IP tunnel b/w Home Agent and Foreign Agent
	 * 3)Exits
	 */
	public void run() {
		boolean exit = false;
		try {
				do {
				//System.out.println("1");
				//String message = din.readUTF();
				//System.out.println("2");
				//System.out.println(message);
				System.out.println("----------------------------MENU-----------------------------");
				System.out.println("1.Registration");
				System.out.println("2.Receive packet from Homeagent");
				System.out.println("3.Exit");
				System.out.println("Please enter your choice :");
				caseValue = scanner.nextInt();
				switch(caseValue) {
				case 1:
					System.out.println("Welcome Mobile Host\nYou are about to leave the home network");
					System.out.println("Please enter the home address :");
					String homeAddress = scanner.next();
					int homeAddressLength = homeAddress.length();
					int homeAddressPadLength = 15 - homeAddressLength;
					for (int i = 0; i < homeAddressPadLength; i++)
						homeAddress += " ";
					System.out.println("Sending the care of address (129.0.0.1) to Home agent :");
					ipPacket = "0" + homeAddress + "129.0.0.1      ";
					System.out.println(ipPacket);
					if (ipPacket != null && ipPacket.length() > 0) {
						System.out.println("writing to the socket");
						try {
							dout.writeUTF(ipPacket);
						} catch (IOException e) {
							System.out.println("Error while writing into socket : " +e.getMessage());
						}
					}
					else{
						System.out.println("Packet is empty");
					}
				break;
				case 2:
					ipPacket = readFromSocket();
					System.out.println("Message from sending host : \n");
					System.out.println(ipPacket);
				break;
				case 3:
					exit = true;
					try {
						writeToSocket("exit");
						endHostClientSocket.close();
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
