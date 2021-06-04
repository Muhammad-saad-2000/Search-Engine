import java.net.*;
import java.io.*;
import java.sql.ResultSet;
import java.util.*;

public class Fetcher {
    
    /**
     * This function is used to fetch the content of the web page and put it into a
     * html file
     *
     * @param url  the web page url
     * @param path the path to put file in
     */
    static void fetchToFile(String url, String path) {
        String content = url + "\n";
        try {
            URLConnection connection = new URL(url).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content += scanner.next();
            scanner.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        try {
            url = url.replaceAll("[^a-zA-Z0-9]", "");
            PrintWriter out = new PrintWriter(path + "/" + url + ".html");
            out.write(content);
            out.close();
        } catch (Exception ex) {
            System.out.println(ex);
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
            System.out.println(ex);
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
            System.out.println(ex);
        }
        return result;
    }


    /**
     * This function is used to check if given web page is changed or not and update
     * the old content
     *
     * @param url The web page url
     */

    static public boolean isChanged(String url) {

        //TODO: maybe better solution to below line ? :D
        //IF being indexed --> say it is changed to avoid concurrency issues with accessing files
        if (!isIndexed(url))
            return true;

        //ELSE  WE CHECK INDEXED FOLDER:
        //get current version
        String current = fetchToString(url);
        current = current.substring(40, 50);
        //get previous version
        String last = readFileToString("./indexedPages/" + url + ".html");
        last = last.substring(40, 50);
        return (current.hashCode() != last.hashCode());
    }

    // TODO this is not req in doc, so mehhhhhhh
    /*
     * static public boolean isDuplicate(String content) { return false; }
     */

    // This func returns true <--> page is in some rank now!
    static public boolean isCrawled(String url) {
        if (isIndexed(url))
            return true;
        // cleaning the url
        url = url.replaceAll("[^a-zA-Z0-9]", "");
        url += ".html";
        // declaring a file
        System.out.println(System.getProperty("user.dir"));
        File testfile = new File("./retrievedPages/", url);

        return testfile.exists();
    }

    static public boolean isIndexed(String url) {
        // cleaning the url
        url = url.replaceAll("[^a-zA-Z0-9]", "");
        url += ".html";
        // declaring a file
        System.out.println(System.getProperty("user.dir"));
        File testfile = new File("./indexedPages/", url);

        return testfile.exists();
    }

}