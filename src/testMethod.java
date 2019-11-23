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
        test2.createTitle("B,C,D,annotation");
        test2.addNewRow("b,c,d,R");
        test2.addNewRow("b,c,f,T");
        test2.addNewRow("g,e,h,Y");

        ProvenanceSemiring provenanceSemiring = new ProvenanceSemiring();
        //provenanceSemiring.selectForAll("B = b,A ! a",test1);
        //provenanceSemiring.joinForAll(test1,test2,"4");



        Table test3 = new Table("3");
        test3.column = 3;
        test3.createTitle("B,C,D,annotation");
        test3.addNewRow("b,c,d,6");
        test3.addNewRow("b,c,f,4");
        test3.addNewRow("g,e,h,7");


        //provenanceSemiring.projectForAll("B,C",test3,"1");


        Table test4 = new Table("4");
        test4.column = 3;
        test4.createTitle("A,B,C,annotation");
        test4.addNewRow("a,b,c,0.5");
        test4.addNewRow("d,b,e,0.8");
        test4.addNewRow("f,g,e,0.9");

        Table test5 = new Table("5");
        test5.column = 3;
        test5.createTitle("A,B,C,annotation");
        test5.addNewRow("a,b,c,0.3");
        test5.addNewRow("b,c,f,0.4");
        test5.addNewRow("f,g,e,0.1");

        provenanceSemiring.unionForAll(test4,test5,"2");

    }








}
