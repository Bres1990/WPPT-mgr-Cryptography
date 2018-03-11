package cryptography_2018.L1;

import java.math.BigInteger;

/**
 * Created by Adam on 2018-03-04.
 */

public abstract class Predictor {

    public Predictor(Generator generator, int predictions) {
        this(populate(generator, predictions));
    }

    public Predictor(int[] series) {
        analyse(series);
    }

    public abstract int[] predictNext();

    protected abstract void analyse(int[] series);

    public static void guess(Generator generator, Predictor predictor, int tries) {
        int guessed = 0;
        for (int i = 0; i < tries; i++) {
            int actual = generator.getNext();
            int[] predicted = predictor.predictNext();

            StringBuilder builder = new StringBuilder();
            builder.append("actual:").append(actual).append("\tpredicted: ");
            for (int aPredicted : predicted) {
                builder.append("|").append(aPredicted);
                if (aPredicted == actual) {
                    guessed++;
                    builder.append("\t").append("guessed");
                } else {
                    builder.append("\t").append("not guessed");
                }
            }
            System.out.println(builder);
        }
        System.out.println("guessed " + guessed + "/" + tries + " (" + ((double) guessed / tries) + ")");
    }

    protected long[] eGCD(long a, long b) {
        long a0 = a;
        long b0 = b;
        long p = 1, q = 0;
        long r = 0, s = 1;

        a = Math.abs(a);
        b = Math.abs(b);

        while (b != 0) {
            long c = a % b;
            long quot = a / b;
            a = b;
            b = c;
            long new_r = p - quot * r;
            long new_s = q - quot * s;
            p = r;
            q = s;
            r = new_r;
            s = new_s;
        }

        return new long[]{
                p * Math.abs(a0) + q * Math.abs(b0),
                (long) (p * Math.signum(a0)),
                (long) (q * Math.signum(b0))
        };
    }

    protected int gcd(long a, long b) {
        BigInteger b1 = BigInteger.valueOf(a);
        BigInteger b2 = BigInteger.valueOf(b);
        BigInteger gcd = b1.gcd(b2);
        return gcd.intValue();
    }

    protected int gcd(long[] a) {
        int gcd = gcd(a[0], a[1]);
        for (int i = 2; i < a.length; i++)
            gcd(gcd, a[i]);
        return gcd;
    }

    private static int[] populate(Generator generator, int size) {
        int[] s = new int[size];
        for (int i = 0; i < size; i++)
            s[i] = generator.getNext();
        return s;
    }
}
