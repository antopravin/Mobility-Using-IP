package ip;

import java.io.IOException;
import java.net.ServerSocket;

public class SendingHostServer {

	public static void main(String[] args) {
		try {
			new SendingHostServer().createSendingHostServer();
		} catch (Exception e) {
			System.out.println("IO failure while calling createSendingHostServer() in server : " + e.getMessage());
		}
	}

	private void createSendingHostServer() {
		ServerSocket sendingHostServerSocket = null;
		boolean listeningAtPort = true;
 		try {
 			//System.out.println("homeagent");
 			sendingHostServerSocket = new ServerSocket(AppConstants.PORTNUMBER_SENDINGHOST);
//			serverSocket = new ServerSocket(AppConstants.PORTNUMBER + 1);
			//System.out.println("Listening at port " + AppConstants.PORTNUMBER);
		} catch (IOException e) {
			System.err.println("Can not listen on port: " + AppConstants.PORTNUMBER);
			System.exit(0);
		}
 
		while (listeningAtPort) {
			onClientRequest(sendingHostServerSocket);
		}
 
	}

	private void onClientRequest(ServerSocket sendingHostServerSocket) {
		try {
			//System.out.println("asdf1");
			new SendingHostClientHandler(sendingHostServerSocket.accept());
			//System.out.println("2");
		} catch (IOException e) {
			System.out.println("IO Error while accepting client request in sendingHostServerSocket : " + e.getMessage() );;
		}
		catch(Exception ex) {
			System.out.println("Exception while accepting in sendingHostServerSocket: " + ex.getMessage());
			ex.printStackTrace();
		}	
	}

}
