package ma.enset.blockchain_projet.controller;


import ma.enset.blockchain_projet.wallet.Wallet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

