import java.sql.*;

public class DataBaseConnection {

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

	public void popUrlFromSeeds() {
		try {
			url = null;
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
	}

	public void pushUrlToSeeds(String url, int priority) {
		try {
			mysqlStatement.executeUpdate("insert into Seeds values ('" + url + "'," + priority + ")");
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	//Gh: this makes more sense to be increasing/decreasing rather than setting a whole new value
	public void increasePriortyInSeeds(String url, int inc_priority) {
		try {
			mysqlStatement
					.executeUpdate("UPDATE Seeds SET PriorityValue = PriorityValue + " + inc_priority + " WHERE UrlName = '" + url + "'");
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void popUrlFromRank(int rank) {
		try {
			url = null;
			ResultSet result = mysqlStatement.executeQuery("SELECT MAX(PriorityValue) FROM Rank" + rank);
			result.next();
			priority = result.getInt(1);
			result = mysqlStatement.executeQuery("SELECT * FROM Rank" + rank + " where priorityValue=" + priority);
			result.next();
			url = result.getString(1);
			mysqlStatement.executeUpdate("DELETE FROM Rank" + rank + " WHERE UrlName = '" + url + "'");
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void pushUrlToRank(int rank, String url, int priority) {
		try {
			mysqlStatement.executeUpdate("insert into Rank" + rank + " values ('" + url + "'," + priority + ")");
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	//this won't be used :(
	//Gh: this makes more sense to be increasing/decreasing rather than setting a whole new value
	public void increasePriortyInRank(int rank, String url, int inc_priority) {
		try {
			mysqlStatement.executeUpdate(
					"UPDATE Rank" + rank + " SET PriorityValue = PriorityValue + " + inc_priority + " WHERE UrlName = '" + url + "'");
		} catch (Exception ex) {
			System.out.println(ex);

		}
	}
}
