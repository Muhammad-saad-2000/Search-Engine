import java.util.*;
import java.net.*;

public class Scheduler {

    public static final int DEFAULT_PRIORITY = 15, MAX_PRIORITY = Integer.MAX_VALUE, MIN_PRIORITY = Integer.MIN_VALUE,
            DUPLICATE_INCREMENT = 4;

    // increment of priority based on domain
    private static final HashMap<String, Integer> INC_DOMAIN = new HashMap<String, Integer>() {
        {
            put("COM", 5);
            put("NET", 3);
            put("GOV", -5);
            put("EDU", -3);
        }
    };

    // returns priority based on multiple HEURISTICS
    public static int getInitialPriority(String url) {
        int ret = DEFAULT_PRIORITY;
        try {
            // tmp
            if (true)
                return 10;

            URI uri = new URI(url);
            String domain = uri.getHost();
            System.out.println("domain\n" + domain);
            System.out.println(domain.startsWith("www."));
            domain = domain.startsWith("www.") ? domain.substring(4) : domain;

            ret += INC_DOMAIN.get(domain);

            return ret;
        } catch (Exception ex) {
            System.out.println(ex);
            return MIN_PRIORITY + 1000;
        }

    }

}
