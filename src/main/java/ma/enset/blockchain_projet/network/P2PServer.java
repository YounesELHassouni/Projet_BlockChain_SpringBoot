package ma.enset.blockchain_projet.network;


import org.apache.logging.log4j.message.Message;
import org.java_websocket.server.WebSocketServer;
//import org.java_websocket.server.WebSocketServerFactory;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.framing.Framedata;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class P2PServer extends WebSocketServer {

    private Set<WebSocket> connections = new HashSet<>();
    private List<Node> nodes;


    public P2PServer(int port) {
        super(new InetSocketAddress(port));
        this.nodes = new ArrayList<>();
    }

    // Ajoute un nœud au réseau
    public void addNode(Node node) {
        nodes.add(node);
    }
    // Method for broadcasting a message to all connected nodes
    public void broadcast(Message message) {
        for (Node node : nodes) {
            node.sendMessage(message);
        }
    }

    // Method for handling received messages
    public void handleMessage(Message message) {
        // Process the received message (e.g., propagate to the blockchain)
    }
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        connections.add(conn);
        System.out.println("New connection: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        connections.remove(conn);
        System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Message from " + conn.getRemoteSocketAddress() + ": " + message);
        broadcast(message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            connections.remove(conn);
        }
    }

    @Override
    public void onStart() {
        System.out.println("P2P server started successfully");
    }
}

