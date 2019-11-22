import java.util.ArrayList;
import java.util.HashMap;

public class ProvenanceSemiring {

    public int type;
    public String query;
    public DataBase dataBase;

    public ProvenanceSemiring(){

    }

    public ProvenanceSemiring(int type, String query, DataBase dataBase) {
        this.type = type;
        this.query = query;
        this.dataBase = dataBase;
    }

    public void calculate(){

    }

    public Table join(Table tableA,Table tableB){
        //todo:need to deal with annotation

        Table joinTable = new Table("joinTable");
        joinTable.title.addAll(tableA.title);

        HashMap<Integer, Integer> sameColumnLocationFromAToB = new HashMap<Integer, Integer>();
        for (String titleInA:
             tableA.title) {
            for (String titleInB:
                 tableB.title) {
                if(titleInA.equals(titleInB)){
                    sameColumnLocationFromAToB.put(tableA.title.indexOf(titleInA),tableB.title.indexOf(titleInB));
                }
            }
        }

        ArrayList<Integer> locationColumnInBNotInA = new ArrayList<>();
        for (int i = 0; i < tableB.title.size(); i++) {
            if(!tableA.title.contains(tableB.title.get(i))){
                locationColumnInBNotInA.add(i);
                joinTable.title.add(tableB.title.get(i));
            }
        }
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
                    newLine.addAll(lineInA);
                    for (int location:
                         locationColumnInBNotInA) {
                        newLine.add(lineInB.get(location));
                    }
                    joinTable.content.add(newLine);
                }

            }
        }

        return joinTable;
    }

    public Table project(String columns, Table table)throws Exception{
        //todo: need to deal with annotation and duplicate eliminate
        Table projectTable = new Table("projectTable");
        projectTable.createTitle(columns);

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
            newContent.add(newRow);
        }
        projectTable.content = newContent;
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
                            newAnnotation =Float.parseFloat(lineInTableA.get(tableA.title.size()-1))+
                                    Float.parseFloat(newLine.get(tableA.title.size()-1))+"";
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
                            newAnnotation = Math.max(Float.parseFloat(lineInTableA.get(tableA.title.size()-1)),Float.parseFloat(newLine.get(tableA.title.size()-1)))+"";
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
