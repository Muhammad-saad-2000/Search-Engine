public class docIndexer implements Runnable {

    public docIndexer(String htmlFilePath){
        this.htmlFilePath = htmlFilePath ;
    }

    @Override
    public void run() {
        new basicTextExtractor(htmlFilePath).run();
    }
    private String htmlFilePath ;
}
