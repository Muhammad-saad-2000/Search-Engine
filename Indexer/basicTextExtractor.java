import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.stemmer.Stemmer;

import java.io.*;
import java.nio.file.*;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;
import java.util.function.Function;

public class basicTextExtractor {

    public void run() {
        Source doc = null;
        try {
            
            doc = new Source(new File(htmlFilePath));
        } catch (IOException e) {
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println(e.toString());
            e.printStackTrace();
        }
        if (doc == null)
            return;

        List<Element> title__ = doc.getAllElements("TITLE");
        String title = title__.get(0).getContent().toString();
        String text = doc.getTextExtractor().toString();

        // Anything that is not a letter is a separator, a sequence of separators is one
        // big separator
        String Separators = "[^\\w]+";
        String[] rawWords = text.split(Separators);

        Stemmer stemmer = new PorterStemmer();
        List<String> stemmedWords = new ArrayList<>();
        for (String word : rawWords) {
            if (Filters[0].apply(word) && Filters[1].apply(word) && Filters[2].apply(word)) {
                stemmedWords.add(stemmer.stem(word).toString().toLowerCase(Locale.ROOT));
            }
        }

        Map<String, Integer> stemWordsFrequency = new HashMap<>();
        for (String stemWord : stemmedWords) {
            if (stemWordsFrequency.containsKey(stemWord)) {
                stemWordsFrequency.put(stemWord, stemWordsFrequency.get(stemWord) + 1);

            } else {
                stemWordsFrequency.put(stemWord, 1);
            }
        }

        persistIntoDB(htmlFilePath, title, text, stemWordsFrequency);

        try {
            Files.move(Paths.get(htmlFilePath), Paths.get("./indexedPages/" + htmlFilePath.substring(16)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void persistIntoDB(String URL, String Title, String rawContent, Map<String, Integer> wordFrequencyTable) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/searchengine?allowMultiQueries=true", "root", "ssmk65108");

            StringBuilder stmt = new StringBuilder("DELETE FROM DOCUMENTS WHERE URL = ? ;\n");
            stmt.append("INSERT INTO DOCUMENTS(URL, TITLE, RAW_CONTENT) VALUES (?,?,?)  ;\n");
            stmt.append("SELECT __ID FROM DOCUMENTS WHERE URL = ? INTO @DOCID           ;\n");// variable to hold the
                                                                                              // auto-generated ID of
                                                                                              // the document

            for (int i = 0; i < wordFrequencyTable.size(); i++) {
                stmt.append("INSERT IGNORE INTO WORDS(WORD) VALUES (?) ;\n");
                stmt.append("INSERT INTO WORDS_IN_DOCS(WORD_ID, DOC_ID, TOTAL_OCCURRENCES)    \n");
                stmt.append("       VALUES( (SELECT _ID FROM WORDS WHERE WORD = ?), @DOCID, ?);\n");
            }

            PreparedStatement pStmt = conn.prepareStatement(stmt.toString());

            pStmt.setString(1, URL);
            pStmt.setString(2, URL);
            pStmt.setString(3, Title);
            pStmt.setString(4, rawContent);
            pStmt.setString(5, URL);

            int insertionIdx = 6;
            for (Map.Entry<String, Integer> wordFrequencyPair : wordFrequencyTable.entrySet()) {
                String word = wordFrequencyPair.getKey();
                Integer frequency = wordFrequencyPair.getValue();

                pStmt.setString(insertionIdx++, word);
                pStmt.setString(insertionIdx++, word);
                pStmt.setInt(insertionIdx++, frequency);

            }
            pStmt.executeUpdate();
            conn.close();
        } catch (Exception e) {
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    public basicTextExtractor(String path) {
        htmlFilePath = path;
    }

    private static final Set<String> StopWords = new HashSet<>(Arrays.asList("a", "about", "above", "above", "across",
            "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also", "although",
            "always", "am", "among", "amongst", "amount", "an", "and", "another", "any", "anyhow", "anyone", "anything",
            "anyway", "anywhere", "are", "around", "as", "at", "back", "be", "became", "because", "become", "becomes",
            "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between",
            "beyond", "bill", "both", "bottom", "but", "by", "call", "can", "cannot", "cant", "co", "con", "could",
            "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight",
            "either", "eleven", "else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone",
            "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five",
            "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give",
            "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein",
            "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in",
            "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly",
            "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more",
            "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never",
            "nevertheless", "next", "nine", "no", "nobody", "none", "no one", "nor", "not", "nothing", "now", "nowhere",
            "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our",
            "ours", "ourselves", "out", "over", "own", "part", "per", "perhaps", "please", "put", "rather", "re",
            "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side",
            "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime",
            "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them",
            "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon",
            "these", "they", "thick", "thin", "third", "this", "those", "though", "three", "through", "throughout",
            "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un",
            "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever",
            "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon",
            "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why",
            "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves",
            "the"));
    private static final Function<String, Boolean>[] Filters = new Function[] {
            // Minimum-Length
            word -> ((String) word).length() > 2
            // No Stop Words
            , word -> !(StopWords.contains(word))
            // No Numbers or acronyms
            , word -> !(((String) word).matches("[0-9A-Z]+")) };

    private final String htmlFilePath;
}
