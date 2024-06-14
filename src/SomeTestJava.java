public class SomeTestJava {
    public static void main(String[] args) {
        int count = 0;
//        double maxN = Double.parseDouble(args[0]);
        double maxN = 1e10;
        System.out.println("==========Java code===========");
        System.out.println("MaxN= "+maxN);
        long startTime = System.nanoTime();
        for (int i = 0; i < maxN; i++) {
            count++;
        }
        long endTime = System.nanoTime();
        System.out.println("count: " + (count==maxN));
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + " seconds");
    }
}
