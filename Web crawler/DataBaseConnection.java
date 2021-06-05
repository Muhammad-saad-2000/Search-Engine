import java.sql.*;
import java.util.*;

public class DataBaseConnection {

    public static final int MAX_RANK = 5;

    Connection mysqlConnection;
    Statement mysqlStatement;
    public String url = null;
    public int priority;

    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            mysqlConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/searchengine", "root",
                    "ssmk65108");
            // mysqlStatement = mysqlConnection.createStatement();
        } catch (Exception ex) {
            System.out.println("AAAAAAAAAAAAA");
            ex.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            mysqlConnection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String popUrlFromSeeds() {
        url = null;
        try {
            mysqlStatement = mysqlConnection.createStatement();
            ResultSet result = mysqlStatement.executeQuery("SELECT MAX(PriorityValue) FROM Seeds");
            if (result != null && !result.next()) {
                return null;
            }
            priority = result.getInt(1);
            result = mysqlStatement.executeQuery("SELECT * FROM Seeds where PriorityValue=" + priority);
            if (result != null && !result.next()) {
                return null;
            }
            url = result.getString(1);
            mysqlStatement.executeUpdate("DELETE FROM Seeds WHERE UrlName = '" + url + "'");
        } catch (Exception ex) {
            System.out.println("exc in popping from seeds");
            ex.printStackTrace();
            System.out.println("");
        }

        return url;
    }

    public void pushUrlToSeeds(String url) {
        int priority = Scheduler.getInitialPriority(url);
        try {
            mysqlStatement = mysqlConnection.createStatement();
            mysqlStatement.executeUpdate("insert into Seeds values ('" + url + "'," + priority + ")");
        } catch (Exception ex) {
            System.out.println("EXC in inserting to seeds");
            ex.printStackTrace();
        }
    }

    // used to check if some url already in Seeds, checking for duplicates
    public boolean isInSeeds(String url) {
        boolean ret = false;
        try {
            mysqlStatement = mysqlConnection.createStatement();
            ResultSet result = mysqlStatement.executeQuery("SELECT * FROM Seeds WHERE UrlName = '" + url + "'");

            if (result != null && result.next())
                ret = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    // Gh: this makes more sense to be increasing/decreasing rather than setting a
    // whole new value
    public void increasePriorityInSeeds(String url, int inc_priority) {
        try {
            //// these lines are there just to avoid overflow of priority
            ResultSet res = mysqlStatement
                    .executeQuery("SELECT PriorityValue FROM Seeds WHERE UrlName = '" + url + "'");

            if (!res.next()) {
                return;
            }
            int old_pr = res.getInt(1);
            int new_Pr = old_pr;
            if (Scheduler.MAX_PRIORITY - inc_priority >= new_Pr)
                new_Pr += inc_priority;
            //////

            mysqlStatement
                    .executeUpdate("UPDATE Seeds SET PriorityValue =  " + new_Pr + " WHERE UrlName = '" + url + "'");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Gh: this fills tmp_Rank for crawling purposes
    public void fillTmpRank(int rank) {
        try {
            // create if not exists
            mysqlStatement.executeUpdate("CREATE TABLE IF NOT EXISTS tmp_Rank" + rank + " (UrlName varchar(2048))");
            // Copy table
            mysqlStatement.executeUpdate("INSERT INTO tmp_Rank" + rank + " SELECT * FROM Rank" + rank);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Gh: Changed to return ALL urls as an array of Strings
    public String popUrlFromRank(int rank) {
        try {
            ResultSet result = mysqlStatement.executeQuery("SELECT * FROM tmp_Rank" + rank + " LIMIT 1");
            // if found return it, else return NULL
            if (result.next()) {
                url = result.getString(1);
                mysqlStatement.executeUpdate("DELETE FROM Rank" + rank + " WHERE UrlName = '" + url + "'");
                return url;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void pushUrlToRank(int rank, String url) {
        try {
            mysqlStatement.executeUpdate("insert into Rank" + rank + " values ('" + url + "')");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // used to decrease freq. of certain url
    public void pushUrlToNextRank(int rank, String url) {
        // do nothing if already in last rank
        if (rank == MAX_RANK)
            return;
        try {
            mysqlStatement.executeUpdate("DELETE FROM Rank" + rank + " WHERE UrlName = '" + url + "'");
            pushUrlToRank(rank + 1, url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
// TODO: close statements ?