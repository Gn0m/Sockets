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
    private boolean game = true;
    private ConsoleText consoleText;
    private String gameName;
    private boolean active = true;
    private int gameNumber;
    private boolean isServer;
    private Socket socket;
    private int type;

    public Game(int count, boolean server, Hub hub, int type) {
        consoleText = new ConsoleText();
        gameNumber = count;
        isServer = server;
        this.hub = hub;
        this.type = type;
    }

    public Game(int type, int count) {
        this.type = type;
        if (type == 2) {
            consoleText = new ConsoleText();
            gamerOne = new Human();
            gamerOne.setName("Player");
            gamerTwo = new Computer();
            gamerTwo.setName("Computer");
            gameNumber = count;
        } else if (type == 3) {
            consoleText = new ConsoleText();
            gamerOne = new Computer();
            gamerOne.setName("Computer_1");
            gamerTwo = new Computer();
            gamerTwo.setName("Computer_2");
            gameNumber = count;
        }
    }

    @SneakyThrows
    public void start() {

        setGameName("Игра номер " + gameNumber);
        System.out.println(getGameName() + " началась!");

        if (type == 1) {
            while (game) {
                humanGame();
            }
        }

        if (type == 2) {
            while (game) {
                humanCompGame();
            }
        }

        if (type == 3) {
            while (game) {
                compGame();
            }
        }

    }

    private void compGame() throws InterruptedException {
        gamerOne.getInputValue();
        gamerTwo.getInputValue();

        winner(gamerOne, gamerTwo);

        if (game) {
            Thread.sleep(1000);
            consoleText.consoleNameValue(gamerOne.getName(), gamerOne.getValue());
            Thread.sleep(1000);
            consoleText.consoleNameValue(gamerTwo.getName(), gamerTwo.getValue());

            definition(gamerOne, gamerTwo);

        }
    }

    private boolean winner(GameMode gamerOne, GameMode gamerTwo) {
        boolean value = false;

        if ((gamerTwo != null && gamerOne != null) && gamerOne.getWinnerCount() == 3) {
            game = false;
            consoleText.winner(gamerOne, gamerTwo);
            value = true;
        }

        if ((gamerTwo != null && gamerOne != null) && gamerTwo.getWinnerCount() == 3) {
            game = false;
            consoleText.winner(gamerTwo, gamerOne);
            value = true;
        }
        return value;
    }

    private void humanCompGame() throws InterruptedException {

        winner(gamerOne, gamerTwo);

        if (game) {

            gamerOne.getInputValue();
            gamerTwo.getInputValue();

            consoleText.consoleNameValue(gamerOne.getName(), gamerOne.getValue());
            Thread.sleep(1000);
            consoleText.consoleNameValue(gamerTwo.getName(), gamerTwo.getValue());

            definition(gamerOne, gamerTwo);
        }
    }

    private void humanGame() {

        if (game) {

            if (isServer) {
                gamerOne = new Human();
                gamerOne.setName("Player_1");

                while (true) {
                    boolean breakWhile;

                    breakWhile = winner(gamerOne, gamerTwo);

                    if (breakWhile) {
                        break;
                    }

                    gamerOne.getInputValue();
                    hub.sendMessage(gamerOne);

                    breakWhile = surrendered(gamerOne);

                    if (breakWhile) {
                        break;
                    }

                    gamerTwo = hub.getMessage();

                    breakWhile = surrenderedWinner(gamerTwo);

                    if (breakWhile) {
                        break;
                    }

                    if (gamerTwo.getValue() == 3) {
                        System.out.println("Согласиться на ничью?");
                        gamerOne.draw();
                        hub.sendMessage(gamerOne);


                        if (gamerOne.getValue() == 1) {
                            System.out.println("Игра завершилась в ничью со счётом: " + gamerOne.getWinnerCount() + " | "
                                    + gamerTwo.getWinnerCount());
                            game = false;
                            break;
                        }
                    }

                    if (gamerTwo.getValue() == 1 && gamerOne.getValue() == 3) {
                        game = false;
                        System.out.println("Игра завершилась в ничью со счётом: " + gamerOne.getWinnerCount() + " | "
                                + gamerTwo.getWinnerCount());
                        break;
                    } else {
                        consoleText.consoleNameValue(gamerTwo.getName(), gamerTwo.getValue());
                        consoleText.consoleNameValue(gamerOne.getName(), gamerOne.getValue());

                        definition(gamerOne, gamerTwo);

                    }
                }
            }

            if (!isServer) {
                gamerTwo = new Human();
                gamerTwo.setName("Player_2");

                while (true) {
                    boolean breakWhile = false;

                    breakWhile = winner(gamerOne, gamerTwo);

                    if (breakWhile) {
                        break;
                    }

                    gamerOne = hub.getMessage();

                    breakWhile = surrenderedWinner(gamerOne);
                    System.out.println("11");
                    if (breakWhile) {
                        break;
                    }


                    if (gamerTwo.getValue() == 3 && gamerOne.getValue() == 1) {
                        System.out.println("Игра завершилась в ничью со счётом: " + gamerOne.getWinnerCount() + " | "
                                + gamerTwo.getWinnerCount());
                        game = false;
                        break;
                    }

                    if (gamerOne.getValue() == 3) {

                        System.out.println("Согласиться на ничью?");
                        gamerTwo.draw();
                        hub.sendMessage(gamerTwo);

                        if (gamerTwo.getValue() == 1) {
                            System.out.println("Игра завершилась в ничью со счётом: " + gamerOne.getWinnerCount() + " | "
                                    + gamerTwo.getWinnerCount());
                            game = false;
                            break;
                        }
                    }

                    gamerTwo.getInputValue();
                    hub.sendMessage(gamerTwo);

                    breakWhile = surrendered(gamerTwo);

                    if (breakWhile) {
                        break;
                    }

                    if (gamerTwo.getValue() != 3) {

                        consoleText.consoleNameValue(gamerOne.getName(), gamerOne.getValue());
                        consoleText.consoleNameValue(gamerTwo.getName(), gamerTwo.getValue());

                        definition(gamerOne, gamerTwo);

                    }
                }
            }
        }
    }

    private void definition(GameMode gamerTwo, GameMode gamerOne) {

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

    private boolean surrenderedWinner(GameMode gamerTwo) {
        boolean value = true;
        if (gamerTwo.getValue() == 4) {
            game = false;
            System.out.println("Вы победили, противник сдался.");
        } else value = false;
        return value;
    }

    private boolean surrendered(GameMode gamerOne) {
        boolean value = true;
        if (gamerOne.getValue() == 4) {
            game = false;
            System.out.println("Вы сдались.");
        } else value = false;
        return value;
    }

}
