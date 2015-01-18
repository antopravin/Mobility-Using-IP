package ip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * End Host - Mobile Host on which mobility IP is tested
 * @author antopravin
 *
 */
public class EndHost {
	
	public static void main(String[] args) {
		try {
			new EndHost().createEndHostClient();
		} catch (Exception e) {
			System.out.println("Error while creating Client: " + e.getMessage());
		}
	}
/**
 * Creates end host client socket and establishes connection with the endHost Server
 */
	private void createEndHostClient() {
		Socket endHostClientSocket = null;
		InetAddress host = null;
		try{
			host = InetAddress.getLocalHost();
//			moblieHostClientSocket = new Socket(host.getHostName(), HomeAgentServer.PORTNUMBER+1);
		//	endHostClientSocket = new Socket("cslinux2.utdallas.edu.", HomeAgentServer.PORTNUMBER_ENDHOST);
			//System.out.println(host.getHostName());
			endHostClientSocket = new Socket(host.getHostName(), AppConstants.PORTNUMBER_ENDHOST);
			//endHostClientSocket = new Socket("loky-PC", AppConstants.PORTNUMBER_ENDHOST);
			//Control transfered to EndHostAgent
			new EndHostAgent(endHostClientSocket);
		}catch (UnknownHostException e) {
			System.err.println("Cannot find the host: " + "cslinux2.utdallas.edu.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't read/write from the connection: " + e.getMessage());
			System.exit(1);
		} finally { 
			//moblieHostClientSocket.close();
		}
		
	}

}
