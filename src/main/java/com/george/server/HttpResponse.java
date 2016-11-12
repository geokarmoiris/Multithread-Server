package main.java.com.george.server;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HttpResponse {
	private static Logger log = Logger.getLogger(HttpResponse.class);
	
	public static final String VERSION = "HTTP/1.1";

	List<String> headers = new ArrayList<String>();
	private String status;
	
	FileInputStream fis = null;
	byte[] responseBody;
	
	public HttpResponse(HttpRequest req) throws IOException {
		switch (req.method) {
			case "GET":
				try {
					//TODO: extend functionality to handle sub-directories
					File file = new File("." + "/dist" + req.uri);
					if (file.exists()) {
						long length = (long) file.length();
						setContentLength(length);
						if (file.getName().endsWith(".htm") || file.getName().endsWith(".html")) {
							setContentType(ContentType.HTML);
						} else if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg")) {
							setContentType(ContentType.IMAGE);
						} else {
							setContentType(ContentType.TEXT);
						}
						setResponse(getBytes(file));
					} else {
						log.info("File not found:" + req.uri);
						setHeaders(StatusCode.NOT_FOUND);
						String msg = "<html><body>File " + file + " not found.</body></html>";
						setContentType(ContentType.HTML);
						setResponse(msg);
					}
				} catch (Exception e) {
					log.error("Response Error", e);
					setHeaders(StatusCode.BAD_REQUEST);
					setResponse(StatusCode.BAD_REQUEST);
				}
				break;
			default:
				log.info("Bad Request" + req);
				setHeaders(StatusCode.BAD_REQUEST);
				setResponse(StatusCode.BAD_REQUEST);
				break;
		}
	}
	
	private byte[] getBytes(File file) throws IOException {
		int length = (int) file.length();
		byte[] array = new byte[length];
		InputStream in = new FileInputStream(file);
		int offset = 0;
		while (offset < length) {
			int count = in.read(array, offset, (length - offset));
			offset += count;
		}
		in.close();
		return array;
	}
	
	
	private void setHeaders(String status) {
		headers.add(HttpResponse.VERSION + " " + status);
		headers.add("Connection: close");
	}
	
	public void setDate(Date date) {
		headers.add("Date:" + " " + date.toString());
	}
	
	private void setResponse(String response) {
		responseBody = response.getBytes();
	}
	
	private void setResponse(byte[] response) {
		responseBody = response;
	}
	
	public void setContentLength(long value) {
		headers.add("Content-Length:" + " " +  String.valueOf(value));
	}
	
	public void setContentType(String value) {
		headers.add("Content-Type:" + " " + value);
	}
	
	public static class StatusCode {
		public static final String OK = "200 OK";
		public static final String NOT_FOUND = "404 Not Found";
		public static final String BAD_REQUEST = "400 Bad Request";
		
		
	}
	
	public static class ContentType {
		public static final String TEXT = "text/plain";
		public static final String HTML = "text/html";
		public static final String IMAGE = "image/jpeg";
	}
	
	/*
	 * If the user wants to download the requested image:
	 * 1. uncomment the code below
	 * 2. uncomment line 18 of Handler class and comment line 16   
	*/
	/*
		public void write(OutputStream os) throws IOException {
			DataOutputStream output = new DataOutputStream(os);
			for (String header : headers) {
				output.writeBytes(header + "\r\n");
			}
			output.writeBytes("\r\n");
			if (body != null) {
				output.write(body);
			}
			output.writeBytes("\r\n");
			output.flush();
		}
	 */
}
