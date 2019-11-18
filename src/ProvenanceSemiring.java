import java.util.ArrayList;

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

    public void union(){

    }

    public void select(String conditions,Table table){

    }
}
