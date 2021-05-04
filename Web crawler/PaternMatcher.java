import java.util.regex.*;

//if there is no paragraph don't fetch
public class PaternMatcher {
	/**
	 * This function is used to matche a url pattern in a string
	 * 
	 * @param srt The string to be searched
	 * @return An array of strings that containing the matches
	 */
	static String[] ExtractUrlsFromString(String str) {
		Pattern pattern = Pattern.compile(
				"https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		int numOfUrls = 0;
		while (matcher.find()) {
			numOfUrls += 1;
		}
		String[] result = new String[numOfUrls];
		matcher = pattern.matcher(str);
		for (int i = 0; i < numOfUrls; i++) {
			matcher.find();
			result[i] = matcher.group(0);
		}
		return result;
	}

	public static void main(String[] args) {
		String content = Fetcher.fetchToString(
				"https://en.wikipedia.org/wiki/Sinc_function#Relationship_to_the_Dirac_delta_distribution");
		String[] result = ExtractUrlsFromString(content);
		System.out.println(result.length);
		for (int i = 0; i < result.length; i++) {
			System.out.println(result[i]);
		}
	}
}
