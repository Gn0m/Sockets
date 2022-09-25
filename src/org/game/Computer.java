package org.game;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class Computer extends GameMode {

    private static final long serialVersionUID = 3812003913183305888L;

    public void getInputValue() {
        Random r;
        try {
            r = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        setValue(r.nextInt(3));
    }

    @Override
    void draw() {
        //просто наследуется
    }

}
