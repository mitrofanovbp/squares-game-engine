package com.ivk.squares.engine;

public class Rules {
    private final Board board;
    private Color next;

    public Rules(int size, Color first) {
        this.board = new Board(size);
        this.next = first;
    }

    public Board board() {
        return board;
    }

    public Color nextColor() {
        return next;
    }

    public GameStatus status() {
        if (SquareDetector.hasSquare(board.pointsOf(Color.W))) return GameStatus.WIN_W;
        if (SquareDetector.hasSquare(board.pointsOf(Color.B))) return GameStatus.WIN_B;
        if (board.isFull()) return GameStatus.DRAW;
        return GameStatus.IN_PROGRESS;
    }

    public GameStatus makeMove(int x, int y) {
        board.place(new Move(x, y, next));
        GameStatus s = status();
        if (s == GameStatus.IN_PROGRESS) next = next.other();
        return s;
    }
}
