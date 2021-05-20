import java.util.regex.*;

public class PatternMatcher {
    /**
     * This function is used to match a url pattern in a string
     *
     * @param str The string to be searched
     * @return An array of strings that containing the matches
     */
    public static String[] ExtractUrlsFromString(String str, String url) {
        // robots.txt
        Pattern robotPatternRegex = Pattern.compile(
                "(https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6})\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)",
                Pattern.CASE_INSENSITIVE);
        Matcher robotMatcher = robotPatternRegex.matcher(url);
        robotMatcher.find();
        url = robotMatcher.group(1);
        String robots = Fetcher.fetchRobotsFile(url);

        robotPatternRegex = Pattern.compile("Disallow: (.*)", Pattern.CASE_INSENSITIVE);
        robotMatcher = robotPatternRegex.matcher(robots);

        String robotsPattern = "";
        if (robotMatcher.find()) {
            if (!robotMatcher.group(1).matches("^/|\\/wiki\\/")) {
                robotsPattern += "\\b" + robotMatcher.group(1).replaceAll("[^a-zA-Z0-9/]", "") + "\\b";
            } else {
                robotsPattern += "fuck";
            }
        }
        while (robotMatcher.find()) {
            if (!robotMatcher.group(1).matches("^/")) {
                robotsPattern += "|\\b" + robotMatcher.group(1).replaceAll("[^a-zA-Z0-9/]", "") + "\\b";
            }
        }
        robotsPattern = robotsPattern.replaceAll("/", "\\\\/");
        robotsPattern = robotsPattern.replaceAll("\\\\b\\\\\\/wiki\\\\\\/\\\\b\\|", "");
        robotPatternRegex = Pattern.compile(robotsPattern, Pattern.CASE_INSENSITIVE);
        //
        Pattern pattern = Pattern.compile(
                "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)|a href=\"(\\/[a-zA-Z0-9][^\"]*)\"",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        int numOfUrls = 0;
        String temp;
        while (matcher.find()) {
            temp = matcher.group(0);
            if (matcher.group(3) != null) {
                temp = url;
                temp += matcher.group(3);
            }
            Pattern pattern2 = Pattern.compile(// remove urls that aren't html
                    "\\bpng\\b|\\bjpg\\b|\\bsvg\\b|\\bjpeg\\b|\\bmp4\\b|\\bmp3\\b|\\bmov\\b|\\bwav\\b|\\bwmv\\b|\\bmkv\\b|\\bflv\\b|\\bavi\\b|\\bwebm\\b|\\bapi\\b|\\bjs\\b|\\bscript\\b",
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher2 = pattern2.matcher(temp);
            if (!matcher2.find()) {
                robotMatcher = robotPatternRegex.matcher(temp);// robots
                if (!robotMatcher.find()) {
                    numOfUrls += 1;
                }
            }
        }
        if (numOfUrls == 0) {
            String[] result = new String[0];
            return result;
        }
        String[] result = new String[numOfUrls];
        matcher = pattern.matcher(str);
        int i = 0;
        while (matcher.find()) {
            result[i] = matcher.group(0);
            if (matcher.group(3) != null) {
                result[i] = url;
                result[i] += matcher.group(3);
            }
            Pattern pattern2 = Pattern.compile(// remove urls that aren't html
                    "\\bpng\\b|\\bjpg\\b|\\bsvg\\b|\\bjpeg\\b|\\bmp4\\b|\\bmp3\\b|\\bmov\\b|\\bwav\\b|\\bwmv\\b|\\bmkv\\b|\\bflv\\b|\\bavi\\b|\\bwebm\\b|\\bapi\\b|\\bjs\\b|\\bscript\\b",
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher2 = pattern2.matcher(result[i]);
            if (!matcher2.find()) {
                robotMatcher = robotPatternRegex.matcher(result[i]);// robots
                if (!robotMatcher.find()) {
                    i++;
                }
            }
        }
        return result;
    }
}
