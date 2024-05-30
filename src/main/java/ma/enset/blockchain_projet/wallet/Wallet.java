package ma.enset.blockchain_projet.wallet;


import lombok.Getter;
import lombok.Setter;

import java.security.*;
import java.util.Base64;

@Getter @Setter
public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public Wallet() {
        generateKeyPair();
    }

    private void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            keyGen.initialize(256, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getAddress() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public byte[] signTransaction(String data) {
        try {
            Signature dsa = Signature.getInstance("SHA256withECDSA");
            dsa.initSign(privateKey);
            dsa.update(data.getBytes());
            return dsa.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifySignature(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature dsa = Signature.getInstance("SHA256withECDSA");
            dsa.initVerify(publicKey);
            dsa.update(data.getBytes());
            return dsa.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

