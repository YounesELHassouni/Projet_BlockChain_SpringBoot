package ma.enset.blockchain_projet.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class Transaction {
    private String sender; // Adresse de l'expéditeur
    private String recipient; // Adresse du destinataire
    private double amount; // Montant de la transaction
    private String signature; // Signature numérique pour authentifier la transaction

    public Transaction(String sender, String recipient, double amount, String signature) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.signature = signature;
    }
}

