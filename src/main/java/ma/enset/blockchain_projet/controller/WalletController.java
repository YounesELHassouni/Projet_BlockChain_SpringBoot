package ma.enset.blockchain_projet.controller;


import lombok.RequiredArgsConstructor;
import ma.enset.blockchain_projet.entities.Transaction;
import ma.enset.blockchain_projet.entities.Wallet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {
    public static final Map<String, Wallet> wallets = new HashMap<>();
    @GetMapping("/all")
    public List<String> getAllWallets() {
        List<String> walletList = List.copyOf(wallets.keySet());
        return walletList;
    }
    @GetMapping("/{address}")
    public ResponseEntity<String> getWallet(@PathVariable String address) {
        Wallet wallet = wallets.get(address);
        System.out.println("wallets size: " + wallets.size());
        System.out.println("address: " + address);
        System.out.println("wallet balance :"+ wallet.getBalance());
        if (wallet != null) {
            System.out.println("Address: " + wallet.getAddress() + " Balance: " + wallet.getBalance());
            return ResponseEntity.ok("Address: " + wallet.getAddress() + " Balance: " + wallet.getBalance() );
        } else {
            System.out.println("Wallet not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wallet not found");
        }
    }
    @PostMapping("/create")
    public ResponseEntity<String> createWallet(@RequestParam double balance) {
        try {
            Wallet newWallet = new Wallet(balance);
            wallets.put(newWallet.getAddress(), newWallet);
            System.out.println("Wallet created. Address: " + newWallet.getAddress());
            System.out.println("wallets size: " + wallets.size());
            return ResponseEntity.ok("Wallet created. Address: " + newWallet.getAddress());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to create wallet.");
        }
    }
    @PostMapping("/transaction")
    public ResponseEntity<?> createTransaction(@RequestParam String sender, @RequestParam String recipient, @RequestParam double amount, @RequestParam String signature) {
        Wallet senderWallet = wallets.get(sender);
        Wallet recipientWallet = wallets.get(recipient);

        if (senderWallet == null || recipientWallet == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sender or recipient wallet not found");
        }

        if (senderWallet.getBalance() < amount) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient balance");
        }

        Transaction transaction = new Transaction(sender, recipient, amount, signature);
        senderWallet.addTransaction(transaction);
        //recipientWallet.addTransaction(transaction);
        return ResponseEntity.ok(transaction);
    }
    @PostMapping("/sign")
    public ResponseEntity<?> signTransaction(@RequestParam String address, @RequestParam String data) {
        Wallet wallet = wallets.get(address);
        if (wallet != null) {
            byte[] signature = wallet.signTransaction(data);
            return ResponseEntity.ok(Base64.getEncoder().encodeToString(signature));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wallet not found");
        }
    }
}

