package ip;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Creates a new thread once a client joins the socket
 * @author antopravin
 *
 */
public class ClientRequestHandler extends Thread {
	private DataInputStream din;
	private DataOutputStream dout;
	private Socket homeAgentClientSocket = null;
	public ClientRequestHandler() {
		
	}
	/**
	 * Spawns a new thread for the clientSocket
	 * @param clientSocket
	 */
	ClientRequestHandler(Socket homeAgentClientSocket) {
		try {
		this.homeAgentClientSocket = homeAgentClientSocket;
		din = new DataInputStream(this.homeAgentClientSocket.getInputStream());
		dout = new DataOutputStream(this.homeAgentClientSocket.getOutputStream());
		if(this.homeAgentClientSocket.isConnected())
			System.out.println("Client is connected,Inet Address : " + homeAgentClientSocket.getRemoteSocketAddress());
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
	 * Spawns a new thread for the client and transfers control to HomeAgentService
	 */
	public void run() {
	new HomeAgentService(homeAgentClientSocket).processIPPacket();
	}
	/**
	 * Closes the socket
	 */
	public void closeSocket() {
		System.out.println("Server is closing socket for client:" + homeAgentClientSocket.getLocalSocketAddress());
		try {
			din.close();
			dout.close();
			homeAgentClientSocket.close();
		} catch(IOException ioe) {
			System.out.println("IO Exception while closing clientSocket : " + ioe.getMessage());
		}
	}
}
