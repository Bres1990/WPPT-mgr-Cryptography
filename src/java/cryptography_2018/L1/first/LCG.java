package cryptography_2018.L1.first;

import cryptography_2018.L1.Generator;
import lombok.Getter;

/**
 * Created by Adam on 2018-03-03.
 */

@Getter
public class LCG implements Generator {

    private long a;
    private long b;
    private long m;
    private long x;

    public LCG(long a, long b, long m, long seed) {
        this.a = a;
        this.b = b;
        this.m = m;
        this.x = seed;
    }

    public int getNext() {
        x = (a * x + b) % m;

        if (x < 0) {
            x += m;
        }
        return (int) x;
    }

}
