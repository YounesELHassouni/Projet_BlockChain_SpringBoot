package ma.enset.blockchain_projet.entities;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter @Setter
public class TransactionPool {
    private List<Transaction> pendingTransactions;

    public TransactionPool() {
        pendingTransactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        pendingTransactions.add(transaction);
    }

    public List<Transaction> getPendingTransactions() {
        return pendingTransactions;
    }

    public void removeTransaction(Transaction transaction) {
        pendingTransactions.remove(transaction);
    }

    public void clear() {
        pendingTransactions.clear();
    }


}

