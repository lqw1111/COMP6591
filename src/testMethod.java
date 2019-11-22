public class testMethod {

    public static void main(String[] args) throws Exception {
        Table test1 = new Table("1");
        test1.column = 3;
        test1.createTitle("A,B,C,annotation");
        test1.addNewRow("a,b,c,0.4");
        test1.addNewRow("d,b,e,0.6");
        test1.addNewRow("f,g,e,0.8");

        Table test2 = new Table("2");
        test2.column = 3;
        test2.createTitle("A,B,C,annotation");
        test2.addNewRow("a,b,c,0.7");
        test2.addNewRow("b,c,f,0.6");
        test2.addNewRow("f,g,e,0.5");

        ProvenanceSemiring provenanceSemiring = new ProvenanceSemiring();
        provenanceSemiring.unionForNormalAndCertainty(test1,test2);


    }








}
