package crypto_2017.lab3;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

/**
 * Created by Adam on 2017-12-05.
 */
public class ex1 {

    final static JFileChooser chooser = new JFileChooser();
    private static JFrame frame = new JFrame();
    private static JButton oracle = new JButton();
    private static JButton challenge = new JButton();

    /**
     * Implement a program which encrypts / decrypts selected files on disk (AES CBC)
     */
    public static void main(String[] args) {
        frame.getContentPane().setLayout(new FlowLayout());

        oracle.setText("Oracle");
        challenge.setText("Challenge");
        frame.add(oracle);
        frame.add(challenge);
        frame.pack();
        frame.setVisible(true);

        oracle.addActionListener(e -> action());
        challenge.addActionListener(e -> action());
    }

    private static void encrypt(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

            processFile(file, Cipher.ENCRYPT_MODE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void decrypt(File file) {
        try {

            processFile(file, Cipher.DECRYPT_MODE);

            BufferedReader br = new BufferedReader(new FileReader("ciphertext"));
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processFile(File file, int mode) {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(256);
            SecretKey key = kg.generateKey();
            SecretKeySpec ks = new SecretKeySpec(key.getEncoded() ,"AES");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("keyfile"));
            oos.writeObject(ks.getEncoded());
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(mode, key, iv);

            FileInputStream inputStream = new FileInputStream(file);
            byte[] inputBytes = new byte[(int) file.length()];
            inputStream.read(inputBytes);

            System.out.println(inputBytes.length);
            System.out.println(inputBytes.length / 16);

            byte[] outputBytes = c.doFinal(inputBytes);
            FileOutputStream outputStream = new FileOutputStream("ciphertext");
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File chooseFile() {
        chooser.showOpenDialog(frame);

        try {
            // Open an input stream
            Scanner reader = new Scanner(chooser.getSelectedFile());

            return chooser.getSelectedFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void action() {
        File chosenFile = chooseFile();
        if (chosenFile != null) {
            System.out.println("Chosen file: " + chosenFile.getName());
        }
        encrypt(chosenFile);
        decrypt(chosenFile);
    }
}
