package ma.enset.blockchain_projet.entities;


import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> blockchain;
    private TransactionPool transactionPool;
    private int difficulty = 4;

    public Blockchain() {
        blockchain = new ArrayList<>();
        transactionPool = new TransactionPool();
        blockchain.add(generateGenesisBlock());
    }

    public Block generateGenesisBlock() {
        return new Block(0, "0", "Genesis Block");
    }

    public Block getLatestBlock() {
        return blockchain.get(blockchain.size() - 1);
    }

    public void addBlock(Block newBlock) {
        newBlock = new Block(blockchain.size(), getLatestBlock().getCurrentHash(), newBlock.getData());
        mineBlock(newBlock);
        blockchain.add(newBlock);
    }

    public boolean validateChain() {
        for (int i = 1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            Block previousBlock = blockchain.get(i - 1);

            if (!currentBlock.getCurrentHash().equals(currentBlock.calculateHash())) {
                return false;
            }

            if (!currentBlock.getPreviousHash().equals(previousBlock.getCurrentHash())) {
                return false;
            }

            if (!currentBlock.getCurrentHash().substring(0, difficulty).equals(getDifficultyString())) {
                return false;
            }
        }
        return true;
    }

    public void mineBlock(Block block) {
        String target = getDifficultyString();
        while (!block.getCurrentHash().substring(0, difficulty).equals(target)) {
            block.incrementNonce();
            block.setCurrentHash(block.calculateHash());
        }
    }

    private String getDifficultyString() {
        return new String(new char[difficulty]).replace('\0', '0');
    }

    public void addTransaction(Transaction transaction) {
        transactionPool.addTransaction(transaction);
    }

    public Block mineBlock() {
        List<Transaction> pendingTransactions = transactionPool.getPendingTransactions();
        String data = pendingTransactions.toString(); // Simplified, usually serialized in a better way
        Block newBlock = new Block(blockchain.size(), getLatestBlock().getCurrentHash(), data);
        mineBlock(newBlock);
        blockchain.add(newBlock);
        transactionPool.clear();
        return newBlock;
    }

    public List<Block> getChain() {
        return blockchain;
    }

    public TransactionPool getTransactionPool() {
        return transactionPool;
    }
}
