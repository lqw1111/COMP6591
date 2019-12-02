import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class ProvenanceSemiring {

    public int type;
    public DataBase dataBase;

    public ProvenanceSemiring(){

    }

    public ProvenanceSemiring(int type, DataBase dataBase) {
        this.type = type;
        this.dataBase = dataBase;
    }

    /**
     * project -> @
     * selecte -> #
     * join -> *
     * union -> +
     * @param formula
     * @return
     */
    public Table calculate(String formula) throws Exception {
        Stack<String> operation = new Stack<>();
        Stack<Table> result = new Stack<>();

        formula = formate(formula);

        for (int i = 0 ; i < formula.length() ; i ++){
            String op = formula.substring(i, i + 1);

            if (op.equals("(")){
                operation.push(op);

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
                String operator1 = operation.pop();
                String operator2 = operation.pop();
                Table res;

                if (operator1.equals("(")){
                    if (operator2.contains("@") || operator2.contains("#")){
                        Table t1 = result.pop();
                        res = executeUnaryOp(operator2, t1);
                    } else {
                        Table t1 = result.pop();
                        Table t2 = result.pop();
                        res = executeBiOp(operator2, t1, t2);
                    }
                } else {
                    if (operator1.contains("@") || operator2.contains("#")){
                        Table t1 = result.pop();
                        res = executeUnaryOp(operator1, t1);
                    } else {
                        Table t1 = result.pop();
                        Table t2 = result.pop();
                        res = executeBiOp(operator1, t1, t2);
                    }
                }
                result.push(res);

            } else if(op.equals(" ")){
                continue;
            } else {
                String tableName = getTableName(i, formula);
                i = i + tableName.length() - 1;
                Table table = dataBase.database.get(tableName);
                result.push(table);
            }

        }
        if (operation.size() != 0){
            String op = operation.pop();
            if (op.contains("@") || op.contains("#")){
                 Table table = result.pop();
                 result.push(executeUnaryOp(op, table));
            } else{
                Table table1 = result.pop();
                Table table2 = result.pop();
                result.push(executeBiOp(op, table1, table2));
            }
        }


        return result.pop();

    }

    private Table executeUnaryOp(String operator2, Table para) throws Exception {
        Table res = new Table("");
        if (operator2.contains("@")) {
            String columns = operator2.replaceAll("@","")
                    .replaceAll("<","")
                    .replaceAll(">","");
            res = projectForAll(columns, para, Integer.toString(this.type));
        } else if(operator2.contains("#")){
            String conditions = operator2.replaceAll("#","")
                    .replaceAll("<","")
                    .replaceAll(">","");
            res = selectForAll(conditions, para);
        }
        return res;
    }

    private Table executeBiOp(String operator2, Table para1, Table para2) throws Exception {
        if (operator2.contains("*")){
            return joinForAll(para1, para2,Integer.toString(this.type));
        } else if (operator2.contains("+")){
            return unionForAll(para1, para2, Integer.toString(this.type));
        } else{
            return new Table("");
        }
    }

    private String getTableName(int start, String formula) {
        int end = -1;
        for (int i = start ; i < formula.length() ; i ++){
            if (formula.charAt(i) == ')'){
                end = i;
                break;
            }
        }
        return formula.substring(start, end);
    }

    private String getProjectOrSelect(int start, String formula) {
        int end = -1;
        for (int i = start + 1 ; i < formula.length() ; i ++){
            if (formula.charAt(i) == '>' && formula.charAt(i + 1) == '('){
                end = i;
                break;
            }
        }
        return formula.substring(start, end + 1);
    }

    private String formate(String formula) {
        return formula.replaceAll("project", "@")
                .replaceAll("select", "#")
                .replaceAll("join", "*")
                .replaceAll("union", "+");
    }

        /*
    operationType：
    1 : bag
    2 : probability
    3 : certainty
    4 : polynomial
    5 : normal
     */

    public Table joinForAll(Table tableA, Table tableB, String operationType){

        Table joinTable = new Table("joinTable");
        joinTable.title.addAll(tableA.title);

        HashMap<Integer, Integer> sameColumnLocationFromAToB = new HashMap<Integer, Integer>();
        ArrayList<String> title = tableA.title;
        for (int i = 0; i < title.size()-1; i++) {
            String titleInA = title.get(i);
            ArrayList<String> strings = tableB.title;
            for (int i1 = 0; i1 < strings.size()-1; i1++) {
                String titleInB = strings.get(i1);
                if (titleInA.equals(titleInB)) {
                    sameColumnLocationFromAToB.put(tableA.title.indexOf(titleInA), tableB.title.indexOf(titleInB));
                }
            }
        }

        ArrayList<Integer> locationColumnInBNotInA = new ArrayList<>();
        joinTable.title.remove(joinTable.title.size()-1);
        for (int i = 0; i < tableB.title.size(); i++) {
            if(!tableA.title.contains(tableB.title.get(i))){
                locationColumnInBNotInA.add(i);
                joinTable.title.add(tableB.title.get(i));
            }
        }
        joinTable.title.add("annotation");
        joinTable.column = tableA.column+locationColumnInBNotInA.size();

        for (ArrayList<String> lineInA:
                tableA.content) {
            for (ArrayList<String> lineInB:
                    tableB.content) {
                boolean rightnessFlag = true;
                for (Integer columnLocationInA:
                        sameColumnLocationFromAToB.keySet()) {
                    if(!lineInA.get(columnLocationInA).equals(lineInB.get(sameColumnLocationFromAToB.get(columnLocationInA)))){
                        rightnessFlag = false;
                        break;
                    }
                }
                if(rightnessFlag){
                    ArrayList<String> newLine = new ArrayList<>();
                    for (int i = 0; i < lineInA.size()-1; i++) {
                        newLine.add(lineInA.get(i));
                    }
                    for (int location:
                            locationColumnInBNotInA) {
                        newLine.add(lineInB.get(location));
                    }
                    String newAnnotation = "";
                    switch (operationType){
                        case "1":
                            newAnnotation = Integer.parseInt(lineInA.get(tableA.title.size()-1))*
                                    Integer.parseInt(lineInB.get(tableB.title.size()-1))+
                                    "";
                            break;
                        case "2":
                            newAnnotation = Float.parseFloat(lineInA.get(tableA.title.size()-1))*
                                    Float.parseFloat(lineInB.get(tableB.title.size()-1))+
                                    "";
                            break;
                        case "3":
                            newAnnotation = Math.min(Float.parseFloat(lineInA.get(tableA.title.size()-1)),
                                    Float.parseFloat(lineInB.get(tableB.title.size()-1)))+
                                    "";
                            break;
                        case "4":
                            newAnnotation = lineInA.get(tableA.title.size()-1)+"*"+lineInB.get(tableB.title.size()-1);
                            break;
                        case "5":
                            newAnnotation = Math.max(Integer.parseInt(lineInA.get(tableA.title.size()-1)),
                                    Integer.parseInt(lineInB.get(tableB.title.size()-1)))+
                                    "";
                            break;
                    }
                    newLine.add(newAnnotation);
                    joinTable.content.add(newLine);
                }

            }
        }

        return joinTable;
    }

    /*
    operationType：
    1 : bag
    2 : probability
    3 : certainty
    4 : polynomial
    5 : normal
     */
    public Table projectForAll(String columns, Table table,String operationType)throws Exception{

        Table projectTable = new Table("projectTable");
        String[] columnArr = columns.split(",");
        String columnsAndannotation = columns+",annotation";
        projectTable.createTitle(columnsAndannotation);

        for (String column:
                projectTable.title) {
            if(!table.title.contains(column)){
                throw new Exception("wrong project column");
            }
        }
        ArrayList<ArrayList<String>> newContent = new ArrayList<ArrayList<String>>();
        for (ArrayList<String> row:
                table.content) {
            ArrayList<String> newRow = new ArrayList<String>();
            for (String column:
                    projectTable.title) {
                newRow.add(row.get(table.title.indexOf(column)));
            }
            boolean findNewAnnotation = false;
            for (ArrayList<String> lineInNewContent:
                 newContent) {
                boolean findSameContentRow = true;
                for (String s :
                        columnArr) {
                    if (!newRow.get(projectTable.title.indexOf(s)).equals(lineInNewContent.get(projectTable.title.indexOf(s)))) {
                        findSameContentRow = false;
                        break;
                    }
                }
                if (findSameContentRow){
                    String newAnnotation = "";
                    switch (operationType){
                        case "1":
                            newAnnotation = Integer.parseInt(lineInNewContent.get(projectTable.title.size()-1))+
                                    Integer.parseInt(newRow.get(projectTable.title.size()-1))+
                                    "";
                            break;
                        case "2":
                            newAnnotation = Float.parseFloat(lineInNewContent.get(projectTable.title.size()-1))+
                                    Float.parseFloat(newRow.get(projectTable.title.size()-1))-
                                    Float.parseFloat(lineInNewContent.get(projectTable.title.size()-1))*
                                            Float.parseFloat(newRow.get(projectTable.title.size()-1))+
                                    "";
                            break;
                        case "3":
                            newAnnotation = Math.max(Float.parseFloat(lineInNewContent.get(projectTable.title.size()-1)),
                                    Float.parseFloat(newRow.get(projectTable.title.size()-1)))+
                                    "";
                            break;
                        case "4":
                            newAnnotation = "("+
                                    lineInNewContent.get(projectTable.title.size()-1)+
                                    "+"+
                                    newRow.get(projectTable.title.size()-1)+
                                    ")";
                            break;
                        case "5":
                            newAnnotation = Math.max(Integer.parseInt(lineInNewContent.get(projectTable.title.size()-1)),
                                    Integer.parseInt(newRow.get(projectTable.title.size()-1)))+
                                    "";
                            break;
                    }
                    lineInNewContent.remove(projectTable.title.size()-1);
                    lineInNewContent.add(newAnnotation);
                    findNewAnnotation = true;
                    break;
                }
            }
            if(!findNewAnnotation){
                newContent.add(newRow);
            }

        }
        projectTable.content.addAll(newContent) ;
        return projectTable;
    }



    /*
    operationType：
    1 : bag
    2 : probability
    3 : certainty
    4 : polynomial
    5 : normal
     */

    public Table unionForAll(Table tableA,Table tableB,String operationType) throws Exception{

        if(tableA.title.size()!=tableB.title.size()){
            throw new Exception("if two table union they must have same number of column");
        }

        for (String columnA:
                tableA.title) {
            if(!tableB.title.contains(columnA)){
                throw new Exception("wrong union");
            }
        }

        Table unionTable = new Table("unionTable");
        ArrayList<ArrayList<String>> newTableOfTableAB = new ArrayList<ArrayList<String>>();
        newTableOfTableAB.addAll(tableA.content);

        HashMap<Integer,Integer> orderOfAMapToOrderOfB = new HashMap<Integer, Integer>();

        for (int i = 0; i < tableA.title.size(); i++) {
            orderOfAMapToOrderOfB.put(i,tableB.title.indexOf(tableA.title.get(i)));
        }
        for (ArrayList<String> lineInTableB:
                tableB.content) {
            ArrayList<String> newLine = new ArrayList<>();
            for (int i = 0; i < tableB.title.size(); i++) {
                newLine.add(lineInTableB.get(orderOfAMapToOrderOfB.get(i)));
            }
            boolean findNewAnnotation = false;
            for (ArrayList<String> lineInTableA:
                 tableA.content) {
                boolean findSameRowInA = true;

                for (int i = 0; i < tableA.title.size()-1; i++) {//one row each column
                    if(!lineInTableA.get(i).equals(newLine.get(i))) {
                        findSameRowInA = false;
                        break;
                    }
                }
                if (findSameRowInA){
                    // calculate annotation
                    String newAnnotation = "";
                    switch (operationType){

                        case "1":
                            newAnnotation =Integer.parseInt(lineInTableA.get(tableA.title.size()-1))+
                                    Integer.parseInt(newLine.get(tableA.title.size()-1))+"";
                            break;
                        case "2":
                            newAnnotation =Float.parseFloat(lineInTableA.get(tableA.title.size()-1))+
                                    Float.parseFloat(newLine.get(tableA.title.size()-1))-
                                    Float.parseFloat(lineInTableA.get(tableA.title.size()-1))*
                                            Float.parseFloat(newLine.get(tableA.title.size()-1))+
                            "";
                            break;
                        case "3":
                            newAnnotation = Math.max(Float.parseFloat(lineInTableA.get(tableA.title.size()-1)),Float.parseFloat(newLine.get(tableA.title.size()-1)))+"";
                            break;
                        case "4":
                            newAnnotation = "("+
                                    lineInTableA.get(tableA.title.size()-1)+
                                    "+"+
                                    newLine.get(tableA.title.size()-1)+
                                    ")";
                            break;
                        case "5":
                            newAnnotation = Math.max(Integer.parseInt(lineInTableA.get(tableA.title.size()-1)),Integer.parseInt(newLine.get(tableA.title.size()-1)))+"";
                            break;


                    }
                    newTableOfTableAB.get(tableA.content.indexOf(lineInTableA)).remove(tableA.title.size()-1);
                    newTableOfTableAB.get(tableA.content.indexOf(lineInTableA)).add(newAnnotation);

                    findNewAnnotation = true;
                    break;
                }
            }
            if (!findNewAnnotation){
                //add newline cannot find the duplicate row in tableA
                newTableOfTableAB.add(newLine);
            }
        }
        unionTable.column = tableA.column;
        unionTable.title.addAll(tableA.title);
        unionTable.content.addAll(newTableOfTableAB);
        return unionTable;
    }

    /*
    use =,!,<,>  the first is title after is the value, use "," to split each condition and use space" "to split title operator and value
    such as name = ABC,ID < 15
     */
    public Table selectForAll(String conditions,Table table){
        String[] separateConditions = conditions.split(",");

        Table selectTable = new Table("selectTable");
        selectTable.column = table.column;
        selectTable.title = table.title;

        for (ArrayList<String> eachLineInTable:
             table.content) {
            boolean satisfyCondition = true;

            for (int i = 0; i < separateConditions.length; i++) {
                String[] presentCondition = separateConditions[i].split(" ");
                String title = presentCondition[0];
                String operator = presentCondition[1];
                String value = presentCondition[2];

                int presentTitleLocation = table.title.indexOf(title);

                switch (operator){
                    case "=":
                        if(!eachLineInTable.get(presentTitleLocation).equals(value)){
                            satisfyCondition = false;
                        }
                        break;

                    case "!":
                        if(eachLineInTable.get(presentTitleLocation).equals(value)){
                            satisfyCondition = false;
                        }
                        break;

                    case ">":
                        if(Integer.parseInt(eachLineInTable.get(presentTitleLocation))<=Integer.parseInt(value)){
                            satisfyCondition = false;
                        }
                        break;

                    case "<":
                        if(Integer.parseInt(eachLineInTable.get(presentTitleLocation))>=Integer.parseInt(value)){
                            satisfyCondition = false;
                        }
                        break;
                }

                if(!satisfyCondition){
                    break;
                }
            }
            if(satisfyCondition){
                selectTable.content.add(eachLineInTable);
            }
        }
        return selectTable;
    }
}
