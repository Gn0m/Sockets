package org.game;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Scanner;

@Getter
@Setter
public abstract class GameMode implements Serializable {

    private static final long serialVersionUID = -1596870055924665300L;
    private String name;
    private int winnerCount;
    private ConsoleText consoleText;
    private int value;
    private transient Scanner sc = null;

    protected GameMode() {
        this.winnerCount = 0;
        consoleText = new ConsoleText();
    }

    abstract void getInputValue();

    public void incWinnerCount() {
        winnerCount++;
    }

    abstract void draw();

}
