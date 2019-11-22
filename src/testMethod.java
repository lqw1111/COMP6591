public class testMethod {

    public static void main(String[] args) throws Exception {
        Table test1 = new Table("1");
        test1.column = 3;
        test1.createTitle("A,B,C,annotation");
        test1.addNewRow("a,b,c,5");
        test1.addNewRow("d,b,e,3");
        test1.addNewRow("f,g,e,7");

        Table test2 = new Table("2");
        test2.column = 3;
        test2.createTitle("B,C,D,annotation");
        test2.addNewRow("b,c,d,4");
        test2.addNewRow("b,c,f,2");
        test2.addNewRow("g,e,h,3");

        ProvenanceSemiring provenanceSemiring = new ProvenanceSemiring();
        provenanceSemiring.joinForAll(test1,test2,"1");


    }








}
