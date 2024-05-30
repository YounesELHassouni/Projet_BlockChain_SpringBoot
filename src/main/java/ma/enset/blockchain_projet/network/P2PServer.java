package ma.enset.blockchain_projet.network;


import org.java_websocket.server.WebSocketServer;
//import org.java_websocket.server.WebSocketServerFactory;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.framing.Framedata;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public class P2PServer extends WebSocketServer {

    private Set<WebSocket> connections = new HashSet<>();

    public P2PServer(int port) {
        super(new InetSocketAddress(port));
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

