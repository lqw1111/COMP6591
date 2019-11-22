public class testMethod {

    public static void main(String[] args) throws Exception {
        Table test1 = new Table("1");
        test1.column = 3;
        test1.createTitle("A,B,C");
        test1.addNewRow("a,b,c");
        test1.addNewRow("d,b,e");
        test1.addNewRow("f,g,e");

        Table test2 = new Table("2");
        test2.column = 3;
        test2.createTitle("A,B,C");
        test2.addNewRow("b,c,d");
        test2.addNewRow("b,c,f");
        test2.addNewRow("g,e,h");

        ProvenanceSemiring provenanceSemiring = new ProvenanceSemiring();
        provenanceSemiring.union(test1,test2);


    }








}
