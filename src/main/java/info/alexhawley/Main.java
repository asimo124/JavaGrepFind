package info.alexhawley;

import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static LinkedHashMap<String, String> OptionKeys = new LinkedHashMap<String, String>();
    static String globArg = "";
    static String searchFilesArg = "";
    static String searchTextArg = "";
    static String excludeArg = "";
    static String[] excludeArgArr;

    public static void main(String[] args) {
        OptionKeys.put("h", "help");
        OptionKeys.put("g", "glob");
        OptionKeys.put("f", "search-files");
        OptionKeys.put("t", "search-text");
        OptionKeys.put("x", "exclude");
        parseArgs(args);
    }
    private static void outputUsage() {
        System.out.println("Description:\n" +
                "  Searches files in current directory, according to a glob string, for a given string, and then \n" +
                "  searches those returned files by a second string. You can also provide multiple strings that if \n" +
                "  found in either the path found in the glob string or in the lines found for the second string, \n" +
                "  will ommit that found line.\n" +
                "\n" +
                "Usage:\n" +
                "  grepfind [options]\n" +
                "\n" +
                "Options:\n" +
                "  -h, --help                 Display this help message\n" +
                "  -g, --glob                 glob string to search set of files\n" +
                "  -f, --search-files         first keyword string to search files from glob\n" +
                "  -t, --search-text          second keyword string to search text in files found from first keyword \n" +
                "                             search \n" +
                "  -x, --exclude              string to exclude if found in path of files returned from glob, or \n" +
                "                             text in lines returned by second keyword search. You can provide \n" +
                "                             as many exclusions as you want, but you have to delimit them with the \n " +
                "                             '~' char\n" +
                "\n" +
                "Help:\n" +
                "  The grepfind command finds a set of files by a string, and then searches that set of files by \n" +
                "  another string, and can also exclude by a string(s), if found in path or search result line\n" +
                "  and debug mode:\n" +
                "\n" +
                "    grepfind -g '*php' -f 'number' -t 'decimal' -x 'vendor~js~css' \n");
    }
    private static void parseArgs(String[] passedArgs) {
        int i = 0;
        boolean isHelp = false;
        for (String arg: passedArgs) {
            String currentOption = "";
            String currentOptionVal = "";
            if (startsWithHyphen(arg)) {
                if (arg.length() == 2) {
                    String optLetter = arg.substring(1, 2);
                    if (OptionKeys.get(optLetter) != null) {
                        currentOption = OptionKeys.get(optLetter);
                    }
                    if (arg.substring(1, 1).equals("h")) {
                        outputUsage();
                    }
                } else {
                    currentOption = arg.substring(2, arg.length());
                }
            }
            if (!currentOption.equals("")) {
                if (currentOption.equals("help")) {
                    isHelp = true;
                    outputUsage();
                    break;
                }
                int j = i + 1;
                if (j < passedArgs.length) {
                    currentOptionVal = passedArgs[j];
                    if (startsEndsWithQuotes(currentOptionVal)) {
                        currentOptionVal = currentOptionVal.substring(1, currentOptionVal.length() - 1);
                    }
                }
                if (!currentOptionVal.equals("")) {
                    if (currentOption.equals("help")) {
                        outputUsage();
                    } else if (currentOption.equals("glob")) {
                        globArg = currentOptionVal;
                    } else if (currentOption.equals("search-files")) {
                        searchFilesArg = currentOptionVal;
                    } else if (currentOption.equals("search-text")) {
                        searchTextArg = currentOptionVal;
                    } else if (currentOption.equals("exclude")) {
                        excludeArg = currentOptionVal;
                        excludeArgArr = excludeArg.split("~");
                    }
                }
            }
            i++;
        }
        if (!isHelp) {
            System.out.println("globArg: " + globArg);
            System.out.println("searchFilesArg: " + searchFilesArg);
            System.out.println("searchTextArg: " + searchTextArg);
            System.out.println("excludeArg: " + excludeArg);
            for (String excludeItem: excludeArgArr) {
                System.out.println("excludeItem: " + excludeItem);
            }
        }
    }
    private static boolean startsWithHyphen(String str) {
        String pattern = "^\\-";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        return m.find();
    }
    private static boolean startsEndsWithQuotes(String str) {
        String pattern = "^[\"\']";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        boolean startsWith = m.find();
        pattern = "[\"\']$";
        r = Pattern.compile(pattern);
        m = r.matcher(str);
        boolean endsWith = m.find();
        return (startsWith == true && endsWith == true);
    }
}