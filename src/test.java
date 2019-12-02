import java.util.Stack;

public class test {

    public static void main(String[] args) {
//        String formula= "project<A,C>((project<A,B>(R)joinproject<B,C>(R))union(project<A,C>(R)joinproject<B,C>(R)))";
//        calculate(formula);
        //        String query = "project<A,C>((project<A,B>(R) join project<B,C>(R)) union (project<A,C>(R) join project<B,C>(R)))";
//        String query1 = "project<staff_id, first_name>(Sales.staffs)";

//        try {
//            Table res = provenanceSemiring.calculate(query1);
//            TablePrinter.print(res);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        System.out.println(padRight("a",20) + padRight("a",20));
        System.out.println(padRight("dddddd",20) + padRight("dddd",20));
        System.out.println(Float.parseFloat("0.5") * Float.parseFloat("0.5") + Float.parseFloat("0.5") * Float.parseFloat("0.5"));

    }

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }

    public static Table calculate(String formula){
        Stack<String> operation = new Stack<>();
        Stack<String> bracket = new Stack<>();
        Stack<Table> result = new Stack<>();

        formula = formate(formula);

        for (int i = 0 ; i < formula.length() ; i ++){
            String op = formula.substring(i, i + 1);

            if (op.equals("(")){
                bracket.push(op);

            } else if (op.equals("@")){
                String p = getProjectOrSelect(i, formula);
                i = i + p.length() - 1;
                operation.push(p);

            } else if (op.equals("#")){
                String s = getProjectOrSelect(i, formula);
                i = i + s.length() - 1;
                operation.push(s);

            } else if (op.equals("*")){
                operation.push("*");

            } else if (op.equals("+")){
                operation.push("+");

            } else if (op.equals(")")){
                String operator = operation.pop();


            } else{
                String tableName = getTableName(i, formula);
                i = i + tableName.length() - 1;
                System.out.println(tableName);
                //
            }

        }

        return new Table("a");

    }

    private static String getTableName(int start, String formula) {
        int end = -1;
        for (int i = start ; i < formula.length() ; i ++){
            if (formula.charAt(i) == ')'){
                end = i;
                break;
            }
        }
        return formula.substring(start, end);
    }

    private static String getProjectOrSelect(int start, String formula) {
        int end = -1;
        for (int i = start + 1 ; i < formula.length() ; i ++){
            if (formula.charAt(i) == '>' && formula.charAt(i + 1) == '('){
                end = i;
                break;
            }
        }
        return formula.substring(start, end + 1);
    }

    private static String formate(String formula) {
        formula = formula.replaceAll("project", "@");
        formula = formula.replaceAll("select", "#");
        formula = formula.replaceAll("join", "*");
        formula = formula.replaceAll("union", "+");
        return formula;


    }
}
