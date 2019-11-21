import java.util.ArrayList;
import java.util.HashMap;

public class ProvenanceSemiring {

    public int type;
    public String query;
    public DataBase dataBase;

    public ProvenanceSemiring(int type, String query, DataBase dataBase) {
        this.type = type;
        this.query = query;
        this.dataBase = dataBase;
    }

    public void calculate(){

    }

    public void join(){

    }

    public Table project(String columns, Table table)throws Exception{
        Table projectTable = new Table("projectTable");
        columns =columns+",annotation";
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

    public Table union(Table tableA, Table tableB)throws Exception{

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
        newTableOfTableAB = tableA.content;

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
            newTableOfTableAB.add(newLine);
        }

        unionTable.column = tableA.column;
        unionTable.title = tableA.title;
        unionTable.content = newTableOfTableAB;

        return unionTable;
    }

    public void select(String conditions,Table table){

    }
}
