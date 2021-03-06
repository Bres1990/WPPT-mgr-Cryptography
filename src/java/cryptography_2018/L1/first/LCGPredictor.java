package cryptography_2018.L1.first;

import cryptography_2018.L1.Generator;
import cryptography_2018.L1.Predictor;
import org.ejml.simple.SimpleMatrix;

/**
 * Created by Adam on 2018-03-04.
 */
public class LCGPredictor extends Predictor {
    private int m;
    private int a;
    private int k;
    private long x;

    public LCGPredictor(Generator generator, int predictions) {
        super(generator, predictions + 3);
    }

    public LCGPredictor(int[] series) {
        super(series);
    }

    @Override
    protected void analyse(int[] series) {
        m = calculateM(series);
        a = calculateA(series, m)[0];
        k = calculateB(series, m, a);
        x = series[series.length - 1];
    }

    private int calculateM(int[] series) {
        long[] m = new long[series.length - 3];

        for (int i = 0; i < series.length - 3; i++) {
            int x1 = series[i];
            int x2 = series[i + 1];
            int x3 = series[i + 2];
            int x4 = series[i + 3];

            SimpleMatrix matrix = new SimpleMatrix(new double[][]{
                    {x1, x2, 1},
                    {x2, x3, 1},
                    {x3, x4, 1}
            });

            // det M = x1 * x3 - x2 * x2

            m[i] = (long) matrix.determinant();
        }

        return gcd(m);
    }

    private int[] calculateA(int[] series, int m) {
        int x1 = series[0];
        int x2 = series[1];
        int x3 = series[2];
        int A = m;
        int B = x1 - x2;
        int C = x2 - x3;

        long[] eGCD = eGCD(A, B);
        long gcd = eGCD[0];
        long q = eGCD[2];

        if (C % gcd != 0)
            throw new RuntimeException();

        q *= C / gcd;

        int[] as = concreteA(m, (int) q, A, (int) gcd);

        return as;
    }

    private int[] concreteA(int m, int q, int A, int gcd) {
        int coefficient = Math.abs(A / gcd);
        while (q > 0)
            q -= coefficient;
        while (q + coefficient < 0)
            q += coefficient;
        int[] asr = new int[Math.abs(gcd)];
        for (int i = 0; i < asr.length; i++)
            asr[i] = (q += coefficient);
        return asr;
    }

    private int calculateB(int[] series, int m, int a) {
        int x1 = series[0];
        int x2 = series[1];
        int b = (x2 - x1 * a) % m;
        return b;
    }

    public int[] predictNext() {
        x = (a * x + k) % m;
        if (x < 0)
            x += m;
        return new int[]{(int) x};
    }
}
