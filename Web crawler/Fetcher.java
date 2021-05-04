import java.net.*;
import java.io.*;
import java.util.*;

public class Fetcher {
	/**
	 * This function is used to fetch the content of the web page and put it into a
	 * file
	 * 
	 * @param url      the web page url
	 * @param filename the name of the file without extension
	 */
	static void fetchToFile(String url, String filename) {

		// Get the content from the URL
		String content = null;
		try {
			URLConnection connection = new URL(url).openConnection();
			Scanner scanner = new Scanner(connection.getInputStream());
			scanner.useDelimiter("\\Z");
			content = scanner.next();
			scanner.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// Write the content to a file
		try {
			PrintWriter out = new PrintWriter(filename + ".html");
			out.println(content);
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * This function is used to fetch the content of the web page and return a
	 * string containing the content
	 * 
	 * @param url the web page url
	 * @return the content of the page as a string
	 */
	static String fetchToString(String url) {
		String content = null;
		URLConnection connection = null;
		try {
			connection = new URL(url).openConnection();
			Scanner scanner = new Scanner(connection.getInputStream());
			scanner.useDelimiter("\\Z");
			content = scanner.next();
			scanner.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return content;
	}

	/**
	 * This function is used to fetch the robots.txt of the web page and return a
	 * string containing its content
	 * 
	 * @param url the web page url
	 * @return the robots.txt content
	 */
	static String fetchRobotsFile(String url) {
		return fetchToString(url + "/robots.txt");
	}

	/**
	 * This function is used to read the urls of the seed file
	 * 
	 * @param path the path to the seed file
	 * @return array of strings that contains the urls
	 */
	static String[] getUrlsFromSeeds(String path) {
		String result[] = null;
		try {
			File seedFile = new File(path);
			// get the number of lines
			Scanner scanner = new Scanner(seedFile);
			int numOfUrls = 0;
			while (scanner.hasNextLine()) {
				scanner.nextLine();
				numOfUrls += 1;
			}
			scanner.close();
			// get the data
			result = new String[numOfUrls];
			scanner = new Scanner(seedFile);
			for (int i = 0; i < numOfUrls; i++) {
				String data = scanner.nextLine();
				result[i] = data;
			}
			scanner.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * This function is used to read the urls of the seed file
	 * 
	 * @param url
	 * @param path the path to the seeds file
	 */
	static void addUrlToSeeds(String url, String path) {
		try {
			PrintWriter out = new PrintWriter(path);
			out.println(url);
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}