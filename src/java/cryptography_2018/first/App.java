package cryptography_2018.first;

/**
 * Created by Adam on 2018-03-04.
 */
public class App {

    public static void main(String[] args) {
        LCG lcg = new LCG((long) Math.pow(2, 32), 1664525, 1013904223, 0); // mod, multiplier, increment, seed

        long x0 = lcg.getSeed();
        long x1 = lcg.nextNumber();
        long x2 = lcg.nextNumber();
        long x3 = lcg.nextNumber();
        long x4 = lcg.nextNumber();

        System.out.println("First values of LCG: " + x1 + ", " + x2 + ", " + x3 + ", (" + x4 + ")");

        Predictor predictor = new Predictor(x0, x1, x2, x3);
        try {
            predictor.predict();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
