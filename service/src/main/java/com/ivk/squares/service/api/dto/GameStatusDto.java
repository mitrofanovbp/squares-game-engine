package com.ivk.squares.service.api.dto;

public class GameStatusDto {
    public int status;     // 0=in progress, 1=win, 2=draw
    public String color;   // for 0: next to move; for 1: winner; for 2: ""
    public String message; // "Ok" | "Win" | "Draw"

    public static GameStatusDto ok(String next) {
        GameStatusDto d = new GameStatusDto();
        d.status = 0;
        d.color = next;
        d.message = "Ok";
        return d;
    }

    public static GameStatusDto win(String winner) {
        GameStatusDto d = new GameStatusDto();
        d.status = 1;
        d.color = winner;
        d.message = "Win";
        return d;
    }

    public static GameStatusDto draw() {
        GameStatusDto d = new GameStatusDto();
        d.status = 2;
        d.color = "";
        d.message = "Draw";
        return d;
    }
}
