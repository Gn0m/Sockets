package org.game;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Connection {

    private final String ipStr;
    private final int port;
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private final Logger logger;

    public Server(String ipStr, int port) {
        this.ipStr = ipStr;
        this.port = port;
        logger = Logger.getLogger(Server.class.getName());
    }


    @Override
    public Socket connection() {
        String status;
            try {
                serverSocket = new ServerSocket(port, 50, InetAddress.getByName(ipStr));
                logger.log(Level.INFO, "The server is running and waiting for a connection");
                status = serverSocket.getInetAddress() + " " + serverSocket.getLocalPort();
                logger.log(Level.INFO, status);
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return socket;
    }

    @Override
    public void closeConnection() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean isClose() {
        return serverSocket.isClosed();
    }

}
