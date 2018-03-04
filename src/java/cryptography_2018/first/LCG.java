package cryptography_2018.first;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Adam on 2018-03-03.
 */
@AllArgsConstructor
@Getter
public class LCG {

    private long mod;
    private long multiplier;
    private long increment;
    private long seed;

    public long nextNumber() {
        long old_seed = seed;
        System.out.println("mod: " + mod + " multiplier: " + multiplier + " increment: " + increment + " seed: " + seed);
        seed = (multiplier * seed + increment) % mod;
//        if (old_seed < 0) {
//            old_seed += mod;
//        }

        return (multiplier * old_seed + increment) % mod;
    }

}
