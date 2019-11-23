
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class DataBase {
    public String filepath;
    public HashMap<String, Table> database = new HashMap<>();

    public DataBase(String filepath) {
        this.filepath = filepath;
        this.constructDataBase();
    }

    private void constructDataBase() {
        File file = new File(this.filepath);
        File[] files = file.listFiles();

        for (File databaseFile:
                files) {
            if (databaseFile.getName().endsWith(".txt")){
                String[] path = databaseFile.getName().split("/");
                String name = path[path.length - 1].replace(".txt","");
                Table table = constructTable(databaseFile, name);
                this.database.put(name, table);
            }
        }

    }

    private Table constructTable(File databaseFile, String name) {
        Table table = new Table(name);
        return readFileByLines(databaseFile, table);
    }

    public Table readFileByLines(File fileName, Table table) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                if (line == 1)
                    table.createTitle(tempString);
                else
                    table.addNewRow(tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return table;
    }

}
