import java.sql.*;
import java.util.*;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class indexer {
    private static final String HTML_FOLDER_PATH = "./retrievedPages/";
    private static int MAX_THREAD_POOL_SIZE = 4;
    
    public static void main(String[] args) {
        System.out.println("How many threads for Indexer? ");
        Scanner sc = new Scanner(System.in);
        MAX_THREAD_POOL_SIZE = sc.nextInt();

        File folder = new File(HTML_FOLDER_PATH);
        File[] htmlFiles = folder.listFiles();

        ExecutorService tPool = Executors.newFixedThreadPool(MAX_THREAD_POOL_SIZE);

        for (File file : htmlFiles) {
            tPool.execute(new docIndexer(file.getPath()));
        }
        tPool.shutdown();
    }
}

class Test {
    public static void main(String[] args) {
        try {
            System.out.println();
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/searchengine?allowMultiQueries=true",
                    "root", "ssmk65108");
            Statement mysqlStatement = conn.createStatement();
            ResultSet result = mysqlStatement.executeQuery("SELECT * FROM words LIMIT 100");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}