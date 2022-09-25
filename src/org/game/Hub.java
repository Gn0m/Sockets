package org.game;

import java.io.*;
import java.net.Socket;

public class Hub {
    private final Socket socket;

    public Hub(Socket socket) {
        this.socket = socket;
    }

    public void sendMessage(GameMode player) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(player);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GameMode getMessage() {
        GameMode player;
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            player = (GameMode) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return player;
    }

}
