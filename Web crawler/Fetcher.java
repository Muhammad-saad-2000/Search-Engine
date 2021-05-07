import java.net.*;
import java.io.*;
import java.util.*;

public class Fetcher {
	/**
	 * This function is used to fetch the content of the web page and put it into a
	 * html file
	 * 
	 * @param url      the web page url
	 * @param filename the name of the file without extension
	 */
	static void fetchToFile(String url, String path) {
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
		try {
			url = url.replaceAll("[^a-zA-Z0-9]", "");
			PrintWriter out = new PrintWriter(path + "/" + url + ".html");
			out.write(content);
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
	 * This function is used to read the first url from a file
	 * 
	 * @param filePath the path to the file
	 * @return a string that contains the url
	 */
	static String popUrlFromFile(String filePath) {
		String result = "";
		try {
			Scanner scanner = new Scanner(new File(filePath));
			if (scanner.hasNextLine()) {
				result = scanner.nextLine();
			}
			scanner.close();
			removeFirstLine(filePath);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * This function is used to write a url to a file
	 * 
	 * @param url      a string representing the url
	 * @param filePath the path to the file the url will be put into
	 */
	static void pushUrlToFile(String url, String filePath) {
		try {
			PrintWriter out = new PrintWriter(new FileOutputStream(new File(filePath), true));
			out.println(url);
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * This function is used to get the number of urls form file
	 * 
	 * @param filePath the path to the file from which the url will be counted
	 */
	static int getUrlsNumber(String filePath) {
		int urlsNumber = 0;
		try {
			File file = new File(filePath);
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				scanner.nextLine();
				urlsNumber += 1;
			}
			scanner.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return urlsNumber;
	}

	/**
	 * This function is used to delete the first line
	 * 
	 * @param filePath the path to the file from which the first lin will be removed
	 */
	public static void removeFirstLine(String fileName) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		// Initial write position
		long writePosition = raf.getFilePointer();
		raf.readLine();
		// Shift the next lines upwards.
		long readPosition = raf.getFilePointer();

		byte[] buff = new byte[1024];
		int n;
		while (-1 != (n = raf.read(buff))) {
			raf.seek(writePosition);
			raf.write(buff, 0, n);
			readPosition += n;
			writePosition += n;
			raf.seek(readPosition);
		}
		raf.setLength(writePosition);
		raf.close();
	}

	/**
	 * This function is used to copy data from a file to a string
	 * 
	 * @param filePath the path to the file from which the data will be read
	 */
	static String readFileToString(String filePath) {
		String result = "";
		try {
			File file = new File(filePath);
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				result += scanner.nextLine();
			}
			scanner.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
}