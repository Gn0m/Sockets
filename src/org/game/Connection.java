package org.game;


import java.net.Socket;

public interface Connection {
     Socket connection();

     void closeConnection();

     boolean isClose();
}
