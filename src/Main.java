import sun.tools.jconsole.Tab;

import javax.xml.crypto.Data;

public class Main {
    public static void main(String[] args) {
        DataBase dataBase = new DataBase("");
        ProvenanceSemiring provenanceSemiring = new ProvenanceSemiring(5, "", dataBase);
        provenanceSemiring.calculate();
    }
}
