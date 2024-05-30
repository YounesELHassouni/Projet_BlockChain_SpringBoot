package ma.enset.blockchain_projet.controller;





import lombok.RequiredArgsConstructor;
import ma.enset.blockchain_projet.entities.Block;
import ma.enset.blockchain_projet.entities.Blockchain;
import ma.enset.blockchain_projet.entities.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BlockchainController {

    private final Blockchain blockchain;

    @GetMapping("/blockchain")
    public List<Block> getBlockchain() {
        return blockchain.getChain();
    }

    @PostMapping("/blockchain/transaction")
    public ResponseEntity<String> addTransaction(@RequestBody Transaction transaction) {
        blockchain.addTransaction(transaction);
        return ResponseEntity.ok("Transaction added successfully.");
    }

    @PostMapping("/blockchain/mine")
    public ResponseEntity<String> mineBlock() {
        Block newBlock = blockchain.mineBlock();
        return ResponseEntity.ok("Block mined successfully. Block hash: " + newBlock.getCurrentHash());
    }

    @GetMapping("/blockchain/block/{index}")
    public ResponseEntity<Block> getBlockByIndex(@PathVariable int index) {
        if (index >= 0 && index < blockchain.getChain().size()) {
            Block block = blockchain.getChain().get(index);
            return ResponseEntity.ok(block);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/blockchain/transaction-pool")
    public List<Transaction> getTransactionPool() {
        return blockchain.getTransactionPool().getPendingTransactions();
    }

    @GetMapping("/blockchain/validate")
    public ResponseEntity<String> validateChain() {
        boolean isValid = blockchain.validateChain();
        if (isValid) {
            return ResponseEntity.ok("Blockchain is valid.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Blockchain is invalid.");
        }
    }
}

