package ma.enset.blockchain_projet.entities;


import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> blockchain;
    private TransactionPool transactionPool;
    private int difficulty = 4;
    private int difficultyAdjustmentInterval = 10; // Number of blocks after which difficulty is adjusted
    private long targetBlockInterval = 60000; // Target time for mining a block in milliseconds (e.g., 60 seconds)


    public Blockchain(int difficulty) {
        blockchain = new ArrayList<>();
        transactionPool = new TransactionPool();
        blockchain.add(generateGenesisBlock());
        this.difficulty = difficulty;
    }

    public Block generateGenesisBlock() {
        return new Block(0, "0", "Genesis Block");
    }

    public Block getLatestBlock() {
        return blockchain.get(blockchain.size() - 1);
    }
    public Block getBlockByIndex(int index) {
        if (index >= 0 && index < this.blockchain.size()) {
            return this.blockchain.get(index);
        } else {
            return null; // or throw an exception if preferred
        }
    }
    public void addBlock(Block newBlock) {
        newBlock = newBlock.generateBlock(blockchain.size(), getLatestBlock().getCurrentHash(), newBlock.getData());
        mineBlock(newBlock,this.difficulty);
        blockchain.add(newBlock);
    }

    public boolean validateChain() {
        for (int i = 1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            Block previousBlock = blockchain.get(i - 1);

            if (!currentBlock.validateBlock() || !currentBlock.getPreviousHash().equals(previousBlock.getCurrentHash())){
                return false;
            }

            if (!currentBlock.getCurrentHash().substring(0, this.difficulty).equals(getDifficultyString(this.difficulty))) {
                return false;
            }
        }
        return true;
    }

    public void mineBlock(Block block,int difficulty) {
        String target = getDifficultyString(difficulty);
        while (!block.getCurrentHash().substring(0, difficulty).equals(target)) {
            block.setCurrentHash(block.calculateHash());
        }
    }

    private String getDifficultyString(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }

    public void addTransaction(Transaction transaction) {
        transactionPool.addTransaction(transaction);
    }

    public Block mineBlock() {
        List<Transaction> pendingTransactions = transactionPool.getPendingTransactions();
        String data = pendingTransactions.toString(); // Simplified, usually serialized in a better way
        Block newBlock = new Block(blockchain.size(), getLatestBlock().getCurrentHash(), data);
        mineBlock(newBlock,this.difficulty);
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
    // Method to adjust the mining difficulty periodically
    public void adjustDifficulty() {
        if (blockchain.size() % difficultyAdjustmentInterval == 0) {
            long startTime = blockchain.get(blockchain.size() - difficultyAdjustmentInterval).getTimestamp();
            long endTime = blockchain.get(blockchain.size() - 1).getTimestamp();
            long elapsedTime = endTime - startTime;

            // Calculate the actual block interval
            long actualBlockInterval = elapsedTime / difficultyAdjustmentInterval;

            // Adjust the difficulty based on the ratio of actual to target block interval
            if (actualBlockInterval < targetBlockInterval) {
                difficulty++;
            } else {
                difficulty--;
            }
        }
    }

    // Method to add a new block to the blockchain with difficulty adjustment
    public void addBlockWithDifficultyAdjustment(Block block) {
        blockchain.add(block);
        adjustDifficulty(); // Adjust the difficulty after adding a block
    }
}
