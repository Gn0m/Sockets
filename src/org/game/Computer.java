package org.game;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class Computer extends GameMode {

    public Computer() {
    }

    public int getInputValue() {
        Random r = null;
        try {
            r = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return r.nextInt(2);
    }

    @Override
    int draw() {
        return 0;
    }

}
