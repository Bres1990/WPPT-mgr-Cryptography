package crypto_2017.lab2;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Adam on 2017-12-04.
 */
public class ex1 {

    private ArrayList<Integer> S;
    private ArrayList<Integer> K;
    private ArrayList<Integer> Output;
    private int k;

    /**
     * Implement RC4 cipher and test quality of generated random bits (TestU01, Diehard, Dieharder)
     */
    public void main(String[] args) {

        // N = 16, 64, 256
        // k = 40, 64, 128

        Output = new ArrayList<>();

        System.out.println("-------- 1 --------");
        RC4(16, 16).iterator().forEachRemaining(System.out::println);
        System.out.println("-------- 2 --------");
        RC4(64, 64).iterator().forEachRemaining(System.out::println);
        System.out.println("-------- 3 --------");
        RC4(256, 256).iterator().forEachRemaining(System.out::println);
        System.out.println("-------- 4 --------");
        drop(RC4(16, 16), 16 * 40).iterator().forEachRemaining(System.out::println);
        System.out.println("-------- 5 --------");
        drop(RC4(16, 16), 16 * 64).iterator().forEachRemaining(System.out::println);
        System.out.println("-------- 6 --------");
        drop(RC4(16, 16), 16 * 128).iterator().forEachRemaining(System.out::println);
        System.out.println("-------- 7 --------");
        drop(RC4(64, 64), 64 * 40).iterator().forEachRemaining(System.out::println);
        System.out.println("-------- 8 --------");
        drop(RC4(64, 64), 64 * 64).iterator().forEachRemaining(System.out::println);
        System.out.println("-------- 9 --------");
        drop(RC4(64, 64), 64 * 128).iterator().forEachRemaining(System.out::println);
        System.out.println("-------- 10 --------");
        drop(RC4(256, 256), 256 * 40).iterator().forEachRemaining(System.out::println);
        System.out.println("-------- 11 --------");
        drop(RC4(256, 256), 256 * 64).iterator().forEachRemaining(System.out::println);
        System.out.println("-------- 12 --------");
        drop(RC4(256, 256), 256 * 128).iterator().forEachRemaining(System.out::println);
        System.out.println("-------- 13 --------");
        RC4(16, 16 * 40).iterator().forEachRemaining(System.out::println);
        System.out.println("-------- 14 --------");
        RC4(16, 16 * 64).iterator().forEachRemaining(System.out::println);
        System.out.println("-------- 15 --------");
        RC4(16, 16 * 128).iterator().forEachRemaining(System.out::println);
        System.out.println("-------- 16 --------");
        RC4(64, 64 * 40).iterator().forEachRemaining(System.out::println);
        System.out.println("-------- 17 --------");
        RC4(64, 64 * 64).iterator().forEachRemaining(System.out::println);
        System.out.println("-------- 18 --------");
        RC4(64, 64 * 128).iterator().forEachRemaining(System.out::println);
        System.out.println("-------- 19 --------");
        RC4(256, 256 * 40).iterator().forEachRemaining(System.out::println);
        System.out.println("-------- 20 --------");
        RC4(256, 256 * 64).iterator().forEachRemaining(System.out::println);
        System.out.println("-------- 21 --------");
        RC4(256, 256 * 128).iterator().forEachRemaining(System.out::println);
    }

    private ArrayList<Integer> KSA(int N, int T, ArrayList<Integer> K) {
        for (int i = 0; i < N; i++) {
            S.add(i, i);
        }

        int j = 0;

        for (int i = 0; i < T; i++) {
            j = (j + S.get(i % N) + K.get(i % K.size())) % N;
            Collections.swap(S, i % N, j % N);
        }

        return S;
    }

    private ArrayList<Integer> PRGA(int N, ArrayList<Integer> S) {
        int i = 0;
        int j = 0;

        for (int k = 0; k < S.size(); k++) {
            i = (i + 1) % N;
            j = (j + S.get(i)) % N;
            Collections.swap(S, i, j);
            Output.add(S.get((S.get(i) + S.get(j)) % N));
        }

        return Output;
    }

    private ArrayList<Integer> RC4(int N, int T) {
        K = new ArrayList<>();
        S = new ArrayList<>();

        // TODO: Key should have k length !
        K.add(1);
        K.add(7);
        K.add(1);
        K.add(7);

        S = KSA(N, T, K);
        return PRGA(N, S);
    }

    private MyArrayList drop(ArrayList<Integer> output, int droppedBytes) {
        MyArrayList myOutput = (MyArrayList) output;
        myOutput.removeRange(0, droppedBytes-1);

        return myOutput;
    }

    private class MyArrayList extends ArrayList {

        @Override
        protected void removeRange(int fromIndex, int toIndex) {
            super.removeRange(fromIndex, toIndex);
        }
    }
}
