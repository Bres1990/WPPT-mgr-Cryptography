package cryptography_2018.first;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Created by Adam on 2018-03-04.
 */

@RequiredArgsConstructor
public class Predictor {

    @NonNull
    private long x0, x1, x2, x3;
    private long mod;

    public void predict() throws Exception {
        System.out.println("================");
        System.out.println("PREDICTION -- x0: " + x0 + " x1: " + x1 + " x2: " + x2 + " x3: " + x3);
        long gcd = GCD(x2 - x1, x1 - x0);

        if (gcd == 0) {
           throw new Exception("GCD can't be 0");
        }
        long lcm = Math.abs((x2 - x1) * (x1 - x0)) / gcd;

        System.out.println("gcd: " + gcd + " lcm: " + lcm);
        mod = calcModulus(lcm);
        System.out.println("mod: " + mod);
        long temp1 = (x3 - x2) - (x2 - x1);
        long temp2 = (x2 - x1) - (x1 - x0);

        System.out.println("temp1: " + temp1);
        System.out.println("temp2: " + temp2);

        if (temp1 < 0) {
            temp1 += mod;
        }
        if (temp2 < 0) {
            temp2 += mod;
        }

        long a = calcA(temp1, temp2);
        long b = (x0 * a + mod) % mod;

        System.out.println("Modulus = " + mod);
        System.out.println("Multiplier = " + a);
        System.out.println("Increment = " + b);

        LCG lcg = new LCG((long)Math.pow(2, 32), a, b, x3);
        System.out.println("Next value from LCG: " + lcg.nextNumber());
    }

    private long calcA(long temp1, long temp2) {
        float k = 0;
        float result = ((float) mod * k + (float) temp1) / (float) temp2;
        while (result != (long) result) {
            k += 1;
            result = ((float) mod * k + (float) temp1) / (float) temp2;
        }
        return (long) result;
    }

    private long calcModulus(long lcm) {
        long tempMod = Math.abs(((x2 - x1) * lcm / (x1 - x0) - (x3 - x2) * lcm / (x2 - x1)));

        return tempMod;
    }

    private long GCD(long a, long b) {
        long c;
        while (b != 0)
        {
            a = b;
            c = a % b;
            b = c;
        }
        return a;
//        if (a <= 0) {
//            if (b > 0) {
//                return b;
//            } else {
//                return 0;
//            }
//        }
//        return GCD(b, a % b);
    }
}
