package main.java.com.george.server;
import java.io.* ;
import java.net.* ;
import java.util.* ;
import java.lang.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.net.ServerSocket;

 
//It extends Thread in order to run the server in a different thread
//than the GUI.
 
public final class WebServer implements Runnable {
    private int port ;
    private GUI display;
    private static final int N_THREADS = 4;
    private static Logger log = Logger.getLogger(WebServer.class);
    protected Thread runningThread = null;
   
    ExecutorService executor;
    
    //  WebServer constructor. Takes two arguments the port number
    //  and a GUI_Server object.   
    public WebServer(int listenPort, GUI disp) {
    	port = listenPort;
    	display = disp;
    	
	}
	
	public void startServer() {
		try {
			ServerSocket s = new ServerSocket(port);
			System.out.println("Web server listening on port " + port);
			toTextArea("Web server listening on port " + port);
			executor = Executors.newFixedThreadPool(N_THREADS);
			while (true) {
				executor.submit(new Handler(s.accept(), display));
			}
		} catch (IOException e) {
			log.error("Error while starting the server", e);
		}
	}
	
	public void shutdownServer() {
		try {
			executor.shutdown();
			if (!executor.awaitTermination(10, TimeUnit.SECONDS)) 
				executor.shutdownNow();
		} catch (InterruptedException e) {
			log.error("Error while shutting down", e);
		}
	}
	
	@Override
	public void run() {
       synchronized(this) {
            this.runningThread = Thread.currentThread();
       }
       startServer();
	}
 
    
	//This method is used to display the messages from workspace to TextArea.
    public void toTextArea(String str) { 
		   display.displaytoTextArea(str);
    }
    

// 
//        // Construct an object to process the HTTP request message.
//        HttpRequest request = new HttpRequest(connectionSocket,disptowindow);
//         
}
