package ma.enset.blockchain_projet.entities;

import lombok.Getter;
import lombok.Setter;
import ma.enset.blockchain_projet.helper.HashUtil;

import java.time.Instant;

@Getter @Setter
public class Block {
    private int index;
    private long timestamp;
    private String previousHash;
    private String currentHash;
    private String data;
    private int nonce;

    public Block(int index, String previousHash, String data) {
        this.index = index;
        this.previousHash = previousHash;
        this.data = data;
        this.timestamp = Instant.now().getEpochSecond();
        this.currentHash = calculateHash();
    }

    public String calculateHash() {
        String input = index + previousHash + Long.toString(timestamp) + data + nonce;
        return HashUtil.applySha256(input);
    }

    public void incrementNonce() {
        nonce++;
    }

}
