package embedded_2017.lab1;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.util.encoders.Hex;

import java.security.*;

/**
 * Created by Adam Poterałowicz
 */
public class ex2 {

    /**
     * Write a program that creates a key pair for the curve
     * brainpoolP256r1 and prints the public key. Your program
     * should set the parameters explicitly.
     * What can you tell about the length and encoding
     * of the public key?
     */

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());

        System.out.println(new String(Hex.encode(generate().getPublic().getEncoded())));
        System.out.println(generate().getPublic().getAlgorithm());
    }

    private static KeyPair generate() {
        try {
            ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("brainpoolP256r1");
            KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", "BC");
            g.initialize(ecSpec, new SecureRandom());

            return g.generateKeyPair();
        } catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
