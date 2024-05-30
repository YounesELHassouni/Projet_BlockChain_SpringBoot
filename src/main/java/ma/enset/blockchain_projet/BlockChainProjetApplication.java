package ma.enset.blockchain_projet;

import jakarta.annotation.PostConstruct;
import ma.enset.blockchain_projet.entities.Blockchain;
import ma.enset.blockchain_projet.network.P2PServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BlockChainProjetApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlockChainProjetApplication.class, args);
	}

	@Bean
	public Blockchain blockchain() {
		return new Blockchain();
	}

	@PostConstruct
	public void startP2PServer() {
		P2PServer p2pServer = new P2PServer(7000);
		p2pServer.start();
	}
}
