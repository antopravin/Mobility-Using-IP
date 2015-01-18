package ip;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
/**
 * Foreign Agent Communicating with Home Agent
 * @author antopravin
 *
 */
public class ForeignAgentClient {
	public static void main(String[] args) {
		try {
			new ForeignAgentClient().createClient();
		} catch (Exception e) {
			System.out.println("Error while creating Client: " + e.getMessage());
		}

	}
/**
 * Creates the client socket to connect with the Server Socket
 * @throws IOException
 */
	private void createClient() throws IOException {
		Socket moblieHostClientSocket = null;
		InetAddress host = null;
		try{
			host = InetAddress.getLocalHost();
			moblieHostClientSocket = new Socket(host.getHostName(), AppConstants.PORTNUMBER);
			//moblieHostClientSocket = new Socket("idea-PC", AppConstants.PORTNUMBER);
//			moblieHostClientSocket = new Socket("cslinux1.utdallas.edu.", HomeAgentServer.PORTNUMBER);
			new ForeignAgentService(moblieHostClientSocket);
		}catch (UnknownHostException e) {
			System.err.println("Cannot find the host: " + "cslinux1.utdallas.edu.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't read/write from the connection: " + e.getMessage());
			System.exit(1);
		} finally { //Make sure we always clean up
			//moblieHostClientSocket.close();
		}
		
	}

}
