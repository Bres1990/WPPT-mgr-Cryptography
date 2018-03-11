package cryptography_2018.first.second;

import cryptography_2018.first.Generator;
import cryptography_2018.first.Predictor;

import java.util.ArrayList;

/**
 * Created by Adam on 2018-03-11.
 */
public class GLIBCPredictor extends Predictor {
    private ArrayList<Integer> series;

    public GLIBCPredictor(Generator generator, int predictions) {
        super(generator, predictions+344);
    }

    public GLIBCPredictor(int[] series) {
        super(series);
    }

    @Override
    protected void analyse(int[] series) {
        this.series = new ArrayList<Integer>();
        for (int i = 0; i < series.length; i++)
            this.series.add(series[i]);
    }

    @Override
    public int[] predictNext() {
        int lastIndex = series.size();
        int p1 = (int) ((series.get(lastIndex-31) + series.get(lastIndex-3)) % 2147483648L);
        int p2 = (int) ((series.get(lastIndex-31) + series.get(lastIndex-3) + 1) % 2147483648L);
        series.add(p1);
        return new int[] { p1, p2 };
    }

    public static void main(String[] arg) {
        GLIBC generator = new GLIBC();
        GLIBCPredictor breaker = new GLIBCPredictor(generator, 10);
        Predictor.guess(generator, breaker, 10);
    }
}
