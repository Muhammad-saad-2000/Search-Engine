import java.util.regex.*;

public class PatternMatcher {
    /**
     * This function is used to match a url pattern in a string
     *
     * @param str The string to be searched
     * @return An array of strings that containing the matches
     */
    public static String[] ExtractUrlsFromString(String str) {
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
}
