//testing crawler as a whole
// I didn't run this :)
class MainTests0{
	public static void main(String[] args) throws  Exception {

		DataBaseConnection seedsConnection = new DataBaseConnection();
		seedsConnection.connect();

		Thread t0 = new Thread(new Crawler.Crawler_Seeds(seedsConnection));
		Thread t1 = new Thread(new Crawler.Crawler_Rank(seedsConnection,1));

		t0.start();
		t1.start();

		//wait some time
		final int one_hour = 1000 * 3600;
		Thread.currentThread().sleep(2*one_hour);

		//interrupt all
		Thread.currentThread().notifyAll();

		//making sure they are done
		t0.join();
		t1.join();

		//disable connection and end
		seedsConnection.disconnect();
	}
}


//test fetch to file
class MainTests1 {
	public static void main(String[] args) {
		Fetcher.fetchToFile("https://en.wikipedia.org/wiki/Sinc_function#Relationship_to_the_Dirac_delta_distribution",
				"./retrievedPages/");
		Fetcher.fetchToFile("https://en.wikipedia.org/wiki/Sinc_function#Relationship_to_the_Dirac_delta_distribution",
				"./temp/");
	}
}

//test is changed
class MainTests2 {
	public static void main(String[] args) {
		String url = "https://en.wikipedia.org/wiki/Sinc_function#Relationship_to_the_Dirac_delta_distribution";
		System.out.println(Fetcher.isChanged(url));
	}
}

//test database connection
class MainTests3 {
	public static void main(String[] args) {
		DataBaseConnection seedsConnection = new DataBaseConnection();
		seedsConnection.connect();
		// seedsConnection.pushUrlToSeeds("https://stackoverflow.com", 15);

		// seedsConnection.popUrlFromSeeds();
		// System.out.println("Url: " + seedsConnection.url + "\npriority: " +
		// seedsConnection.priority);

		// seedsConnection.updatePriortyInSeeds("https://stackoverflow.com", 50);
		seedsConnection.disconnect();
	}
}

//test scheduler
class MainTests4{
	public static void main(String[] args){
		DataBaseConnection seedsConnection = new DataBaseConnection();
		seedsConnection.connect();



		seedsConnection.disconnect();
	}

}

//test is Crawled
class MainTests5{
	public static void main(String[] args){
		String url = "https://en.wikipedia.org/wiki/Sinc_function#Relationship_to_the_Dirac_delta_distribution";
		System.out.println(Fetcher.isCrawled(url));
	}
}