package main.java.com.george.server;
import java.io.* ;
import java.net.* ;
import java.util.* ;

public class HttpRequest {
    final static String CRLF = "\r\n";
    Socket socket;
    private GUI object;
    private static Logger log = Logger.getLogger(HttpRequest.class);
	String uri;
	String version;
	String method;

	List<String> headers = new ArrayList<String>();
     
    public HttpRequest(InputStream is, GUI obj) throws Exception {
        object = obj;
        // Set up input stream
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        // Get the request line of the HTTP request message
        String str = reader.readLine();
        System.out.println("Incoming request" + " " + str);
        object.displaytoTextArea("Incoming request" + " " + str);
        parseInputRequest(str);

        while (!str.equals("")) {
			str = reader.readLine();
			//TODO: Check why an empty header is added at the end of header's list
			parseRequestHeader(str);
		}
    }

	private void parseInputRequest(String str) {
		log.info(str);
		String[] split = str.split("\\s+");
		method = split[0];
		uri = split[1];
		version = split[2];
	}
	
	private void parseRequestHeader(String str) {
		headers.add(str);
	}
}
