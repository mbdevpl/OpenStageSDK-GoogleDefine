package pl.mbdev.openstage.define;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import pl.mbdev.openstage.*;

/**
 * Uses Google's "define:" keyword to find definition of an entered word.
 * 
 * @author Mateusz Bysiek
 */
public class GoogleDefine {
	
	/**
	 * @param out
	 * @param appAddress
	 * @param word
	 *            initial word to be defined
	 */
	public GoogleDefine(PrintWriter out, String appAddress, String word) {
		IppForm fo = new IppForm("Google define tool", appAddress,
				IppForm.Proportion.L25_R75);
		
		IppItem i1 = new IppItem();
		i1.add(new IppTextField("Word", word, "word"));
		i1.add(new IppCommand("Find definition", IppCommand.Type.SELECT, null, null,
				IppCommand.DisplayOn.LISTITEM, false));
		fo.add(i1);
		
		IppStringItem si = null;
		String definition = "";
		try {
			// setting up the connection
			URL google = new URL("http://www.google.com/search?hl=en&q=define%3A" + word);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
					InetAddress.getByName("88.220.37.150"), 8080));
			URLConnection c = google.openConnection(proxy);
			System.setProperty("http.agent", "");
			c.setRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
			
			// reading page
			BufferedReader in = new BufferedReader(new InputStreamReader(
					c.getInputStream()));
			StringBuffer input = new StringBuffer();
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				input.append(inputLine);
			in.close();
			
			// parsing content
			int start = input.indexOf("<body");
			int end = input.indexOf("Dictionary.com", start);
			definition = input.substring(start, end);
			definition = definition.substring(definition.indexOf("<em>"));
			definition = definition.substring(definition.indexOf("<div class=s>"))
					.substring(18);
			definition = definition.substring(0, definition.indexOf("<a class=fl"));
			definition = definition.substring(0, definition.length() - 12).replaceAll(
					"\\<.*?>", "");
			
		} catch (MalformedURLException e) {
			si = new IppStringItem("Error", "your word contains forbidden characters");
		} catch (IOException e) {
			si = new IppStringItem("Error", "could not connect to google.com");
		} catch (Exception e) {
			si = new IppStringItem("Error", "Java exception: " + e.getClass());
		}
		
		IppItem i2 = new IppItem();
		i2.add(new IppStringItem("Definition", definition));
		i2.add(new IppCommand("Exit", IppCommand.Type.EXIT, null, null,
				IppCommand.DisplayOn.LISTITEM, false));
		fo.add(i2);
		
		if (si != null) {
			fo.add(si);
		}
		
		fo.sendTo(out);
	}
}
