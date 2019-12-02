import java.util.Scanner;

public class Main {



    public static void main(String[] args) {
        DataBase dataBase = new DataBase("/Users/luoqinwei/Documents/COMP6591_Project/database");

        while(true){
            Scanner s = new Scanner(System.in);
            System.out.println("Input your query: ");
            String query = s.nextLine();
            System.out.println("Input type (1.bag 2.probability 3.certainty 4.polynomial 5.normal)");
            String type = s.nextLine();
            if (query.equals("exit"))
                break;
            ProvenanceSemiring provenanceSemiring = new ProvenanceSemiring(Integer.valueOf(type), dataBase);
            try {
                Table res = provenanceSemiring.calculate(query);
                TablePrinter.print(res);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
