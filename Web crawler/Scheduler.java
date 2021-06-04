import java.util.*;
import java.net.*;

public class Scheduler {

    public static final int DEFAULT_PRIORITY = 10, MAX_PRIORITY = Integer.MAX_VALUE, MIN_PRIORITY = Integer.MIN_VALUE,
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
            URI uri = new URI(url);
            String domain = uri.getHost();
            domain = domain.substring(domain.lastIndexOf('.') + 1, domain.length());
            System.out.println("domain\n" + domain);

            if (INC_DOMAIN.get(domain.toUpperCase()) != null)
                ret += INC_DOMAIN.get(domain.toUpperCase());

            return ret;
        } catch (Exception ex) {
            System.out.println(ex);
            return MIN_PRIORITY + 1000;
        }

    }

}
