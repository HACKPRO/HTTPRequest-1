import httplib.*;

public class Tester {
	public static void main(String[] args) {
		HTTPRequest request = new HTTPRequest();
		
		String sGoogle = request.get("http://www.google.com/");
		
		System.out.println("Google: " + sGoogle);
		
	}
}