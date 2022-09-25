package org.game;

import java.util.Scanner;

public class Human extends GameMode {

    private static final long serialVersionUID = 3869331180412146056L;

    public void getInputValue() {
        int value;
        if (getSc() == null) {
            setSc(new Scanner(System.in));
        }
        while (true) {
            getConsoleText().getInputHuman();
            value = inputInteger();
            if (value > -1 && value < 5) {
                setValue(value);
                break;
            }
        }
    }

    @Override
    void draw() {
        int value;
        if (getSc() == null) {
            setSc(new Scanner(System.in));
        }
        while (true) {
            getConsoleText().confirmRefuse();
            value = inputInteger();
            if (value > 0 && value < 3) {
                setValue(value);
                break;
            }
        }
    }

    private int inputInteger() {
        int num = 0;
        String strInput;

        strInput = getSc().nextLine();

        try {
            num = Integer.parseInt(strInput);
        } catch (NumberFormatException e) {
            System.out.println("Не верный выбор");
        }

        return num;
    }
}
