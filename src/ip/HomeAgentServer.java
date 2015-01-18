package ip;

import java.io.IOException;
import java.net.ServerSocket;
/**
 * Home Agent in Mobility IP Implementation
 * @author antopravin
 *
 */
public class HomeAgentServer {
	public static void main(String[] args) {
		try {
			new HomeAgentServer().createHomeAgentServer();
		} catch (Exception e) {
			System.out.println("IO failure while calling createHomeAgentServer() in server : " + e.getMessage());
		}
	}
/**
 * Creating socket and listening at port no 12000
 */
	public void createHomeAgentServer() throws Exception {
		ServerSocket homeAgentServerSocket = null;
		boolean listeningAtPort = true;
 		try {
 		//	System.out.println("homeagent");
 			homeAgentServerSocket = new ServerSocket(AppConstants.PORTNUMBER);
//			serverSocket = new ServerSocket(AppConstants.PORTNUMBER + 1);
		//	System.out.println("Listening at port " + AppConstants.PORTNUMBER);
		} catch (IOException e) {
			System.err.println("Can not listen on port: " + AppConstants.PORTNUMBER);
			System.exit(0);
		}
 
		while (listeningAtPort) {
			onClientRequest(homeAgentServerSocket);
		}
 
		//serverSocket.close();
	}
	/**
	 * Handling client request at the port and transferring control to ClientRequestHandler
	 * @param serverSocket
	 */
	public void onClientRequest(ServerSocket homeAgentServerSocket) {
		try {
			//System.out.println("asdf1");
			new ClientRequestHandler(homeAgentServerSocket.accept());
			//System.out.println("2");
		} catch (IOException e) {
			System.out.println("IO Error while accepting client request in homeAgentServerSocket : " + e.getMessage() );;
		}
		catch(Exception ex) {
			System.out.println("Exception while accepting in homeAgentServerSocket: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

}
