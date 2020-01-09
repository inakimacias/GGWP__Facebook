package remote;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import services.ValidationService;
/**
 * This class process the request of each client as a separated Thread.
 */
public class Router extends Thread {
	
	private DataInputStream in;
	private DataOutputStream out;
	private Socket tcpSocket;
	List<String> goodData = new ArrayList<String>();
	private ValidationService vs = ValidationService.getInstance();
	
	public Router(Socket socket) {
		try {
			this.tcpSocket = socket;
		    this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			this.start();
		} catch (IOException e) {
			System.err.println("# Router - TCPConnection IO error:" + e.getMessage());
		}
	}

	public void run() {
		//Echo server
		try {
			//Read request from the client
			String data = this.in.readUTF();			
			System.out.println(" - Router - Received data from '" + tcpSocket.getInetAddress().getHostAddress() + ":" + tcpSocket.getPort() + "' -> '" + data + "'");		
			
			for(int i = 0; i<data.split(";").length; i++) {
				goodData.add(data.split(";")[i]);
			}
			
			//Route
			switch(goodData.get(0)) {
			case "val":
				System.out.println(" - Router - Routing to Validation Service as nick:"+goodData.get(1)+", password:"+goodData.get(2));
				data = vs.loginAutenticar(goodData.get(1), goodData.get(2));
				break;
			default:
				System.out.println("Error routing");
			}
			
			goodData.clear();
			
			//Send response to the client
			this.out.writeUTF(data);			
			System.out.println(" - Router - Sent data to '" + tcpSocket.getInetAddress().getHostAddress() + ":" + tcpSocket.getPort() + "' -> '" + data + "'");
		} catch (EOFException e) {
			System.err.println("   # Router - TCPConnection EOF error" + e.getMessage());
		} catch (IOException e) {
			System.err.println("   # Router - TCPConnection IO error:" + e.getMessage());
		} finally {
			try {
				tcpSocket.close();
			} catch (IOException e) {
				System.err.println("   # Router - TCPConnection IO error:" + e.getMessage());
			}
		}
		
	}
}