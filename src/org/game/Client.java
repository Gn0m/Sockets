package org.game;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements Connection {

    private final String ipStr;
    private final int port;
    private Socket socket = null;
    private Logger logger;

    public Client(String ipStr, int port) {
        this.ipStr = ipStr;
        this.port = port;
        logger = Logger.getLogger(Client.class.getName());
    }

    @Override
    public Socket connection() {
        try {
            socket = new Socket(ipStr, port);
            logger.log(Level.INFO, "Connection established");
        } catch (IOException e) {
            logger.log(Level.INFO, "Не верно указан ip адрес/порт подключения");
        }
        return socket;
    }

    @Override
    public void closeConnection() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean isClose() {
        return socket.isClosed();
    }
}
