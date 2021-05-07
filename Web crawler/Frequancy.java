public class Frequancy {
	static public boolean isChanged(String url) {// TODO:Check
		String current = Fetcher.fetchToString(url);
		url = url.replaceAll("[^a-zA-Z0-9]", "");
		String last = Fetcher.readFileToString("./retrievedPages/" + url + ".html");
		return (current.hashCode()!=last.hashCode());
	}

	static public String popUrlFromRank(int rank) {
		String result = "";
		result = Fetcher.popUrlFromFile("./crawlingData/rnk" + rank + ".txt");
		return result;
	}

	static public void pushUrlToRank(String url, int rank) {
		Fetcher.pushUrlToFile(url, "./crawlingData/rnk" + rank + ".txt");
	}

	static public String popUrlFromSeeds() {
		String result = "";
		result = Fetcher.popUrlFromFile("./crawlingData/seeds.txt");
		return result;
	}

	static public void pushUrlToSeeds(String url) {
		Fetcher.pushUrlToFile(url, "./crawlingData/seeds.txt");
	}
}
