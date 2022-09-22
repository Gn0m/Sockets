package org.game;

import java.io.IOException;
import java.net.Inet4Address;
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
    private boolean active = true;
    private int countGame = 1;
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
        while (active) {

            startGame();
        }

    }

    private void startGame() {
        int gameType = choiceGameType();
        int countPlayer = 1;
        Hub hub = null;
        Game game = null;
        int hostType;

        while (true) {
            consoleText.appChoiceConnectType();

            hostType = inputInteger();

            if (hostType < 1 || hostType > 2) {
                logger.log(Level.INFO, "Не верный выбор");
            } else {
                break;
            }
        }


        if (gameType == 1) {

            closeConnection();

            socket = getConnection(hostType);

            if (socket != null) {
                hub = new Hub(socket);
                game = new Game(countGame, isServer, hub);
                game.start();
            }


        } else {
            game = new Game(gameType, countGame);
            game.start();
        }
        countGame++;
    }

    private int choiceGameType() {
        int choice;
        while (true) {
            consoleText.appChoiceGameType();
            choice = inputInteger();
            if (choice <= 0 || choice > 3) {
                logger.log(Level.INFO, "Не верный выбор");
            } else {
                break;
            }
        }
        return choice;
    } //выбор типа игры

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

            while (true) {
                boolean valid;
                consoleText.specifyIpAndPort();

                String strIp = sc.nextLine();

                int port = inputInteger();

                valid = adressValidator(strIp);

                if (valid) {

                    connection = new Client(strIp, port);

                    socket = connection.connection();
                    break;
                }
            }
        }
        return socket;
    }

    private boolean adressValidator(String strIp) {
        try {
            return InetAddress.getByName(strIp)
                    .getHostAddress().equals(strIp);
        } catch (UnknownHostException ex) {
            return false;
        }
    }

    private int inputInteger() {
        int num = 0;
        String strInput;

        strInput = sc.nextLine();

        try {
            num = Integer.parseInt(strInput);
        } catch (NumberFormatException e) {
            logger.log(Level.INFO, "Не верный выбор");
        }

        return num;
    } // для обработки ошибки не int

    private void closeConnection(){
        if (connection != null && !connection.isClose()) {
            connection.closeConnection();
        }
    }
}

