package ip;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class SendingHostAgentService extends Thread {
	DataOutputStream dout;
	DataInputStream din;
	int caseValue =0;
	StringBuffer sendPacket = new StringBuffer();
	Scanner scanner = new Scanner(System.in);
	String ipPacket;
	Socket sendingHostClientSocket;
	InputStreamReader streamReader = new InputStreamReader(System.in);
	BufferedReader bufferedReader = new BufferedReader(streamReader);
	public SendingHostAgentService(Socket sendingHostClientSocket) {
		try {
			this.sendingHostClientSocket = sendingHostClientSocket;
			//System.out.println("hi");
			din = new DataInputStream(sendingHostClientSocket.getInputStream());
			dout = new DataOutputStream(sendingHostClientSocket.getOutputStream());
			start();
			try {
				this.join();
			} catch (InterruptedException ie) {
				System.out.println("Main Thread Interrupted Ex in Foreign Agent " + ie.getMessage());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run() {
		boolean exit = false;
		try {
				do {
					System.out.println("----------------------------MENU-----------------------------");
					System.out.println("1.Send an IP packet to end host");
					System.out.println("2.Exit");
					System.out.println("Please enter your choice :");
					caseValue = scanner.nextInt();
					switch(caseValue) {
					case 1:
						System.out.println("Enter data to be sent to End Host with static IP : " + AppConstants.STATIC_IP_ADDRESS);
						String messageBody = "";
						String temp = "";
						while(!(temp = bufferedReader.readLine()).equals("~"))
							messageBody += temp + "\n";
						String  staticIP = AppConstants.STATIC_IP_ADDRESS;
						System.out.println("Sending a IP Packet to End host with static IP : "+ AppConstants.STATIC_IP_ADDRESS +" to Home agent :");
						ipPacket = "1" + staticIP +"\n" + messageBody;
						if (ipPacket != null && ipPacket.length() > 0) {
							//System.out.println("writing to the socket");
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
						exit = true;
						try {
							dout.writeUTF("exit");
							sendingHostClientSocket.close();
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
