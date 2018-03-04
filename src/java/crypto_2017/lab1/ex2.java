package crypto_2017.lab1;

import com.sun.istack.internal.Nullable;

import java.math.BigInteger;
import java.util.Stack;

/**
 * Created by Adam on 2017-12-04.
 */
public class ex2 {

    static int counter = 0;
    static Stack<BigInteger> lastNumbers;
    static BigInteger lastRandomNumber;

    /**
     * Construct and implement an efficient statistical test that predicts (with
     * non-negligible probability) next bits of glibc's random().
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
        lastNumbers = new Stack<>();
        glibc(new BigInteger("first"), 36);
    }

    @Nullable
    static BigInteger glibc(BigInteger seed, int iterations) {
        counter++;
        BigInteger a = new BigInteger("16807");
        BigInteger m = BigInteger.valueOf((int) Math.pow(2, 31));

        System.out.println(seed);
        if (counter <= iterations) {
            if (counter <= 34) {
                lastRandomNumber = glibc((a.multiply(seed)).mod(m), iterations);
            } else {
                BigInteger thirdNumber = new BigInteger(lastNumbers.elementAt(lastNumbers.size()-3).toString());
                BigInteger thirtyFirstNumber = new BigInteger(lastNumbers.elementAt(lastNumbers.size()-31).toString());
                BigInteger linearFeedbackItem = thirdNumber.add(thirtyFirstNumber);

                lastRandomNumber = glibc(linearFeedbackItem.mod(m), iterations);
            }

            lastNumbers.addElement(lastRandomNumber);
            return lastRandomNumber;
        } else {
            return null;
        }
    }
}
