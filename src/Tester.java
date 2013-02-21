import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Tester {
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		String myString = "dir=" + URLEncoder.encode("hello", "UTF-8") +
		        "&lName=" + URLEncoder.encode("world", "UTF-8");
		HTTPRequest request = new HTTPRequest();
		request.setURL("http://www.whatarecookies.com/cookietest.asp");
		
		request.setUserAgent("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9) Gecko/2008061015 Firefox/3.0");
		request.setAccept("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8", 
				"en-us,en;q=0.5", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
	
		request.setKeepAlive("300");
		request.setConnection("keep-alive");
		request.setReferer("http://www.w3.org/");
		
		request.myCookie.setCookies("name=username; domain=google.com");
		/*
		
		System.out.println("Request: " + request);
		System.out.println("URL: " + request.getURL());
		System.out.println("Get: " + request.get());
		System.out.println("Post: " + request.post("/post.php", myString));
		System.out.println("Get: " + request.get());
		*/
		
		request.get();
	}
}
