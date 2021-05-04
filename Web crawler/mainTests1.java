public class mainTests1 {
	public static void main(String[] args) {
		String content = Fetcher.fetchToString(
				"https://en.wikipedia.org/wiki/Sinc_function#Relationship_to_the_Dirac_delta_distribution");
		String[] result = PaternMatcher.ExtractUrlsFromString(content);
		System.out.println(result.length);
		for (int i = 0; i < result.length; i++) {
			System.out.println(result[i]);
		}
	}
}
