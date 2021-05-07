public class MainTests1 {
	public static void main(String[] args) {
		Fetcher.fetchToFile("https://en.wikipedia.org/wiki/Sinc_function#Relationship_to_the_Dirac_delta_distribution","./retrievedPages/");
		Fetcher.fetchToFile("https://en.wikipedia.org/wiki/Sinc_function#Relationship_to_the_Dirac_delta_distribution","./temp/");
	}
}

class MainTests2 {
	public static void main(String[] args) {
		String url;
		url = Frequancy.popUrlFromSeeds();
		while (!url.equals("")) {
			Frequancy.pushUrlToRank(url, 1);
			String content = Fetcher.fetchToString(url);
			String[] result = PaternMatcher.ExtractUrlsFromString(content);
			System.out.println("The url:" + url + " | num of inner links: " + result.length);
			System.out.println(result[0]);
			for (int i = 0; i < result.length; i++) {
				Frequancy.pushUrlToSeeds(result[i]);
			}
			url = Frequancy.popUrlFromSeeds();
		}
	}
}

class MainTests3 {
	public static void main(String[] args) {
		String url = "https://en.wikipedia.org/wiki/Sinc_function#Relationship_to_the_Dirac_delta_distribution";
		url = url.replaceAll("[^a-zA-Z0-9]", "");
		String last = Fetcher.readFileToString("./retrievedPages/" + url + ".html");
		String current = Fetcher.readFileToString("./temp/" + url + ".html");
		System.out.println(current.hashCode());
		System.out.println(last.hashCode());
	}
}