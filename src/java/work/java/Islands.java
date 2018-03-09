package work.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Adam Poterałowicz
 */
public class Islands {

    static int[][] A;

    public static void main(String[] args) {

        constructMatrix();

        int count = 0;
        int currentCount = 0;

        for (int x = 0; x < A.length; x++) {
            for (int y = 0; y < A[x].length; y++) {
                if (A[x][y] == 1) {
                    currentCount = 1;
                    count = count + cleanBlock(A, x, y, currentCount);
                }
            }
        }

        System.out.println("Count of islands: " + count);
    }

    public static int cleanBlock(int[][] A, int x_in, int y_in, int currentCount) {
        A[x_in][y_in] = 0;

        searchForCoordinates(x_in - 1, y_in, currentCount);
        searchForCoordinates(x_in + 1, y_in, currentCount);
        searchForCoordinates(x_in, y_in - 1, currentCount);
        searchForCoordinates(x_in, y_in + 1, currentCount);
        searchForCoordinates(x_in - 1, y_in - 1, currentCount);
        searchForCoordinates(x_in + 1, y_in + 1, currentCount);
        searchForCoordinates(x_in - 1, y_in + 1, currentCount);
        searchForCoordinates(x_in + 1, y_in - 1, currentCount);
        return currentCount;
    }

    public static void constructMatrix() {

        BufferedReader reader;
        FileReader f1, f2;
        int columns, rows;

        try {
            f1 = new FileReader("islands.txt");

            reader = new BufferedReader(f1);
            FileWriter writer = new FileWriter("islands2.txt");

            String line;
            int lines = 0;

            while ((line = reader.readLine()) != null) {
                writer.write(line.replaceAll("\\s", ""));
                writer.write("\n");
                lines++;
            }

            rows = lines;
            reader.close();
            writer.close();

            f2 = new FileReader("islands2.txt");

            reader = new BufferedReader(f2);
            reader.mark(1);
            line = reader.readLine();
            columns = line.length();
            A = new int[rows][columns];
            reader.reset();

            lines = 0;
            while ((line = reader.readLine()) != null) {
                for (int i = 0; i < columns; i++) {
                    A[lines][i] = Integer.parseInt(String.valueOf(line.charAt(i)));
                }
                lines++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void searchForCoordinates(int x_in, int y_in, int currentCount) {
        if (coordinate_exists(x_in, y_in, A.length, A[0].length) == 1 && A[x_in][y_in] == 1) {
            cleanBlock(A, x_in, y_in, currentCount);
            currentCount = 1;
        }
    }

    private static int coordinate_exists(int x_in, int y_in, int A_x_length, int A_y_length) {
        if (x_in >= 0 && x_in <= (A_x_length - 1) && y_in >= 0 && y_in <= (A_y_length - 1)) {
            return 1;
        } else {
            return 0;
        }
    }
}
