package org.game;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    private final Scanner sc;
    private final ConsoleText consoleText;
    private final Logger logger;
    private int gameNumber = 1;
    private boolean isServer = false;
    private Socket socket;
    private Connection connection;

    public App() {
        sc = new Scanner(System.in);
        consoleText = new ConsoleText();
        logger = Logger.getLogger(App.class.getName());
        socket = null;
        connection = null;
    }

    public void start() {
        boolean active = true;
        while (active) {

            startGame();
        }

    }

    private void startGame() {
        int type = getGameType();
        Hub hub;
        Game game;
        int hostType;


        if (type == 1) {

            hostType = getHostType();

            closeConnection();

            socket = getConnection(hostType);

            if (socket != null) {
                hub = new Hub(socket);
                game = new Game(gameNumber, isServer, hub,type);
                game.start();
            }

        } else {
            game = new Game(type, gameNumber);
            game.start();
        }
        gameNumber++;
    }

    private int getHostType() {
        int hostType;
        while (true) {
            consoleText.appChoiceConnectType();

            hostType = getInputInteger();

            if (hostType < 1 || hostType > 2) {
                logger.log(Level.INFO, "Не верный выбор");
            } else {
                break;
            }
        }
        return hostType;
    }

    private int getGameType() {
        int choice;
        while (true) {
            consoleText.appChoiceGameType();
            choice = getInputInteger();
            if (choice <= 0 || choice > 3) {
                logger.log(Level.INFO, "Не верный выбор");
            } else {
                break;
            }
        }
        return choice;
    }

    private Socket getConnection(int hostType) {

        if (hostType < 1 || hostType > 2) {
            logger.log(Level.INFO, "Не верный выбор");
        }

        if (hostType == 1) {

            connection = new Server("127.0.0.1", 1024);
            socket = connection.connection();
            isServer = true;
        }

        if (hostType == 2) {

            createClient();

        }
        return socket;
    }

    private void createClient() {

        while (true) {
            boolean valid;
            consoleText.specifyIpAndPort();

            String strIp = sc.nextLine();

            int port = getInputInteger();

            valid = isValidIp(strIp);

            if (valid) {

                connection = new Client(strIp, port);

                socket = connection.connection();
                break;
            }
        }
    }

    private boolean isValidIp(String strIp) {
        try {
            return InetAddress.getByName(strIp)
                    .getHostAddress().equals(strIp);
        } catch (UnknownHostException ex) {
            return false;
        }
    }

    private int getInputInteger() {
        int num = 0;
        String strInput;

        strInput = sc.nextLine();

        try {
            num = Integer.parseInt(strInput);
        } catch (NumberFormatException e) {
            logger.log(Level.INFO, "Не верный выбор");
        }

        return num;
    }

    private void closeConnection() {
        if (connection != null && !connection.isClose()) {
            connection.closeConnection();
        }
    }
}

