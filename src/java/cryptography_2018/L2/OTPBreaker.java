package cryptography_2018.L2;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Adam on 2018-03-11.
 */

//        You have captured l ciphertexts c1 ,..., cl. You know that each message
//        mi was encrypted using a stream cipher but every time the same key k and the same initial
//        vector IV was used, so ci = Enc(k, IV, mi) = mi  G(k, IV ).
//        Implement a program which on input:
//         l – number of ciphertexts,
//         c1 ,..., cl – ciphertexts,
//        returns ml – the last plaintext.
//        To obtain data for the task, enter your student’s number on the course webpage.
public class OTPBreaker {

    static ArrayList<String> ciphertexts = new ArrayList<>();

    public static void main(String[] args) {
        ciphertexts.add(0, "cdcdcd");
        ciphertexts.add(1, "ababab");
        retrieveLastMessage(ciphertexts);
    }

    public static void retrieveLastMessage(ArrayList<String> ciphertexts) {
        String mixedPlaintext = "";
        if (ciphertexts.size() > 1) {
            mixedPlaintext = encode(ciphertexts.get(0), ciphertexts.get(1));
            System.out.println(mixedPlaintext);
//            try {
//                encrypt(ciphertexts.get(0), mixedPlaintext, ciphertexts.get(1));
//                System.out.println(mixedPlaintext);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    private static String XORCiphertexts(String ciphertext1, String ciphertext2) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ciphertext1.length(); i++) {
            sb.append(ciphertext1.charAt(i) ^ ciphertext2.charAt(i));
        }

        return sb.toString();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////

    public static String encode(String s, String key) {
        return new String(xorWithKey(s.getBytes(), key.getBytes()));
    }

    public static String decode(String s, String key) {
        return new String(xorWithKey(s.getBytes(), key.getBytes()));
    }

    private static byte[] xorWithKey(byte[] a, byte[] key) {
        byte[] out = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = (byte) (a[i] ^ key[i%key.length]);
        }
        return out;
    }

    private static byte[] base64Decode(String s) {
        try {
            BASE64Decoder d = new BASE64Decoder();
            return d.decodeBuffer(s);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static String base64Encode(byte[] bytes) {
        BASE64Encoder enc = new BASE64Encoder();
        return enc.encode(bytes).replaceAll("\\s", "");

    }

    ////////////////////////////////////////////////////////////////////////////////////////

    public static void encrypt(String input, String output, String key) throws IOException {


//        InputStream in = new FileInputStream(input);
//        OutputStream out = new FileOutputStream(output);

        byte[] byteinput = input.getBytes();
        byte[] bytekey = key.getBytes();

        //index of incoming data, should not equal -1
        int index;

        //Key-index
        int keyIndex = 0;

        //process while the index of our incoming-data does not equal -1
        byte[] xored = new byte[byteinput.length];
        for (int i = 0; i < byteinput.length; i++) {

            //XOR operate '^' all incoming data
            //if the data-index is longer than the key,simply modulo operate '%'
            xored[i] = (byte) (byteinput[i] ^ bytekey[keyIndex % bytekey.length]);

            //Move one step further on the key index, modulo ?
            keyIndex++;
        }

        BASE64Encoder encoder = new BASE64Encoder();
        output = encoder.encode(xored);
    }
}
