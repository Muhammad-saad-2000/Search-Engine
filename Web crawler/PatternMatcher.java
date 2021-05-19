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
        String temp;
        while (matcher.find()) {
            temp = matcher.group(0);
            Pattern pattern2 = Pattern.compile(// remove urls that aren't html
                    "\\bpng\\b|\\bjpg\\b|\\bsvg\\b|\\bjpeg\\b|\\bmp4\\b|\\bmp3\\b|\\bmov\\b|\\bwav\\b|\\bwmv\\b|\\bmkv\\b|\\bflv\\b|\\bavi\\b|\\bwebm\\b|\\bapi\\b|\\bjs\\b|\\bscript\\b",
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher2 = pattern2.matcher(temp);
            if (!matcher2.find()) {
                numOfUrls += 1;
            }
        }
        String[] result = new String[numOfUrls];
        matcher = pattern.matcher(str);
        int i = 0;
        while (matcher.find()) {
            result[i] = matcher.group(0);
            Pattern pattern2 = Pattern.compile(// remove urls that aren't html
                    "\\bpng\\b|\\bjpg\\b|\\bsvg\\b|\\bjpeg\\b|\\bmp4\\b|\\bmp3\\b|\\bmov\\b|\\bwav\\b|\\bwmv\\b|\\bmkv\\b|\\bflv\\b|\\bavi\\b|\\bwebm\\b|\\bapi\\b|\\bjs\\b|\\bscript\\b",
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher2 = pattern2.matcher(result[i]);
            if (!matcher2.find()) {
                i++;
            }
        }
        return result;
    }
}
