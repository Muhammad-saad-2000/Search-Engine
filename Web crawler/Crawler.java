import java.io.File;

public class Crawler {

    public static class Crawler_Seeds implements Runnable {

        DataBaseConnection conn;

        public Crawler_Seeds(DataBaseConnection C) {
            conn = C;
        }

        public void run() {
            doWork();
        }

        private void doWork() {
            // while not interrupted, do work
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("craweler says hi");
                String curr_url = conn.popUrlFromSeeds();
                if (curr_url == null) {
                    System.out.println("NO MORE SEEDSF");
                    break;
                }
                // chk if already in some rank
                if (Fetcher.isCrawled(curr_url)) {
                    continue;
                }
                // else, fetch its content to file , then crawl it
                Fetcher.fetchToFile(curr_url, "../retrievedPages/");
                String curr_content = Fetcher.fetchToString(curr_url);
                String[] inner_urls = PatternMatcher.ExtractUrlsFromString(curr_content, curr_url);// Saad:i add the url
                                                                                                   // so i could get the
                                                                                                   // robots
                // push found urls into Seed if NOT CRAWLED
                for (String inner_url : inner_urls) {
                    // chk if already in some rank
                    if (Fetcher.isCrawled(inner_url)) {
                        continue;
                    }
                    System.out.println("checking if already ins eeds");
                    // chk if already in Seeds
                    if (conn.isInSeeds(inner_url)) {
                        conn.increasePriorityInSeeds(inner_url, Scheduler.DUPLICATE_INCREMENT);
                        continue;
                    }
                    // else, push it to Seeds
                    System.out.println("PUSHing to seeds");
                    conn.pushUrlToSeeds(inner_url);
                }

            }

        }

    }

    public static class Crawler_Rank implements Runnable {
        DataBaseConnection conn;
        int rank;

        Crawler_Rank(DataBaseConnection C, int R) {
            conn = C;
            rank = R;
        }

        public void run() {
            doWork();
        }

        private void doWork() {
            // preferably we wait until it finishes without interrupting
            while (!Thread.currentThread().isInterrupted()) {
                String curr_url = conn.popUrlFromRank(rank);
                // if no more urls, DONE!
                if (curr_url == null)
                    break;
                // else, process it
                // not changed so we push to next rank
                if (!Fetcher.isChanged(curr_url)) {
                    conn.pushUrlToNextRank(rank, curr_url);
                    continue;
                }
                //CHANGED -> re-index:
                //1-remove old page from indexed
                File ob = new File("../indexedPages/" + curr_url + ".html");
                ob.delete();

                //2-in queue to be re-indexed
                Fetcher.fetchToFile(curr_url, "../retrievedPages/");

                // now  crawl it
                String curr_content = Fetcher.fetchToString(curr_url);
                String[] inner_urls = PatternMatcher.ExtractUrlsFromString(curr_content, curr_url);// Saad:i add the url
                                                                                                   // so i could get the
                                                                                                   // robots
                // push found urls into Seed if NOT CRAWLED
                for (String inner_url : inner_urls) {
                    if (Fetcher.isCrawled(inner_url))
                        continue;
                    // chk if already in Seeds
                    if (conn.isInSeeds(inner_url)) {
                        conn.increasePriorityInSeeds(inner_url, Scheduler.DUPLICATE_INCREMENT);
                        continue;
                    }
                    // else, push it to Seeds
                    conn.pushUrlToSeeds(inner_url);
                }
            }
        }

    }
}