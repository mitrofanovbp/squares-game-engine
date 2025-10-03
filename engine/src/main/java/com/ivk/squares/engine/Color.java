package com.ivk.squares.engine;

public enum Color {
    W, B;

    public Color other() {
        return this == W ? B : W;
    }

    public static Color fromChar(char c) {
        if (c == 'w' || c == 'W') return W;
        if (c == 'b' || c == 'B') return B;
        throw new IllegalArgumentException("Unknown color: " + c);
    }

    public char toLowerChar() {
        return this == W ? 'w' : 'b';
    }
}
