import java.util.*;
import java.net.*;

public class Scheduler {

    ////////////////////////////////////////////////////////////
    // ----  Start OF STATIC MEMBERS
    ////////////////////////////////////////////////////////////

    private static final int
            DEFAULT_PRIORITY = 15,
            MAX_PRIORITY = Integer.MAX_VALUE,
            MIN_PRIORITY = Integer.MIN_VALUE,
            DUPLICATE_INCREMENT = 4;

    //increment of priority based on domain
    private static final HashMap<String, Integer> INC_DOMAIN = new HashMap<String, Integer>() {{
        put("COM", 5);
        put("NET", 3);
        put("GOV", -5);
        put("EDU", -3);
    }};

    //returns priority based on multiple HEURISTICS
    private static int getInitialPriority(String url) {
        int ret = DEFAULT_PRIORITY;
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            domain = domain.startsWith("www.") ? domain.substring(4) : domain;

            ret += INC_DOMAIN.get(domain);

            return ret;
        } catch (Exception ex) {
            System.out.println(ex);
            return MIN_PRIORITY + 1000;
        }

    }

    ////////////////////////////////////////////////////////////
    // ----  END OF STATIC MEMBERS
    ////////////////////////////////////////////////////////////

   /*   below is if we wanted class to be not static
    //private final DataBaseConnection connection;
    //private final String Seed;                          // Seed I am interacting with , i.e.: table in database
    // if ypu have a better name, go ahead

    public Scheduler(DataBaseConnection C,String S){
        connection = C;
        Seed = S;
    }

    public static void pushUrl(String url){
        boolean duplicate = connection.existsUrl(url,Seed);
        if(duplicate)
        {
            connection.increasePriorty(url,Seed,DUPLICATE_INCREMENT);
            return;
        }

        int priority = getInitialPriority(url);
        connection.insertUrl(url,Seed,priority);
    }

    public String popUrl(){
        return connection.getTopUrl(Seed);
    }
*/
}
