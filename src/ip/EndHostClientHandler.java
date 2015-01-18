package ip;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
/**
 * Creates separate thread for each client communicating with the Server
 * @author antopravin
 *
 */
public class EndHostClientHandler extends Thread {
	private DataInputStream din;
	private DataOutputStream dout;
	private Socket endHostClientSocket = null;
	String ipPacket;
	Scanner scanner = new Scanner(System.in);
	/**
	 * Read from Socket
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
		System.out.println("Server is closing socket for client:" + endHostClientSocket.getLocalSocketAddress());
		try {
			din.close();
			dout.close();
			endHostClientSocket.close();
		} catch(IOException ioe) {
			System.out.println("IO Exception while closing clientSocket : " + ioe.getMessage());
		}
	}
	/**
	 * Handling the new client with multi threaading concept
	 * @param endHostClientSocket
	 */
	public EndHostClientHandler(Socket endHostClientSocket) {
		try {
			this.endHostClientSocket = endHostClientSocket;
			din = new DataInputStream(this.endHostClientSocket.getInputStream());
			dout = new DataOutputStream(this.endHostClientSocket.getOutputStream());
			if(this.endHostClientSocket.isConnected())
				System.out.println("Client is connected,Inet Address : " + endHostClientSocket.getRemoteSocketAddress());
			} catch(IOException ioe) {
				System.out.println("IO Exception while executing run() for the child thread : " + ioe.getMessage());
			}
			start();
			try {
				this.join();
			} catch (InterruptedException e) {
				System.out.println("Main Thread Interrupted Ex in ClientHandler " + e.getMessage());
			}
	}
	public void run() {
		/**
		 * New thread is created to communicate with the client
		 */
		new ForeignAgentForwardService(endHostClientSocket).processIPPacket();
	}
}
