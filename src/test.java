public class test {

    public static void main(String[] args) {
        String str = "/Users/luoqinwei/Documents/COMP6591_Project/database/Sales.order_times.txt";
        String[] path = str.split("/");
        String name = path[path.length - 1].replace(".txt","");
        System.out.println(name);
    }
}
