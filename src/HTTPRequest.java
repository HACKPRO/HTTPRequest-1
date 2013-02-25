import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
	public Cookie myCookie;
	private HttpURLConnection URLConnection;
	private URL myURL;
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
	
	public HTTPRequest(String URL) {
		this.myCookie = new Cookie();
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
		try { this.myURL = new URL(URL); }
		catch (Exception e) { e.printStackTrace(); }
	}
	
	public HTTPRequest() {
		this.myCookie = new Cookie();
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
	}
	public void setURL(String URL) {
		try { myURL = new URL(URL); } 
		catch (MalformedURLException e) { e.printStackTrace(); }
	}
	
	public URL getURL() {
		return myURL;
	}
	
	public void setUserAgent(String agent) {
		this.myUserAgent = agent;
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
	
	public HttpURLConnection getURLConnection() {
		return URLConnection;
	}
	
	public String get(String link) {
		StringBuffer sReturn = new StringBuffer();
		String sTemporary = "";
		try {
			//Set Request Information
			URLConnection = (HttpURLConnection)new URL(myURL + link).openConnection();
			URLConnection.setRequestMethod("GET");
			URLConnection.setAllowUserInteraction(false);
			URLConnection.setDoOutput(false);
			URLConnection.setInstanceFollowRedirects(false);
			
			//Set Request Properties
			URLConnection.setRequestProperty("User-Agent", this.getUserAgent());
			URLConnection.setRequestProperty("Accept", this.getAccept());
			URLConnection.setRequestProperty("Accept-Language", this.getAcceptLanguage());
			URLConnection.setRequestProperty("Accept-Charset", this.getAcceptCharset());
			URLConnection.setRequestProperty("Keep-Alive", this.getKeepAlive());
			URLConnection.setRequestProperty("Connection", this.getConnection());
			URLConnection.setRequestProperty("Referer", this.getReferer());
			
			//Set Cookies
			if (!myCookie.toString().isEmpty()) {
				URLConnection.setRequestProperty("Cookie", myCookie.toString());
			}
			
			//Connect
			URLConnection.connect();
			
			//Read Response
			BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream)URLConnection.getContent()));
			while ((sTemporary = reader.readLine()) != null) {
				sReturn.append(sTemporary + "\r");
			}
			
			//Clean Up
			reader.close();
			myCookie.updateCookies(URLConnection);
			this.setReferer(myURL.toString());
			
			return sReturn.toString();
		} catch (Exception e) { 
			e.printStackTrace(); 
			return null;
		} finally {
			if (URLConnection != null) { URLConnection.disconnect(); }
		}
	}
	
	public void post(String link, String parameters) {
		StringBuffer sReturn = new StringBuffer();
		String sTemporary = "";
		try {
			//Set Request Information
			URLConnection = (HttpURLConnection)new URL(myURL + link).openConnection();
			URLConnection.setRequestMethod("POST");
			URLConnection.setAllowUserInteraction(false);
			URLConnection.setDoOutput(true);
			URLConnection.setInstanceFollowRedirects(false);
			
			//Set Request Properties
			URLConnection.setRequestProperty("User-Agent", this.getUserAgent());
			URLConnection.setRequestProperty("Accept", this.getAccept());
			URLConnection.setRequestProperty("Accept-Language", this.getAcceptLanguage());
			URLConnection.setRequestProperty("Accept-Charset", this.getAcceptCharset());
			URLConnection.setRequestProperty("Keep-Alive", this.getKeepAlive());
			URLConnection.setRequestProperty("Connection", this.getConnection());
			URLConnection.setRequestProperty("Referer", this.getReferer());
			URLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			URLConnection.setRequestProperty("Content-Length", Integer.toString(parameters.length()));
			
			//Set Cookies
			if (!myCookie.toString().isEmpty()) {
				URLConnection.setRequestProperty("Cookie", myCookie.toString());
			}
			
			OutputStreamWriter writer = new OutputStreamWriter(URLConnection.getOutputStream());
			writer.write(parameters);
			writer.flush();
			writer.close();
			
			String sResult = "";
			String sLine = null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(URLConnection.getInputStream())));
			while ((sLine = reader.readLine()) != null) sResult += sLine + "\n";
			
			//Clean Up
			reader.close();
			myCookie.updateCookies(URLConnection);
			this.setReferer(myURL.toString());
		} catch (Exception e) {
			e.printStackTrace(); 
			return;
		}
	}
	
}
