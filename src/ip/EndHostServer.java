package ip;

import java.io.IOException;
import java.net.*;
import java.io.*;
/**
 * Opens Server socket to communicate with client EndHost(Provides Foreign Agent functionality between Foreign Agent and Mobile End host)
 * @author antopravin
 *
 */
public class EndHostServer {

	public static void main(String[] args) {
		try {
			new EndHostServer().createEndHostServer();
		} catch (Exception e) {
			System.out.println("IO failure while calling createHomeAgentServer() in server : " + e.getMessage());
		}

	}
/**
 * Create Server Socket
 */
	private void createEndHostServer() {
		ServerSocket endHostServerSocket = null;
		boolean listeningAtPort = true;
 		try {
 			//System.out.println("homeagent");
		//	serverSocket = new ServerSocket(AppConstants.PORTNUMBER);
 			endHostServerSocket = new ServerSocket(AppConstants.PORTNUMBER_ENDHOST);
		//	System.out.println("Listening at port " + AppConstants.PORTNUMBER_ENDHOST);
		} catch (IOException e) {
			System.err.println("Can not listen on port: " + AppConstants.PORTNUMBER_ENDHOST);
			System.exit(0);
		}
 		while (listeningAtPort) {
			onClientRequest(endHostServerSocket);
		}
 
		
	}
	 /* Handling client request at the port
	 * @param endHostServerSocket
	 */
	public void onClientRequest(ServerSocket endHostServerSocket) {
		try {
			//System.out.println("asdf1");
			new EndHostClientHandler(endHostServerSocket.accept());
			//System.out.println("2");
		} catch (IOException e) {
			System.out.println("Error while accepting end host client request : " + e.getMessage() );;
		}
		catch(Exception ex) {
			System.out.println("Exception while accepting end host : " + ex.getMessage());
			ex.printStackTrace();
		}
	}

}
