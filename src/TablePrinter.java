import java.util.ArrayList;
import java.util.List;

public class TablePrinter {

    public static void print(Table table){
        System.out.println("------------" + table.name + "------------");
        printTitle(table.title);
        printContent(table.content);
    }

    private static void printContent(ArrayList<ArrayList<String>> content) {

        for (ArrayList<String> row:
             content) {

            StringBuilder sb = new StringBuilder();
            for (String element :
                    row) {
                sb.append(padRight(element,15));
            }
            System.out.println(sb.toString());
        }
    }

    private static void printTitle(List<String> titles){
        StringBuilder sb = new StringBuilder();
        for (String title :
                titles) {
            sb.append(padRight(title,15));
        }
        System.out.println(sb.toString());
    }


    private static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    private static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }
}
