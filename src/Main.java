

public class Main {
    public static void main(String[] args) {
        DataBase dataBase = new DataBase("/Users/luoqinwei/Documents/COMP6591_Project/database");
        String query = "project<A,C>((project<A,B>(R)joinproject<B,C>(R))union(project<A,C>(R)joinproject<B,C>(R)))";
        ProvenanceSemiring provenanceSemiring = new ProvenanceSemiring(1, query, dataBase);
        try {
            Table res = provenanceSemiring.calculate(query);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
