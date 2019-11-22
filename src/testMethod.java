public class testMethod {

    public static void main(String[] args) throws Exception {
        Table test1 = new Table("1");
        test1.column = 3;
        test1.createTitle("A,B,C,annotation");
        test1.addNewRow("a,b,c,Q");
        test1.addNewRow("d,b,e,W");
        test1.addNewRow("f,g,e,E");

        Table test2 = new Table("2");
        test2.column = 3;
        test2.createTitle("A,B,C,annotation");
        test2.addNewRow("a,b,c,R");
        test2.addNewRow("b,c,f,T");
        test2.addNewRow("f,g,e,Y");

        ProvenanceSemiring provenanceSemiring = new ProvenanceSemiring();
        provenanceSemiring.unionForAll(test1,test2,"4");


    }








}
