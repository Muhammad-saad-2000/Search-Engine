import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class indexer {
    private static final String HTML_FOLDER_PATH = "../retrievedPages/" ;
    private static final int MAX_THREAD_POOL_SIZE = 4;

    public static void main(String[] args){

        File folder = new File(HTML_FOLDER_PATH);
        File[] htmlFiles = folder.listFiles() ;

        ExecutorService tPool = Executors.newFixedThreadPool(MAX_THREAD_POOL_SIZE) ;

        for(File file : htmlFiles) {
            tPool.execute(new docIndexer(file.getPath()));
        }
        tPool.shutdown();
    }
}
