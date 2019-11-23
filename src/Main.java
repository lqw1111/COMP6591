

public class Main {
    public static void main(String[] args) {
        DataBase dataBase = new DataBase("/Users/luoqinwei/Documents/COMP6591_Project/database");
        String query = "project<A,C>((project<A,B>(R) join project<B,C>(R)) union (project<A,C>(R) join project<B,C>(R)))";
        ProvenanceSemiring provenanceSemiring = new ProvenanceSemiring(4, query, dataBase);
        try {
            Table res = provenanceSemiring.calculate(query);
            TablePrinter.print(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
