package ip;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SendingHostClientHandler extends Thread {
	private DataInputStream din;
	private DataOutputStream dout;
	private Socket sendingHostClientSocket = null;
	public SendingHostClientHandler(Socket sendingHostClientSocket) {
		try {
			this.sendingHostClientSocket = sendingHostClientSocket;
			din = new DataInputStream(this.sendingHostClientSocket.getInputStream());
			dout = new DataOutputStream(this.sendingHostClientSocket.getOutputStream());
			if(this.sendingHostClientSocket.isConnected())
				writeToSocket("Client is connected,Inet Address : " + sendingHostClientSocket.getRemoteSocketAddress());
			} catch(IOException ioe) {
				System.out.println("IO Exception while executing run() for the child thread in sendingHostClientSocket : " + ioe.getMessage());
			}
			start();
			try {
				this.join();
			} catch (InterruptedException e) {
				System.out.println("Main Thread Interrupted Ex in sendingHostClientSocket ClientHandler " + e.getMessage());
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
	new SendingHostService(sendingHostClientSocket).processIPPacket();
	}
	/**
	 * Closes the socket
	 */
	public void closeSocket() {
		System.out.println("Server is closing socket sendingHostClientSocket for client :" + sendingHostClientSocket.getLocalSocketAddress());
		try {
			din.close();
			dout.close();
			sendingHostClientSocket.close();
		} catch(IOException ioe) {
			System.out.println("IO Exception while closing clientSocket 'sendingHostClientSocket' : " + ioe.getMessage());
		}
	}

}
