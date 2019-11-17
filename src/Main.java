

public class Main {
    public static void main(String[] args) {
        DataBase dataBase = new DataBase("");
        ProvenanceSemiring provenanceSemiring = new ProvenanceSemiring(5, "", dataBase);
        provenanceSemiring.calculate();
    }
}
