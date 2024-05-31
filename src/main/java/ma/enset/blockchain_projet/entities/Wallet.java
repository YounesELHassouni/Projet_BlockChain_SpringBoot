package ma.enset.blockchain_projet.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.enset.blockchain_projet.controller.WalletController;

import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Getter @Setter
public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private double balance = 0.0;
    private String address;
    private List<Transaction> transactions;
    public Wallet(double balance ) {
        generateKeyPair();
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }
    private void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            keyGen.initialize(256, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
            this.address = getAddress();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public String getAddress() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
    public byte[] signTransaction(String data) {
        try {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(privateKey);
            signature.update(data.getBytes());
            return signature.sign();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static boolean verifySignature(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature sig = Signature.getInstance("SHA256withECDSA");
            sig.initVerify(publicKey);
            sig.update(data.getBytes());
            return sig.verify(signature);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        WalletController walletController = new WalletController();
        if(walletController.wallets.get(transaction.getSender()).balance>transaction.getAmount()){
            walletController.wallets.get(transaction.getRecipient()).balance += transaction.getAmount();
            walletController.wallets.get(transaction.getSender()).balance -= transaction.getAmount();


        }
    }

}

