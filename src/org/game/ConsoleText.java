package org.game;

import java.io.Serializable;

public class ConsoleText implements Serializable {

    private static final long serialVersionUID = 4583924641089848293L;
    private String[] strings = {"Камень", "Ножницы", "Бумага","Ничья","Сдаться"};

    public void appChoiceGameType() {
        String[] strings = {"1. Против человека", "2. Против компьютера", "3. Компьютер против компьютера"};
        System.out.println("Выберите тип игры: ");
        for (String str : strings) {
            System.out.println(str);
        }
    }

    public void appChoiceConnectType() {
        String[] strings = {"1. Стать хостом", "2. Подключиться"};
        System.out.println("Выберите тип подключения: ");
        for (String str : strings) {
            System.out.println(str);
        }
    }

    public void specifyIpAndPort() {
        System.out.println("Укажите Ip адресс и номер порта в формате строки и числа (127.0.0.1 и 1024): ");
    }

    public void getInputHuman() {
        System.out.println("Введите значение из списка: ");
        int i = 0;
        for (String s : strings) {
            System.out.println(i + " " + s);
            i++;
        }
    }

    public void consoleNameValue(String name, int i) {
        System.out.println(name + " значение " + strings[i]);
    }

    public void draw() {
        System.out.println("Ничья");
    }

    public void playerWin(String s) {
        System.out.println(s + " победил!");
    }

    public void winner(GameMode player1, GameMode player2) {
        System.out.println(player1.getName() + " победил со счётом: " + player1.getWinnerCount()
                + " | " + player2.getWinnerCount());
    }

    public void confirmRefuse(){
        System.out.println("1. Согласиться" + "\n" + "2. Отказаться");
    }
}
