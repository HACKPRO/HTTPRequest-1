import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 
 * @author Isonyx
 * @date 2/21/2013
 * 
 * HTTPWrapper designed to handle specific header values as well as cookies.
 * 
 */
public class HTTPRequest {
	private URL myURL;
	private HttpURLConnection URLConnection;
	private String myUserAgent;
	private String myAcceptType;
	private String myAcceptLanguage;
	private String myAcceptCharset;
	private String myKeepAlive;
	private String myConnection;
	private String myReferer;
	private String myContentType;
	private String myContentLength;
	private String myContentLanguage;
	public Cookie myCookie;
	
	public HTTPRequest(String myURL) {
		this.URLConnection = null;
		this.myUserAgent = "";
		this.myAcceptType = "";
		this.myAcceptLanguage = "";
		this.myAcceptCharset = "";
		this.myKeepAlive = "";
		this.myConnection = "";
		this.myReferer = "";
		this.myContentType = "";
		this.myContentLength = "";
		this.myContentLanguage = "";
		this.myCookie = new Cookie();
		try { this.myURL = new URL(myURL); }
		catch (Exception e) { e.printStackTrace(); }
	}
	
	public HTTPRequest() {
		this.URLConnection = null;
		this.myUserAgent = "";
		this.myAcceptType = "";
		this.myAcceptLanguage = "";
		this.myAcceptCharset = "";
		this.myKeepAlive = "";
		this.myConnection = "";
		this.myReferer = "";
		this.myCookie = new Cookie();
	}
	
	public void setURL(String URL) {
		try { myURL = new URL(URL); } 
		catch (MalformedURLException e) { e.printStackTrace(); }
	}
	
	public URL getURL() {
		return myURL;
	}
	
	public void setUserAgent(String userAgent) {
		this.myUserAgent = userAgent;
	}
	
	public String getUserAgent() {
		return myUserAgent;
	}
	
	public void setAccept(String type, String language, String charset) {
		this.myAcceptType = type;
		this.myAcceptLanguage = language;
		this.myAcceptCharset = charset;
	}
	
	public String getAccept() {
		return myAcceptType;
	}
	
	public String getAcceptLanguage() {
		return myAcceptLanguage;
	}
	
	public String getAcceptCharset() {
		return myAcceptCharset;
	}
	
	public void setKeepAlive(String keepAlive) {
		this.myKeepAlive = keepAlive;
	}
	
	public String getKeepAlive() {
		return myKeepAlive;
	}
	
	public void setConnection(String connection) {
		this.myConnection = connection;
	}
	
	public String getConnection() {
		return myConnection;
	}
	
	public void setReferer(String referer) {
		this.myReferer = referer;
	}
	
	public String getReferer() {
		return myReferer;
	}
	
	public void setContent(String type, String length, String language) {
		this.myContentType = type;
		this.myContentLength = length;
		this.myContentLanguage = language;
	}
	
	public String getContentType() {
		return myContentType;
	}
	
	public String getContentLength() {
		return myContentLength;
	}
	
	public String getContentLanguage() {
		return myContentLanguage;
	}

	public String get() {
		StringBuffer returnString = new StringBuffer();
		String temporary = "";
		try {
			URLConnection = (HttpURLConnection)myURL.openConnection();
			URLConnection.setRequestMethod("GET");
			URLConnection.setInstanceFollowRedirects(true);
			URLConnection.setDoOutput(true);		
			URLConnection.setDoInput(true);
			
			URLConnection.setRequestProperty("User-Agent", this.getUserAgent());
			URLConnection.setRequestProperty("Accept", this.getAccept());
			URLConnection.setRequestProperty("Accept-Language", this.getAcceptLanguage());
			URLConnection.setRequestProperty("Accept-Charset", this.getAcceptCharset());
			URLConnection.setRequestProperty("Keep-Alive", this.getKeepAlive());
			URLConnection.setRequestProperty("Connection", this.getConnection());
			URLConnection.setRequestProperty("Referer", this.getReferer());
	
			if (!myCookie.toString().isEmpty()) { URLConnection.setRequestProperty("Cookie", myCookie.toString()); }
			
			URLConnection.connect();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream)URLConnection.getContent()));
			while((temporary = reader.readLine()) != null) { returnString.append(temporary + "\r"); }
			reader.close();
			
			myCookie.updateCookies(URLConnection);
			
			this.setReferer(myURL.toString());
			
			return returnString.toString();
		} catch (Exception e) { 
			e.printStackTrace(); 
			return null;
		} finally {
			if (URLConnection != null) { URLConnection.disconnect(); }
		}
	}
	
	public String post(String theLink, String parameters) {
		StringBuffer returnString = new StringBuffer();
		String temporary = "";
		try {
			URLConnection = (HttpURLConnection)new URL(myURL + theLink).openConnection();
			URLConnection.setRequestMethod("POST");
			URLConnection.setRequestProperty("User-Agent", this.getUserAgent());
			URLConnection.setRequestProperty("Accept", this.getAccept());
			URLConnection.setRequestProperty("Accept-Language", this.getAcceptLanguage());
			URLConnection.setRequestProperty("Accept-Charset", this.getAcceptCharset());
			URLConnection.setRequestProperty("Keep-Alive", this.getKeepAlive());
			URLConnection.setRequestProperty("Connection", this.getConnection());
			URLConnection.setRequestProperty("Referer", this.getReferer());
	
			if (!myCookie.toString().isEmpty()) { URLConnection.setRequestProperty("Cookie", myCookie.toString()); }
			
			URLConnection.setRequestProperty("Content-Type", this.getContentType());
			URLConnection.setRequestProperty("Content-Length", "" + Integer.toString(parameters.getBytes().length));
		    URLConnection.setRequestProperty("Content-Language", this.getContentLanguage());  
		    URLConnection.setUseCaches (false);
		    URLConnection.setDoInput(true);
		    URLConnection.setDoOutput(true);
		    
		    DataOutputStream write = new DataOutputStream(URLConnection.getOutputStream());
		    write.writeBytes(parameters);
		    write.flush();
		    write.close();
		    BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream)URLConnection.getInputStream()));
		    while((temporary = reader.readLine()) != null) { returnString.append(temporary + "\r"); }
		    reader.close();
		    return returnString.toString();
		} catch (Exception e) { 
			e.printStackTrace();
			return null;
		} finally {	
			if (URLConnection != null) { URLConnection.disconnect(); }
		}
	}
	
	public String toString() {
		return myURL.getHost();
	}
	
}
