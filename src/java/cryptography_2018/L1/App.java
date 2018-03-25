package cryptography_2018.L1;

import cryptography_2018.L1.second.GLIBC;
import cryptography_2018.L1.second.GLIBCPredictor;

/**
 * Created by Adam on 2018-03-04.
 */
public class App {

    public static void main(String[] args) {

//        LCG generator = new LCG(123, 97, 10237, 12312);
//        for (int i = 0; i < 20; i++)
//            generator.getNext();
//        LCGPredictor breaker = new LCGPredictor(generator, 4);
//        Predictor.guess(generator, breaker, 10000);

        GLIBC generator = new GLIBC(1);
        GLIBCPredictor breaker = new GLIBCPredictor(generator, 4);
        Predictor.guess(generator, breaker, 10);
    }
}