package crypto_2017.lab1;

import java.util.stream.IntStream;

/**
 * Created by Adam on 2017-11-30.
 */
public class ex1 {

    final static int mask = (1 << 31) - 1;
    static int counter = 0;

    /**
     * Construct and implement an efficient statistical test that predicts (with
     * non-negligible probability) next bits of linear congruencial generator.
     */

    /**
     * Spectral test is a statistical test for the quality of LCG.
     * LCGs have a property that when plotted in 2 or more dimensions,
     * lines or hyperplanes will form, on which all possible outputs can be found.
     * <p>
     * Spectral test compares the distance between these planes.
     * The further apart they are, the worse the generator is.
     */
    public static void main(String args[]) {
        linearCongruentialGenerator(1000);
    }

    private static void linearCongruentialGenerator(int iterations) {
            System.out.println("BSD:");
            randBSD(0).limit(iterations).forEach(System.out::println);
            System.out.println("---------------------");
    }

    static IntStream randBSD(int seed) {
        return IntStream.iterate(seed, s -> (s * 1_103_515_245 + 12345) & mask).skip(1);
    }
}
