package org.game;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.net.Socket;


@Getter
@Setter
public class Game {

    private Hub hub = null;
    private GameMode gamerOne = null;
    private GameMode gamerTwo = null;
    private boolean compGame;
    private boolean compHuman;
    private boolean humanGame;
    private ConsoleText consoleText;
    private String gameName;
    private boolean free = true;
    private boolean active = true;
    private int gameNumber;
    private boolean isServer;
    private Socket socket;

    public Game(int count, boolean server, Hub hub) {
        consoleText = new ConsoleText();
        humanGame = true;
        gameNumber = count;
        isServer = server;
        this.hub = hub;
    }

    public Game(int gameType, int count) {
        if (gameType == 2) {
            consoleText = new ConsoleText();
            gamerOne = new Human();
            gamerOne.setName("Player");
            gamerTwo = new Computer();
            gamerTwo.setName("Computer");
            compHuman = true;
            gameNumber = count;
        } else if (gameType == 3) {
            consoleText = new ConsoleText();
            gamerOne = new Computer();
            gamerOne.setName("Computer_1");
            gamerTwo = new Computer();
            gamerTwo.setName("Computer_2");
            compGame = true;
            gameNumber = count;
        }
    }

    @SneakyThrows
    public void start() {

        setGameName("Игра номер " + gameNumber);
        System.out.println(getGameName() + " началась!");

        while (compGame) {
            compGame();
        }

        while (compHuman) {
            humanCompGame();
        }

        while (humanGame) {
            humanGame();
        }

    }

    private void compGame() throws InterruptedException {
        int a = gamerOne.getInputValue();
        int b = gamerTwo.getInputValue();

        if (gamerOne.getWinnerCount() == 3) {
            compGame = false;
            consoleText.winner(gamerOne, gamerTwo);
            setFree(true);
        }

        if (gamerTwo.getWinnerCount() == 3) {
            compGame = false;
            consoleText.winner(gamerTwo, gamerOne);
            setFree(true);
        }

        if (compGame) {
            Thread.sleep(1000);
            consoleText.consoleNameValue(gamerOne.getName(), a);
            Thread.sleep(1000);
            consoleText.consoleNameValue(gamerTwo.getName(), b);

            if ((a == 0 && b == 1) || (a == 1 && b == 2) || (a == 2 && b == 0)) {
                gamerOne.incWinnerCount();
                consoleText.playerWin(gamerOne.getName());
            }

            if ((a == 0 && b == 0) || (a == 1 && b == 1) || (a == 2 && b == 2)) {
                consoleText.draw();
            }

            if ((a == 0 && b == 2) || (a == 1 && b == 0) || (a == 2 && b == 1)) {
                gamerTwo.incWinnerCount();
                consoleText.playerWin(gamerTwo.getName());
            }
        }
    }

    private void humanCompGame() throws InterruptedException {

        if (gamerOne.getWinnerCount() == 3) {
            compHuman = false;
            consoleText.winner(gamerOne, gamerTwo);
            setFree(true);
        }

        if (gamerTwo.getWinnerCount() == 3) {
            compHuman = false;
            consoleText.winner(gamerTwo, gamerOne);
            setFree(true);
        }


        if (compHuman) {

            int a = gamerOne.getInputValue();
            int b = gamerTwo.getInputValue();

            consoleText.consoleNameValue(gamerOne.getName(), a);
            Thread.sleep(1000);
            consoleText.consoleNameValue(gamerTwo.getName(), b);

            if ((a == 0 && b == 1) || (a == 1 && b == 2) || (a == 2 && b == 0)) {
                gamerOne.incWinnerCount();
                consoleText.playerWin(gamerOne.getName());
            }

            if ((a == 0 && b == 0) || (a == 1 && b == 1) || (a == 2 && b == 2)) {
                consoleText.draw();
            }

            if ((a == 0 && b == 2) || (a == 1 && b == 0) || (a == 2 && b == 1)) {
                gamerTwo.incWinnerCount();
                consoleText.playerWin(gamerTwo.getName());
            }
        }
    }

    private void humanGame() throws InterruptedException {


        if (humanGame) {
            if (isServer) {
                gamerOne = new Human();
                gamerOne.setName("Player_1");

                while (true) {

                    //выбор победителя
                    if (gamerTwo != null && gamerTwo.getWinnerCount() == 3) {
                        humanGame = false;
                        consoleText.winner(gamerTwo, gamerOne);
                        setFree(true);
                        break;
                    }
                    //выбор победителя
                    if (gamerOne.getWinnerCount() == 3) {
                        humanGame = false;
                        consoleText.winner(gamerOne, gamerTwo);
                        setFree(true);
                        break;
                    }

                    gamerOne.getInputValue();
                    hub.sendMessage(gamerOne);

                    gamerTwo = hub.getMessage();
                    //когда предлагают согласиться
                    if (gamerTwo.getValue() == 3) {
                        System.out.println("Согласиться на ничью?");
                        gamerOne.draw();
                        hub.sendMessage(gamerOne);

                        //завершение если оба согласны
                        if (gamerOne.getValue() == 1) {
                            System.out.println("Игра завершилась в ничью со счётом: " + gamerOne.getWinnerCount() + " | "
                                    + gamerTwo.getWinnerCount());
                            humanGame = false;
                            break;
                        }
                    }

                    if (gamerTwo.getValue() == 1 && gamerOne.getValue() == 3) {
                        humanGame = false;
                        System.out.println("Игра завершилась в ничью со счётом: " + gamerOne.getWinnerCount() + " | "
                                + gamerTwo.getWinnerCount());
                        break;
                    } else { //выводим значения и проверяем варианты
                        consoleText.consoleNameValue(gamerTwo.getName(), gamerTwo.getValue());
                        Thread.sleep(1000);
                        consoleText.consoleNameValue(gamerOne.getName(), gamerOne.getValue());

                        if ((gamerTwo.getValue() == 0 && gamerOne.getValue() == 1)
                                || (gamerTwo.getValue() == 1 && gamerOne.getValue() == 2)
                                || (gamerTwo.getValue() == 2 && gamerOne.getValue() == 0)) {
                            gamerTwo.incWinnerCount();
                            consoleText.playerWin(gamerTwo.getName());
                        }

                        if ((gamerTwo.getValue() == 0 && gamerOne.getValue() == 0)
                                || (gamerTwo.getValue() == 1 && gamerOne.getValue() == 1)
                                || (gamerTwo.getValue() == 2 && gamerOne.getValue() == 2)) {
                            consoleText.draw();
                        }

                        if ((gamerTwo.getValue() == 0 && gamerOne.getValue() == 2)
                                || (gamerTwo.getValue() == 1 && gamerOne.getValue() == 0)
                                || (gamerTwo.getValue() == 2 && gamerOne.getValue() == 1)) {
                            gamerOne.incWinnerCount();
                            consoleText.playerWin(gamerOne.getName());
                        }
                    }
                }
            }

            if (!isServer) {
                gamerTwo = new Human();
                gamerTwo.setName("Player_2");

                while (true) {
                    //выбор победителя
                    if (gamerTwo.getWinnerCount() == 3) {
                        humanGame = false;
                        consoleText.winner(gamerTwo, gamerOne);
                        setFree(true);
                        break;
                    }
                    //выбор победителя
                    if (gamerOne != null && gamerOne.getWinnerCount() == 3) {
                        humanGame = false;
                        consoleText.winner(gamerOne, gamerTwo);
                        setFree(true);
                        break;
                    }

                    gamerOne = hub.getMessage();
                    //завершение если оба согласны
                    if (gamerTwo.getValue() == 3 && gamerOne.getValue() == 1) {
                        System.out.println("Игра завершилась в ничью со счётом: " + gamerOne.getWinnerCount() + " | "
                                + gamerTwo.getWinnerCount());
                        humanGame = false;
                        break;
                    }

                    if (gamerOne.getValue() == 3) {

                        System.out.println("Согласиться на ничью?");
                        gamerTwo.draw();
                        hub.sendMessage(gamerTwo);

                        if (gamerTwo.getValue() == 1) {
                            System.out.println("Игра завершилась в ничью со счётом: " + gamerOne.getWinnerCount() + " | "
                                    + gamerTwo.getWinnerCount());
                            humanGame = false;
                            break;
                        }
                    }

                    gamerTwo.getInputValue();
                    hub.sendMessage(gamerTwo);
                    //предложение ничьей

                    if (gamerTwo.getValue() != 3) {

                        consoleText.consoleNameValue(gamerOne.getName(), gamerOne.getValue());
                        consoleText.consoleNameValue(gamerTwo.getName(), gamerTwo.getValue());

                        if ((gamerOne.getValue() == 0 && gamerTwo.getValue() == 1)
                                || (gamerOne.getValue() == 1 && gamerTwo.getValue() == 2)
                                || (gamerOne.getValue() == 2 && gamerTwo.getValue() == 0)) {
                            gamerOne.incWinnerCount();
                            consoleText.playerWin(gamerOne.getName());
                        }

                        if ((gamerOne.getValue() == 0 && gamerTwo.getValue() == 0)
                                || (gamerOne.getValue() == 1 && gamerTwo.getValue() == 1)
                                || (gamerOne.getValue() == 2 && gamerTwo.getValue() == 2)) {
                            consoleText.draw();
                        }

                        if ((gamerOne.getValue() == 0 && gamerTwo.getValue() == 2)
                                || (gamerOne.getValue() == 1 && gamerTwo.getValue() == 0)
                                || (gamerOne.getValue() == 2 && gamerTwo.getValue() == 1)) {
                            gamerOne.incWinnerCount();
                            consoleText.playerWin(gamerOne.getName());
                        }
                    }
                }
            }
        }
    }
}
