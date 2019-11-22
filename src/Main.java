

public class Main {
    public static void main(String[] args) {
        DataBase dataBase = new DataBase("/Users/luoqinwei/Documents/COMP6591_Project/database");
        ProvenanceSemiring provenanceSemiring = new ProvenanceSemiring(5, "", dataBase);
//        provenanceSemiring.calculate();
    }
}
