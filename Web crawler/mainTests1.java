import java.util.Scanner;
import java.util.regex.*;

// U L T I M A T E    T E S T

//CRAWLING ( SEEDS )
class maintest {
    static  final int SECOND = 1000;
    static final int MINUTE = 60 * SECOND;
    static final int HOUR = 60 * MINUTE;
    static final int DAY = 24 * HOUR;
    static final int WEEK = 7 * DAY;
    static final long MONTH = 4L * WEEK;
    static final long YEAR = 12 * MONTH;

    public static void main(String[] args) {

        DataBaseConnection conn = new DataBaseConnection();
        conn.connect();

        //read number of threads wanted
        Scanner sc = new Scanner(System.in);
        System.out.println("how many Crawlers (on Seeds) you want?");
        int NUM_THREADS = sc.nextInt();
        Thread[] Thread_array = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            Thread_array[i] = new Thread(new Crawler.Crawler_Seeds(conn));
            Thread_array[i].start();
        }

        System.out.println("Printing num of active threads: (this number is off by TWO <one for main>)");
        System.out.println(Thread.activeCount());

        // wait for some time
//        try{ Thread.sleep(15 * MINUTE);} catch(Exception e){e.printStackTrace();};

        // interrupt all
        Thread.currentThread().getThreadGroup().interrupt();

        // join all
        for (int i = 0; i < NUM_THREADS; i++) {
            try {
                Thread_array[i].join();
            } catch (InterruptedException e) {
                System.out.println("Exc in joining\n");
                e.printStackTrace();
            }
        }


        // disable connection and end
        conn.disconnect();
    }
}

// RE-CRAWLING (RANK)
class maintest2 {
    static  final int SECOND = 1000;
    static final int MINUTE = 60 * SECOND;
    static final int HOUR = 60 * MINUTE;
    static final int DAY = 24 * HOUR;
    static final int WEEK = 7 * DAY;
    static final long MONTH = 4 * WEEK;
    static final long YEAR = 12 * MONTH;

    public static void main(String[] args) {

        DataBaseConnection conn = new DataBaseConnection();
        conn.connect();

        //read number of threads wanted
        Scanner sc = new Scanner(System.in);
        System.out.println("WHAT RANK TO RE-CRAWL (1->6?) ?");
        int R = sc.nextInt();
        System.out.println("how many Re-Crawlers (on Rank"+R+") you want?");
        int NUM_THREADS = sc.nextInt();
        Thread[] Thread_array = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            Thread_array[i] = new Thread(new Crawler.Crawler_Rank(conn,R));
            Thread_array[i].start();
        }

        System.out.println("Printing num of active threads: (this number is off by TWO <one for main>)");
        System.out.println(Thread.activeCount());

        // wait for some time
        try{
            Thread.sleep(15 * MINUTE);} catch(Exception e){e.printStackTrace();};

        System.out.println("WAITING FOR RE-CRAWLERS TO FINISH");
        // join all
        for (int i = 0; i < NUM_THREADS; i++) {
            try {
                Thread_array[i].join();
            } catch (InterruptedException e) {
                System.out.println("Exc in joining\n");
                e.printStackTrace();
            }
        }

        // disable connection and end
        conn.disconnect();
    }
}

//CRAWLER Seeds
class MainTests0 {
    static final int one_hour = 1000 * 3600;

    public static void main(String[] args) throws Exception {

        DataBaseConnection seedsConnection = new DataBaseConnection();
        seedsConnection.connect();

        Thread t0 = new Thread(new Crawler.Crawler_Seeds(seedsConnection));
        Thread t1 = new Thread(new Crawler.Crawler_Seeds(seedsConnection));
        Thread t2 = new Thread(new Crawler.Crawler_Seeds(seedsConnection));

        t0.start();
        t1.start();
        t2.start();

        // wait for some time
        //Thread.currentThread().sleep(2 * 1000*60);
        System.out.println(Thread.activeCount());

        // interrupt all
        Thread.currentThread().getThreadGroup().interrupt();

        // disable connection and end
        seedsConnection.disconnect();
    }

}

//CRAWLER Rank
class MainTest00 {
    static final int one_hour = 1000 * 3600;

    public static void main(String[] args) throws Exception {

        DataBaseConnection seedsConnection = new DataBaseConnection();
        seedsConnection.connect();

        Thread t0 = new Thread(new Crawler.Crawler_Rank(seedsConnection, 1));
        Thread t1 = new Thread(new Crawler.Crawler_Rank(seedsConnection, 1));
        Thread t2 = new Thread(new Crawler.Crawler_Rank(seedsConnection, 1));

        // preparing tmp_Rank1
        seedsConnection.fillTmpRank(1);

        // START
        t0.start();
        t1.start();
        t2.start();

        // wait until they finish
        t0.join();
        t1.join();
        t2.join();

        // disable connection and end
        seedsConnection.disconnect();
    }

}

//test initial priority
class Maintst {
    public static void main(String[] args) {
        System.out.println(Scheduler.getInitialPriority("https://stackoverflow.com"));
        System.out.println(Scheduler.getInitialPriority("https://en.wikipedia.org/wiki/Sinc_function"));
        System.out.println(Scheduler.getInitialPriority("https://en.wikipedia.org/wiki/Sinc_function#Relationship_to_the_Dirac_delta_distribution"));
        System.out.println(Scheduler.getInitialPriority("https://stackoverflow.com"));
    }
}

// test fetch to file
class MainTests1 {
    public static void main(String[] args) {
        Fetcher.fetchToFile("https://en.wikipedia.org/wiki/Sinc_function#Relationship_to_the_Dirac_delta_distribution",
                "./retrievedPages/");
        Fetcher.fetchToFile("https://en.wikipedia.org/wiki/Sinc_function#Relationship_to_the_Dirac_delta_distribution",
                "./temp/");
    }
}

// test is changed
class MainTests2 {
    public static void main(String[] args) {
        String url = "https://en.wikipedia.org/wiki/Sinc_function#Relationship_to_the_Dirac_delta_distribution";
        System.out.println(Fetcher.isChanged(url));
    }
}

// test database connection
class MainTests3 {
    public static void main(String[] args) {
        DataBaseConnection seedsConnection = new DataBaseConnection();
        seedsConnection.connect();
        seedsConnection.pushUrlToSeeds("Good game5");
        seedsConnection.pushUrlToSeeds("Good game6");
        System.out.println(seedsConnection.popUrlFromSeeds());
        seedsConnection.pushUrlToSeeds("Good game7");
        System.out.println(seedsConnection.popUrlFromSeeds());
        Fetcher.fetchToFile("https://en.wikipedia.org/wiki/Sinc_function#Relationship_to_the_Dirac_delta_distribution",
                "./");
        seedsConnection.pushUrlToSeeds("Good game8");
        System.out.println(seedsConnection.popUrlFromSeeds());
        System.out.println(seedsConnection.isInSeeds("Good game4"));
        Fetcher.fetchToFile("https://stackoverflow.com", "./");
        System.out.println(seedsConnection.isInSeeds("Good game1"));
        System.out.println(seedsConnection.isInSeeds("Good game8"));
        seedsConnection.disconnect();
    }
}

// test is Crawled
class MainTests5 {
    public static void main(String[] args) {
        String url = "https://en.wikipedia.org/wiki/Sinc_function#Relationship_to_the_Dirac_delta_distribution";
        System.out.println(Fetcher.isCrawled(url));
    }
}

// test retrieved urls
class MainTests6 {
    public static void main(String[] args) {
        String url = "https://en.wikipedia.org/wiki/Sinc_function";
        String site = Fetcher.fetchToString(url);
        String[] result = PatternMatcher.ExtractUrlsFromString(site, url);
        System.out.println(result.length);
        for (int i = 0; i < result.length; i++) {
            System.out.println(result[i]);
        }
    }
}

// test robots
class MainTests7 {
    public static void main(String[] args) {
        String url = "https://en.wikipedia.org/wiki/Sinc_function";

        Pattern robotPatternRegex = Pattern.compile(
                "(https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6})\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)",
                Pattern.CASE_INSENSITIVE);
        Matcher robotMatcher = robotPatternRegex.matcher(url);
        robotMatcher.find();
        url = robotMatcher.group(1);
        String robots = Fetcher.fetchRobotsFile(url);

        robotPatternRegex = Pattern.compile("Disallow: (.*)", Pattern.CASE_INSENSITIVE);
        robotMatcher = robotPatternRegex.matcher(robots);

        String robotsPattern = "";
        if (robotMatcher.find()) {
            if (!robotMatcher.group(1).matches("^/|\\/wiki\\/")) {
                robotsPattern += "\\b" + robotMatcher.group(1).replaceAll("[^a-zA-Z0-9/]", "") + "\\b";
            } else {
                robotsPattern += "fuck";
            }
        }
        while (robotMatcher.find()) {
            if (!robotMatcher.group(1).matches("^/")) {
                robotsPattern += "|\\b" + robotMatcher.group(1).replaceAll("[^a-zA-Z0-9/]", "") + "\\b";
            }
        }
        robotsPattern = robotsPattern.replaceAll("/", "\\\\/");
        robotsPattern = robotsPattern.replaceAll("\\\\b\\\\\\/wiki\\\\\\/\\\\b\\|", "");
        robotPatternRegex = Pattern.compile(robotsPattern, Pattern.CASE_INSENSITIVE);

        System.out.println(robotsPattern);
        robotMatcher = robotPatternRegex.matcher("https://en.wikipedia.org/wiki/Fourier_transform");
        if (robotMatcher.find()) {
            System.out.println("w");
        }
    }

}
