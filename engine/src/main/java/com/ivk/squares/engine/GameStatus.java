package com.ivk.squares.engine;

public enum GameStatus {
    IN_PROGRESS, WIN_W, WIN_B, DRAW;

    public static GameStatus winOf(Color c) {
        return c == Color.W ? WIN_W : WIN_B;
    }
}
