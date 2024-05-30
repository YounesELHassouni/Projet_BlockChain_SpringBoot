package ma.enset.blockchain_projet.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.message.Message;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class Node {
    private String nodeId;
    private List<Node> connectedPeers;

    public Node(String nodeId) {
        this.nodeId = nodeId;
        this.connectedPeers = new ArrayList<>();
    }

    public String getNodeId() {
        return nodeId;
    }

    // Method to establish connections with other nodes
    public void connectToNode(Node node) {
        if (!connectedPeers.contains(node)) {
            connectedPeers.add(node);
            node.connectedPeers.add(this); // Bidirectional connection
            System.out.println(nodeId + " connected to " + node.getNodeId());
        }
    }

    // Method to send a message to a connected peer
    public void sendMessage(Message message) {
        for (Node peer : connectedPeers) {
            peer.receiveMessage(message);
        }
    }

    // Method to handle received messages
    public void receiveMessage(Message message) {
        System.out.println(nodeId + " received message: " + message.getFormattedMessage());
    }

}


