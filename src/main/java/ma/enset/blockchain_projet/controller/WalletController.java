package ma.enset.blockchain_projet.controller;


import ma.enset.blockchain_projet.entities.Transaction;
import ma.enset.blockchain_projet.wallet.Wallet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private Wallet wallet;

    public WalletController() {
        this.wallet = new Wallet();
    }

    @GetMapping("/address")
    public String getAddress() {
        return wallet.getAddress();
    }

    @PostMapping("/create")
    public ResponseEntity<String> createWallet() {
        try {
            wallet = new Wallet();
            return ResponseEntity.ok("Wallet created. Address: " + wallet.getAddress());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to create wallet.");
        }
    }


    @PostMapping("/sign")
    public ResponseEntity<?> signTransaction(String data) {
        try {
            byte[] signature = wallet.signTransaction(data);
            return ResponseEntity.ok(signature);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to sign transaction: " + e.getMessage());
        }
    }



}

