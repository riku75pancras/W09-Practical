import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class W09Practical {
    private static Set<String> queryArray = new HashSet<String>(Arrays.asList(new String[]
            {"--search", "--query", "--cache"}));

    public static void main(String[] args) {
        if(args.length != 6){
            System.out.println("The length of the command line argument needs to be 6");
        }
        else if (!queryArray.contains(args[0]) && !queryArray.contains(args[2]) && !queryArray.contains(args[4])){
            System.out.println("The search queries are invalid");
        }
        else {
            SearchAPI searchAPI = new SearchAPI();
        }
    }
}
