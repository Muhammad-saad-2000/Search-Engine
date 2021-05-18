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
			mysqlStatement = mysqlConnection.createStatement();
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void disconnect() {
		try {
			mysqlConnection.close();
		} catch (Exception ex) {
		}
	}

	public String popUrlFromSeeds() {
		url = null;
		try {
			ResultSet result = mysqlStatement.executeQuery("SELECT MAX(PriorityValue) FROM Seeds");
			result.next();
			priority = result.getInt(1);
			result = mysqlStatement.executeQuery("SELECT * FROM Seeds where priorityValue=" + priority);
			result.next();
			url = result.getString(1);
			mysqlStatement.executeUpdate("DELETE FROM Seeds WHERE UrlName = '" + url + "'");

		} catch (Exception ex) {
			System.out.println(ex);
		}
		return url;
	}

	public void pushUrlToSeeds(String url) {
		int priority = Scheduler.getInitialPriority(url);
		try {
			mysqlStatement.executeUpdate("insert into Seeds values ('" + url + "'," + priority + ")");
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	//Gh: this makes more sense to be increasing/decreasing rather than setting a whole new value
	public void increasePriorityInSeeds(String url, int inc_priority) {
		try {
			//// these lines are there just to avoid overflow of priority
			ResultSet res = mysqlStatement.executeQuery(
					"SELECT Priority FROM Seeds WHERE UrlName = '" + url + "'");
			res.next();
			int old_pr = res.getInt(0);
			int new_Pr = old_pr;
			if(Scheduler.MAX_PRIORITY - inc_priority >= new_Pr)
				new_Pr += inc_priority;
			//////

			mysqlStatement
					.executeUpdate("UPDATE Seeds SET PriorityValue =  " + new_Pr + " WHERE UrlName = '" + url + "'");
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	//Gh: Changed to return ALL urls as an array of Strings
	public String[] getUrlsFromRank(int rank) {
		try {
			ResultSet result = mysqlStatement.executeQuery("SELECT * FROM Rank" + rank);

			ArrayList<String> list= new ArrayList<String>();
			while (result.next()) {
				list.add(result.getString(0));
			}

			String[] ret = new String[list.size()];
			ret = list.toArray(ret);

			for(int i =0; i<ret.length; i++){
				System.out.println(ret[i]);
			}

			return ret;
		} catch (Exception ex) {
			System.out.println(ex);
			return new String[0];
		}
	}

	public void pushUrlToRank(int rank, String url) {
		try {
			mysqlStatement.executeUpdate("insert into Rank" + rank + " values ('" + url  + "')");
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	//used to decrease freq. of certain url
	public void pushUrlToNextRank(int rank,String url){
		//do nothing if already in last rank
		if(rank==MAX_RANK)
			return;
		try {
			mysqlStatement.executeUpdate("DELETE FROM Rank" + rank + " WHERE UrlName = '" + url + "'");
			pushUrlToRank(rank+1,url);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	//used to check if some url already in Seeds, checking for duplicates
	public boolean isInSeeds(String url){
		boolean ret = false;
		try {
			ResultSet result = mysqlStatement.executeQuery("SELECT * FROM Seeds WHERE UrlName = '" + url + "'");
			if(result.next())
				ret=true;
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return ret;
	}
}
