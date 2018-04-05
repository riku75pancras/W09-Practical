import javax.xml.xpath.XPathExpressionException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class W09Practical {
    private static Set<String> queryArray = new HashSet<String>(Arrays.asList(new String[]
            {"--search", "--query", "--cache"}));


    public static void main(String[] args) {
        try {
            if (args.length != 6) {
                System.out.println("The length of the command line argument needs to be 6");
            } else if (!queryArray.contains(args[0]) && !queryArray.contains(args[2]) && !queryArray.contains(args[4])) {
                System.out.println("The search queries are invalid");
            } else {
                String searchCategory = null;
                String queryString = null;
                String cacheDirectoryPath = null;

                for (int i = 0; i < args.length; i += 2) {
                    if (args[i].equals("--search")) {
                        if (!args[i + 1].equals("author") && !args[i + 1].equals("publication") && !args[i + 1].equals("venue")) {
                            System.out.println("The search category is invalid");
                        } else {
                            searchCategory = args[i + 1];
                        }
                    } else if (args[i].equals("--query")) {
                        queryString = args[i + 1];
                    } else if (args[i].equals("--cache")) {
                        cacheDirectoryPath = args[i + 1];
                    }
                }
                if (searchCategory != null) {
                    SearchAPI searchAPI = new SearchAPI();
                    searchAPI.startFunctions(searchCategory, queryString, cacheDirectoryPath);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
