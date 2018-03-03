package embedded_2017.lab1;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

/**
 * Created by Adam Poterałowicz.
 */
public class ex1 {

    /**
     * Write a function hash(byte[] in, byte fun)
     * that generates the hash value of input in denoted by fun.
     * SHA1 (fun = 0x01),
     * SHA-256 (fun = 0x04),
     * SHA-512 (fun = 0x06).
     * Use BouncyCastle.
     */

    public static void main(String[] args) {

        try {
            Security.addProvider(new BouncyCastleProvider());
            String text = "Jestem_kwiatem_lotosu_na_tafli_jeziora";

            System.out.println(new String(Hex.encode(hash(text.getBytes("UTF-8"), (byte) 0x01))));
            System.out.println(new String(Hex.encode(hash(text.getBytes("UTF-8"), (byte) 0x04))));
            System.out.println(new String(Hex.encode(hash(text.getBytes("UTF-8"), (byte) 0x06))));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static byte[] hash(byte[] in, byte fun) {
        try {
            String algorithm = "";

            if (fun == 0x01) {
                algorithm = "SHA";
            } else if (fun == 0x04) {
                algorithm = "SHA-256";
            } else if (fun == 0x06) {
                algorithm = "SHA-512";
            }

            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(in);

            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
