package ma.enset.blockchain_projet.entities;

import lombok.Getter;
import lombok.Setter;
import ma.enset.blockchain_projet.helper.HashUtil;

import java.security.MessageDigest;
import java.time.Instant;

import java.security.NoSuchAlgorithmException;

@Getter @Setter
public class Block {
    private int index;
    private long timestamp;
    private String previousHash;
    private String currentHash;
    private String data;

    public Block(int index, String previousHash, String data) {
        this.index = index;
        this.timestamp = Instant.now().toEpochMilli();
        this.previousHash = previousHash;
        this.data = data;
        this.currentHash = calculateHash();
    }

    // Method to calculate the hash of the block
    public String calculateHash() {
        String input = index + timestamp + previousHash + data;
        return applySha256(input);
    }

    // Method to generate a new block
    public static Block generateBlock(int index, String previousHash, String data) {
        return new Block(index, previousHash, data);
    }

    // Method to validate the block's integrity
    public boolean validateBlock() {
        return this.currentHash.equals(calculateHash());
    }

    // Apply SHA-256 hashing to the input string
    private String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String toString() {
        return "Block{" +
                "index=" + index +
                ", timestamp=" + timestamp +
                ", previousHash='" + previousHash + '\'' +
                ", currentHash='" + currentHash + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
