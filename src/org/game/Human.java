package org.game;

import java.io.Serializable;
import java.util.Scanner;
import java.util.logging.Level;

public class Human extends GameMode implements Serializable {
    private static final long serialVersionUID = 822615197046293447L;
    private transient Scanner sc = null;

    public Human() {
    }

    public int getInputValue() {
        int value;
        if (sc == null) {
            sc = new Scanner(System.in);
        }
        while (true){
            getConsoleText().getInputHuman();
            value = inputInteger();
            if (value > -1 && value < 5){
                setValue(value);
                break;
            }
        }
        return value;
    }

    @Override
    int draw() {
        int value;
        if (sc == null) {
            sc = new Scanner(System.in);
        }
        while (true){
            getConsoleText().confirmRefuse();
            value = inputInteger();
            if (value > 0 && value < 3){
                setValue(value);
                break;
            }
        }
        return value;
    }

    private int inputInteger() {
        int num = 0;
        String strInput;

        strInput = sc.nextLine();

        try {
            num = Integer.parseInt(strInput);
        } catch (NumberFormatException e) {
            System.out.println("Не верный выбор");
        }

        return num;
    }
}
