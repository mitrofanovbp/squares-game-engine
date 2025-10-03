package com.ivk.squares.service.api.dto;

import com.ivk.squares.engine.Board;
import com.ivk.squares.engine.Color;
import com.ivk.squares.engine.Move;

public class BoardDto {
    public int size;
    public String data;
    public String nextPlayerColor; // "w" or "b"

    public Board toBoard() {
        if (size < 3) throw new IllegalArgumentException("size must be > 2");
        if (data == null || data.length() != size * size)
            throw new IllegalArgumentException("data length must be size*size");
        Board b = new Board(size);
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                char c = data.charAt(y * size + x);
                if (c == 'w' || c == 'W') b.place(new Move(x, y, Color.W));
                else if (c == 'b' || c == 'B') b.place(new Move(x, y, Color.B));
                else if (c == ' ') { /* empty */ } else
                    throw new IllegalArgumentException("invalid board char: '" + c + "'");
            }
        }
        return b;
    }

    public Color nextColor() {
        if (nextPlayerColor == null) throw new IllegalArgumentException("nextPlayerColor required");
        return Color.fromChar(nextPlayerColor.charAt(0));
    }
}
