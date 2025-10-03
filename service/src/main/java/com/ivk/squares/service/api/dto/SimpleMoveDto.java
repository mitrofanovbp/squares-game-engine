package com.ivk.squares.service.api.dto;

public class SimpleMoveDto {
    public int x;
    public int y;
    public String color; // "w"|"b"

    public SimpleMoveDto() {
    }

    public SimpleMoveDto(int x, int y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
}
