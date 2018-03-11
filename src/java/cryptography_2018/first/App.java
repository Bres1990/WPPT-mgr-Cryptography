package cryptography_2018.first;

import cryptography_2018.first.first.LCG;
import cryptography_2018.first.first.LCGPredictor;

/**
 * Created by Adam on 2018-03-04.
 */
public class App {

    public static void main(String[] args) {

        LCG generator = new LCG(123, 97, 10237, 12312);
        for (int i = 0; i < 20; i++)
            generator.getNext();
        LCGPredictor breaker = new LCGPredictor(generator, 10);
        Predictor.guess(generator, breaker, 3000);
    }
}
