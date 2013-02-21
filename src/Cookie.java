import java.net.URLConnection;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * 
 * @author Isonyx
 * @date 2/21/2013
 *
 * Cookie class designed for HTTPRequest.java HTTPWrapper
 * 
 */
public class Cookie {
	private List<String> myCookies = new ArrayList<String>();
	String myCookie;
	
	public Cookie() {
		this.myCookie = "";
	}
	
	public Cookie(String theCookies) {
		this.myCookie = theCookies;
		setCookies(theCookies);
	}
	
	public void setCookies(String theCookies) {
		String storage[];
		this.myCookie = theCookies;
		
		if (theCookies.contains(";")) {
			storage = theCookies.split("; ");
			for (String cookie : storage) {
				this.myCookies.add(cookie);
			}
		} else {
			this.myCookies.add(theCookies);
		}
	}

	public String getCookie(String cookie) {
		String theCookie = "";
		String storage[];
		if (this.myCookies.isEmpty()) { return theCookie; }
		for (String cookies : this.myCookies) {
			storage = cookies.split("=");
			if (storage[0].contains(cookie)) { theCookie = cookies; }
		}
		return theCookie;
	}
	
	public void updateCookies(URLConnection theConnection) {
		int iCounter = 1;
		String key, field;
		while ((key = theConnection.getHeaderFieldKey(iCounter)) != null) {
			if (key.equals("Set-Cookie")) {
				this.myCookie = theConnection.getHeaderField(iCounter);
				this.setCookies(this.myCookie);
			}
			iCounter++;
		}
	}
	
	public void removeCookie(String cookie) {
		String newCookies = "";
		String storage[];
		
		this.myCookie = cookie;
		
		if (this.myCookies.isEmpty()) { return; }
		for (String cookies : this.myCookies) {
			storage = cookies.split("=");
			if (!storage[0].contains(cookie)) { newCookies += cookies + "; "; }
		}
		this.clearCookies();
		this.setCookies(newCookies);
	}
	
	public String getCookies() {
		return myCookie;
	}
	
	public void clearCookies() {
		this.myCookies.clear();
	}
	
	public String toString() {
		return myCookie;
	}
}
 