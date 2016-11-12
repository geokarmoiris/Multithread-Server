package main.java.com.george.server;
import java.net.Socket;

public class Handler implements Runnable {

	private static Logger log = Logger.getLogger(Handler.class);

	private Socket socket;
	private GUI display;

	public Handler(Socket socket, GUI obj) {
		this.socket = socket;
		display = obj;
	}
	public void run() {
		try {
			HttpRequest req = new HttpRequest(socket.getInputStream(), display);
			HttpResponse res = new HttpResponse(req);
			socket.getOutputStream().write(res.responseBody);
			//check respective comment on HttpResponse class
			//res.write(socket.getOutputStream());
			socket.close();
		} catch (Exception e) {
			log.error("Runtime Error", e);
		}
		
	}
}
