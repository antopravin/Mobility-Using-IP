package ip;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SendingHostClient {

	public static void main(String[] args) {
		try {
			new SendingHostClient().createClient();
		} catch (Exception e) {
			System.out.println("Error while creating Client: " + e.getMessage());
		}
	}

	private void createClient() {
		Socket sendingHostClientSocket = null;
		InetAddress host = null;
		try{
			host = InetAddress.getLocalHost();
			sendingHostClientSocket = new Socket(host.getHostName(), AppConstants.PORTNUMBER_SENDINGHOST);
//			moblieHostClientSocket = new Socket("cslinux1.utdallas.edu.", HomeAgentServer.PORTNUMBER);
			new SendingHostAgentService(sendingHostClientSocket);
		}catch (UnknownHostException e) {
			System.err.println("Cannot find the host: " + "cslinux1.utdallas.edu.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't read/write from the connection: " + e.getMessage());
			System.exit(1);
		} finally { //Make sure we always clean up
			//sendingHostClientSocket.close();
		}
		
	}
}
