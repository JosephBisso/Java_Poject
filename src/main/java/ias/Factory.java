package ias;

import student.MyGame;

public final class Factory {
    public static Game createGame(String name) throws GameException {
        return new MyGame(name);
    }

    public static Game loadGame(String path) throws GameException {
        return MyGame.loadGame(path);
    }
}